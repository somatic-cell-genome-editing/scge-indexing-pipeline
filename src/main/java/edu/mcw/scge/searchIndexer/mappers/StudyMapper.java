package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.GrantDao;
import edu.mcw.scge.dao.implementation.StudyDao;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Grant;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.process.UI;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;

public class StudyMapper implements Mapper {
    StudyDao studyDao=new StudyDao();
    ExperimentDao experimentDao=new ExperimentDao();
    GrantDao grantDao=new GrantDao();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<String> studies=new HashSet<>();
        Set<String> pi=new HashSet<>();
        Set<String> experimentName=new HashSet<>();
        Set<String> experimentType=new HashSet<>();
        Map<Integer, String> studyMap=new HashMap<>();
        Map<Long, String> experimentMap=new HashMap<>();
        Set<String> grantInitiatives= new HashSet<>();
        Set<Long> experimentIds=experimentRecords.stream()
                .map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
        for(Long experimentId:experimentIds){
            Study study=new Study();
            try {
              List<Study> studyList=  studyDao.getStudyByExperimentId(experimentId);
              if(studyList.size()>0) {
                  study = studyList.get(0);
                  studies.add(study.getStudy());
                  Grant grant=grantDao.getGrantByGroupId(study.getGroupId());
                  grantInitiatives.add(UI.getLabel( grant.getGrantInitiative()));
                  studyMap.put(study.getStudyId(), study.getStudy());
                  if(study.getPiFirstName()!=null && study.getPiLastName()!=null)
                  pi.add(study.getPiLastName()+" "+ study.getPiFirstName());
                  else
                      if(study.getPi()!=null)
                      pi.add(study.getPi().replaceAll(",", " "));
                      else System.err.println("NO PI NAME:"+study.getStudyId());
              }
            }catch (Exception e){
                System.out.println("EXPERIMENT ID:"+experimentId);
                e.printStackTrace();
            }

            Experiment experiment=experimentDao.getExperiment(experimentId);
            experimentName.add(experiment.getName());
            experimentType.add(experiment.getType());
            experimentMap.put(experiment.getExperimentId(), experiment.getName());
        }
        if(indexDocument.getCategory().equalsIgnoreCase("Study"))
        indexDocument.setInitiative(grantInitiatives);

        indexDocument.setStudyNames(studyMap);
        indexDocument.setExperimentNames(experimentMap);
        indexDocument.setStudy(studies);

        indexDocument.setExperimentName(experimentName);
        if(indexDocument.getCategory().equalsIgnoreCase("Study") || indexDocument.getCategory().equalsIgnoreCase("Experiment"))
        {
            indexDocument.setExperimentType(experimentType);
            indexDocument.setPi(pi);
        }
    }
}
