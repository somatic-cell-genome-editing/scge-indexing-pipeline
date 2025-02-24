package edu.mcw.scge.toolkit.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.GrantDao;
import edu.mcw.scge.dao.implementation.StudyDao;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class zeroExperimentRecordsMapper  {

    ExperimentDao experimentDao=new ExperimentDao();
    StudyDao studyDao=new StudyDao();
    GrantDao grantDao=new GrantDao();
   public void mapExperiment(long experimentId) throws Exception {
       Experiment ex=experimentDao.getExperiment(experimentId);
       Study study=studyDao.getStudyByStudyId(ex.getStudyId());
       Grant grant=grantDao.getGrantByGroupId(study.getGroupId());
       List<Person> grantPi=grantDao.getGrantPi(grant.getGrantId());
       IndexDocument o = new IndexDocument();
       o.setAccessLevel("consortium");
       mapDetails(ex, o,study);
       Map<Integer, String> studyMap=new HashMap<>();
       studyMap.put(study.getStudyId(), study.getStudy());
       o.setStudyNames(studyMap);

       //objects.add(o);
       if(study.getTier()==4){
           IndexDocument publicObject = new IndexDocument();
           publicObject.setAccessLevel("public");
           mapDetails(ex,publicObject,study);

        //   objects.add(publicObject);
       }
   }
    public void mapDetails(Experiment x, IndexDocument o, Study s) throws Exception {

        o.setId(x.getExperimentId());
        //   o.setExperimentType(Collections.singleton(x.getType()));
        if(x.getLastModifiedDate()!=null)
            o.setLastModifiedDate(x.getLastModifiedDate().toString());
        else{
            o.setLastModifiedDate(s.getSubmissionDate().toString());
        }
        o.setCategory("Experiment");
        o.setName(x.getName());
        o.setReportPageLink("/toolkit/data/experiments/experiment/");
        o.setTier(s.getTier());
        o.setStudy(Stream.of(s.getStudy()).collect(Collectors.toSet()));
        o.setStudyId(s.getStudyId());
        Set<String> pis=new HashSet<>();
        for(Person pi:s.getMultiplePis()){
            if(pi.getFirstName()!=null && !pi.getFirstName().equals(""))
                pis.add(pi.getLastName()+" "+ pi.getFirstName());
            else pis.add(pi.getName());
        }
        o.setPi(pis);


    }
}
