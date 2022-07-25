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
            o.setCategory("Model System");
            o.setId(m.getModelId());
           // o.setModelType(Collections.singleton(m.getType()));
            o.setName(m.getDisplayName());
          //  o.setModelSubtype(Collections.singleton(m.getSubtype()));
            o.setDescription(m.getDescription());
            o.setTier(m.getTier());
            o.setReportPageLink("/toolkit/data/models/model/?id=");
            List<ExperimentRecord> experimentRecords=experimentDao.getExperimentsByModel(m.getModelId());
            int expCount=experimentRecords.stream().map(ExperimentRecord::getExperimentId).collect(Collectors.toSet()).size();
            o.setExperimentCount(expCount);

            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);

            objects.add(o);
        }
        return objects;
    }
}
