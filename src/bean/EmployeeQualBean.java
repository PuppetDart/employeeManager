package bean;

public class EmployeeQualBean {
	private String qualName;
	private String passYear;
	private String orgName;
	private float result;
	
	public EmployeeQualBean() {
		this.qualName=null;
		this.passYear=null;
		this.orgName=null;
		this.result=0;
	}
	
	public void setQualName(String qualName) {
		this.qualName=qualName;
	}
	public String getQualName() {
		return qualName;
	}
	
	public void setPassYear(String passYear) {
		this.passYear=passYear;
	}
	public String getPassYear() {
		return passYear;
	}
	
	public void setOrgName(String orgName) {
		this.orgName=orgName;
	}
	public String getOrgName() {
		return orgName;
	}
	
	public void setResult(float result) {
		this.result=result;
	}
	public float getResult() {
		return result;
	}
}
