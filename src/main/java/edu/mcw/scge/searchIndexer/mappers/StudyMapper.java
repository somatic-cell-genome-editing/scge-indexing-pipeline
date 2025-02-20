package edu.mcw.scge.searchIndexer.mappers;

import edu.mcw.scge.dao.implementation.*;
import edu.mcw.scge.datamodel.*;
import edu.mcw.scge.process.UI;
import edu.mcw.scge.searchIndexer.model.IndexDocument;

import java.util.*;
import java.util.stream.Collectors;

public class StudyMapper implements Mapper {
    StudyDao studyDao=new StudyDao();
    ExperimentDao experimentDao=new ExperimentDao();
    GrantDao grantDao=new GrantDao();
    PersonDao personDao=new PersonDao();
    @Override
    public void mapFields(List<ExperimentRecord> experimentRecords, IndexDocument indexDocument) throws Exception {
        Set<String> studies=new HashSet<>();
        Set<String> status=new HashSet<>();
        Set<String> access=new HashSet<>();
        Set<String> studyType=new HashSet<>();
        Set<String> pi=new HashSet<>();
        Set<String> experimentName=new HashSet<>();
        Set<String> experimentType=new HashSet<>();
        Map<Integer, String> studyMap=new HashMap<>();
        Map<Long, String> experimentMap=new HashMap<>();
        List<Date> lastModifiedDate=new ArrayList<>();

        Set<String> grantInitiatives= new HashSet<>();
        Set<Long> experimentIds=experimentRecords.stream()
                .map(ExperimentRecord::getExperimentId).collect(Collectors.toSet());
        Set<Integer> studyIds=new HashSet<>();
        for(Long experimentId:experimentIds){
            Study study=new Study();
            try {
              List<Study> studyList=  studyDao.getStudyByExperimentId(experimentId);

              if(studyList.size()>0) {
                  study = studyList.get(0);
                  if(study.getTier()<indexDocument.getTier() && indexDocument.getCategory().equalsIgnoreCase("Publication")){
                      indexDocument.setTier(study.getTier());
                  }
                  if (indexDocument.getAccessLevel().equalsIgnoreCase("consortium")
                          || (indexDocument.getAccessLevel().equalsIgnoreCase("public") && study.getTier() == 4)) {
                      studies.add(study.getStudy());
                      Grant grant = grantDao.getGrantByGroupId(study.getGroupId());
                      if(grant.getGrantInitiative().equalsIgnoreCase("Collaborative Opportunity Fund")) {
                          List<String> cofProjectInitiatives =grantDao.getCOFProjectInitiatives(grant.getGrantId());
                          if(!cofProjectInitiatives.isEmpty()) {
                              for(String cofInitiative:cofProjectInitiatives) {
                                  grantInitiatives.add(UI.correctInitiative(cofInitiative));
                              }
                          }
                      }
                      grantInitiatives.add(UI.correctInitiative(grant.getGrantInitiative()));
                      studyMap.put(study.getStudyId(), study.getStudy());

                      Set<String> pis=new HashSet<>();
                      for(Person p:study.getMultiplePis()){
                          if(p.getFirstName()!=null && !p.getFirstName().equals(""))
                              pis.add(p.getLastName()+" "+ p.getFirstName());
                          else
                              pis.add(p.getName());
                      }
                      pi.addAll(pis);
                     /* if (study.getPiFirstName() != null && study.getPiLastName() != null)
                          pi.add(study.getPiLastName() + " " + study.getPiFirstName());
                      else if (study.getPi() != null)
                          pi.add(study.getPi().replaceAll(",", " "));
                      else System.err.println("NO PI NAME:" + study.getStudyId());*/

                      //    if(!studyIds.contains(study.getStudyId())){
                      //        studyIds.add(study.getStudyId());
                      List<Experiment> experiments = experimentDao.getExperimentsByStudy(study.getStudyId());
                      if (experiments.size() > 0) {
                          status.add("Processed");
                      } else {
                          status.add("Received");
                      }

                      if (study.getIsValidationStudy() == 1) {
                          studyType.add("Validation");
                      } else {
                          studyType.add("Experimental");
                      }

                      if (study.getTier() == 4) {
                          access.add("Public");
                      } else {
                          access.add("Restricted");
                      }

                      try {
                          if (!isInDCCorNIHGroup(personDao.getPersonById(study.getModifiedBy()).get(0))) {
                              status.add("Verified");
                          }
                      } catch (Exception e) {
                        //  System.err.println("STUDY ID with no Modified By:" + study.getStudyId());
                      }
                      //  o.setSubmissionDate(study.getSubmissionDate().toString());
                      //   }
                      Experiment experiment=experimentDao.getExperiment(experimentId);
                      experimentName.add(experiment.getName());
                      experimentType.add(experiment.getType());
                      experimentMap.put(experiment.getExperimentId(), experiment.getName());
                      if(experiment.getLastModifiedDate()!=null)
                      lastModifiedDate.add(experiment.getLastModifiedDate());
                  }
              }
            }catch (Exception e){
                System.out.println("EXPERIMENT ID:"+experimentId);
                e.printStackTrace();
            }


        }
      //  if(indexDocument.getCategory().equalsIgnoreCase("Study"))
        indexDocument.setInitiative(grantInitiatives);
        if(indexDocument.getCategory().equalsIgnoreCase("Experiment")) {
          //  indexDocument.setAccess(String.join(",", access));
            indexDocument.setStudyType(String.join(",", studyType));
          //  indexDocument.setStatus(String.join(",", status));
        }
        indexDocument.setStudyNames(studyMap);
        if(!indexDocument.getCategory().equalsIgnoreCase("Experiment"))
        indexDocument.setExperimentNames(experimentMap);
        indexDocument.setStudy(studies);

        indexDocument.setExperimentName(experimentName);
        if(indexDocument.getCategory().equalsIgnoreCase("Project") || indexDocument.getCategory().equalsIgnoreCase("Experiment")
                || indexDocument.getCategory().equalsIgnoreCase("Protocol"))
        {
            indexDocument.setExperimentType(experimentType);
            indexDocument.setPi(pi);
        }

        if(lastModifiedDate.size()>0) {
            // System.out.println("MAX DATE:"+ Collections.max(lastModifiedDate));
        }
    }
    public boolean isInDCCorNIHGroup(Person p) throws Exception{

        GroupDAO gdao=new GroupDAO();
        List<Integer> DCCNIHGroupsIds=gdao.getDCCNIHGroupIds();
        PersonDao pdao = new PersonDao();
        List<PersonInfo> personInfoRecords = pdao.getPersonInfo(p.getId());

        for(PersonInfo i:personInfoRecords) {
            if (DCCNIHGroupsIds.contains(i.getGroupId())) {
                return true;
            }
        }
        return false;
    }
}
