package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.VectorDao;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Vector;

import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;

public class VectorIndexer implements Indexer {
    VectorDao vectorDao=new VectorDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objList= new ArrayList<>();
        for(Vector e:vectorDao.getAllVectors()){
            IndexDocument o=new IndexDocument();
            o.setAccessLevel("consortium");
            mapDetails(o, e);
            List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByVector(e.getVectorId());
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);


            objList.add(o);
            if(e.getTier()==4){
                IndexDocument publicObject = new IndexDocument();
                publicObject.setAccessLevel("public");
                mapDetails(publicObject,e);
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);

                objList.add(publicObject);
            }
        }
        return objList;
    }
    public void mapDetails(IndexDocument o, Vector e){
        o.setCategory("Vector");
        o.setId(e.getVectorId());
        if(e.getType()!=null)
            if(e.getSubtype()!=null)
                if(e.getName()!=null)
                    o.setSymbol(e.getName().trim());
        o.setDescription(e.getDescription());
        o.setTier(e.getTier());
        o.setReportPageLink("/toolkit/data/vector/format?id=");
    }
}
