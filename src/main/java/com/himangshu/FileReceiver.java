package com.himangshu;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.*;
import java.net.*;

public class FileReceiver {
    public static void receiveFile() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("[RECEIVER] Waiting for secure file...");

            // 1. Prepare the Cipher for DECRYPT mode
            Cipher cipher = EncryptionUtils.getCipher(Cipher.DECRYPT_MODE);

            try (Socket socket = serverSocket.accept();
                 // 2. Wrap the socket input stream with the cipher
                 CipherInputStream cis = new CipherInputStream(socket.getInputStream(), cipher);
                 DataInputStream dis = new DataInputStream(cis)) {

                // 3. Read Metadata
                String fileName = dis.readUTF();
                long fileSize = dis.readLong();

                System.out.println("[RECEIVER] Connected! Receiving: " + fileName);

                // 4. Save the file
                FileOutputStream fos = new FileOutputStream("decrypted_" + fileName);
                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalReceived = 0;

                while (totalReceived < fileSize && (bytesRead = dis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                    totalReceived += bytesRead;
                }

                fos.close();
                System.out.println("[RECEIVER] Securely saved as: decrypted_" + fileName);
            }
        } catch (Exception e) {
            System.err.println("[RECEIVER ERROR] Decryption failed or connection lost.");
            e.printStackTrace();
        }
    }
}
