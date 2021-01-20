package edu.mcw.scge.indexer.model;

import com.google.gson.Gson;

import java.util.List;

public class IndexObject {

    private int id;
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
    private List<String> experimentTags;


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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getExperimentTags() {
        return experimentTags;
    }

    public void setExperimentTags(List<String> experimentTags) {
        this.experimentTags = experimentTags;
    }

    public String toString(){
        StringBuffer sb=new StringBuffer();
        Gson gson=new Gson();
        boolean first=true;
        sb.append("{");

        if(id!=0) {
            sb.append("\"id\":" + id);
            first=false;
        }
        if(name!=null) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"name\":\"" + name+"\"" );
        }
        if(type!=null) {
            if(!first){
                sb.append(",");
            }else
            first=false;
            sb.append("\"type\":\"" + type+"\"" );
        }
        if(subType!=null) {
            if (!first) {
                sb.append(",");
            }else
                    first=false;
            sb.append("\"subType\":\"" + subType + "\"");
        }
        if(species!=null) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"species\":\"" + species + "\"");
        }
        if(symbol!=null) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"symbol\":\"" + symbol + "\"");
        }
    /*    if(description!=null && !description.equals("")) {
            if(!first){
                sb.append(",");
            }else
                first=false;

            sb.append("\"description\":\"" + description + "\"");
        }*/
        if(pam!=null) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"pam\":\"" + pam + "\"");
        }
        if(detectionMethod!=null) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"detectionMethod\":\"" + detectionMethod + "\"");
        }
        if(site!=null) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"site\":\"" + site + "\"");
        }
        if(category!=null) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"category\":\"" + category + "\"");
        }
        if(sequence!=null) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"sequence\":\"" + sequence + "\"");
        }
        if(target!=null && target.size()>0) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"target\":" + gson.toJson(target) );
        }
        if(externalId!=null && externalId.size()>0) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"externalId\":" + gson.toJson(externalId) );
        }
        if(experimentTags!=null && experimentTags.size()>0) {
            if(!first){
                sb.append(",");
            }else
                first=false;
            sb.append("\"experimentalTags\":" + gson.toJson(experimentTags));
        }
        sb.append("}");
            return sb.toString();
    }
}
