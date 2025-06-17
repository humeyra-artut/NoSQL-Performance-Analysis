package app.store;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap; // Buradaki import satırını düzelttik!

public class HazelcastStore {
    private static HazelcastInstance client;
    private static IMap<String, String> studentsMap; // String olarak tutalım

    public static void init() {
        if (client == null) {
            try {
                ClientConfig clientConfig = new ClientConfig();
                // Varsayılan Hazelcast portu 5701
                clientConfig.getNetworkConfig().addAddress("localhost:5701");
                client = HazelcastClient.newHazelcastClient(clientConfig);
                studentsMap = client.getMap("students"); // "students" adında bir map alalım
                System.out.println("Hazelcast'e bağlandı.");
            } catch (Exception e) {
                System.err.println("Hazelcast bağlantı hatası: " + e.getMessage());
            }
        }
    }

    public static void put(String key, String value) {
        if (studentsMap != null) {
            studentsMap.put(key, value);
        } else {
            System.err.println("Hazelcast bağlı değil, veri eklenemedi.");
        }
    }

    public static String get(String key) {
        if (studentsMap != null) {
            return studentsMap.get(key);
        }
        System.err.println("Hazelcast bağlı değil, veri çekilemedi.");
        return null;
    }

    public static void close() {
        if (client != null) {
            client.shutdown();
            System.out.println("Hazelcast bağlantısı kapatıldı.");
        }
    }
}