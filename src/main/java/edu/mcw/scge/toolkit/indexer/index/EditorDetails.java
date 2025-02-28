package edu.mcw.scge.toolkit.indexer.index;

import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.toolkit.indexer.model.AccessLevel;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

import java.util.Collections;


public class EditorDetails extends ObjectDetails<Editor>{
    public EditorDetails(Editor editor) throws Exception {
        super(editor);
        this.editors= Collections.singletonList(editor);
    }

    @Override
    public int getTier() {
        return t.getTier();
    }

    @Override
    public void setStudies() throws Exception {
        this.studies= studyDao.getStudiesByEditor(t.getId());

    }

    @Override
    public void mapObject(IndexDocument o, AccessLevel accessLevel) {
        if (getObject(accessLevel) != null) {
            o.setCategory("Genome Editor");
            o.setId(t.getId());
            if (t.getSymbol() != null)
                o.setSymbol(t.getSymbol().trim());
            o.setDescription(t.getEditorDescription());
            o.setTier(t.getTier());
            o.setReportPageLink("/toolkit/data/editors/editor?id=");
        }
    }
}
