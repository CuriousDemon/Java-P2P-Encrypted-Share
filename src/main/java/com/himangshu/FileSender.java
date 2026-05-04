package com.himangshu;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.net.*;

public class FileSender {
    // Change the method signature to include 'String targetIp'
    public static void sendFile(String filePath, String targetIp) {
        File file = new File(filePath);
        if (!file.exists()) return;

        try {
            Cipher cipher = EncryptionUtils.getCipher(Cipher.ENCRYPT_MODE);

            // Replace "localhost" with the targetIp variable
            try (Socket socket = new Socket(targetIp, 5000);
                 FileInputStream fis = new FileInputStream(file);
                 CipherOutputStream cos = new CipherOutputStream(socket.getOutputStream(), cipher);
                 DataOutputStream dos = new DataOutputStream(cos)) {

                dos.writeUTF(file.getName());
                dos.writeLong(file.length());

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
                System.out.println("[SENDER] Sent to " + targetIp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
