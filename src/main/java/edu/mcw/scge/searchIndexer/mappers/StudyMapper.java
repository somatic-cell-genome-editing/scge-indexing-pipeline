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
                  if (indexDocument.getAccessLevel().equalsIgnoreCase("consortium")
                          || (indexDocument.getAccessLevel().equalsIgnoreCase("public") && study.getTier() == 4)) {
                      studies.add(study.getStudy());
                      Grant grant = grantDao.getGrantByGroupId(study.getGroupId());
                      grantInitiatives.add(UI.getLabel(grant.getGrantInitiative()));
                      studyMap.put(study.getStudyId(), study.getStudy());
                      if (study.getPiFirstName() != null && study.getPiLastName() != null)
                          pi.add(study.getPiLastName() + " " + study.getPiFirstName());
                      else if (study.getPi() != null)
                          pi.add(study.getPi().replaceAll(",", " "));
                      else System.err.println("NO PI NAME:" + study.getStudyId());

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
                          System.err.println("STUDY ID with no Modified By:" + study.getStudyId());
                      }
                      //  o.setSubmissionDate(study.getSubmissionDate().toString());
                      //   }
                  }
              }
            }catch (Exception e){
                System.out.println("EXPERIMENT ID:"+experimentId);
                e.printStackTrace();
            }

            Experiment experiment=experimentDao.getExperiment(experimentId);
            experimentName.add(experiment.getName());
            experimentType.add(experiment.getType());
            experimentMap.put(experiment.getExperimentId(), experiment.getName());
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
        if(indexDocument.getCategory().equalsIgnoreCase("Study") || indexDocument.getCategory().equalsIgnoreCase("Experiment"))
        {
            indexDocument.setExperimentType(experimentType);
            indexDocument.setPi(pi);
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
