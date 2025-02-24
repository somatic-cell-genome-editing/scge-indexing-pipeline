package edu.mcw.scge.toolkit.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.AntibodyDao;
import edu.mcw.scge.datamodel.Antibody;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AntibodyMapper implements Mapper {
    AntibodyDao antibodyDao=new AntibodyDao();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<String> antibodies = new HashSet<>();
        Set<String> otherIds = new HashSet<>();
        Set<String> description = new HashSet<>();
        if (!indexDocument.getCategory().equalsIgnoreCase("antibody")) {
            for (ExperimentRecord r : experimentRecords) {
                List<Antibody> antibodyList = antibodyDao.getAntibodysByExpRecId(r.getExperimentRecordId());
                if (antibodyList.size() > 0) {
                    for(Antibody a:antibodyList){
                        antibodies.add(a.getRrid());
                        otherIds.add(a.getOtherId());
                        description.add(a.getDescription());
                    }
                }
            }
            if (!antibodies.isEmpty()) indexDocument.setAntibody(antibodies);
            if (!otherIds.isEmpty()) indexDocument.setExternalId(otherIds);

           /* StringBuilder generatedDescription = new StringBuilder();
            if (indexDocument.getGeneratedDescription() != null) {
                generatedDescription.append(indexDocument.getGeneratedDescription()).append("..");
            }
            generatedDescription.append(description.stream().collect(Collectors.joining("..")));
            indexDocument.setGeneratedDescription(generatedDescription.toString());*/
        }
    }

}
