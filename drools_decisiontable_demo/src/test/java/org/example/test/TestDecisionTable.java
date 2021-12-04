package org.example.test;

import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.example.drools.entity.PersonInfoEntity;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestDecisionTable {

    @Test
    public void test1() throws Exception {
        String realPath = "testRule.xls";//指定决策表xls文件的磁盘路径
        Path path = Paths.get(realPath);
        InputStream is = new FileInputStream(path.toFile());
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        String drl = compiler.compile(is, InputType.XLS);

        System.out.println(drl);
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);
        KieSession session = kieHelper.build().newKieSession();

        PersonInfoEntity personInfoEntity = new PersonInfoEntity();
        personInfoEntity.setSex("男");
        personInfoEntity.setAge(35);
        personInfoEntity.setSalary(1000);

        List<String> list = new ArrayList<>();
        session.setGlobal("listRules", list);

        session.insert(personInfoEntity);

        session.getAgenda().getAgendaGroup("sign").setFocus();

        session.fireAllRules();

        for (String s : list) {
            System.out.println(s);
        }
        session.dispose();
    }
}
