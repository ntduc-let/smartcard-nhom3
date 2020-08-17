package dichvucong;

import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class JavaSmartcard {

    protected List<CardTerminal> terminals;
    protected static CardTerminal terminal;
    protected static TerminalFactory factory;
    protected static Card card;
    public static ResponseAPDU rAPDU;
    protected static CardChannel channel;
    static boolean IsConnectedToCard = false;
    static String lastMessage ;
    
    public JavaSmartcard()
    {
        
    }
    
    public List<CardTerminal> getTerminals() throws Exception{
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();

        return terminals;
    }
    
    public CardTerminal getCardReader(String name) {
        CardTerminal reader = null;
        for (int i = 0; i < terminals.size(); i++) 
        {
            if (terminals.get(i).getName().equals(name)) 
            {
                reader = terminals.get(i);
            }
        }

        return reader;
    } 
    
    protected void connectToCard(CardTerminal terninalSource) throws CardException {
            terminal = terninalSource;
            card = terminal.connect("*");
    }    
    
    public void sendApdu(byte[] apdu) throws CardException, IllegalArgumentException, NullPointerException{
        channel = card.getBasicChannel();
        rAPDU = channel.transmit(new CommandAPDU(apdu));
        //System.out.println(htos(rAPDU.getData()));
    }    
    
    public byte[] getData() {
        if (rAPDU!=null) {
            return rAPDU.getData();
        }
        else {
            return null;
        }
    }    
    
    public int getStatusWords() {
        return rAPDU.getSW();
    }
        
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }    
    
    public static String byteArrayToHexString(byte[] b){
    String result = "";
    for (int i = 0; i < b.length; i++) {
        result +=
                Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
    }    
    
    public static String htos(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String tmp = Integer.toHexString(((int) bytes[i]) & 0xFF);
            while (tmp.length() < 2) {
                tmp = "0" + tmp;
            }
            if (i != bytes.length - 1) {
                sb.append(tmp).append(" ");
            } else {
                sb.append(tmp);
            }
            if(((i+1)%17) == 0)
            {
                sb.append("\n");
                sb.append("        ");
            }

        }
        return sb.toString().toUpperCase();
    }
}
