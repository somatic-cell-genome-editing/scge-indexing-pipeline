package edu.mcw.scge.searchIndexer;

import edu.mcw.scge.indexer.model.RgdIndex;
import edu.mcw.scge.searchIndexer.indexers.Indexer;
import edu.mcw.scge.searchIndexer.indexers.Indexers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class IndexerThread implements Runnable{
    String category;
    Indexers indexers=new Indexers();
    public IndexerThread(String category){this.category=category;}
    @Override
    public void run() {
        Logger log= LogManager.getLogger(category);
        log.info(Thread.currentThread().getName() + ": " + category + " started " + new Date());

        Indexer indexer = indexers.getIndexer(category);
        try {
            indexer.index(RgdIndex.getNewAlias());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
