package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.ModelDao;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Model;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelMapper implements Mapper {
    ModelDao modelDao=new ModelDao();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<Long> modelIds=new HashSet<>();
        Set<String> modelType=new HashSet<>();
        Set<String> modelOrganism=new HashSet<>();
        Set<String> modelRrid=new HashSet<>();
        Set<String> modelSource=new HashSet<>();
        Set<String> modelSubtype=new HashSet<>();
        Set<String> modelAnnotatedMap=new HashSet<>();
        Set<String> modelName=new HashSet<>();
        Set<String> transgene=new HashSet<>();
        Set<String> transgeneReporter=new HashSet<>();
        Set<String> parentalOrigin=new HashSet<>();
        Set<String> strainCode=new HashSet<>();
        Set<String> strainAlias=new HashSet<>();
        Set<String> description=new HashSet<>();
        for(ExperimentRecord r: experimentRecords) {
            if (r.getModelId() != 0)
                if (!modelIds.contains(r.getModelId())) {
                    modelIds.add(r.getModelId());
                    Model model=modelDao.getModelById(r.getModelId());
                    if(model.getType()!=null && ! model.getType().equals(""))

                        modelType.add(StringUtils.capitalize(model.getType().trim()));
                    if(model.getOrganism()!=null && ! model.getOrganism().equals(""))
                    modelOrganism.add(StringUtils.capitalize(model.getOrganism().trim()));
                    if(model.getRrid()!=null && ! model.getRrid().equals(""))
                    modelRrid.add(model.getRrid());
                    if(model.getSource()!=null && ! model.getSource().equals(""))
                    modelSource.add(StringUtils.capitalize(model.getSource().trim()));
                    if(model.getSubtype()!=null && ! model.getSubtype().equals(""))
                    modelSubtype.add(StringUtils.capitalize(model.getSubtype().trim()));
                    if(model.getAnnotatedMap()!=null && ! model.getAnnotatedMap().equals(""))
                    modelAnnotatedMap.add(model.getAnnotatedMap());
                    if(model.getName()!=null && ! model.getName().equals(""))
                    modelName.add(model.getName());
                    if(model.getTransgene()!=null && ! model.getTransgene().equals(""))
                    transgene.add(model.getTransgene());
                    if(model.getDescription()!=null && ! model.getDescription().equals(""))
                    description.add(model.getDescription());
                    if(model.getTransgeneDescription()!=null && ! model.getTransgeneDescription().equals(""))
                    description.add(model.getTransgeneDescription());
                    if(model.getTransgeneReporter()!=null && ! model.getTransgeneReporter().equals(""))
                    transgeneReporter.add(model.getTransgeneReporter());
                    if(model.getParentalOrigin()!=null && ! model.getParentalOrigin().equals(""))
                    parentalOrigin.add(model.getParentalOrigin());
                  //  if(model.getStrainCode()!=null && ! model.getStrainCode().equals(""))
                  //  strainCode.add(model.getStrainCode());
                    if(model.getStrainAlias()!=null && ! model.getStrainAlias().equals(""))
                    strainAlias.add(model.getStrainAlias());
                }
        }
        if(!indexDocument.getCategory().equalsIgnoreCase("model")) {
            if(!modelType.isEmpty())   indexDocument.setModelType(modelType);
            if(!modelSubtype.isEmpty())   indexDocument.setModelSubtype(modelSubtype);
            if(!modelName.isEmpty())  indexDocument.setModelName(modelName);
            if(!modelOrganism.isEmpty())  indexDocument.setModelOrganism(modelOrganism);
        }
        if(!modelRrid.isEmpty())  indexDocument.setModelRrid(modelRrid);
        if(!modelSource.isEmpty())  indexDocument.setModelSource(modelSource);
        if(!modelAnnotatedMap.isEmpty())  indexDocument.setModelAnnotatedMap(modelAnnotatedMap);
        if(!transgene.isEmpty())  indexDocument.setTransgene(transgene);
        if(!transgeneReporter.isEmpty()) indexDocument.setTransgeneReporter(transgeneReporter);
        if(!parentalOrigin.isEmpty()) indexDocument.setParentalOrigin(parentalOrigin);
        if(!strainCode.isEmpty()) indexDocument.setStrainCode(strainCode);
        if(!strainAlias.isEmpty()) indexDocument.setStrainAlias(strainAlias);
        StringBuilder generatedDescription=new StringBuilder();
        if(indexDocument.getGeneratedDescription()!=null){
            generatedDescription.append(indexDocument.getGeneratedDescription()).append("..");
        }
        generatedDescription.append(description.stream().collect(Collectors.joining("..")));
        indexDocument.setGeneratedDescription(generatedDescription.toString());
    }
}
