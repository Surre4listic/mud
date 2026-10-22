package se.mud.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class CommandParser {

    private HashMap<String[], Method> commandMap = new HashMap<String[], Method>();
    CommandMethods commandMethods;

    public CommandParser(Player player) {

        // create an instance of CommandMethods
        commandMethods = new CommandMethods(player);

        // load methods into HashMap once when initiated
        for (var method : CommandMethods.class.getDeclaredMethods()) {

            // can be used if we want, we'll see "&& method.getReturnType() == String.class"
            if (method.isAnnotationPresent(CommandAnnontation.class)) {
                var annotation = method.getAnnotation(CommandAnnontation.class);
                // add this method to HashMap > commandMap                
                commandMap.put(annotation.commands(), method);
                Utl.debug("Adding method: " + method.getName() + ". Alias: " + ".");
                
            }
        }
    }


    public void Parse(String input) {

        Utl.debug("Input recieved: " + input + ".");
        input = input.toLowerCase();

        // loop through our HashMap and compare alias array with first word of input
        for (String[] aliases : commandMap.keySet()) {
            // check each alias for first word of any input
            if (Arrays.asList(aliases).contains(input.split(" ")[0])) {
                System.out.println("Found method for " + input + ". Method: " + commandMap.get(aliases).getName());
                // try and invoke the method and send anything text behind alias as string parameter
                try {
                    commandMap.get(aliases).invoke(commandMethods, input.replaceAll("^[^\s]+(.*)", "$1").trim());
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                } 
            }
        }

    }
    
}
