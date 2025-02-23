package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.EditorDao;
import edu.mcw.scge.datamodel.Editor;

import edu.mcw.scge.indexerRefactored.indexer.EditorDetails;
import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;

public class EditorIndexer extends Indexer<Editor> {
    EditorDao editorDao=new EditorDao();
    @Override
    List<Editor> getObjects() throws Exception {
        return editorDao.getAllEditors();
    }
    @Override
    public List<IndexDocument> getIndexObjects() throws Exception {
        List<IndexDocument> objList=new ArrayList<>();
        for(Editor e:getObjects()) {
            ObjectDetails<Editor> objectDetails=new EditorDetails(e);
            objList.addAll(indexObjects(objectDetails));
        }
        return objList;
    }

}
