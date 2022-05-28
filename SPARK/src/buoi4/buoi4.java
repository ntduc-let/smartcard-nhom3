package buoi4;

import javacard.framework.*;

public class buoi4 extends Applet
{
	public static byte[] OpA = new byte[8];
	public static byte[] OpB = new byte[8];
	
	public static byte lenA;
	public static byte lenB;
	
	private static final byte CLA = 0x00;
	
	private static final byte INS_INPUT = 0x10;
	private static final byte INS_OUTPUT = 0x20;
	
	private static final byte IN_A = 0x01;
	private static final byte IN_B = 0x02;
	
	
	private static final byte OUT_NAME = 0x01;
	private static final byte OUT_DATE = 0x02;
	private static final byte OUT_NAMEDATE = 0x03;
	
	public static short SW_NOT_EQUAL_LENGHT = (short) 0x6910;
	public static short SW_OVERFLOW_OCCURS = (short) 0x6911;
	public static short SW_UNDERFLOW_OCCURS = (short) 0x6912;
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new buoi4().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
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
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
