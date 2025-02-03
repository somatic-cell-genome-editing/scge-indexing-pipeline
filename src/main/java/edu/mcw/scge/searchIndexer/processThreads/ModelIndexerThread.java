package edu.mcw.scge.searchIndexer.processThreads;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.ModelDao;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Model;
import edu.mcw.scge.indexer.Manager;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModelIndexerThread extends Indexer implements Runnable{
    ModelDao modelDao=new ModelDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public void run() {
        Logger log= LogManager.getLogger(Manager.class);

        log.info(Thread.currentThread().getName() + ": " + "Model" + " started " + new Date());
        try {
            for(Model m:modelDao.getModels()){
                IndexDocument o=new IndexDocument();
                o.setAccessLevel("consortium");

                List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByModel(m.getModelId());

                int expCount=experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet()).size();
                o.setExperimentCount(expCount);
                mapDetails(o,m);
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);

               index(o);
                if(m.getTier()==4){
                    IndexDocument publicObject = new IndexDocument();
                    publicObject.setAccessLevel("public");
                    mapDetails(publicObject,m);
                    Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);

                    index(publicObject);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info(Thread.currentThread().getName() + ": " + "Model" + " END " + new Date());
    }
    public void mapDetails(IndexDocument o, Model m){
        o.setCategory("Model System");
        o.setId(m.getModelId());
        o.setModelType(Collections.singleton(m.getType()));
        o.setModelOrganism(Collections.singleton(m.getOrganism()));
        if(m.getDisplayName()!=null && !m.getDisplayName().equals(""))
            o.setName(m.getDisplayName());
        else
            o.setName(m.getName());
        o.setDescription(m.getDescription());
        o.setTier(m.getTier());
        o.setReportPageLink("/toolkit/data/models/model/?id=");
    }

}
