package edu.mcw.scge.toolkit;

import edu.mcw.scge.toolkit.indexer.model.MyThreadPoolExecutor;

import edu.mcw.scge.toolkit.indexer.indexers.Indexer;
import edu.mcw.scge.toolkit.service.ESClient;
import edu.mcw.scge.toolkit.service.IndexAdmin;
import edu.mcw.scge.toolkit.indexer.model.RgdIndex;

import edu.mcw.scge.toolkit.indexer.utils.Utils;
import edu.mcw.scge.toolkit.indexer.model.Category;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Manager {
    private String version;
    private RgdIndex rgdIndex;
    private static List<String> environments;
    private IndexAdmin admin;
    String command;
    String env;
    BulkRequest bulkRequest;
    public static void main(String[] args) throws IOException {

       DefaultListableBeanFactory bf= new DefaultListableBeanFactory();
        new XmlBeanDefinitionReader(bf) .loadBeanDefinitions(new FileSystemResource("properties/AppConfigure.xml"));
        Manager manager= (Manager) bf.getBean("manager");
        manager.command=args[0];
         manager.env=args[1];
        String index="scge_search";
        List<String> indices= new ArrayList<>();
       ESClient es= (ESClient) bf.getBean("client");
        if (environments.contains(manager.env)) {
            manager.rgdIndex.setIndex(index +"_"+manager.env);
            indices.add(index+"_"+manager.env + "1");
            indices.add(index + "_"+manager.env + "2");
            manager.rgdIndex.setIndices(indices);
        }
        manager.rgdIndex= (RgdIndex) bf.getBean("rgdIndex");
        manager.bulkRequest=new BulkRequest();
        Indexer.bulkRequest=manager.bulkRequest;
        try {
            manager.run();
        } catch (Exception e) {
            if(es!=null) {
                try {
                    ESClient.destroy();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        if(es!=null)
            ESClient.destroy();
        System.out.println(manager.version);
    }
    public void run() throws Exception {
        long start = System.currentTimeMillis();

        if (command.equalsIgnoreCase("reindex"))
            admin.createIndex("", "");
        else if (command.equalsIgnoreCase("update"))
            admin.updateIndex();
        ExecutorService executor= new MyThreadPoolExecutor(10,10,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
       for(Category category:Category.values()){
//           if(category.equals(Category.PUBLICATION)) {
               Runnable workerThread = new IndexerThread(category);
               executor.execute(workerThread);
//           }
       }
       executor.shutdown();
       while (!executor.isTerminated()){}

        if(bulkRequest!=null) {
            BulkResponse bulkResponse = ESClient.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                System.err.println("Bulk indexing had errors!");
            } else {
                System.out.println("Bulk indexing completed.");
            }

        }
        String clusterStatus = this.getClusterHealth(RgdIndex.getNewAlias());
        if (!clusterStatus.equalsIgnoreCase("ok")) {
            System.out.println(clusterStatus + ", refusing to continue with operations");
        } else {
            if (command.equalsIgnoreCase("reindex")) {
                System.out.println("CLUSTER STATUR:" + clusterStatus + ". Switching Alias...");
                switchAlias();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(" - " + Utils.formatElapsedTime(start, end));
        System.out.println("CLIENT IS CLOSED");
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public RgdIndex getRgdIndex() {
        return rgdIndex;
    }

    public void setRgdIndex(RgdIndex rgdIndex) {
        this.rgdIndex = rgdIndex;
    }

    public static List<String> getEnvironments() {
        return environments;
    }

 /*   public static void setEnvironments(List<String> environments) {
        Manager.environments = environments;
    }*/

    public IndexAdmin getAdmin() {
        return admin;
    }

    public void setAdmin(IndexAdmin admin) {
        this.admin = admin;
    }

    public String getClusterHealth(String index) throws Exception {

        ClusterHealthRequest request = new ClusterHealthRequest(index);
        ClusterHealthResponse response = ESClient.getClient().cluster().health(request, RequestOptions.DEFAULT);
        System.out.println(response.getStatus().name());
        //     log.info("CLUSTER STATE: " + response.getStatus().name());
        if (response.isTimedOut()) {
            return   "cluster state is " + response.getStatus().name();
        }

        return "OK";
    }
    public boolean switchAlias() throws Exception {
        System.out.println("NEW ALIAS: " + RgdIndex.getNewAlias() + " || OLD ALIAS:" + RgdIndex.getOldAlias());
        IndicesAliasesRequest request = new IndicesAliasesRequest();


        if (RgdIndex.getOldAlias() != null) {

            IndicesAliasesRequest.AliasActions removeAliasAction =
                    new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.REMOVE)
                            .index(RgdIndex.getOldAlias())
                            .alias(rgdIndex.getIndex());
            IndicesAliasesRequest.AliasActions addAliasAction =
                    new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                            .index(RgdIndex.getNewAlias())
                            .alias(rgdIndex.getIndex());
            request.addAliasAction(removeAliasAction);
            request.addAliasAction(addAliasAction);
            //    log.info("Switched from " + RgdIndex.getOldAlias() + " to  " + RgdIndex.getNewAlias());

        }else{
            IndicesAliasesRequest.AliasActions addAliasAction =
                    new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                            .index(RgdIndex.getNewAlias())
                            .alias(rgdIndex.getIndex());
            request.addAliasAction(addAliasAction);
            //    log.info(rgdIndex.getIndex() + " pointed to " + RgdIndex.getNewAlias());
        }
        AcknowledgedResponse indicesAliasesResponse =
                ESClient.getClient().indices().updateAliases(request, RequestOptions.DEFAULT);

        return  true;

    }
    public void setEnvironments(List<String> environments) {
        Manager.environments = environments;

    }
}
