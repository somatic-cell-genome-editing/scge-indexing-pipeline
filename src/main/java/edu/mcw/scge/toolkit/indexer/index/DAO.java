package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.dao.implementation.*;

public  class DAO {
   public StudyDao studyDao=new StudyDao();
    public GrantDao grantDao=new GrantDao();
    public GroupDAO groupdao=new GroupDAO();
    public ExperimentDao experimentDao=new ExperimentDao();
    public EditorDao editorDao=new EditorDao();

    public GuideDao guideDao=new GuideDao();
    public VectorDao vectorDao=new VectorDao();
    public ModelDao modelDao=new ModelDao();
    public DeliveryDao deliveryDao=new DeliveryDao();
    public AntibodyDao antibodyDao=new AntibodyDao();

    public HRDonorDao hrDonorDao=new HRDonorDao();
    public OntologyXDAO xdao=new OntologyXDAO();
    public ProtocolDao protocolDao=new ProtocolDao();
    public PublicationDAO publicationDAO=new PublicationDAO();
}
