package Core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// custom annotation for our commands and use this at runtime
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandAnnontation {
    String command();
    String alias();
    String desc();
}