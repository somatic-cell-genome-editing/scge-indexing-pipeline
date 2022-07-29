package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.StudyDao;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ExperimentMapper implements Mapper {
    ExperimentDao experimentDao=new ExperimentDao();
    StudyDao studyDao=new StudyDao();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {

        Set<String> genotype=new HashSet<>();
        Set<String> sex=new HashSet<>();
        Set<String> experimentType=new HashSet<>();
        Set<Long> experimentIds=new HashSet<>();
        for(ExperimentRecord record:experimentRecords) {
            Study study = studyDao.getStudyByExperimentId(record.getExperimentId()).get(0);
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
        }
        for(long experiemntId:experimentIds){
           Experiment experiment= experimentDao.getExperiment(experiemntId);
           Study study=studyDao.getStudyByExperimentId(experiemntId).get(0);
            if (indexDocument.getAccessLevel().equalsIgnoreCase("consortium")
                    || (indexDocument.getAccessLevel().equalsIgnoreCase("public") && study.getTier() == 4)) {
                experimentType.add(experiment.getType());
            }
        }
        indexDocument.setExperimentType(experimentType);
        if(!sex.isEmpty())indexDocument.setSex(sex);
     //   if(!samplePrep.isEmpty())indexDocument.setSamplePrep(samplePrep);
     //   if(!experimentName.isEmpty())indexDocument.setExperimentName(experimentName);
        if(!genotype.isEmpty())indexDocument.setGenotype(genotype);


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
