package util;

public class DBQueryUtil {
	
	public static String masterTableName="m_employee_ap";
	
	//select statements
	public static String selectAll() {
		return "select empid, designation, officelocid, contact, dob, doj, maritalStatus, department, name, mail, salary, gender from "+masterTableName;
	}
	public static String selectAllBank() {
		return "select empid, bankName, accnum, branch, acctype, ifsc from m_employee_bank_ap";
	}
	public static String selectInd() {
		return "select name, designation, mail, maritalStatus, department,"
				+ "salary, gender, officelocid, contact, dob, doj from "+masterTableName + " where empID=?";
	}
	public static String selectIndBank() {
		return "select bankName, accnum, branch, acctype, ifsc from m_employee_bank_ap where empID=?";
	}
	public static String selectIndQual() {
		return "select qualName, passYear, orgName, result from m_employee_qual_ap where empID=?";
	}
	public static String getLogSheetAll() {
		return "select username, logdate, login, logout from logsheet";
	}
	public static String getLogSheetUser() {
		return "select username, logdate, login, logout from logsheet where username=?";
	}
	public static String checkID() {
		return "select empID from "+masterTableName+" where empID=?";
	}
	public static String checkQual() {
		return "select qualName from m_employee_qual_ap where empID=? and qualName=?";
	}
	public static String checkCEO() {
		return "select empid from m_employee_ap where designation='ceo'";
	}
	public static String fetchUser() {
		return "select password, privilege from m_employee_users_ap where username=?";
	}
	public static String fetchPrivilegeRequest() {
		return "select username from m_employee_users_ap where request='pending'";
	}
	
	//insert statements
	public static String newEmployee() {
		return "insert into "+masterTableName+" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}
	public static String newEmployeeBank() {
		return "insert into m_employee_bank_ap values(?,?,?,?,?,?,?)";
	}
	public static String addQual() {
		return "insert into m_employee_qual_ap values(?,?,?,?,?,?)";
	}
	public static String newUserCredentials() {
		return "insert into m_employee_users_ap values(?,?,?,?,?)";
	}
	public static String logSheetEntry() {
		return "insert into logSheet values(?, ?, ?, ?)";
	}
	
	//delete statements
	public static String removeEmployee() {
		return "delete from "+masterTableName+" where empid=?";
	}
	public static String removeEmployeeBank() {
		return "delete from m_employee_bank_ap where empid=?";
	}
	public static String removeEmployeeQual() {
		return "delete from m_employee_qual_ap where empid=?";
	}
	public static String removeCredentials() {
		return "delete from m_employee_users_ap where username=?";
	}
	
	//update statements
	public static String updateField() {
		return "update "+masterTableName+" set #=?, lastupdated=? where empid=?";
	}
	public static String updateBankField() {
		return "update m_employee_bank_ap set #=?, lastupdated=? where empid=?";
	}
	public static String updateCredentials() {
		return "update m_employee_users_ap set password=?, lastupdated=? where username=?";
	}
	public static String handlePrivilegeRequest() {
		return "update m_employee_users_ap set privilege=1, lastupdated=?, request='granted' where username=?";
	}
	public static String requestPrivilege() {
		return "update m_employee_users_ap set request='pending' where username=?";
	}
	
	//select statements for meta
	public static String getDepartments() {
		return "select department from employee_department_ap";
	}
	public static String getOffices() {
		return "select officelocid from employee_office_ap";
	}
	public static String getGenders() {
		return "select gender from employee_gender_ap";
	}
	public static String getDesigs() {
		return "select designation, rank from employee_desig_ap";
	}
	public static String getAccTypes() {
		return "select accountType from employee_acctype_ap";
	}
	public static String getMaritalStatus() {
		return "select married, val from employee_marital_ap";
	}
	public static String getQualNames() {
		return "select qualName from employee_qualname_ap";
	}
}
