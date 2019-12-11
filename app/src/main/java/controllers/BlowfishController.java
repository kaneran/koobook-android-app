package controllers;

import java.util.Random;

public class BlowfishController {

    public String generateKey(){
        String key = "";
        Random random = new Random();

        for(int i =0; i <20; i++){
            if(random.nextInt(2) == 1){
                key = key + generateRandomDigit();
            } else{
                key = key + generateRandomLetter();

            }
        }
        return key;
    }

    public char generateRandomLetter(){
        String alphabet  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        return alphabet.charAt(random.nextInt(25));

    }

    public char generateRandomDigit(){
        String digits = "0123456789";
        Random random = new Random();
        return digits.charAt(random.nextInt(9));
    }


}
