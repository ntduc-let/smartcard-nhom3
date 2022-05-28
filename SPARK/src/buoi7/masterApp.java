package buoi7;

import javacard.framework.*;

public class masterApp extends Applet implements masterInterface
{
	private byte[] testArray;
	private masterApp(){
		testArray = new byte[]{0x01,0x02,0x03,0x04,0x05};
	}
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new masterApp().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
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
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	public Shareable getShareableInterfaceObject(AID clientAID,byte parameter){
		if(parameter!=(byte)0x00){
			return null;
		}
		return this;
	}
	public short getArray(byte[] buf){
		short len = (short)testArray.length;
		Util.arrayCopy(testArray,(short)0,buf,(short)0,len);
		return len;
	}
}
