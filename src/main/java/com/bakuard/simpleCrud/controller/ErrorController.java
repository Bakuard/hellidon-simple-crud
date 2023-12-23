package com.bakuard.simpleCrud.controller;

import com.bakuard.simpleCrud.exception.DomainException;
import com.bakuard.simpleCrud.exception.UnknownEntityException;
import com.fasterxml.jackson.core.JacksonException;
import io.helidon.webserver.http.ErrorHandler;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorController implements ErrorHandler<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class.getName());

    public ErrorController() {}

    @Override
    public void handle(ServerRequest request, ServerResponse response, Throwable exception) {
        logger.error(exception.getMessage(), exception);

        switch (exception) {
            case UnknownEntityException unknownEntityException -> response.status(4040).send(exception.getMessage());
            case DomainException domainException -> response.status(400).send(exception.getMessage());
            case JacksonException jacksonException -> response.status(400).send(exception.getMessage());
            default -> response.status(500).send("Unknown exception");
        }
    }
}
