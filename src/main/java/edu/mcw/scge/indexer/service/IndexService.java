package edu.mcw.scge.indexer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mcw.scge.indexer.model.DeliveryObject;
import edu.mcw.scge.indexer.model.RgdIndex;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.List;

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
}
