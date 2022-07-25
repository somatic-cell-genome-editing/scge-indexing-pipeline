package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.DeliveryDao;
import edu.mcw.scge.datamodel.Delivery;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.searchIndexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DeliveryMapper implements Mapper {
    DeliveryDao deliveryDao=new DeliveryDao();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<Long> deliveryIds=new HashSet<>();
        Set<String> deliverySystemName= new HashSet<>();
        Set<String> molTargetingAgent= new HashSet<>();
        Set<String> deliveryType= new HashSet<>();
        Set<String> deliverySubtype= new HashSet<>();
        Set<String> deliverySource= new HashSet<>();
        Set<String> deliveryLabId= new HashSet<>();
        Set<String> deliveryAnnotatedMap= new HashSet<>();
        Set<String> description=new HashSet<>();

        for(ExperimentRecord r: experimentRecords) {
            if (r.getDeliverySystemId() != 0)
                if (!deliveryIds.contains(r.getDeliverySystemId())) {
                    deliveryIds.add(r.getDeliverySystemId());
                    Delivery delivery=deliveryDao.getDeliverySystemsById(r.getDeliverySystemId()).get(0);
                    if(delivery.getName()!=null && !delivery.getName().equals(""))

                        deliverySystemName.add(delivery.getName().trim());
                    if(delivery.getMolTargetingAgent()!=null && !delivery.getMolTargetingAgent().equals(""))

                        molTargetingAgent.add(delivery.getMolTargetingAgent());
                    if(delivery.getType()!=null && !delivery.getType().equals(""))

                        deliveryType.add(StringUtils.capitalize(delivery.getType().trim()));
                    if(delivery.getSubtype()!=null && !delivery.getSubtype().equals(""))
                    deliverySubtype.add(StringUtils.capitalize(delivery.getSubtype().trim()));
                    if(delivery.getSource()!=null && delivery.getSource().equals(""))

                        deliverySource.add(delivery.getSource());
                    if(delivery.getLabId()!=null && !delivery.getLabId().equals(""))

                        deliveryLabId.add(delivery.getLabId().trim());
                    if(delivery.getAnnotatedMap()!=null && !delivery.getAnnotatedMap().equals(""))

                        deliveryAnnotatedMap.add(delivery.getAnnotatedMap());
                    if(delivery.getDescription()!=null && !delivery.getDescription().equals(""))

                        description.add(delivery.getDescription());
                }
        }

            if(!deliverySystemName.isEmpty())    indexDocument.setDeliverySystemName(deliverySystemName);
            if(!deliveryType.isEmpty())    indexDocument.setDeliveryType( deliveryType);
            if(!deliverySubtype.isEmpty())   indexDocument.setDeliverySubtype(deliverySubtype);

        if(!molTargetingAgent.isEmpty()) indexDocument.setMolTargetingAgent( molTargetingAgent);

        if(!deliverySource.isEmpty()) indexDocument.setDeliverySource(deliverySource);
        if(!deliveryLabId.isEmpty()) indexDocument.setDeliveryLabId(deliveryLabId);
        if(!deliveryAnnotatedMap.isEmpty()) indexDocument.setDeliveryAnnotatedMap(deliveryAnnotatedMap);
        StringBuilder generatedDescription=new StringBuilder();

        if(indexDocument.getGeneratedDescription()!=null){
            generatedDescription.append(indexDocument.getGeneratedDescription()).append("..");
        }
        generatedDescription.append(description.stream().collect(Collectors.joining("..")));
        if(!generatedDescription.toString().isEmpty())    indexDocument.setGeneratedDescription(generatedDescription.toString());
    }
}
