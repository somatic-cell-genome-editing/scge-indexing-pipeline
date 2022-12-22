package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.dao.implementation.ModelDao;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Model;

import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;

public class ModelIndexer implements Indexer {
    ModelDao modelDao=new ModelDao();
    ExperimentDao experimentDao=new ExperimentDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objects=new ArrayList<>();
        for(Model m:modelDao.getModels()){
            IndexDocument o=new IndexDocument();
            o.setAccessLevel("consortium");

            List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByModel(m.getModelId());
            int expCount=experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet()).size();
            o.setExperimentCount(expCount);
            mapDetails(o,m);
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);

            objects.add(o);
            if(m.getTier()==4){
                IndexDocument publicObject = new IndexDocument();
                publicObject.setAccessLevel("public");
                mapDetails(publicObject,m);
                Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, publicObject);

                objects.add(publicObject);
            }
        }
        return objects;
    }
    public void mapDetails(IndexDocument o, Model m){
        o.setCategory("Model System");
        o.setId(m.getModelId());
        if(m.getDisplayName()!=null && !m.getDisplayName().equals(""))
        o.setName(m.getDisplayName());
        else
            o.setName(m.getName());
        o.setDescription(m.getDescription());
        o.setTier(m.getTier());
        o.setReportPageLink("/toolkit/data/models/model/?id=");
    }
}
