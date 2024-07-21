package com.example.reportGenerator.controller;


import com.example.reportGenerator.data.InCSVData;
import com.example.reportGenerator.data.OutCSVData;
import com.example.reportGenerator.data.RefCSVData;
import com.example.reportGenerator.report.CSVReport;
import com.example.reportGenerator.service.CSVReportServiceImpl;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class ReportController {
    Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    CSVReportServiceImpl reportService;
    @Autowired
    CSVReport csvReport;
    @GetMapping("/data")
    public String getData(){
        return reportService.mssg();
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> parseCSV(@RequestParam MultipartFile inputFile, @RequestParam MultipartFile referenceFile) throws IOException, CsvException {

        Reader inputReader = new InputStreamReader(inputFile.getInputStream());  // Normal Reader
        Reader referneceReader = new InputStreamReader(referenceFile.getInputStream());

        Map<String, String> columnInCSVMappings = Map.of(
                "field1", "field1",
                "field2", "field2",
                "field3", "field3",
                "field4", "field4",
                "field5", "field5",
                "refkey1", "refkey1",
                "refkey2", "refkey2"

        );
        logger.info("temporary headers are created to form a structure to read input ");
        HeaderColumnNameTranslateMappingStrategy inCSVMappingStrategy =
                new HeaderColumnNameTranslateMappingStrategy();
        inCSVMappingStrategy.setColumnMapping(columnInCSVMappings);
        inCSVMappingStrategy.setType(InCSVData.class);


        CSVReader inCSVReader = new CSVReaderBuilder(inputReader).build();  //CSV Reader

        CsvToBean<InCSVData> inCSVBean = new CsvToBeanBuilder(inCSVReader)
                .withType(InCSVData.class)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .withMappingStrategy(inCSVMappingStrategy)
                .build();

        // Parse CSV data
        Map<String, String> columnRefCSVMappings = Map.of(
                "refkey1", "refkey1",
                "refdata1", "refdata1",
                "refkey2", "refkey2",
                "refdata2", "refdata2",
                "refdata3", "refdata3",
                "refdata4", "refdata4"

        );

        HeaderColumnNameTranslateMappingStrategy refMappingStrategy =
                new HeaderColumnNameTranslateMappingStrategy();
        refMappingStrategy.setColumnMapping(columnRefCSVMappings);// Temporary structure to read input files
        refMappingStrategy.setType(RefCSVData.class);

        //CsvToBean<RefCSVData> refCSVToBean = new CsvToBean<RefCSVData>();
        CSVReader RefCSVReader = new CSVReaderBuilder(referneceReader).build();

        CsvToBean<RefCSVData> refCSVBean = new CsvToBeanBuilder(RefCSVReader)
                .withType(RefCSVData.class)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)                        //Convertion of files to beans
                .withMappingStrategy(refMappingStrategy)
                .build();
        logger.info("CSV file is coverted to Beans");
        List<InCSVData> indata = inCSVBean.parse();   // beans are used to create a lists using parse()
        List<RefCSVData> refdata = refCSVBean.parse();
logger.info("beans are parsed into lists");
        //CSVReader refCSVReader = new CSVReaderBuilder(referneceReader).build();
        //List<String[]> refRows = refCSVReader.readAll();
        // Analyze data...
logger.info("Generating the report and saving it to a output list");
        List<OutCSVData> listOutData= reportService.generateReport(indata, refdata);
logger.info("headers are created for an output list");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Kushal.csv");
logger.info("list is converted to byte array");
        byte[] csvBytes = csvReport.generateCsv(listOutData).getBytes(StandardCharsets.UTF_8);

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);

       // return "Processed " + 10 + " rows!";
    }

}
