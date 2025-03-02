package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.datamodel.Model;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

import java.util.Collections;

public class ModelDetails extends ObjectDetails<Model>{
    public ModelDetails(Model model) throws Exception {
        super(model);
        this.models=Collections.singletonList(model);
    }

    @Override
    public int getTier() {
        return t.getTier();
    }

    @Override
    public void setStudies() throws Exception {
        this.studies= studyDao.getStudiesByModel(t.getModelId());
    }

    @Override
    public void setObjectId() {
        this.associationIds=  Collections.singleton((long) t.getModelId());
    }

    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {

        if (getObject(accessLevel) != null) {
            o.setCategory("Model System");
            o.setId(t.getModelId());
            o.setModelType(Collections.singleton(t.getType()));
            o.setModelOrganism(Collections.singleton(t.getOrganism()));
            if(t.getDisplayName()!=null && !t.getDisplayName().equals(""))
                o.setName(t.getDisplayName());
            else
                o.setName(t.getName());
            o.setDescription(t.getDescription());
            o.setTier(t.getTier());
            o.setReportPageLink("/toolkit/data/models/model/?id=");
        }
    }
}
