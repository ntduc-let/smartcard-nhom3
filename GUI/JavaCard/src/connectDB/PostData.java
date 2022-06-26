/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectDB;

import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Iterator;
import org.bson.Document;

/**
 *
 * @author anhki
 */
public class PostData {
    MongoDatabase db = new ConnectDB().database;
    Iterator it ;
    public PostData(String maNV,String phone,String name,String coquan,String chucvu,String birth,String image,String pin,String publicKey,ArrayList<String> cc ) {
        
        Document document = new Document();
        document.append("maNV", maNV);
        document.append("phone", phone);
        document.append("name", name);
        document.append("coquan", coquan);
        document.append("chucvu", chucvu);
        document.append("birth", birth);
        document.append("image", image);
        document.append("pin", pin);
        document.append("publicKey", publicKey);
        document.append("chamcong", cc);
        db.getCollection("userInfor").insertOne(document);
        
    }
//    public static void main(String[] args) {
//        ArrayList<String> cc = new ArrayList<>();
//        cc.add("ngay 1/1/300");
//        new PostData("NV003", "", "", "", "", "", "", "", "", cc);
//    }
}
