package se.mud.Core;

import java.util.HashMap;
import java.lang.reflect.Method;

public class CommandMethods {

    public CommandMethods() {

        System.out.println("test");


    }

    @CommandAnnontation (commands={"help"}, desc="help")
    private String help(String cmd) {
        return "";
    }

    
    @CommandAnnontation (commands={"move", "run"}, desc="help")
    private String move(String cmd) {
        return "";
    }

    
    
}
