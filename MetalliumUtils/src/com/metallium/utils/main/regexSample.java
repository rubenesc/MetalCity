/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.metallium.utils.main;

import java.util.regex.*;

class regexSample
{

    public static final Pattern REGEX_EMAIL = Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern REGEX_EMAIL2 = Pattern.compile("[a-zA-Z0-9\\.\\_\\-]+@[a-zA-Z0-9\\_\\-]+\\.[a-zA-Z0-9\\.\\_\\-]+", Pattern.CASE_INSENSITIVE);

   public static void main(String args[])
   {
      //Input the string for validation
      String email = "2$.s@ssd.sd.co.si.co";

      //Match the given string with the pattern
      Matcher m = REGEX_EMAIL.matcher(email);

      //check whether match is found
      boolean matchFound = m.matches();

      if (matchFound)
        System.out.println("Valid Email Id.");
      else
        System.out.println("Invalid Email Id.");
   }
}
