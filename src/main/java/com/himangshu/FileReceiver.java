package com.himangshu;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.*;
import java.net.*;

public class FileReceiver {
    public static void receiveFile() {
        // Port 5000 is our dedicated "listening" port
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("[RECEIVER] Secure Server is ONLINE and waiting...");

            while (!Thread.currentThread().isInterrupted()) {
                // Wait for an incoming connection
                try (Socket socket = serverSocket.accept()) {

                    // FIX: Initialize the Cipher OUTSIDE of the try-with-resources parentheses
                    // because Cipher does not implement AutoCloseable.
                    Cipher decryptCipher = EncryptionUtils.getCipher(Cipher.DECRYPT_MODE);

                    // Now wrap the socket's input stream with our decryption engine
                    try (CipherInputStream cis = new CipherInputStream(socket.getInputStream(), decryptCipher);
                         DataInputStream dis = new DataInputStream(cis)) {

                        // 1. Read Metadata (Sent by FileSender)
                        String fileName = dis.readUTF();
                        long fileSize = dis.readLong();

                        System.out.println("[RECEIVER] Receiving encrypted file: " + fileName + " (" + fileSize + " bytes)");

                        // 2. Prepare the local file for writing
                        File receivedFile = new File("decrypted_" + fileName);
                        try (FileOutputStream fos = new FileOutputStream(receivedFile)) {

                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            long totalReceived = 0;

                            // 3. Receive and decrypt the data in 4KB chunks
                            while (totalReceived < fileSize && (bytesRead = dis.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                                totalReceived += bytesRead;
                            }
                        }

                        System.out.println("[RECEIVER] File successfully decrypted and saved: " + receivedFile.getName());
                    }
                } catch (Exception e) {
                    System.err.println("[RECEIVER ERROR] Failed to process incoming file: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[RECEIVER ERROR] Could not start server on port 5000: " + e.getMessage());
        }
    }
}
