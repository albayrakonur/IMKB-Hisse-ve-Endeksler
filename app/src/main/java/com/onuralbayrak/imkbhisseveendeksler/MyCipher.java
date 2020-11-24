package com.onuralbayrak.imkbhisseveendeksler;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MyCipher {

    private byte[] key;
    private byte[] iv;
    private String period;

    public MyCipher(byte[] key, byte[] iv, String period) {
        this.key = key;
        this.iv = iv;
        this.period = period;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String encryption() {
        String ciphertext = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(getKey(), "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(getIv());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            ciphertext = Base64.encodeToString(cipher.doFinal(getPeriod().getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ciphertext;
    }

    public String decryption(String text) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(getKey(), "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(getIv());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            return new String(cipher.doFinal(Base64.decode(text,Base64.DEFAULT)), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
