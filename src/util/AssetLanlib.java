package util;

import ast.*;

public class AssetLanlib {

    public static boolean isSubtype (Node a, Node b) {
        return a.getClass().equals(b.getClass());
    }
}