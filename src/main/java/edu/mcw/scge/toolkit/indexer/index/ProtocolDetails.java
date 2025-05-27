package edu.mcw.scge.toolkit.indexer.index;


import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.toolkit.indexer.model.Category;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;

import java.util.*;

public class ProtocolDetails extends ObjectDetails<Protocol>{
    public ProtocolDetails(Protocol protocol) throws Exception {
        super(protocol);
        this.protocols= Collections.singletonList(protocol);
    }

    @Override
    public int getTier() {
        return t.getTier();
    }

    @Override
    public void setStudies() throws Exception {
        setAssociatedIds();
        if(associationIds!=null && associationIds.size()>0) {
            setProtocolsAssociatedObjectType(new ArrayList<>(associationIds));
            this.studies = getStudiesSCGEIds(new ArrayList<>(associationIds));
        }
    }

    @Override
    public void setObjectId() {

    }

    public void setAssociatedIds() throws Exception {
       List<Long> ids= this.protocolDao.getProtocolAssociatedObjectIds(t.getId());
       if(ids!=null && ids.size()>0 )
           this.associationIds= new HashSet<>(ids);
    }

    public void setProtocolsAssociatedObjectType(List<Long> associatedObjectIds) throws Exception {
        Set<Category> objectTypes=new HashSet<>();

            for(long id :associatedObjectIds){
                objectTypes.add(getObjectTypeOfSCGEId(id));
            }
            this.protocolObjectTypes=objectTypes;
    }
    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {
        if(getObject(accessLevel)!=null) {
            o.setCategory("Protocol");
            o.setId(t.getId());
            o.setName(t.getTitle());
            o.setDescription(t.getDescription());
            o.setTier(t.getTier());
            o.setReportPageLink("/toolkit/data/protocols/protocol?id=");
        }
    }
}
