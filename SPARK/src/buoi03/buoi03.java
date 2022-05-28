package buoi03;

import javacard.framework.*;

public class buoi03 extends Applet
{
	private static final byte CLA=0x00;
	private static final byte INS_NAME=0x00;
	private static final byte INS_BIRTHDAY = 0x01;
	private static final byte INS_INFO = 0x02;

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new buoi03().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}
		byte[] name = {'S','P','A','R','K'};
		short nameLenght = (short)name.length;
		byte[] birthDay = {(byte)25,(byte)10,(byte)1999};
		short	birthDayLenght = (short)birthDay.length;
		short infoLenght = (short)(nameLenght+birthDayLenght);
		apdu.setIncomingAndReceive();
		
		byte[] buf = apdu.getBuffer();
		if(buf[ISO7816.OFFSET_CLA]!= CLA)
				ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		switch (buf[ISO7816.OFFSET_INS])
		{
		case (byte)INS_NAME:
			apdu.setOutgoing();
			apdu.setOutgoingLength((short)nameLenght);
			Util.arrayCopy(name,(short)0,buf,(short)0,nameLenght);
			apdu.sendBytes((short)0,nameLenght);
			break;
		case (byte)INS_BIRTHDAY:
			apdu.setOutgoing();
			apdu.setOutgoingLength((short)birthDayLenght);
			Util.arrayCopy(birthDay,(short)0,buf,(short)0,birthDayLenght);
			apdu.sendBytes((short)0,birthDayLenght);
			break;
		case (byte)INS_INFO:
			apdu.setOutgoing();
			apdu.setOutgoingLength((short)infoLenght);
			Util.arrayCopy(name,(short)0,buf,(short)0,nameLenght);
			Util.arrayCopy(birthDay,(short)0,buf,(short)nameLenght,birthDayLenght);
			apdu.sendBytes((short)0,infoLenght);
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
