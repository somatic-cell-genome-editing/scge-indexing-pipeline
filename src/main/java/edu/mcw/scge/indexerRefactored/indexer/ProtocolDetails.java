package edu.mcw.scge.indexerRefactored.indexer;


import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.indexerRefactored.indexer.model.AccessLevel;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

public class ProtocolDetails extends ObjectDetails<Protocol>{
    public ProtocolDetails(Protocol protocol) throws Exception {
        super(protocol);
    }

    @Override
    public int getTier() {
        return 0;
    }

    @Override
    public void setStudies() throws Exception {

    }

    @Override
    public void mapObject(IndexDocument indexDocument, AccessLevel accessLevel) {

    }
}
