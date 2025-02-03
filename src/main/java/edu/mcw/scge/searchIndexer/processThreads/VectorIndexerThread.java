package edu.mcw.scge.searchIndexer.processThreads;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.VectorDao;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Vector;
import edu.mcw.scge.indexer.Manager;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class VectorIndexerThread extends Indexer implements Runnable{
    VectorDao vectorDao=new VectorDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public void run() {
        Logger log= LogManager.getLogger(Manager.class);

        log.info(Thread.currentThread().getName() + ": " + "VECTOR" + " started " + new Date());
        try {
            for(Vector e:vectorDao.getAllVectors()){
                IndexDocument o=new IndexDocument();
                o.setAccessLevel("consortium");
                mapDetails(o, e);
                List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByVector(e.getVectorId());
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);


                index(o);
                if(e.getTier()==4){
                    IndexDocument publicObject = new IndexDocument();
                    publicObject.setAccessLevel("public");
                    mapDetails(publicObject,e);
                    Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);

                    index(publicObject);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info(Thread.currentThread().getName() + ": " + "VECTOR" + " started " + new Date());
    }
    public void mapDetails(IndexDocument o, Vector e){
        o.setCategory("Vector");
        o.setId(e.getVectorId());
        if(e.getType()!=null)
            if(e.getSubtype()!=null)
                if(e.getName()!=null)
                    o.setSymbol(e.getName().trim());
        o.setDescription(e.getDescription());
        o.setTier(e.getTier());
        o.setReportPageLink("/toolkit/data/vector/format?id=");
    }
}
