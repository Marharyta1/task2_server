package by.itsm.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerConfig.class);
        context.registerShutdownHook();

        Runnable server = (Runnable) context.getBean("Server");
        server.run();
    }


}
