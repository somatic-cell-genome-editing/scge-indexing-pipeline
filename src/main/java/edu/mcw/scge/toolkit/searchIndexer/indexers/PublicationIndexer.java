package edu.mcw.scge.toolkit.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.AssociationDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.PublicationDAO;
import edu.mcw.scge.datamodel.Association;
import edu.mcw.scge.datamodel.publications.Publication;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PublicationIndexer extends Indexer<Publication> {
    PublicationDAO publicationDAO=new PublicationDAO();
    ExperimentDao experimentDao=new ExperimentDao();
    AssociationDao associationDao=new AssociationDao();

    @Override
    List<Publication> getObjects() throws Exception {
        return publicationDAO.getAllPublications();
    }


    public void getIndexObjects() throws Exception {

        List<IndexDocument> objects=new ArrayList<>();
        List<Publication> publications=getObjects();
        for(Publication publication:publications){
            IndexDocument o=new IndexDocument();
            o.setAccessLevel("consortium");
            o.setTier(4); // publications do not have TIER. So manually set it to 4 initially then update with object tier if it is less
             mapDetails(o,publication);
            Association association=associationDao.getPublicationAssociations(publication.getReference().getKey());
            AssociationDetails details=new AssociationDetails( o, association);
            details.associateDetails();
            objects.add(o);
            IndexDocument publicDoc=new IndexDocument();
            publicDoc.setAccessLevel("public");
            publicDoc.setTier(4);
            mapDetails(publicDoc,publication);
            AssociationDetails details1=new AssociationDetails( publicDoc, association);
            details1.associateDetails();
            if(publicDoc.getEditorSymbol()!=null || publicDoc.getDeliverySystemName()!=null
            || publicDoc.getGuide()!=null || publicDoc.getVectorName()!=null || publicDoc.getModelName()!=null || publicDoc.getExperimentName()!=null || publicDoc.getModelOrganism()!=null)
            objects.add(publicDoc);

        }


    }

    public void mapDetails(IndexDocument o,Publication publication){
        o.setCategory("Publication");
        o.setId(publication.getReference().getKey());
        o.setAuthorList(publication.getAuthorList());
        o.setArticleIds(publication.getArticleIds());
        o.setName(publication.getReference().getTitle());
        o.setDescription(publication.getReference().getRefAbstract());
        if(publication.getReference().getMeshTerms()!=null && publication.getReference().getMeshTerms().size()>0 ){
            o.setKeywords(new HashSet<>(publication.getReference().getMeshTerms()));
        }
        o.setReportPageLink("/toolkit/data/publications/publication/?key=");

    }
}
