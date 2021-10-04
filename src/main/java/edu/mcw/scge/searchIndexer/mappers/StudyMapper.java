package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.StudyDao;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        Set<Long> experimentIds=experimentRecords.stream()
                .map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
        for(Long experimentId:experimentIds){
            Study study=studyDao.getStudyByExperimentId(experimentId).get(0);
            studies.add(study.getStudy());
            pi.add(study.getPi());
            Experiment experiment=experimentDao.getExperiment(experimentId);
            experimentName.add(experiment.getName());
            experimentType.add(experiment.getType());
        }
        indexDocument.setStudy(studies);
        indexDocument.setPi(pi);
        indexDocument.setExperimentName(experimentName);
        indexDocument.setExperimentType(experimentType);
    }
}
