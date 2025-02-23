package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.AntibodyDao;
import edu.mcw.scge.datamodel.Antibody;

import edu.mcw.scge.indexerRefactored.indexer.AntibodyDetails;
import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;

public class AntibodyIndexer extends Indexer<Antibody> {
    AntibodyDao antibodyDao=new AntibodyDao();

    @Override
    List<Antibody> getObjects() throws Exception {
        return antibodyDao.getAntibodies();
    }

    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objects=new ArrayList<>();
        for(Antibody antibody: getObjects()) {
            ObjectDetails<Antibody> objectDetails=new AntibodyDetails(antibody);
            objects.addAll(indexObjects(objectDetails));
        }
        return objects;
    }

}
