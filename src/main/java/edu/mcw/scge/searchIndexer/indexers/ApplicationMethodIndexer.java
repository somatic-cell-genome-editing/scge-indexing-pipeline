package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.ApplicationMethodDao;
import edu.mcw.scge.dao.implementation.ExperimentDao;
import edu.mcw.scge.datamodel.ApplicationMethod;
import edu.mcw.scge.datamodel.Editor;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.searchIndexer.mappers.MapperFactory;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApplicationMethodIndexer implements Indexer{
    ApplicationMethodDao dao=new ApplicationMethodDao();
    ExperimentDao experimentDao=new ExperimentDao();
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


        //  o.setEditorType(Collections.singleton(StringUtils.capitalize(e.getType().trim())));
        //   o.setEditorSubType(Collections.singleton(StringUtils.capitalize(e.getSubType().trim())));
        o.setReportPageLink("/toolkit/data/editors/editor?id=");
    }
}
