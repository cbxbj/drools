package org.example.drools.entity;

import lombok.Data;

@Data
public class Calculation {
    private double wage;//税前工资
    private double wagemore;//应纳税所得额
    private double cess;//税率
    private double preminus;//速算扣除数
    private double wageminus;//扣税额
    private double actualwage;//税后工资
}
