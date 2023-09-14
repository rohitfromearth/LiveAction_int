package com.ext.liveaction_int;

import android.os.Build;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypti {
    private static final String SECRET_KEY = "my_super_secret_key_ho_ho_ho";
    private static final String SALT = "ssshhhhhhhhhhh!!!!";

    public String enrycpp(String strToEncrypt) {

        try {
            // Create default byte array
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            // Create SecretKeyFactory object
            SecretKeyFactory factory
                    = SecretKeyFactory.getInstance(
                    "PBKDF2WithHmacSHA256");

            // Create KeySpec object and assign with
            // constructor\
            KeySpec spec = new PBEKeySpec(
                    SECRET_KEY.toCharArray(), SALT.getBytes(),
                    65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(
                    tmp.getEncoded(), "AES");


            Cipher cipher = Cipher.getInstance(
                    "AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,
                    ivspec);
            // Return encrypted string
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(
                        cipher.doFinal(strToEncrypt.getBytes()));
            }
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public Boolean encryptFile(String inputFilepath, String outputFilepath) {
        try {
            // Transmit the encodedKey to your client
            // Decode the Base64 string back to a byte array
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                final String KEY = "0123456789abcdef"; // 16-byte key (128-bit)
                final String IV = "abcdef0123456789";

                SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
                IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

                FileInputStream inputStream = new FileInputStream(inputFilepath);
                FileOutputStream outputStream = new FileOutputStream(outputFilepath);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    cipherOutputStream.write(buffer, 0, bytesRead);
                }
                cipherOutputStream.close();
                outputStream.close();
                inputStream.close();
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
