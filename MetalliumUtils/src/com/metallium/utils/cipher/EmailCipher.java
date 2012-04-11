/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.cipher;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EmailCipher {

    private static String sessionKey = "987654321";
    private static String hackSessionKey = null;
    private static final int SESSION_KEY_LEN = 8;
    private static boolean testDecrypt = true;

    private static String getSecretKey(String sessionKey) {
        String key = "this-is-the-secret-key";
        return key.concat(sessionKey);
    }

    private static String generateRandomSessionKey() {
        // Generate random sessionKey. Very simplistic - just use current time -
        // munge it to correct length
        long t = System.currentTimeMillis();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < SESSION_KEY_LEN; i++) {
            buf.append("0");
        }
        NumberFormat nf = new DecimalFormat(buf.toString());
        String s = nf.format(t);
        if (s.length() > SESSION_KEY_LEN) {
            int p = s.length() - SESSION_KEY_LEN - 1;
            int q = s.length() - 1;
            s = s.substring(p, q);
        }
        return s;
    }

    private static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        // cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(cal.getTime());
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    private static byte[] convertFromHexStr(String str) {
        int i = 0;
        byte[] buf = new byte[str.length() / 2];
        while (i < str.length()) {
            String ss = str.substring(i, i + 2);
            int ch = Integer.parseInt(ss, 16);
            if (ch > 0xff) {
                System.out.println("!!!Err - ch = " + ch);
            } else {
                buf[i / 2] = (byte) ch;
            }
            i += 2;
        }
        return buf;
    }

    private static String SHA1(String text) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        // md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md.update(text.getBytes("UTF-8"), 0, text.length());
        sha1hash = md.digest();
        String sx = new String(sha1hash);
        return convertToHex(sha1hash);
    }

    public static String encode(String email) throws Exception {

        String finalToAltus = "";
        if (hackSessionKey != null) {
            sessionKey = hackSessionKey;
        } else {
            sessionKey = generateRandomSessionKey();
        }
        String shaKey = getSecretKey(sessionKey);
        String s40 = SHA1(shaKey);
        String sAESKey = s40.substring(0, 16);
        sAESKey = sAESKey.toUpperCase();
        Cipher mEncryptCipher = Cipher.getInstance("AES");
        SecretKey aesKey = new SecretKeySpec(sAESKey.getBytes(), "AES");
        mEncryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] bEncryptedUserData = mEncryptCipher.doFinal(email.getBytes());
        String hexOfEncryptedUserData = convertToHex(bEncryptedUserData);
        finalToAltus = hexOfEncryptedUserData + sessionKey;
        return finalToAltus;

    }

    public static String decode(String encryptedString) throws Exception {

        int p = encryptedString.length() - SESSION_KEY_LEN;
        String skey = encryptedString.substring(p);
        String encryptedData = encryptedString.substring(0, p);

        String shaKey = getSecretKey(skey);
        String s40 = SHA1(shaKey);
        String sAESKey = s40.substring(0, 16);
        sAESKey = sAESKey.toUpperCase();

        Cipher mEncryptCipher = Cipher.getInstance("AES");

        SecretKey aesKey = new SecretKeySpec(sAESKey.getBytes(), "AES");
        mEncryptCipher.init(Cipher.DECRYPT_MODE, aesKey);
        // Note: encryptedData.getBytes is returning s bogus string. Using
        // convertFromHex. There should
        // be a better way - passing some encoding to encryptData.getBytes()
        byte[] bDecryptedUserData = mEncryptCipher.doFinal(convertFromHexStr(encryptedData));
        String decryptedData = new String(bDecryptedUserData);

        return decryptedData;
    }

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();
        String encStr = EmailCipher.encode("johndoe@oracle.com");
        System.out.println("encStr = " + encStr);
        long end = System.currentTimeMillis();
        System.out.println("Execution time was " + (end - start) + " ms.");


        System.out.println("-----------");
        if (testDecrypt) {
            start = System.currentTimeMillis();
            System.out.println("Decrypting generated token...");
            String decrypted = EmailCipher.decode(encStr);
            System.out.println("--> " + decrypted);
            end = System.currentTimeMillis();
            System.out.println("Execution time was " + (end - start) + " ms.");
        }


    }
}
