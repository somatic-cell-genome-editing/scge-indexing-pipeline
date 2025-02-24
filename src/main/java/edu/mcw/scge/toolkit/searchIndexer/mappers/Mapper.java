package edu.mcw.scge.toolkit.searchIndexer.mappers;

import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

import java.util.List;

public interface Mapper  {
    void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception;
}
