package util;

import ast.*;

public class AssetLanlib {

    private static int labCount=0;
    private static int funLabCount=0;

    public static boolean isSubtype (Node a, Node b) {
        return a.getClass().equals(b.getClass());
    }

    public static String freshLabel() { 
        return "label"+(labCount++);
    } 
    
    public static String freshFunLabel() { 
        return "function"+(funLabCount++);
    } 
      
}