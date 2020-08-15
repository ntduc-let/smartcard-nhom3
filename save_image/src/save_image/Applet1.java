package save_image;

import javacard.framework.*;

public class Applet1 extends Applet
{
	final static byte INS_IMAGE = (byte)0x00;
	final static byte INS_OUT_IMAGE = (byte)0x01;
	//final static byte INS_INFO_1 = (byte)0x05;
	final static byte INS_INFO = (byte)0x03;
	final static byte INS_OUT_INFO = (byte)0x04;
	final static byte INS_RESET = (byte)0x45;
	private static byte[] image,info;
	private static byte gender;
	private static short lengImage,imageOffset,lengInfo;
	//final static short LENGTH_CARD_ID = (short)12;
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new Applet1().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
		image = new byte[32767];
		lengImage = (short)0;
		imageOffset = (short)0;
		info = new byte[200];
		lengInfo = (short)0;
		
	}
	

	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();
		apdu.setIncomingAndReceive();
		short lc = (short)(buf[ISO7816.OFFSET_LC]);
		switch (buf[ISO7816.OFFSET_INS])
		{
		case INS_IMAGE:
			Util.arrayCopy(buf,ISO7816.OFFSET_CDATA,image,lengImage,lc);
			lengImage=(short)(lengImage+lc);
			break;
		case INS_OUT_IMAGE:
			if(imageOffset<(short)(lengImage-1)){
				short le = apdu.setOutgoing();
				apdu.setOutgoingLength((short)99);
				Util.arrayCopy(image,imageOffset,buf,(short)0,(short)99);
				imageOffset =(short)(imageOffset+99);
				apdu.sendBytes((short)0,(short)99);
			}
			break;
		case (byte)0x02:
			imageOffset = (short)0;
			break;
		case INS_INFO:
			//p1->gender
			gender = (byte)(buf[ISO7816.OFFSET_P1]);
			//cdata
			Util.arrayCopy(buf,ISO7816.OFFSET_CDATA,info,(short)0,lc);
			lengInfo = lc;
			break;
		case INS_OUT_INFO:
			short le = apdu.setOutgoing();
			if(lengInfo>0){
				apdu.setOutgoingLength((short)(lengInfo+2));
				Util.arrayCopy(info,(short)0,buf,(short)0,lengInfo);
				buf[lengInfo]=(byte)0x01;
				buf[(short)(lengInfo+1)]=(byte)gender;
				apdu.sendBytes((short)0,(short)(lengInfo+2));
			}
			break;
		case INS_RESET:
			image = new byte[32767];
			lengImage = (short)0;
			imageOffset = (short)0;
			info = new byte[200];
			lengInfo = (short)0;
			break;
			
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
