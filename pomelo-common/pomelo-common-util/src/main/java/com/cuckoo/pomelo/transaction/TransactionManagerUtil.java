package com.cuckoo.pomelo.transaction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionManagerUtil {

    public static void main(String[] args) {
        String str = "11234567891234";
        Pattern pattern = Pattern.compile("(1)([3-9])(\\d)(\\d{4})(\\d{4})");
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            System.out.println(matcher.group(4));
        }

    }
}
