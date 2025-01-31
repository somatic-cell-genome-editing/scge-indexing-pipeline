package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.OntologyXDAO;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.ontologyx.Term;
import edu.mcw.scge.datamodel.ontologyx.TermSynonym;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TissueMapper implements Mapper {
    OntologyXDAO xdao=new OntologyXDAO();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<String> tissueTerm=new HashSet<>();

        Set<String> synonyms=new HashSet<>();
        Set<String> cellTypes=new HashSet<>();
        Set<String> tissueIds=experimentRecords.stream().map(r->r.getTissueId()).collect(Collectors.toSet());
        for(String tissueId:tissueIds) {
            try {
                if (tissueId != null && !tissueId.equals("")) {
                    String t = xdao.getTerm(tissueId).getTerm();
                    if (!tissueTerm.contains(t)) {
                        tissueTerm.add(StringUtils.capitalize(t.trim()));
                        getActiveSynonyms(synonyms, tissueId);

                    }
                }
            } catch (Exception e) {
                System.out.println("TISSUE ID:" + tissueId);
                e.printStackTrace();
            }
        }
        Set<String>    cellTypeIds=experimentRecords.stream().map(r->r.getCellType()).collect(Collectors.toSet());
            for(String id:cellTypeIds) {
                if (id != null && !id.equals("")) {
                    String cellType = new String();
                    try {
                        cellType = xdao.getTerm(id).getTerm();
                    } catch (Exception e) {
                        System.err.println("CELL TYPE:" + cellType );
                        e.printStackTrace();
                    }
                    if (cellType != null && !cellType.equals(""))
                        cellTypes.add(StringUtils.capitalize(cellType.trim()));
                    getActiveSynonyms(synonyms, id);

                }
            }

       if(!tissueTerm.isEmpty()) indexDocument.setTissueTerm(tissueTerm);
        if(!tissueIds.isEmpty()) indexDocument.setTissueIds(tissueIds);
        if(!cellTypes.isEmpty()) indexDocument.setCellTypeTerm(cellTypes);
        if(!cellTypeIds.isEmpty()) indexDocument.setCellType(cellTypeIds);
        if(!synonyms.isEmpty()) indexDocument.setTermSynonyms(synonyms);
    }

    private void getActiveSynonyms(Set<String> synonyms, String id) throws Exception {
        for (TermSynonym synonym : xdao.getActiveSynonyms(id)) {
            synonyms.add(StringUtils.capitalize(synonym.getName().trim()));
            synonyms.add(synonym.getTermAcc());
        }
        for (Term term : xdao.getAllActiveTermAncestors(id)) {
            synonyms.add(StringUtils.capitalize(term.getTerm().trim()));
            synonyms.add(term.getAccId());
        }
    }
}
