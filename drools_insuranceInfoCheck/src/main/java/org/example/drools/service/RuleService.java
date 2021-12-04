package org.example.drools.service;

import org.example.drools.entity.InsuranceInfo;
import org.example.drools.utils.KieSessionUtils;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService {
    public List<String> insuranceInfoCheck(InsuranceInfo insuranceInfo) throws Exception {
        KieSession session = KieSessionUtils.getKieSessionFromXLS("insuranceInfoCheck.xls");
        session.getAgenda().getAgendaGroup("sign").setFocus();

        session.insert(insuranceInfo);

        List<String> listRules = new ArrayList<>();
        session.setGlobal("listRules", listRules);

        session.fireAllRules();
        session.dispose();

        return listRules;
    }
}
