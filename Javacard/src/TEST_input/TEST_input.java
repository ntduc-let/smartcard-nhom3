package TEST_input;

import javacard.framework.*;

public class TEST_input extends Applet
{
	public static byte[] OpA = new byte[8];
	public static byte[] OpB = new byte[8];
	public static byte[] OpData = new byte[16];
	
	public static byte lenA = (byte)0;
	public static byte lenB;
	public static byte lenData;
	
	private static final byte CLA = 0x00;
	
	private static final byte INS_INPUT = 0x10;
	private static final byte INS_OUTPUT = 0x20;
	private static final byte INS_INPUT_ALL = 0x30;
	
	private static final byte IN_A = 0x01;
	private static final byte IN_B = 0x02;
	
	private static final byte IN_DATA = 0x03;
	private static final byte IN_FLAG = 0x04;
	
	
	private static final byte OUT_NAME = 0x01;
	private static final byte OUT_DATE = 0x02;
	private static final byte OUT_NAMEDATE = 0x03;
	private static final byte OUT_ALL = 0x04;
	
	public static short SW_NOT_EQUAL_LENGHT = (short) 0x6910;
	public static short SW_OVERFLOW_OCCURS = (short) 0x6911;
	public static short SW_UNDERFLOW_OCCURS = (short) 0x6912;
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new TEST_input().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
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
		case (byte)INS_INPUT:
			if(buf[ISO7816.OFFSET_LC]>(byte) 0x08){
				ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
			}
			
			if(buf[ISO7816.OFFSET_P1] == IN_A){
				Util.arrayCopyNonAtomic(buf,ISO7816.OFFSET_CDATA,OpA,(short) 0x00,buf[ISO7816.OFFSET_LC]);
				lenA = buf[ISO7816.OFFSET_LC];
			}
			else if(buf[ISO7816.OFFSET_P1] == IN_B){
				Util.arrayCopyNonAtomic(buf,ISO7816.OFFSET_CDATA,OpB,(short) 0x00,buf[ISO7816.OFFSET_LC]);
				lenB = buf[ISO7816.OFFSET_LC];
			}
			else{
				ISOException.throwIt(ISO7816.SW_WRONG_P1P2);
			}
			break;
		case (byte) INS_OUTPUT:
			if(buf[ISO7816.OFFSET_P1] == OUT_NAME){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenA);
				Util.arrayCopy(OpA,(short)0,buf,(short)0,lenA);
				apdu.sendBytes((short)0,lenA);
			}
			else if(buf[ISO7816.OFFSET_P1] == OUT_DATE){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenB);
				Util.arrayCopy(OpB,(short)0,buf,(short)0,lenB);
				apdu.sendBytes((short)0,lenB);
			}
			else if(buf[ISO7816.OFFSET_P1] == OUT_NAMEDATE){
				apdu.setOutgoing();
				apdu.setOutgoingLength((short)(lenA+lenB));
				Util.arrayCopy(OpA,(short)0,buf,(short)0,lenA);
				Util.arrayCopy(OpB,(short)0,buf,(short)lenA,lenB);
				apdu.sendBytes((short)0,(short)(lenA+lenB));
			}
			else{
				ISOException.throwIt(ISO7816.SW_WRONG_P1P2);
			}
			break;
		case (byte)INS_INPUT_ALL:
			if(buf[ISO7816.OFFSET_LC]>(byte) 16){
				ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
			}
			
			if(buf[ISO7816.OFFSET_P1] == IN_DATA){
				Util.arrayCopyNonAtomic(buf,ISO7816.OFFSET_CDATA,OpData,(short) 0x00,buf[ISO7816.OFFSET_LC]);
				lenData = buf[ISO7816.OFFSET_LC];
				
				short flag = (short)0;
				byte[] objData = new byte[8];
				byte objDatai = 0;
				for(short i=0;i<=lenData;i++){
					if((byte)(OpData[i]) == (byte)0x02){
						flag = (short)1;
						continue;
					}
					else if((byte)(OpData[i]) == (byte)0x03){
						flag = (short)0;
					};
					
					if(flag == (short)1){
						objData[objDatai] = OpData[i];
						objDatai++;
					}
					else if(flag == (short)0 && objDatai != (byte)0){
						if(lenA == (byte)0){
							Util.arrayCopyNonAtomic(objData,(short) 0x00,OpA,(short) 0x00,(byte)(objDatai));
							lenA = (byte)(objDatai);
							objData = new byte[8];
						}
						else{
							Util.arrayCopyNonAtomic(objData,(short) 0x00,OpB,(short) 0x00,(byte)(objDatai));
							lenB = (byte)(objDatai);
						}
						objDatai = (short)0;
					};
				}
			}
			else{
				ISOException.throwIt(ISO7816.SW_WRONG_P1P2);
			}
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
