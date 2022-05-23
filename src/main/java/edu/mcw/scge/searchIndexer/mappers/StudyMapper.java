package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.StudyDao;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;

public class StudyMapper implements Mapper {
    StudyDao studyDao=new StudyDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<String> studies=new HashSet<>();
        Set<String> pi=new HashSet<>();
        Set<String> experimentName=new HashSet<>();
        Set<String> experimentType=new HashSet<>();
        Map<Integer, String> studyMap=new HashMap<>();
        Map<Long, String> experimentMap=new HashMap<>();
        Set<Long> experimentIds=experimentRecords.stream()
                .map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
        for(Long experimentId:experimentIds){
            Study study=new Study();
            try {
              List<Study> studyList=  studyDao.getStudyByExperimentId(experimentId);
              if(studyList.size()>0) {
                  study = studyList.get(0);
                  studies.add(study.getStudy());
                  studyMap.put(study.getStudyId(), study.getStudy());
                  pi.add(study.getPiLastName()+" "+ study.getPiFirstName());
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
        indexDocument.setStudyNames(studyMap);
        indexDocument.setExperimentNames(experimentMap);
        indexDocument.setStudy(studies);
        indexDocument.setPi(pi);
        indexDocument.setExperimentName(experimentName);
        indexDocument.setExperimentType(experimentType);
    }
}
