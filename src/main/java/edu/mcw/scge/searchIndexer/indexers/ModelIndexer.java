package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.ModelDao;
import edu.mcw.scge.datamodel.Model;

import edu.mcw.scge.indexerRefactored.indexer.ModelDetails;
import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;

public class ModelIndexer extends Indexer<Model> {
    ModelDao modelDao=new ModelDao();

    @Override
    List<Model> getObjects() throws Exception {
        return modelDao.getModels();
    }

    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objects=new ArrayList<>();
        for(Model m:getObjects()){
            ObjectDetails<Model> objectDetails=new ModelDetails(m);
            objects.addAll(indexObjects(objectDetails));
        }
        return objects;
    }

}
