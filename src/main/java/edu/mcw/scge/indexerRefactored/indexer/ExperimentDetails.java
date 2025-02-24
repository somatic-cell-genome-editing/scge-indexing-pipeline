package edu.mcw.scge.indexerRefactored.indexer;

import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.indexerRefactored.indexer.model.AccessLevel;
import edu.mcw.scge.indexerRefactored.indexer.model.IndexDocument;

import java.util.Collections;
import java.util.List;

public class ExperimentDetails extends ObjectDetails<Experiment> {
    public ExperimentDetails(Experiment experiment) throws Exception {
        super(experiment);
    }

    @Override
    public int getTier() {
        return studies.get(0).getTier();//assumed 1 experiment associated to 1 study
    }

    @Override
    public void setStudies() throws Exception {
        this.studies= Collections.singletonList(studyDao.getStudyByStudyId(t.getStudyId()));
    }
    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {
        List<Study> studyList=getStudies(accessLevel);
        if(studyList!=null && studyList.size()>0) {
            o.setId(t.getExperimentId());
            o.setTier(getTier());
            o.setExperimentType(Collections.singleton(t.getType()));
            o.setLastModifiedDate(getLastModifiedDate(accessLevel));
            o.setCategory("Experiment");
            o.setName(t.getName());
            o.setReportPageLink("/toolkit/data/experiments/experiment/");

            o.setDescription(t.getDescription());

            o.setSex(getSex(accessLevel));
            o.setGenotype(getGenotype(accessLevel));
        }
    }
//    public String getLastModifiedDate(AccessLevel accessLevel){
//        switch (accessLevel){
//            case CONSORTIUM:
//                if(t.getLastModifiedDate()!=null)
//                return (t.getLastModifiedDate().toString());
//                else{
//                return  (studies.get(0).getSubmissionDate().toString());
//            }
//            case PUBLIC:
//                if(studies.get(0).getTier()==4){
//                    if(t.getLastModifiedDate()!=null)
//                        return (t.getLastModifiedDate().toString());
//                    else{
//                        return  (studies.get(0).getSubmissionDate().toString());
//                    }
//                }
//            default:
//        }
//        return null;
//    }
}
