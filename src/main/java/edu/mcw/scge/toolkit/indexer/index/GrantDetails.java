package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.datamodel.Grant;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class GrantDetails extends ObjectDetails<Grant>{
    public GrantDetails(Grant grant) throws Exception {
        super(grant);
    }

    @Override
    public int getTier() {
        List<Integer> tiers=studies.stream().map(Study::getTier).collect(Collectors.toList());
        if(tiers.contains(4)) return 4;
        return 0;
    }

    @Override
    public void setStudies() throws Exception {
        this.studies= studyDao.getStudiesByGroupId(t.getGroupId());

    }

    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {
        if(getTier()==4 || accessLevel.equals(AccessLevel.CONSORTIUM)) {
            o.setId(t.getGroupId());
            try {
                o.setInitiative(getInitiatives());
            } catch (Exception e) {
                e.printStackTrace();
            }
            o.setCategory("Project");
            o.setName(t.getGrantTitle());
            o.setReportPageLink("/toolkit/data/experiments/group/");
            o.setPi(getPi(accessLevel));
            o.setDescription(t.getDescription());
            o.setCurrentGrantNumber(t.getCurrentGrantNumber());
            if (t.getFormerGrantNumbers().size() > 0)
                o.setFormerGrantNumbers(new HashSet<>(t.getFormerGrantNumbers()));
            if (t.getNihReporterLink() != null && !t.getNihReporterLink().equals(""))
                o.setNihReporterLink(t.getNihReporterLink());
        }
    }
}
