package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ExperimentMapper implements Mapper {
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<String> experimentName=new HashSet<>();
        Set<String> samplePrep=new HashSet<>();
        Set<String> genotype=new HashSet<>();
        Set<String> sex=new HashSet<>();
        for(ExperimentRecord record:experimentRecords){
            if(record.getSex()!=null && !record.getSex().equals(""))
            sex.add(record.getSex());
            if(record.getSamplePrep()!=null && !record.getSamplePrep().equals(""))
            samplePrep.add(record.getSamplePrep());
            if(record.getGenotype()!=null && !record.getGenotype().equals(""))
            genotype.add(record.getGenotype());
        }
        if(!sex.isEmpty())indexDocument.setSex(sex);
        if(!samplePrep.isEmpty())indexDocument.setSamplePrep(samplePrep);
     //   if(!experimentName.isEmpty())indexDocument.setExperimentName(experimentName);
        if(!genotype.isEmpty())indexDocument.setGenotype(genotype);

        Objects.requireNonNull(MapperFactory.getMapper("editor")).mapFields(experimentRecords, indexDocument);
        Objects.requireNonNull(MapperFactory.getMapper("guide")).mapFields(experimentRecords, indexDocument);
        Objects.requireNonNull(MapperFactory.getMapper("vector")).mapFields(experimentRecords, indexDocument);
        Objects.requireNonNull(MapperFactory.getMapper("delivery")).mapFields(experimentRecords, indexDocument);
        Objects.requireNonNull(MapperFactory.getMapper("model")).mapFields(experimentRecords, indexDocument);
        Objects.requireNonNull(MapperFactory.getMapper("tissue")).mapFields(experimentRecords, indexDocument);
     //   Objects.requireNonNull(MapperFactory.getMapper("application")).mapFields(experimentRecords, indexDocument);
        if(!indexDocument.getCategory().equalsIgnoreCase("experiment")){
            Objects.requireNonNull(MapperFactory.getMapper("study")).mapFields(experimentRecords, indexDocument);

        }
    }
}
