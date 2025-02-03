package edu.mcw.scge.searchIndexer.processThreads;

import edu.mcw.scge.dao.implementation.EditorDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.datamodel.Editor;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.indexer.Manager;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EditorInderThread extends Indexer implements Runnable{
    EditorDao editorDao=new EditorDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public void run()  {
        Logger log= LogManager.getLogger(Manager.class);

        log.info(Thread.currentThread().getName() + ": " + "Editor" + " started " + new Date());
        List<Editor> editors= null;
        try {
            editors = editorDao.getAllEditors();

        for(Editor e:editors) {
            IndexDocument o = new IndexDocument();
            o.setAccessLevel("consortium");

            mapDetails(o, e);
            List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByEditor(e.getId());
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
        log.info(Thread.currentThread().getName() + ": " + "Editor" + " END " + new Date());

    }
    public void mapDetails(IndexDocument o, Editor e){
        o.setCategory("Genome Editor");
        o.setId(e.getId());
        if (e.getSymbol() != null)
            o.setSymbol(e.getSymbol().trim());
        o.setDescription(e.getEditorDescription());
        o.setTier(e.getTier());
        o.setReportPageLink("/toolkit/data/editors/editor?id=");
    }
}
