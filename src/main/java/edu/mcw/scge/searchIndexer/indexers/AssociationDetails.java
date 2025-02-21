package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.datamodel.Vector;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;

public class AssociationDetails {

    private final IndexDocument o;
    private final Association association;
    ExperimentDao experimentDao =new ExperimentDao();

    public AssociationDetails(IndexDocument o, Association association){
        this.association=association;
        this.o=o;
    }
     void associateDetails() throws Exception {
       associateExperimentRecords();
       associateEditor();
       associateGuide();
       associateModel();
       associateDeliverySystem();
       associateVector();

    }
    private void associateExperimentRecords() throws Exception {
        List<ExperimentRecord> experimentRecords=new ArrayList<>();
        for(Experiment experiment:association.getAssociatedExperiments()){
            experimentRecords.addAll(experimentDao.getExperimentRecords(experiment.getExperimentId()));
        }
        if(experimentRecords.size()>0)
            Objects.requireNonNull(MapperFactory.getMapper("experiment")).mapFields(experimentRecords, o);

        else {

        }
    }
    private void associateEditor() throws Exception {
        for(Editor e:association.getAssociatedEditors()) {
            List<ExperimentRecord> experimentRecords = experimentDao.getExperimentsByEditor(e.getId());
            Objects.requireNonNull(MapperFactory.getMapper("editor")).mapFields(experimentRecords, o);
        }

    }
    private void associateDeliverySystem() throws Exception {

        for(Delivery e:association.getAssociatedDeliverySystems()) {
            List<ExperimentRecord> experimentRecords = experimentDao.getExperimentsByDeliverySystem(e.getId());
            Objects.requireNonNull(MapperFactory.getMapper("delivery")).mapFields(experimentRecords, o);
        }
    }
    private void associateModel() throws Exception {
        for(Model e:association.getAssociatedModels()) {
            List<ExperimentRecord> experimentRecords = experimentDao.getExperimentsByModel(e.getModelId());
            Objects.requireNonNull(MapperFactory.getMapper("model")).mapFields(experimentRecords, o);
        }
    }
    private void associateVector() throws Exception {
        for(Vector e:association.getAssociatedVectors()) {
            List<ExperimentRecord> experimentRecords = experimentDao.getExperimentsByVector(e.getVectorId());
            Objects.requireNonNull(MapperFactory.getMapper("vector")).mapFields(experimentRecords, o);
        }
    }
    private void associateGuide() throws Exception {
        for(Guide e:association.getAssociatedGuides()) {
            List<ExperimentRecord> experimentRecords = experimentDao.getExperimentsByGuide(e.getGuide_id());
            Objects.requireNonNull(MapperFactory.getMapper("guide")).mapFields(experimentRecords, o);
        }
    }

}
