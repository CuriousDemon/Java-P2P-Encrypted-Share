package com.himangshu;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.net.*;

public class FileSender {
    public static void sendFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found!");
            return;
        }

        try {
            // 1. Prepare the Cipher for ENCRYPT mode
            Cipher cipher = EncryptionUtils.getCipher(Cipher.ENCRYPT_MODE);

            try (Socket socket = new Socket("localhost", 5000);
                 FileInputStream fis = new FileInputStream(file);
                 // 2. Wrap the socket output stream with the cipher
                 CipherOutputStream cos = new CipherOutputStream(socket.getOutputStream(), cipher);
                 DataOutputStream dos = new DataOutputStream(cos)) {

                System.out.println("[SENDER] Encrypting and sending: " + file.getName());

                // 3. Send Metadata
                dos.writeUTF(file.getName());
                dos.writeLong(file.length());

                // 4. Send File Data in 4KB chunks
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }

                System.out.println("[SENDER] Finished sending encrypted file!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
