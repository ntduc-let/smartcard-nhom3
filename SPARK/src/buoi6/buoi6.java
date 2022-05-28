package buoi6;

import javacard.framework.*;

public class buoi6 extends Applet
{
	final static byte INS_TRANSACTION = (byte)0x00;
	final static byte INS_SEND = (byte)0x01;
	
	private static byte[] buffer1, buffer2;
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new buoi6().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
		buffer1 = new byte[10];
		buffer2 = new byte[10];
		for(short i=0;i<10;i++){
			buffer1[i] = (byte)i;
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
		case INS_TRANSACTION:
			JCSystem.beginTransaction();
			for(short i=0;i<10;i++){
				buffer2[i] = buffer1[i];
				if(i==(short)3){
					JCSystem.abortTransaction();
					JCSystem.beginTransaction();
				}
				
			}
			JCSystem.commitTransaction();
			break;
		case INS_SEND:
			apdu.setOutgoing();
			apdu.setOutgoingLength((short)10);
			apdu.sendBytesLong(buffer2,(short)0,(short)10);
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
