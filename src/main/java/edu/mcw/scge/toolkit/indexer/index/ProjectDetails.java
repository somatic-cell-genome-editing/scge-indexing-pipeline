package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;
import edu.mcw.scge.process.UI;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectDetails extends DAO {

   private final Grant grant;
   private List<Study> studies;
   private List<Experiment> experimentsTier4;
   private List<Experiment> experiments;

    public ProjectDetails(Grant grant) throws Exception {
        this.grant=grant;
        setStudies();
        setExperiments();
    }

    public List<Study> getStudies(AccessLevel accessLevel) throws Exception {
        switch (accessLevel){
            case CONSORTIUM:
                return studies;
            case PUBLIC:
                return studies.stream().filter(s->getTier()==4).collect(Collectors.toList());
            default:
        }
       return null;

    }
    public void setStudies() throws Exception {
        this.studies= studyDao.getStudiesByGroupId(grant.getGroupId());

    }
    public void setExperiments() throws Exception {
            List<Experiment> experiments=new ArrayList<>();
            List<Experiment> experimentsTier4=new ArrayList<>();
            for(Study study:studies) {
                List<edu.mcw.scge.datamodel.Experiment> exps = experimentDao.getExperimentsByStudy(study.getStudyId());
                if(exps!=null){
                    experiments.addAll(exps);
                    if(study.getTier()==4){
                        experimentsTier4.addAll(experiments);
                    }
                }

            }
            this.experimentsTier4=experimentsTier4;
            this.experiments = experiments;
    }
    public List<Experiment> getExperiments(AccessLevel accessLevel){
        switch (accessLevel){
            case PUBLIC:
                return experimentsTier4;
            case CONSORTIUM:
                return experiments;
            default:
        }
        return null;
    }
    public Set<String> getExperimentTypes(AccessLevel accessLevel){
        switch (accessLevel){
            case CONSORTIUM:
                return experiments.stream().map(Experiment::getType).collect(Collectors.toSet());
            case PUBLIC:
                return experimentsTier4.stream().map(Experiment::getType).collect(Collectors.toSet());
        }
       return null;
    }
    public Set<String> getExperimentNames(AccessLevel accessLevel){
        switch (accessLevel){
            case PUBLIC:
                return experimentsTier4.stream().map(Experiment::getName).collect(Collectors.toSet());
            case CONSORTIUM:
                return experiments.stream().map(Experiment::getName).collect(Collectors.toSet());
            default:
        }
        return null;
    }
    public TreeMap<Long, String> getExperimentNameIdMap(AccessLevel accessLevel){
        TreeMap<Long, String> expNameIdMap=new TreeMap<>();
        List<Experiment> experimentList=new ArrayList<>();
        switch (accessLevel){
            case CONSORTIUM:
                experimentList.addAll(experiments);
                break;
            case PUBLIC:
              experimentList.addAll(experimentsTier4);
              break;
            default:
        }
        for(Experiment experiment:experimentList){
            expNameIdMap.put(experiment.getExperimentId(), experiment.getName());
        }
        return expNameIdMap;

    }
    public Set<String> getPi() throws Exception {
        List<Person> piList=grantDao.getGrantPi(grant.getGroupId());
        Set<String> pis=new HashSet<>();
        for(Person pi:piList){
            if(pi.getFirstName()!=null && !pi.getFirstName().equals(""))
                pis.add(pi.getLastName()+" "+ pi.getFirstName());
            else pis.add(pi.getName());
        }
       return pis;
    }
    public Set<String> getInitiatives() throws Exception {
        Set<String> initiatives=new HashSet<>();
        initiatives.addAll(Collections.singleton(UI.correctInitiative(grant.getGrantInitiative())));
        if(grant.getGrantInitiative().equalsIgnoreCase("Collaborative Opportunity Fund")) {
            List<String> cofProjectInitiatives =grantDao.getCOFProjectInitiatives(grant.getGrantId());
            if(!cofProjectInitiatives.isEmpty()) {

                for(String cofInitiative:cofProjectInitiatives) {
                    initiatives.add(UI.correctInitiative(cofInitiative));
                }

            }

        }
   return initiatives;
    }
    public Set<Integer> getStudyIds(AccessLevel  accessLevel){
        switch (accessLevel){
            case CONSORTIUM:
                return studies.stream().map(Study::getStudyId).collect(Collectors.toSet());
            case PUBLIC:
                return studies.stream().filter(s->s.getTier()==4).map(Study::getStudyId).collect(Collectors.toSet());
            default:
        }
        return null;
    }
    public int getTier(){
       List<Integer> tiers=studies.stream().map(Study::getTier).collect(Collectors.toList());
       if(tiers.contains(4)) return 4;
       return 0;
    }
    public Set<String> getProjectMembers(AccessLevel accessLevel) throws Exception {
        Set<String> members=new HashSet<>();
        for(Study study:getStudies(accessLevel)){
            List<Person> projectMembers=   groupdao.getGroupMembersByGroupId(study.getGroupId());
            if(projectMembers.size()>0){
                members.addAll(projectMembers.stream().map(Person::getName).collect(Collectors.toSet()));
            }
        }
        return members;
    }
    public String getLastModifiedDate(AccessLevel accessLevel) throws Exception {
        List<Date> lastModifiedDate=new ArrayList<>();

        for(Experiment x:getExperiments(accessLevel)){
            if(x.getLastModifiedDate()!=null)
                lastModifiedDate.add(x.getLastModifiedDate());
        }
        if(lastModifiedDate.size()==0){
            for(Study study:getStudies(accessLevel))
            lastModifiedDate.add(study.getSubmissionDate());
        }

        if(lastModifiedDate.size()>0)
        return Collections.max(lastModifiedDate).toString();
        return null;
    }

    public void mapGrant(IndexDocument o, AccessLevel accessLevel) throws Exception {
        if(getTier()==4 || accessLevel.equals(AccessLevel.CONSORTIUM)) {
            o.setId(grant.getGroupId());
            try {
                o.setInitiative(getInitiatives());
            } catch (Exception e) {
                e.printStackTrace();
            }
            o.setCategory("Project");
            o.setName(grant.getGrantTitle());
            o.setReportPageLink("/toolkit/data/experiments/group/");
            o.setPi(getPi());
            o.setDescription(grant.getDescription());
            o.setCurrentGrantNumber(grant.getCurrentGrantNumber());
            if (grant.getFormerGrantNumbers().size() > 0)
                o.setFormerGrantNumbers(new HashSet<>(grant.getFormerGrantNumbers()));
            if (grant.getNihReporterLink() != null && !grant.getNihReporterLink().equals(""))
                o.setNihReporterLink(grant.getNihReporterLink());
        }
    }
    public void mapStudies(IndexDocument o, AccessLevel accessLevel) throws Exception {

        try {
            o.setStudyIds(getStudyIds(accessLevel));
        }catch (Exception e){e.printStackTrace();}
        try{
            o.setTier(getTier());
        }catch (Exception e){e.printStackTrace();}
        try{
            o.setLastModifiedDate(getLastModifiedDate(accessLevel));
        }catch (Exception e){e.printStackTrace();}
        try{
            o.setProjectMembers(getProjectMembers(accessLevel));
        }catch (Exception e){e.printStackTrace();}
    }

//    @Override
    public IndexDocument getIndexObject( AccessLevel accessLevel) throws Exception {
        IndexDocument o=new IndexDocument();
        o.setAccessLevel(accessLevel.toString().toLowerCase());
        mapGrant(o,accessLevel);
        mapStudies(o,accessLevel);
        try{
            o.setExperimentNames(getExperimentNameIdMap(accessLevel));
        }catch (Exception e){e.printStackTrace();}

        if(o.getId()!=0)
        return o;
        return null;
    }
}
