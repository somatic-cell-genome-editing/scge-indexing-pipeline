package edu.mcw.scge.searchIndexer.processThreads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mcw.scge.indexer.model.RgdIndex;
import edu.mcw.scge.indexer.service.BulkIndexProcessor;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.xcontent.XContentType;

public class Indexer {
    ObjectMapper mapper = new ObjectMapper();
    public void index(IndexDocument o) throws JsonProcessingException {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = mapper.writeValueAsString(o);
        System.out.println("INDEX:"+ RgdIndex.getNewAlias()+"\t"+json.toString());
        BulkIndexProcessor.bulkProcessor.add(new IndexRequest(RgdIndex.getNewAlias()).source(json, XContentType.JSON));
    }
}
