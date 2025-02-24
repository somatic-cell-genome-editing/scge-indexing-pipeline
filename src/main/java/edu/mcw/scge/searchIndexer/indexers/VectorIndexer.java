package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.VectorDao;
import edu.mcw.scge.datamodel.Vector;

import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;
import edu.mcw.scge.indexerRefactored.indexer.VectorDetails;

import java.util.*;

public class VectorIndexer extends Indexer<Vector> implements ObjectIndexer {
    VectorDao vectorDao=new VectorDao();

    @Override
    List<Vector> getObjects() throws Exception {
        return vectorDao.getAllVectors();
    }
    @Override
    public void getIndexObjects() throws Exception {

        for(Vector e:getObjects()){
            ObjectDetails<Vector> objectDetails=new VectorDetails(e);
           indexObjects(objectDetails);
        }

    }

}
