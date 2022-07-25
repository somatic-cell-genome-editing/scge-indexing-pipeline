package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.EditorDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.ExperimentRecordDao;
import edu.mcw.scge.datamodel.Editor;
import edu.mcw.scge.datamodel.ExperimentRecord;

import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class EditorIndexer implements Indexer {
    EditorDao editorDao=new EditorDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objList= new ArrayList<>();
        List<Editor> editors=editorDao.getAllEditors();
        for(Editor e:editors) {
            IndexDocument o = new IndexDocument();
            o.setCategory("Genome Editor");
            o.setId(e.getId());
            if (e.getSymbol() != null)
                o.setSymbol(e.getSymbol().trim());
            o.setDescription(e.getEditorDescription());
            o.setTier(e.getTier());
          //  o.setEditorType(Collections.singleton(StringUtils.capitalize(e.getType().trim())));
         //   o.setEditorSubType(Collections.singleton(StringUtils.capitalize(e.getSubType().trim())));
            o.setReportPageLink("/toolkit/data/editors/editor?id=");
            List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByEditor(e.getId());
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);
            objList.add(o);
        }
        return objList;
    }
}
