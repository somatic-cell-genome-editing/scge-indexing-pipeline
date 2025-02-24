package edu.mcw.scge.toolkit.searchIndexer.indexers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;
import edu.mcw.scge.toolkit.indexer.model.RgdIndex;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.xcontent.XContentType;

import java.util.List;

public abstract class Indexer<T> {

    ObjectMapper mapper=new ObjectMapper();
    public static BulkRequest bulkRequest;
    abstract List<T> getObjects() throws Exception;


    public void indexObjects(ObjectDetails<T> objectDetails) throws Exception {
        IndexDocument consortiumDoc=objectDetails.getIndexObject(AccessLevel.CONSORTIUM);
        if(consortiumDoc!=null)
            index(consortiumDoc);
        IndexDocument publicDoc=objectDetails.getIndexObject(AccessLevel.PUBLIC);
        if(publicDoc!=null){
            index(publicDoc);
        }

    }
    public void index(IndexDocument o) throws Exception {

            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            bulkRequest.timeout(TimeValue.timeValueMinutes(2));
            bulkRequest.timeout("2m");

                try {
                    String json = mapper.writeValueAsString(o);
                    bulkRequest.add(new IndexRequest(RgdIndex.getNewAlias()).source(json, XContentType.JSON));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

    }



}
