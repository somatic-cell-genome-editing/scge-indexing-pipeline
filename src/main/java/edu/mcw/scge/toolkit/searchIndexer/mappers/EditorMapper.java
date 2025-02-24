package edu.mcw.scge.toolkit.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.EditorDao;
import edu.mcw.scge.dao.implementation.GuideDao;
import edu.mcw.scge.datamodel.Editor;
import edu.mcw.scge.datamodel.ExperimentRecord;
import edu.mcw.scge.datamodel.Guide;
import edu.mcw.scge.toolkit.indexer.model.IndexDocument;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EditorMapper implements Mapper {

    EditorDao editorDao=new EditorDao();
    GuideDao guideDao=new GuideDao();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords,IndexDocument indexDocument) throws Exception {
        Set<Long> editorIds = new HashSet<>();
        Set<String> type = new HashSet<>();
        Set<String> subType = new HashSet<>();
        Set<String> symbol = new HashSet<>();
        Set<String> alias = new HashSet<>();
        Set<String> species = new HashSet<>();
        Set<String> editorVariant = new HashSet<>();
        Set<String> pamPreference = new HashSet<>();
        Set<String> substrateTarget = new HashSet<>();
        Set<String> activity = new HashSet<>();
        Set<String> fusion = new HashSet<>();
        Set<String> dsbCleavageType = new HashSet<>();
        Set<String> targetSequence = new HashSet<>();
        Set<String> source = new HashSet<>();
        Set<String> proteinSequence = new HashSet<>();
        Set<String> annotatedMap = new HashSet<>();
        Set<String> targetLocus = new HashSet<>();
        Set<String> assembly = new HashSet<>();
        Set<String> location = new HashSet<>();
        Set<String> description = new HashSet<>();
        if (!indexDocument.getCategory().equalsIgnoreCase("Guide")) {
        for (ExperimentRecord r : experimentRecords) {
            if (r.getEditorId() != 0 )
                if (!editorIds.contains(r.getEditorId())) {
                    editorIds.add(r.getEditorId());
                    Editor editor = new Editor();
                    try {
                        List<Editor> editors = editorDao.getEditorById(r.getEditorId());
                        if (editors.size() > 0) {
                            editor = editors.get(0);
                            if(editor.getTier()<indexDocument.getTier() && indexDocument.getCategory().equalsIgnoreCase("Publication")){
                                indexDocument.setTier(editor.getTier());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(r.getEditorId());
                        e.printStackTrace();
                    }

                    if (indexDocument.getAccessLevel().equalsIgnoreCase("consortium")
                            || (indexDocument.getAccessLevel().equalsIgnoreCase("public") && editor.getTier() == 4)) {
                        if (editor.getType() != null && !editor.getType().equals(""))
                            type.add(StringUtils.capitalize(editor.getType().trim()));
                        if (editor.getSubtype() != null && !editor.getSubtype().equals(""))
                            subType.add(StringUtils.capitalize(editor.getSubtype().trim()));
                        if (editor.getSymbol() != null && !editor.getSymbol().equals(""))
                            symbol.add(editor.getSymbol().trim());
                        if (editor.getSource() != null && !editor.getSource().equals(""))

                            source.add(editor.getSource().trim());
                        if (editor.getAlias() != null && !editor.getAlias().equals(""))

                            alias.add(editor.getAlias().trim());
                        if (editor.getSpecies() != null && !editor.getSpecies().equals(""))

                            species.add(StringUtils.capitalize(editor.getSpecies().trim()));
                        if (editor.getEditorVariant() != null && !editor.getEditorVariant().equals(""))

                            editorVariant.add(editor.getEditorVariant());
                        if (editor.getPamPreference() != null && editor.getPamPreference().equals(""))

                            pamPreference.add(editor.getPamPreference());
                        if (editor.getSubstrateTarget() != null && !editor.getSubstrateTarget().equals(""))

                            substrateTarget.add(editor.getSubstrateTarget());
                        if (editor.getActivity() != null && !editor.getActivity().equals(""))

                            activity.add(editor.getActivity());
                        if (editor.getDsbCleavageType() != null && !editor.getDsbCleavageType().equals(""))

                            dsbCleavageType.add(editor.getDsbCleavageType());
                        if (editor.getEditorDescription() != null && !editor.getEditorDescription().equals(""))

                            description.add(editor.getEditorDescription());
                        if (editor.getFusion() != null && !editor.getFusion().equals(""))

                            fusion.add(editor.getFusion());
                        if (editor.getTarget_sequence() != null && !editor.getTarget_sequence().equals(""))

                            targetSequence.add(editor.getTarget_sequence().toLowerCase().trim());
                        if (editor.getProteinSequence() != null && !editor.getProteinSequence().equals(""))

                            proteinSequence.add(editor.getProteinSequence());
                        if (editor.getTargetLocus() != null && !editor.getTargetLocus().equals("")) {

                            targetLocus.add(editor.getTargetLocus().trim());
                        }

                        List<Guide> guides = guideDao.getGuidesByEditor(editor.getId());
                  /*  for(Guide g:guides){
                        System.out.print("guide locus:"+ g.getTargetLocus() );

                        for(Editor editor1: editorDao.getEditorByGuide(g.getGuide_id())){
                            System.out.print("\tEditor Target:" +editor1.getTargetLocus());
                        }
                        System.out.print("\n");
                    }*/
                        if (editor.getAnnotatedMap() != null && !editor.getAnnotatedMap().equals(""))

                            annotatedMap.add(editor.getAnnotatedMap());
                        if (editor.getChr() != null && !editor.getChr().equals("")
                                && !editor.getStart().equals("null") && editor.getStart() != null && !editor.getStart().equals("")
                                && !editor.getStart().equals("null") && editor.getStop() != null && !editor.getStop().equals(""))
                            location.add("CHR" + editor.getChr() + ":" + editor.getStart() + " - " + editor.getStop());


                    }
                }
            if (!alias.isEmpty()) indexDocument.setEditorAlias(alias);
            if (!species.isEmpty()) indexDocument.setEditorSpecies(species);
            if (!symbol.isEmpty()) indexDocument.setEditorSymbol(symbol);
            if (!type.isEmpty()) indexDocument.setEditorType(type);
            if (!subType.isEmpty()) indexDocument.setEditorSubType(subType);

            if (!editorVariant.isEmpty()) indexDocument.setEditorVariant(editorVariant);
            if (!activity.isEmpty()) indexDocument.setActivity(activity);
            if (!annotatedMap.isEmpty()) indexDocument.setEditorAnnotatedMap(annotatedMap);
            if (!location.isEmpty()) indexDocument.setEditorLocation(location);
                  /*  StringBuilder generatedDescription = new StringBuilder();
                    if (indexDocument.getGeneratedDescription() != null) {
                        generatedDescription.append(indexDocument.getGeneratedDescription()).append("..");
                    }
                    generatedDescription.append(description.stream().collect(Collectors.joining("..")));
                    indexDocument.setGeneratedDescription(generatedDescription.toString());*/
                    if (!fusion.isEmpty()) indexDocument.setFusion(fusion);
                    if (!substrateTarget.isEmpty()) indexDocument.setSubstrateTarget(substrateTarget);
                    if (!targetSequence.isEmpty()) indexDocument.setEditorTargetSequence(targetSequence);
                    if (!targetLocus.isEmpty()) indexDocument.setEditorTargetLocus(targetLocus);
                    if (!dsbCleavageType.isEmpty()) indexDocument.setDsbCleavageType(dsbCleavageType);
                    if (!pamPreference.isEmpty()) indexDocument.setEditorPamPreference(pamPreference);
                    if (!proteinSequence.isEmpty()) indexDocument.setProteinSequence(proteinSequence);
                    if (!source.isEmpty()) indexDocument.setEditorSource(source);
                }
        }
    }

}
