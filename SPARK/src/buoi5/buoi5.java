package buoi5;

import javacard.framework.*;

public class buoi5 extends Applet
{
	private static byte[] buffer, buffer1, buffer2;
	private byte x;
	
	final static byte CLA = (byte) 0x00;
	final static byte INS_SEND = (byte) 0x00;
	final static byte INS_UPDATE = (byte) 0x01;
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new buoi5().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
		buffer = new byte[2];
		buffer1 = JCSystem.makeTransientByteArray((short)2,JCSystem.CLEAR_ON_DESELECT);
		buffer2 = JCSystem.makeTransientByteArray((short)2,JCSystem.CLEAR_ON_RESET);
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
		case INS_SEND:
			short le = apdu.setOutgoing();
			apdu.setOutgoingLength((short)7);
			buf[0] = x;
			apdu.sendBytes((short)0,(short)1);
			apdu.sendBytesLong(buffer,(short)0,(short)2);
			apdu.sendBytesLong(buffer1,(short)0,(short)2);
			apdu.sendBytesLong(buffer2,(short)0,(short)2);
			break;
		case INS_UPDATE:
			x = 9;
			buffer[0] = 0x01;
			buffer1[0] = 0x03;
			buffer2[0] = 0x05;
			buffer[1] = 0x02;
			buffer1[1] = 0x04;
			buffer2[1] = 0x06;
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
