package com.example.reportGenerator.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//class CSVReportServiceImplTest {
//
//    @InjectMocks
//    private CSVReportServiceImpl csvReportService;
//
//    @Test
//    void mssg() {
//        Assertions.assertEquals("My Mesage", csvReportService.mssg());
//    }
//}





import com.example.reportGenerator.controller.ReportController;
import com.example.reportGenerator.data.InCSVData;
import com.example.reportGenerator.data.OutCSVData;
import com.example.reportGenerator.data.RefCSVData;
import com.example.reportGenerator.report.CSVReport;
import com.example.reportGenerator.service.ReportService;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CsvControllerTest {

    @InjectMocks
    private ReportController csvController;

    @Mock
    private ReportService reportService;

    @Mock
    private CSVReport csvReport;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testParseCSV() throws IOException, CsvException {
        // Prepare mock input CSV files
        String inputCsvContent = "field1,field2,field3,field4,field5,refkey1,refkey2\n" +
                "value1,value2,value3,value4,5.0,key1,key2";
        MultipartFile inputFile = new MockMultipartFile("inputFile", "input.csv", "text/csv", inputCsvContent.getBytes(StandardCharsets.UTF_8));

        String referenceCsvContent = "refkey1,refdata1,refkey2,refdata2,refdata3,refdata4\n" +
                "key1,refValue1,key2,refValue2,refValue3,4.0";
        MultipartFile referenceFile = new MockMultipartFile("referenceFile", "reference.csv", "text/csv", referenceCsvContent.getBytes(StandardCharsets.UTF_8));

        // Prepare mock output data
        List<OutCSVData> mockOutData = List.of(new OutCSVData("value1value2", "refValue1", "refValue2refValue3", 20.0, 5.0));

        // Mock the behavior of reportService and csvReport
        List<InCSVData> ipList= new ArrayList<>();
        InCSVData incsvData = new InCSVData("value1","value2","value3","value4",5.0,"key1","key2");
        ipList.add(incsvData);
        List<RefCSVData> refList= new ArrayList<>();
        RefCSVData refCSVData= new RefCSVData("key1","refValue1","key2","refValue2","refValue3",4.0);
        refList.add(refCSVData);
        when(reportService.generateReport( ipList, refList)).thenReturn(mockOutData);
        when(csvReport.generateCsv(mockOutData)).thenReturn("outfield1,outfield2,outfield3,outfield4,outfield5\nvalue1value2,refValue1,refValue2refValue3,20.0,5.0");

        // Call the method
        ResponseEntity<byte[]> response = csvController.parseCSV(inputFile, referenceFile);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("outfield1,outfield2,outfield3,outfield4,outfield5\nvalue1value2,refValue1,refValue2refValue3,20.0,5.0", new String(response.getBody(), StandardCharsets.UTF_8));
        assertEquals("attachment; filename=\"Output.csv\"", response.getHeaders().getContentDisposition().toString());
        assertEquals("application/octet-stream", response.getHeaders().getContentType().toString());
    }
}
