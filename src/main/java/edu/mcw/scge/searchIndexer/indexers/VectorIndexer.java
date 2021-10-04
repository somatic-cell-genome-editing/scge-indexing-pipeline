package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.indexer.dao.delivery.Crawler;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.List;

public class VectorIndexer implements Indexer {
    Crawler crawler=new Crawler();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        return null;
    }
}
