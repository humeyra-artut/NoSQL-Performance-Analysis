package app.store;

import app.model.Student;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.time.Duration; // !!! Yeni import: java.time.Duration ekledik

public class RedisStore {
    private static JedisPool jedisPool;
    private static final Gson gson = new Gson();

    public static void init() {
        if (jedisPool == null) {
            try {
                JedisPoolConfig poolConfig = new JedisPoolConfig();
                poolConfig.setMaxTotal(100);
                poolConfig.setMaxIdle(20);
                poolConfig.setMinIdle(5);
                poolConfig.setTestOnBorrow(true);
                poolConfig.setTestOnReturn(true);
                poolConfig.setTestWhileIdle(true);

                // HATA VEREN SATIRLARI DÜZELTİYORUZ
                // int yerine Duration.ofMillis() kullanıyoruz
                poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(180000)); // 3 dakika (ms)
                poolConfig.setMinEvictableIdleTime(Duration.ofMillis(60000));   // 1 dakika (ms)

                // JedisPool'u başlatıyoruz
                // Burada da timeout parametresini Duration olarak verebiliriz, ama int de çoğu versiyonda kabul edilir.
                // Şimdilik 2000 ms olarak bırakalım, eğer yine hata verirse burayı da Duration yaparız.
                jedisPool = new JedisPool(poolConfig, "localhost", 6379, 2000); // 2000ms timeout

                try (Jedis jedis = jedisPool.getResource()) {
                    System.out.println("Redis bağlantı havuzu oluşturuldu ve test edildi: " + jedis.ping());
                }
            } catch (Exception e) {
                System.err.println("!!! Redis bağlantı havuzu başlatma hatası: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void put(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        } catch (JedisConnectionException e) {
            System.err.println("!!! Redis bağlantısı kesildi, put işlemi başarısız (Key: " + key + "): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("!!! Redis veri ekleme hatası (Key: " + key + "): " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Student get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(key);
            if (json != null) {
                System.out.println(">>> Redis'ten veri çekildi: Key=" + key);
                try {
                    return gson.fromJson(json, Student.class);
                } catch (JsonSyntaxException e) {
                    System.err.println("!!! Redis'ten çekilen JSON dönüşüm hatası (Key: " + key + "): " + e.getMessage());
                    System.err.println("!!! JSON içeriği: " + json);
                    e.printStackTrace();
                    return null;
                }
            } else {
                System.out.println("!!! Redis'te '" + key + "' anahtarı bulunamadı.");
                return null;
            }
        } catch (JedisConnectionException e) {
            System.err.println("!!! Redis bağlantısı kesildi, get işlemi başarısız (Key: " + key + "): " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("!!! Redis veri çekme hatası (Key: " + key + "): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void close() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.destroy();
            System.out.println("Redis bağlantı havuzu kapatıldı.");
        }
    }
}