package com.example.reportGenerator.transformation;

import com.example.reportGenerator.data.InCSVData;
import com.example.reportGenerator.data.OutCSVData;
import com.example.reportGenerator.data.RefCSVData;

import java.util.List;

public interface TransformationRules {
    List<OutCSVData> rules(List<InCSVData> inRows, List<RefCSVData> refRows);
}
