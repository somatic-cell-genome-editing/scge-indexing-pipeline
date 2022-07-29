package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.GuideDao;
import edu.mcw.scge.datamodel.*;

import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;

public class GuideIndexer implements Indexer {

    ExperimentDao experimentDao=new ExperimentDao();
    GuideDao guideDao=new GuideDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objList = new ArrayList<>();
        for(Guide g: guideDao.getGuides()){
            IndexDocument o=new IndexDocument();
            o.setAccessLevel("consortium");

            mapDetails(o,g);
            List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByGuide(g.getGuide_id());
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);
            objList.add(o);
            if(g.getTier()==4){
                IndexDocument publicObject = new IndexDocument();
                publicObject.setAccessLevel("public");
                mapDetails(publicObject,g);
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);

                objList.add(publicObject);
            }
        }
        return objList;
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
