package by.itsm.server.core.processors;

import by.itsm.server.core.BeanSleeper;
import by.itsm.server.dto.SimpleRequest;
import by.itsm.server.dto.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Random;

import static by.itsm.server.core.processors.BaseRequestProcessor.BEAN_NAME;

@Component
@Scope("prototype")
@Order(2)
@Qualifier(BEAN_NAME)
public class BaseRequestProcessor implements RequestProcessor {

    public static final String BEAN_NAME = "simpleRequestProcessor";

    private final BeanSleeper sleeper;

    @Autowired
    public BaseRequestProcessor(BeanSleeper sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public boolean accept(SimpleRequest request) {
        return true;
//        return request != null && !"moo".equals(request.getMessage());
    }

    @Override
    public SimpleResponse process(SimpleRequest request) {
        String message = request.getMessage();
        String name = request.getName();

        System.out.println(sleeper.sleep(new Random().nextInt(150)));

        System.out.println(String.format("message from: %s, content: %s", name, message));
        return new SimpleResponse("Hello, " + name);
    }
}
