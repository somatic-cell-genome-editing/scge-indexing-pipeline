package edu.mcw.scge.indexerRefactored.indexer;


import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.indexerRefactored.indexer.model.AccessLevel;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.Vector;
import java.util.stream.Collectors;

public abstract class ObjectDetails<T> extends ExperimentDetails implements Index<T> {
     T t;

    List<Study> studies;
     List<Experiment> experiments;
     List<Experiment> experimentsTier4;
     List<Grant> grants;

     public ObjectDetails(T t) throws Exception {
         this.t=t;
         setStudies();
//        setGrants();
         setExperiments();
         setRecords();
         if(!(t instanceof Editor))
             setEditors();
         if(!(t instanceof Model))
             setModels();
         if(!(t instanceof Vector))
             setVectors();
         if(!(t instanceof Guide))
             setGuides();
         if(!(t instanceof Delivery))
             setDeliveries();
         if(!(t instanceof HRDonor))
             setHrDonors();
         if(!(t instanceof Antibody))
             setAntibodies();

     }

    public void setExperiments() throws Exception {
        List<Experiment> experiments=new ArrayList<>();
        List<Experiment> experimentsTier4=new ArrayList<>();
        for(Study study:studies) {
            List<Experiment> exps = experimentDao.getExperimentsByStudy(study.getStudyId());
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
    public void setRecords() throws Exception {
        List<ExperimentRecord> recordsAll=new ArrayList<>();
        for(Experiment e:experiments){
            recordsAll.addAll(experimentDao.getExperimentRecords(e.getExperimentId()));
        }
        this.recordList = recordsAll;

    }

    public List<Study> getStudies(AccessLevel accessLevel) {
        switch (accessLevel){
            case PUBLIC:
                return studies.stream().filter(s->s.getTier()==4).collect(Collectors.toList());
            case CONSORTIUM:
                return studies;
        }
        return null;
    }
    public List<Experiment> getExperiments(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return experiments;
            case PUBLIC:
                return experimentsTier4;
        }
        return experiments;
    }
    public List<ExperimentRecord> getRecordList(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return recordList;
            case PUBLIC: {
                List<ExperimentRecord> records=new ArrayList<>();
                for (Experiment e : experimentsTier4) {
                    for (ExperimentRecord r : recordList) {
                        if (e.getExperimentId() == r.getExperimentId()) {
                            records.add(r);
                        }
                    }
                }
                return records;
            }
        }
        return null;
    }
    public Set<String> getPiMultipleStudies(AccessLevel accessLevel){
        Set<String> pis=new HashSet<>();
        List<Study> studies=getStudies(accessLevel);
        if(studies.size()>0) {
            for(Study s:studies) {
                if (s != null) {
                    for (Person pi : s.getMultiplePis()) {
                        if (pi.getFirstName() != null && !pi.getFirstName().equals(""))
                            pis.add(pi.getLastName() + " " + pi.getFirstName());
                        else pis.add(pi.getName());
                    }
                }
            }
        }
        return pis;
    }
    public Set<String> getProjectMembersMultipleStudies(AccessLevel accessLevel) throws Exception {
        List<Study> studies=getStudies(accessLevel);
        Set<String> projectMembers=new HashSet<>();
        if(studies!=null) {
            for(Study study:studies) {
                projectMembers.addAll(groupdao.getGroupMembersByGroupId(study.getGroupId()).stream().map(Person::getName).collect(Collectors.toSet()));
            }
        }
        return projectMembers;
    }
    public void mapStudies(IndexDocument o, AccessLevel accessLevel) throws Exception {
        List<Study> studyList=getStudies(accessLevel);
        if(studyList.size()>0) {
            o.setStudy(studyList.stream().map(Study::getStudy).collect(Collectors.toSet()));
            o.setStudyIds(studyList.stream().map(Study::getStudyId).collect(Collectors.toSet()));
            o.setPi(getPiMultipleStudies(accessLevel));
            o.setProjectMembers(getProjectMembersMultipleStudies(accessLevel));
        }

    }
    public void mapExperiments(IndexDocument o, AccessLevel accessLevel) throws Exception {
        List<Experiment> experimentList=getExperiments(accessLevel);
        o.setExperimentType(experimentList.stream().map(Experiment::getType).collect(Collectors.toSet()));
//            o.setLastModifiedDate(getLastModifiedDate(accessLevel));
        o.setSex(getSex(accessLevel));
        o.setGenotype(getGenotype(accessLevel));

    }
    public void  mapAntiBodiesMultipleStudies(IndexDocument o,AccessLevel accessLevel) throws Exception {
        List<Antibody> antibodyList=new ArrayList<>();
        Set<Long> experimentIds=getRecordList(accessLevel).stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
        for(long experimentId:experimentIds) {
            antibodyList .addAll(antibodyDao.getDistinctAntibodyByExperimentId(experimentId));
        }
        o.setAntibody(antibodyList.stream().map(Antibody::getRrid).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        o.setExternalId(antibodyList.stream().map(Antibody::getOtherId).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
    }

    public void mapOtherExperimentalDetails(IndexDocument o, AccessLevel accessLevel) throws Exception {
        mapStudies(o, accessLevel);
//        mapGrant(o, accessLevel);
        mapExperiments(o, accessLevel);
        if(!t.equals(Delivery.class) && deliveries!=null)
            mapDelivery(o, accessLevel);
        if(!t.equals(Antibody.class) && antibodies!=null)
            mapAntiBodiesMultipleStudies(o, accessLevel);
        if(!t.equals(Guide.class) && guides!=null)
            mapGuides(o, accessLevel);
        if(!t.equals(Vector.class) && vectors!=null)
            mapVectors(o, accessLevel);
        if(!t.equals(Model.class) && models!=null)
            mapModels(o, accessLevel);
        if(!t.equals(Editor.class) && editors!=null)
            mapEditors(o,accessLevel);

        mapTissues(o, accessLevel);
    }
    @Override
    public T getObject(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return t;
            case PUBLIC:
                if(getTier()==4)
                    return t;
        }
        return null;
    }
    @Override
    public IndexDocument getIndexObject(AccessLevel accessLevel) throws Exception {
        IndexDocument o=new IndexDocument();
        o.setAccessLevel(accessLevel.toString().toLowerCase());
        mapObject( o, accessLevel);
        if(o.getId()!=0) {
            mapOtherExperimentalDetails(o,accessLevel);
            return o;
        }
        return null;
    }
}
