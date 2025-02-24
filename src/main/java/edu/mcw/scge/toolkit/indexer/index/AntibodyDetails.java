package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.datamodel.Antibody;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AntibodyDetails extends ObjectDetails<Antibody>{
    public AntibodyDetails(Antibody antibody) throws Exception {
        super(antibody);
    }

    @Override
    public int getTier() {
        if (studies != null) {
            Set<Integer> tiers=studies.stream().map(Study::getTier).collect(Collectors.toSet());
            if(tiers.size()>0)
            return Collections.max(tiers);
        }
        return 1;
    }

    @Override
    public void setStudies() throws Exception {
        this.studies= studyDao.getStudiesByAntibody(t.getAntibodyId());
        System.out.println("STUIDIES SIZE: ID: "+t.getAntibodyId()+"\t"+ this.studies.size());
    }



    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {
        if(getObject(accessLevel)!=null) {
            o.setId(t.getAntibodyId());
            o.setTier(getTier());
            o.setName(t.getRrid());
            o.setDescription(t.getDescription());
            o.setCategory("Antibody");
            o.setExternalLink("https://scicrunch.org/resolver/" + t.getRrid());
            Set<String> externalIds = new HashSet<>();
            externalIds.add(t.getRrid());
            externalIds.add(t.getOtherId());
            o.setExternalId(externalIds);
        }
    }
}
