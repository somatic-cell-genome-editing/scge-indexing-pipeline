package edu.mcw.scge.toolkit.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.DeliveryDao;
import edu.mcw.scge.datamodel.Delivery;

import edu.mcw.scge.toolkit.indexer.index.DeliveryDetails;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;

import java.util.*;

public class DeliveryIndexer extends Indexer<Delivery> implements ObjectIndexer {
    DeliveryDao deliveryDao=new DeliveryDao();

    @Override
    List<Delivery> getObjects() throws Exception {
        return deliveryDao.getDeliverySystems();
    }

    @Override
    public void getIndexObjects() throws Exception {
        for(Delivery d: getObjects()) {
            ObjectDetails<Delivery> objectDetails=new DeliveryDetails(d);
            indexObjects(objectDetails);
        }

    }

}
