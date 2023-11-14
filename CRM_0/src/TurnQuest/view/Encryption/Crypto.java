package TurnQuest.view.Encryption;

import java.security.MessageDigest;

public class Crypto {
    public Crypto() {
        super();
    }
    public static String MD5(String text)  
            {       
                    StringBuffer sb = new StringBuffer();
                    try{
                            MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(text.getBytes());
                    byte byteData[] = md.digest();
                    //convert the byte to hex format
                    for (int i = 0; i < byteData.length; i++) {
                     sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    }catch(Exception e){
                            e.printStackTrace();
                    }
                    return sb.toString();
            }
     
}
