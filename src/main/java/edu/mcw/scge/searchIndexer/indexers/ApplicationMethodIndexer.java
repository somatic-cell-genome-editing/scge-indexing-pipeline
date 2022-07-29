package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.ApplicationMethodDao;
import edu.mcw.scge.datamodel.ApplicationMethod;

import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.ArrayList;
import java.util.List;

public class ApplicationMethodIndexer implements Indexer{
    ApplicationMethodDao dao=new ApplicationMethodDao();
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objList= new ArrayList<>();
        List<ApplicationMethod> methods=dao.getAllApplicationMethods();
        for(ApplicationMethod method:methods) {
            IndexDocument o = new IndexDocument();
            o.setAccessLevel("consortium");

            mapDetails(o, method);

        }
        return objList;
    }
    public void mapDetails(IndexDocument o, ApplicationMethod method){
        o.setCategory("Application Method");
        o.setId(method.getApplicationId());

        o.setReportPageLink("/toolkit/data/editors/editor?id=");
    }
}
