package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;

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

            try {
                o.setSex(getSex(accessLevel));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                o.setGenotype(getGenotype(accessLevel));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
