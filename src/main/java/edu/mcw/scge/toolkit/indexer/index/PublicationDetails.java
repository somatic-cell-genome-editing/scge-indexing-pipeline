package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.datamodel.publications.Publication;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;
import edu.mcw.scge.toolkit.indexer.model.Category;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PublicationDetails extends ObjectDetails<Publication>{
    public PublicationDetails(Publication publication) throws Exception {
        super(publication);
        this.publications=Collections.singletonList(publication);
    }

    @Override
    public int getTier() {
        if(studies!=null) {
            List<Integer> tiers = studies.stream().map(Study::getTier).collect(Collectors.toList());
            if (tiers.contains(4)) return 4;
            return tiers.size() > 0 ? Collections.max(tiers) : 0;
        }
        return 0;
    }

    @Override
    public void setStudies() throws Exception {
        List<Long> associatedObjectIds = this.publicationDAO.getPublicationAssoicatedSCGEIds(t.getReference().getKey());
        if(associatedObjectIds!=null && associatedObjectIds.size()>0) {
            this.publicationAssociationIds = new HashSet<>(associatedObjectIds);
            setPublicationAssociatedObjectType(associatedObjectIds);
            this.studies = getStudiesSCGEIds(associatedObjectIds);
        }
    }
    public void setPublicationAssociatedObjectType(List<Long> associatedObjectIds) throws Exception {
        Set<Category> objectTypes=new HashSet<>();

        for(long id :associatedObjectIds){
            objectTypes.add(getObjectTypeOfSCGEId(id));
        }
        this.publicationObjectTypes=objectTypes;
    }

    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {
        if(getObject(accessLevel)!=null){
            o.setTier(getTier());
            o.setCategory("Publication");
            o.setId(t.getReference().getKey());
            o.setAuthorList(t.getAuthorList());
            o.setArticleIds(t.getArticleIds());
            o.setName(t.getReference().getTitle());
            o.setDescription(t.getReference().getRefAbstract());
            if(t.getReference().getMeshTerms()!=null && t.getReference().getMeshTerms().size()>0 ){
                o.setKeywords(new HashSet<>(t.getReference().getMeshTerms()));
            }
            o.setReportPageLink("/toolkit/data/publications/publication/?key=");

        }

    }
}
