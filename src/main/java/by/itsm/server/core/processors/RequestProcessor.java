package by.itsm.server.core.processors;


import by.itsm.server.dto.SimpleRequest;
import by.itsm.server.dto.SimpleResponse;

public interface RequestProcessor {
    SimpleResponse process(SimpleRequest request);

    boolean accept(SimpleRequest request);
}
