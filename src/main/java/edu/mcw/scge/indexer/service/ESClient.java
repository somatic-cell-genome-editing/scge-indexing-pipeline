package edu.mcw.scge.indexer.service;

import edu.mcw.scge.indexer.Manager;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ESClient {
    private static Logger log=Logger.getLogger(Manager.class);
    private static RestHighLevelClient client = null;
    private ESClient(){}
    public void init(){
        System.out.println("Initializing Elasticsearch Client...");
        client=getInstance();
    }
    public static void destroy() throws IOException {
        System.out.println("destroying Elasticsearh Client...");
        if(client!=null) {
            try{
                client.close();
                client = null;
            }catch (Exception e){
                log.info(e);
            }

        }
    }

    public static RestHighLevelClient getClient() {
        return getInstance();
    }

    public static void setClient(RestHighLevelClient client) {
        ESClient.client = client;
    }

    public static RestHighLevelClient getInstance() {

        if (client == null) {
            if(getHostName().contains("morn")){
                try {
              /*  client= new PreBuiltTransportClient(settings)
                        .addTransportAddress(new TransportAddress(InetAddress.getByName("green.rgd.mcw.edu"), 9300));*/
                    client = new RestHighLevelClient(RestClient.builder(
                            new HttpHost("erika01.rgd.mcw.edu", 9200, "http"),
                            new HttpHost("erika02.rgd.mcw.edu", 9200, "http"),
                            new HttpHost("erika03.rgd.mcw.edu", 9200, "http"),
                            new HttpHost("erika04.rgd.mcw.edu", 9200, "http"),
                            new HttpHost("erika05.rgd.mcw.edu", 9200, "http")

                    ).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {

                        @Override
                        public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                            return requestConfigBuilder
                                    .setConnectTimeout(5000)
                                    .setSocketTimeout(120000)
                                    ;
                        }
                    })
                    );

                } catch (Exception e) {
                    log.info(e);
                    e.printStackTrace();
                }
            }else {
                try {
              /*  client= new PreBuiltTransportClient(settings)
                        .addTransportAddress(new TransportAddress(InetAddress.getByName("green.rgd.mcw.edu"), 9300));*/
                    client = new RestHighLevelClient(RestClient.builder(
                            new HttpHost("travis.rgd.mcw.edu", 9200, "http")

                    ).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {

                        @Override
                        public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                            return requestConfigBuilder
                                    .setConnectTimeout(5000)
                                    .setSocketTimeout(120000)
                                    ;
                        }
                    })
                    );

                } catch (Exception e) {
                    log.info(e);
                    e.printStackTrace();
                }
            }
        }

        return client;
    }
    public static String getHostName(){
        String hostname = "Unknown";

        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }
        System.out.println("hostname:"+hostname);
        return hostname;
    }
}
