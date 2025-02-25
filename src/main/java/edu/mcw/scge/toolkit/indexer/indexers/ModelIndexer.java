package edu.mcw.scge.toolkit.indexer.indexers;

import edu.mcw.scge.datamodel.Model;

import edu.mcw.scge.toolkit.indexer.index.ModelDetails;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;

import java.util.*;

public class ModelIndexer extends Indexer<Model> implements ObjectIndexer {

    @Override
    List<Model> getObjects() throws Exception {
        return modelDao.getModels();
    }

    @Override
    public void getIndexObjects() throws Exception {
        for(Model m:getObjects()){
            ObjectDetails<Model> objectDetails=new ModelDetails(m);
            indexObjects(objectDetails);
        }
    }

}
