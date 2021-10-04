package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.datamodel.*;

import edu.mcw.scge.datamodel.ontologyx.Term;
import edu.mcw.scge.datamodel.ontologyx.TermSynonym;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExperimentIndexer implements Indexer {
    StudyDao studyDao=new StudyDao();
    ExperimentDao experimentDao=new ExperimentDao();

    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        return getExperiments();
    }
    public List<IndexDocument> getExperiments() throws  Exception{
        List<IndexDocument> objects= new ArrayList<>();
        List<Study> studies=studyDao.getStudies();
        for(Study s: studies) {
            List<Experiment> experiments = experimentDao.getExperimentsByStudy(s.getStudyId());
            for (Experiment x : experiments) {
                IndexDocument o = new IndexDocument();

                o.setId(x.getExperimentId());
                o.setType(x.getType());
                o.setCategory("Experiment");
                o.setName(x.getName());
                o.setReportPageLink("/toolkit/data/experiments/experiment/");
                o.setTier(s.getTier());
                o.setStudy(Stream.of(s.getStudy()).collect(Collectors.toSet()));
                o.setPi(Stream.of(s.getPi()).collect(Collectors.toSet()));
                o.setDescription(x.getDescription());

                o.setGeneratedDescription(x.getDescription());
                List<ExperimentRecord> experimentRecords=experimentDao.getExperimentRecords(x.getExperimentId());
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);

                objects.add(o);
            }
        }
        return objects;
    }

}
