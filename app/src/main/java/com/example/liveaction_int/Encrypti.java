package com.example.liveaction_int;

import android.os.Build;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
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

//            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//            keyGen.init(128);
//            SecretKey key = keyGen.generateKey();
//
//            // Get the raw bytes of the key
//            final byte[] KEY  = key.getEncoded();
//            Log.e("datafeed", String.valueOf(KEY));


// Convert the key to Base64 encoding
//            String encodedKey = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                encodedKey = Base64.getEncoder().encodeToString(rawKey);
//            }

// Transmit the encodedKey to your client

            // Decode the Base64 string back to a byte array

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                final String PASSWORD = "myconstantkey1234";
               final int ITERATIONS = 10000;
                 final int KEY_LENGTH = 128; // in bits

                SecureRandom random = new SecureRandom();
                byte[] salt = new byte[16];
                random.nextBytes(salt);

                // Derive the key using PBKDF2 with the constant password and salt
                KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                byte[] decodedKey = factory.generateSecret(spec).getEncoded();
                Log.e("datafeed", String.valueOf(decodedKey));

//                byte[] decodedKey = new byte[0];
//                decodedKey = Base64.getDecoder().decode("[B@e70801c");


                // Create a cipher
                Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
                SecretKeySpec keySpec = new SecretKeySpec(decodedKey, "AES");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);

                // Open the input and output files
                FileInputStream in = new FileInputStream(inputFilepath);
                FileOutputStream out = new FileOutputStream(outputFilepath);

                // Encrypt the data
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    byte[] encrypted = cipher.update(buffer, 0, bytesRead);
                    out.write(encrypted);
                }
                byte[] finalEncrypted = cipher.doFinal();
                out.write(finalEncrypted);

                // Close the input and output files
                in.close();
                out.close();
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
