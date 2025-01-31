package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.GroupDAO;
import edu.mcw.scge.dao.implementation.StudyDao;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

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
        Set<String> projectMembers=new HashSet<>();
        Set<Integer> studyIds=experimentRecords.stream().map(e->e.getStudyId()).collect(Collectors.toSet());

        if(studyIds.size()>0) {
            for(int studyId:studyIds){
                List<Study> studies = studyDao.getStudyById(studyId);
                if (studies != null && studies.size() > 0) {
                  Study  study = studies.get(0);
                  List<Experiment> experiments=experimentDao.getExperimentsByStudy(studyId);

                    projectMembers.addAll(groupDAO.getGroupMembersByGroupId(study.getGroupId()).stream().map(p -> p.getName()).collect(Collectors.toSet()));
                    if (indexDocument.getAccessLevel().equalsIgnoreCase("consortium")
                            || (indexDocument.getAccessLevel().equalsIgnoreCase("public") && study.getTier() == 4)) {
                        for(ExperimentRecord record:experimentRecords){
                            if(record.getStudyId()==studyId){
                                if (record.getSex() != null && !record.getSex().equals(""))
                                    sex.add(record.getSex());
                                if (record.getGenotype() != null && !record.getGenotype().equals(""))
                                    genotype.add(record.getGenotype());
                                experimentType.addAll(experiments.stream().map(e->e.getType()).collect(Collectors.toSet()));
                            }
                        }
                    }

                }
            }
            if(!projectMembers.isEmpty()) indexDocument.setProjectMembers(projectMembers);
            if(!experimentType.isEmpty()) indexDocument.setExperimentType(experimentType);
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
