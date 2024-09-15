package edu.mcw.scge.indexer.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mcw.scge.indexer.model.DeliveryObject;
import edu.mcw.scge.indexer.model.IndexObject;
import edu.mcw.scge.indexer.model.RgdIndex;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class IndexService {
    public void indexDeliveryObjects(List<DeliveryObject> objects) throws IOException {
        for(DeliveryObject o:objects){
            ObjectMapper mapper=new ObjectMapper();
            byte[] json = new byte[0];
            try {
                json = mapper.writeValueAsBytes(o);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            IndexRequest request= new IndexRequest(RgdIndex.getNewAlias()).source(json, XContentType.JSON);
            ESClient.getClient().index(request, RequestOptions.DEFAULT);

        }

    }
    public void indexObjects(List<IndexObject> objs, String index, String type) throws ExecutionException, InterruptedException, IOException {
        // BulkRequestBuilder bulkRequestBuilder= ESClient.getClient().prepareBulk().setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        ObjectMapper mapper=new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        BulkRequest bulkRequest=new BulkRequest();
        //  bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        bulkRequest.timeout(TimeValue.timeValueMinutes(2));
        bulkRequest.timeout("2m");
        int docCount=0;
        for (IndexObject o : objs) {
        //    System.out.println(o.toString());
           docCount++;


            try {
               String json = mapper.writeValueAsString(o);
                bulkRequest.add(new IndexRequest(index).source(json, XContentType.JSON));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //     bulkRequestBuilder.add(new IndexRequest(index, type,o.getTerm_acc()).source(json, XContentType.JSON));



        }
        ESClient.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        //   ESClient.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);

        //   BulkResponse response=       bulkRequestBuilder.get();

        //  ESClient.getClient().admin().indices().refresh(refreshRequest()).actionGet();
        RefreshRequest refreshRequest=new RefreshRequest();
        ESClient.getClient().indices().refresh(refreshRequest, RequestOptions.DEFAULT);
    }
}
