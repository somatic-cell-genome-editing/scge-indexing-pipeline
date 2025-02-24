package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.AntibodyDao;
import edu.mcw.scge.datamodel.Antibody;

import edu.mcw.scge.indexerRefactored.indexer.AntibodyDetails;
import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;

import java.util.*;

public class AntibodyIndexer extends Indexer<Antibody> implements ObjectIndexer {
    AntibodyDao antibodyDao=new AntibodyDao();

    @Override
    List<Antibody> getObjects() throws Exception {
        return antibodyDao.getAntibodies();
    }

    @Override
    public void getIndexObjects() throws Exception {
        for(Antibody antibody: getObjects()) {
            ObjectDetails<Antibody> objectDetails=new AntibodyDetails(antibody);
            indexObjects(objectDetails);
        }
    }

}
