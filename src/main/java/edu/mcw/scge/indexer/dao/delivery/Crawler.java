package edu.mcw.scge.indexer.dao.delivery;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.dao.implementation.DeliveryDao;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.indexer.model.IndexObject;
import htsjdk.tribble.index.Index;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Crawler {
    GuideDao guideDao=new GuideDao();
    EditorDao editorDao=new EditorDao();
    StudyDao studyDao=new StudyDao();
    ModelDao modelDao=new ModelDao();
    ExperimentDao experimentDao =new ExperimentDao();
    DeliveryDao deliveryDao=new DeliveryDao();
    ExperimentRecordDao erdao= new ExperimentRecordDao();
    AnimalTestingResultsDAO animalResultsDAO= new AnimalTestingResultsDAO();

    public List<String> getExperimentTags(Object o) throws Exception {
        List<String> tags=new ArrayList<>();
        List<ExperimentRecord> xList=new ArrayList<>();
        if(o instanceof Editor){
            Editor e= (Editor) o;
         //   xList=experimentDao.getExperimentsByEditor(e.getId());
        }else if(o instanceof Delivery){
            Delivery d= (Delivery) o;
         //   xList=experimentDao.getExperimentsByDeliverySystem(d.getId());
        }else if(o instanceof Model){
            Model m=(Model) o;
        //    xList=experimentDao.getExperimentsByModel(m.getModelId());
        }else if(o instanceof Guide){
            Guide g= (Guide) o;
            xList=experimentDao.getExperimentsByGuide(g.getGuide_id());

        }else if(o instanceof Study){
            Study s=(Study) o;
       //     xList=experimentDao.getExperimentsByStudy(s.getStudyId());
        }
            for(ExperimentRecord x:xList){
                    if(x.getStudyName()!=null)
                        tags.add(x.getStudyName());
                    if(x.getExperimentName()!=null)
                        tags.add(x.getExperimentName());
                    if(x.getGuide()!=null)
                        tags.add(x.getGuide());
                    if(x.getModelName()!=null)
                        tags.add(x.getModelName());
                    if(x.getDeliverySystemType()!=null)
                        tags.add(x.getDeliverySystemType());
                    if(x.getEditorSymbol()!=null)
                        tags.add(x.getEditorSymbol());
                    if(x.getSamplePrep()!=null)
                        tags.add(x.getSamplePrep());
                        List<AnimalTestingResultsSummary> results = animalResultsDAO.getResultsByExperimentRecId(x.getExperimentRecordId());
                        for (AnimalTestingResultsSummary s : results) {
                            if(s.getTissueTerm()!=null)
                            tags.add(s.getTissueTerm());
                            //  tags.add(s.getParentTissueTerm());
                        }
            }

        return tags;
    }

    public List<IndexObject> getEditors() throws Exception {
        List<IndexObject> objList= new ArrayList<>();
        List<Editor> editors=editorDao.getAllEditors();
        System.out.println("EDITORS SIZE: "+editors.size());
        for(Editor e:editors){
            IndexObject o=new IndexObject();
            o.setCategory("Editor");
            o.setId(e.getId());
            o.setType(e.getType());
            o.setSubType(e.getSubType());
            o.setSpecies(e.getSpecies());
            o.setSymbol(e.getSymbol());
            if(e.getAlias()!= null)
            o.setAliases(Arrays.asList(e.getAlias()));
            o.setPam(e.getPamPreference());
            o.setDescription(e.getNote());
            if(e.getAddGeneLink()!=null)
            o.setExternalId(Arrays.asList(e.getAddGeneLink()));
            o.setExperimentTags(getExperimentTags(e));
           objList.add(o);
        }
        return objList;
    }
    public List<IndexObject> getDeliverySystems() throws Exception{
        List<IndexObject> objects=new ArrayList<>();
        for(Delivery d:deliveryDao.getDeliverySystems() ){
            IndexObject o=new IndexObject();
            o.setId(d.getId());
            o.setType(d.getType());
            o.setSubType(d.getSubtype());
            o.setName(d.getName());
            o.setDescription(d.getDescription());
            List<String> externalsIds=new ArrayList<>();
            if(d.getLabId()!=null)
            externalsIds.add(d.getLabId());
            if(d.getRrid()!=null)
            externalsIds.add(d.getRrid());
            o.setExternalId(externalsIds);
            o.setCategory("Delivery");
            o.setExperimentTags(getExperimentTags(d));
            objects.add(o);
        }
        return objects;
    }
    public List<IndexObject> getGuides() throws Exception{
        List<IndexObject> objects=new ArrayList<>();
        for(Guide g: guideDao.getGuides()){
            IndexObject o=new IndexObject();
            o.setCategory("Guide");
            o.setId(g.getGuide_id());
            o.setName(g.getGuide());
            o.setDetectionMethod(g.getDetectionMethod());
            o.setPam(g.getPam());
            List<String> externalIds=new ArrayList<>();
            if(g.getGrnaLabId()!=null)
            externalIds.add(g.getGrnaLabId());
            o.setExternalId(externalIds);
            List<String> targets= new ArrayList<>();
            if(g.getTargetLocus()!=null)
            targets.add(g.getTargetLocus());
            if(g.getTargetSequence()!=null)
            targets.add(g.getTargetSequence());
            o.setTarget(targets);
            o.setExperimentTags(getExperimentTags(g));
            objects.add(o);
        }
        return objects;
    }
    public List<IndexObject> getModels() throws Exception{
        List<IndexObject> objects=new ArrayList<>();
        for(Model m:modelDao.getModels()){
            IndexObject o=new IndexObject();
            o.setCategory("Model");
            o.setId(m.getModelId());
            o.setType(m.getType());
            o.setName(m.getName());
            o.setSpecies(m.getOrganism());
            o.setSubType(m.getSubtype());
            List<String> alias=new ArrayList<>();
            if(m.getShortName()!=null)
            alias.add(m.getShortName());
            List<String> externalIds=new ArrayList<>();
            if(m.getRrid()!=null)
            externalIds.add(m.getRrid());
            if(m.getStockNumber()!=null)
            externalIds.add(m.getStockNumber());
            o.setAliases(alias);
            o.setExternalId(externalIds);
            o.setExperimentTags(getExperimentTags(m));
            objects.add(o);
        }
        return objects;
    }
    public List<IndexObject> getTransgenes() throws Exception {
        List<IndexObject> objects=new ArrayList<>();
        for(Model m:modelDao.getModels()){
            IndexObject o=new IndexObject();
            o.setCategory("Transgene");
            o.setId(m.getModelId());
            o.setName(m.getTransgene());
            o.setDescription(m.getTransgeneDescription());
            List<String> alias=new ArrayList<>();
            if(m.getTransgeneReporter()!=null)
            alias.add(m.getTransgeneReporter());
            List<String> externalIds=new ArrayList<>();
            if(m.getReporterDbIds()!=null)
            externalIds.add(m.getReporterDbIds());
            o.setAliases(alias);
            o.setExternalId(externalIds);
            o.setExperimentTags(getExperimentTags(m));
            objects.add(o);
        }
        return objects;
    }
    public List<IndexObject> getStudies() throws Exception {
        List<IndexObject> objects=new ArrayList<>();
        for(Study s: studyDao.getStudies()){
            IndexObject o=new IndexObject();
            o.setCategory("Study");
            o.setName(s.getStudy());
            o.setId(s.getStudyId());
           // o.setType(s.getType());
            o.setExperimentTags(getExperimentTags(s));

        }

        return objects;
    }


}
