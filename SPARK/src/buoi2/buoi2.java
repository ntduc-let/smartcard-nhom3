package buoi2;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Util;
import buoi2.BigIntergerNumber.*;

public class buoi2 extends Applet
{
	public static byte[] OpA = new byte[8];
	public static byte[] OpB = new byte[8];
	public static byte[] result = new byte[8];
	
	public static byte lenA;
	public static byte lenB;
	
	private static final byte CLA = 0x00;
	private static final byte INS_SUM = 0x10;
	private static final byte INS_SUB = 0x20;
	private static final byte INS_MUL = 0x30;
	private static final byte INS_DIV = 0x40;
	
	private static final byte INS_INIT_OPERAND = 0x50;
	
	private static final byte OPERAND_A = 0x01;
	private static final byte OPERAND_B = 0x02;
	
	public static short SW_NOT_EQUAL_LENGHT = (short) 0x6910;
	public static short SW_OVERFLOW_OCCURS = (short) 0x6911;
	public static short SW_UNDERFLOW_OCCURS = (short) 0x6912;
		
	private buoi2(){
		
	}
	
	public static void install(byte[] bArray, short bOffset, byte bLength) throws ISOException
	{
		new buoi2().register();
	}

	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();
		
		if(buf[ISO7816.OFFSET_CLA]!= CLA)
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		
		
		switch (buf[ISO7816.OFFSET_INS])
		{
		case INS_INIT_OPERAND:
			apdu.setIncomingAndReceive();
			if(buf[ISO7816.OFFSET_LC]>(byte) 0x08){
				ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
			}
			
			if(buf[ISO7816.OFFSET_P1] == OPERAND_A){
				Util.arrayCopyNonAtomic(buf,ISO7816.OFFSET_CDATA,OpA,(short) 0x00,buf[ISO7816.OFFSET_LC]);
				lenA = buf[ISO7816.OFFSET_LC];
			}
			else if(buf[ISO7816.OFFSET_P2] == OPERAND_B){
				Util.arrayCopyNonAtomic(buf,ISO7816.OFFSET_CDATA,OpB,(short) 0x00,buf[ISO7816.OFFSET_LC]);
				lenB = buf[ISO7816.OFFSET_LC];
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
