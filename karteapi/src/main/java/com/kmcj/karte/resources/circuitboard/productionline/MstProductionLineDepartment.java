package com.kmcj.karte.resources.circuitboard.productionline;

/**
 * Created by MinhDTB on 2018/03/01
 */
public class MstProductionLineDepartment {

    private String choice;
    private int department;

    public MstProductionLineDepartment() {
    }

    public MstProductionLineDepartment(String choice, int department) {
        this.choice = choice;
        this.department = department;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }
}
