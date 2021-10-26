package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.GuideDao;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.indexer.dao.delivery.Crawler;
import edu.mcw.scge.indexer.model.IndexObject;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class GuideIndexer implements Indexer {

    ExperimentDao experimentDao=new ExperimentDao();
    GuideDao guideDao=new GuideDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objList = new ArrayList<>();
        for(Guide g: guideDao.getGuides()){
            IndexDocument o=new IndexDocument();
            o.setCategory("Guide");
            o.setId(g.getGuide_id());
            o.setName(g.getGuide());
            o.setDescription(g.getGuideDescription());
            o.setTier(g.getTier());
            o.setReportPageLink("/toolkit/data/guide/system?id=");
            List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByGuide(g.getGuide_id());
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);
            objList.add(o);
        }
        return objList;
    }

}
