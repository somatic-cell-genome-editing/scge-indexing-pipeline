package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.process.UI;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;

public class GrantIndexer implements Indexer{
    StudyDao studyDao=new StudyDao();
    GrantDao grantDao=new GrantDao();
    GroupDAO groupdao=new GroupDAO();
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
            o.setAccessLevel("consortium");
            mapDetails(o,grant);
            objects.add(o);
            if(o.getTier()==4){
                IndexDocument publicObject = new IndexDocument();
                publicObject.setAccessLevel("public");
                mapDetails(publicObject,grant);
                objects.add(publicObject);

            }
        }

        return objects;
    }
    public void mapDetails(IndexDocument o,Grant grant) throws Exception {

        o.setId(grant.getGroupId());
        o.setInitiative(Collections.singleton(UI.getLabel(grant.getGrantInitiative())));
        o.setCategory("Project");
        o.setName(grant.getGrantTitle());
        o.setReportPageLink("/toolkit/data/experiments/group/");
        List<Person> piList=grantDao.getGrantPi(grant.getGroupId());
        Set<String> pis=new HashSet<>();
        for(Person pi:piList){
            pis.add(pi.getLastName()+" "+ pi.getFirstName());
        }
        o.setPi(pis);
       List<Study> studies= studyDao.getStudiesByGroupId(grant.getGroupId());
       Map<Long, String> experimentNames=new HashMap<>();
        List<Date> lastModifiedDate=new ArrayList<>();

        Set<String> members=new HashSet<>();
        boolean flag=false;
       for(Study study:studies){
           if(study.getTier()==4){
               flag=true;

           }
           List<Experiment> experiments=experimentDao.getExperimentsByStudy(study.getStudyId());
           if(o.getAccessLevel().equalsIgnoreCase("consortium")) {
               for (Experiment x : experiments) {
                   experimentNames.put(x.getExperimentId(), x.getName());
               }
           }else{
               if(study.getTier()==4) {
                   for (Experiment x : experiments) {
                       experimentNames.put(x.getExperimentId(), x.getName());
                   }
               }
           }
           for(Experiment x:experiments){
               if(x.getLastModifiedDate()!=null)
                   lastModifiedDate.add(x.getLastModifiedDate());
           }
           if(lastModifiedDate.size()==0){
               lastModifiedDate.add(study.getSubmissionDate());
           }
        List<Person> projectMembers=   groupdao.getGroupMembersByGroupId(study.getGroupId());
        if(projectMembers.size()>0){
            members.addAll(projectMembers.stream().map(p->p.getName()).collect(Collectors.toSet()));
        }
       }
       if(lastModifiedDate.size()>0)
           o.setLastModifiedDate(Collections.max(lastModifiedDate).toString());
       o.setProjectMembers(members);
       o.setExperimentNames(experimentNames);
       if(flag){
           o.setTier(4);
       }else{
           o.setTier(0);
       }
       o.setDescription(grant.getDescription());
       o.setCurrentGrantNumber(grant.getCurrentGrantNumber());
       if(grant.getFormerGrantNumbers().size()>0)
       o.setFormerGrantNumbers(new HashSet<>(grant.getFormerGrantNumbers()));


    }
    public boolean isInDCCorNIHGroup(Person p) throws Exception{


        List<Integer> DCCNIHGroupsIds=groupdao.getDCCNIHGroupIds();
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
