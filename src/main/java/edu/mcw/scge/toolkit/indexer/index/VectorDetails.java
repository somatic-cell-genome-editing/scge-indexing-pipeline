package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.datamodel.Vector;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;


import java.util.Collections;

public class VectorDetails extends ObjectDetails<Vector> {
    public VectorDetails(Vector vector) throws Exception {
        super(vector);
        this.vectors= Collections.singletonList(vector);
    }
    @Override
    public int getTier() {
        return t.getTier();
    }
    @Override
    public void setStudies() throws Exception {
        this.studies= studyDao.getStudiesByVector(t.getVectorId());

    }

    @Override
    public void setObjectId() {
        this.associationIds=  Collections.singleton(t.getVectorId());
    }

    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {

        if (getObject(accessLevel) != null) {
            o.setCategory("Vector");
            o.setId(t.getVectorId());
            if(t.getType()!=null)
                if(t.getSubtype()!=null)
                    if(t.getName()!=null)
                        o.setSymbol(t.getName().trim());
            o.setDescription(t.getDescription());
            o.setTier(t.getTier());
            o.setReportPageLink("/toolkit/data/vector/format?id=");
        }
    }
}
