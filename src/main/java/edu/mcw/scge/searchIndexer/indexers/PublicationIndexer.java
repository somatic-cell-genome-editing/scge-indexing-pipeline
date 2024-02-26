package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.AssociationDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.PublicationDAO;
import edu.mcw.scge.datamodel.Association;
import edu.mcw.scge.datamodel.publications.Publication;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.ArrayList;
import java.util.List;

public class PublicationIndexer implements Indexer{
    PublicationDAO publicationDAO=new PublicationDAO();
    ExperimentDao experimentDao=new ExperimentDao();
    AssociationDao associationDao=new AssociationDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {

        List<IndexDocument> objects=new ArrayList<>();
        List<Publication> publications=publicationDAO.getAllPublications();
        for(Publication publication:publications){
            IndexDocument o=new IndexDocument();
            o.setAccessLevel("consortium");
            o.setTier(4);
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
            objects.add(publicDoc);

        }
        return objects;

    }

    public void mapDetails(IndexDocument o,Publication publication){
        o.setCategory("Publication");
        o.setId(publication.getReference().getKey());
        o.setAuthorList(publication.getAuthorList());
        o.setArticleIds(publication.getArticleIds());
        o.setName(publication.getReference().getTitle());
        o.setDescription(publication.getReference().getRefAbstract());

    }
}
