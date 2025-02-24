package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.indexerRefactored.indexer.model.IndexDocument;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ApplicationMethodMapper implements Mapper {
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<String> dosage=new HashSet<>();
        Set<String> injectionFrequency=new HashSet<>();

    }
}
