# DistributedStorageSystem

A **distributed storage system** built with **Spring Boot** and **MongoDB**, designed to store, distribute, and retrieve files efficiently across multiple database servers using consistent hashing.

### 🚀 Features
- Accepts **multipart files** (e.g., PDF, JPG, etc.).
- **Splits files into chunks** and distributes them across multiple MongoDB servers.
- Stores **metadata** (chunk IDs, sequence, and storage location) in a metadata database.
- Retrieves and merges chunks to reconstruct the original file.
- Implements **consistent hashing** for load balancing across multiple storage nodes.

### 🛠️ Technologies Used
- **Java (SpringBoot)**
- **MongoDB**
- **Consistent Hashing Algorithm**
- **Multipart File Handling**

### 📂 Project Structure
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

### ⚙️ Project's Design Diagram

#### ConsistentHashing
<img width="800" alt="Screenshot 2025-03-13 at 4 51 04 PM" src="https://github.com/user-attachments/assets/0cec46be-fb95-4391-a9fd-3f83ceb2c8b2" />
#### ModelStructure
<img width="948" alt="Screenshot 2025-03-13 at 5 13 34 PM" src="https://github.com/user-attachments/assets/7b26cc4d-38e1-4a4f-8ca8-dcee3cebf28d" />
#### Storing Files
<img width="1152" alt="Screenshot 2025-03-13 at 4 51 41 PM" src="https://github.com/user-attachments/assets/9b7965f4-e2c7-47bb-bb0a-8065720e18e4" />
#### Fetching Files
<img width="953" alt="Screenshot 2025-03-13 at 4 51 47 PM" src="https://github.com/user-attachments/assets/d28e5811-aa23-4bcd-9adf-e5a7ced545e8" />

### ⚙️ Exposed APIs

1. `{{baseUrl}}/dss/store` → Attach multipart file as `RequestBody` in the request  
2. `{{baseUrl}}/dss/fetch` → Receive as downloadable file

### 🏗️ Setup & Installation
### Prerequisites
- Ensure **Java+** on your system. Install **MongoDB** and **MongoDB Compass** for storing data and graphically visualization. (set up multiple instances for distributed storage)
- **Clone the repository**
- **Build & Run the Project** (Use `./gradlew clean build`)




