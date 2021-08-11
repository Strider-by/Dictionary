package com.github.strider_by.util;

public class NumberFormatChecker {
    
    private  NumberFormatChecker() {
        
    }
    
    public static boolean isInteger(String str) {
        boolean result = false;
        try {
            Integer.parseInt(str);
            result = true;
        } catch(NumberFormatException ex) {
            // no, it isn't
        }

        return result;
    }
    
    public static boolean isPositiveInteger(String str) {
        boolean result = false;
        try {
            int value = Integer.parseInt(str);
            result = value > 0;
        } catch(NumberFormatException ex) {
            // it isn't an Integer
        }

        return result;
    }
}
