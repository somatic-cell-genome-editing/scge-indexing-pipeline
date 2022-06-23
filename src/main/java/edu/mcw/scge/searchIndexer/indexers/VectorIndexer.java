package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.VectorDao;
import edu.mcw.scge.datamodel.Experiment;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Study;
import edu.mcw.scge.datamodel.Vector;
import edu.mcw.scge.indexer.dao.delivery.Crawler;
import edu.mcw.scge.indexer.model.IndexObject;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;

public class VectorIndexer implements Indexer {
    VectorDao vectorDao=new VectorDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objList= new ArrayList<>();
        for(Vector e:vectorDao.getAllVectors()){
            IndexDocument o=new IndexDocument();
            o.setCategory("Vector");
            o.setId(e.getVectorId());
            if(e.getType()!=null)
           //    o.setVectorType(Collections.singleton(e.getType().trim()));
           if(e.getSubtype()!=null)
          //     o.setVectorSubtype(Collections.singleton(e.getSubtype().trim()));
            if(e.getName()!=null)
                o.setSymbol(e.getName().trim());
            o.setDescription(e.getDescription());
            o.setTier(e.getTier());
            o.setReportPageLink("/toolkit/data/vector/format?id=");
            List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByVector(e.getVectorId());
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);


            objList.add(o);
        }
        return objList;
    }
}
