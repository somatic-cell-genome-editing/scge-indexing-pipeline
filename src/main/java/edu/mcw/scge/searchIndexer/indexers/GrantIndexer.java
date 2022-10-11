package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.process.UI;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrantIndexer implements Indexer{
    StudyDao studyDao=new StudyDao();
    GrantDao grantDao=new GrantDao();
    GroupDAO gdao=new GroupDAO();
    ExperimentDao experimentDao=new ExperimentDao();

    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        return getGrants();
    }
    public List<IndexDocument> getGrants() throws  Exception{
        List<IndexDocument> objects= new ArrayList<>();
        List<Integer> submittedGrantIds=studyDao.getAllSubmittedGrantIds();
        System.out.println("SUBMITTED GRANT IDS:" + submittedGrantIds.size());
        for(int id:submittedGrantIds) {
            IndexDocument o = new IndexDocument();
            Grant grant = grantDao.getGrantByGroupId(id);
            o.setAccessLevel("Consortium");

            mapDetails(o,grant);
            objects.add(o);
        }

        return objects;
    }
    public void mapDetails(IndexDocument o,Grant grant) throws Exception {
        Map<Long, String> experimentMap=new HashMap<>();

        o.setId(grant.getGroupId());
        o.setInitiative(Collections.singleton(UI.getLabel(grant.getGrantInitiative())));
        o.setCategory("Grant");
        o.setName(grant.getGrantTitle());
        o.setReportPageLink("/toolkit/data/experiments/group/");
        List<Person> piList=grantDao.getGrantPi(grant.getGroupId());
        Set<String> pis=new HashSet<>();
        for(Person pi:piList){
            pis.add(pi.getLastName()+" "+ pi.getFirstName());
        }
        o.setPi(pis);
       /* //   o.setStudyNames(studyMap);
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
        o.setSubmissionDate(s.getSubmissionDate().toString());*/
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
