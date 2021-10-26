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

public class TissueMapper implements Mapper {
    OntologyXDAO xdao=new OntologyXDAO();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<String> tissueTerm=new HashSet<>();
        Set<String> tissueIds=new HashSet<>();
        Set<String> synonyms=new HashSet<>();
        Set<String> cellTypes=new HashSet<>();
        Set<String> cellTypeIds=new HashSet<>();
        for(ExperimentRecord r: experimentRecords) {
            if (r.getTissueId() != null && !r.getTissueId().equals("")) {
                String t = xdao.getTerm(r.getTissueId()).getTerm();
                if (!tissueTerm.contains(t)) {
                    tissueTerm.add(StringUtils.capitalize(t.trim()));
                    tissueIds.add(r.getTissueId());
                    for(TermSynonym synonym:xdao.getActiveSynonyms(r.getTissueId())){
                        synonyms.add(StringUtils.capitalize(synonym.getName().trim()));
                        synonyms.add(synonym.getTermAcc());
                    }
                    for(Term term: xdao.getAllActiveTermAncestors(r.getTissueId())){
                        synonyms.add(StringUtils.capitalize(term.getTerm().trim()));
                        synonyms.add(term.getAccId());
                    }

                }
            }
            if(r.getCellType()!=null && !r.getCellType().equals("")) {
                cellTypeIds.add(r.getCellType());
                String cellType =new String();
                try {
                   cellType= xdao.getTerm(r.getCellType()).getTerm();
                }catch (Exception e){
                    System.err.println("CELL TYPE:"+ cellType);
                    e.printStackTrace();}
                if(cellType!=null)
                    cellTypes.add(StringUtils.capitalize(cellType.trim()));
                for (TermSynonym synonym : xdao.getActiveSynonyms(r.getCellType())) {
                    synonyms.add(StringUtils.capitalize(synonym.getName().trim()));
                    synonyms.add(synonym.getTermAcc());
                }
                for (Term term : xdao.getAllActiveTermAncestors(r.getCellType())) {
                    synonyms.add(StringUtils.capitalize(term.getTerm().trim()));
                    synonyms.add(term.getAccId());
                }

            }
        }
       if(!tissueTerm.isEmpty()) indexDocument.setTissueTerm(tissueTerm);
        if(!tissueIds.isEmpty()) indexDocument.setTissueIds(tissueIds);
        if(!cellTypes.isEmpty()) indexDocument.setCellTypeTerm(cellTypes);
        if(!cellTypeIds.isEmpty()) indexDocument.setCellType(cellTypeIds);
        if(!synonyms.isEmpty()) indexDocument.setTermSynonyms(synonyms);
    }
}
