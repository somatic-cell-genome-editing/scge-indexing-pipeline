package edu.mcw.scge.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.EditorDao;
import edu.mcw.scge.datamodel.Editor;

import edu.mcw.scge.indexerRefactored.indexer.EditorDetails;
import edu.mcw.scge.indexerRefactored.indexer.ObjectDetails;

import java.util.*;

public class EditorIndexer extends Indexer<Editor> implements ObjectIndexer{
    EditorDao editorDao=new EditorDao();
    @Override
    List<Editor> getObjects() throws Exception {
        return editorDao.getAllEditors();
    }
    @Override
    public void getIndexObjects() throws Exception {
        for(Editor e:getObjects()) {
            ObjectDetails<Editor> objectDetails=new EditorDetails(e);
            indexObjects(objectDetails);
        }
    }

}
