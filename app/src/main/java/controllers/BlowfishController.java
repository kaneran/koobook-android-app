package controllers;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;

import java.util.Random;

import dataaccess.sqlserver.SqlServerDatabase;

public class BlowfishController {

    BlowfishEngine engine;
    SqlServerDatabase ssd;
    Random random;

    public BlowfishController() {
        engine = new BlowfishEngine();
        ssd = new SqlServerDatabase();
        random = new Random();
    }

    //This method randomly generates a cipher key of char length 20
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

    //Returns random letter to be appended to the currently generated cipher key
    public char generateRandomLetter() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        return alphabet.charAt(random.nextInt(25));

    }

    //Returns random number to be appended to the currently generated cipher key
    public char generateRandomDigit() {
        String digits = "0123456789";
        return digits.charAt(random.nextInt(9));
    }

    //Source -  http://javaprogramming.language-tutorial.com/2012/10/encryption-using-bouncy-castle-api.html for the blowfish implementation
    //This method works by initialising the engine, cipher and the key. It then proceeds to convert the plain text to a byte array and then the cipher is used to encrypt the plain text
    //to cipher text which is encoded using Base64 and returned from the method
    public String encrypt(String plainText, String encryptionKey) {
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

    //The initialisation process remaining the same for the engine, key and cipher. However, this time, the cipher text which is taken from the methods argument is decoded into Base64
    //The cipher then transforms the decoded cipher text into plaintext and this output is used to create a new string. Then, each character in this string is checked to see
    //if it equals 0. If it doesn't then the string builder will append this character. After iterating through all the characters, the output from the string builder is essentially
    //the plain text which is returned from the method
    public String decrypt(String cipherText, String encrpytionKey) {
        StringBuffer plainText = new StringBuffer();
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

    //Executes an update to the SQL server database to insert a new record into the Blowfish table where the encryption key will be that of the encryption
    //from this method's arugment. The other arugment which is the email is used retrieve the userId from the User table. The retrieved userId will be foreign key
    //from the Blowfish table to the User table
    public boolean insertBlowfishKeyInSqlServerDatabase(String encrpytionKey, String email){
        try {
            ssd.executeUpdateStatement("insert into [dbo].[Blowfish] (BlowfishKey,User_UserId) values ('" + encrpytionKey + "',\n" +
                    "(select [dbo].[User].UserId from [dbo].[User] where [dbo].[User].Email = '" + email + "')\n" +
                    ");");
            return true;
        } catch(Exception e){
            return false;
        }
    }

    //Executes select query to the SQL server datbase to get the blowfish key from the Blowfish where the foreign key to the User table equals the userId from this method's
    //argument
    public String getBlowfishKeyFromSqlServerDatabase(String userId){
        return ssd.executeSelectStatement("select [dbo].[Blowfish].[BlowfishKey] from [dbo].[Blowfish] where User_UserId="+userId+";", SqlServerDatabase.Returns.String);

    }

}
