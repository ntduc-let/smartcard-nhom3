package buoi6;

import javacard.framework.*;

public class BTH4 extends Applet
{
	final static byte SV_ID_LENGHT = (byte)0x04;
	
	private static byte[] diemThi, sinhVien;
	private static byte soLuongMonThi;
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new BTH4(bArray,bOffset,bLength);
	}
	private BTH4(byte[] bArray, short bOffset, byte bLenght){
		byte iLen = bArray[bOffset];
		if(iLen ==0){
			register();
		}
		else{
			register(bArray,(short)(bOffset+1),iLen);
		}
		bOffset = (short) (bOffset+iLen+1);
		byte cLen = bArray[bOffset];
		bOffset = (short) (bOffset+cLen+1);
		byte aLen = bArray[bOffset];
		bOffset = (short) (bOffset+1);
		
		if(aLen != 0){
			sinhVien = new byte[SV_ID_LENGHT];
			Util.arrayCopy(bArray,bOffset,sinhVien,(byte)0,SV_ID_LENGHT);
			bOffset += SV_ID_LENGHT;
			soLuongMonThi = bArray[bOffset];
		}
		else{
			sinhVien = new byte[]{'S','V','0','1'};
			soLuongMonThi = (byte)0x08;
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
		case (byte)0x00:
			apdu.setOutgoing();
			apdu.setOutgoingLength((short)5);
			apdu.sendBytesLong(sinhVien,(short)0,SV_ID_LENGHT);
			buf[0] = soLuongMonThi;
			apdu.sendBytes((short)0,(short)1);
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
