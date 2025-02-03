package edu.mcw.scge.searchIndexer.processThreads;


import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.StudyDao;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Person;
import edu.mcw.scge.datamodel.Study;

import edu.mcw.scge.indexer.Manager;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExperimentIndexerThread extends Indexer implements Runnable{
    ExperimentDao experimentDao=new ExperimentDao();
    StudyDao studyDao=new StudyDao();

    public ExperimentIndexerThread(){}
    @Override
    public void run() {
        Logger log= LogManager.getLogger(Manager.class);

        log.info(Thread.currentThread().getName() + ": " + "Experiment" + " started " + new Date());
        try {
            getExperiments();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info(Thread.currentThread().getName() + ": " + "Experiment" + " END " + new Date());
    }
    public void getExperiments() throws  Exception{
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
                index(o);

                if(s.getTier()==4){
                    IndexDocument publicObject = new IndexDocument();
                    publicObject.setAccessLevel("public");
                    mapDetails(x,publicObject,s);
                    index(publicObject);
                }

            }
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
        o.setDescription(x.getDescription());
        //  o.setGeneratedDescription(x.getDescription());
        List<ExperimentRecord> experimentRecords=experimentDao.getExperimentRecords(x.getExperimentId());
        Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);


    }


}
