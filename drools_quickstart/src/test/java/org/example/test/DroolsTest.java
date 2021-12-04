package org.example.test;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.example.drools.entity.ComparisonOperatorEntity;
import org.example.drools.entity.Order;
import org.example.drools.entity.Student;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则引擎构成:
 * <br/>
 * 1.Working Memory(工作内存)   存放Fact对象
 * <br/>
 * 2.Rule Base(规则库)         存放编写的业务规则 drl文件的规则
 * <br/>
 * 3.Inference Engine(推理引擎):
 * <pre>
 *     (1)Pattern Matcher(匹配器)      将规则库与工作内存中的Fact进行模式匹配
 *                                    LHS(左手边)为空则为true(pattern) RHS(右手边)
 *                                    pattern语法:绑定变量名:Object(Field约束),
 *                                    绑定变量名建议$开始,非必须,可用在对象上也可用在属性上
 *                                    多个pattern可用and或or,若不写则为and
 *     (2)Agenda(议程)                 匹配成功的规则
 *     (3)Execution Engine(执行引擎)    执行匹配成功的规则
 * </pre>
 * 比较操作符:
 * <pre>
 * (1)contains      检查一个Fact对象的某个属性值是否包含一个指定的对象值
 * (2)not contains  检查一个Fact对象的某个属性值是否不包含一个指定的对象值
 * (3)memberOf      判断一个Fact对象的某个属性是否在一个或多个集合中
 * (4)not memberOf  判断一个Fact对象的某个属性是否不在一个或多个集合中
 * (5)matches       判断一个Fact对象的属性是否与提供的标准的Java正则表达式进行匹配
 * (6)not matches   判断一个Fact对象的属性是否不与提供的标准的Java正则表达式进行匹配
 * </pre>
 */
public class DroolsTest {

    @Test
    public void test1() {
        KieServices kieServices = KieServices.Factory.get();
        //获取容器 读取kmodule.xml
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        //获取session kmodule.xml中配置的session
        KieSession kieSession = kieContainer.newKieSession();
        //Fact对象(事实对象)
        Order order = new Order();
        order.setOriginalPrice(300D);
        //将Fact对象(Order)插到工作内存中
        kieSession.insert(order);
        //激活规则,由Drools自动进行规则匹配,如果匹配成功则执行当前规则
        kieSession.fireAllRules();
        //关闭session
        kieSession.dispose();
        System.out.println("优惠后价格：" + order.getRealPrice());
    }

    //比较操作符
    @Test
    public void test2() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();
        ComparisonOperatorEntity coe = new ComparisonOperatorEntity();
        coe.setNames("张三1");
        List<String> list = new ArrayList<>();
        list.add("张三1");
        coe.setList(list);
        kieSession.insert(coe);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    //规则过滤器,多个规则都满足时执行指定规则
    @Test
    public void test3() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();
        ComparisonOperatorEntity coe = new ComparisonOperatorEntity();
        coe.setNames("张三1");
        List<String> list = new ArrayList<>();
        list.add("张三1");
        coe.setList(list);
        kieSession.insert(coe);
        //通常Agent中有多个规则(默认顺序执行)
        //只会执行指定的规则
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("rule_comparison_contains"));
        //执行以指定字符串开始的规则
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_"));
        kieSession.dispose();
    }

    //关键字
    @Test
    public void test4() {
        System.out.println("Drools的关键字分为:");
        System.out.println("硬关键字(Hard keywords):true、false、null");
        System.out.println("软关键字(Soft keywords)");
        System.out.println("硬关键字是我们在规则文件中定义包名或者规则名时明确不能使用的,否则程序会报错");
        System.out.println("软关键字虽然可以使用,但是不建议使用");
    }

    //测试内置方法
    //update、insert、retract
    @Test
    public void test5() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();
        Student student = new Student();
        //student.setAge(5);
        student.setAge(10);
        kieSession.insert(student);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    /**
     * 规则属性
     * (1)enabled:默认值true,设为false表示不启用该规则
     * (2)dialect属性用于指定当前规则使用的语言类型,取值为java和mvel，默认值为java
     * (3)salience属性用于指定规则的执行优先级,取值类型为Integer
     * 数值越大越优先执行,默认为0
     * 每个规则都有一个默认的执行顺序,如果不设置salience属性,规则体的执行顺序为由上到下
     * (4)no-loop属性用于防止死循环 (内置方法)
     * 取值类型为Boolean,默认值为false,设为true
     * (5)activation-group属性是指激活分组,取值为String类型
     * 具有相同分组名称的规则只能有一个规则被触发
     * 执行的是优先级最高的
     * (6)agenda-group属性为议程分组,属于另一种可控的规则执行方式
     * 用户可以通过设置agenda-group来控制规则的执行,只有获取焦点的组中的规则才会被触发
     * (7)auto-focus属性为自动获取焦点,取值类型为Boolean,默认值为false
     * 一般结合agenda-group属性使用,当一个议程分组未获取焦点时,可以设置auto-focus属性来控制
     * (8)timer属性可以通过定时器的方式指定规则执行的时间,使用方式有两种:
     * 方式一：timer (int: <initial delay> <repeat interval>?)
     * 此种方式遵循java.util.Timer对象的使用方式
     * 第一个参数表示几秒后执行,第二个参数表示每隔几秒执行一次,第二个参数为可选
     * timer(3s 2s)3秒后触发,每隔2s触发一次
     * 方式二：timer(cron: <cron expression>)
     * 此种方式使用标准的unix cron表达式的使用方式来定义规则执行的时间
     * timer (cron:0/1 * * * * ?) 每隔1秒触发一次
     * (9)date-effective属性用于指定规则的生效时间
     * 即只有当前系统时间大于等于设置的时间或者日期规则才有可能触发
     * 默认日期格式为:dd-MMM-yyyy
     * 用户也可以自定义日期格式
     * (10)date-expires属性用于指定规则的失效时间
     * 即只有当前系统时间小于设置的时间或者日期规则才有可能触发
     * 默认日期格式为:dd-MMM-yyyy
     * 用户也可以自定义日期格式
     */
    @Test//演示(6)agenda-group
    public void test6() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();
        Student student = new Student();
        student.setAge(10);
        //指定该组获得焦点(只有属于agentGroup的规则才有可能被触发),
        //若未设置规则的agentGroup,可在规则上添加auto-focus属性,自动获得焦点
        //若写了agentGroup写了auto-focus true则该议程组都会获得焦点
        kieSession.getAgenda().getAgendaGroup("要获得焦点的agentGroup").setFocus();
        kieSession.insert(student);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    @Test//演示(8)timer
    public void test7() throws InterruptedException {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        final KieSession kieSession = kieContainer.newKieSession();
        Student student = new Student();
        student.setAge(10);
        kieSession.insert(student);

        new Thread(new Runnable() {
            public void run() {
                //启动规则引擎进行规则匹配，直到调用halt方法才结束规则引擎
                kieSession.fireUntilHalt();
            }
        }).start();

        Thread.sleep(10000);
        //结束规则引擎
        kieSession.halt();
        kieSession.dispose();
    }

    @Test//演示(9)date-effective (10)date-expires
    public void test8() {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.fireAllRules();
        kieSession.dispose();
    }


}
