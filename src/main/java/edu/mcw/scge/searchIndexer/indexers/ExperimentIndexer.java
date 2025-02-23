package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.datamodel.*;


import edu.mcw.scge.indexerRefactored.indexer.model.AccessLevel;
import edu.mcw.scge.indexerRefactored.indexer.ExperimentDetails;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;


public class ExperimentIndexer extends Indexer<Experiment> {
    ExperimentDao experimentDao=new ExperimentDao();

    @Override
    List<Experiment> getObjects() throws Exception {
        return experimentDao.getAllExperiments();
    }

    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        return getExperiments();
    }
    public List<IndexDocument> getExperiments() throws  Exception{
        List<IndexDocument> objects= new ArrayList<>();
            for (Experiment x : getObjects()) {
                ExperimentDetails experimentDetails=new ExperimentDetails(x);
                IndexDocument consortiumDoc = experimentDetails.getIndexObject(AccessLevel.CONSORTIUM);
                if(consortiumDoc!=null){
                    objects.add(consortiumDoc);
                }
                IndexDocument publicDoc = experimentDetails.getIndexObject(AccessLevel.PUBLIC);
                if(publicDoc!=null){
                    objects.add(publicDoc);
                }

            }

        return objects;
    }
//public void mapDetails(Experiment x, IndexDocument o, Study s) throws Exception {
//
//    o.setId(x.getExperimentId());
//    //   o.setExperimentType(Collections.singleton(x.getType()));
//    if(x.getLastModifiedDate()!=null)
//        o.setLastModifiedDate(x.getLastModifiedDate().toString());
//    else{
//        o.setLastModifiedDate(s.getSubmissionDate().toString());
//    }
//    o.setCategory("Experiment");
//    o.setName(x.getName());
//    o.setReportPageLink("/toolkit/data/experiments/experiment/");
//    o.setTier(s.getTier());
//    o.setStudy(Stream.of(s.getStudy()).collect(Collectors.toSet()));
//    o.setStudyId(s.getStudyId());
//    Set<String> pis=new HashSet<>();
//    for(Person pi:s.getMultiplePis()){
//        if(pi.getFirstName()!=null && !pi.getFirstName().equals(""))
//            pis.add(pi.getLastName()+" "+ pi.getFirstName());
//        else pis.add(pi.getName());
//    }
//    o.setPi(pis);
//    o.setDescription(x.getDescription());
//  //  o.setGeneratedDescription(x.getDescription());
//    List<ExperimentRecord> experimentRecords=experimentDao.getExperimentRecords(x.getExperimentId());
//    Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);
//
//
//}
}
