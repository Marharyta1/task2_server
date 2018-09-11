package by.itsm.server;

import by.itsm.server.core.Server;
import by.itsm.server.core.processors.RequestProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.PostConstruct;
import inject.Provider;
import java.util.List;

@Configuration
@ComponentScan("by.itsm.server")
@PropertySource(value = "classpath:server.properties")
public class ServerConfig {
    @Autowired
    private ConfigurableEnvironment environment;

    @Value("${server.thread.count}")
    private Integer threadCount;

    @Value("${spring.profiles.active}")
    private String profiles;

    @PostConstruct
    public void initProfiles() {
        environment.setActiveProfiles(profiles.split(","));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean(name = "Server")
    public Server getServer(
            ObjectMapper mapper,
            Provider<List<RequestProcessor>> processor) {
        return new Server(
                environment.getProperty("server.port", Integer.class, 8082),
                threadCount,
                mapper,
                processor);
    }

}
