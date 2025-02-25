package edu.mcw.scge.toolkit.searchIndexer.indexers;

import edu.mcw.scge.toolkit.indexer.model.Category;

public class IndexerFactory {
    public static ObjectIndexer getIndexer(Category category){
        switch (category){
            case PROJECT:
                return new GrantIndexer();
            case EXPERIMENT:
                return new ExperimentIndexer();
            case GUIDE:
                return new GuideIndexer();
            case MODEL:
                return new ModelIndexer();
            case EDITOR:
                return new EditorIndexer();
            case VECTOR:
                return new VectorIndexer();
            case ANTIBODY:
                return new AntibodyIndexer();
            case DELIVERY:
                return new DeliveryIndexer();
            case PROTOCOL:
                return new ProtocolIndexer();
            case PUBLICATION:
                return new PublicationIndexer();
            default:
        }
        return null;
    }
}
