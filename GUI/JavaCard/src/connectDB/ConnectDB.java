/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectDB;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *{"_id":"NV001","name":"temp","phone":"09111","coquan":"HVKTMM","chucvu":"SV","birth":"30/03/2000","image":"fasfweroihf","pin":"nhom3","publicKey":"sdfjaskfjoisodfwqefxsdfa"}
 * @author anhki
 */
public class ConnectDB {
    MongoDatabase database ;
    
    public ConnectDB() {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://kieuhuynh:nhom3smartcard@cluster0.oidybk0.mongodb.net/?retryWrites=true&w=majority");
        
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("smartcard");
//        System.out.println(database.listCollectionNames().toString());
//        for (String t: database.listCollectionNames()) {
//            System.out.println(t);
//        }
    }
}
