package edu.mcw.scge.searchIndexer.indexers;

public class Indexers {
    public Indexer getIndexer(String category){
        if(category.equalsIgnoreCase("experiment")){
            return new ExperimentIndexer();
        }
        if(category.equalsIgnoreCase("guide")){
            return new GuideIndexer();
        }
        if(category.equalsIgnoreCase("model")){
            return new ModelIndexer();
        }
        if(category.equalsIgnoreCase("vector")){
            return new VectorIndexer();
        }
        if(category.equalsIgnoreCase("editor")){
            return new EditorIndexer();
        }
        if(category.equalsIgnoreCase("study")){
            return new StudyIndexer();
        }
        if(category.equalsIgnoreCase("delivery")){
            return new DeliveryIndexer();
        }
        if(category.equalsIgnoreCase("studies")){
            return new StudyIndexer();
        }
        if(category.equalsIgnoreCase("protocol")){
            return new ProtocolIndexer();
        }
        if(category.equalsIgnoreCase("antibody")){
            return new AntibodyIndexer();
        }
        return null;
    }
}
