package com.example.reportGenerator.service;

import com.example.reportGenerator.data.InCSVData;
import com.example.reportGenerator.data.OutCSVData;
import com.example.reportGenerator.data.RefCSVData;

import java.util.List;

public interface ReportService {

    List<OutCSVData>  generateReport(List<InCSVData> indata, List<RefCSVData> refdata);

}
