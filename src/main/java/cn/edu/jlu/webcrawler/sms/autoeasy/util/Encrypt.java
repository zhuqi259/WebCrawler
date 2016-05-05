package cn.edu.jlu.webcrawler.sms.autoeasy.util;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Encrypt
{
    private static byte[] IV = "k9589Iau".getBytes(Charset.forName("UTF-8"));
    private static byte[] KEY = "mrXn5pHX".getBytes(Charset.forName("UTF-8"));

    public static String decrypt(String paramString)
            throws Exception
    {
        SecureRandom localSecureRandom = new SecureRandom();
        Object localObject = new DESKeySpec(KEY);
        localObject = SecretKeyFactory.getInstance("DES").generateSecret((KeySpec)localObject);
        Cipher localCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        localCipher.init(2, (Key)localObject, new IvParameterSpec(IV), localSecureRandom);
        return new String(localCipher.doFinal(Base64.decode(paramString.toCharArray())), "UTF-8");
    }

    public static String encrypt(String paramString)
            throws Exception
    {
        SecureRandom localSecureRandom = new SecureRandom();
        Object localObject = new DESKeySpec(KEY);
        localObject = SecretKeyFactory.getInstance("DES").generateSecret((KeySpec)localObject);
        Cipher localCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        localCipher.init(1, (Key)localObject, new IvParameterSpec(IV), localSecureRandom);
        return new String(Base64.encode(localCipher.doFinal(paramString.getBytes(Charset.forName("UTF-8")))));
    }
}
