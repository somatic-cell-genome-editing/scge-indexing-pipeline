package edu.mcw.scge.searchIndexer.indexers;


import edu.mcw.scge.dao.implementation.AssociationDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.ProtocolDao;
import edu.mcw.scge.datamodel.Association;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ProtocolIndexer implements Indexer {
    ProtocolDao protocolDao=new ProtocolDao();
    AssociationDao associationDao=new AssociationDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objects=new ArrayList<>();

        for(Protocol protocol:protocolDao.getProtocols()){
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
        return objects;
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
