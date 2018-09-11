package by.itsm.server.core;

import by.itsm.server.core.processors.RequestProcessor;
import by.itsm.server.dto.SimpleRequest;
import by.itsm.server.dto.SimpleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import inject.Provider;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final int messageDelay = 1000;
    private final int port;
    private final int threadCount;
    private final ObjectMapper objectMapper;
    private final Provider<List<RequestProcessor>> requestProcessorProvider;
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public Server(
            int port,
            int threadCount,
            ObjectMapper objectMapper,
            Provider<List<RequestProcessor>> requestProcessorProvider) {
        this.port = port;
        this.threadCount = threadCount;
        this.objectMapper = objectMapper;
        this.requestProcessorProvider = requestProcessorProvider;
    }


    @PostConstruct
    public void init() throws IOException {
        serverSocket = new ServerSocket(port);
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    @PreDestroy
    public void destroy() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executorService.shutdownNow();
    }

    @Override
    public void run() {
        try {
            Socket socket = serverSocket.accept();
            accept(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void accept(Socket socket) {
        executorService.submit(() -> {
            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                DataInputStream reader = new DataInputStream(is);
                DataOutputStream writer = new DataOutputStream(os);

                String requestString = reader.readUTF();
                SimpleRequest request = objectMapper.readValue(requestString, SimpleRequest.class);



                List<RequestProcessor> processors = requestProcessorProvider.get();

                SimpleResponse response = null;

                for (RequestProcessor processor : processors) {
                    if (processor.accept(request)) {
                        response = processor.process(request);
                        break;
                    }
                }


                String responseString = objectMapper.writeValueAsString(response);

                writer.writeUTF(responseString);
                writer.flush();

                socket.close();

                BeanSleeper.sleep(messageDelay);
                System.out.println("> "+request.getName()+": "+request.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
