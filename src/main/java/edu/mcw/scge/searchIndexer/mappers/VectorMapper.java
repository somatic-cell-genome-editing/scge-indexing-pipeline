package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.VectorDao;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Vector;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VectorMapper implements Mapper {
    VectorDao vectorDao=new VectorDao();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<Long> vectorIds=new HashSet<>();
        Set<String> vectorName=new HashSet<>();
        Set<String> vectorType=new HashSet<>();
        Set<String> vectorSubtype=new HashSet<>();
        Set<String> source=new HashSet<>();
        Set<String> vectorlabId=new HashSet<>();
        Set<String> vectorAnnotatedMap=new HashSet<>();
        Set<String> genomeSerotype=new HashSet<>();
        Set<String> capsidSerotype=new HashSet<>();
        Set<String> capsidVariant=new HashSet<>();
        Set<String> titerMethod=new HashSet<>();
        Set<String> description=new HashSet<>();
        for(ExperimentRecord r: experimentRecords) {
            for(Vector v:     vectorDao.getVectorsByExpRecId(r.getExperimentRecordId()) ){
                if(!vectorIds.contains(v.getVectorId())){
                    vectorIds.add(v.getVectorId());
                    if(v.getName()!=null && !v.getName().equals(""))
                    vectorName.add(StringUtils.capitalize(v.getName().trim()));
                    if(v.getType()!=null && !v.getType().equals(""))

                        vectorType.add(StringUtils.capitalize(v.getType().trim()));
                    if(v.getName()!=null && !v.getName().equals(""))

                        vectorSubtype.add(StringUtils.capitalize(v.getSubtype().trim()));
                    if(v.getSource()!=null && !v.getSource().equals(""))

                        source.add(StringUtils.capitalize(v.getSource().trim()));
                    if(v.getLabId()!=null && !v.getLabId().equals(""))

                        vectorlabId.add(v.getLabId());
                    if(v.getAnnotatedMap()!=null && !v.getAnnotatedMap().equals(""))

                        vectorAnnotatedMap.add(v.getAnnotatedMap());
                    if(v.getGenomeSerotype()!=null && !v.getGenomeSerotype().equals(""))

                        genomeSerotype.add(v.getGenomeSerotype());
                    if(v.getCapsidSerotype()!=null && !v.getCapsidSerotype().equals(""))

                        capsidSerotype.add(v.getCapsidSerotype());
                    if(v.getCapsidVariant()!=null && !v.getCapsidVariant().equals(""))

                        capsidVariant.add(v.getCapsidVariant());
                    if(v.getTiterMethod()!=null && !v.getTiterMethod().equals(""))

                        titerMethod.add(v.getTiterMethod());
                    if(v.getDescription()!=null && !v.getDescription().equals(""))

                        description.add(v.getDescription());
                }
            }
        }
        if(!indexDocument.getCategory().equalsIgnoreCase("vector")) {
            if(!vectorName.isEmpty())   indexDocument.setVectorName(vectorName);
            if(!vectorType.isEmpty())   indexDocument.setVectorType(vectorType);
            if(!vectorSubtype.isEmpty()) indexDocument.setVectorSubtype(vectorSubtype);
        }
       if(!source.isEmpty()) indexDocument.setVectorSource(source);
        if(!vectorlabId.isEmpty()) indexDocument.setVectorlabId(vectorlabId);
        if(!vectorAnnotatedMap.isEmpty()) indexDocument.setVectorAnnotatedMap(vectorAnnotatedMap);
        if(!genomeSerotype.isEmpty()) indexDocument.setGenomeSerotype(genomeSerotype);
        if(!capsidSerotype.isEmpty()) indexDocument.setCapsidSerotype(capsidSerotype);
        if(!capsidVariant.isEmpty()) indexDocument.setCapsidVariant(capsidVariant);
        if(!titerMethod.isEmpty()) indexDocument.setTiterMethod(titerMethod);
        StringBuilder generatedDescription=new StringBuilder();
        if(indexDocument.getGeneratedDescription()!=null){
            generatedDescription.append(indexDocument.getGeneratedDescription()).append("..");
        }
        generatedDescription.append(description.stream().collect(Collectors.joining("..")));
        indexDocument.setGeneratedDescription(generatedDescription.toString());
    }
}
