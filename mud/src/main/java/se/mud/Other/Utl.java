package se.mud.Other;

import org.mindrot.jbcrypt.BCrypt;

public class Utl {

    // Print text to console
    public static void print(String text) {
        System.out.println(text);
    }

    // Debug text to console
    public static void debug(String text) {
        System.out.println("[DEBUG] " + text);
    }

    // Capitalize first letter
    public static void cap(String text) {

    }

    // Cut after certain char's
    public static void cut(String text) {
        
    }

    // Compare two strings, ignoring case
    public static boolean equal(String compare1, String compare2) {
        if (compare1 != null && compare2 != null && compare1.equalsIgnoreCase(compare2)) {
            return true;
        }
        return false;
    }

    // Add x amount of spaces minus text length after
    public static String whitespace(String text, int space) {
        return text + " ".repeat(space - text.length());
    }

    // Hash a password using BCrypt
    public static String hashPassword(String password) {
        // 12 takes about 100-200ms to compute, which is a good balance between security and performance
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    // Check a password against a hashed password
    public static boolean checkPassword(String password, String hashedPassword){
        try {
            // Bcrypt handles the comparison between the plain text password and the hashed password from the database
            return BCrypt.checkpw(password, hashedPassword);
        } catch (IllegalArgumentException e) {
            // This exception is thrown if the hashed password is not a valid BCrypt hash
            return false;
        }
    }


    public final class ANSI {
        // Reset everything back to default
        public static final String RESET = "\u001B[0m";

        // Text Colors
        public static final String BLACK   = "\u001B[30m";
        public static final String RED     = "\u001B[31m";
        public static final String GREEN   = "\u001B[32m";
        public static final String YELLOW  = "\u001B[33m";
        public static final String BLUE    = "\u001B[34m";
        public static final String MAGENTA = "\u001B[35m";
        public static final String CYAN    = "\u001B[36m";
        public static final String WHITE   = "\u001B[37m";

        // Styles
        public static final String BOLD    = "\u001B[1m";
        public static final String UNDERLINE = "\u001B[4m";
    }
    
}
