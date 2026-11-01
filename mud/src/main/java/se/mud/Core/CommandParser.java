package se.mud.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import se.mud.Player;

public class CommandParser {

    // Create a HashMap to store command aliases and their corresponding methods
    private HashMap<String[], Method> commandMap = new HashMap<String[], Method>();
    // Create an instance of CommandMethods to invoke methods on
    CommandMethods commandMethods;

    public CommandParser(Player player) {

        // Initialize CommandMethods with the player instance
        commandMethods = new CommandMethods(player);

        // Load methods into HashMap once when initiated
        for (var method : CommandMethods.class.getDeclaredMethods()) {

            // Can be used if we want, we'll see "&& method.getReturnType() == String.class"
            if (method.isAnnotationPresent(CommandAnnontation.class)) {
                var annotation = method.getAnnotation(CommandAnnontation.class);
                // Add this method to HashMap > commandMap                
                commandMap.put(annotation.commands(), method);
                Utl.debug("Adding method: " + method.getName() + ". Alias: " + ".");
                
            }
        }
    }


    public void Input(String input) {

        Utl.debug("Input recieved: " + input + ".");
        input = input.toLowerCase(); // Convert input to lowercase for case-insensitive matching

        // Loop through our HashMap and compare alias array with first word of input
        for (String[] aliases : commandMap.keySet()) {
            // Check each alias for first word of any input
            if (Arrays.asList(aliases).contains(input.split(" ")[0])) {
                Utl.debug("Found method for " + input + ". Method: " + commandMap.get(aliases).getName());
                // Use try for reflection invoke, if fails catch and debug
                try {
                    /* Regex to remove first word of input and trim whitespace then invokes 
                    the method with the rest of the input as a string parameter */
                    commandMap.get(aliases).invoke(commandMethods, input.replaceAll("^[^\s]+(.*)", "$1").trim());
                } catch (Exception e) {
                    Utl.debug("Error: " + e);
                } 
            }
        }

    }
    
}
