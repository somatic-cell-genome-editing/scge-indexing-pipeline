package edu.mcw.scge.toolkit.indexer.indexers;

import edu.mcw.scge.datamodel.Editor;

import edu.mcw.scge.toolkit.indexer.index.EditorDetails;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;

import java.util.*;

public class EditorIndexer extends Indexer<Editor> implements ObjectIndexer{
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
