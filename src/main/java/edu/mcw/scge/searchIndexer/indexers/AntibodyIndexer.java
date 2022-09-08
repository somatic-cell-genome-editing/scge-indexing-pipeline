package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.AntibodyDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.datamodel.Antibody;
import edu.mcw.scge.datamodel.Delivery;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.indexer.model.IndexObject;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;

public class AntibodyIndexer implements Indexer {
    AntibodyDao antibodyDao=new AntibodyDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objects=new ArrayList<>();
        for(Antibody antibody:antibodyDao.getAntibodies() ) {
            IndexDocument o = new IndexDocument();
            o.setAccessLevel("consortium");
            mapDetails(o,antibody);

            List<ExperimentRecord> experimentRecords = antibodyDao.getAssociatedExperimentRecords(antibody.getAntibodyId());
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);
            objects.add(o);
            IndexDocument publicObject=new IndexDocument();
            publicObject.setAccessLevel("public");
            mapDetails(publicObject, antibody);
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);
            objects.add(publicObject);
        }
        return objects;
    }
    public void mapDetails(IndexDocument o,Antibody antibody){
        o.setId(antibody.getAntibodyId());
        o.setTier(4);
        // o.setDeliveryType(Collections.singleton(d.getType()));
        //  o.setDeliverySubtype(Collections.singleton(d.getSubtype()));
        o.setName(antibody.getRrid());
        o.setDescription(antibody.getDescription());
        o.setCategory("Antibody");
      //  o.setTier(antibody.getTier());
      //  o.setReportPageLink("/toolkit/data/delivery/system?id=");
        o.setExternalLink("https://scicrunch.org/resolver/"+antibody.getRrid());
        Set<String> externalIds=new HashSet<>();
        externalIds.add(antibody.getRrid());
        externalIds.add(antibody.getOtherId());
        o.setExternalId(externalIds);
    }


}
