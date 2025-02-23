package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.GuideDao;
import edu.mcw.scge.datamodel.*;

import edu.mcw.scge.indexerRefactored.indexer.GuideDetails;
import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;

public class GuideIndexer extends Indexer<Guide> {
    GuideDao guideDao=new GuideDao();

    @Override
    List<Guide> getObjects() throws Exception {
        return guideDao.getGuides();
    }

    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objList = new ArrayList<>();
        for(Guide g: getObjects()){
            ObjectDetails<Guide> objectDetails=new GuideDetails(g);
            objList.addAll(indexObjects(objectDetails));
        }
        return objList;
    }

}
