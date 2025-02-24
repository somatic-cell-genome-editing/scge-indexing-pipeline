package edu.mcw.scge.indexerRefactored.indexer.model;




import edu.mcw.scge.datamodel.publications.Publication;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndexDocument extends Publication {

    private long id;
    private String name;
    private String symbol;
 //   private String type;
 //   private String subType;
 //   private String species;
    private String description;
    private String category;
    private Set<String> aliases;
    private String assembly;
    private String chr;
    private String start;
    private String stop;
    private String strand;
    private int tier;
    private String reportPageLink;
    private String externalLink;
    private Set<String> externalId; //stocknumber etc.
    private int experimentCount;
    private String withExperiments;
    private Map<Integer, String> studyNames;
    private Map<Long, String> experimentNames;
  //  private String generatedDescription;
    private String lastModifiedDate;
    /**********************EXPERIMENT FIELDS*********************/
    private Set<String> experimentType;
    private Set<String> experimentName;
    private Set<String> samplePrep;
    private Set<String> genotype;
    private Set<String> sex;
    private Set<String> cellType;
    private Set<String> tissueTerm;
    private Set<String> tissueIds;
    private Set<String> cellTypeTerm;
    private Set<String> dosage;
    private Set<String> injectionFrequency;
    private Set<String> study;
    private Set<String> termSynonyms;

    /*******************STUDY FIELDS****************************/
    private Set<String> studyName;
    private Set<String> labName;
    private Set<String> pi;
    private String status;
    private String access;
    private String accessLevel;
    private String submissionDate;
    private Set<String> initiative;
    private String studyType;
    private int studyId;
    private Set<Integer> studyIds;

    /******************EDITOR FIELDS*************************/
    private Set<String> editorType;
    private Set<String> editorSubType;
    private Set<String> editorAlias;
    private Set<String> editorSpecies;
    private Set<String> editorAssembly;
    private Set<String> editorChr;
    private Set<String> editorStart;
    private Set<String> editorStop;
    private Set<String> editorStrand;
    private Set<String> editorSymbol;
    private Set<String> editorVariant;
    private Set<String> editorPamPreference;
    private Set<String> substrateTarget;
    private Set<String> activity;
    private Set<String> fusion;
    private Set<String> dsbCleavageType;
    private Set<String> editorTargetSequence;
    private Set<String> proteinSequence;
    private Set<String> editorLocation;
    private Set<String> editorAnnotatedMap;
    private Set<String> editorSource;
    private Set<String> editorTargetLocus;
   /****************************GUIDE FIELDS*********************/

   private Set<String> guideTargetLocus;
   private Set<String> guideSpecies;
    private Set<String> guideTargetSequence;
    private Set<String> guidePam;
    private Set<String> grnaLabId;
    private Set<String> guideSpacerSequence;
    private Set<String> guideRepeatSequence;
    private Set<String> guide;
    private Set<String> guideForwardPrimer;
    private Set<String> guideReversePrimer;
    private Set<String> guideLinkerSequence;
    private Set<String> guideAntiRepeatSequence;
    private Set<String> guideStemloopSequence;
    private Set<String> guideFormat;
    private Set<String> modifications;
    private Set<String> standardScaffoldSequence;
    private Set<String> ivtConstructSource;

    private Set<String> specificityRatio;
    private Set<String> guideLocation;
    private Set<String> guideAnnotatedMap;
    private Set<String> guideCompatibility;
    private Set<String> guideSource;
    /**************************MODEL FIELDS*********************************/
    private Set<String> modelType;
    private Set<String> modelOrganism;
    private Set<String> modelRrid;
    private Set<String> modelSource;
    private Set<String> modelSubtype;
    private Set<String> modelAnnotatedMap;
    private Set<String> modelName;
    private Set<String> transgene;
    private Set<String> transgeneDescription;
    private Set<String> transgeneReporter;
    private Set<String> parentalOrigin;
    private Set<String> strainCode;
    private Set<String> strainAlias;
    /******************************VECTOR FIELDS**************************/
    private Set<String> vectorName;
    private Set<String> vectorType;
    private Set<String> vectorSubtype;
    private Set<String> vectorSource;
    private Set<String> vectorlabId;
    private Set<String> vectorAnnotatedMap;
    private Set<String> genomeSerotype;
    private Set<String> capsidSerotype;
    private Set<String> capsidVariant;
    private Set<String> titerMethod;

    /******************************Delivery FIELDS************************/

    private Set<String> deliverySystemName;
    private Set<String> molTargetingAgent;
    private Set<String> deliveryType;
    private Set<String> deliverySubtype;
    private Set<String> deliverySource;
    private Set<String> deliveryLabId;
    private Set<String> deliveryAnnotatedMap;

    /************************Antibody************************************/
    private Set<String> antibody;
    /*********************Grant number*******************************/
    private Set<String> formerGrantNumbers;
    private String currentGrantNumber;
    private Set<String> projectMembers;
    private String nihReporterLink;
    private Set<String> keywords;


    public Set<Integer> getStudyIds() {
        return studyIds;
    }

    public void setStudyIds(Set<Integer> studyIds) {
        this.studyIds = studyIds;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public String getNihReporterLink() {
        return nihReporterLink;
    }

    public void setNihReporterLink(String nihReporterLink) {
        this.nihReporterLink = nihReporterLink;
    }

    public int getStudyId() {
        return studyId;
    }

    public void setStudyId(int studyId) {
        this.studyId = studyId;
    }

    public Set<String> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(Set<String> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public Set<String> getFormerGrantNumbers() {
        return formerGrantNumbers;
    }

    public void setFormerGrantNumbers(Set<String> formerGrantNumbers) {
        this.formerGrantNumbers = formerGrantNumbers;
    }

    public String getCurrentGrantNumber() {
        return currentGrantNumber;
    }

    public void setCurrentGrantNumber(String currentGrantNumber) {
        this.currentGrantNumber = currentGrantNumber;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public Set<String> getAntibody() {
        return antibody;
    }

    public void setAntibody(Set<String> antibody) {
        this.antibody = antibody;
    }

    public Set<String> getGuideCompatibility() {
        return guideCompatibility;
    }

    public void setGuideCompatibility(Set<String> guideCompatibility) {
        this.guideCompatibility = guideCompatibility;
    }

    public Set<String> getInitiative() {
        return initiative;
    }

    public void setInitiative(Set<String> initiative) {
        this.initiative = initiative;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   /* public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public String getReportPageLink() {
        return reportPageLink;
    }

    public void setReportPageLink(String reportPageLink) {
        this.reportPageLink = reportPageLink;
    }

    public Set<String> getExternalId() {
        return externalId;
    }

    public void setExternalId(Set<String> externalId) {
        this.externalId = externalId;
    }



    public int getExperimentCount() {
        return experimentCount;
    }

    public void setExperimentCount(int experimentCount) {
        this.experimentCount = experimentCount;
    }

    public String getWithExperiments() {
        return withExperiments;
    }

    public void setWithExperiments(String withExperiments) {
        this.withExperiments = withExperiments;
    }

    public Map<Integer, String> getStudyNames() {
        return studyNames;
    }

    public void setStudyNames(Map<Integer, String> studyNames) {
        this.studyNames = studyNames;
    }

    public Map<Long, String> getExperimentNames() {
        return experimentNames;
    }

    public void setExperimentNames(Map<Long, String> experimentNames) {
        this.experimentNames = experimentNames;
    }


    public Set<String> getSamplePrep() {
        return samplePrep;
    }

    public void setSamplePrep(Set<String> samplePrep) {
        this.samplePrep = samplePrep;
    }

    public Set<String> getGenotype() {
        return genotype;
    }

    public void setGenotype(Set<String> genotype) {
        this.genotype = genotype;
    }

    public Set<String> getSex() {
        return sex;
    }

    public void setSex(Set<String> sex) {
        this.sex = sex;
    }

    public Set<String> getCellType() {
        return cellType;
    }

    public void setCellType(Set<String> cellType) {
        this.cellType = cellType;
    }

    public Set<String> getTissueTerm() {
        return tissueTerm;
    }

    public void setTissueTerm(Set<String> tissueTerm) {
        this.tissueTerm = tissueTerm;
    }

    public Set<String> getCellTypeTerm() {
        return cellTypeTerm;
    }

    public void setCellTypeTerm(Set<String> cellTypeTerm) {
        this.cellTypeTerm = cellTypeTerm;
    }

    public Set<String> getDosage() {
        return dosage;
    }

    public void setDosage(Set<String> dosage) {
        this.dosage = dosage;
    }

    public Set<String> getInjectionFrequency() {
        return injectionFrequency;
    }

    public void setInjectionFrequency(Set<String> injectionFrequency) {
        this.injectionFrequency = injectionFrequency;
    }

    public Set<String> getStudy() {
        return study;
    }

    public void setStudy(Set<String> study) {
        this.study = study;
    }

    public Set<String> getTermSynonyms() {
        return termSynonyms;
    }

    public void setTermSynonyms(Set<String> termSynonyms) {
        this.termSynonyms = termSynonyms;
    }

    public Set<String> getLabName() {
        return labName;
    }

    public void setLabName(Set<String> labName) {
        this.labName = labName;
    }

    public Set<String> getPi() {
        return pi;
    }

    public void setPi(Set<String> pi) {
        this.pi = pi;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Set<String> getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(Set<String> experimentName) {
        this.experimentName = experimentName;
    }

    public Set<String> getStudyName() {
        return studyName;
    }

    public void setStudyName(Set<String> studyName) {
        this.studyName = studyName;
    }

    public Set<String> getEditorSymbol() {
        return editorSymbol;
    }

    public void setEditorSymbol(Set<String> editorSymbol) {
        this.editorSymbol = editorSymbol;
    }



    public Set<String> getDeliverySystemName() {
        return deliverySystemName;
    }

    public void setDeliverySystemName(Set<String> deliverySystemName) {
        this.deliverySystemName = deliverySystemName;
    }

    public Set<String> getEditorVariant() {
        return editorVariant;
    }

    public void setEditorVariant(Set<String> editorVariant) {
        this.editorVariant = editorVariant;
    }

    public Set<String> getEditorPamPreference() {
        return editorPamPreference;
    }

    public void setEditorPamPreference(Set<String> editorPamPreference) {
        this.editorPamPreference = editorPamPreference;
    }

    public Set<String> getSubstrateTarget() {
        return substrateTarget;
    }

    public void setSubstrateTarget(Set<String> substrateTarget) {
        this.substrateTarget = substrateTarget;
    }

    public Set<String> getActivity() {
        return activity;
    }

    public void setActivity(Set<String> activity) {
        this.activity = activity;
    }

    public Set<String> getFusion() {
        return fusion;
    }

    public void setFusion(Set<String> fusion) {
        this.fusion = fusion;
    }

    public Set<String> getDsbCleavageType() {
        return dsbCleavageType;
    }

    public void setDsbCleavageType(Set<String> dsbCleavageType) {
        this.dsbCleavageType = dsbCleavageType;
    }

    public Set<String> getEditorTargetSequence() {
        return editorTargetSequence;
    }

    public void setEditorTargetSequence(Set<String> editorTargetSequence) {
        this.editorTargetSequence = editorTargetSequence;
    }

    public Set<String> getProteinSequence() {
        return proteinSequence;
    }

    public void setProteinSequence(Set<String> proteinSequence) {
        this.proteinSequence = proteinSequence;
    }
    public Set<String> getGrnaLabId() {
        return grnaLabId;
    }

    public void setGrnaLabId(Set<String> grnaLabId) {
        this.grnaLabId = grnaLabId;
    }



    public Set<String> getGuide() {
        return guide;
    }

    public void setGuide(Set<String> guide) {
        this.guide = guide;
    }





    public Set<String> getGuideFormat() {
        return guideFormat;
    }

    public void setGuideFormat(Set<String> guideFormat) {
        this.guideFormat = guideFormat;
    }

    public Set<String> getModifications() {
        return modifications;
    }

    public void setModifications(Set<String> modifications) {
        this.modifications = modifications;
    }

    public Set<String> getStandardScaffoldSequence() {
        return standardScaffoldSequence;
    }

    public void setStandardScaffoldSequence(Set<String> standardScaffoldSequence) {
        this.standardScaffoldSequence = standardScaffoldSequence;
    }

    public Set<String> getIvtConstructSource() {
        return ivtConstructSource;
    }

    public void setIvtConstructSource(Set<String> ivtConstructSource) {
        this.ivtConstructSource = ivtConstructSource;
    }

    public Set<String> getVectorName() {
        return vectorName;
    }

    public void setVectorName(Set<String> vectorName) {
        this.vectorName = vectorName;
    }

    public Set<String> getVectorSubtype() {
        return vectorSubtype;
    }

    public void setVectorSubtype(Set<String> vectorSubtype) {
        this.vectorSubtype = vectorSubtype;
    }

    public Set<String> getVectorSource() {
        return vectorSource;
    }

    public void setVectorSource(Set<String> vectorSource) {
        this.vectorSource = vectorSource;
    }

    public Set<String> getVectorlabId() {
        return vectorlabId;
    }

    public void setVectorlabId(Set<String> vectorlabId) {
        this.vectorlabId = vectorlabId;
    }

    public Set<String> getVectorAnnotatedMap() {
        return vectorAnnotatedMap;
    }

    public void setVectorAnnotatedMap(Set<String> vectorAnnotatedMap) {
        this.vectorAnnotatedMap = vectorAnnotatedMap;
    }

    public Set<String> getVectorType() {
        return vectorType;
    }

    public void setVectorType(Set<String> vectorType) {
        this.vectorType = vectorType;
    }

    public Set<String> getSpecificityRatio() {
        return specificityRatio;
    }

    public void setSpecificityRatio(Set<String> specificityRatio) {
        this.specificityRatio = specificityRatio;
    }

    public Set<String> getModelType() {
        return modelType;
    }

    public void setModelType(Set<String> modelType) {
        this.modelType = modelType;
    }

    public Set<String> getModelOrganism() {
        return modelOrganism;
    }

    public void setModelOrganism(Set<String> modelOrganism) {
        this.modelOrganism = modelOrganism;
    }

    public Set<String> getModelRrid() {
        return modelRrid;
    }

    public void setModelRrid(Set<String> modelRrid) {
        this.modelRrid = modelRrid;
    }

    public Set<String> getModelSource() {
        return modelSource;
    }

    public void setModelSource(Set<String> modelSource) {
        this.modelSource = modelSource;
    }

    public Set<String> getModelSubtype() {
        return modelSubtype;
    }

    public void setModelSubtype(Set<String> modelSubtype) {
        this.modelSubtype = modelSubtype;
    }

    public Set<String> getModelAnnotatedMap() {
        return modelAnnotatedMap;
    }

    public void setModelAnnotatedMap(Set<String> modelAnnotatedMap) {
        this.modelAnnotatedMap = modelAnnotatedMap;
    }

    public Set<String> getModelName() {
        return modelName;
    }

    public void setModelName(Set<String> modelName) {
        this.modelName = modelName;
    }

    public Set<String> getTransgene() {
        return transgene;
    }

    public void setTransgene(Set<String> transgene) {
        this.transgene = transgene;
    }

    public Set<String> getTransgeneDescription() {
        return transgeneDescription;
    }

    public void setTransgeneDescription(Set<String> transgeneDescription) {
        this.transgeneDescription = transgeneDescription;
    }

    public Set<String> getTransgeneReporter() {
        return transgeneReporter;
    }

    public void setTransgeneReporter(Set<String> transgeneReporter) {
        this.transgeneReporter = transgeneReporter;
    }

    public Set<String> getParentalOrigin() {
        return parentalOrigin;
    }

    public void setParentalOrigin(Set<String> parentalOrigin) {
        this.parentalOrigin = parentalOrigin;
    }

    public Set<String> getStrainCode() {
        return strainCode;
    }

    public void setStrainCode(Set<String> strainCode) {
        this.strainCode = strainCode;
    }

    public Set<String> getStrainAlias() {
        return strainAlias;
    }

    public void setStrainAlias(Set<String> strainAlias) {
        this.strainAlias = strainAlias;
    }

    public Set<String> getGenomeSerotype() {
        return genomeSerotype;
    }

    public void setGenomeSerotype(Set<String> genomeSerotype) {
        this.genomeSerotype = genomeSerotype;
    }

    public Set<String> getCapsidSerotype() {
        return capsidSerotype;
    }

    public void setCapsidSerotype(Set<String> capsidSerotype) {
        this.capsidSerotype = capsidSerotype;
    }

    public Set<String> getCapsidVariant() {
        return capsidVariant;
    }

    public void setCapsidVariant(Set<String> capsidVariant) {
        this.capsidVariant = capsidVariant;
    }

    public Set<String> getTiterMethod() {
        return titerMethod;
    }

    public void setTiterMethod(Set<String> titerMethod) {
        this.titerMethod = titerMethod;
    }

    public Set<String> getMolTargetingAgent() {
        return molTargetingAgent;
    }

    public void setMolTargetingAgent(Set<String> molTargetingAgent) {
        this.molTargetingAgent = molTargetingAgent;
    }

    public Set<String> getEditorType() {
        return editorType;
    }

    public void setEditorType(Set<String> editorType) {
        this.editorType = editorType;
    }

    public Set<String> getEditorSubType() {
        return editorSubType;
    }

    public void setEditorSubType(Set<String> editorSubType) {
        this.editorSubType = editorSubType;
    }

    public Set<String> getEditorAlias() {
        return editorAlias;
    }

    public void setEditorAlias(Set<String> editorAlias) {
        this.editorAlias = editorAlias;
    }

    public Set<String> getEditorSpecies() {
        return editorSpecies;
    }

    public void setEditorSpecies(Set<String> editorSpecies) {
        this.editorSpecies = editorSpecies;
    }

    public Set<String> getEditorAssembly() {
        return editorAssembly;
    }

    public void setEditorAssembly(Set<String> editorAssembly) {
        this.editorAssembly = editorAssembly;
    }

    public Set<String> getEditorChr() {
        return editorChr;
    }

    public void setEditorChr(Set<String> editorChr) {
        this.editorChr = editorChr;
    }

    public Set<String> getEditorStart() {
        return editorStart;
    }

    public void setEditorStart(Set<String> editorStart) {
        this.editorStart = editorStart;
    }

    public Set<String> getEditorStop() {
        return editorStop;
    }

    public void setEditorStop(Set<String> editorStop) {
        this.editorStop = editorStop;
    }

    public Set<String> getEditorStrand() {
        return editorStrand;
    }

    public void setEditorStrand(Set<String> editorStrand) {
        this.editorStrand = editorStrand;
    }

    public Set<String> getEditorLocation() {
        return editorLocation;
    }

    public void setEditorLocation(Set<String> editorLocation) {
        this.editorLocation = editorLocation;
    }

    public Set<String> getEditorAnnotatedMap() {
        return editorAnnotatedMap;
    }

    public void setEditorAnnotatedMap(Set<String> editorAnnotatedMap) {
        this.editorAnnotatedMap = editorAnnotatedMap;
    }

    public Set<String> getGuideLocation() {
        return guideLocation;
    }

    public void setGuideLocation(Set<String> guideLocation) {
        this.guideLocation = guideLocation;
    }

    public Set<String> getEditorSource() {
        return editorSource;
    }

    public void setEditorSource(Set<String> editorSource) {
        this.editorSource = editorSource;
    }

    public Set<String> getGuideTargetLocus() {
        return guideTargetLocus;
    }

    public void setGuideTargetLocus(Set<String> guideTargetLocus) {
        this.guideTargetLocus = guideTargetLocus;
    }

    public Set<String> getGuideSpecies() {
        return guideSpecies;
    }

    public void setGuideSpecies(Set<String> guideSpecies) {
        this.guideSpecies = guideSpecies;
    }

    public Set<String> getGuideTargetSequence() {
        return guideTargetSequence;
    }

    public void setGuideTargetSequence(Set<String> guideTargetSequence) {
        this.guideTargetSequence = guideTargetSequence;
    }

    public Set<String> getGuidePam() {
        return guidePam;
    }

    public void setGuidePam(Set<String> guidePam) {
        this.guidePam = guidePam;
    }

    public Set<String> getGuideSpacerSequence() {
        return guideSpacerSequence;
    }

    public void setGuideSpacerSequence(Set<String> guideSpacerSequence) {
        this.guideSpacerSequence = guideSpacerSequence;
    }

    public Set<String> getGuideRepeatSequence() {
        return guideRepeatSequence;
    }

    public void setGuideRepeatSequence(Set<String> guideRepeatSequence) {
        this.guideRepeatSequence = guideRepeatSequence;
    }

    public Set<String> getGuideForwardPrimer() {
        return guideForwardPrimer;
    }

    public void setGuideForwardPrimer(Set<String> guideForwardPrimer) {
        this.guideForwardPrimer = guideForwardPrimer;
    }

    public Set<String> getGuideReversePrimer() {
        return guideReversePrimer;
    }

    public void setGuideReversePrimer(Set<String> guideReversePrimer) {
        this.guideReversePrimer = guideReversePrimer;
    }

    public Set<String> getGuideLinkerSequence() {
        return guideLinkerSequence;
    }

    public void setGuideLinkerSequence(Set<String> guideLinkerSequence) {
        this.guideLinkerSequence = guideLinkerSequence;
    }

    public Set<String> getGuideAntiRepeatSequence() {
        return guideAntiRepeatSequence;
    }

    public void setGuideAntiRepeatSequence(Set<String> guideAntiRepeatSequence) {
        this.guideAntiRepeatSequence = guideAntiRepeatSequence;
    }

    public Set<String> getGuideStemloopSequence() {
        return guideStemloopSequence;
    }

    public void setGuideStemloopSequence(Set<String> guideStemloopSequence) {
        this.guideStemloopSequence = guideStemloopSequence;
    }

    public Set<String> getEditorTargetLocus() {
        return editorTargetLocus;
    }

    public void setEditorTargetLocus(Set<String> editorTargetLocus) {
        this.editorTargetLocus = editorTargetLocus;
    }

    public Set<String> getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Set<String> deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Set<String> getDeliverySubtype() {
        return deliverySubtype;
    }

    public void setDeliverySubtype(Set<String> deliverySubtype) {
        this.deliverySubtype = deliverySubtype;
    }

    public Set<String> getDeliveryAnnotatedMap() {
        return deliveryAnnotatedMap;
    }

    public void setDeliveryAnnotatedMap(Set<String> deliveryAnnotatedMap) {
        this.deliveryAnnotatedMap = deliveryAnnotatedMap;
    }

    public Set<String> getDeliverySource() {
        return deliverySource;
    }

    public void setDeliverySource(Set<String> deliverySource) {
        this.deliverySource = deliverySource;
    }

    public Set<String> getDeliveryLabId() {
        return deliveryLabId;
    }

    public void setDeliveryLabId(Set<String> deliveryLabId) {
        this.deliveryLabId = deliveryLabId;
    }

    public Set<String> getGuideAnnotatedMap() {
        return guideAnnotatedMap;
    }

    public void setGuideAnnotatedMap(Set<String> guideAnnotatedMap) {
        this.guideAnnotatedMap = guideAnnotatedMap;
    }

    public Set<String> getTissueIds() {
        return tissueIds;
    }

    public void setTissueIds(Set<String> tissueIds) {
        this.tissueIds = tissueIds;
    }

    public Set<String> getExperimentType() {
        return experimentType;
    }

    public void setExperimentType(Set<String> experimentType) {
        this.experimentType = experimentType;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getGuideSource() {
        return guideSource;
    }

    public void setGuideSource(Set<String> guideSource) {
        this.guideSource = guideSource;
    }
}
