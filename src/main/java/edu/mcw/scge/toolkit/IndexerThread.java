package edu.mcw.scge.toolkit;

import edu.mcw.scge.toolkit.indexer.model.Category;
import edu.mcw.scge.toolkit.searchIndexer.indexers.IndexerFactory;
import edu.mcw.scge.toolkit.searchIndexer.indexers.ObjectIndexer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class IndexerThread implements Runnable{
    private final Category category;
    public IndexerThread(Category category){
        this.category=category;
    }
    @Override
    public void run() {
        Logger log= LogManager.getLogger(Manager.class);
        System.out.println(Thread.currentThread().getName()  + ": " + category+ " started " + new Date() );
        log.info(Thread.currentThread().getName()  + ": " + category+ " started " + new Date() );

        ObjectIndexer indexer=  IndexerFactory.getIndexer(category);
        try {
            if(indexer!=null)
                indexer.getIndexObjects();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName()  + ": " + category+ " END " + new Date() );
        log.info(Thread.currentThread().getName()  + ": " + category+ " END " + new Date() );

    }
}
