package edu.mcw.scge.searchIndexer.indexers;


import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.ProtocolDao;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Model;
import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.ArrayList;
import java.util.List;


public class ProtocolIndexer implements Indexer {
    ProtocolDao protocolDao=new ProtocolDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objects=new ArrayList<>();
        List<Protocol> protocols=protocolDao.getProtocols();
        System.out.print("PROTOCOLS SIZE:"+protocols.size() );
        for(Protocol protocol:protocolDao.getProtocols()){
            IndexDocument o=new IndexDocument();
            o.setCategory("Protocol");
            o.setId(protocol.getId());
            // o.setModelType(Collections.singleton(m.getType()));
            o.setName(protocol.getFilename());
            //  o.setModelSubtype(Collections.singleton(m.getSubtype()));
            o.setDescription(protocol.getDescription());
            o.setTier(protocol.getTier());
            o.setReportPageLink("https://scge.mcw.edu/toolkit/data/protocols/protocol/?id=");
          //  List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByModel(m.getModelId());
          //  int expCount=experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet()).size();
         //   o.setExperimentCount(expCount);

         //   Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);

            objects.add(o);
        }
        return objects;
    }


}
