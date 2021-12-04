package org.example.test;

import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.example.drools.entity.Student;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * lhs/rhs高级语法
 */
public class DroolsTest2 {
    /**
     * lhs:
     * in/not in/eval/not/exists/extends
     * (1)in/not in:Object(field in (比较值1,比较值2...))
     * (2)eval:eval(表达式) 表达式内返回boolean,eval()返回表达式内的结果
     * (3)not:not Object(可选属性约束) not用于判断Working Memory中是否存在某个Fact对象
     * (4)exists:exists Object(可选属性约束) 用于判断Working Memory中是否存在某个Fact对象
     * 当向Working Memory中加入多个满足条件的Fact对象时
     * 使用了exists的规则执行一次
     * 不使用exists的规则会执行多次
     * (5)extends 规则之间可以使用extends关键字进行规则条件部分的继承
     * 类似于java类之间的继承
     * 汁即成LHS,继承的与自己特殊的规则是and关系
     */
    @Test
    public void test1() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession();
        Student student = new Student();
        student.setName("张三");
        Student student1 = new Student();
        student1.setName("哈哈");
        kieSession.insert(student);
        kieSession.insert(student1);
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("lhs_"));
        kieSession.dispose();
    }

    /**
     * rhs:
     * halt/getWorkingMemory/getRule
     * (1)halt:立即终止后面所有规则的执行
     * (2)getWorkingMemory:返回工作内存对象
     * (3)getRule:返回规则对象
     */
    @Test
    public void test2() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession();
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rhs_"));
        kieSession.dispose();
    }
}
