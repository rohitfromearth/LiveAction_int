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




    public Boolean encryptFile(String inputFilepath, String outputFilepath) {
        try {
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
