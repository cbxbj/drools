package org.example.test;

import org.example.drools.entity.Student;
import org.example.drools.service.UserService;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Drools高级语法
 */
public class DroolsTest1 {

    /**
     * global
     * 包装类一个规则改变值,只对该规则生效
     * javaBean和集合要想让其余规则生效,那么修改的规则优先级要高(salience)
     */
    @Test
    public void test1() {
        KieServices kieServices = KieServices.Factory.get();
        //之前写法
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        //现在写法
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession();

        //设置全局变量，名称和类型必须和规则文件中定义的全局变量名称对应
        kieSession.setGlobal("userService", new UserService());
        kieSession.setGlobal("count", 5);
        List<String> list = new ArrayList<>();//size为0
        kieSession.setGlobal("gList", list);

        kieSession.fireAllRules();
        kieSession.dispose();

        //因为在规则中为全局变量添加了两个元素，所以现在的size为2
        System.out.println(list.size());
    }

    /**
     * query查询提供了一种查询working memory中符合约束条件的Fact对象的简单方法
     * <pre>
     * query 查询的名称(可选参数)
     *     LHS
     * end
     * </pre>
     */
    @Test
    public void tes2() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession();

        Student student1 = new Student();
        student1.setName("张三");
        student1.setAge(12);

        Student student2 = new Student();
        student2.setName("李四");
        student2.setAge(8);

        Student student3 = new Student();
        student3.setName("王五");
        student3.setAge(22);

        //将对象插入Working Memory中
        kieSession.insert(student1);
        kieSession.insert(student2);
        kieSession.insert(student3);

        //调用规则文件中的查询
        QueryResults results1 = kieSession.getQueryResults("query_1");
        int size = results1.size();
        System.out.println("size=" + size);
        for (QueryResultsRow row : results1) {
            Student student = (Student) row.get("$student");
            System.out.println(student);
        }

        //调用规则文件中的查询
        QueryResults results2 = kieSession.getQueryResults("query_2", "王五");
        size = results2.size();
        System.out.println("size=" + size);
        for (QueryResultsRow row : results2) {
            Student student = (Student) row.get("$student");
            System.out.println(student);
        }
        //kieSession.fireAllRules();
        kieSession.dispose();
    }

    /**
     * function关键字用于在规则文件中定义函数
     * 可以在规则体中调用定义的函数
     * 使用函数的好处是可以将业务逻辑集中放置在一个地方,根据需要可以对函数进行修改
     */
    @Test
    public void tes3() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession();

        Student student = new Student();
        student.setName("小明");

        kieSession.insert(student);

        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
