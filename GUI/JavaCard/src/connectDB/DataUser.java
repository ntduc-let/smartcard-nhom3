/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectDB;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author anhki
 */
public class DataUser {
    public String name = "";
    public String maNV = "";
    public String birth = "";
    public String coquan = "";
    public String chucvu = "";
    public String image = "";
    public String pin = "";
    public String publicKey = "";
    public String phone = "";
    public ArrayList<String> chamCong = new ArrayList<>();
    final static Class<? extends List> docClazz = new ArrayList<Document>().getClass();

    public DataUser(String maNV) {
        Document a = new GetData(maNV).document;
        name = (String) a.get("name");
        this.maNV = (String) a.get("maNV");
        birth = (String) a.get("birth");
        coquan = (String) a.get("coquan");
        chucvu = (String) a.get("chucvu");
        image = (String) a.get("image");
        pin = (String) a.get("pin");
        publicKey = (String) a.get("publicKey");
        phone = (String) a.get("phone");

        try {
            List<Document> comments = a.get("chamcong", docClazz);
            for (Object x : comments){
                chamCong.add(x.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
    public DataUser(String maNV,String phone,String name,String coquan,String chucvu,String birth,String image,String pin,String publicKey,ArrayList<String> cc ){
        this.maNV = maNV;
        this.name = name;
        this.birth = birth;
        this.chucvu = chucvu;
        this.image = image;
        this.pin = pin;
        this.coquan = coquan;
        this.publicKey = publicKey;
        this.phone = phone;
        this.chamCong = cc;
    }
    
    public DataUser(){
    }

    public void setChamCong(ArrayList<String> chamCong) {
        this.chamCong = chamCong;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setCoquan(String coquan) {
        this.coquan = coquan;
    }

    public void setChucvu(String chucvu) {
        this.chucvu = chucvu;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
    public GetData Get(String maNV){
        return new GetData(maNV);
    }
    
    public int Update(){
        try {
            new UpdateData( maNV, phone, name, coquan, chucvu, birth, image, pin, publicKey, chamCong);
        } catch(Exception ex) {
            return 0;
        }
        return 1;
    }
    public int Post(){
        try {
            new PostData(maNV, phone, name, coquan, chucvu, birth, image, pin, publicKey, chamCong);
        } catch(Exception ex) {
            return 0;
        }
        return 1;
    }
    
    
}
