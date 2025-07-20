package sn.groupeisi.matcompta.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
public class PasswordUtil {
    public static String hasher(String motDePasse) {
        return BCrypt.withDefaults().hashToString(12, motDePasse.toCharArray());
    }

    public static boolean verifier(String motDePasseSaisi, String hashEnBase) {
        return BCrypt.verifyer().verify(motDePasseSaisi.toCharArray(), hashEnBase).verified;
    }
}
