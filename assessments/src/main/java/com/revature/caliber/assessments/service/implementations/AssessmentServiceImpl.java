package com.revature.caliber.assessments.service.implementations;

import com.revature.caliber.assessments.beans.Assessment;
import com.revature.caliber.assessments.data.Facade;
import com.revature.caliber.assessments.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service(value = "assessmentService")
public class AssessmentServiceImpl implements AssessmentService {

    private Facade facade;
    //    Spring setter based DI
    @Autowired
    public void setFacade(Facade facade) {
        this.facade = facade;
    }

    @Override
    public HashSet<Assessment> getAll() {
        return facade.getAllAssessments();
    }

    @Override
    public Assessment getById(int id) {
        return facade.getById(id);
    }

    @Override
    public HashSet<Assessment> getByTrainerId(int id) {
        return facade.getAssessmentsByTrainerId(id);
    }

    @Override
    public HashSet<Assessment> getByWeekId(int id) {
        return facade.getAssessmentsByWeekId(id);
    }

    @Override
    public HashSet<Assessment> getByBatchId(int id) {
        return facade.getAssessmentsByBatchId(id);
    }

    @Override
    public void insert(Assessment assessment) {
        facade.insertAssessment(assessment);
    }

    @Override
    public void update(Assessment assessment) {
        facade.updateAssessment(assessment);
    }

    @Override
    public void delete(Assessment assessment) {
        facade.deleteAssessment(assessment);
    }
}
