package edu.mcw.scge.toolkit.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.GuideDao;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Guide;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuideMapper implements Mapper {
    GuideDao guideDao=new GuideDao();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<Long> guideIds=new HashSet<>();
        List<Guide> guides=new ArrayList<>();
        Set<String> guideTargetLocus=new HashSet<>();
        Set<String> guideSpecies=new HashSet<>();
        Set<String> guideTargetSequence=new HashSet<>();
        Set<String> guidePam=new HashSet<>();
        Set<String> grnaLabId=new HashSet<>();
        Set<String> guideSpacerSequence=new HashSet<>();
        Set<String> guideRepeatSequence=new HashSet<>();
        Set<String> guide=new HashSet<>();
        Set<String> guideForwardPrimer=new HashSet<>();
        Set<String> guideReversePrimer=new HashSet<>();
        Set<String> guideLinkerSequence=new HashSet<>();
        Set<String> guideAntiRepeatSequence=new HashSet<>();
        Set<String> guideStemloopSequence=new HashSet<>();
        Set<String> guideFormat=new HashSet<>();
        Set<String> modifications=new HashSet<>();
        Set<String> standardScaffoldSequence=new HashSet<>();
        Set<String> ivtConstructSource=new HashSet<>();
        Set<String> vectorName=new HashSet<>();
        Set<String> vectorDescription=new HashSet<>();
        Set<String> vectorType=new HashSet<>();
        Set<String> specificityRatio=new HashSet<>();
        Set<String> guideAnnotatedMap=new HashSet<>();
        Set<String> guideLocation=new HashSet<>();
        Set<String> description= new HashSet<>();
        Set<String> guideCompaitibility=new HashSet<>();
        Set<String> source = new HashSet<>();

        for(ExperimentRecord r: experimentRecords) {
            for (Guide g : guideDao.getGuidesByExpRecId(r.getExperimentRecordId())) {
                //   if (!guideIds.contains(r.getGuideId())) {
                if(g.getTier()<indexDocument.getTier() && indexDocument.getCategory().equalsIgnoreCase("Publication")){
                    indexDocument.setTier(g.getTier());
                }
                if (indexDocument.getAccessLevel().equalsIgnoreCase("consortium")
                        || (indexDocument.getAccessLevel().equalsIgnoreCase("public") && g.getTier() == 4)) {
                    if (!guideIds.contains(g.getGuide_id())) {
                        guideIds.add(g.getGuide_id());
                        if (g.getSource() != null && !g.getSource().equals(""))

                            source.add(g.getSource().trim());
                        if (g.getTargetLocus() != null && !g.getTargetLocus().equals(""))

                            guideTargetLocus.add(g.getTargetLocus());
                        if (g.getTargetSequence() != null && !g.getTargetSequence().equals(""))
                            guideTargetSequence.add(g.getTargetSequence().toUpperCase());
                        if (g.getSpecies() != null && !g.getSpecies().equals(""))
                            guideSpecies.add(StringUtils.capitalize(g.getSpecies().trim()));
                        if (g.getPam() != null && !g.getPam().equals(""))
                            guidePam.add(g.getPam());
                        if (g.getGrnaLabId() != null && !g.getGrnaLabId().equals(""))
                            grnaLabId.add(g.getGrnaLabId());
                        if (g.getSpacerSequence() != null && !g.getSpacerSequence().equals(""))
                            guideSpacerSequence.add(g.getSpacerSequence());
                        if (g.getGuide() != null && !g.getGuide().equals(""))
                            guide.add(g.getGuide());
                        if (g.getRepeatSequence() != null && !g.getRepeatSequence().equals(""))
                            guideRepeatSequence.add(g.getRepeatSequence());
                        if (g.getForwardPrimer() != null && !g.getForwardPrimer().equals(""))
                            guideForwardPrimer.add(g.getForwardPrimer());
                        if (g.getReversePrimer() != null && !g.getReversePrimer().equals(""))
                            guideReversePrimer.add(g.getReversePrimer());
                        if (g.getChr() != null && !g.getChr().equals(""))
                            guideLocation.add("CHR:" + g.getChr() + ":" + g.getStart() + " - " + g.getStop());
                        if (g.getModifications() != null && !g.getModifications().equals(""))
                            modifications.add(g.getModifications());
                        if (g.getAntiRepeatSequence() != null && !g.getAntiRepeatSequence().equals(""))
                            guideAntiRepeatSequence.add(g.getAntiRepeatSequence());
                        if (g.getAnnotatedMap() != null && !g.getAnnotatedMap().equals(""))
                            guideAnnotatedMap.add(g.getAnnotatedMap());
                        if (g.getGuideDescription() != null && !g.getGuideDescription().equals(""))
                            description.add(g.getGuideDescription());
                        if (g.getGuideCompatibility() != null && !g.getGuideCompatibility().equals(""))
                            guideCompaitibility.add(g.getGuideCompatibility());
                    }
                }
            }
        }
        if(!guideCompaitibility.isEmpty()) indexDocument.setGuideCompatibility(guideCompaitibility);
        if(!guideTargetLocus.isEmpty())  indexDocument.setGuideTargetLocus(guideTargetLocus);
        if(!guideSpecies.isEmpty()) indexDocument.setGuideSpecies(guideSpecies);
        if(!guideTargetSequence.isEmpty()) indexDocument.setGuideTargetSequence(guideTargetSequence);
        if(!guidePam.isEmpty()) indexDocument. setGuidePam(guidePam);
        if(!grnaLabId.isEmpty()) indexDocument. setGrnaLabId( grnaLabId);
        if(!guideSpacerSequence.isEmpty()) indexDocument. setGuideSpacerSequence(guideSpacerSequence);
        if(!guideRepeatSequence.isEmpty()) indexDocument.setGuideRepeatSequence(guideRepeatSequence);
            if(!guide.isEmpty())   indexDocument.setGuide(guide);
        if(!guideForwardPrimer.isEmpty()) indexDocument.setGuideForwardPrimer(guideForwardPrimer);
        if(!guideReversePrimer.isEmpty()) indexDocument.setGuideReversePrimer( guideReversePrimer);
        if(!guideLinkerSequence.isEmpty()) indexDocument. setGuideLinkerSequence( guideLinkerSequence);
        if(!guideAntiRepeatSequence.isEmpty()) indexDocument. setGuideAntiRepeatSequence(guideAntiRepeatSequence);

        if(!modifications.isEmpty())  indexDocument.setModifications( modifications);

        if(!guideLocation.isEmpty())indexDocument.setGuideLocation( guideLocation);
        if(!guideAnnotatedMap.isEmpty()) indexDocument.setGuideAnnotatedMap(guideAnnotatedMap);
        if (!source.isEmpty()) indexDocument.setGuideSource(source);

        /*   StringBuilder generatedDescription=new StringBuilder();
        if(indexDocument.getGeneratedDescription()!=null){
            generatedDescription.append(indexDocument.getGeneratedDescription()).append("..");
        }
        generatedDescription.append(description.stream().collect(Collectors.joining("..")));
        if(!generatedDescription.toString().isEmpty())  indexDocument.setGeneratedDescription(generatedDescription.toString());*/
    }
}
