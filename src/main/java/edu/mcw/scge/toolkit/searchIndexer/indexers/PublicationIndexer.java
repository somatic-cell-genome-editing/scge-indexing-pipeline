package edu.mcw.scge.toolkit.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.AssociationDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.PublicationDAO;
import edu.mcw.scge.datamodel.Association;
import edu.mcw.scge.datamodel.Protocol;
import edu.mcw.scge.datamodel.publications.Publication;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;
import edu.mcw.scge.toolkit.indexer.index.ProtocolDetails;
import edu.mcw.scge.toolkit.indexer.index.PublicationDetails;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PublicationIndexer extends Indexer<Publication>implements ObjectIndexer {
    PublicationDAO publicationDAO=new PublicationDAO();
    AssociationDao associationDao=new AssociationDao();

    @Override
    List<Publication> getObjects() throws Exception {
        return publicationDAO.getAllPublications();
    }


    public void getIndexObjects() throws Exception {


        List<Publication> publications=getObjects();
        for(Publication publication:publications){
            ObjectDetails<Publication> objectDetails=new PublicationDetails(publication);
            indexObjects(objectDetails);

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
