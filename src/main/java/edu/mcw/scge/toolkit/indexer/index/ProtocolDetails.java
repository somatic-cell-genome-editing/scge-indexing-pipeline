package edu.mcw.scge.toolkit.indexer.index;


import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.toolkit.indexer.model.Category;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProtocolDetails extends ObjectDetails<Protocol>{
    public ProtocolDetails(Protocol protocol) throws Exception {
        super(protocol);
    }

    @Override
    public int getTier() {
        return t.getTier();
    }

    @Override
    public void setStudies() throws Exception {
        List<Long> associatedObjectIds = this.protocolDao.getProtocolAssociatedObjectIds(t.getId());
        setProtocolsAssociatedObjectType(associatedObjectIds);
        List<Study> studies=new ArrayList<>();
        for(long id:associatedObjectIds){
            List<Study> studyList=studyDao.getStudiesBySCGEId(id);
            if(studyList!=null){
                studies.addAll(studyList);
            }
        }
        this.studies=studies;
    }
    public void setProtocolsAssociatedObjectType(List<Long> associatedObjectIds) throws Exception {
        Set<Category> objectTypes=new HashSet<>();

            for(long id :associatedObjectIds){
                objectTypes.add(getObjectTypeOfSCGEId(id));
            }
            this.protocolObjectTypes=objectTypes;
        System.out.println("PROTOCOL OBJECT TYPES:" +protocolObjectTypes.toString());
    }
    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {
        if(getObject(accessLevel)!=null) {
            o.setCategory("Protocol");
            o.setId(t.getId());
            o.setName(t.getTitle());
            o.setDescription(t.getDescription());
            o.setTier(t.getTier());
            o.setReportPageLink("/toolkit/data/protocols/protocol/?id=");
        }
    }
}
