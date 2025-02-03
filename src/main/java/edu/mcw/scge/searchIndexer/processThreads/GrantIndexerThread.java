package edu.mcw.scge.searchIndexer.processThreads;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.GrantDao;
import edu.mcw.scge.dao.implementation.GroupDAO;
import edu.mcw.scge.dao.implementation.StudyDao;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.Grant;
import edu.mcw.scge.datamodel.Person;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.indexer.Manager;
import edu.mcw.scge.process.UI;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class GrantIndexerThread extends Indexer implements Runnable{
    StudyDao studyDao=new StudyDao();
    GrantDao grantDao=new GrantDao();
    GroupDAO groupDAO=new GroupDAO();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public void run() {
        Logger log= LogManager.getLogger(Manager.class);

        log.info(Thread.currentThread().getName() + ": " + "GRANT" + " started " + new Date());
        List<Integer> submittedGrantIds= null;
        try {
            submittedGrantIds = studyDao.getAllSubmittedGrantIds();

        for(int id:submittedGrantIds) {
            IndexDocument o = new IndexDocument();
            Grant grant = grantDao.getGrantByGroupId(id);
            o.setAccessLevel("consortium");
            mapDetails(o,grant);
            index(o);
            if(o.getTier()==4){
                IndexDocument publicObject = new IndexDocument();
                publicObject.setAccessLevel("public");
                mapDetails(publicObject,grant);
                index(publicObject);

            }
        }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info(Thread.currentThread().getName() + ": " + "GRANT" + " END " + new Date());
    }
    public void mapDetails(IndexDocument o,Grant grant) throws Exception {

        o.setId(grant.getGroupId());
        o.setInitiative(Collections.singleton(UI.correctInitiative(grant.getGrantInitiative())));
        if(grant.getGrantInitiative().equalsIgnoreCase("Collaborative Opportunity Fund")) {
            List<String> cofProjectInitiatives =grantDao.getCOFProjectInitiatives(grant.getGrantId());
            if(!cofProjectInitiatives.isEmpty()) {
                Set<String> initiatives=new HashSet<>();
                if(o.getInitiative()!=null)
                    initiatives.addAll( o.getInitiative());

                for(String cofInitiative:cofProjectInitiatives) {
                    initiatives.add(UI.correctInitiative(cofInitiative));
                }

                o.setInitiative(initiatives);
            }
        }
        o.setCategory("Project");
        o.setName(grant.getGrantTitle());
        o.setReportPageLink("/toolkit/data/experiments/group/");
        List<Person> piList=grantDao.getGrantPi(grant.getGroupId());
        Set<String> pis=new HashSet<>();
        for(Person pi:piList){
            if(pi.getFirstName()!=null && !pi.getFirstName().equals(""))
                pis.add(pi.getLastName()+" "+ pi.getFirstName());
            else pis.add(pi.getName());
        }
        o.setPi(pis);
        List<Study> studies= studyDao.getStudiesByGroupId(grant.getGroupId());
        TreeMap<Long, String> experimentNames=new TreeMap<>();
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
            List<Person> projectMembers=   groupDAO.getGroupMembersByGroupId(study.getGroupId());
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
        if(grant.getNihReporterLink()!=null && !grant.getNihReporterLink().equals(""))
            o.setNihReporterLink(grant.getNihReporterLink());

    }
}
