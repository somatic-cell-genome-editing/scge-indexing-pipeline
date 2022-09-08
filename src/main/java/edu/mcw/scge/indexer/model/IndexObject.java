package edu.mcw.scge.indexer.model;

import com.google.gson.Gson;
import edu.mcw.scge.datamodel.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndexObject {

    private long id;
    private String name;
    private String type;
    private String subType;
    private String species;
    private String symbol;
    private List<String> aliases;
    private String pam;
    private String description;
    private String category;
    private String detectionMethod;
    private String site;
    private String sequence;
    private List<String> target;
    private List<String> externalId; //stocknumber etc.
    private List<String> additionalData;
    private List<String> experimentTags;
    private String experimentName;
    private int tier;
    private String reportPageLink;

    private Study study;
    private List<Editor> editors;
    private List<Delivery> deliveries;
    private List<Model> models;
    private List<Guide> guides;
    private List<Experiment> experiments;
    private List<Vector> vectors;

    private int experimentCount;
    private String withExperiments;
   private Map<Integer, String> studyNames;
   private Map<Long, String> experimentNames;

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

    public String getWithExperiments() {
        return withExperiments;
    }

    public void setWithExperiments(String withExperiments) {
        this.withExperiments = withExperiments;
    }

    public int getExperimentCount() {
        return experimentCount;
    }

    public void setExperimentCount(int experimentCount) {
        this.experimentCount = experimentCount;
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public void setExperiments(List<Experiment> experiments) {
        this.experiments = experiments;
    }

    public String getReportPageLink() {
        return reportPageLink;
    }

    public void setReportPageLink(String reportPageLink) {
        this.reportPageLink = reportPageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
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
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getPam() {
        return pam;
    }

    public void setPam(String pam) {
        this.pam = pam;
    }

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

    public String getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(String detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public List<String> getExternalId() {
        return externalId;
    }

    public void setExternalId(List<String> externalId) {
        this.externalId = externalId;
    }

    public List<String> getTarget() {
        return target;
    }

    public void setTarget(List<String> target) {
        this.target = target;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(List<String> additionalData) {
        this.additionalData = additionalData;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public List<String> getExperimentTags() {
        return experimentTags;
    }

    public void setExperimentTags(List<String> experimentTags) {
        this.experimentTags = experimentTags;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public List<Editor> getEditors() {
        return editors;
    }

    public void setEditors(List<Editor> editors) {
        this.editors = editors;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public List<Guide> getGuides() {
        return guides;
    }

    public void setGuides(List<Guide> guides) {
        this.guides = guides;
    }

    public List<Vector> getVectors() {
        return vectors;
    }

    public void setVectors(List<Vector> vectors) {
        this.vectors = vectors;
    }


}
