package edu.mcw.scge.toolkit.indexer.indexers;

import edu.mcw.scge.datamodel.*;

import edu.mcw.scge.toolkit.indexer.index.GuideDetails;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;

import java.util.*;

public class GuideIndexer extends Indexer<Guide> implements ObjectIndexer {

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
