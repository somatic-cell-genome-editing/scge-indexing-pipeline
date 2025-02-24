package edu.mcw.scge.toolkit.searchIndexer.indexers;


import edu.mcw.scge.dao.implementation.AssociationDao;
import edu.mcw.scge.dao.implementation.ProtocolDao;
import edu.mcw.scge.datamodel.Association;
import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

import java.util.ArrayList;
import java.util.List;


public class ProtocolIndexer extends Indexer<Protocol>implements ObjectIndexer  {
    ProtocolDao protocolDao=new ProtocolDao();
    AssociationDao associationDao=new AssociationDao();

    @Override
    List<Protocol> getObjects() throws Exception {
        return protocolDao.getProtocols();
    }

    @Override
    public void getIndexObjects() throws Exception {
        List<IndexDocument> objects=new ArrayList<>();

        for(Protocol protocol:getObjects()){
            IndexDocument o=new IndexDocument();
            o.setAccessLevel("consortium");
            mapDetails(o,protocol);
            Association protocolAssociation=associationDao.getProtocolAssociations(protocol.getId());
            AssociationDetails details= new AssociationDetails(o, protocolAssociation);
            details.associateDetails();
            objects.add(o);
            if(protocol.getTier()==4){
                IndexDocument publicObject = new IndexDocument();
                publicObject.setAccessLevel("public");
                mapDetails(publicObject,protocol);
                AssociationDetails details1= new AssociationDetails(publicObject, protocolAssociation);
                details1.associateDetails();
                objects.add(publicObject);
            }
        }

    }


    public void mapDetails(IndexDocument o, Protocol protocol){
        o.setCategory("Protocol");
        o.setId(protocol.getId());
        o.setName(protocol.getTitle());
        o.setDescription(protocol.getDescription());
        o.setTier(protocol.getTier());
        o.setReportPageLink("/toolkit/data/protocols/protocol/?id=");
    }

}
