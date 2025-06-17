package app.store;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.google.gson.Gson; // Gson'u import ediyoruz
import app.model.Student; // Student modelini import ediyoruz

public class MongoStore {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;
    private static final Gson gson = new Gson(); // Gson instance'ı

    public static void init() {
        if (mongoClient == null) {
            try {
                // MongoDB varsayılan portu 27017
                mongoClient = MongoClients.create("mongodb://localhost:27017");
                database = mongoClient.getDatabase("nosql_lab_db"); // Veritabanı adınız
                collection = database.getCollection("students"); // Koleksiyon adınız
                System.out.println("MongoDB'ye bağlandı.");
            } catch (Exception e) {
                System.err.println("MongoDB bağlantı hatası: " + e.getMessage());
            }
        }
    }

    public static void put(Student student) { // Student objesini doğrudan alacak şekilde güncellendi
        if (collection != null) {
            // Student objesini bir Document'e dönüştür
            Document doc = Document.parse(gson.toJson(student));
            collection.insertOne(doc);
        } else {
            System.err.println("MongoDB bağlı değil, veri eklenemedi.");
        }
    }

    public static String get(String studentNo) { // String olarak studentNo alacak
        if (collection != null) {
            Document query = new Document("student_no", studentNo);
            Document result = collection.find(query).first();
            if (result != null) {
                // MongoDB Document'i tekrar Student objesine veya JSON String'ine dönüştürelim
                // _id alanını kaldırarak daha temiz bir JSON elde edebiliriz
                result.remove("_id");
                return result.toJson(); // JSON string olarak döndür
            }
        } else {
            System.err.println("MongoDB bağlı değil, veri çekilemedi.");
        }
        return null;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB bağlantısı kapatıldı.");
        }
    }
}