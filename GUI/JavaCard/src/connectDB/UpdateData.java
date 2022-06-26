/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectDB;

import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.Iterator;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author anhki
 */
public class UpdateData {

    MongoDatabase db = new ConnectDB().database;
    Iterator it ;
    public UpdateData(String maNV,String phone,String name,String coquan,String chucvu,String birth,String image,String pin,String publicKey,ArrayList<String> cc) {
        
        Document document = new Document();
        GetData a = new GetData(maNV);
        ObjectId id =a.document.getObjectId("_id");
        document.append("_id", id);

//        System.out.println("id: " + id.toString());
        
        Bson updates = Updates.combine(
                    Updates.set("maNV", maNV),
                    Updates.set("phone", phone),
                    Updates.set("name", name),
                    Updates.set("coquan", coquan),
                    Updates.set("chucvu",chucvu ),
                    Updates.set("birth", birth),
                    Updates.set("image", image),
                    Updates.set("pin",pin ),
                    Updates.set("publicKey",publicKey ),
                    Updates.set("chamcong", cc)
                    );
            UpdateOptions options = new UpdateOptions().upsert(true);
            try {
                UpdateResult result =  db.getCollection("userInfor").updateOne(document, updates, options);
                System.out.println("Modified document count: " + result.getModifiedCount());
                System.out.println("Upserted id: " + result.getUpsertedId()); 
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        
    }
    
//    public static void main(String[] args) {
//        ArrayList<String> cc = new ArrayList<>();
//        cc.add("ngay 1/1/300");
//        cc.add("ngay 3/1/300");
//        cc.add("ngay 7/1/300");
//        UpdateData a = new UpdateData("NV001", "099999", "AAAAAAAA","HVKTMM", "Sv", "10/03/2000", "xxxxxx.jpg", "nhom3", "gjsfoiwenzeiFksdjf",cc);
//    }
    
}
