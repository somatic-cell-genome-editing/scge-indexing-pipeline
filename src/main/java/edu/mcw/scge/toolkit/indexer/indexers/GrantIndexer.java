package edu.mcw.scge.toolkit.indexer.indexers;

import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.toolkit.indexer.index.ObjectDetails;
import edu.mcw.scge.toolkit.indexer.index.GrantDetails;


import java.util.*;

public class GrantIndexer extends Indexer<Grant> implements ObjectIndexer {
    @Override
    List<Grant> getObjects() throws Exception {
        List<Integer> submittedGrantIds=studyDao.getAllSubmittedGrantIds();
        List<Grant> grants=new ArrayList<>();
        for(int id:submittedGrantIds) {
            Grant grant = grantDao.getGrantByGroupId(id);
            grants.add(grant);
        }
        return grants;
    }

    @Override
    public void getIndexObjects() throws Exception {
         getGrants();
    }
    public void getGrants() throws  Exception{
        for(Grant grant:getObjects()) {
            ObjectDetails<Grant> objectDetails=new GrantDetails(grant);
            indexObjects(objectDetails);
        }
    }
}
