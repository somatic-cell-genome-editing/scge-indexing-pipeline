package edu.mcw.scge.searchIndexer.processThreads;


import edu.mcw.scge.dao.implementation.AntibodyDao;
import edu.mcw.scge.datamodel.Antibody;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.indexer.Manager;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class AntibodyIndexerThread extends Indexer implements Runnable {
    AntibodyDao antibodyDao=new AntibodyDao();
    @Override
    public void run() {
        Logger log= LogManager.getLogger(Manager.class);
        log.info(Thread.currentThread().getName() + ": " + "Antibody" + " started " + new Date());
        try {
            for(Antibody antibody:antibodyDao.getAntibodies() ) {
                IndexDocument o = new IndexDocument();
                o.setAccessLevel("consortium");
                mapDetails(o,antibody);

                List<ExperimentRecord> experimentRecords = antibodyDao.getAssociatedExperimentRecords(antibody.getAntibodyId());
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);
                index(o);
                IndexDocument publicObject=new IndexDocument();
                publicObject.setAccessLevel("public");
                mapDetails(publicObject, antibody);
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);
                index(publicObject);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info(Thread.currentThread().getName() + ": " + "Antibody" + " End " + new Date());
    }
    public void mapDetails(IndexDocument o,Antibody antibody){
        o.setId(antibody.getAntibodyId());
        o.setTier(4);
        o.setName(antibody.getRrid());
        o.setDescription(antibody.getDescription());
        o.setCategory("Antibody");
        o.setExternalLink("https://scicrunch.org/resolver/"+antibody.getRrid());
        Set<String> externalIds=new HashSet<>();
        externalIds.add(antibody.getRrid());
        externalIds.add(antibody.getOtherId());
        o.setExternalId(externalIds);
    }
}
