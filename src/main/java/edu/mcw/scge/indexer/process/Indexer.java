package edu.mcw.scge.indexer.process;


import edu.mcw.scge.indexer.dao.delivery.Crawler;
import edu.mcw.scge.indexer.model.IndexObject;
import edu.mcw.scge.indexer.model.RgdIndex;
import edu.mcw.scge.indexer.service.IndexService;


import java.util.Date;
import java.util.List;

public class Indexer {
    IndexService indexService=new IndexService();
  public void index(String objectType){
      try {
          Crawler crawler=new Crawler();
          List<IndexObject> objs = (List<IndexObject>) crawler.getClass().getMethod("get" + objectType).invoke(crawler);
          System.out.println(objectType + " size: " + objs.size());
          if(objs.size()>0){
              indexService.indexObjects(objs, RgdIndex.getNewAlias(), "search");
          }

          System.out.println("Indexed " + objectType + " objects Size: " + objs.size() + " Exiting thread.");
          System.out.println(Thread.currentThread().getName()  +  " " + objectType+ " END " + new Date() );

      }  catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException();
      }
  }

}
