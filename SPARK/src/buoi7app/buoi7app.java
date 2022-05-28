package buoi7app;

import javacard.framework.*;
import buoi7.masterInterface;

public class buoi7app extends Applet
{
	final static byte[] serverAID = new byte[]{(byte)0x25,(byte)0x10,(byte)0x19,(byte)0x99,(byte)0x00,(byte)0x00,(byte)0x07,(byte)0x01};
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new buoi7app().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
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
		case (byte)0x00:
			AID masterAID = JCSystem.lookupAID(serverAID,(short)0,(byte)serverAID.length);
			masterInterface sio = (masterInterface)(JCSystem.getAppletShareableInterfaceObject(masterAID,(byte)0x00));
			short len = sio.getArray(buf);
			apdu.setOutgoingAndSend((short)0,len);
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
