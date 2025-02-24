package edu.mcw.scge.toolkit.indexer.model;

public enum Category {
    EXPERIMENT("18"),
    EDITOR("11"),
    DELIVERY("12"),
    VECTOR("14"),
    GUIDE("10"),
    MODEL("13"),
    PROTOCOL("21"),
    ANTIBODY(""),
    PROJECT("");
//    PUBLICATION
    private final String objectCode;
     Category(String objectCode){
        this.objectCode=objectCode;

}
public String getObjectCode(){
         return objectCode;
}
}
