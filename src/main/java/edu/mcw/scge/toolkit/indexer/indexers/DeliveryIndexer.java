package edu.mcw.scge.toolkit.indexer.indexers;

import edu.mcw.scge.datamodel.Delivery;

import edu.mcw.scge.toolkit.indexer.index.DeliveryDetails;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;

import java.util.*;

public class DeliveryIndexer extends Indexer<Delivery> implements ObjectIndexer {

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
