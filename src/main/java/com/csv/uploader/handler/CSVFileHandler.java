package com.csv.uploader.handler;

import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Paths.get;
import static java.util.UUID.randomUUID;
import static java.util.logging.Logger.getLogger;
import static reactor.core.publisher.Mono.fromCallable;
import static reactor.core.scheduler.Schedulers.boundedElastic;

import com.csv.uploader.dto.CSVFileData;
import com.csv.uploader.repository.CSVFileRepository;
import java.io.File;
import java.util.logging.Logger;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public record CSVFileHandler(CSVFileRepository fileRepository) {

    final static Logger logger = getLogger("CSVFileHandler");

    private static final String CLOUD_STORAGE_PATH = "src/main/resources/cloudstorage/";

    public Flux<CSVFileData> getAllFiles() {
        return fileRepository.findAll();
    }

    public Mono<Void> create(final Part file) {
        return saveCSVFile((FilePart) file);
    }

    public Mono<Void> deleteFile(final String fileId) {
        return deleteSCVFile(fileId);
    }

    private Mono<Void> saveCSVFile(final FilePart csvFile) {
        final String csvFilePath = CLOUD_STORAGE_PATH + randomUUID() + csvFile.filename();
        return saveCSVFileToCassandra(csvFile, csvFilePath)    //Save file data to Cassandra
            .then(csvFile.transferTo(new File(csvFilePath)));  //Save file to Local Storage
    }

    private Mono<Void> deleteSCVFile(final String fileId) {
        logger.info("Deleting file with id: " + fileId);
        return fileRepository.findById(fileId).publishOn(boundedElastic()).flatMap(file ->
            fromCallable(() ->
                deleteIfExists(get(file.getFilePath())))   //Remove from Local Storage
                .then(fileRepository.deleteById(fileId))); //Remove from Cassandra)
    }

    private Mono<CSVFileData> saveCSVFileToCassandra(final FilePart csvFile, final String csvFilePath) {
        final CSVFileData fileData = new CSVFileData(randomUUID().toString(), csvFilePath, csvFile.filename());
        return fileRepository.save(fileData);
    }
}
