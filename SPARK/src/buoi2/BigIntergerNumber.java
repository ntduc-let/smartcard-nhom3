package buoi2;
public class BigIntergerNumber {
	
		public static boolean SUM(byte[] A, byte AOff, byte[] B, byte BOff,byte[]C,byte COff, byte len){
			short result = 0;
			for(len =(byte)(len-1);len>=0;len--){
				result = (short)(getUnsignedByte(A,AOff,len)+getUnsignedByte(B,BOff,len)+result);
				C[(byte)(len+COff)] = (byte)result;
				if(result>0x00FF){
					result = 1;
					result = (short)(result + 0x100);
				} else{
					result = 0;
				}
			}
			if(result ==1){
				return false;
			}
			return true;
		}
		
		public static boolean SUB(byte[] A, byte AOff, byte[] B, byte BOff,byte[]C,byte COff, byte len){
			short borrow = 0;
			short result;
			for(len =(byte)(len-1);len>=0;len--){
				result = (short)(getUnsignedByte(A,AOff,len)-getUnsignedByte(B,BOff,len)-borrow);
				if(result<0){
					borrow = 1;
					result = (short)(result + 0x100);
				} else{
					borrow = 0;
				}
				
				C[(byte)(len+COff)] = (byte)result;
			}
			if(borrow ==1){
				return false;
			}
			return true;
		}
		
		public static short getUnsignedByte(byte[] A, byte AOff, byte count){
			return (short)(A[(short)(count+AOff)&0x00FF]);
		}
		
}
