package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.dao.implementation.*;

public  class DAO {
    StudyDao studyDao=new StudyDao();
    GrantDao grantDao=new GrantDao();
    GroupDAO groupdao=new GroupDAO();
    ExperimentDao experimentDao=new ExperimentDao();
    EditorDao editorDao=new EditorDao();

    ModelDao modelDao=new ModelDao();
    DeliveryDao deliveryDao=new DeliveryDao();
    AntibodyDao antibodyDao=new AntibodyDao();
    HRDonorDao hrDonorDao=new HRDonorDao();
    OntologyXDAO xdao=new OntologyXDAO();
    ProtocolDao protocolDao=new ProtocolDao();
    PublicationDAO publicationDAO=new PublicationDAO();
    AssociationDao associationDao=new AssociationDao();

}
