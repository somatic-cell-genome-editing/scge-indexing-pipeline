package edu.mcw.scge.toolkit.searchIndexer.indexers;

import edu.mcw.scge.dao.implementation.EditorDao;
import edu.mcw.scge.datamodel.Editor;

import edu.mcw.scge.toolkit.indexer.index.EditorDetails;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;

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
