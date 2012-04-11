/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.framework.utilities;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Ruben
 */
public class RandomHelper
{

    private SecureRandom seeder;
    private String midValue;
    private String midValueUnformated;
    private long checkTime;


    public RandomHelper()
    {
        try
        {
            StringBuilder stringbuffer = new StringBuilder();
            StringBuilder stringbuffer1 = new StringBuilder();
            seeder = new SecureRandom();
            InetAddress inetaddress = InetAddress.getLocalHost();
            byte abyte0[] = inetaddress.getAddress();
            String s = hexFormat(getInt(abyte0), 8);
            String s1 = hexFormat(hashCode(), 8);
            stringbuffer.append("-");
            stringbuffer1.append(s.substring(0, 4));
            stringbuffer.append(s.substring(0, 4));
            stringbuffer.append("-");
            stringbuffer1.append(s.substring(4));
            stringbuffer.append(s.substring(4));
            stringbuffer.append("-");
            stringbuffer1.append(s1.substring(0, 4));
            stringbuffer.append(s1.substring(0, 4));
            stringbuffer.append("-");
            stringbuffer1.append(s1.substring(4));
            stringbuffer.append(s1.substring(4));
            midValue = stringbuffer.toString();
            midValueUnformated = stringbuffer1.toString();
            checkTime = System.currentTimeMillis();
            int i = seeder.nextInt();
        }
        catch(UnknownHostException excepcion)
        {
            LogHelper.makeLog("Aleatorio->Error creando el aleatorio", excepcion, null);
        }
    }

    private String getVal(String s)
    {
        long l = System.currentTimeMillis();
        int i = (int)l & -1;
        int j = seeder.nextInt();
        return (new StringBuilder()).append(hexFormat(i, 8)).append(s).append(hexFormat(j, 8)).toString();
    }

    public String getUnformatedUUID()
    {
        return getVal(midValueUnformated);
    }

    public String getUUID()
    {
        return getVal(midValue);
    }

    private String hexFormat(int i, int j)
    {
        String s = Integer.toHexString(i);
        return (new StringBuilder()).append(padHex(s, j)).append(s).toString();
    }

    private String padHex(String s, int i)
    {
        StringBuilder stringbuffer = new StringBuilder();
        if(s.length() < i)
        {
            for(int j = 0; j < i - s.length(); j++)
                stringbuffer.append("0");

        }
        return stringbuffer.toString();
    }

    private int getInt(byte abyte0[])
    {
        int i = 0;
        int j = 24;
        for(int k = 0; j >= 0; k++)
        {
            int l = abyte0[k] & 0xff;
            i += l << j;
            j -= 8;
        }

        return i;
    }

    /**
     * Just a simple 6 letter password, nothing fancy.
     *
     * @return
     */
    public static String generateRandomPassword() {
        StringBuilder s = new StringBuilder();
        int c = 'A';
        for (int i = 0; i < 4; i++) {
            c = 'a' + (int) (Math.random() * 26);
            s.append((char) c);
        }
        for (int i = 0; i < 2; i++) {
            c = '0' + (int) (Math.random() * 10);
            s.append((char) c);
        }
//        for (int i = 0; i < 4 ; i++) {
//            c = 'A' + (int) (Math.random() * 26);
//            s.append((char) c);
//        }

        return getScrambled(s.toString()); //and finally I scramble the password and return it.
    }




    /**
     * It just scrambles a String.
     *
     * @param s
     * @return
     */
    private static String getScrambled(String s) {
        String[] scram = s.split("");
        List<String> letters = Arrays.asList(scram);
        Collections.shuffle(letters);
        StringBuilder sb = new StringBuilder(s.length());
        for (String c : letters) {
            sb.append(c);
        }
      return sb.toString();
    }

}