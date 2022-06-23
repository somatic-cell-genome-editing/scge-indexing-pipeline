package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.indexer.dao.delivery.Crawler;
import edu.mcw.scge.process.UI;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudyIndexer implements Indexer {
    StudyDao studyDao=new StudyDao();
    GrantDao grantDao=new GrantDao();
    PersonDao personDao=new PersonDao();
    GroupDAO gdao=new GroupDAO();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        return getStudies();
    }
    public List<IndexDocument> getStudies() throws  Exception{
        List<IndexDocument> objects= new ArrayList<>();
        List<Study> studies=studyDao.getStudies();

        for(Study s: studies) {
            IndexDocument o = new IndexDocument();

            Map<Long, String> experimentMap=new HashMap<>();

          Grant grant=grantDao.getGrantByGroupId(s.getGroupId());
                o.setId(s.getStudyId());
                o.setInitiative(Collections.singleton(UI.getLabel(grant.getGrantInitiative())));
                o.setCategory("Study");
                o.setName(s.getStudy().trim());
                o.setReportPageLink("/toolkit/data/experiments/study/");
                o.setTier(s.getTier());
                o.setStudy(Stream.of(s.getStudy()).collect(Collectors.toSet()));
                o.setPi(Stream.of(s.getPiLastName()+" "+s.getPiFirstName()).collect(Collectors.toSet()));
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
                if(s.getTier()==4){
                    o.setAccess("Public");
                }else{
                    o.setAccess("Restricted");
                }

                try {
                    if (!isInDCCorNIHGroup(personDao.getPersonById(s.getModifiedBy()).get(0))) {
                        o.setStatus("Verified");
                    }
                }catch (Exception e){
                    System.err.println("STUDY ID with no Modified By:"+ s.getStudyId());
                }
                o.setSubmissionDate(s.getSubmissionDate().toString());
                objects.add(o);

        }
        return objects;
    }
    public boolean isInDCCorNIHGroup(Person p) throws Exception{


        List<Integer> DCCNIHGroupsIds=gdao.getDCCNIHGroupIds();
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
