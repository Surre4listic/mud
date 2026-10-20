package se.mud.Core;

import java.util.HashMap;
import java.lang.reflect.Method;

public class CommandMethods {

    private HashMap<String[], Method> commandMap = new HashMap<String[], Method>();

    public CommandMethods() {

        // loop methods and save them to a list
        for (var method : this.getClass().getDeclaredMethods()) {
            // can be used if we want, we'll see
            // && method.getReturnType() == String.class
            if (method.isAnnotationPresent(CommandAnnontation.class)) {
                var annotation = method.getAnnotation(CommandAnnontation.class);
                System.out.println(annotation.commands() + ":" + annotation.desc());

            }
        }

    }

    @CommandAnnontation (commands={"help"}, desc="help")
    private String help(String cmd) {
        return "";
    }

    
    
}
