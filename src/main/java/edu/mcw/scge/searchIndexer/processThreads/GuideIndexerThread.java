package edu.mcw.scge.searchIndexer.processThreads;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.GuideDao;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Guide;
import edu.mcw.scge.indexer.Manager;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GuideIndexerThread extends Indexer implements Runnable{
    GuideDao guideDao=new GuideDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public void run() {
        Logger log= LogManager.getLogger(Manager.class);

        log.info(Thread.currentThread().getName() + ": " + "GUIDE" + " started " + new Date());
        try {
            for(Guide g: guideDao.getGuides()){
                IndexDocument o=new IndexDocument();
                o.setAccessLevel("consortium");

                mapDetails(o,g);
                List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByGuide(g.getGuide_id());
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);
                index(o);
                if(g.getTier()==4){
                    IndexDocument publicObject = new IndexDocument();
                    publicObject.setAccessLevel("public");
                    mapDetails(publicObject,g);
                    Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);

                    index(publicObject);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info(Thread.currentThread().getName() + ": " + "GUIDE" + " started " + new Date());
    }
    public void mapDetails(IndexDocument o, Guide g){
        o.setCategory("Guide");
        o.setId(g.getGuide_id());
        o.setName(g.getGuide());
        o.setDescription(g.getGuideDescription());
        o.setTier(g.getTier());
        o.setReportPageLink("/toolkit/data/guide/system?id=");
    }
}
