package com.csv.uploader.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.web.reactive.function.BodyExtractors.toMultipartData;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.csv.uploader.dto.CSVFileData;
import com.csv.uploader.handler.CSVFileHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class CSVFileRouter {

    private static final String FILE = "file";
    private static final String ID = "id";
    private static final String UPLOAD_MESSAGE = "File is uploading";

    @Bean
    public RouterFunction<ServerResponse> fileRoute(final CSVFileHandler handler) {
        return RouterFunctions
            .route(GET("/csvfiles").and(accept(APPLICATION_JSON)), request -> getAllFiles(handler))
            .andRoute(POST("/csvfiles").and(contentType(MULTIPART_FORM_DATA)), request -> create(handler, request))
            .andRoute(DELETE("/csvfiles/{id}").and(accept(APPLICATION_JSON)), request -> deleteFile(handler, request));
    }

    private Mono<ServerResponse> getAllFiles(final CSVFileHandler handler) {
        return ok().body(handler.getAllFiles(), CSVFileData.class);
    }

    private Mono<ServerResponse> deleteFile(final CSVFileHandler handler, final ServerRequest request) {
        return ok().body(handler.deleteFile(request.pathVariable(ID)), CSVFileData.class);
    }

    private Mono<ServerResponse> create(final CSVFileHandler handler, final ServerRequest request) {
        return request.body(toMultipartData())
            .flatMap(parts -> handler.create(parts.toSingleValueMap().get(FILE)))
            .then(ok().bodyValue(UPLOAD_MESSAGE));
    }
}
