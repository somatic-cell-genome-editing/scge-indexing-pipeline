package edu.mcw.scge.toolkit.searchIndexer.indexers;


import edu.mcw.scge.dao.implementation.PublicationDAO;

import edu.mcw.scge.datamodel.publications.Publication;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;

import edu.mcw.scge.toolkit.indexer.index.PublicationDetails;

import java.util.List;

public class PublicationIndexer extends Indexer<Publication>implements ObjectIndexer {
    PublicationDAO publicationDAO=new PublicationDAO();

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
}
