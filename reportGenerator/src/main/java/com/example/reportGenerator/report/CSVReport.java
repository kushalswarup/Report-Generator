package com.example.reportGenerator.report;

import com.example.reportGenerator.data.OutCSVData;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;


@Component
public class CSVReport {


    private static final String CSV_HEADER = "outfield1,outfield2,outfield3,outfield4,outfield5\n";

    public String generateCsv(List<OutCSVData> listOutCSVData) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);

        for (OutCSVData outCSVData : listOutCSVData) {
            csvContent.append(outCSVData.getOutfield1()).append(",")
                    .append(outCSVData.getOutfield2()).append(",")
                    .append(outCSVData.getOutfield3()).append(",")
                    .append(outCSVData.getOutfield4()).append(",")
                    .append(outCSVData.getOutfield5()).append("\n");
        }

        return csvContent.toString();
    }
}
