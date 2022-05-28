package TEST_input;

import javacard.framework.*;

public class BTH4 extends Applet
{
	final static byte SV_ID_LENGTH = (byte) 0x04;
	
	
	private static byte [] diemThi, sinhVien,monThi,OpData;
	private static byte soLuongMonThi,lenData;
	
	private static final byte INS_INPUT = 0x01;
	private static final byte INS_OUTPUT = 0x04;
	private static final byte INS_EDIT= 0x02;
	private static final byte INS_DELETE = 0x03;
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new BTH4(bArray, bOffset, bLength);
	}
	
	private BTH4(byte[] bArray, short bOffset, byte bLength){
		byte iLen = bArray[bOffset];
		if(iLen == 0)
		{
			register();
		}
		else{
			register(bArray, (short)(bOffset +1), iLen);
		}
		bOffset = (short) (bOffset+iLen+1);
		byte cLen = bArray[bOffset];
		bOffset = (short) (bOffset+cLen+1);
		byte aLen = bArray[bOffset];
		bOffset = (short) (bOffset + 1);
		if (aLen != 0){
			sinhVien = new byte[SV_ID_LENGTH];
			Util.arrayCopy(bArray, bOffset, sinhVien, (byte)0, SV_ID_LENGTH);
			bOffset += SV_ID_LENGTH;
			soLuongMonThi = bArray[bOffset];
			monThi = new byte[soLuongMonThi];
			for(short i= (short)0;i<soLuongMonThi;i++){
				monThi[(short)i] = (byte) i;
			}
			diemThi = new byte[soLuongMonThi];
		}
		else{
			sinhVien = new byte[] {'S','V','0','1'};
			soLuongMonThi = (byte)0x09;
			monThi = new byte[] {(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07,(byte)0x08};
			diemThi = new byte[soLuongMonThi];
		}
	}

	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();
		apdu.setIncomingAndReceive();
		
		switch (buf[ISO7816.OFFSET_INS])
		{
		case INS_INPUT:
			lenData = buf[ISO7816.OFFSET_LC];
			OpData = new byte[lenData];
			Util.arrayCopyNonAtomic(buf,ISO7816.OFFSET_CDATA,OpData,(short) 0x00,buf[ISO7816.OFFSET_LC]);
			
			for(short i=(short)0;i<=lenData;i+=2){
				for(short j=(short)0;j<soLuongMonThi;i++){
					if((byte)(OpData[i]) == (byte)(monThi[j])){
						monThi[j] = OpData[(short)(i+1)];
						break;
					}
				}
			}
			break;
		case INS_EDIT:
			break;
		case INS_DELETE:
			break;
		case INS_OUTPUT:
			short dataThiLenght = (short)(soLuongMonThi * 2);
			byte[] dataThi = new byte[dataThiLenght];
			short iThi = (short)0;
			for(short i=(short)0;i<dataThiLenght;i+=2){
				dataThi[i] = monThi[iThi];
				dataThi[(short)(i+1)] = diemThi[iThi];
				iThi++;
			}
			apdu.setOutgoing();
			apdu.setOutgoingLength((short)(dataThiLenght+SV_ID_LENGTH));
			Util.arrayCopy(sinhVien,(short)0,buf,(short)0,SV_ID_LENGTH);
			Util.arrayCopy(dataThi,(short)0,buf,(short)SV_ID_LENGTH,dataThiLenght);
			apdu.sendBytes((short)0, (short)(dataThiLenght+SV_ID_LENGTH));
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
