package edu.mcw.scge.searchIndexer.processThreads;

import edu.mcw.scge.dao.implementation.DeliveryDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.datamodel.Delivery;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DeliveryIndexerThread extends Indexer implements Runnable{
    DeliveryDao deliveryDao=new DeliveryDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public void run() {
        Logger log= LogManager.getLogger("delivery");
        log.info(Thread.currentThread().getName() + ": " + "DELIVERY" + " started " + new Date());
        try {
            for(Delivery d:deliveryDao.getDeliverySystems() ) {
                IndexDocument o = new IndexDocument();
                o.setAccessLevel("consortium");
                mapDetails(o,d);
                List<ExperimentRecord> experimentRecords = experimentDao.getExperimentsByDeliverySystem(o.getId());
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);
                index(o);
                if(d.getTier()==4){
                    IndexDocument publicObject = new IndexDocument();
                    publicObject.setAccessLevel("public");
                    mapDetails(publicObject,d);
                    Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);

                   index(publicObject);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info(Thread.currentThread().getName() + ": " + "Delivery" + " End " + new Date());
    }
    public void mapDetails(IndexDocument o,Delivery d){
        o.setId(d.getId());
        o.setName(d.getName());
        o.setDescription(d.getDescription());
        o.setCategory("Delivery System");
        o.setTier(d.getTier());
        o.setReportPageLink("/toolkit/data/delivery/system?id=");
    }

}
