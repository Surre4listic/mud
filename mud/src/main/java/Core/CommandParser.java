package Core;

public class CommandParser {

    public CommandParser() { }

    public void Input(String cmd) {
        System.out.println("Input recieved from client: " + cmd);

        // split cmd into strings between whitespaces
        String arrayTest[] = cmd.trim().split("\\s+");
        for (String s : arrayTest) {
            System.out.println(s);
        }

        // check for method

    }
    
}
