package edu.mcw.scge.indexer.dao.delivery;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.dao.implementation.DeliveryDao;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.indexer.model.IndexObject;


import java.util.*;

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
          xList=experimentDao.getExperimentsByEditor(e.getId());
        }else if(o instanceof Delivery){
            Delivery d= (Delivery) o;
           xList=experimentDao.getExperimentsByDeliverySystem(d.getId());
        }else if(o instanceof Model){
            Model m=(Model) o;
           xList=experimentDao.getExperimentsByModel(m.getModelId());
        }else if(o instanceof Guide){
            Guide g= (Guide) o;
            xList=experimentDao.getExperimentsByGuide(g.getGuide_id());

        }else if(o instanceof Study){
            Study s=(Study) o;
            xList=erdao.getExperimentRecordsByStudyId(s.getStudyId());
        }else if(o instanceof Experiment){
            Experiment exp=(Experiment) o;
         //   xList=erdao.getExperimentRecordById(exp.getExperimentId());
            xList=experimentDao.getExperimentRecords(exp.getExperimentId());
        }
            for(ExperimentRecord x:xList){

             List<Guide> guides=   guideDao.getGuidesByExpRecId(x.getExperimentRecordId());
                  for(Guide g:guides){
                      tags.add(g.getGuide());
                  }
                    if(x.getStudyName()!=null)
                        tags.add(x.getStudyName());
                    if(x.getExperimentName()!=null)
                        tags.add(x.getExperimentName());
                   /* if(x.getGuide()!=null)
                        tags.add(x.getGuide());*/
                    if(x.getModelName()!=null)
                        tags.add(x.getModelName());
                    if(x.getDeliverySystemType()!=null)
                        tags.add(x.getDeliverySystemType());
                    if(x.getEditorSymbol()!=null)
                        tags.add(x.getEditorSymbol());
                    if(x.getExperimentType()!=null)
                    tags.add(x.getExperimentType());
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
            List<String> additionalData=new ArrayList<>();
            IndexObject o=new IndexObject();
            o.setCategory("Genome Editor");
            o.setId(e.getId());
            o.setType(e.getType());
            o.setSubType(e.getSubType());
            o.setSpecies(e.getSpecies());
            o.setSymbol(e.getSymbol());
            if(e.getAlias()!= null)
            o.setAliases(Arrays.asList(e.getAlias()));
            o.setPam(e.getPamPreference());
            o.setDescription(e.getEditorDescription());
            if(e.getSource()!=null)
            o.setExternalId(Arrays.asList(e.getSource()));
       //     o.setExperimentTags(getExperimentTags(e));
            if(e.getPamPreference()!=null && !e.getPamPreference().equals(""))
            additionalData.add(e.getPamPreference());
            if(e.getProteinSequence()!=null && !e.getProteinSequence().equals(""))
            additionalData.add(e.getProteinSequence());
            if(e.getDsbCleavageType()!=null && !e.getDsbCleavageType().equals(""))
            additionalData.add(e.getDsbCleavageType());
            if(e.getTarget_sequence()!=null && !e.getTarget_sequence().equals(""))
            additionalData.add(e.getTarget_sequence());
            if(e.getAnnotatedMap()!=null && !e.getAnnotatedMap().equals(""))
            additionalData.add(e.getAnnotatedMap());
            if(e.getEditorVariant()!=null && !e.getEditorVariant().equals(""))
            additionalData.add(e.getEditorVariant());
            if(e.getFusion()!=null && !e.getFusion().equals(""))
            additionalData.add(e.getFusion());
            if(e.getSubstrateTarget()!=null && !e.getSubstrateTarget().equals(""))
            additionalData.add(e.getSubstrateTarget());
            if(additionalData.size()>0)
           o.setAdditionalData(additionalData);
           o.setTier(e.getTier());
           if(e.getTier()==4){
               System.out.println("EDITOR TIER:"+e.getTier());
           }
           o.setReportPageLink("/toolkit/data/editors/editor?id=");
           o.setExperimentTags(getExperimentTags(e));
           objList.add(o);
        }
        return objList;
    }
    public List<IndexObject> getDeliverySystems() throws Exception{
        List<IndexObject> objects=new ArrayList<>();
        for(Delivery d:deliveryDao.getDeliverySystems() ){
            IndexObject o=new IndexObject();
            List<String> additionalTags=new ArrayList<>();
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
            o.setCategory("Delivery System");
        //    o.setExperimentTags(getExperimentTags(d));
            if(d.getMolTargetingAgent()!=null)
            additionalTags.add(d.getMolTargetingAgent());
            if(d.getAnnotatedMap()!=null)
            additionalTags.add(d.getAnnotatedMap());
            if(additionalTags.size()>0)
            o.setAdditionalData(additionalTags);
            o.setTier(d.getTier());
            if(d.getTier()==4){
                System.out.println("DELIVERT TIER:"+d.getTier());
            }
            o.setReportPageLink("/toolkit/data/delivery/system?id=");
            o.setExperimentTags(getExperimentTags(d));
            objects.add(o);
        }
        return objects;
    }
    public List<IndexObject> getGuides() throws Exception{
        List<IndexObject> objects=new ArrayList<>();
        for(Guide g: guideDao.getGuides()){
            IndexObject o=new IndexObject();
            List<String> additionalTags=new ArrayList<>();
            o.setCategory("Guide");
            o.setId(g.getGuide_id());
            o.setName(g.getGuide());
        //    o.setDetectionMethod(g.getDetectionMethod());
            o.setDescription(g.getGuideDescription());
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
       //     o.setExperimentTags(getExperimentTags(g));
            if(g.getLinkerSequence()!=null && !g.getLinkerSequence().equals(""))
            additionalTags.add(g.getLinkerSequence());
            if(g.getRepeatSequence()!=null && !g.getRepeatSequence().equals(""))
            additionalTags.add(g.getRepeatSequence());
            if(g.getSpacerSequence()!=null && !g.getSpacerSequence().equals(""))
            additionalTags.add(g.getSpacerSequence());
            if(g.getStandardScaffoldSequence()!=null && !g.getStandardScaffoldSequence().equals(""))
            additionalTags.add(g.getStandardScaffoldSequence());
            if(g.getGuideFormat()!=null && !g.getGuideFormat().equals(""))
            additionalTags.add(g.getGuideFormat());
            if(g.getForwardPrimer()!=null &&!g.getForwardPrimer().equals(""))
            additionalTags.add(g.getForwardPrimer());
            if(g.getModifications()!=null && !g.getModifications().equals(""))
            additionalTags.add(g.getModifications());
            if(g.getPam()!=null && !g.getPam().equals(""))
            additionalTags.add(g.getPam());
            if(g.getReversePrimer()!=null && !g.getReversePrimer().equals(""))
            additionalTags.add(g.getReversePrimer());
            if(additionalTags.size()>0)
            o.setAdditionalData(additionalTags);
            o.setTier(g.getTier());
            if(g.getTier()==4){
                System.out.println("GUIDE TIER:"+g.getTier());
            }
            o.setReportPageLink("/toolkit/data/guide/guide?id=");
            o.setExperimentTags(getExperimentTags(g));
            objects.add(o);
        }
        return objects;
    }
    public List<IndexObject> getModels() throws Exception{
        List<IndexObject> objects=new ArrayList<>();
        for(Model m:modelDao.getModels()){
            IndexObject o=new IndexObject();
            List<String> additionalTags=new ArrayList<>();
            o.setCategory("Model");
            o.setId(m.getModelId());
            o.setType(m.getType());
            o.setName(m.getName());
            o.setSpecies(m.getOrganism());
            o.setSubType(m.getSubtype());
            o.setDescription(m.getDescription());
            List<String> alias=new ArrayList<>();
        /*    if(m.getShortName()!=null)
            alias.add(m.getShortName());*/
            List<String> externalIds=new ArrayList<>();
            if(m.getRrid()!=null)
            externalIds.add(m.getRrid());
         /*   if(m.getStockNumber()!=null)
            externalIds.add(m.getStockNumber());*/
            o.setAliases(alias);
            o.setExternalId(externalIds);
         //   o.setExperimentTags(getExperimentTags(m));
            if(m.getTransgeneReporter()!=null && ! m.getTransgeneReporter().equals(""))
            additionalTags.add(m.getTransgeneReporter());
            if(m.getTransgene()!=null && !m.getTransgene().equals(""))
            additionalTags.add(m.getTransgene());
            if(m.getStrainCode()!=null &&! m.getStrainCode().equals(""))
            additionalTags.add(m.getStrainCode());
            if(m.getAnnotatedMap()!=null && !m.getAnnotatedMap().equals(""))
            additionalTags.add(m.getAnnotatedMap());
            if(m.getParentalOrigin()!=null && !m.getParentalOrigin().equals(""))
            additionalTags.add(m.getParentalOrigin());
            if(m.getTransgeneDescription()!=null && m.getTransgeneDescription().equals(""))
            additionalTags.add(m.getTransgeneDescription());
            if(additionalTags.size()>0)
            o.setAdditionalData(additionalTags);
            o.setTier(m.getTier());
            if(m.getTier()==4){
                System.out.println("MODEL TIER:"+m.getTier());
            }
            o.setReportPageLink("/toolkit/data/models/model/?id=");
            o.setExperimentTags(getExperimentTags(m));
            objects.add(o);
        }
        return objects;
    }
    public List<IndexObject> getTransgenes() throws Exception {
        List<IndexObject> objects=new ArrayList<>();
        for(Model m:modelDao.getModels()){
            if((m.getTransgene()!=null && !m.getTransgene().equals(""))
            ||(m.getTransgeneReporter()!=null ) && !m.getTransgeneReporter().equals("")) {
                IndexObject o = new IndexObject();
                o.setCategory("Reporter Element");
                o.setId(m.getModelId());
                o.setName(m.getTransgene());
                o.setDescription(m.getTransgeneDescription());
                List<String> alias = new ArrayList<>();
                if (m.getTransgeneReporter() != null)
                    alias.add(m.getTransgeneReporter());
                List<String> externalIds = new ArrayList<>();
             /*   if (m.getReporterDbIds() != null)
                    externalIds.add(m.getReporterDbIds());*/
                o.setAliases(alias);
                o.setExternalId(externalIds);
                o.setTier(m.getTier());
                if(m.getTier()==4){
                    System.out.println("Study TIER:"+m.getTier());
                }
        //        o.setExperimentTags(getExperimentTags(m));
                o.setReportPageLink("/toolkit/data/models/model/?id=");
                o.setExperimentTags(getExperimentTags(m));
                objects.add(o);
            }
        }
        return objects;
    }
  /*  public List<IndexObject> getStudies() throws Exception {
        List<IndexObject> objects=new ArrayList<>();
        List<Study> studies=studyDao.getStudies();
        System.out.println("STUDIES SIZE: "+studies.size() );
        for(Study s: studies){
            List<Experiment> records=experimentDao.getExperimentsByStudy(s.getStudyId());
            for(Experiment r:records) {
                IndexObject o = new IndexObject();
                o.setCategory("Study");
                o.setName(s.getStudy());
                o.setExperimentName(r.getName());
                o.setId(s.getStudyId());
                o.setType(r.getType());
                o.setExperimentTags(getExperimentTags(r));
                o.setTier(s.getTier());
                if(s.getTier()==4){
                    System.out.println("Study TIER:"+s.getTier());
                }
                o.setReportPageLink("/toolkit/data/experiments/study/");
                objects.add(o);
            }
        }

        return objects;
    }*/

    public List<IndexObject> getExperiments() throws  Exception{
        List<IndexObject> objects= new ArrayList<>();
        List<Study> studies=studyDao.getStudies();
        for(Study s: studies) {
            List<Experiment> experiments = experimentDao.getExperimentsByStudy(s.getStudyId());
            for (Experiment x : experiments) {
                IndexObject o = new IndexObject();
                o.setId(x.getExperimentId());
                o.setType(x.getType());
                o.setCategory("Experiment");
                o.setName(x.getName());
          //      o.setExperimentTags(getExperimentTags(x));
                o.setReportPageLink("/toolkit/data/experiments/experiment/");
                o.setTier(s.getTier());
                o.setStudy(s);

                Set<Integer> editorIds=new HashSet<>();
                Set<Integer> deliveryIds=new HashSet<>();
                Set<Integer> modelIds=new HashSet<>();
                Set<Integer> guideIds=new HashSet<>();
                List<String> targets=new ArrayList<>();
            //   for(ExperimentRecord r: erdao.getExperimentRecordById(x.getExperimentId())){
                   for(ExperimentRecord r: experimentDao.getExperimentRecords(x.getExperimentId())){

                       if(!editorIds.contains(r.getEditorId())) {
                       editorIds.add(r.getEditorId());
                       List<Editor> editors = new ArrayList<>();
                       if(o.getEditors()!=null)
                       editors.addAll(o.getEditors());
                       if(r.getEditorId()!=0)
                       editors.addAll(editorDao.getEditorById(r.getEditorId()));
                       o.setEditors(editors);
                   }
                   if(!deliveryIds.contains(r.getDeliverySystemId())) {
                       deliveryIds.add(r.getDeliverySystemId());
                       List<Delivery> deliveries = new ArrayList<>();
                       if(o.getDeliveries()!=null)
                       deliveries.addAll(o.getDeliveries());
                       if(r.getDeliverySystemId()!=0)
                       deliveries.addAll(deliveryDao.getDeliverySystemsById(r.getDeliverySystemId()));
                       o.setDeliveries(deliveries);
                   }

                   if(!modelIds.contains(r.getModelId())) {
                       modelIds.add(r.getModelId());
                       List<Model> models = new ArrayList<>();
                       if(o.getModels()!=null)
                           models.addAll(o.getModels());
                       if(r.getModelId()!=0)
                       models.add(modelDao.getModelById(r.getModelId()));
                       o.setModels(models);
                   }
                   for(Guide g:guideDao.getGuidesByExpRecId(r.getExperimentRecordId())) {
                    //   if (!guideIds.contains(r.getGuideId())) {
                           if (!guideIds.contains(g.getGuide_id())) {
                           guideIds.add(g.getGuide_id());
                           List<Guide> guides = new ArrayList<>();
                           if (o.getGuides() != null)
                               guides.addAll(o.getGuides());
                           if (g.getGuide_id() != 0)
                               guides.add(g);
                           o.setGuides(guides);
                       }
                   }
                if(r.getTissueId()!=null && !r.getTissueId().equals(""))
                   targets.add(r.getTissueId());
                   }
               o.setTarget(targets);
                objects.add(o);
            }
        }
        return objects;
    }
}
