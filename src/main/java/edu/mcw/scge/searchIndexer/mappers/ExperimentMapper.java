package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.GroupDAO;
import edu.mcw.scge.dao.implementation.StudyDao;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.indexerRefactored.indexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;

public class ExperimentMapper implements Mapper {
    ExperimentDao experimentDao=new ExperimentDao();
    StudyDao studyDao=new StudyDao();
    GroupDAO groupDAO=new GroupDAO();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {

        Set<String> genotype=new HashSet<>();
        Set<String> sex=new HashSet<>();
        Set<String> experimentType=new HashSet<>();
        Set<Long> experimentIds=new HashSet<>();
        Set<String> projectMembers=new HashSet<>();
        Map<Long, Study> experimentStudyMap=new HashMap<>();
        Set<Integer> studyIds=experimentRecords.stream().map(e->e.getStudyId()).collect(Collectors.toSet());
        if(studyIds.size()>0) {
            for (ExperimentRecord record : experimentRecords) {
                Study study = experimentStudyMap.get(record.getExperimentId());
                if (study == null) {
                    List<Study> studies = studyDao.getStudyByExperimentId(record.getExperimentId());
                    if (studies != null && studies.size() > 0) {
                        study = studies.get(0);
                        experimentStudyMap.put(record.getExperimentId(), study);

                    }
                }

                if (study != null) {
                    if (indexDocument.getAccessLevel().equalsIgnoreCase("consortium")
                            || (indexDocument.getAccessLevel().equalsIgnoreCase("public") && study.getTier() == 4)) {
                        if (record.getSex() != null && !record.getSex().equals(""))
                            sex.add(record.getSex());
                        //  if(record.getSamplePrep()!=null && !record.getSamplePrep().equals(""))
                        //       samplePrep.add(record.getSamplePrep());
                        if (record.getGenotype() != null && !record.getGenotype().equals(""))
                            genotype.add(record.getGenotype());
                        experimentIds.add(record.getExperimentId());
                    }
                    projectMembers.addAll(groupDAO.getGroupMembersByGroupId(study.getGroupId()).stream().map(p -> p.getName()).collect(Collectors.toSet()));
                }
            }
            indexDocument.setProjectMembers(projectMembers);
            for (long experiemntId : experimentIds) {
                Experiment experiment = experimentDao.getExperiment(experiemntId);
                Study study = experimentStudyMap.get(experiemntId);
                if (study == null) {
                    study = studyDao.getStudyByExperimentId(experiemntId).get(0);
                    experimentStudyMap.put(experiemntId, study);
                }
                if (indexDocument.getAccessLevel().equalsIgnoreCase("consortium")
                        || (indexDocument.getAccessLevel().equalsIgnoreCase("public") && study.getTier() == 4)) {
                    experimentType.add(experiment.getType());
                }
            }
            indexDocument.setExperimentType(experimentType);
            if (!sex.isEmpty()) indexDocument.setSex(sex);
            //   if(!samplePrep.isEmpty())indexDocument.setSamplePrep(samplePrep);
            //   if(!experimentName.isEmpty())indexDocument.setExperimentName(experimentName);
            if (!genotype.isEmpty()) indexDocument.setGenotype(genotype);


            Objects.requireNonNull(MapperFactory.getMapper("editor")).mapFields(experimentRecords, indexDocument);


            Objects.requireNonNull(MapperFactory.getMapper("guide")).mapFields(experimentRecords, indexDocument);


            Objects.requireNonNull(MapperFactory.getMapper("vector")).mapFields(experimentRecords, indexDocument);


            Objects.requireNonNull(MapperFactory.getMapper("delivery")).mapFields(experimentRecords, indexDocument);


            Objects.requireNonNull(MapperFactory.getMapper("model")).mapFields(experimentRecords, indexDocument);
            Objects.requireNonNull(MapperFactory.getMapper("antibody")).mapFields(experimentRecords, indexDocument);


            Objects.requireNonNull(MapperFactory.getMapper("tissue")).mapFields(experimentRecords, indexDocument);
            //   Objects.requireNonNull(MapperFactory.getMapper("application")).mapFields(experimentRecords, indexDocument);

            Objects.requireNonNull(MapperFactory.getMapper("study")).mapFields(experimentRecords, indexDocument);
        }

    }
}
