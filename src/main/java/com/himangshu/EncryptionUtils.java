package com.himangshu;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class EncryptionUtils {
    // We are using AES (Advanced Encryption Standard)
    private static final String ALGORITHM = "AES";

    // AES-128 requires a 16-character key.
    // This is the "password" that locks and unlocks your files.
    private static final String SECRET_KEY = "MySuperSecretKey";

    /**
     * Converts our String password into a Java Key object.
     */
    private static Key getKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
    }

    /**
     * Creates a Cipher object.
     * @param mode Use Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
     */
    public static Cipher getCipher(int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, getKey());
        return cipher;
    }
}
