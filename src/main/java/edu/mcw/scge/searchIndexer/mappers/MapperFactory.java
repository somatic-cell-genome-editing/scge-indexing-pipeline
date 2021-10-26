package edu.mcw.scge.searchIndexer.mappers;

public class MapperFactory  {
    public static Mapper getMapper(String type){
        if(type.equalsIgnoreCase("experiment")){
            return new ExperimentMapper();
        }
        if(type.equalsIgnoreCase("editor")){
            return new EditorMapper();
        }
        if(type.equalsIgnoreCase("tissue")){
            return new TissueMapper();
        }
        if(type.equalsIgnoreCase("guide")){
            return new GuideMapper();
        }
        if(type.equalsIgnoreCase("model")){
            return new ModelMapper();
        }
        if(type.equalsIgnoreCase("vector")){
            return new VectorMapper();
        }
        if(type.equalsIgnoreCase("delivery")){
            return new DeliveryMapper();
        }
        if(type.equalsIgnoreCase("application")){
            return new ApplicationMapper();
        }
        if(type.equalsIgnoreCase("study")){
            return new StudyMapper();
        }

        return null;
    }
}
