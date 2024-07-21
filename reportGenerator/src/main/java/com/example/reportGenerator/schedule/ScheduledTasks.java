package com.example.reportGenerator.schedule;

import com.example.reportGenerator.controller.ReportController;
import com.opencsv.exceptions.CsvException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class ScheduledTasks {


    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    ReportController reportController;

    public static MultipartFile convertFileToMultipartFile(File file) {
        return new CustomMultipartFile(file, file.getName());
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() throws IOException, CsvException {
        log.info("The time is now {}", dateFormat.format(new Date()));
        String inFilePath = "C:\\Users\\Kushal\\Desktop\\InFile.csv";
        String refFilePath = "C:\\Users\\Kushal\\Desktop\\RefFile.csv";
        ResponseEntity<byte[]> entity= reportController.parseCSV(convertFileToMultipartFile(new File(inFilePath)),convertFileToMultipartFile(new File(refFilePath)));
        log.info(entity.getBody().toString());
    }
}
