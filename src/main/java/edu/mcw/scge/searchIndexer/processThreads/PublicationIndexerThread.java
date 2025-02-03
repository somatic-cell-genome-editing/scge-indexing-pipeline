package edu.mcw.scge.searchIndexer.processThreads;

import edu.mcw.scge.dao.implementation.AssociationDao;
import edu.mcw.scge.dao.implementation.PublicationDAO;
import edu.mcw.scge.datamodel.Association;
import edu.mcw.scge.datamodel.publications.Publication;
import edu.mcw.scge.indexer.Manager;
import edu.mcw.scge.searchIndexer.indexers.AssociationDetails;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashSet;

public class PublicationIndexerThread extends Indexer implements Runnable{
    PublicationDAO publicationDAO=new PublicationDAO();
    AssociationDao associationDao=new AssociationDao();
    @Override
    public void run() {
        Logger log= LogManager.getLogger(Manager.class);

        log.info(Thread.currentThread().getName() + ": " + "PUBLICATION" + " started " + new Date());
        try {
            for(Publication publication:publicationDAO.getAllPublications()){
                IndexDocument o=new IndexDocument();
                o.setAccessLevel("consortium");
                o.setTier(4); // publications do not have TIER. So manually set it to 4 initially then update with object tier if it is less
                mapDetails(o,publication);
                Association association=associationDao.getPublicationAssociations(publication.getReference().getKey());
                AssociationDetails details=new AssociationDetails( o, association);
                details.associateDetails();
                index(o);
                IndexDocument publicDoc=new IndexDocument();
                publicDoc.setAccessLevel("public");
                publicDoc.setTier(4);
                mapDetails(publicDoc,publication);
                AssociationDetails details1=new AssociationDetails( publicDoc, association);
                details1.associateDetails();
                if(publicDoc.getEditorSymbol()!=null || publicDoc.getDeliverySystemName()!=null
                        || publicDoc.getGuide()!=null || publicDoc.getVectorName()!=null || publicDoc.getModelName()!=null || publicDoc.getExperimentName()!=null || publicDoc.getModelOrganism()!=null)
                   index(publicDoc);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info(Thread.currentThread().getName() + ": " + "PUBLICATION" + " END " + new Date());
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
