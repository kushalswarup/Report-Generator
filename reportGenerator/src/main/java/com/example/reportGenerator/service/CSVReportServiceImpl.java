package com.example.reportGenerator.service;

import com.example.reportGenerator.data.InCSVData;
import com.example.reportGenerator.data.OutCSVData;
import com.example.reportGenerator.data.RefCSVData;
import com.example.reportGenerator.transformation.CSVTransformationRulesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSVReportServiceImpl implements ReportService{

    @Autowired
    CSVTransformationRulesImpl csvTransformationRulesImpl;

    public String mssg(){
        return "My Mesage";

    }

    public List<OutCSVData> generateReport(List<InCSVData> indata, List<RefCSVData> refdata){
        return csvTransformationRulesImpl.rules(indata, refdata);

    }


}
