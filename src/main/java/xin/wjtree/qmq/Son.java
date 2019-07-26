package xin.wjtree.qmq;

import jdk.nashorn.internal.ir.annotations.Ignore;

import java.time.LocalDate;

public class Son extends Father {

	@Ignore
	private Double salary;

	private LocalDate birtyDay;

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDate getBirtyDay() {
        return birtyDay;
    }

    public void setBirtyDay(LocalDate birtyDay) {
        this.birtyDay = birtyDay;
    }
}
