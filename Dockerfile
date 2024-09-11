# Menggunakan gambar Java resmi
FROM openjdk:17-jdk-alpine

# Menentukan direktori kerja di dalam kontainer
WORKDIR /app

COPY build/libs/product-service-0.0.1-SNAPSHOT.jar product-service.jar

# Jalankan aplikasi di port 5000
EXPOSE 5000

# Menjalankan aplikasi
ENTRYPOINT ["java","-jar","product-service.jar"]


