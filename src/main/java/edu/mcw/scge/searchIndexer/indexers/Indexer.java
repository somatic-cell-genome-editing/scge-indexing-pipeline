package edu.mcw.scge.searchIndexer.indexers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mcw.scge.indexer.service.ESClient;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.xcontent.XContentType;

import java.util.List;

public interface Indexer {
     List<IndexDocument> getIndexObjects() throws Exception;

    default void index(String index) throws Exception {
        List<IndexDocument> objects=getIndexObjects();
        if(objects.size()>0){
            System.out.println("objects Size:"+ objects.size());

            ObjectMapper mapper=new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

            BulkRequest bulkRequest=new BulkRequest();
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        //    bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
          //  bulkRequest.timeout(TimeValue.timeValueMinutes(2));
          //  bulkRequest.timeout("2m");
            for (IndexDocument o : objects) {
                try {
                    String json = mapper.writeValueAsString(o);
                    bulkRequest.add(new IndexRequest(index).source(json, XContentType.JSON));

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                //     bulkRequestBuilder.add(new IndexRequest(index, type,o.getTerm_acc()).source(json, XContentType.JSON));



            }
            ESClient.getClient().bulk(bulkRequest, RequestOptions.DEFAULT);

            RefreshRequest refreshRequest=new RefreshRequest();
            ESClient.getClient().indices().refresh(refreshRequest, RequestOptions.DEFAULT);        }
    }


}
