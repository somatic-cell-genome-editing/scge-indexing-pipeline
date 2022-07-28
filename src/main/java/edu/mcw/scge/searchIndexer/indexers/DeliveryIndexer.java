package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.DeliveryDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.datamodel.Delivery;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.indexer.dao.delivery.Crawler;
import edu.mcw.scge.indexer.model.IndexObject;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;

public class DeliveryIndexer implements Indexer {
    DeliveryDao deliveryDao=new DeliveryDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objects=new ArrayList<>();
        for(Delivery d:deliveryDao.getDeliverySystems() ) {
            IndexDocument o = new IndexDocument();
            o.setAccessLevel("consortium");
            mapDetails(o,d);
            List<ExperimentRecord> experimentRecords = experimentDao.getExperimentsByDeliverySystem(o.getId());
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);
            objects.add(o);
            if(d.getTier()==4){
                IndexDocument publicObject = new IndexDocument();
                publicObject.setAccessLevel("public");
                mapDetails(publicObject,d);
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);

                objects.add(publicObject);
            }
        }
        return objects;
    }
    public void mapDetails(IndexDocument o,Delivery d){
        o.setId(d.getId());
        // o.setDeliveryType(Collections.singleton(d.getType()));
        //  o.setDeliverySubtype(Collections.singleton(d.getSubtype()));
        o.setName(d.getName());
        o.setDescription(d.getDescription());
        o.setCategory("Delivery System");
        o.setTier(d.getTier());
        o.setReportPageLink("/toolkit/data/delivery/system?id=");
    }
}
