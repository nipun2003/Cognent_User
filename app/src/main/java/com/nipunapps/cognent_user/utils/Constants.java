package com.nipunapps.cognent_user.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

    public static boolean validateEmail(String email){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validatePhone(String phone){
        String regex = "(0/91)?[7-9][0-9]{9}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
