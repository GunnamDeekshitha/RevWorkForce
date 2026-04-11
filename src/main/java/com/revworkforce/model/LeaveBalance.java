package com.revworkforce.model;

public class LeaveBalance {

    private int employeeId;
    private int casualLeave;
    private int sickLeave;
    private int paidLeave;
    private int privilegeLeave;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getCasualLeave() {
        return casualLeave;
    }

    public void setCasualLeave(int casualLeave) {
        this.casualLeave = casualLeave;
    }

    public int getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(int sickLeave) {
        this.sickLeave = sickLeave;
    }

    public int getPaidLeave() {
        return paidLeave;
    }

    public void setPaidLeave(int paidLeave) {
        this.paidLeave = paidLeave;
    }
    public int getPrivilegeLeave() {
        return privilegeLeave;
    }

    public void setPrivilegeLeave(int privilegeLeave) {
        this.privilegeLeave = privilegeLeave;
    }
}