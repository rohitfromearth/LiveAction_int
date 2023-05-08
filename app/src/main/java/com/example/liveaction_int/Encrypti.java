package com.example.liveaction_int;

import android.os.Build;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypti {
    private static final String SECRET_KEY = "my_super_secret_key_ho_ho_ho";

    private static final String SALT = "ssshhhhhhhhhhh!!!!";


        public String enrycpp(String strToEncrypt){

            try {
//Log.e("string",strToEncrypt);
                // Create default byte array
                byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0 };
                IvParameterSpec ivspec= new IvParameterSpec(iv);

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
//                Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");  ///26-april-change
                cipher.init(Cipher.ENCRYPT_MODE, secretKey,
                        ivspec);
                // Return encrypted string
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
                    return Base64.getEncoder().encodeToString(
                            cipher.doFinal(strToEncrypt.getBytes()));

                }
            }
            catch (Exception e) {
                System.out.println("Error while encrypting: "+ e.toString());
            }
            return null;


    }
    public Boolean encryptFile(String inputFilepath, String outputFilepath) {
        try {


// Transmit the encodedKey to your client

            // Decode the Base64 string back to a byte array

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                final String SALT = "ssshhhhhhhhhhh!!!!";
                final String PASSWORD = "myconstantkey1234";
                final int ITERATIONS = 10000;
                final int KEY_LENGTH = 128;
                FileInputStream fis = new FileInputStream(inputFilepath);

                byte[] salt = SALT.getBytes();

                KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                byte[] decodedKey = factory.generateSecret(spec).getEncoded();

                FileOutputStream fos = new FileOutputStream(outputFilepath);
                SecretKeySpec keySpec = new SecretKeySpec(decodedKey, "AES");
                byte[] iv = new byte[16];
                Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
                CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, bytesRead);
                }
                cos.flush();
                cos.close();
                fis.close();
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
