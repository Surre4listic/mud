package se.mud.Core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class CommandParser {

    private HashMap<String[], Method> commandMap = new HashMap<String[], Method>();

    public CommandParser() {

        // load methods into HashMap once when initiated
        for (var method : CommandMethods.class.getDeclaredMethods()) {

            // can be used if we want, we'll see "&& method.getReturnType() == String.class"
            if (method.isAnnotationPresent(CommandAnnontation.class)) {
                var annotation = method.getAnnotation(CommandAnnontation.class);
/*                 String aliases = "";
                // add one entry for each alias
                for (String cmd : annotation.commands()) {
                    aliases += (aliases.isEmpty() ? "" : ", ") + cmd;
                    commandMap.put(annotation.commands(), method);
                } */
                
                commandMap.put(annotation.commands(), method);
                Utl.debug("Adding method: " + method.getName() + ". Alias: " + ".");
                
            }
        }
    
    }


    public void Parse(String input) {

        Utl.debug("Input recieved: " + input + ".");

        // loop through our HashMap
        for (String[] aliases : commandMap.keySet()) {
            // check each alias for first word of any input
            if (Arrays.asList(aliases).contains(input.split("´")[0])) {
                System.out.println("Found method for " + input + ". Method: " + commandMap.get(aliases).getName());
            }
        }


        // only check first word of input against our methods

        // check for method


    }
    
}
