package save_image;

import javacard.framework.*;

public class Applet1 extends Applet
{
	final static byte INS_IMAGE = (byte)0x00;
	final static byte INS_OUT_IMAGE = (byte)0x01;
	//final static byte INS_INFO_1 = (byte)0x05;
	//final static byte INS_INFO = (byte)0x03;
	//final static byte INS_OUT_INFO = (byte)0x04;
	final static byte INS_NAME = (byte)0x10;
	final static byte INS_BIRTH = (byte)0x11;
	final static byte INS_ADDRESS= (byte)0x12;
	final static byte INS_ISSUE = (byte)0x13;
	final static byte INS_EXP = (byte)0x14;
	final static byte INS_CARD_ID = (byte)0x15;
	final static byte INS_PIN = (byte)0x16;
	final static byte INS_GENDER = (byte)0x17;
	
	final static byte INS_ONAME=(byte)0x20;
	final static byte INS_OBIRTH=(byte)0x21;
	final static byte INS_OADDRESS=(byte)0x22;
	final static byte INS_OISSUE=(byte)0x23;
	final static byte INS_OEXP=(byte)0x24;
	final static byte INS_OCARD_ID=(byte)0x25;
	final static byte INS_OPIN=(byte)0x26;
	final static byte INS_OGENDER=(byte)0x27;
	final static byte INS_RESET = (byte)0x45;
	private static byte[] image,name,birth,address,issue,exp,pin,gender,id;
	private static short lengImage,imageOffset,lcName,lcBirth,lcAddress,lcIssue,lcExp,lcPin,lcGender,lcId;
	//final static short LENGTH_CARD_ID = (short)12;
	public Applet1(){
		image = new byte[32767];
		lengImage = (short)0;
		imageOffset = (short)0;
		// info = new byte[200];
		// lengInfo = (short)0;
	}
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new Applet1().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
		
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
		case INS_NAME:
			name = new byte[lc];
			Util.arrayCopy(buf,ISO7816.OFFSET_CDATA,name,(short)0,lc);
			lcName = lc;
			break;
		case INS_ONAME:
			Util.arrayCopy(name,(short)0,buf,(short)0,lcName);
			apdu.setOutgoingAndSend((short)0,lcName);
			break;
		case INS_BIRTH:
			birth = new byte[lc];
			Util.arrayCopy(buf,ISO7816.OFFSET_CDATA,birth,(short)0,lc);
			lcBirth = lc;
			break;
		case INS_OBIRTH:
			Util.arrayCopy(birth,(short)0,buf,(short)0,lcBirth);
			apdu.setOutgoingAndSend((short)0,lcBirth);
			break;
		case INS_ADDRESS:
			address = new byte[lc];
			Util.arrayCopy(buf,ISO7816.OFFSET_CDATA,address,(short)0,lc);
			lcAddress = lc;
			break;
		case INS_OADDRESS:
			Util.arrayCopy(address,(short)0,buf,(short)0,lcAddress);
			apdu.setOutgoingAndSend((short)0,lcAddress);
			break;
		case INS_ISSUE:
			issue = new byte[lc];
			Util.arrayCopy(buf,ISO7816.OFFSET_CDATA,issue,(short)0,lc);
			lcIssue = lc;
			break;
		case INS_OISSUE:
			Util.arrayCopy(issue,(short)0,buf,(short)0,lcIssue);
			apdu.setOutgoingAndSend((short)0,lcIssue);
			break;
		case INS_EXP:
			exp = new byte[lc];
			Util.arrayCopy(buf,ISO7816.OFFSET_CDATA,exp,(short)0,lc);
			lcExp = lc;
			break;
		case INS_OEXP:
			Util.arrayCopy(exp,(short)0,buf,(short)0,lcName);
			apdu.setOutgoingAndSend((short)0,lcName);
			break;
		case INS_CARD_ID:
			id = new byte[lc];
			Util.arrayCopy(buf,ISO7816.OFFSET_CDATA,id,(short)0,lc);
			lcId = lc;
			break;
		case INS_OCARD_ID:
			Util.arrayCopy(id,(short)0,buf,(short)0,lcId);
			apdu.setOutgoingAndSend((short)0,lcId);
			break;
		case INS_GENDER:
			gender = new byte[lc];
			Util.arrayCopy(buf,ISO7816.OFFSET_CDATA,gender,(short)0,lc);
			lcGender = lc;
			break;
		case INS_OGENDER:
			Util.arrayCopy(gender,(short)0,buf,(short)0,lcGender);
			apdu.setOutgoingAndSend((short)0,lcGender);
			break;
		// case INS_INFO:
			// //p1->gender
			// gender = (byte)(buf[ISO7816.OFFSET_P1]);
			// //cdata
			// Util.arrayCopy(buf,ISO7816.OFFSET_CDATA,info,(short)0,lc);
			// lengInfo = lc;
			// buf[0]= (byte)lc;
			// apdu.setOutgoingAndSend((short)0,(short)1);
			// break;
		// case INS_OUT_INFO:
			// short le = apdu.setOutgoing();
			// if(lengInfo>0){
				// apdu.setOutgoingLength((short)(lengInfo+2));
				// Util.arrayCopy(info,(short)0,buf,(short)0,lengInfo);
				// buf[lengInfo]=(byte)0x01;
				// buf[(short)(lengInfo+1)]=(byte)gender;
				// apdu.sendBytes((short)0,(short)(lengInfo+2));
			// }
			// break;
		case INS_RESET:
			image = new byte[32767];
			lengImage = (short)0;
			imageOffset = (short)0;
			// info = new byte[200];
			// lengInfo = (short)0;
			break;
			
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
//[B@5ca85aa5 publickey
//privateKey: [B@50b449b4