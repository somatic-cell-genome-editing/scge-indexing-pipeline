package edu.mcw.scge.indexerRefactored.indexer;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

public abstract class DAO {
    StudyDao studyDao=new StudyDao();
    GrantDao grantDao=new GrantDao();
    GroupDAO groupdao=new GroupDAO();
    ExperimentDao experimentDao=new ExperimentDao();
    EditorDao editorDao=new EditorDao();
    VectorDao vectorDao=new VectorDao();
    GuideDao guideDao=new GuideDao();
    ModelDao modelDao=new ModelDao();
    DeliveryDao deliveryDao=new DeliveryDao();
    AntibodyDao antibodyDao=new AntibodyDao();
    HRDonorDao hrDonorDao=new HRDonorDao();
    OntologyXDAO xdao=new OntologyXDAO();


    public abstract IndexDocument getIndexObject( AccessLevel accessLevel) throws Exception;
}
