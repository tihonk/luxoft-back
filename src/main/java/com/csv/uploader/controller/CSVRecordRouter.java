package com.csv.uploader.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.csv.uploader.dto.CSVFileData;
import com.csv.uploader.dto.CSVRecord;
import com.csv.uploader.handler.CSVRecordHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class CSVRecordRouter {

    private static final String ID = "id";

    @Bean
    public RouterFunction<ServerResponse> recordRoute(final CSVRecordHandler handler) {
        return RouterFunctions
            .route(GET("/records").and(accept(APPLICATION_JSON)), request -> getAllRecords(handler))
            .andRoute(GET("/records/{id}").and(accept(APPLICATION_JSON)), request -> getRecord(handler, request))
            .andRoute(DELETE("/records/{id}").and(accept(APPLICATION_JSON)), request -> deleteRecord(handler, request))
            .andRoute(GET("/parse/{id}").and(accept(APPLICATION_JSON)), request -> parseFile(handler, request));
    }

    private Mono<ServerResponse> getAllRecords(final CSVRecordHandler handler) {
        return ok().body(handler.getAllRecords(), CSVRecord.class);
    }

    private Mono<ServerResponse> getRecord(final CSVRecordHandler handler, final ServerRequest request) {
        return ok().body(handler.getRecord(request.pathVariable(ID)), CSVRecord.class);
    }

    private Mono<ServerResponse> parseFile(final CSVRecordHandler handler, final ServerRequest request) {
        return ok().body(handler.parseFile(request.pathVariable(ID)), CSVRecord.class);
    }

    private Mono<ServerResponse> deleteRecord(final CSVRecordHandler handler, final ServerRequest request) {
        return ok().body(handler.deleteRecord(request.pathVariable(ID)), CSVFileData.class);
    }
}
