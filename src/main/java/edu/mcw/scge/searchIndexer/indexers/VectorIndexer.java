package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.VectorDao;
import edu.mcw.scge.datamodel.Vector;

import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;
import edu.mcw.scge.indexerRefactored.indexer.VectorDetails;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;

public class VectorIndexer extends Indexer<Vector> {
    VectorDao vectorDao=new VectorDao();

    @Override
    List<Vector> getObjects() throws Exception {
        return vectorDao.getAllVectors();
    }
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objList= new ArrayList<>();
        for(Vector e:getObjects()){
            ObjectDetails<Vector> objectDetails=new VectorDetails(e);
            objList.addAll(indexObjects(objectDetails));
        }
        return objList;
    }

}
