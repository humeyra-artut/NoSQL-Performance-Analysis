# NoSQL Performans Analizi ve REST Servisi

Redis, Hazelcast ve MongoDB kullanılarak geliştirilen NoSQL tabanlı bir REST servis uygulamasıdır.

Bu projede öğrenci verileri üç farklı NoSQL teknolojisinde saklanmakta ve öğrenci numarası üzerinden REST endpoint'leri aracılığıyla sorgulanmaktadır. Ayrıca Redis, Hazelcast ve MongoDB'nin performanslarının karşılaştırılması amaçlanmıştır.

## 🛠️ Kullanılan Teknolojiler

- Java
- Maven
- Redis
- Hazelcast
- MongoDB
- REST API
- JSON
- Apache Siege

## 🎯 Projenin Amacı

Bu proje kapsamında aynı veri yapısının farklı NoSQL teknolojileri kullanılarak saklanması ve sorgulanması gerçekleştirilmiştir.

Projenin temel amaçları:

- Redis kullanarak veri saklama ve sorgulama
- Hazelcast kullanarak veri saklama ve sorgulama
- MongoDB kullanarak veri saklama ve sorgulama
- REST endpoint'leri geliştirme
- JSON veri formatını kullanma
- Farklı NoSQL teknolojilerinin performanslarını karşılaştırma
- Eş zamanlı isteklerle performans testi gerçekleştirme

## 🗄️ Veri Modeli

Projede öğrenci kayıtları kullanılmaktadır.

Her kayıt aşağıdaki alanlardan oluşmaktadır:

```json
{
  "student_no": "2025000001",
  "name": "Student Name",
  "department": "Computer Engineering"
}
```
Öğrenci numarası kayıtların sorgulanmasında kullanılmaktadır.
🏗️ Proje Yapısı
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

Sınıfların Görevleri
- Main.java → Uygulamanın başlangıç ve servis yapısı
- Student.java → Öğrenci veri modelini temsil eder
- RedisStore.java → Redis veri işlemlerini gerçekleştirir
- HazelcastStore.java → Hazelcast veri işlemlerini gerçekleştirir
- MongoStore.java → MongoDB veri işlemlerini gerçekleştirir
🔌 REST API Endpoint'leri
Uygulamada üç farklı veri depolama teknolojisi için ayrı endpoint bulunmaktadır.
🔴 Redis
GET /nosql-lab-rd/student_no={student_no}

Örnek:
http://localhost:8080/nosql-lab-rd/student_no=2025000001

Bu endpoint öğrenci kaydını doğrudan Redis üzerinden getirir.
🟢 Hazelcast
GET /nosql-lab-hz/student_no={student_no}

Örnek:
http://localhost:8080/nosql-lab-hz/student_no=2025000001

Bu endpoint öğrenci kaydını doğrudan Hazelcast üzerinden getirir.
🟡 MongoDB
GET /nosql-lab-mon/student_no={student_no}

Örnek:
http://localhost:8080/nosql-lab-mon/student_no=2025000001

Bu endpoint öğrenci kaydını doğrudan MongoDB üzerinden getirir.
📊 Performans Testleri
Redis, Hazelcast ve MongoDB endpoint'lerinin performanslarını karşılaştırmak amacıyla Apache Siege kullanılmıştır.
Test senaryosunda:
- 1000 toplam HTTP isteği
- 10 eş zamanlı istemci
- Her istemciden 100 istek
- JSON response
- Redis, Hazelcast ve MongoDB karşılaştırması
kullanılmaktadır.
Apache Siege Kurulumu
Ubuntu/Linux ortamında:
sudo apt-get install siege

🔴 Redis Performans Testi
siege -H "Accept: application/json" -c10 -r100 \
"http://localhost:8080/nosql-lab-rd/student_no=2025000001" \
> ~/redis-siege.results

🟢 Hazelcast Performans Testi
siege -H "Accept: application/json" -c10 -r100 \
"http://localhost:8080/nosql-lab-hz/student_no=2025000001" \
> ~/hz-siege.results

🟡 MongoDB Performans Testi
siege -H "Accept: application/json" -c10 -r100 \
"http://localhost:8080/nosql-lab-mon/student_no=2025000001" \
> ~/mongodb-siege.results

⚙️ Siege Parametreleri
Parametre	Açıklama
-H	HTTP header gönderir
-c10	10 eş zamanlı istemci
-r100	Her istemci 100 istek gönderir
URL	Test edilecek REST endpoint'i


Bu yapı ile:
10 istemci × 100 istek = 1000 toplam istek
gerçekleştirilir.
⏱️ Çalışma Süresi Testi
Endpoint'lerin çalışma sürelerini karşılaştırmak için eş zamanlı curl istekleri kullanılabilir.
Redis
time seq 1 100 | xargs -n1 -P10 -I{} \
curl -s "http://localhost:8080/nosql-lab-rd/student_no=2025000001" \
> ~/redis-time.results

Hazelcast
time seq 1 100 | xargs -n1 -P10 -I{} \
curl -s "http://localhost:8080/nosql-lab-hz/student_no=2025000001" \
> ~/hz-time.results

MongoDB
time seq 1 100 | xargs -n1 -P10 -I{} \
curl -s "http://localhost:8080/nosql-lab-mon/student_no=2025000001" \
> ~/mongodb-time.results

📈 Performans Sonuçları
Performans testlerinde aşağıdaki metrikler karşılaştırılabilir:
- Transactions
- Availability
- Elapsed time
- Data transferred
- Response time
- Transaction rate
- Throughput
- Concurrency
- Successful transactions
- Failed transactions
Not: Gerçek test sonuçları elde edildiğinde bu bölüme eklenmelidir. Örnek veya varsayımsal değerler gerçek performans sonucu olarak gösterilmemiştir.

🚀 Kurulum
Projeyi çalıştırmak için:
1. Repository'yi klonlayın.
2. Java ve Maven ortamını hazırlayın.
3. Redis'i çalıştırın.
4. Hazelcast'i çalıştırın.
5. MongoDB'yi çalıştırın.
6. Projenin Maven bağımlılıklarını yükleyin.
7. Uygulamayı çalıştırın.
8. REST endpoint'lerini test edin.
🔎 Test
Endpoint'ler çalıştırıldıktan sonra Redis, Hazelcast ve MongoDB için ayrı ayrı istek gönderilebilir.
Redis
http://localhost:8080/nosql-lab-rd/student_no=2025000001

Hazelcast
http://localhost:8080/nosql-lab-hz/student_no=2025000001

MongoDB
http://localhost:8080/nosql-lab-mon/student_no=2025000001

💡 Kazanımlar
Bu proje kapsamında aşağıdaki konularda uygulamalı çalışma yapılmıştır:
- NoSQL veri tabanları
- Redis
- Hazelcast
- MongoDB
- REST API
- Java
- Maven
- JSON veri işleme
- Performans testi
- Eş zamanlı HTTP istekleri
- Veri tabanı performans karşılaştırması
👩‍💻 Geliştirici
Hümeyra Artut
Computer Engineer
GitHub
📌 Proje Durumu
Bu proje eğitim ve uygulama amacıyla geliştirilmiştir. NoSQL veri tabanları ve performans karşılaştırması üzerine çalışmayı içermektedir.

**Önemli:** En sondaki ` ``` ` işareti de README'nin içinde olacak. Onu da kopyala.

Sonra **Commit changes** yap.
