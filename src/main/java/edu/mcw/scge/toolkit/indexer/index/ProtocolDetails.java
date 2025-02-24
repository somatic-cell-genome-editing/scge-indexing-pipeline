package edu.mcw.scge.toolkit.indexer.index;


import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;

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
