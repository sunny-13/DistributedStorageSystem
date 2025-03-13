# Distributed Storage System

A **distributed storage system** built with **Spring Boot** and **MongoDB**, designed to store, distribute, and retrieve files efficiently across multiple servers using consistent hashing.

## 🚀 Features
- Accepts **multipart files** (e.g., PDF, JPG, etc.).
- **Splits files into chunks** and distributes them across multiple MongoDB servers.
- Stores **metadata** (chunk IDs, sequence, and storage location) in a metadata database.
- Retrieves and merges chunks to reconstruct the original file.
- Implements **consistent hashing** for load balancing across storage nodes.

## 🛠️ Technologies Used
- **Java 17** with **Spring Boot**
- **MongoDB** (distributed across multiple servers)
- **Spring Data MongoDB**
- **Consistent Hashing Algorithm**
- **Multipart File Handling**

## 📂 Project Structure
``` 
distributed-storage-system/
│── src/
│ ├──main/java/com/example/distributedstorage/
│ │ ├── controller/ # REST controllers for file operations (Storing & Fetching files)
│ │ ├── service/ # Core logic for chunking, storage, and retrieval
│ │ ├── model/ # Data models (chunkData, chunkMetadata & dtos)
│ │ ├── repository/ # MongoDB templates handling
│ │ ├── config/ # Configuration files for MongoDB multiple instances handling
│ ├── resources/
│ │ ├── application.properties # configurations data
│── build.gradle # Dependencies (Spring Boot, MongoDB, etc.)
```
