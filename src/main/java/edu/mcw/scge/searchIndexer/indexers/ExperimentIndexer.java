package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.datamodel.*;


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
            Map<Integer, String> studyMap=new HashMap<>();
            studyMap.put(s.getStudyId(), s.getStudy());
            List<Experiment> experiments = experimentDao.getExperimentsByStudy(s.getStudyId());
            for (Experiment x : experiments) {
                IndexDocument o = new IndexDocument();
                o.setAccessLevel("consortium");
                mapDetails(x, o,s);
                o.setStudyNames(studyMap);
                o.setGeneratedDescription(x.getDescription());
                List<ExperimentRecord> experimentRecords=experimentDao.getExperimentRecords(x.getExperimentId());
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);

                objects.add(o);
                if(s.getTier()==4){
                    IndexDocument publicObject = new IndexDocument();
                    publicObject.setAccessLevel("public");
                    mapDetails(x,publicObject,s);
                    Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);

                    objects.add(publicObject);
                }

            }
        }
        return objects;
    }
public void mapDetails(Experiment x, IndexDocument o, Study s) throws Exception {

    o.setId(x.getExperimentId());
    //   o.setExperimentType(Collections.singleton(x.getType()));
    o.setCategory("Experiment");
    o.setName(x.getName());
    o.setReportPageLink("/toolkit/data/experiments/experiment/");
    o.setTier(s.getTier());
    o.setStudy(Stream.of(s.getStudy()).collect(Collectors.toSet()));
    o.setPi(Stream.of(s.getPiLastName()+" "+ s.getPiFirstName()).collect(Collectors.toSet()));
    o.setDescription(x.getDescription());
    o.setGeneratedDescription(x.getDescription());
    List<ExperimentRecord> experimentRecords=experimentDao.getExperimentRecords(x.getExperimentId());
    Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);


}
}
