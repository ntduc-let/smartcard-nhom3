/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package connectDB;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 *
 * @author anhki
 */
public class GetData {
    MongoDatabase db = new ConnectDB().database;
    Iterator it ;
    Document document = new Document();
    public GetData(String maNV) {
        
        BasicDBObject inQuery = new BasicDBObject();
	inQuery.put("maNV", maNV);
	FindIterable<Document> cursor = db.getCollection("userInfor").find(inQuery);
        it = cursor.iterator();
       
        while (it.hasNext()) {
            document = (Document) it.next();
        }
//        String xxx = document.toJson();
//        System.out.println(document.getString("name"));
      
    }
//    final static Class<? extends List> docClazz = new ArrayList<Document>().getClass();
//    public static void main(String[] args) {
//        GetData a = new GetData("NV002");
//        List<Document> comments = a.document.get("chamcong", docClazz);
//        ArrayList<String> cham = new ArrayList<>();
//        for (Object x : comments){
//            System.out.println(x);
//            cham.add(x.toString());
//        }
//        
//    }

}
