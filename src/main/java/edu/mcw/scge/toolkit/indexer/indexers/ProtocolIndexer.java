package edu.mcw.scge.toolkit.indexer.indexers;


import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;
import edu.mcw.scge.toolkit.indexer.index.ProtocolDetails;

import java.util.List;


public class ProtocolIndexer extends Indexer<Protocol>implements ObjectIndexer  {
    @Override
    List<Protocol> getObjects() throws Exception {
        return protocolDao.getProtocols();
    }
    @Override
    public void getIndexObjects() throws Exception {
        System.out.println("PROTOCOL SIZE:"+ getObjects().size());
        for(Protocol protocol:getObjects()){
            ObjectDetails<Protocol> objectDetails=new ProtocolDetails(protocol);
            indexObjects(objectDetails);

        }

    }

}
