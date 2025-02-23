package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.DeliveryDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.datamodel.Delivery;
import edu.mcw.scge.datamodel.ExperimentRecord;

import edu.mcw.scge.indexerRefactored.indexer.DeliveryDetails;
import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;

public class DeliveryIndexer extends Indexer<Delivery> {
    DeliveryDao deliveryDao=new DeliveryDao();

    @Override
    List<Delivery> getObjects() throws Exception {
        return deliveryDao.getDeliverySystems();
    }

    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objects=new ArrayList<>();
        for(Delivery d: getObjects()) {
            ObjectDetails<Delivery> objectDetails=new DeliveryDetails(d);
            objects.addAll(indexObjects(objectDetails));
        }
        return objects;
    }

}
