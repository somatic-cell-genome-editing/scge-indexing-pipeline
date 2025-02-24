package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.datamodel.*;


import edu.mcw.scge.indexerRefactored.indexer.ExperimentDetails;
import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;


import java.util.*;


public class ExperimentIndexer extends Indexer<Experiment> implements ObjectIndexer  {
    ExperimentDao experimentDao = new ExperimentDao();

    @Override
    List<Experiment> getObjects() throws Exception {
        return experimentDao.getAllExperiments();
    }
    public void getIndexObjects() throws Exception {
        getExperiments();
    }

    public void getExperiments() throws Exception {
        for (Experiment x : getObjects()) {
            ObjectDetails<Experiment> objectDetails = new ExperimentDetails(x);
            indexObjects(objectDetails);
        }
    }

}
