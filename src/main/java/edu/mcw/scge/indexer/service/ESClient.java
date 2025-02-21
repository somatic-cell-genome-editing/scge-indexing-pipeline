package edu.mcw.scge.indexer.service;

import edu.mcw.scge.Manager;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

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

        if(getHostName().contains("morn") || getHostName().contains("saru")){
            Properties props= getProperties();
            System.out.println("PRODUCTION ENVIRONMENT...");
            try {

                client = new RestHighLevelClientBuilder(RestClient.builder(
                        new HttpHost((String) props.get("HOST1"), 9200, "http"),
                        new HttpHost((String) props.get("HOST2"), 9200, "http"),
                        new HttpHost((String) props.get("HOST3"), 9200, "http"),
                        new HttpHost((String) props.get("HOST4"), 9200, "http"),
                        new HttpHost((String) props.get("HOST5"), 9200, "http")

                ).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {

                    @Override
                    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder
                                .setConnectTimeout(5000)
                                .setSocketTimeout(120000)
                                ;
                    }
                }).build()
                ).setApiCompatibilityMode(true).build();

            } catch (Exception e) {
                log.info(e);
                e.printStackTrace();
            }
        }else {
            System.out.println("DEV ENVIRONMENT...");

            try {
              /*  client= new PreBuiltTransportClient(settings)
                        .addTransportAddress(new TransportAddress(InetAddress.getByName("green.rgd.mcw.edu"), 9300));*/
                client = new RestHighLevelClientBuilder(RestClient.builder(
                        new HttpHost("travis.rgd.mcw.edu", 9200, "http")

                ).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {

                    @Override
                    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder
                                .setConnectTimeout(5000)
                                .setSocketTimeout(120000)
                                ;
                    }
                }).build()
                ).setApiCompatibilityMode(true).build();

            } catch (Exception e) {
                log.info(e);
                e.printStackTrace();
            }
        }
        }

        return client;
    }
    static Properties getProperties(){
        Properties props= new Properties();
        FileInputStream fis=null;


        try{
           fis=new FileInputStream("/data/pipelines/properties/elasticsearchProps.properties");
            props.load(fis);

        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
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
