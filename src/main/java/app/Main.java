package app;

import static spark.Spark.*;
import com.google.gson.Gson;
import app.store.*;
import app.model.Student; // Student modelini import ediyoruz

import java.util.concurrent.atomic.AtomicLong; // Rastgele öğrenci no için
import java.util.Random; // Rastgele isim ve bölüm için

public class Main {

    private static final String[] NAMES = {"Münip Utandı", "Nağme Yarkın", "Aysun Gültekin", "Barış Manço", "Cem Karaca", "Sezen Aksu", "Tarkan", "Ajda Pekkan"};
    private static final String[] DEPARTMENTS = {"Classical Turkish Music", "Turkish Folk Music", "Pop Music", "Rock Music", "Jazz Studies", "Ethnomusicology"};
    private static final int NUM_STUDENTS = 10000; // Toplam 10.000 kayıt

    public static void main(String[] args) {
        System.out.println("Spark sunucusunu başlatmaya çalışıyor...");
        port(8080);
        System.out.println("Spark port 8080 olarak ayarlandı.");
        Gson gson = new Gson();

        // Veritabanı başlatma ve veri ekleme
        initDatabases(gson);

        System.out.println("API endpointleri tanımlanıyor...");

        // Endpoint tanımları
        // URL yapısı: /nosql-lab-rd/student_no=xxxxxxxxxx
        // Burada :parametre yerine * (splat) kullanıyoruz ve gelen path'i manuel ayıklayacağız.
        get("/nosql-lab-rd/*", (req, res) -> {
            String fullPath = req.splat()[0]; // * ile gelen tüm path kısmını alır
            String studentNo = extractStudentNo(fullPath); // student_no=xxxxxx kısmını ayrıştır
            System.out.println("Redis endpoint çağrıldı: " + studentNo);
            res.type("application/json");
            return gson.toJson(RedisStore.get(studentNo)); // studentNo'yu get metoduna veriyoruz
        });

        get("/nosql-lab-hz/*", (req, res) -> {
            String fullPath = req.splat()[0];
            String studentNo = extractStudentNo(fullPath);
            System.out.println("Hazelcast endpoint çağrıldı: " + studentNo);
            res.type("application/json");
            return gson.toJson(HazelcastStore.get(studentNo));
        });

        get("/nosql-lab-mon/*", (req, res) -> {
            String fullPath = req.splat()[0];
            String studentNo = extractStudentNo(fullPath);
            System.out.println("MongoDB endpoint çağrıldı: " + studentNo);
            res.type("application/json");
            return gson.toJson(MongoStore.get(studentNo));
        });

        // /hello endpoint'i aynı kalacak
        get("/hello", (req, res) -> {
            System.out.println("/hello endpoint çağrıldı!");
            res.type("text/plain");
            return "Merhaba Spark!";
        });

        System.out.println("Spark sunucusu başlatıldı ve dinlemede...");
    }

    // URL'den student_no değerini çeken yardımcı metot
    private static String extractStudentNo(String path) {
        if (path != null && path.startsWith("student_no=")) {
            return path.substring("student_no=".length());
        }
        return null; // veya hata fırlatabiliriz
    }

    // Veritabanlarına 10.000 rastgele kayıt ekleyen metot
    private static void initDatabases(Gson gson) {
        System.out.println("Veritabanlarına rastgele 10.000 kayıt ekleniyor...");
        Random random = new Random();
        AtomicLong studentNoCounter = new AtomicLong(2025000000L); // Başlangıç öğrenci numarası

        // Redis, Hazelcast, MongoDB init metotlarını çağıralım
        // Bu metotları daha sonra her Store sınıfının içine taşıyacağız
        RedisStore.init();
        HazelcastStore.init();
        MongoStore.init();

        for (int i = 0; i < NUM_STUDENTS; i++) {
            String studentNo = String.valueOf(studentNoCounter.incrementAndGet());
            String name = NAMES[random.nextInt(NAMES.length)];
            String department = DEPARTMENTS[random.nextInt(DEPARTMENTS.length)];
            Student student = new Student(studentNo, name, department);

            // Student objesini String/JSON olarak kaydetmemiz gerekiyor
            String studentJson = gson.toJson(student);

            RedisStore.put(studentNo, studentJson);
            HazelcastStore.put(studentNo, studentJson);
            MongoStore.put(student); // MongoDB Student objesini direkt alabilir
        }
        System.out.println("10.000 kayıt başarıyla veritabanlarına eklendi.");
    }
}