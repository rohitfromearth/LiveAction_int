package com.example.liveaction_int;

import android.os.Build;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
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
                System.out.println("Error while encrypting: "
                        + e.toString());
            }
            return null;


    }
}
