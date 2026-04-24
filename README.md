# SecureDrop-P2P 🛡️

A high-performance, peer-to-peer file sharing application built in Java. This tool allows users to send files directly between computers on a local network with **End-to-End Encryption (E2EE)**.

## 🚀 Features
- **P2P Architecture:** No central server; computers communicate directly via Sockets.
- **AES-128 Encryption:** All data streams are encrypted using the Java Cryptography Architecture (JCA).
- **Chunked Streaming:** Efficiently handles large files by processing data in 4KB buffers.
- **Multi-Instance Support:** Designed to run both Sender and Receiver roles simultaneously for local testing.

## 🛠️ Tech Stack
- **Language:** Java 17+
- **Build Tool:** Maven
- **Security:** AES (Advanced Encryption Standard)
- **Networking:** TCP/IP Sockets

## 📐 How it Works
1. **The Handshake:** The Sender connects to the Receiver's IP on port 5000.
2. **Metadata Exchange:** The Sender transmits the filename and file size in an encrypted format.
3. **The Secure Stream:** The file is read from the disk, passed through a `CipherOutputStream`, and poured into the network socket.
4. **The Reconstruction:** The Receiver uses a `CipherInputStream` to decrypt the bytes in real-time and save them to the disk.



## 🚦 Getting Started
### Prerequisites
- Java 17 or higher
- Maven installed

### Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/CuriousDemon/Java-P2P-Encrypted-Share.git](https://github.com/CuriousDemon/Java-P2P-Encrypted-Share.git)
