package edu.mcw.scge.indexer.dao.delivery;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.dao.implementation.DeliveryDao;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.datamodel.Vector;
import edu.mcw.scge.indexer.model.IndexObject;


import java.util.*;
import java.util.stream.Collectors;

public class Crawler {
    GuideDao guideDao=new GuideDao();
    EditorDao editorDao=new EditorDao();
    StudyDao studyDao=new StudyDao();
    ModelDao modelDao=new ModelDao();
    VectorDao vectorDao=new VectorDao();
    ExperimentDao experimentDao =new ExperimentDao();
    DeliveryDao deliveryDao=new DeliveryDao();
    ExperimentRecordDao erdao= new ExperimentRecordDao();
    AnimalTestingResultsDAO animalResultsDAO= new AnimalTestingResultsDAO();
    OntologyXDAO xdao=new OntologyXDAO();
    public List<ExperimentRecord> getExperimentTags(Object o) throws Exception {
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
        }else if(o instanceof Vector){
            Vector v= (Vector) o;
            xList=experimentDao.getExperimentsByVector(v.getVectorId());
        }
        Set<Integer> experimentIds=new HashSet<>();
     //     experimentIds=  xList.stream().map(p->p.getExperimentId()).collect(Collectors.toSet());
      /*      for(ExperimentRecord x:xList){

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
             /*       if(x.getModelName()!=null)
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
            */
    //    return tags;
    //    return experimentIds.size();
        return xList;
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
        /*    if(e.getType()!=null)
            o.setType(e.getType().trim());
            if(e.getSubType()!=null)
            o.setSubType(e.getSubType().trim());
            o.setSpecies(e.getSpecies());*/
            if(e.getSymbol()!=null)
            o.setSymbol(e.getSymbol().trim());
            if(e.getAlias()!= null)
            o.setAliases(Arrays.asList(e.getAlias()));
         /*   o.setPam(e.getPamPreference());*/
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
        //   o.setExperimentTags(getExperimentTags(e));
            List<ExperimentRecord> experimentRecords=getExperimentTags(e);
            o.setGuides(mapGuides(experimentRecords));
            o.setDeliveries(mapDeliveries(experimentRecords));
            o.setVectors(mapVectors(experimentRecords));
            o.setModels(mapModels(experimentRecords));
            o.setEditors(mapEditors(experimentRecords));
            o.setTarget(mapTarget(experimentRecords));
            int expCount=experimentRecords.stream().map(p->p.getExperimentId()).collect(Collectors.toSet()).size();
            o.setExperimentCount(expCount);
            if(expCount>0){
                o.setWithExperiments(e.getSubType());
            }

            Map<Integer, String> studies=  studyDao.getStudiesByEditor(e.getId()).stream().collect(Collectors.toMap(Study::getStudyId,Study::getStudy));
            o.setStudyNames(studies);

            try {
             Set<Long> experimentIds = experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
             Map<Long, String> experimentNames = experimentIds.stream().map(p -> {
                 try {
                     return new ExperimentDao().getExperiment(p);
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
                 return new Experiment();
             }).collect(Collectors.toMap(Experiment::getExperimentId, Experiment::getName));
             o.setExperimentNames(experimentNames);
         }catch (Exception ex){

         }
           objList.add(o);
        }
        return objList;
    }
    public List<IndexObject> getVectors() throws Exception {
        List<IndexObject> objList= new ArrayList<>();
        List<Vector> vectors=vectorDao.getAllVectors();
        System.out.println("EDITORS SIZE: "+vectors.size());
        for(Vector e:vectors){
            List<String> additionalData=new ArrayList<>();
            IndexObject o=new IndexObject();
            o.setCategory("Vector");
          o.setId(e.getVectorId());
//            if(e.getType()!=null)
//                o.setType(e.getType().trim());
//            if(e.getSubtype()!=null)
//                o.setSubType(e.getSubtype().trim());
            if(e.getName()!=null)
                o.setSymbol(e.getName().trim());
            o.setDescription(e.getDescription());
            if(e.getSource()!=null)
                o.setExternalId(Arrays.asList(e.getSource()));
            //     o.setExperimentTags(getExperimentTags(e));
            if(e.getCapsidSerotype()!=null && !e.getCapsidSerotype().equals("")){
                additionalData.add(e.getCapsidSerotype());
            }
            if(e.getGenomeSerotype()!=null && !e.getGenomeSerotype().equals("")){
                additionalData.add(e.getGenomeSerotype());
            }
            if(e.getLabId()!=null && !e.getLabId().equals("")){
                additionalData.add(e.getLabId());
            }
            if(e.getAnnotatedMap()!=null && !e.getAnnotatedMap().equals("")){
                additionalData.add(e.getAnnotatedMap());
            }
            if(e.getCapsidVariant()!=null && !e.getCapsidVariant().equals("")){
                additionalData.add(e.getCapsidVariant());
            }
            if(e.getSource()!=null && !e.getSource().equals("")){
                additionalData.add(e.getSource());
            }
            if(e.getTiterMethod()!=null && !e.getTiterMethod().equals("")){
                additionalData.add(e.getTiterMethod());
            }
            if(additionalData.size()>0)
                o.setAdditionalData(additionalData);
            o.setTier(e.getTier());
            if(e.getTier()==4){
                System.out.println("EDITOR TIER:"+e.getTier());
            }
            o.setReportPageLink("/toolkit/data/vector/format?id=");
            //   o.setExperimentTags(getExperimentTags(e));
            List<ExperimentRecord> experimentRecords=getExperimentTags(e);
            o.setGuides(mapGuides(experimentRecords));
            o.setDeliveries(mapDeliveries(experimentRecords));
            o.setEditors(mapEditors(experimentRecords));
            o.setModels(mapModels(experimentRecords));
            o.setVectors(mapVectors(experimentRecords));
            o.setTarget(mapTarget(experimentRecords));
            int expCount=experimentRecords.stream().map(p->p.getExperimentId()).collect(Collectors.toSet()).size();
            o.setExperimentCount(expCount);
            if(expCount>0){
                o.setWithExperiments(e.getName());
            }
            Map<Integer, String> studies=  studyDao.getStudiesByVector(e.getVectorId()).stream().collect(Collectors.toMap(Study::getStudyId,Study::getStudy));
            o.setStudyNames(studies);
            try {
               Set<Long> experimentIds = experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
               Map<Long, String> experimentNames = experimentIds.stream().map(p -> {
                   try {
                       return new ExperimentDao().getExperiment(p);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
                   return new Experiment();
               }).collect(Collectors.toMap(Experiment::getExperimentId,
                       Experiment::getName));
               o.setExperimentNames(experimentNames);
           }catch (Exception xe){

           }
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
//            o.setType(d.getType());
//            o.setSubType(d.getSubtype());
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
         //   o.setExperimentTags(getExperimentTags(d));
            List<ExperimentRecord> experimentRecords=getExperimentTags(d);
            int expCount=experimentRecords.stream().map(p->p.getExperimentId()).collect(Collectors.toSet()).size();
            o.setGuides(mapGuides(experimentRecords));
            o.setEditors(mapEditors(experimentRecords));
            o.setVectors(mapVectors(experimentRecords));
            o.setModels(mapModels(experimentRecords));
            o.setDeliveries(mapDeliveries(experimentRecords));
            o.setTarget(mapTarget(experimentRecords));
            o.setExperimentCount(expCount);
            if(expCount>0){
                o.setWithExperiments(d.getType());
            }
            Map<Integer, String> studies=  studyDao.getStudiesByDeliverySystem(d.getId()).stream().collect(Collectors.toMap(Study::getStudyId,Study::getStudy));
            o.setStudyNames(studies);
            try {
               Set<Long> experimentIds = experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
               Map<Long, String> experimentNames = experimentIds.stream().map(p -> {
                   try {
                       return new ExperimentDao().getExperiment(p);
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
                   return new Experiment();
               }).collect(Collectors.toMap(Experiment::getExperimentId,
                       Experiment::getName));
               o.setExperimentNames(experimentNames);
           }catch (Exception e){

           }
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
//            o.setSpecies(g.getSpecies());
//        //    o.setDetectionMethod(g.getDetectionMethod());
            o.setDescription(g.getGuideDescription());
//            o.setPam(g.getPam());
            List<String> externalIds=new ArrayList<>();
            if(g.getGrnaLabId()!=null)
            externalIds.add(g.getGrnaLabId());
            o.setExternalId(externalIds);
            List<String> targets= new ArrayList<>();
            if(g.getTargetLocus()!=null)
        //    targets.add(g.getTargetLocus());
         //   if(g.getTargetSequence()!=null)
        //    targets.add(g.getTargetSequence());
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
            o.setReportPageLink("/toolkit/data/guide/system?id=");
        //    o.setExperimentTags(getExperimentTags(g));
            List<ExperimentRecord> experimentRecords=getExperimentTags(g);
            int expCount=experimentRecords.stream().map(p->p.getExperimentId()).collect(Collectors.toSet()).size();
            o.setExperimentCount(expCount);
            if(expCount>0){
                o.setWithExperiments(g.getGuide());
            }
            Map<Integer, String> studies=  studyDao.getStudiesByGuide(g.getGuide_id()).stream().collect(Collectors.toMap(Study::getStudyId,Study::getStudy));
            o.setStudyNames(studies);

            try {
                Set<Long> experimentIds = experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
                o.setEditors(mapEditors(experimentRecords));
                o.setModels(mapModels(experimentRecords));
                o.setVectors(mapVectors(experimentRecords));
                o.setDeliveries(mapDeliveries(experimentRecords));
                o.setGuides(mapGuides(experimentRecords));
                o.setTarget(mapTarget(experimentRecords));
                Map<Long, String> experimentNames = experimentIds.stream().map(p -> {
                    try {
                        return new ExperimentDao().getExperiment(p);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return new Experiment();
                }).collect(Collectors.toMap(Experiment::getExperimentId,
                        Experiment::getName));
                o.setExperimentNames(experimentNames);
            }catch (Exception e){

            }
            objects.add(o);
        }
        return objects;
    }
    public List<IndexObject> getModels() throws Exception{
        List<IndexObject> objects=new ArrayList<>();
        for(Model m:modelDao.getModels()){
            IndexObject o=new IndexObject();
            List<String> additionalTags=new ArrayList<>();
            o.setCategory("Model System");
            o.setId(m.getModelId());
//            o.setType(m.getType());
            o.setName(m.getName());
//            o.setSpecies(m.getOrganism());
//            o.setSubType(m.getSubtype());
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
         //   o.setExperimentTags(getExperimentTags(m));
            List<ExperimentRecord> experimentRecords=getExperimentTags(m);
            o.setGuides(mapGuides(experimentRecords));
            o.setDeliveries(mapDeliveries(experimentRecords));
            o.setVectors(mapVectors(experimentRecords));
            o.setEditors(mapEditors(experimentRecords));
            o.setModels(mapModels(experimentRecords));
            o.setTarget(mapTarget(experimentRecords));
            int expCount=experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet()).size();
            o.setExperimentCount(expCount);
            if(expCount>0){
                o.setWithExperiments(m.getOrganism().toUpperCase());
            }
            Map<Integer, String> studies=  studyDao.getStudiesByModel(m.getModelId()).stream().collect(Collectors.toMap(Study::getStudyId,Study::getStudy));
            o.setStudyNames(studies);
            Set<Long> experimentIds=  experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
            try {
                Map<Long, String> experimentNames = experimentIds.stream().map(p -> {
                    try {
                        return new ExperimentDao().getExperiment(p);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return new Experiment();
                }).collect(Collectors.toMap(Experiment::getExperimentId,
                        Experiment::getName));

                o.setExperimentNames(experimentNames);
            }catch (Exception e){

            }
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
           //     o.setExperimentTags(getExperimentTags(m));
                List<ExperimentRecord> experimentRecords=getExperimentTags(m);
                int expCount=experimentRecords.stream().map(p->p.getExperimentId()).collect(Collectors.toSet()).size();
                o.setGuides(mapGuides(experimentRecords));
                o.setDeliveries(mapDeliveries(experimentRecords));
                o.setVectors(mapVectors(experimentRecords));
                o.setEditors(mapEditors(experimentRecords));
                o.setExperimentCount(expCount);
                if(expCount>0){
                    o.setWithExperiments(m.getTransgene());
                }
                Map<Integer, String> studies=  studyDao.getStudiesByModel(m.getModelId()).stream().collect(Collectors.toMap(Study::getStudyId,Study::getStudy));
                o.setStudyNames(studies);
                try {
                    Set<Long> experimentIds = experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
                    Map<Long, String> experimentNames = experimentIds.stream().map(p -> {
                        try {
                            return new ExperimentDao().getExperiment(p);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return new Experiment();
                    }).collect(Collectors.toMap(Experiment::getExperimentId,
                            Experiment::getName));
                    o.setExperimentNames(experimentNames);
                }catch (Exception e){}
                objects.add(o);
            }
        }
        return objects;
    }
/*   public List<IndexObject> getStudies() throws Exception {
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
              //  o.setExperimentTags(getExperimentTags(r));
                o.setTier(s.getTier());
                if(s.getTier()==4){
                    System.out.println("Study TIER:"+s.getTier());
                }
                o.setReportPageLink("/toolkit/data/experiments/study/");
                objects.add(o);
            }
        }

        return objects;
    }
*/
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
                List<ExperimentRecord> experimentRecords=experimentDao.getExperimentRecords(x.getExperimentId());

                o.setEditors(mapEditors(experimentRecords));
                o.setDeliveries(mapDeliveries(experimentRecords));
                o.setModels(mapModels(experimentRecords));
                o.setGuides(mapGuides(experimentRecords));
                o.setVectors(mapVectors(experimentRecords));
                o.setTarget(mapTarget(experimentRecords));

                objects.add(o);
            }
        }
        return objects;
    }
    public void addExperimentDetails(String objectType, int experimentId) throws Exception {
        Set<Long> editorIds=new HashSet<>();
        Set<Long> deliveryIds=new HashSet<>();
        Set<Long> modelIds=new HashSet<>();
        Set<Long> guideIds=new HashSet<>();
        Set<Long> vectorIds=new HashSet<>();

        List<String> targets=new ArrayList<>();
        List<Editor> editors = new ArrayList<>();
        List<Delivery> deliveries = new ArrayList<>();
        List<Model> models = new ArrayList<>();
        List<Guide> guides = new ArrayList<>();
        List<Vector> vectors=new ArrayList<>();
        for(ExperimentRecord r: experimentDao.getExperimentRecords(experimentId)) {
            if (r.getEditorId() != 0)
                if (!editorIds.contains(r.getEditorId())) {
                    editorIds.add(r.getEditorId());
                    editors.addAll(editorDao.getEditorById(r.getEditorId()));
                }
            if (r.getDeliverySystemId() != 0)
                if (!deliveryIds.contains(r.getDeliverySystemId())) {
                    deliveryIds.add(r.getDeliverySystemId());
                    deliveries.addAll(deliveryDao.getDeliverySystemsById(r.getDeliverySystemId()));
                }
            if (r.getModelId() != 0)
                if (!modelIds.contains(r.getModelId())) {
                    modelIds.add(r.getModelId());
                    models.add(modelDao.getModelById(r.getModelId()));
                }
            for (Guide g : guideDao.getGuidesByExpRecId(r.getExperimentRecordId())) {
                //   if (!guideIds.contains(r.getGuideId())) {
                if (!guideIds.contains(g.getGuide_id())) {
                    guideIds.add(g.getGuide_id());
                    guides.add(g);
                }
            }
            if (r.getTissueId() != null && !r.getTissueId().equals("")) {
                String t = xdao.getTerm(r.getTissueId()).getTerm();
                if (!targets.contains(t))
                    targets.add(t);
            }

            for(Vector v:     vectorDao.getVectorsByExpRecId(r.getExperimentRecordId()) ){
                if(!vectorIds.contains(v.getVectorId())){
                    vectorIds.add(v.getVectorId());
                    vectors.add(v);
                }
            }
        }
    }
    public List<Editor> mapEditors(List<ExperimentRecord> experimentRecords) throws Exception {
        Set<Long> editorIds=new HashSet<>();
        List<Editor> editors=new ArrayList<>();
        for(ExperimentRecord r: experimentRecords) {
            if (r.getEditorId() != 0)
                if (!editorIds.contains(r.getEditorId())) {
                    editorIds.add(r.getEditorId());
                    editors.addAll(editorDao.getEditorById(r.getEditorId()));
                }
        }
        return editors;
    }

    public List<Model> mapModels(List<ExperimentRecord> experimentRecords) throws Exception {
        Set<Long> modelIds=new HashSet<>();
        List<Model> models=new ArrayList<>();
        for(ExperimentRecord r: experimentRecords) {
            if (r.getModelId() != 0)
                if (!modelIds.contains(r.getModelId())) {
                    modelIds.add(r.getModelId());
                    models.add(modelDao.getModelById(r.getModelId()));
                }
        }
        return models.stream().map(m->{m.setOrganism(m.getOrganism().toUpperCase());
        m.setType(m.getType().toUpperCase()) ;return m;}).collect(Collectors.toList());
    }
    public List<Vector> mapVectors(List<ExperimentRecord> experimentRecords) throws Exception {
        Set<Long> vectorIds=new HashSet<>();
        List<Vector> vectors=new ArrayList<>();
        for(ExperimentRecord r: experimentRecords) {
            for(Vector v:     vectorDao.getVectorsByExpRecId(r.getExperimentRecordId()) ){
                if(!vectorIds.contains(v.getVectorId())){
                    vectorIds.add(v.getVectorId());
                    vectors.add(v);
                }
            }
        }
        return vectors;
    }
    public List<Delivery> mapDeliveries(List<ExperimentRecord> experimentRecords) throws Exception {
        Set<Long> deliveryIds=new HashSet<>();
        List<Delivery> deliveries=new ArrayList<>();
        for(ExperimentRecord r: experimentRecords) {
            if (r.getDeliverySystemId() != 0)
                if (!deliveryIds.contains(r.getDeliverySystemId())) {
                    deliveryIds.add(r.getDeliverySystemId());
                    deliveries.addAll(deliveryDao.getDeliverySystemsById(r.getDeliverySystemId()));
                }
        }
        return deliveries;
    }
    public List<Guide> mapGuides(List<ExperimentRecord> experimentRecords) throws Exception {
        Set<Long> guideIds=new HashSet<>();
        List<Guide> guides=new ArrayList<>();
        for(ExperimentRecord r: experimentRecords) {
            for (Guide g : guideDao.getGuidesByExpRecId(r.getExperimentRecordId())) {
                //   if (!guideIds.contains(r.getGuideId())) {
                if (!guideIds.contains(g.getGuide_id())) {
                    guideIds.add(g.getGuide_id());
                    guides.add(g);
                }
            }
        }
        return guides;
    }
    public List<String> mapTarget(List<ExperimentRecord> experimentRecords) throws Exception {
        Set<Long> ids=new HashSet<>();
        List<String> targets=new ArrayList<>();
        for(ExperimentRecord r: experimentRecords) {
            if (r.getTissueId() != null && !r.getTissueId().equals("")) {
                String t = xdao.getTerm(r.getTissueId()).getTerm();
                if (!targets.contains(t))
                    targets.add(t);
            }
        }
        return targets;
    }
}
