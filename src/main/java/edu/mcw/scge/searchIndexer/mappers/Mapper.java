package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.List;

public interface Mapper  {
    void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception;
}
