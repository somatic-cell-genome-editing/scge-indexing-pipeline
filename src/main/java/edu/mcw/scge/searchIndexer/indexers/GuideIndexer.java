package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.GuideDao;
import edu.mcw.scge.datamodel.*;

import edu.mcw.scge.indexerRefactored.indexer.GuideDetails;
import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;

import java.util.*;

public class GuideIndexer extends Indexer<Guide> implements ObjectIndexer {
    GuideDao guideDao=new GuideDao();

    @Override
    List<Guide> getObjects() throws Exception {
        return guideDao.getGuides();
    }

    @Override
    public void  getIndexObjects() throws Exception {
        for(Guide g: getObjects()){
            ObjectDetails<Guide> objectDetails=new GuideDetails(g);
            indexObjects(objectDetails);
        }
    }

}
