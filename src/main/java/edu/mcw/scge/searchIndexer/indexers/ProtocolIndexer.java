package edu.mcw.scge.searchIndexer.indexers;


import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.ProtocolDao;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.datamodel.ProtocolAssociation;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ProtocolIndexer implements Indexer {
    ProtocolDao protocolDao=new ProtocolDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objects=new ArrayList<>();

        for(Protocol protocol:protocolDao.getProtocols()){
            IndexDocument o=new IndexDocument();
            o.setAccessLevel("consortium");
            mapDetails(o,protocol);
            ProtocolAssociation protocolAssociation=protocolDao.getProtocolAssociations(protocol.getId());
            List<ExperimentRecord> experimentRecords=new ArrayList<>();
            for(Experiment experiment:protocolAssociation.getAssociatedExperiments()){
            experimentRecords.addAll(experimentDao.getExperimentRecords(experiment.getExperimentId()));
            }
            if(experimentRecords.size()>0)
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);
            objects.add(o);
            if(protocol.getTier()==4){
                IndexDocument publicObject = new IndexDocument();
                publicObject.setAccessLevel("public");
                mapDetails(publicObject,protocol);
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);

                objects.add(publicObject);
            }
        }
        return objects;
    }
    public void mapDetails(IndexDocument o, Protocol protocol){
        o.setCategory("Protocol");
        o.setId(protocol.getId());
        o.setName(protocol.getFilename());
        o.setDescription(protocol.getDescription());
        o.setTier(protocol.getTier());
        o.setReportPageLink("/toolkit/data/protocols/protocol/?id=");
    }

}
