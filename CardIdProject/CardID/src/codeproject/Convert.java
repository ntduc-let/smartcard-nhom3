/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codeproject;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.List;
import javax.imageio.ImageIO;
import sun.misc.BASE64Decoder;

/**
 *
 * @author kienn
 */
public class Convert{
    public static String imageToBase64(File f)throws IOException{
        String encodedFile = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            byte[] bytes = new byte[(int)f.length()];
            fileInputStream.read(bytes);
            encodedFile = Base64.getEncoder().encodeToString(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return encodedFile;
    }
    public static String stringToHex(String string){
        return String.format("%x", new BigInteger(1, string.getBytes()));
    }
    public static String hexToString(List<Byte> imageBytes){
        byte[] bytes = new byte[imageBytes.size()];
        for(int i = 0; i<imageBytes.size();i++){
            bytes[i]=imageBytes.get(i);
        }
        String imageString = "";
        try {
            imageString = new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return imageString;
    }
    public static BufferedImage base64ToImage(String base64String){
        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(base64String);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
        } catch (Exception e) {
        }
        return image;
    }
    public static String decToHex(int dec){
        return Integer.toHexString(dec);
    }
}
