package edu.mcw.scge.searchIndexer.processThreads;

import edu.mcw.scge.dao.implementation.AssociationDao;
import edu.mcw.scge.dao.implementation.ProtocolDao;
import edu.mcw.scge.datamodel.Association;
import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.indexer.Manager;
import edu.mcw.scge.searchIndexer.indexers.AssociationDetails;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class ProtocolIndexerThread extends Indexer implements Runnable{
    ProtocolDao protocolDao=new ProtocolDao();
    AssociationDao associationDao=new AssociationDao();
    @Override
    public void run() {
        Logger log= LogManager.getLogger(Manager.class);

        log.info(Thread.currentThread().getName() + ": " + "PROTOCOL" + " started " + new Date());
        try {
            for(Protocol protocol:protocolDao.getProtocols()){
                IndexDocument o=new IndexDocument();
                o.setAccessLevel("consortium");
                mapDetails(o,protocol);
                Association protocolAssociation=associationDao.getProtocolAssociations(protocol.getId());
                AssociationDetails details= new AssociationDetails(o, protocolAssociation);
                details.associateDetails();
                index(o);
                if(protocol.getTier()==4){
                    IndexDocument publicObject = new IndexDocument();
                    publicObject.setAccessLevel("public");
                    mapDetails(publicObject,protocol);
                    AssociationDetails details1= new AssociationDetails(publicObject, protocolAssociation);
                    details1.associateDetails();
                    index(publicObject);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info(Thread.currentThread().getName() + ": " + "PROTOCOL" + " END " + new Date());
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
