package edu.mcw.scge.toolkit;

import edu.mcw.scge.toolkit.indexer.model.Category;
import edu.mcw.scge.toolkit.searchIndexer.indexers.IndexerFactory;
import edu.mcw.scge.toolkit.searchIndexer.indexers.ObjectIndexer;

public class IndexerThread implements Runnable{
    private final Category category;
    public IndexerThread(Category category){
        this.category=category;
    }
    @Override
    public void run() {
        ObjectIndexer indexer=  IndexerFactory.getIndexer(category);
        try {
            if(indexer!=null)
                indexer.getIndexObjects();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
