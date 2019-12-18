package controllers;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;

import java.util.Random;

import dataaccess.sqlserver.SqlServerDatabase;

public class BlowfishController {

    public String generateKey() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            if (random.nextInt(2) == 1) {
                key.append(generateRandomDigit());
            } else {
                key.append(generateRandomLetter());

            }
        }
        return key.toString();
    }

    public char generateRandomLetter() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        return alphabet.charAt(random.nextInt(25));

    }

    public char generateRandomDigit() {
        String digits = "0123456789";
        Random random = new Random();
        return digits.charAt(random.nextInt(9));
    }

    //Source -  http://javaprogramming.language-tutorial.com/2012/10/encryption-using-bouncy-castle-api.html
    public String encrypt(String plainText, String encryptionKey) {
        BlowfishEngine engine = new BlowfishEngine();
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(engine);
        KeyParameter key = new KeyParameter(encryptionKey.getBytes());
        cipher.init(true, key);
        byte in[] = plainText.getBytes();
        byte out[] = new byte[cipher.getOutputSize(in.length)];
        int len1 = cipher.processBytes(in, 0, in.length, out, 0);
        try {
            cipher.doFinal(out, len1);
        } catch (CryptoException e) {
            e.printStackTrace();


        }
        String cipherText = new String(Base64.encode(out));
        return cipherText;

    }

    public String decrypt(String cipherText, String encrpytionKey) {
        StringBuffer plainText = new StringBuffer();
        BlowfishEngine engine = new BlowfishEngine();
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(engine);
        KeyParameter key = new KeyParameter(encrpytionKey.getBytes());
        cipher.init(false, key);
        byte out[] = Base64.decode(cipherText);
        byte out2[] = new byte[cipher.getOutputSize(out.length)];
        int len2 = cipher.processBytes(out, 0, out.length, out2, 0);
        try {
            cipher.doFinal(out2, len2);
        } catch (CryptoException e) {
        }
        String s2 = new String(out2);
        for (int i = 0; i < s2.length(); i++) {
            char c = s2.charAt(i);
            if (c != 0) {
                plainText.append(c);

            }

        }
        return plainText.toString();

    }

    public boolean insertBlowfishKeyInSqlServerDatabase(String encrpytionKey, String email){
        SqlServerDatabase ssd = new SqlServerDatabase();
        try {
            ssd.executeUpdateStatement("insert into [dbo].[Blowfish] (BlowfishKey,User_UserId) values ('" + encrpytionKey + "',\n" +
                    "(select [dbo].[User].UserId from [dbo].[User] where [dbo].[User].Email = '" + email + "')\n" +
                    ");");
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public String getBlowfishKeyFromSqlServerDatabase(String userId){
        SqlServerDatabase ssd = new SqlServerDatabase();
        return ssd.executeSelectStatement("select [dbo].[Blowfish].[BlowfishKey] from [dbo].[Blowfish] where User_UserId="+userId+";", SqlServerDatabase.returns.String);

    }

    public boolean checkIfPasswordExistsInDatabase(String encryptedPassword){
        return true;

    }



}
