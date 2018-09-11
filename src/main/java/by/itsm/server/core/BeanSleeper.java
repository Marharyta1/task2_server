package by.itsm.server.core;

import org.springframework.stereotype.Component;

@Component
public class BeanSleeper {

    public static String sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Awake!";
    }
}
