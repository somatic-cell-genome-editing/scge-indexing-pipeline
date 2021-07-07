package edu.mcw.scge.indexer.model;

import java.util.List;

public class DeliveryObject {
    private String organism;
    private List<String> animalModels;
    private String editor;
    private List<String> editTypes;
    private List<String> deliveryVehicles;
    private List<String> targetTissue;
    private List<String> routeOfAdministration;
    private List<String> allelesUsed;

    private String pi;

    public String getPi() {
        return pi;
    }

    public void setPi(String pi) {
        this.pi = pi;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public List<String> getAnimalModels() {
        return animalModels;
    }

    public void setAnimalModels(List<String> animalModels) {
        this.animalModels = animalModels;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public List<String> getEditTypes() {
        return editTypes;
    }

    public void setEditTypes(List<String> editTypes) {
        this.editTypes = editTypes;
    }

    public List<String> getDeliveryVehicles() {
        return deliveryVehicles;
    }

    public void setDeliveryVehicles(List<String> deliveryVehicles) {
        this.deliveryVehicles = deliveryVehicles;
    }

    public List<String> getTargetTissue() {
        return targetTissue;
    }

    public void setTargetTissue(List<String> targetTissue) {
        this.targetTissue = targetTissue;
    }

    public List<String> getRouteOfAdministration() {
        return routeOfAdministration;
    }

    public void setRouteOfAdministration(List<String> routeOfAdministration) {
        this.routeOfAdministration = routeOfAdministration;
    }

    public List<String> getAllelesUsed() {
        return allelesUsed;
    }

    public void setAllelesUsed(List<String> allelesUsed) {
        this.allelesUsed = allelesUsed;
    }
}
