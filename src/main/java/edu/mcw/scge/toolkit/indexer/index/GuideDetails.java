package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.datamodel.Guide;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

import java.util.Collections;

public class GuideDetails extends ObjectDetails<Guide>{
    public GuideDetails(Guide guide) throws Exception {
        super(guide);
        this.guides= Collections.singletonList(guide);
    }
    @Override
    public int getTier() {
        return t.getTier();
    }
    @Override
    public void setStudies() throws Exception {
        this.studies= studyDao.getStudiesByGuide(t.getGuide_id());

    }

    @Override
    public void setObjectId() {
        this.associationIds=  Collections.singleton((long) t.getGuide_id());
    }

    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {

        if (getObject(accessLevel) != null) {
            o.setCategory("Guide");
            o.setId(t.getGuide_id());
            o.setName(t.getGuide());
            o.setDescription(t.getGuideDescription());
            o.setTier(t.getTier());
            o.setReportPageLink("/toolkit/data/guide/system?id=");
        }
    }
}
