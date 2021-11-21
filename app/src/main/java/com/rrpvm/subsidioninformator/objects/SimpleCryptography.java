package com.rrpvm.subsidioninformator.objects;

//думаю,можно обчислить хеш-сумму строки и от этого менять xor_key. В дб передавать (ключ - 1)
public class SimpleCryptography {
    //encryption
    public String encrypt(String password)
    {
        String enc_string = new String();
        for(char c : password.toCharArray()){
            c = (char)(c^XOR_KEY);
            enc_string += c;
        }
        return enc_string;
    }
    public static final short XOR_KEY = 42;//phylosophy question required
}
