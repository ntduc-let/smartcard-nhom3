package nhom3;

import javacard.framework.*;
import javacard.framework.OwnerPIN;
import javacard.security.KeyBuilder;
import javacard.security.RandomData;
import javacardx.crypto.Cipher;
import javacard.security.AESKey;
import javacard.security.Key;
import javacard.security.KeyAgreement;
import javacard.security.KeyPair;
import javacard.security.RSAPrivateCrtKey;
import javacard.security.RSAPrivateKey;
import javacard.security.RSAPublicKey;

public class nhom3 extends Applet
{

	// CLA
    final static byte nhom3_CLA =(byte)0xB0;
    
    //Kich thuoc ma pin
	private final static byte PIN_MIN_SIZE = (byte) 4;
	private final static byte PIN_MAX_SIZE = (byte) 16;
	private final static byte[] PIN_INIT_VALUE={(byte)'n',(byte)'h',(byte)'o',(byte)'m',(byte)'3'};
    
    //Information
	public static byte[] OpData = new byte[256];
	public static byte lenData = (byte)0;
	
	public static byte[] OpID;
	public static byte lenID = (byte)0;
	public static byte[] OpNAME;
	public static byte lenNAME = (byte)0;
	public static byte[] OpDATE;
	public static byte lenDATE= (byte)0;
	public static byte[] OpPHONE;
	public static byte lenPHONE= (byte)0;
	public static byte[] OpCoQuan;
	public static byte lenCoQuan= (byte)0;
	public static byte[] OpChucVu;
	public static byte lenChucVu= (byte)0;
	
	public static byte[] OpImage,size;
    
    // Maximum number of keys handled by the Cardlet
	private final static byte MAX_NUM_KEYS = (byte) 16;
	//object ID
	private final static byte KEY_ACL_SIZE = (byte) 6;
	private final static byte ACL_READ = (byte) 0;
	private final static byte ACL_WRITE = (byte) 2;
	private final static byte ACL_USE = (byte) 4;
    // INS - Khoi tao
	private final static byte INS_SETUP = (byte) 0x2A;
	// Keys' use and management
	private final static byte INS_GEN_KEYPAIR = (byte) 0x30;
	//INS-PIN
	private final static byte INS_CREATE_PIN = (byte) 0x40;
	private final static byte INS_VERIFY_PIN = (byte) 0x42;
	private final static byte INS_CHANGE_PIN = (byte) 0x44;
	private final static byte INS_UNBLOCK_PIN = (byte) 0x46;
	private final static byte INS_RESET_PIN = (byte) 0x48;
	
	private final static byte INS_CREATE_INFORMATION = (byte)0x50;
	private final static byte INS_OUT_INFORMATION = (byte)0x51;
	private final static byte INS_CHANGE_INFORMATION = (byte)0x52;
	private final static byte OUT_ID = (byte)0x01;
	private final static byte OUT_NAME = (byte)0x02;
	private final static byte OUT_DATE = (byte)0x03;
	private final static byte OUT_PHONE = (byte)0x04;
	private final static byte OUT_CO_QUAN = (byte)0x05;
	private final static byte OUT_CHUC_VU = (byte)0x06;
	
	private final static byte INS_CREATE_IMAGE = (byte)0x53;
	private final static byte INS_CREATE_SIZEIMAGE = (byte)0x54;//countanh
	private final static byte INS_OUT_SIZEIMAGE = (byte)0x55;
	private final static byte INS_OUT_IMAGE = (byte)0x56;
	
	private final static byte INS_LOGOUT_ALL = (byte) 0x60;
	private final static byte INS_CHECK_LOGIN = (byte) 0x61;
	// Cipher Modes admitted in ComputeCrypt
	private final static byte OPT_DEFAULT = (byte) 0x00; // Use JC defaults
	private final static byte OPT_RSA_PUB_EXP = (byte) 0x01; // RSA: provide public exponent
	// Standard public ACL
	private static byte[] STD_PUBLIC_ACL;
	private static byte[] acl; // Temporary ACL
	/* Kiem tra trang thai setup */
	private boolean setupDone = false;
	private byte create_object_ACL;
	private byte create_key_ACL;
	private byte create_pin_ACL;
    
	private OwnerPIN pin, ublk_pin;
    
    private byte[] tmpBuffer;
	/** ghi trang thai dang nhap*/
	private short logged_ids;
	//check first connect
	private final static byte[] first_logged_ids = new byte[]{(byte)0x01};
    
    // Key objects (allocated on demand)
	private Key[] keys;
	// Key ACLs
	private byte[] keyACLs;
	// Key Tries Left
	private byte[] keyTries;
	// Key iterator for ListKeys: it's an offset in the keys[] array.
	private byte key_it;
	//crypt
	private Cipher aesCipher;
	private AESKey aesKey;
	private static short KEY_SIZE = 32;
	//define
	private static short LENGTH_BLOCK_AES = (short)64;
	/*****
	*RSA**
	*****/
	private KeyPair[] keyPairs;
	// Offsets in buffer[] for key generation
	private final static short OFFSET_GENKEY_ALG = (short) (ISO7816.OFFSET_CDATA);
	private final static short OFFSET_GENKEY_SIZE = (short) (ISO7816.OFFSET_CDATA + 1);
	private final static short OFFSET_GENKEY_PRV_ACL = (short) (ISO7816.OFFSET_CDATA + 3);
	private final static short OFFSET_GENKEY_PUB_ACL = (short) (OFFSET_GENKEY_PRV_ACL + KEY_ACL_SIZE);
	private final static short OFFSET_GENKEY_OPTIONS = (short) (OFFSET_GENKEY_PUB_ACL + KEY_ACL_SIZE);
	private final static short OFFSET_GENKEY_RSA_PUB_EXP_LENGTH = (short) (OFFSET_GENKEY_OPTIONS + 1);
	private final static short OFFSET_GENKEY_RSA_PUB_EXP_VALUE = (short) (OFFSET_GENKEY_RSA_PUB_EXP_LENGTH + 2);
    
    /**
	* ERROR CONTROL*
	**/
	/** parameter passed invalid */
	private final static short SW_INVALID_PARAMETER = (short) 0x9C0F;
	/** returns error 9c0c when card is locked */
	private final static short SW_IDENTITY_BLOCKED = (short) 0x9C0C;
	
	
	
	private final static short SW_VERYFI_2 = (short) 0x9C00;
	private final static short SW_VERYFI_1 = (short) 0x9C01;
	
	
	
	
	
	/** tra lai 9c02 khi nhap ma pin sai */
	private final static short SW_AUTH_FAILED = (short) 0x9C02;
	/** tra lai khi pin khong bi khoa*/
	private final static short SW_OPERATION_NOT_ALLOWED = (short) 0x9C03;
	/** Kiem soat loi */
	private final static short SW_INTERNAL_ERROR = (short) 0x9CFF;

	private final static short SW_SETUP_NOT_DONE = (short) 0x9C04;
	/** Loi P1*/
	private final static short SW_INCORRECT_P1 = (short) 0x9C10;
	/** Loi P2*/
	private final static short SW_INCORRECT_P2 = (short) 0x9C11;
	/** Required operation was not authorized because of a lack of privileges */
	private final static short SW_UNAUTHORIZED = (short) 0x9C06;
	/** Algorithm specified is not correct */
	private final static short SW_INCORRECT_ALG = (short) 0x9C09;
    
    private nhom3(byte[] bArray, short bOffset, byte bLength){
		if (!CheckPINPolicy(PIN_INIT_VALUE, (short) 0, (byte) PIN_INIT_VALUE.length))
		    ISOException.throwIt(SW_INTERNAL_ERROR);
		OpImage = new byte[10000];
		size = new byte[7];

		/*pin khoi tao*/
		pin = new OwnerPIN((byte) 3, (byte) PIN_INIT_VALUE.length);
		pin.update(PIN_INIT_VALUE, (short) 0, (byte) PIN_INIT_VALUE.length);
		register();
	}
	
	public boolean select() {
		LogOut();
		return true;
	}

	public void deselect() {
		LogOut();
	}
    
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		nhom3 wal = new nhom3(bArray, bOffset, bLength);
	}

	public void process(APDU apdu)
	{
		byte[] buffer = apdu.getBuffer();
		if (selectingApplet())
		{
			CheckFisrtUse(apdu,buffer);
			ISOException.throwIt(ISO7816.SW_NO_ERROR);
		}
		//apdu.setIncomingAndReceive();
		if ((buffer[ISO7816.OFFSET_CLA] == 0) && (buffer[ISO7816.OFFSET_INS] == (byte) 0xA4))
			return;
		if (buffer[ISO7816.OFFSET_CLA] != nhom3_CLA)
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		byte ins = buffer[ISO7816.OFFSET_INS];
		if (!setupDone && (ins != (byte) INS_SETUP))
			ISOException.throwIt(SW_SETUP_NOT_DONE);

		if (setupDone && (ins == (byte) INS_SETUP))
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);

		switch (ins)
		{
		case INS_SETUP:
			setup(apdu, buffer);
			break;
		case INS_CREATE_PIN:
			CreatePIN(apdu, buffer);
			break;
		case INS_VERIFY_PIN:
			VerifyPIN(apdu, buffer);
			break;
		case INS_CHANGE_PIN:
			ChangePIN(apdu, buffer);
			break;
		case INS_UNBLOCK_PIN:
			UnblockPIN(apdu, buffer);
			break;
		case INS_RESET_PIN:
			ResetPIN(apdu, buffer);
			break;
		case INS_CHECK_LOGIN:
			CheckLogin(apdu,buffer);
			break;
		case INS_LOGOUT_ALL:
			LogOut();
			break;
		case INS_CREATE_INFORMATION:
			SetupInformation(apdu,buffer);
			break;
		case INS_OUT_INFORMATION:
			OutputInformation(apdu,buffer);
			break;
		case INS_CHANGE_INFORMATION:
			ChangeInformation(apdu,buffer);
			break;
		case INS_CREATE_IMAGE:
			SetupImage(apdu,buffer);
			break;
		case INS_CREATE_SIZEIMAGE:
			SetCount(apdu,buffer);
			break;
		case INS_OUT_SIZEIMAGE:
			OuputSize(apdu,buffer);
			break;
		case INS_OUT_IMAGE:
			OututImage(apdu,buffer);
			break;
		// case INS_GEN_KEYPAIR:
			// GenerateKeyPair(apdu, buffer);
			//break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

	/*SETUP*/
	private void setup(APDU apdu, byte[] buffer) {
		try {
			tmpBuffer = JCSystem.makeTransientByteArray((short) 256, JCSystem.CLEAR_ON_DESELECT);
		} catch (SystemException e) {
			tmpBuffer = new byte[(short) 256];
		}
		
		aesCipher = Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_CBC_NOPAD, false);
        aesKey = (AESKey) KeyBuilder.buildKey(KeyBuilder.TYPE_AES, KeyBuilder.LENGTH_AES_128, false);
        Sha512.init();
		HMacSHA512.init(tmpBuffer);
        byte[] keyBytes = JCSystem.makeTransientByteArray(LENGTH_BLOCK_AES, JCSystem.CLEAR_ON_DESELECT);
        try {
            HMacSHA512.computeHmacSha512(PIN_INIT_VALUE,(short)0x00,(short)PIN_INIT_VALUE.length,keyBytes,(short)0);
            aesKey.setKey(keyBytes, (short) 0);
        } finally {
            Util.arrayFillNonAtomic(keyBytes, (short) 0, LENGTH_BLOCK_AES, (byte) 0);
        }
		// first_logged_ids[0] = (byte)0x00;
		setupDone = true;
	}
	
	private void CreatePIN(APDU apdu, byte[] buffer) {
		//send B0400003050409090909
		//send CLA:B0 INS:40 P1:00 P2:*max-tries-03* LC:*05* DATA:*pinsize-04|pincode-09090909*
		byte num_tries = buffer[ISO7816.OFFSET_P2];
		/* Kiem tra dang nhap */
		// if ((create_pin_ACL == (byte) 0xFF)
				// || (((logged_ids & create_pin_ACL) == (short) 0x0000) && (create_pin_ACL != (byte) 0x00)))
			// ISOException.throwIt(SW_UNAUTHORIZED);
		short avail = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]); // 05
		if (apdu.setIncomingAndReceive() != avail)
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		// toi thieu 1 byte s... size pin va 1 byte pin code
		if (avail < (short)2)
			ISOException.throwIt(SW_INVALID_PARAMETER);
		byte pin_size = buffer[ISO7816.OFFSET_CDATA]; // 04
		if (avail < (short) (1 + pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (!CheckPINPolicy(buffer, (short) (ISO7816.OFFSET_CDATA + 1), pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
			
		////unblock
		// byte ucode_size = buffer[(short) (ISO7816.OFFSET_CDATA + 1 + pin_size)];
		// if (avail != (short) (1 + pin_size + 1 + ucode_size))
			// ISOException.throwIt(SW_INVALID_PARAMETER);
		// if (!CheckPINPolicy(buffer, (short) (ISO7816.OFFSET_CDATA + 1 + pin_size + 1), ucode_size))
			// ISOException.throwIt(SW_INVALID_PARAMETER);
			
		pin = new OwnerPIN(num_tries, PIN_MAX_SIZE);
		pin.update(buffer, (short) (ISO7816.OFFSET_CDATA + 1), pin_size);
		first_logged_ids[0] = (byte)0x00;
		
		////unblock
		//ublk_pins[pin_nb] = new OwnerPIN((byte) 3, PIN_MAX_SIZE);
		// Recycle variable pin_size
		//pin_size = (byte) (ISO7816.OFFSET_CDATA + 1 + pin_size + 1);
		//ublk_pins[pin_nb].update(buffer, pin_size, ucode_size);
	}
	
	private void VerifyPIN(APDU apdu, byte[] buffer) {
		//send CLA:B0 INS:42
		if (pin == null)
			ISOException.throwIt(SW_INCORRECT_P1);
		if (buffer[ISO7816.OFFSET_P2] != 0x00)
			ISOException.throwIt(SW_INCORRECT_P2);
		short numBytes = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		if (numBytes != apdu.setIncomingAndReceive())
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		if (!CheckPINPolicy(buffer, ISO7816.OFFSET_CDATA, (byte) numBytes))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (!pin.check(buffer, (short) ISO7816.OFFSET_CDATA, (byte) numBytes)) {
			LogOut();
			if (pin.getTriesRemaining() == (byte) 0x02)
				ISOException.throwIt(SW_VERYFI_2);
			if (pin.getTriesRemaining() == (byte) 0x01)
				ISOException.throwIt(SW_VERYFI_1);
			if (pin.getTriesRemaining() == (byte) 0x00)
				ISOException.throwIt(SW_IDENTITY_BLOCKED);
		}
		logged_ids = (short) (0x0010);
	}
	
	private void ChangePIN(APDU apdu, byte[] buffer) {
		//send 
		//send CLA:B0 INS:44 P1:00 P2:00 LC:*0B* DATA:*pinsize-04|pincode-09090909|pinsize-05|pincode-0102030405*
		//byte pin_nb = buffer[ISO7816.OFFSET_P1];
		if (pin == null)
			ISOException.throwIt(SW_INCORRECT_P1);
		if (buffer[ISO7816.OFFSET_P2] != (byte) 0x00)
			ISOException.throwIt(SW_INCORRECT_P2);
		short avail = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		if (apdu.setIncomingAndReceive() != avail)
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		// co toi thieu 1 byte pin-size 1 byte pin-code
		if (avail < (short)4)
			ISOException.throwIt(SW_INVALID_PARAMETER);
		byte pin_size = buffer[ISO7816.OFFSET_CDATA];
		if (avail < (short) (1 + pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (!CheckPINPolicy(buffer, (short) (ISO7816.OFFSET_CDATA + 1), pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		byte new_pin_size = buffer[(short) (ISO7816.OFFSET_CDATA + 1 + pin_size)];
		if (avail < (short) (1 + pin_size + new_pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (!CheckPINPolicy(buffer, (short) (ISO7816.OFFSET_CDATA + 1 + pin_size + 1), new_pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (pin.getTriesRemaining() == (byte) 0x00)
			ISOException.throwIt(SW_IDENTITY_BLOCKED);
		if (!pin.check(buffer, (short) (ISO7816.OFFSET_CDATA + 1), pin_size)) {
			LogOut();
			ISOException.throwIt(SW_AUTH_FAILED);
		}
		pin.update(buffer, (short) (ISO7816.OFFSET_CDATA + 1 + pin_size + 1), new_pin_size);
		
		logged_ids = (short) (0x0010);
	}
	
	private void UnblockPIN(APDU apdu, byte[] buffer) {
		//byte pin_nb = buffer[ISO7816.OFFSET_P1];
		//OwnerPIN ublk_pin = ublk_pins[pin_nb];
		if (pin == null)
			ISOException.throwIt(SW_INCORRECT_P1);
		//if (ublk_pin == null)
			//ISOException.throwIt(SW_INTERNAL_ERROR);
		// Neu ma PIN khong bi chan, khong hop le
		if (pin.getTriesRemaining() != 0)
			ISOException.throwIt(SW_OPERATION_NOT_ALLOWED);
		if (buffer[ISO7816.OFFSET_P2] != 0x00)
			ISOException.throwIt(SW_INCORRECT_P2);
		short numBytes = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		
		if (numBytes != apdu.setIncomingAndReceive())
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		// if (!CheckPINPolicy(buffer, ISO7816.OFFSET_CDATA, (byte) numBytes))
			// ISOException.throwIt(SW_INVALID_PARAMETER);
		// if (!ublk_pin.check(buffer, ISO7816.OFFSET_CDATA, (byte) numBytes))
			// ISOException.throwIt(SW_AUTH_FAILED);
		//
		pin.resetAndUnblock();
	}
	
	private void ResetPIN(APDU apdu, byte[] buffer) {
		pin.update(PIN_INIT_VALUE, (short) 0, (byte) PIN_INIT_VALUE.length);
		first_logged_ids[0] = (byte)0x01;
	}
	
	private void SetupInformation(APDU apdu, byte[] buffer){
			ChangeInformation(apdu,buffer);
			apdu.setIncomingAndReceive();
			lenData = buffer[ISO7816.OFFSET_LC];
			
			// if(lenData>(byte) 0x3C){
				// ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
			// }
			
			Util.arrayCopyNonAtomic(buffer,ISO7816.OFFSET_CDATA,OpData,(short) 0x00,lenData);
					
			short flag = (short)0;
			byte[] objData = new byte[0];
			byte objDatalen = (short)0;
			
			for(short i=0;i<=lenData;i++){
				if((byte)(OpData[i]) == (byte)0x02){
					flag = (short)1;
					continue;
				}
				else if((byte)(OpData[i]) == (byte)0x03){
					flag = (short)0;
				};
				if(flag == (short)1){
					byte[] temp = new byte[objDatalen];
					if(objDatalen>0){
						for(short t=0;t<(short)temp.length;t++){
							temp[t] = objData[t];
						}
						
					}
					
					objData = new byte[objDatalen+1];
					if(objDatalen>0){
						for(short j=0;j<(short)objData.length;j++){
							if(j!=(short)temp.length){
								objData[j] = temp[j];
							}
						}
					}
					objData[objDatalen] = OpData[i];
					objDatalen++;
				}
				else if(flag == (short)0 && objDatalen != (byte)0){
					if(lenID == (byte)0){
						if(objDatalen > 6){
							ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
						}
						byte[] temp = encrypt(objData);
						OpID = new byte[128];
						Util.arrayCopyNonAtomic(temp,(short)0x00,OpID,(short)0x00,(short)(temp.length));
						lenID = (byte)(objDatalen);
						objData = new byte[0];
					}
					else if(lenNAME == (byte)0){
						if(objDatalen > 25){
							ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
						}
						byte[] temp = encrypt(objData);
						OpNAME = new byte[128];
						Util.arrayCopyNonAtomic(temp,(short)0x00,OpNAME,(short)0x00,(short)(temp.length));
						lenNAME = (byte)(objDatalen);
						objData = new byte[0];
					}
					else if(lenDATE == (byte)0){
						if(objDatalen > 10){
							ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
						}
						byte[] temp = encrypt(objData);
						OpDATE = new byte[128];
						Util.arrayCopyNonAtomic(temp,(short)0x00,OpDATE,(short)0x00,(short)(temp.length));
						lenDATE = (byte)(objDatalen);
						objData = new byte[0];
					}
					else if(lenPHONE == (byte)0){
						if(objDatalen > 10){
							ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
						}
						byte[] temp = encrypt(objData);
						OpPHONE = new byte[128];
						Util.arrayCopyNonAtomic(temp,(short)0x00,OpPHONE,(short)0x00,(short)(temp.length));
						lenPHONE = (byte)(objDatalen);
						objData = new byte[0];
					}
					else if(lenCoQuan == (byte)0){
						if(objDatalen > 25){
							ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
						}
						byte[] temp = encrypt(objData);
						OpCoQuan = new byte[128];
						Util.arrayCopyNonAtomic(temp,(short)0x00,OpCoQuan,(short)0x00,(short)(temp.length));
						lenCoQuan = (byte)(objDatalen);
						objData = new byte[0];
					}
					else if(lenChucVu == (byte)0){
						if(objDatalen > 25){
							ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
						}
						byte[] temp = encrypt(objData);
						OpChucVu = new byte[128];
						Util.arrayCopyNonAtomic(temp,(short)0x00,OpChucVu,(short)0x00,(short)(temp.length));
						lenChucVu = (byte)(objDatalen);
						objData = new byte[0];
					}
					objDatalen = (short)0;
				};
			}
	}
	
	private void OutputInformation(APDU apdu, byte[] buffer){
		apdu.setIncomingAndReceive();
			if(buffer[ISO7816.OFFSET_P1] == OUT_ID){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenID);
				Util.arrayCopy(decrypt(OpID,lenID),(short)0,buffer,(short)0,lenID);
				apdu.sendBytes((short)0,lenID);
			}
			else if(buffer[ISO7816.OFFSET_P1] == OUT_NAME){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenNAME);
				Util.arrayCopy(decrypt(OpNAME,lenNAME),(short)0,buffer,(short)0,lenNAME);
				apdu.sendBytes((short)0,lenNAME);
			}
			else if(buffer[ISO7816.OFFSET_P1] == OUT_DATE){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenDATE);
				Util.arrayCopy(decrypt(OpDATE,lenDATE),(short)0,buffer,(short)0,lenDATE);
				apdu.sendBytes((short)0,lenDATE);
			}
			else if(buffer[ISO7816.OFFSET_P1] == OUT_PHONE){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenPHONE);
				Util.arrayCopy(decrypt(OpPHONE,lenPHONE),(short)0,buffer,(short)0,lenPHONE);
				apdu.sendBytes((short)0,lenPHONE);
			}
			else if(buffer[ISO7816.OFFSET_P1] == OUT_CO_QUAN){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenCoQuan);
				Util.arrayCopy(decrypt(OpCoQuan,lenCoQuan),(short)0,buffer,(short)0,lenCoQuan);
				apdu.sendBytes((short)0,lenCoQuan);
			}
			else if(buffer[ISO7816.OFFSET_P1] == OUT_CHUC_VU){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenChucVu);
				Util.arrayCopy(decrypt(OpChucVu,lenChucVu),(short)0,buffer,(short)0,lenChucVu);
				apdu.sendBytes((short)0,lenChucVu);
			}
	}
	
	private void ChangeInformation(APDU APDU,byte[] buffer){
		lenID = (byte) 0;
		lenDATE = (byte) 0;
		lenNAME = (byte) 0;
		lenPHONE = (byte) 0;
		lenCoQuan = (byte) 0;
		lenChucVu = (byte) 0;
	}
	
	//Input Image
	private void SetupImage(APDU apdu, byte[] buffer){
		apdu.setIncomingAndReceive();
		short p1 = (short)(buffer[ISO7816.OFFSET_P1]&0xff);
	// short count = (short)(249 * p1);
	// Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, OpImage, count, (short)249);
	
		short count = (short)(256 * p1);
		
		byte[] data = new byte[249];
		Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, data, (short)0, (short)249);
		byte[] data1 = new byte[128];
		Util.arrayCopy(data,(short)0, data1, (short)0, (short)128);
		
		Util.arrayCopy(encrypt(data1),(short)0, OpImage, count, (short)128);
		
		
		
		short count2 = (short)(256 * p1 + 128);
		byte[] data2 = new byte[121];
		
		
		Util.arrayCopy(data, (short)128, data2, (short)0, (short)121);
		Util.arrayCopy(encrypt(data2),(short)0, OpImage, count2 , (short)128);
		
	
	}
	private void SetCount(APDU apdu, byte[] buffer){
		apdu.setIncomingAndReceive();
		Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, size, (short)0, (short)7);
	}
	private void OuputSize(APDU apdu, byte[] buffer){
		apdu.setIncomingAndReceive();
		apdu.setOutgoing();
		apdu.setOutgoingLength((short)7);
		Util.arrayCopy(size, (short)0, buffer, (short)0, (short)(size.length));
		apdu.sendBytes((short)0,(short)7);
	}
	private void OututImage(APDU apdu, byte[] buffer){
		apdu.setIncomingAndReceive();
		apdu.setOutgoing();
		short p = (short)(buffer[ISO7816.OFFSET_P1]&0xff);
	//short count = (short)(249 * p);
		short count = (short)(256 * p);
		
		
		byte[] data = new byte[249];
		
		byte[] temp = new byte[128];
		Util.arrayCopy(OpImage, count, temp,(short) 0, (short)128);
		Util.arrayCopy(decrypt(temp, (byte)10), (short)0, data, (short)0, (short)128);
		
		short count2 = (short)(256 * p + 128);
		byte[] temp2 = new byte[128];
		Util.arrayCopy(OpImage, count2, temp2, (short)0, (short)128);
		Util.arrayCopy(decrypt(temp2, (byte)10), (short) 0, data, (short)128, (short)121);
		
		
		apdu.setOutgoingLength((short)249);
	//apdu.sendBytesLong(OpImage, count, (short)249);
		
		apdu.sendBytesLong(data, (short) 0, (short)249);
	}
	/*CHECK PIN POLICY*/
	private boolean CheckPINPolicy(byte[] pin_buffer, short pin_offset, byte pin_size) {
		if ((pin_size < PIN_MIN_SIZE) || (pin_size > PIN_MAX_SIZE))
			return false;
		return true;
	}
	private void LogOut() {
		logged_ids = (short) 0x0000; 
		pin.reset();
	}
	private void CheckFisrtUse(APDU apdu,byte[] buffer){
		apdu.setOutgoing();
		apdu.setOutgoingLength((short)1);
		Util.arrayCopy(first_logged_ids,(short)0,buffer,(short)0,(short)1);
		apdu.sendBytes((short)0,(short)1);
	}
	private void CheckLogin(APDU apdu,byte[] buffer){
		apdu.setOutgoing();
		apdu.setOutgoingLength((short)1);
		byte[] bytelog = new byte[]{(byte)logged_ids};
		Util.arrayCopy(bytelog,(short)0,buffer,(short)0,(short)1);
		apdu.sendBytes((short)0,(short)1);
	}
	
	/*AES*/
    /**Encrypt*/
	private byte[] encrypt(byte[] encryptData) {
        aesCipher.init(aesKey, Cipher.MODE_ENCRYPT);
        short flag = (short) 1;
	    byte[] temp = new byte[128];
    	while(flag == (short)1){
    		for(short i=0;i<=(short) encryptData.length;i++){
    			if(i!=(short) encryptData.length){
					temp[i] = encryptData[i];
    			}
    			else{
	    			flag = (short) 0;
    			}
    		}
    	}

        byte[] dataEncrypted; 
        
        try{
				dataEncrypted = JCSystem.makeTransientByteArray((short) 128, JCSystem.CLEAR_ON_DESELECT);
			} catch(SystemException e){
				dataEncrypted = new byte[(short)128];
			}
        aesCipher.doFinal(temp, (short) 0 , (short)128, dataEncrypted, (short) 0x00);
        return dataEncrypted;
    }

    /**Decrypt
     * Decrypt data.
     */
    private byte[] decrypt(byte[] decryptData, byte length) {
    	if(length != (short)0){
			aesCipher.init(aesKey, Cipher.MODE_DECRYPT);
			byte[] dataDecrypted;
			try{
				dataDecrypted = JCSystem.makeTransientByteArray((short) 128, JCSystem.CLEAR_ON_DESELECT);
			} catch(SystemException e){
				dataDecrypted = new byte[(short)128];
			}
			aesCipher.doFinal(decryptData, (short) 0, (short) 128, dataDecrypted, (short) 0x00);
			return dataDecrypted;
    	}
    	else{
    		byte[] dataDecrypted = new byte[1];
	    	return dataDecrypted;
    	}

    }

}


