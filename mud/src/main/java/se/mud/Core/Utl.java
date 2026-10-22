package se.mud.core;

public class Utl {

    // debug
    public static void debug(String text) {
        System.out.println("\n  [DEBUG] " + text + "\n");
    }

    // capitalize first letter
    public static void cap(String text) {

    }

    // cut after certain char's
    public static void cut(String text) {
        
    }

    // add x amount of spaces minus text length after
    public static String whitespace(String text, int space) {
        return text + " ".repeat(space - text.length());
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
