package edu.mcw.scge.indexerRefactored.indexer;

import edu.mcw.scge.indexerRefactored.indexer.model.AccessLevel;
import edu.mcw.scge.indexerRefactored.indexer.model.IndexDocument;

public interface Index<T> {
    int getTier();
     void setStudies() throws Exception ;
     T getObject(AccessLevel accessLevel);
    void mapObject(IndexDocument indexDocument, AccessLevel accessLevel);
     IndexDocument getIndexObject( AccessLevel accessLevel) throws Exception;
}
