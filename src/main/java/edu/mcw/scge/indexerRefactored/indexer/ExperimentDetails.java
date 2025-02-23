package edu.mcw.scge.indexerRefactored.indexer;

import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.datamodel.Vector;
import edu.mcw.scge.datamodel.ontologyx.Term;

import edu.mcw.scge.indexerRefactored.indexer.model.AccessLevel;
import edu.mcw.scge.process.UI;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExperimentDetails extends DAO {
    private  Experiment experiment;
    private Study study;
    private Grant grant;
     List<Editor> editors;
     List<Model> models;
     List<Vector> vectors;
     List<Guide> guides;
     List<Delivery> deliveries;
     List<HRDonor> hrDonors;
     List<Antibody> antibodies;
     List<ExperimentRecord> recordList;

    public ExperimentDetails(){}
    public ExperimentDetails(Experiment experiment) throws Exception {
        this.experiment=experiment;
        setRecordList();
        setStudy();
        setGrant();
       // setRecordList();
        setEditors();
        setModels();
        setVectors();
        setGuides();
        setDeliveries();
        setHrDonors();
        setAntibodies();

    }

    public Study getStudy(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return study;
            case PUBLIC:
                if(study.getTier()==4){
                    return study;
                }

        }
      return null;
    }

    public void setStudy() throws Exception {
        if(experiment.getStudyId()!=0)
       this.study=studyDao.getStudyByStudyId(experiment.getStudyId());
    }

    public Grant getGrant(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return grant;
            case PUBLIC:
                if(study.getTier()==4){
                    return  grant;
                }
        }
        return null;
    }

    public void setGrant() throws Exception {
       if(study!=null){
           this.grant=grantDao.getGrantByGroupId(study.getGroupId());
       }
    }

    public List<ExperimentRecord> getRecordList(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return recordList;
            case PUBLIC:
                if(study.getTier()==4)
                    return recordList;
        }
       return null;
    }


    public void setRecordList() throws Exception {
       if(study!=null){
           this.recordList=experimentDao.getExperimentRecords(experiment.getExperimentId());
       }
    }

    public Set<String> getPi(AccessLevel accessLevel){
        Set<String> pis=new HashSet<>();
        Study s=getStudy(accessLevel);
        if(s!=null) {
            for (Person pi : s.getMultiplePis()) {
                if (pi.getFirstName() != null && !pi.getFirstName().equals(""))
                    pis.add(pi.getLastName() + " " + pi.getFirstName());
                else pis.add(pi.getName());
            }
        }
        return pis;
    }
    public String getLastModifiedDate(AccessLevel accessLevel){
        switch (accessLevel){
            case CONSORTIUM:
                if(experiment.getLastModifiedDate()!=null)
                    return (experiment.getLastModifiedDate().toString());
                else{
                    return  (study.getSubmissionDate().toString());
                }
            case PUBLIC:
                if(study.getTier()==4){
                    if(experiment.getLastModifiedDate()!=null)
                        return (experiment.getLastModifiedDate().toString());
                    else{
                        return  (study.getSubmissionDate().toString());
                    }
                }
            default:
        }
      return null;
    }
//    public Map<Long, Study> getExperimentStudyMap(){
//        Map<Long, Study> experimentStudyMap=new HashMap<>();
//        experimentStudyMap.put(experiment.getExperimentId(), study);
//        return  experimentStudyMap;
//    }
    public Set<String> getSex(AccessLevel accessLevel){
        return getRecordList(accessLevel).stream().filter(r->r.getSex()!=null && !r.getSex().equals("")).map(ExperimentRecord::getSex).map(StringUtils::capitalize).collect(Collectors.toSet());
    }
    public Set<String> getGenotype(AccessLevel accessLevel){
        return getRecordList(accessLevel).stream().filter(r->r.getGenotype()!=null && !r.getGenotype().equals("")).map(ExperimentRecord::getGenotype).map(StringUtils::capitalize).collect(Collectors.toSet());

    }
    public Set<String> getProjectMembers(AccessLevel accessLevel) throws Exception {
        if(getStudy(accessLevel)!=null)
       return groupdao.getGroupMembersByGroupId(getStudy(accessLevel).getGroupId()).stream().map(Person::getName).collect(Collectors.toSet());
        return null;
    }

    public List<Editor> getEditors(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return editors;
            case PUBLIC:
              return   editors.stream().filter(e->e.getTier()==4).collect(Collectors.toList());
            default:
        }
        return null;
    }

    public void setEditors() throws Exception {
        List<Editor> editorList=new ArrayList<>();
        Set<Long> editorIds=recordList.stream().map(ExperimentRecord::getEditorId).collect(Collectors.toSet());
        for(long id:editorIds){
           List<Editor> editors= editorDao.getEditorById(id);
           editorList.addAll(editors);
        }
        this.editors = editorList;
    }

    public List<Model> getModels(AccessLevel accessLevel) {
        if(models!=null)
        switch (accessLevel){
            case CONSORTIUM:
                return models;
            case PUBLIC:
                return models.stream().filter(Objects::nonNull).filter(m->m.getTier()==4).collect(Collectors.toList());

        }
        return null;
    }

    public void setModels() throws Exception {
        List<Model> modelList=new ArrayList<>();
        Set<Long> modelIds=recordList.stream().map(ExperimentRecord::getModelId).filter(modelId -> modelId !=0L).collect(Collectors.toSet());
        for(long id:modelIds){
            Model model= modelDao.getModelById(id);
            modelList.add(model);
        }

        this.models = modelList;
    }

    public List<Vector> getVectors(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return vectors;
            case PUBLIC:
                return vectors.stream().filter(v->v.getTier()==4).collect(Collectors.toList());
        }
        return null;
    }

    public void setVectors() throws Exception {
        this.vectors = recordList.stream().filter(r->r.getVectors()!=null).map(r-> new ArrayList<>(r.getVectors())).flatMap(List::stream).collect(Collectors.toList());
    }

    public List<Guide> getGuides(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return guides;
            case PUBLIC:
                return guides.stream().filter(g->g.getTier()==4).collect(Collectors.toList());
        }
        return null;
    }

    public void setGuides() {
        this.guides = recordList.stream().filter(r->r.getGuides()!=null).map(r-> new ArrayList<>(r.getGuides())).flatMap(List::stream).collect(Collectors.toList());
    }

    public List<Delivery> getDeliveries(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return  deliveries;
            case PUBLIC:
                return deliveries.stream().filter(d->d.getTier()==4).collect(Collectors.toList());
        }
        return null;
    }

    public void setDeliveries() throws Exception {
        List<Delivery> list=new ArrayList<>();
        Set<Long> ids=recordList.stream().map(ExperimentRecord::getDeliverySystemId).collect(Collectors.toSet());
        for(long id:ids){
            List<Delivery> ds= deliveryDao.getDeliverySystemsById(id);
            list.addAll(ds);
        }
        this.deliveries = list;
    }

    public List<HRDonor> getHrDonors(AccessLevel accessLevel) {
        return getRecordList(accessLevel).stream().filter(r->r.getHrDonors()!=null).map(r->r.getHrDonors()).flatMap(List::stream).collect(Collectors.toList());
    }

    public void setHrDonors() throws Exception {
        List<HRDonor>  list=new ArrayList<>();
       list=recordList.stream().filter(r->r.getHrDonors()!=null).map(ExperimentRecord::getHrDonors).flatMap(List::stream).collect(Collectors.toList());

        this.hrDonors = list;
    }

    public List<Antibody> getAntibodies(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return antibodies;
            case PUBLIC:
                if(study.getTier()==4)
                return antibodies;
        }
        return null;
    }

    public void setAntibodies() throws Exception {
        List<Antibody> list=new ArrayList<>();
        Set<Long> experimentIds=recordList.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
        for(long experimentId:experimentIds) {
            list .addAll(antibodyDao.getDistinctAntibodyByExperimentId(experimentId));
        }
        this.antibodies = list;
    }
    public Set<String> getTissueIds(AccessLevel accessLevel) {
        return getRecordList(accessLevel).stream().filter(r->r.getTissueId()!=null && !r.getTissueId().equals("")).map(ExperimentRecord::getTissueId).filter(Objects::nonNull).collect(Collectors.toSet());

    }
    public Set<String> getTissueTerms(AccessLevel accessLevel) {

                return getTissueIds(accessLevel).stream().filter(Objects::nonNull).filter(t->!t.equals("")).map(id-> {
                    try {
                        return xdao.getTerm(id).getTerm();
                    } catch (Exception e) {
                        System.out.println(id);
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull).filter(obj -> true).map(StringUtils::capitalize).collect(Collectors.toSet());


    }
    public Set<String> getCellTypeIds(AccessLevel accessLevel) {
        return recordList.stream().map(ExperimentRecord::getCellType).filter(Objects::nonNull).filter(t->!t.equals("")).collect(Collectors.toSet());

    }
    public Set<String> getCellTypeTerms(AccessLevel accessLevel) {
        return getCellTypeIds(accessLevel).stream().filter(Objects::nonNull).filter(t->!t.equals("")).map(id-> {
            try {
                return xdao.getTerm(id).getTerm();
            } catch (Exception e) {
                System.out.println(id);
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).filter(obj -> true).map(StringUtils::capitalize).collect(Collectors.toSet());


    }
    public Set<String> getSynonyms(AccessLevel accessLevel) throws Exception {
        Set<String> synonyms=new HashSet<>();
        Set<String> termIds=new HashSet<>();
        termIds.addAll(getTissueIds(accessLevel));
        termIds.addAll(getCellTypeIds(accessLevel));
        for(String id:termIds) {
            for (Term term : xdao.getAllActiveTermAncestors(id)) {
                synonyms.add(StringUtils.capitalize(term.getTerm().trim()));
                synonyms.add(term.getAccId());
            }
        }
        return synonyms;
    }


    public void mapStudy(IndexDocument o, AccessLevel accessLevel) throws Exception {
        Study s=getStudy(accessLevel);
        if(s!=null) {
            o.setTier(s.getTier());
            o.setStudy(Stream.of(s.getStudy()).collect(Collectors.toSet()));
            o.setStudyId(s.getStudyId());
            o.setPi(getPi(accessLevel));
            o.setProjectMembers(getProjectMembers(accessLevel));
        }
    }
    public void mapExperiment(IndexDocument o, AccessLevel accessLevel) throws Exception {
        if(getStudy(accessLevel)!=null) {
            o.setId(experiment.getExperimentId());
            o.setExperimentType(Collections.singleton(experiment.getType()));
            o.setLastModifiedDate(getLastModifiedDate(accessLevel));
            o.setCategory("Experiment");
            o.setName(experiment.getName());
            o.setReportPageLink("/toolkit/data/experiments/experiment/");

            o.setDescription(experiment.getDescription());

            o.setSex(getSex(accessLevel));
            o.setGenotype(getGenotype(accessLevel));
        }
    }

    public void mapTissues(IndexDocument indexDocument, AccessLevel accessLevel) throws Exception {
        indexDocument.setTissueTerm(getTissueTerms(accessLevel));
        indexDocument.setTissueIds(getTissueIds(accessLevel));
        indexDocument.setCellTypeTerm(getCellTypeTerms(accessLevel));
        indexDocument.setCellType(getCellTypeIds(accessLevel));
        indexDocument.setTermSynonyms(getSynonyms(accessLevel));
    }
    public void mapModels(IndexDocument indexDocument, AccessLevel accessLevel){
        List<Model> models=getModels(accessLevel);
        if(models!=null && models.size()>0) {
            indexDocument.setModelType(models.stream().map(Model::getType).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setModelSubtype(models.stream().map(Model::getSubtype).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
           Set<String> modelName=new HashSet<>();
            for(Model model:models) {
                if (model.getDisplayName() != null && !model.getDisplayName().equals(""))
                    modelName.add(model.getDisplayName());
                else
                    modelName.add(model.getName());
            }
            indexDocument.setModelName(modelName);
            indexDocument.setModelOrganism(models.stream().map(Model::getOrganism).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setModelRrid(models.stream().map(Model::getRrid).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setModelSource(models.stream().map(Model::getSource).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setModelAnnotatedMap(models.stream().map(Model::getAnnotatedMap).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setTransgene(models.stream().map(Model::getTransgene).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setTransgeneReporter(models.stream().map(Model::getTransgeneReporter).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setParentalOrigin(models.stream().map(Model::getParentalOrigin).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setStrainAlias(models.stream().map(Model::getStrainAlias).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        }
    }
    public void mapVectors(IndexDocument indexDocument, AccessLevel accessLevel){

        List<Vector> vectorList=getVectors(accessLevel);
        if(vectorList!=null && vectorList.size()>0){
        indexDocument.setVectorName(vectorList.stream().map(Vector::getName).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        indexDocument.setVectorType(vectorList.stream().map(Vector::getType).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        indexDocument.setVectorSubtype(vectorList.stream().map(Vector::getSubtype).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        indexDocument.setVectorSource(vectorList.stream().map(Vector::getSource).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        indexDocument.setVectorlabId(vectorList.stream().map(Vector::getLabId).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        indexDocument.setVectorAnnotatedMap(vectorList.stream().map(Vector::getAnnotatedMap).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        indexDocument.setGenomeSerotype(vectorList.stream().map(Vector::getGenomeSerotype).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        indexDocument.setCapsidSerotype(vectorList.stream().map(Vector::getCapsidSerotype).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        indexDocument.setCapsidVariant(vectorList.stream().map(Vector::getCapsidVariant).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        indexDocument.setTiterMethod(vectorList.stream().map(Vector::getTiterMethod).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
    }
    }
    public void mapGuides(IndexDocument indexDocument, AccessLevel accessLevel){
        List<Guide> guideList=getGuides(accessLevel);
        if(guideList!=null && guideList.size()>0) {
            indexDocument.setGuideCompatibility(guideList.stream().map(Guide::getGuideCompatibility).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideTargetLocus(guideList.stream().map(Guide::getTargetLocus).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideSpecies(guideList.stream().map(Guide::getSpecies).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideTargetSequence(guideList.stream().map(Guide::getTargetSequence).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuidePam(guideList.stream().map(Guide::getPam).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGrnaLabId(guideList.stream().map(Guide::getGrnaLabId).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideSpacerSequence(guideList.stream().map(Guide::getSpacerSequence).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideRepeatSequence(guideList.stream().map(Guide::getRepeatSequence).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuide(guideList.stream().map(Guide::getGuide).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideForwardPrimer(guideList.stream().map(Guide::getForwardPrimer).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideReversePrimer(guideList.stream().map(Guide::getReversePrimer).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideLinkerSequence(guideList.stream().map(Guide::getLinkerSequence).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideAntiRepeatSequence(guideList.stream().map(Guide::getAntiRepeatSequence).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setModifications(guideList.stream().map(Guide::getModifications).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideLocation(guideList.stream().filter(g -> g.getChr() != null && !g.getChr().equals("")).map(g -> g.getChr() + ":" + g.getStart() + " - " + g.getStop()).collect(Collectors.toSet()));
            indexDocument.setGuideAnnotatedMap(guideList.stream().map(Guide::getAnnotatedMap).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideSource(guideList.stream().map(Guide::getSource).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        }

    }
    public void mapAntiBodies(IndexDocument indexDocument, AccessLevel accessLevel){
        List<Antibody> antibodyList=getAntibodies(accessLevel);
        if(antibodyList!=null) {
            indexDocument.setAntibody(antibodyList.stream().map(Antibody::getRrid).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setExternalId(antibodyList.stream().map(Antibody::getOtherId).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        }
    }
    public void mapDelivery(IndexDocument indexDocument, AccessLevel accessLevel){
        List<Delivery> deliveriesList=getDeliveries(accessLevel);
        if(deliveriesList!=null && deliveriesList.size()>0) {
            indexDocument.setDeliverySystemName(deliveriesList.stream().map(Delivery::getName).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setDeliveryType(deliveriesList.stream().map(Delivery::getType).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setDeliverySubtype(deliveriesList.stream().map(Delivery::getSubtype).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setMolTargetingAgent(deliveriesList.stream().map(Delivery::getMolTargetingAgent).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setDeliverySource(deliveriesList.stream().map(Delivery::getSource).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setDeliveryLabId(deliveriesList.stream().map(Delivery::getLabId).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setDeliveryAnnotatedMap(deliveriesList.stream().map(Delivery::getAnnotatedMap).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
        }

    }
    public void mapEditors(IndexDocument indexDocument, AccessLevel accessLevel){
        List<Editor> editors=getEditors(accessLevel);
        if(editors!=null && editors.size()>0) {
            indexDocument.setEditorAlias(editors.stream().map(Editor::getAlias).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setEditorSpecies(editors.stream().map(Editor::getSpecies).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setEditorSymbol(editors.stream().map(Editor::getSymbol).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setEditorType(editors.stream().map(Editor::getType).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setEditorSubType(editors.stream().map(Editor::getSubtype).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setEditorVariant(editors.stream().map(Editor::getEditorVariant).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setActivity(editors.stream().map(Editor::getActivity).map(StringUtils::capitalize).filter(Objects::nonNull).collect(Collectors.toSet()));
            indexDocument.setEditorAnnotatedMap(editors.stream().map(Editor::getAnnotatedMap).filter(Objects::nonNull).collect(Collectors.toSet()));
            indexDocument.setEditorLocation(editors.stream().map(Editor::getAlias).filter(Objects::nonNull).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setFusion(editors.stream().map(Editor::getFusion).filter(Objects::nonNull).collect(Collectors.toSet()));
            indexDocument.setSubstrateTarget(editors.stream().map(Editor::getSubstrateTarget).filter(Objects::nonNull).collect(Collectors.toSet()));
            indexDocument.setEditorTargetSequence(editors.stream().map(Editor::getTarget_sequence).filter(Objects::nonNull).collect(Collectors.toSet()));
            indexDocument.setEditorTargetLocus(editors.stream().map(Editor::getTargetLocus).filter(Objects::nonNull).collect(Collectors.toSet()));
            indexDocument.setDsbCleavageType(editors.stream().map(Editor::getDsbCleavageType).filter(Objects::nonNull).collect(Collectors.toSet()));
            indexDocument.setEditorPamPreference(editors.stream().map(Editor::getPamPreference).filter(Objects::nonNull).collect(Collectors.toSet()));
            indexDocument.setProteinSequence(editors.stream().map(Editor::getProteinSequence).filter(Objects::nonNull).collect(Collectors.toSet()));
            indexDocument.setEditorSource(editors.stream().map(Editor::getSource).filter(Objects::nonNull).collect(Collectors.toSet()));
            indexDocument.setEditorLocation(editors.stream().filter(e->e.getChr()!=null && !e.getChr().equals(""))
                    .map(e->"CHR"+e.getChr()+":"+e.getStart()+" - "+e.getStop()).collect(Collectors.toSet()));
        }
    }



    public void mapGrant(IndexDocument o, AccessLevel accessLevel) throws Exception {
        if(getStudy(accessLevel)!=null) {
            try {
                o.setInitiative(getInitiatives());
            } catch (Exception e) {
                e.printStackTrace();
            }
            o.setPi(getPi(accessLevel));
            o.setCurrentGrantNumber(grant.getCurrentGrantNumber());
            if (grant.getFormerGrantNumbers().size() > 0)
                o.setFormerGrantNumbers(new HashSet<>(grant.getFormerGrantNumbers()));
            if (grant.getNihReporterLink() != null && !grant.getNihReporterLink().equals(""))
                o.setNihReporterLink(grant.getNihReporterLink());
        }
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

    @Override
    public IndexDocument getIndexObject(AccessLevel accessLevel) throws Exception {
        IndexDocument o=new IndexDocument();
        o.setAccessLevel(accessLevel.toString().toLowerCase());
        mapExperiment(o,accessLevel);
        if(o.getId()!=0) {
            mapStudy(o, accessLevel);

            mapGrant(o, accessLevel);

            mapEditors(o, accessLevel);
            mapDelivery(o, accessLevel);
            mapAntiBodies(o, accessLevel);
            mapGuides(o, accessLevel);
            mapVectors(o, accessLevel);
            mapModels(o, accessLevel);
            mapTissues(o, accessLevel);
            return o;
        }
        return  null;
    }
}
