package edu.nanoracket.npr.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String convertString(String str){
        String regexPat = "#([0-9]+)(:|((\\s)-(\\s)))";

        Pattern pattern = Pattern.compile(regexPat);
        Matcher matcher = pattern.matcher(str);

        if(matcher.find()){
            return matcher.replaceAll("");
        }else {
            return str;
        }
    }

    public static String convertStoryTitle(String str){
        String regexPat = "(\\s)|((?![@',&])\\p{Punct})";
        Pattern pattern = Pattern.compile(regexPat);
        Matcher matcher = pattern.matcher(str);

        if(matcher.find()){
            return matcher.replaceAll("_");
        }else{
            return str;
        }
    }
}
