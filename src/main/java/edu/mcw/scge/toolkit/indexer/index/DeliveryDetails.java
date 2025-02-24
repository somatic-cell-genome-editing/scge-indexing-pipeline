package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.datamodel.Delivery;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

public class DeliveryDetails extends ObjectDetails<Delivery> {
    public DeliveryDetails(Delivery delivery) throws Exception {
        super(delivery);
    }

    @Override
    public int getTier() {
        return t.getTier();
    }

    @Override
    public void setStudies() throws Exception {
        this.studies= studyDao.getStudiesByDeliverySystem(t.getId());

    }
    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {
        if (getObject(accessLevel) != null) {
            o.setId(t.getId());
            o.setName(t.getName());
            o.setDescription(t.getDescription());
            o.setCategory("Delivery System");
            o.setTier(t.getTier());
            o.setReportPageLink("/toolkit/data/delivery/system?id=");
        }
    }
}
