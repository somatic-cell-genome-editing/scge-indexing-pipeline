package edu.mcw.scge.toolkit.indexer.index;


import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.datamodel.ontologyx.Term;
import edu.mcw.scge.datamodel.publications.Publication;
import edu.mcw.scge.datamodel.publications.Reference;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;
import edu.mcw.scge.process.UI;
import edu.mcw.scge.toolkit.indexer.model.Category;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.Vector;
import java.util.stream.Collectors;

public abstract class ObjectDetails<T> extends DAO implements Index<T> {
     T t;

    List<Study> studies;
    List<Experiment> experiments;
    List<Experiment> experimentsTier4;
    List<Grant> grants;
    List<Editor> editors;
    List<Model> models;
    List<edu.mcw.scge.datamodel.Vector> vectors;
    List<Guide> guides;
    List<Delivery> deliveries;
    List<HRDonor> hrDonors;
    List<Antibody> antibodies;
    List<ExperimentRecord> recordList;
    List<Protocol> protocols;
    Set<Category> protocolObjectTypes;

    List<Publication> publications;
    Set<Category> publicationObjectTypes;

    Set<Long> allObjectsIdList;

    public ObjectDetails(T t) throws Exception {
         this.t=t;

         setStudies();
         if(t instanceof Experiment || t instanceof Grant)
             setGrants();
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
        mapAllObjectsIdList();
        if(!(t instanceof Protocol)) {
            setProtocols();
            setProtocolsAssociatedObjectType();
        }
        if(!(t instanceof Publication)) {
            setPublications();
            setPublicationAssociatedObjectTypes();
        }
     }

    public List<Protocol> getProtocols(AccessLevel accessLevel) {
        if(protocols!=null){
            switch (accessLevel){
                case CONSORTIUM:
                    return protocols;
                case PUBLIC:
                    return protocols.stream().filter(p->p.getTier()==4).collect(Collectors.toList());
            }
        }
        return null;
    }
    public void mapAllObjectsIdList(){
        Set<Long> scgeIds=new HashSet<>();
        if(editors!=null)
            scgeIds.addAll(editors.stream().map(Editor::getId).collect(Collectors.toSet()));
        if(experiments!=null)
            scgeIds.addAll(experiments.stream().map(Experiment::getExperimentId).collect(Collectors.toSet()));
        if(guides!=null)
            scgeIds.addAll(guides.stream().map(Guide::getGuide_id).collect(Collectors.toSet()));
        if(models!=null)
            scgeIds.addAll(models.stream().map(Model::getModelId).collect(Collectors.toSet()));
        if(vectors!=null)
            scgeIds.addAll(vectors.stream().map(edu.mcw.scge.datamodel.Vector::getVectorId).collect(Collectors.toSet()));
        if(deliveries!=null)
            scgeIds.addAll(deliveries.stream().map(Delivery::getId).collect(Collectors.toSet()));
        if(hrDonors!=null)
            scgeIds.addAll(hrDonors.stream().map(HRDonor::getId).collect(Collectors.toSet()));
        this.allObjectsIdList=scgeIds;

    }

    public void setProtocols() throws Exception {
        if(allObjectsIdList.size()>0)
            this.protocols = protocolDao.getProtocolsBySCGEObjectIdsList(new ArrayList<>(allObjectsIdList));
    }
    public void setProtocolsAssociatedObjectType() throws Exception {
        Set<Category> objectTypes=new HashSet<>();
        if(protocols!=null){
           for(Protocol p:protocols){
             long id=  p.getAssociatedObjectId();
             objectTypes.add(getObjectTypeOfSCGEId(id));
           }
        }
        this.protocolObjectTypes=objectTypes;
    }

    public List<Publication> getPublications(AccessLevel accessLevel) {
        switch (accessLevel){
            case CONSORTIUM:
                return publications;
            case PUBLIC:
                if(getTier()==4)
                    return publications;

        }
        return null;
    }

    public void setPublications() throws Exception {
        this.publications = publicationDAO.getPublicationsByScgeIdList(new ArrayList<>(allObjectsIdList));
    }

    public void setPublicationAssociatedObjectTypes() throws Exception {
        Set<Category> objectTypes=new HashSet<>();
        if(publications!=null){
            for(Publication p:publications){
                long id=  p.getReference().getAssociatedSCGEId();
                objectTypes.add(getObjectTypeOfSCGEId(id));
            }
        }
        this.publicationObjectTypes=objectTypes;
    }
    public Category getObjectTypeOfSCGEId(long id){
        String objectCode=String.valueOf(id).substring(0,2);
        for(Category category:Category.values()) {
            if(objectCode.equals(category.getObjectCode()))
                return category;
        }
        return null;
    }


    public void setGrants() throws Exception {
        List<Grant> grants=new ArrayList<>();
        for(Study study:studies) {
            grants.add( grantDao.getGrantByGroupId(study.getGroupId()));
        }
        this.grants=grants;
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

    public List<Study> getStudiesSCGEIds(List<Long> associatedSCGEIds) throws Exception {
        List<Study> studies=new ArrayList<>();
        for(long id:associatedSCGEIds){
            List<Study> studyList=studyDao.getStudiesBySCGEId(id);
            if(studyList!=null){
                studies.addAll(studyList);
            }
        }
        return studies;
    }

    public List<Study> getStudies(AccessLevel accessLevel) {
        switch (accessLevel){
            case PUBLIC:
                List<Study> studyList= studies.stream().filter(s->s.getTier()==4).collect(Collectors.toList());
                if(studyList.size()>0){
                    return studyList;
                }
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

    public List<edu.mcw.scge.datamodel.Vector> getVectors(AccessLevel accessLevel) {
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
    public void setAntibodies() throws Exception {
        List<Antibody> list=new ArrayList<>();
        Set<Long> experimentIds=recordList.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
        for(long experimentId:experimentIds) {
            list .addAll(antibodyDao.getDistinctAntibodyByExperimentId(experimentId));
        }
        this.antibodies = list;
    }
    public Set<String> getTissueIds(AccessLevel accessLevel) {
        return getRecordList(accessLevel).stream().filter(r->r.getTissueId()!=null && !r.getTissueId().equals("")).map(ExperimentRecord::getTissueId).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet());

    }
    public Set<String> getTissueTerms(AccessLevel accessLevel) {

        return getTissueIds(accessLevel).stream().filter(e->e!=null && !e.isEmpty()).map(id-> {
            try {
                return xdao.getTerm(id).getTerm();
            } catch (Exception e) {
                System.out.println(id);
                e.printStackTrace();
            }
            return null;
        }).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet());


    }
    public Set<String> getCellTypeIds(AccessLevel accessLevel) {
        return recordList.stream().map(ExperimentRecord::getCellType).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet());

    }
    public Set<String> getCellTypeTerms(AccessLevel accessLevel) {
        return getCellTypeIds(accessLevel).stream().filter(e->e!=null && !e.isEmpty()).map(id-> {
            try {
                return xdao.getTerm(id).getTerm();
            } catch (Exception e) {
                System.out.println(id);
                e.printStackTrace();
            }
            return null;
        }).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet());


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


    public Set<String> getPi(AccessLevel accessLevel){
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
    public Set<String> getProjectMembers(AccessLevel accessLevel) throws Exception {
        List<Study> studies=getStudies(accessLevel);
        Set<String> projectMembers=new HashSet<>();
        if(studies!=null) {
            for(Study study:studies) {
                projectMembers.addAll(groupdao.getGroupMembersByGroupId(study.getGroupId()).stream().map(Person::getName).collect(Collectors.toSet()));
            }
        }
        return projectMembers;
    }
    public Set<String> getSex(AccessLevel accessLevel){
        return getRecordList(accessLevel).stream().filter(r->r.getSex()!=null && !r.getSex().equals("")).map(ExperimentRecord::getSex).map(StringUtils::capitalize).collect(Collectors.toSet());
    }
    public Set<String> getGenotype(AccessLevel accessLevel){
        return getRecordList(accessLevel).stream().filter(r->r.getGenotype()!=null && !r.getGenotype().equals("")).map(ExperimentRecord::getGenotype).map(StringUtils::capitalize).collect(Collectors.toSet());

    }

    public void mapStudies(IndexDocument o, AccessLevel accessLevel) throws Exception {
        List<Study> studyList=getStudies(accessLevel);
        if(studyList.size()>0) {
            o.setStudy(studyList.stream().map(Study::getStudy).map(StringUtils::capitalize).collect(Collectors.toSet()));
            o.setStudyIds(studyList.stream().map(Study::getStudyId).collect(Collectors.toSet()));
            o.setPi(getPi(accessLevel));
            o.setProjectMembers(getProjectMembers(accessLevel));
        }

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
    public void mapExperiments(IndexDocument o, AccessLevel accessLevel) throws Exception {
        List<Experiment> experimentList=getExperiments(accessLevel);
        o.setExperimentType(experimentList.stream().map(Experiment::getType).map(StringUtils::capitalize).collect(Collectors.toSet()));
//            o.setLastModifiedDate(getLastModifiedDate(accessLevel));
        o.setSex(getSex(accessLevel));
        o.setGenotype(getGenotype(accessLevel));
        try{
            o.setExperimentNames(getExperimentNameIdMap(accessLevel));
        }catch (Exception e){e.printStackTrace();}


    }
    public void mapModels(IndexDocument indexDocument, AccessLevel accessLevel){
        List<Model> models=getModels(accessLevel);
        if(models!=null && models.size()>0) {
            indexDocument.setModelType(models.stream().map(Model::getType).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setModelSubtype(models.stream().map(Model::getSubtype).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            Set<String> modelName=new HashSet<>();
            for(Model model:models) {
                if (model.getDisplayName() != null && !model.getDisplayName().equals(""))
                    modelName.add(model.getDisplayName());
                else
                    modelName.add(model.getName());
            }
            indexDocument.setModelName(modelName);
            indexDocument.setModelOrganism(models.stream().map(Model::getOrganism).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setModelRrid(models.stream().map(Model::getRrid).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setModelSource(models.stream().map(Model::getSource).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setModelAnnotatedMap(models.stream().map(Model::getAnnotatedMap).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setTransgene(models.stream().map(Model::getTransgene).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setTransgeneReporter(models.stream().map(Model::getTransgeneReporter).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setParentalOrigin(models.stream().map(Model::getParentalOrigin).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setStrainAlias(models.stream().map(Model::getStrainAlias).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
        }
    }
    public void mapVectors(IndexDocument indexDocument, AccessLevel accessLevel){

        List<edu.mcw.scge.datamodel.Vector> vectorList=getVectors(accessLevel);
        if(vectorList!=null && vectorList.size()>0){
            indexDocument.setVectorName(vectorList.stream().map(edu.mcw.scge.datamodel.Vector::getName).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setVectorType(vectorList.stream().map(edu.mcw.scge.datamodel.Vector::getType).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setVectorSubtype(vectorList.stream().map(edu.mcw.scge.datamodel.Vector::getSubtype).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setVectorSource(vectorList.stream().map(edu.mcw.scge.datamodel.Vector::getSource).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setVectorlabId(vectorList.stream().map(edu.mcw.scge.datamodel.Vector::getLabId).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setVectorAnnotatedMap(vectorList.stream().map(edu.mcw.scge.datamodel.Vector::getAnnotatedMap).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGenomeSerotype(vectorList.stream().map(edu.mcw.scge.datamodel.Vector::getGenomeSerotype).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setCapsidSerotype(vectorList.stream().map(edu.mcw.scge.datamodel.Vector::getCapsidSerotype).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setCapsidVariant(vectorList.stream().map(edu.mcw.scge.datamodel.Vector::getCapsidVariant).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setTiterMethod(vectorList.stream().map(edu.mcw.scge.datamodel.Vector::getTiterMethod).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
        }
    }
    public void mapGuides(IndexDocument indexDocument, AccessLevel accessLevel){
        List<Guide> guideList=getGuides(accessLevel);
        if(guideList!=null && guideList.size()>0) {
            indexDocument.setGuideCompatibility(guideList.stream().map(Guide::getGuideCompatibility).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideTargetLocus(guideList.stream().map(Guide::getTargetLocus).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideSpecies(guideList.stream().map(Guide::getSpecies).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideTargetSequence(guideList.stream().map(Guide::getTargetSequence).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuidePam(guideList.stream().map(Guide::getPam).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGrnaLabId(guideList.stream().map(Guide::getGrnaLabId).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideSpacerSequence(guideList.stream().map(Guide::getSpacerSequence).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideRepeatSequence(guideList.stream().map(Guide::getRepeatSequence).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuide(guideList.stream().map(Guide::getGuide).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideForwardPrimer(guideList.stream().map(Guide::getForwardPrimer).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideReversePrimer(guideList.stream().map(Guide::getReversePrimer).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideLinkerSequence(guideList.stream().map(Guide::getLinkerSequence).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideAntiRepeatSequence(guideList.stream().map(Guide::getAntiRepeatSequence).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setModifications(guideList.stream().map(Guide::getModifications).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideLocation(guideList.stream().filter(g -> g.getChr() != null && !g.getChr().equals("")).map(g -> g.getChr() + ":" + g.getStart() + " - " + g.getStop()).collect(Collectors.toSet()));
            indexDocument.setGuideAnnotatedMap(guideList.stream().map(Guide::getAnnotatedMap).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setGuideSource(guideList.stream().map(Guide::getSource).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
        }

    }
    public void mapDelivery(IndexDocument indexDocument, AccessLevel accessLevel){
        List<Delivery> deliveriesList=getDeliveries(accessLevel);
        if(deliveriesList!=null && deliveriesList.size()>0) {
            indexDocument.setDeliverySystemName(deliveriesList.stream().map(Delivery::getName).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setDeliveryType(deliveriesList.stream().map(Delivery::getType).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setDeliverySubtype(deliveriesList.stream().map(Delivery::getSubtype).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setMolTargetingAgent(deliveriesList.stream().map(Delivery::getMolTargetingAgent).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setDeliverySource(deliveriesList.stream().map(Delivery::getSource).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setDeliveryLabId(deliveriesList.stream().map(Delivery::getLabId).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setDeliveryAnnotatedMap(deliveriesList.stream().map(Delivery::getAnnotatedMap).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
        }

    }
    public void mapEditors(IndexDocument indexDocument, AccessLevel accessLevel){
        List<Editor> editors=getEditors(accessLevel);
        if(editors!=null && editors.size()>0) {
            indexDocument.setEditorAlias(editors.stream().map(Editor::getAlias).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setEditorSpecies(editors.stream().map(Editor::getSpecies).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setEditorSymbol(editors.stream().map(Editor::getSymbol).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setEditorType(editors.stream().map(Editor::getType).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setEditorSubType(editors.stream().map(Editor::getSubtype).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setEditorVariant(editors.stream().map(Editor::getEditorVariant).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setActivity(editors.stream().map(Editor::getActivity).map(StringUtils::capitalize).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet()));
            indexDocument.setEditorAnnotatedMap(editors.stream().map(Editor::getAnnotatedMap).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet()));
            indexDocument.setEditorLocation(editors.stream().map(Editor::getAlias).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            indexDocument.setFusion(editors.stream().map(Editor::getFusion).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet()));
            indexDocument.setSubstrateTarget(editors.stream().map(Editor::getSubstrateTarget).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet()));
            indexDocument.setEditorTargetSequence(editors.stream().map(Editor::getTarget_sequence).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet()));
            indexDocument.setEditorTargetLocus(editors.stream().map(Editor::getTargetLocus).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet()));
            indexDocument.setDsbCleavageType(editors.stream().map(Editor::getDsbCleavageType).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet()));
            indexDocument.setEditorPamPreference(editors.stream().map(Editor::getPamPreference).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet()));
            indexDocument.setProteinSequence(editors.stream().map(Editor::getProteinSequence).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet()));
            indexDocument.setEditorSource(editors.stream().map(Editor::getSource).filter(e->e!=null && !e.isEmpty()).collect(Collectors.toSet()));
            indexDocument.setEditorLocation(editors.stream().filter(e->e.getChr()!=null && !e.getChr().equals(""))
                    .map(e->"CHR"+e.getChr()+":"+e.getStart()+" - "+e.getStop()).collect(Collectors.toSet()));
        }
    }
    public void mapProtocols(IndexDocument o, AccessLevel accessLevel){
        List<Protocol> protocolList=getProtocols(accessLevel);
        if(protocolList!=null && protocolList.size()>0) {
            o.setProtocolTitle(protocolList.stream().map(Protocol::getTitle).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            o.setProtocolDescription(protocolList.stream().map(Protocol::getDescription).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
        }
    }
    public void mapPublications(IndexDocument o, AccessLevel accessLevel){
        List<Publication> publicationList=getPublications(accessLevel);
        if(publicationList!=null && publicationList.size()>0) {
            o.setPublicationTitle(publicationList.stream().map(p->p.getReference().getTitle()).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            o.setPublicationDescription(publicationList.stream().map(p->p.getReference().getRefAbstract()).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
            o.setAuthorList(publicationList.stream().map(Publication::getAuthorList).filter(Objects::nonNull).flatMap(List::stream).collect(Collectors.toList()));
            o.setArticleIds(publicationList.stream().map(Publication::getArticleIds).filter(Objects::nonNull).flatMap(List::stream).collect(Collectors.toList()));
            o.setKeywords(publicationList.stream().map(p->p.getReference().getMeshTerms()).filter(Objects::nonNull).flatMap(List::stream).collect(Collectors.toSet()));
        }
    }

    public void  mapAntiBodies(IndexDocument o,AccessLevel accessLevel) throws Exception {
        List<Antibody> antibodyList=new ArrayList<>();
        Set<Long> experimentIds=getRecordList(accessLevel).stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
        for(long experimentId:experimentIds) {
            antibodyList .addAll(antibodyDao.getDistinctAntibodyByExperimentId(experimentId));
        }
        o.setAntibody(antibodyList.stream().map(Antibody::getRrid).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
        o.setExternalId(antibodyList.stream().map(Antibody::getOtherId).filter(e->e!=null && !e.isEmpty()).map(StringUtils::capitalize).collect(Collectors.toSet()));
    }
    public void mapTissues(IndexDocument indexDocument, AccessLevel accessLevel) throws Exception {
        indexDocument.setTissueTerm(getTissueTerms(accessLevel));
        indexDocument.setTissueIds(getTissueIds(accessLevel));
        indexDocument.setCellTypeTerm(getCellTypeTerms(accessLevel));
        indexDocument.setCellType(getCellTypeIds(accessLevel));
        indexDocument.setTermSynonyms(getSynonyms(accessLevel));
    }
    public void mapGrant(IndexDocument o, AccessLevel accessLevel) throws Exception {
        if(getStudies(accessLevel)!=null) {
            try {
                o.setInitiative(getInitiatives());
            } catch (Exception e) {
                e.printStackTrace();
            }
            o.setPi(getPi(accessLevel));
            o.setCurrentGrantNumber(grants.get(0).getCurrentGrantNumber());
            if (grants.get(0).getFormerGrantNumbers().size() > 0)
                o.setFormerGrantNumbers(new HashSet<>(grants.get(0).getFormerGrantNumbers()));
            if (grants.get(0).getNihReporterLink() != null && !grants.get(0).getNihReporterLink().equals(""))
                o.setNihReporterLink(grants.get(0).getNihReporterLink());
        }
    }
    public Set<String> getInitiatives() throws Exception {
        Set<String> initiatives=new HashSet<>();
        for(Grant grant:grants) {
            initiatives.addAll(Collections.singleton(UI.correctInitiative(grant.getGrantInitiative())));
            if (grant.getGrantInitiative().equalsIgnoreCase("Collaborative Opportunity Fund")) {
                List<String> cofProjectInitiatives = grantDao.getCOFProjectInitiatives(grant.getGrantId());
                if (!cofProjectInitiatives.isEmpty()) {
                    for (String cofInitiative : cofProjectInitiatives) {
                        initiatives.add(UI.correctInitiative(cofInitiative));
                    }

                }

            }
        }
        return initiatives;
    }
    public String getLastModifiedDate(AccessLevel accessLevel)  {
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

    public void mapOtherExperimentalDetails(IndexDocument o, AccessLevel accessLevel) throws Exception {
        mapStudies(o, accessLevel);
        if((t instanceof Experiment) || (t instanceof Grant))
            mapGrant(o, accessLevel);
        if(!(t instanceof Experiment))
            mapExperiments(o, accessLevel);
        if((!(t instanceof Delivery) && deliveries!=null && !(t instanceof Grant) && !(t instanceof Protocol) && !(t instanceof Publication))
                || (t instanceof Protocol) && protocolObjectTypes.contains(Category.DELIVERY)
                || (t instanceof Publication) && publicationObjectTypes.contains(Category.DELIVERY))
            mapDelivery(o, accessLevel);
        if((!(t instanceof Antibody) && antibodies!=null && !(t instanceof Grant) && !(t instanceof Protocol) && !(t instanceof Publication))
                || ((t instanceof Protocol) && protocolObjectTypes.contains(Category.ANTIBODY))
                || (t instanceof Publication) && publicationObjectTypes.contains(Category.ANTIBODY))
            mapAntiBodies(o, accessLevel);
        if((!(t instanceof Guide) && guides!=null && !(t instanceof Grant) && !(t instanceof Protocol) && !(t instanceof Publication))
                || ((t instanceof Protocol) && protocolObjectTypes.contains(Category.GUIDE))
                || (t instanceof Publication) && publicationObjectTypes.contains(Category.GUIDE))
            mapGuides(o, accessLevel);
        if((!(t instanceof Vector) && vectors!=null && !(t instanceof Grant) && !(t instanceof Protocol) && !(t instanceof Publication))
                || ((t instanceof Protocol) && protocolObjectTypes.contains(Category.VECTOR))
                || (t instanceof Publication) && publicationObjectTypes.contains(Category.VECTOR))
            mapVectors(o, accessLevel);
        if((!(t instanceof Model) && models!=null&& !(t instanceof Grant) && !(t instanceof Protocol) && !(t instanceof Publication))
                || ((t instanceof Protocol) && protocolObjectTypes.contains(Category.MODEL))
                || (t instanceof Publication) && publicationObjectTypes.contains(Category.MODEL))
            mapModels(o, accessLevel);
        if((!(t instanceof Editor) && editors!=null && !(t instanceof Grant) && !(t instanceof Protocol) && !(t instanceof Publication))
                || ((t instanceof Protocol) && protocolObjectTypes.contains(Category.EDITOR))
                || (t instanceof Publication) && publicationObjectTypes.contains(Category.EDITOR) )
            mapEditors(o,accessLevel);
        if(!(t instanceof Protocol) && protocols!=null && !(t instanceof Grant) )
            mapProtocols(o,accessLevel);
        if(!(t instanceof Publication) && publications!=null && !(t instanceof Grant))
            mapPublications(o,accessLevel);
        if(!(t instanceof Protocol) && !(t instanceof Publication))
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
