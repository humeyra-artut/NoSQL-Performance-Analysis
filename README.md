# NoSQL Performans Analizi ve REST Servisi

Redis, Hazelcast ve MongoDB kullanılarak geliştirilen NoSQL tabanlı bir REST servis uygulamasıdır.

Bu projede aynı öğrenci verileri üç farklı NoSQL teknolojisinde tutulmakta ve öğrenci numarası üzerinden REST endpoint'leri aracılığıyla sorgulanmaktadır. Ayrıca veri depolama teknolojilerinin performanslarının karşılaştırılması amaçlanmıştır.

## 🛠️ Kullanılan Teknolojiler

- Java
- Maven
- Redis
- Hazelcast
- MongoDB
- REST API
- JSON
- Apache Siege

## 📁 Proje Yapısı

```text
src/
└── main/
    └── java/
        └── app/
            ├── Main.java
            ├── model/
            │   └── Student.java
            └── store/
                ├── RedisStore.java
                ├── HazelcastStore.java
                └── MongoStore.java
