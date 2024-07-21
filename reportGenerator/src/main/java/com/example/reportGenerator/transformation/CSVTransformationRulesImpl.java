package com.example.reportGenerator.transformation;

import com.example.reportGenerator.data.InCSVData;
import com.example.reportGenerator.data.OutCSVData;
import com.example.reportGenerator.data.RefCSVData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CSVTransformationRulesImpl implements TransformationRules{


    public List<OutCSVData> rules (List<InCSVData> listInCSVData, List<RefCSVData> listRefCSVData){

        List<OutCSVData> outList = new ArrayList<>();

        for(InCSVData inCSVData:listInCSVData){
            OutCSVData outCSVData = new OutCSVData();
            outCSVData.setOutfield1(inCSVData.getField1() + inCSVData.getField2());

            for(RefCSVData refCSVData : listRefCSVData){
                if(inCSVData.getRefkey1().equalsIgnoreCase(refCSVData.getRefkey1())
                && inCSVData.getRefkey2().equalsIgnoreCase(refCSVData.getRefkey2()) ){
                    outCSVData.setOutfield2(refCSVData.getRefdata1());
                    outCSVData.setOutfield3(refCSVData.getRefdata2() + refCSVData.getRefdata3());
                    outCSVData.setOutfield4(Integer.parseInt(inCSVData.getField3()) * Math.max(inCSVData.getField5(), refCSVData.getRefdata4()));
                    outCSVData.setOutfield5(Math.max(inCSVData.getField5(), refCSVData.getRefdata4()));

                }
            }
            outList.add(outCSVData);
        }
        return outList;
    }
}
