package edu.mcw.scge.searchIndexer.processThreads;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.indexer.Manager;
import edu.mcw.scge.process.UI;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudyIndexerThread extends Indexer implements Runnable{
    StudyDao studyDao=new StudyDao();
    GroupDAO groupDAO=new GroupDAO();
    ExperimentDao experimentDao=new ExperimentDao();
    GrantDao grantDao=new GrantDao();
    PersonDao personDao=new PersonDao();
    @Override
    public void run() {
        Logger log= LogManager.getLogger(Manager.class);

        log.info(Thread.currentThread().getName() + ": " + "STUDY" + " started " + new Date());
        try {
            for(Study s: studyDao.getStudies()) {
                IndexDocument o = new IndexDocument();
                o.setAccessLevel("consortium");
                mapDetails(o,s);
                index(o);
                if(s.getTier()==4){
                    IndexDocument publicObject = new IndexDocument();
                    publicObject.setAccessLevel("public");
                    mapDetails(publicObject,s);
                    index(publicObject);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info(Thread.currentThread().getName() + ": " + "STUDY" + " END " + new Date());
    }
    public void mapDetails(IndexDocument o, Study s) throws Exception {
        Map<Long, String> experimentMap=new HashMap<>();

        Grant grant=grantDao.getGrantByGroupId(s.getGroupId());
        o.setId(s.getStudyId());
        o.setInitiative(Collections.singleton(UI.correctInitiative(grant.getGrantInitiative())));
        if(grant.getGrantInitiative().equalsIgnoreCase("Collaborative Opportunity Fund")) {
            List<String> cofProjectInitiatives =grantDao.getCOFProjectInitiatives(grant.getGrantId());
            if(!cofProjectInitiatives.isEmpty()) {
                Set<String> initiatives=o.getInitiative();

                for(String cofInitiative:cofProjectInitiatives) {
                    initiatives.add(UI.correctInitiative(cofInitiative));
                }

                o.setInitiative(initiatives);
            }
        }
        o.setCategory("Project");
        o.setName(s.getStudy().trim());
        o.setReportPageLink("/toolkit/data/experiments/study/");
        o.setTier(s.getTier());
        o.setStudy(Stream.of(s.getStudy()).collect(Collectors.toSet()));
        Set<String> pis=new HashSet<>();
        for(Person pi:s.getMultiplePis()){
            pis.add(pi.getFirstName()+" "+ pi.getLastName());
        }
        o.setPi(pis);
        //   o.setStudyNames(studyMap);
        // o.setGeneratedDescription(grant.);
        List<Experiment> experiments=experimentDao.getExperimentsByStudy(s.getStudyId());
        if(experiments.size()>0){
            o.setStatus("Processed");
        }else{
            o.setStatus("Received");
        }
        for(Experiment experiment:experiments){
            experimentMap.put(experiment.getExperimentId(), experiment.getName());
        }
        if(s.getIsValidationStudy()==1){
            o.setStudyType("Validation");
        }else{
            o.setStudyType("Experimental");
        }
        o.setExperimentNames(experimentMap);

        try {
            if (!isInDCCorNIHGroup(personDao.getPersonById(s.getModifiedBy()).get(0))) {
                o.setStatus("Verified");
            }
        }catch (Exception e){
        }
        o.setSubmissionDate(s.getSubmissionDate().toString());
    }
    public boolean isInDCCorNIHGroup(Person p) throws Exception{


        List<Integer> DCCNIHGroupsIds=groupDAO.getDCCNIHGroupIds();
        PersonDao pdao = new PersonDao();
        List<PersonInfo> personInfoRecords = pdao.getPersonInfo(p.getId());

        for(PersonInfo i:personInfoRecords) {
            if (DCCNIHGroupsIds.contains(i.getGroupId())) {
                return true;
            }
        }
        return false;
    }

}
