package bean;

import java.sql.Date;
import java.util.ArrayList;

public class EmployeeBean {
    private String empID;
    private String name;
    private String designation;
    private int rank;
    private String mailID;
    private int maritalStatus;
    private String department;
    private long salary;
    private String gender;
    private String officeLocID;
    private String contact;
    private String hQual;
    private int holidayCount;
    private Date DOB;
    private Date DOJ;
    
    private String bankName;
    private String accNum;
    private String branch;
    private String accType;
    private String IFSC;
    
    private int newUserPrivilege;
    
    private java.sql.Timestamp lastUpdated;
    private ArrayList<EmployeeQualBean> qual;
	private Date currentDate;
    
    private String user;
    private String password;
    private int privilege;
    
    public EmployeeBean(){
    	this.empID=null;
    	this.name=null;
    	this.designation=null;
    	this.rank=-1;
    	this.mailID=null;
    	this.maritalStatus=-1;
    	this.department=null;
    	this.salary=-1;
    	this.gender=null;
    	this.officeLocID=null;
    	this.contact=null;
    	this.hQual=null;
    	this.holidayCount=-1;
    	this.DOB=null;
    	this.DOJ=null;
    	this.bankName=null;
    	this.accNum=null;
    	this.branch=null;
    	this.accType=null;
    	this.IFSC=null;
    	this.newUserPrivilege=-1;
    	this.lastUpdated=null;
    	this.qual=null;
    	this.user=null;
    	this.password=null;
    	this.privilege=-1;
    	this.currentDate=null;
    }

    //.................................
    public String getempID(){
        if(empID !=null){
            return empID;
        }
        return "employee ID not set";
    }
    public void setempID(String empID){
        this.empID=empID;
    }
    //.................................
    
    //.................................
    public String getName(){
        if(name !=null){
            return name;
        }
        return "name not set";
    }
    public void setName(String name){
        this.name=name;
    }
    //.................................
    
    //.................................
    public String getDesignation(){
        if(designation !=null){
            return designation;
        }
        return "designation not set";
    }
    public void setDesignation(String designation){
        this.designation=designation;
    }
    //.................................
    
    //.................................
    public int getRank(){
        return rank;
    }
    public void setRank(int rank){
        this.rank=rank;
    }
    //.................................
    
    //.................................
    public String getMail(){
    	return mailID;
    }
    public void setMail(String mailID) {
    	this.mailID=mailID;
    }
    //.................................
    
    //.................................
    public int getMaritalStatus(){
    	return maritalStatus;
    }
    public void setMaritalStatus(int maritalStatus) {
    	this.maritalStatus=maritalStatus;
    }
    //.................................
    
    //.................................
    public String getDepartment(){
    	return department;
    }
    public void setDepartment(String department) {
    	this.department=department;
    }
    //.................................
    
    //.................................
    public long getSalary(){
    	return salary;
    }
    public void setSalary(long salary) {
    	this.salary=salary;
    }
    //.................................
    
    //.................................
    public int getHolidayCount(){
    	return holidayCount;
    }
    public void setHolidayCount(int holidayCount) {
    	this.holidayCount=holidayCount;
    }
    //.................................
    
    //.................................
    public String getGender(){
    	return gender;
    }
    public void setGender(String gender) {
    	this.gender=gender;
    }
    //.................................
    
    //.................................
    public String getOfficeLocID(){
    	return officeLocID;
    }
    public void setOfficeLocID(String officeLocID) {
    	this.officeLocID=officeLocID;
    }
    //.................................
    
    //.................................
    public String getContact(){
    	return contact;
    }
    public void setContact(String contact) {
    	this.contact=contact;
    }
    //.................................
    
    //.................................
    public String getHQual(){
    	return hQual;
    }
    public void setHQual(String hQual) {
    	this.hQual=hQual;
    }
    //.................................
    
    //.................................
    public Date getDOB(){
    	return DOB;
    }
    public void setDOB(Date DOB) {
    	this.DOB=DOB;
    }
    //.................................
    
    //.................................
    public Date getDOJ(){
    	return DOJ;
    }
    public void setDOJ(Date DOJ) {
    	this.DOJ=DOJ;
    }
    //.................................
    
    //.................................
    public int getNewUserPrivilege() {
		return newUserPrivilege;
	}

	public void setNewUserPrivilege(int newUserPrivilege) {
		this.newUserPrivilege = newUserPrivilege;
	}
    //.................................
    
    //.................................
    public String getBankName(){
    	return bankName;
    }
	public void setBankName(String bankName) {
    	this.bankName=bankName;
    }
    //.................................
    
    //.................................
    public String geAccNum(){
    	return accNum;
    }
    public void setAccNum(String accNum) {
    	this.accNum=accNum;
    }
    //.................................
    
    //.................................
    public String getBranch(){
    	return branch;
    }
    public void setBranch(String branch) {
    	this.branch=branch;
    }
    //.................................
    
    //.................................
    public String getAccType(){
    	return accType;
    }
    public void setAccType(String accType) {
    	this.accType=accType;
    }
    //.................................
    
    //.................................
    public String getIFSC(){
    	return IFSC;
    }
    public void setIFSC(String IFSC) {
    	this.IFSC=IFSC;
    }
    //.................................
    
    //.................................
    public java.sql.Timestamp getLastUpdated(){
    	return lastUpdated;
    }
    public void setLastUpdated(java.sql.Timestamp lastUpdated) {
    	this.lastUpdated=lastUpdated;
    }
    //.................................
    
    //.................................
    public ArrayList<EmployeeQualBean> getQual(){
    	return qual;
    }
    public void setQual(ArrayList<EmployeeQualBean> qual) {
    	this.qual=qual;
    }
    //.................................
    
    //.................................
    public String getCurrentUser(){
        if(user !=null){
            return user;
        }
        return "User not set";
    }
    public void setCurrentUser(String user){
        this.user=user;
    }
    //.................................
    
    //.................................
    public String getPassword(){
        if(password !=null){
            return password;
        }
        return "Password not set";
    }
    public void setPassword(String password){
        this.password=password;
    }
    //.................................
    
    //.................................
    public int getPrivilege() {
		return privilege;
	}

	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
    //.................................
    
    //.................................
    public Date getCurrentDate(){
        if(currentDate !=null){
            return currentDate;
        }
        return null;
    }
    public void setCurrentDate(Date currentDate){
        this.currentDate=currentDate;
    }
    //.................................
    
    //.................................
}

