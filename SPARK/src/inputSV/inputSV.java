package inputSV;

import javacard.framework.*;

public class inputSV extends Applet
{
	
	private static final byte CLA = (byte)0x00;
	
	private static final byte INS_INPUT = (byte)0x01;
	private static final byte INS_OUTPUT = (byte)0x02;
	
	private static final byte OUT_ID = (byte)0x01;
	private static final byte OUT_NAME = (byte)0x02;
	private static final byte OUT_DATE = (byte)0x03;
	private static final byte OUT_ADDRESS = (byte)0x04;
	private static final byte OUT_ALL = (byte)0x05;
	
	public static byte[] OpData = new byte[20];
	public static byte lenData = (byte)0;
	
	public static byte[] OpID = new byte[5];
	public static byte[] OpNAME = new byte[5];
	public static byte[] OpDATE = new byte[5];
	public static byte[] OpADDRESS = new byte[5];
	
	public static byte lenID = (byte)0;
	public static byte lenNAME = (byte)0;
	public static byte lenDATE = (byte)0;
	public static byte lenADDRESS = (byte)0;
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new inputSV().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
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
			if(buf[ISO7816.OFFSET_LC]>(byte) 20){
				ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
			}
			
			Util.arrayCopyNonAtomic(buf,ISO7816.OFFSET_CDATA,OpData,(short) 0x00,buf[ISO7816.OFFSET_LC]);
			lenData = buf[ISO7816.OFFSET_LC];
				
			short flag = (short)0;
			byte[] objData = new byte[5];
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
					if(lenID == (byte)0){
						Util.arrayCopyNonAtomic(objData,(short) 0x00,OpID,(short) 0x00,(byte)(objDatai));
						lenID = (byte)(objDatai);
						objData = new byte[5];
					}
					else if(lenNAME == (byte)0){
						Util.arrayCopyNonAtomic(objData,(short) 0x00,OpNAME,(short) 0x00,(byte)(objDatai));
						lenNAME = (byte)(objDatai);
						objData = new byte[5];
					}
					else if(lenDATE == (byte)0){
						Util.arrayCopyNonAtomic(objData,(short) 0x00,OpDATE,(short) 0x00,(byte)(objDatai));
						lenDATE = (byte)(objDatai);
						objData = new byte[5];
					}
					else{
						Util.arrayCopyNonAtomic(objData,(short) 0x00,OpADDRESS,(short) 0x00,(byte)(objDatai));
						lenADDRESS = (byte)(objDatai);
					}
					objDatai = (short)0;
				};
			}
			break;
		case (byte)INS_OUTPUT:
			if(buf[ISO7816.OFFSET_P1] == OUT_ID){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenID);
				Util.arrayCopy(OpID,(short)0,buf,(short)0,lenID);
				apdu.sendBytes((short)0,lenID);
			}
			else if(buf[ISO7816.OFFSET_P1] == OUT_NAME){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenNAME);
				Util.arrayCopy(OpNAME,(short)0,buf,(short)0,lenNAME);
				apdu.sendBytes((short)0,lenNAME);
			}
			else if(buf[ISO7816.OFFSET_P1] == OUT_DATE){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenDATE);
				Util.arrayCopy(OpDATE,(short)0,buf,(short)0,lenDATE);
				apdu.sendBytes((short)0,lenDATE);
			}
			else if(buf[ISO7816.OFFSET_P1] == OUT_ADDRESS){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenADDRESS);
				Util.arrayCopy(OpADDRESS,(short)0,buf,(short)0,lenADDRESS);
				apdu.sendBytes((short)0,lenADDRESS);
			}
			else if(buf[ISO7816.OFFSET_P1] == OUT_ALL){
				apdu.setOutgoing();
				apdu.setOutgoingLength((short)(lenADDRESS+lenDATE+lenID+lenNAME));
				Util.arrayCopy(OpID,(short)0,buf,(short)0,lenID);
				Util.arrayCopy(OpNAME,(short)0,buf,(short)lenID,lenNAME);
				Util.arrayCopy(OpDATE,(short)0,buf,(short)(lenNAME+lenID),lenDATE);
				Util.arrayCopy(OpADDRESS,(short)0,buf,(short)(lenDATE+lenNAME+lenID),lenADDRESS);
				apdu.sendBytes((short)0,(short)(lenADDRESS+lenDATE+lenID+lenNAME));
			}
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
