package home;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import bean.EmployeeBean;
import bean.EmployeeQualBean;
import bean.EmployeeTimeBean;
import doa.DBConnection;
import exceptionHandlers.EmptyInputException;
import exceptionHandlers.InvalidInputException;
import util.DBQueryUtil;
import validator.Validator;

public class Home{
	

	private static int stCounter=1;

	//Containers for loading meta tables
	private static HashMap<String,Integer> desigMap=null;
	private static HashSet<String> deptSet=null;
	private static HashSet<String> officeLocSet=null;
	private static HashSet<String> genderSet=null;
	private static HashSet<String> accTypeSet=null;
	private static HashSet<String> qualNameSet=null;
	private static HashMap<String,Integer> maritalStatusMap=null;

	private static HashMap<Date, ArrayList<EmployeeTimeBean> > logDateMap = new HashMap<>();
	private static HashMap<String, HashMap<Date, ArrayList<EmployeeTimeBean> > > logUserMap = new HashMap<>();

	static Scanner in =new Scanner(System.in);

	//This function takes in a DBConnection object
	//& inputs a employee ID, validates it,
	//runs a select query to check whether data for
	//the entered Employee ID exists in the master table
	public static String idExists(DBConnection connector){

		String empID="";
		Connection connObj=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try {
			while(empID.equals("")) {
				System.out.print("Enter Employee ID: ");
				empID = in.next().trim().toLowerCase();

				System.out.print("Matching Employee ID . . . ");
				try {
					if(empID.equals(""))
						throw new EmptyInputException("Employee ID");
					else {
						connObj=connector.connect();
						
						st=connObj.prepareStatement(DBQueryUtil.checkID());
						st.setString(stCounter++, empID);
						stCounter=1;

						rs=st.executeQuery();

						if(rs.next()==false) {
							System.out.println("Entered Employee ID not found. Try Again.");
						}
						else {
							System.out.println("Found.");
							break;
						}
					}
				}
				catch(Exception e) {
					System.out.println("Error"+ e.getMessage());
				}
				finally {
					connObj.close();
					st.close();
					rs.close();
				}

				empID="";
			}
		}
		catch(Exception e) {
			System.out.println("IDExists Error: "+ e);
		}
		return empID;
	}

	//filling containers with data from meta tables
	public static void fillPresets(DBConnection connector) {

		Connection connObj=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		
		desigMap=new HashMap<String,Integer>();
		deptSet=new HashSet<String>();
		officeLocSet=new HashSet<String>();
		genderSet=new HashSet<String>();
		accTypeSet=new HashSet<String>();
		qualNameSet=new HashSet<String>();
		maritalStatusMap=new HashMap<String,Integer>();

		try {
			try {

				connObj=connector.connect();

				st =connObj.prepareStatement(DBQueryUtil.getDesigs());
				rs=st.executeQuery();
				while(rs.next())
					desigMap.put(rs.getString(1), rs.getInt(2));

				st=connObj.prepareStatement(DBQueryUtil.getDepartments());
				rs=st.executeQuery();
				while(rs.next())
					deptSet.add(rs.getString(1));

				st=connObj.prepareStatement(DBQueryUtil.getOffices());
				rs=st.executeQuery();
				while(rs.next())
					officeLocSet.add(rs.getString(1));

				st=connObj.prepareStatement(DBQueryUtil.getGenders());
				rs=st.executeQuery();
				while(rs.next())
					genderSet.add(rs.getString(1));

				st=connObj.prepareStatement(DBQueryUtil.getAccTypes());
				rs=st.executeQuery();
				while(rs.next())
					accTypeSet.add(rs.getString(1));
				
				st=connObj.prepareStatement(DBQueryUtil.getQualNames());
				rs=st.executeQuery();
				while(rs.next())
					qualNameSet.add(rs.getString(1));

				st =connObj.prepareStatement(DBQueryUtil.getMaritalStatus());
				rs=st.executeQuery();
				while(rs.next())
					maritalStatusMap.put(rs.getString(1), rs.getInt(2));
			}
			catch(Exception e) {
				System.out.println("Error"+ e.getMessage());
			}
			finally {
				if(connObj!=null) connObj.close();
				if(st!=null) st.close();
				if(rs!=null) rs.close();
			}
		}
		catch(Exception e) {
			System.out.println("FillPresets Error: "+ e);
		}
	}

	public static void editBasic(EmployeeBean beanObj, DBConnection connector) {

		boolean exitEdit=true;
		try {
			while(exitEdit) {
				System.out.println();
				System.out.println(">] Edit Employee Basic Details [<");
				System.out.println("----------------------------------");
				System.out.println("1. Designation");
				System.out.println("2. Mail Address");
				System.out.println("3. Marital Status");
				System.out.println("4. Department");
				System.out.println("5. Salary (INR)");
				System.out.println("6. Office Location");
				System.out.println("7. Contact Number");
				System.out.println("8. Back to Main Menu");
				System.out.println();

				String empID="";
				Connection connObj=null;
				PreparedStatement st=null;

				switch(Validator.choiceValidate()) {

				case 1:

					empID= (beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setDesignation(Validator.desigValidate(desigMap, connector));
					beanObj.setRank(desigMap.get(beanObj.getDesignation()));
					System.out.print("Updating field . . . ");

					try {

						connObj=connector.connect();

						//first update designation
						st=connObj.prepareStatement(DBQueryUtil.updateField().replaceAll("#", "designation"));
						st.setString(stCounter++, beanObj.getDesignation());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int desigResult = st.executeUpdate();

						st.close();
						//then update rank
						st=connObj.prepareStatement(DBQueryUtil.updateField().replaceAll("#", "rank"));
						st.setInt(stCounter++, beanObj.getRank());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int rankResult = st.executeUpdate();

						System.out.println(rankResult==0 || desigResult==0 ? "Update Failed":"Designation updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;
				case 2:

					empID= (beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setMail(Validator.mailValidate());
					System.out.print("Updating field . . . ");

					try {
						connObj=connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateField().replaceAll("#", "mail"));
						st.setString(stCounter++,beanObj.getMail());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int queryResult = st.executeUpdate();

						System.out.println(queryResult==0 ? "Update failed": "Mail updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;
				case 3:

					empID= (beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setMaritalStatus(Validator.maritalStatusValidate(maritalStatusMap));
					System.out.print("Updating field . . . ");

					try {
						connObj=connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateField().replaceAll("#", "maritalstatus"));
						st.setInt(stCounter++,beanObj.getMaritalStatus());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int queryResult = st.executeUpdate();
						System.out.println(queryResult==0 ? "Update failed": "Marital Status updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;
				case 4:

					empID= (beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setDepartment(Validator.deptValidate(deptSet));
					System.out.print("Updating field . . . ");

					try {
						connObj=connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateField().replaceAll("#", "department"));
						st.setString(stCounter++,beanObj.getDepartment());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int queryResult = st.executeUpdate();

						System.out.println(queryResult==0 ? "Update failed": "Department updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;
				case 5:

					empID= (beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setSalary(Validator.salaryValidate());
					System.out.print("Updating field . . . ");

					try {
						connObj= connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateField().replaceAll("#", "salary"));
						st.setLong(stCounter++, beanObj.getSalary());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int queryResult = st.executeUpdate();

						System.out.println(queryResult==0 ? "Update failed": "Salary updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;
				case 6:

					empID= (beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setOfficeLocID(Validator.officeLocValidate(officeLocSet));
					System.out.print("Updating field . . . ");

					try {
						connObj= connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateField().replaceAll("#", "officelocid"));
						st.setString(stCounter++, beanObj.getOfficeLocID());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int queryResult = st.executeUpdate();

						System.out.println(queryResult==0 ? "Update failed": "Office Location updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;

				case 7:

					empID= (beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setContact(Validator.contactValidate());
					System.out.print("Updating field . . . ");

					try {
						connObj= connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateField().replaceAll("#", "contact"));
						st.setString(stCounter++,beanObj.getContact());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int queryResult = st.executeUpdate();

						System.out.println(queryResult==0 ? "Update failed": "Contact updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;

				case 8:
					exitEdit=false;
					break;

				default: System.out.println("Incorrect choice.");
				}
			}
		}
		catch(Exception e) {
			System.out.println("EditBasic Error: "+ e.getMessage());
		}
	}

	public static void editBank(EmployeeBean beanObj, DBConnection connector) {

		boolean exitBankEdit=true;
		try {
			while(exitBankEdit) {
				System.out.println();
				System.out.println(">] Edit Employee Bank Details [<");
				System.out.println("---------------------------------");
				System.out.println("1. Bank Name");
				System.out.println("2. Account Number");
				System.out.println("3. Branch");
				System.out.println("4. Account Type");
				System.out.println("5. IFSC");
				System.out.println("6. Back to Main Menu");
				System.out.println();

				String empID = "";
				Connection connObj=null;
				PreparedStatement st=null;

				switch(Validator.choiceValidate()) {

				case 1:

					empID=(beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setBankName(Validator.bankNameValidate());
					System.out.print("Updating field . . . ");

					try {
						connObj= connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateBankField().replaceAll("#", "bankname"));
						st.setString(stCounter++,beanObj.getBankName());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int queryResult = st.executeUpdate();

						System.out.println(queryResult==0 ? "Update failed": "Bank Name updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;

				case 2:

					empID=(beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setAccNum(Validator.accNumValidate());
					System.out.print("Updating field . . . ");

					try {
						connObj= connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateBankField().replaceAll("#", "accnum"));
						st.setString(stCounter++, beanObj.geAccNum());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int queryResult = st.executeUpdate();

						System.out.println(queryResult==0 ? "Update failed": "Account Number updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;

				case 3:

					empID=(beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setBranch(Validator.branchValidate());
					System.out.print("Updating field . . . ");

					try {
						connObj= connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateBankField().replaceAll("#", "branch"));
						st.setString(stCounter++, beanObj.getBranch());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int queryResult = st.executeUpdate();

						System.out.println(queryResult==0 ? "Update failed": "Branch updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;

				case 4:

					empID=(beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setAccType(Validator.accTypeValidate(accTypeSet));
					System.out.print("Updating field . . . ");

					try {
						connObj= connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateBankField().replaceAll("#", "acctype"));
						st.setString(stCounter++, beanObj.getAccType());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						System.out.println(st.executeUpdate()==0 ? "Update failed": "Account Type updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;

				case 5:

					empID=(beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

					beanObj.setIFSC(Validator.IFSCValidate());
					System.out.print("Updating field . . . ");

					try {
						connObj= connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.updateBankField().replaceAll("#", "ifsc"));
						st.setString(stCounter++, beanObj.getIFSC());
						st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
						st.setString(stCounter++, empID);
						stCounter=1;
						
						int queryResult = st.executeUpdate();

						System.out.println(queryResult==0 ? "Update failed": "IFSC updated.");
					}
					catch(SQLException e) {
						System.out.println("SQL Error: "+e);
					}
					catch(Exception e) {
						System.out.println("Error: "+e);
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
					}

					break;

				case 6:
					exitBankEdit=false;
					break;

				default: System.out.println("Incorrect choice.");
				}
			}
		}
		catch(Exception e) {
			System.out.println("EditBank Error: "+ e.getMessage());
		}
	}

	public static void addEducation(EmployeeBean beanObj, DBConnection connector) {

		try {

			String empID= (beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

			//list of qualifications
			ArrayList <EmployeeQualBean> qualList =new ArrayList<>();

			Connection connObj=null;
			PreparedStatement st=null;
			ResultSet rs=null;

			while(true) {

				//Qualification object
				EmployeeQualBean qualObj=new EmployeeQualBean();

				//Equivalent to: qualObj.setProperty("validator-returned-value");
				qualObj.setQualName(Validator.qualNameValidate(qualNameSet));
				try {
					connObj=connector.connect();
					st=connObj.prepareStatement(DBQueryUtil.checkQual());
					st.setString(stCounter++, empID);
					st.setString(stCounter++, qualObj.getQualName());
					stCounter=1;
					
					rs=st.executeQuery();
				}
				catch(Exception e){
					System.out.println("Error: "+ e.getMessage());
				}
				finally {
					if(connObj!=null) connObj.close();
					if(st!=null) st.close();
					if(rs!=null) rs.close();
				}

				if(rs.next()==true)
					System.out.println(qualObj.getQualName().toUpperCase()+" qualification already exists for employee ID: "+empID+".");

				else {
					qualObj.setPassYear(Validator.passYearValidate());
					qualObj.setOrgName(Validator.orgNameValidate());
					qualObj.setResult(Validator.resultValidate());

					//add object to Qualification List
					qualList.add(qualObj);
				}

				System.out.println();
				System.out.println("0: Exit");
				System.out.println("1: Continue");

				if (Validator.choiceValidate()==0)
					break;
			}

			beanObj.setQual(qualList);
			qualList=beanObj.getQual();

			if(qualList.size()!=0) {
				System.out.print("Adding qualification details . . . ");
				try {
					connObj=connector.connect();
					for (EmployeeQualBean obj:qualList){
						st=connObj.prepareStatement(DBQueryUtil.addQual());
						st.setString(stCounter++, empID);
						st.setString(stCounter++, obj.getQualName());
						st.setString(stCounter++, obj.getPassYear());
						st.setString(stCounter++, obj.getOrgName());
						st.setFloat(stCounter++, obj.getResult());
						st.setTimestamp(stCounter++, new java.sql.Timestamp(System.currentTimeMillis()));
						stCounter=1;
						st.executeUpdate();
					}
					System.out.println("Qualifications updated.");
				}
				catch(Exception e) {
					System.out.println("Error: "+e);
				}
				finally {
					if(connObj!=null) connObj.close();
					if(st!=null) st.close();
				}
			}
		}
		catch(Exception e) {
			System.out.println("AddQualification Error: "+ e.getMessage());
		}
	}

	public static void viewSingleUser(DBConnection connector, EmployeeBean beanObj) {
		boolean exitDisplay =true;

		try {
			String empID= (beanObj.getPrivilege()==1)? idExists(connector) : beanObj.getCurrentUser();

			Connection connObj=null;
			PreparedStatement st=null;
			ResultSet rs=null;

			while(exitDisplay) {
				System.out.println();
				System.out.println(">] Get Employee Details [<");
				System.out.println("-----------------------------");
				System.out.println("1. Basic Details");
				System.out.println("2. Bank Details");
				System.out.println("3. Educational Details");
				System.out.println("4. Back to Main Menu");
				System.out.println();

				switch(Validator.choiceValidate()) {
				case 1:

					System.out.println("Fetching Basic details . . . ");
					System.out.println();
					try {

						connObj=connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.selectInd());
						st.setString(stCounter++, empID);
						stCounter=1;
						
						rs=st.executeQuery();
						while(rs.next()) {
							System.out.println("           Name: " +rs.getString(1));
							System.out.println("    Designation: " +rs.getString(2));
							System.out.println("           Mail: " +rs.getString(3));
							String maritalStatus = (rs.getString(4).equals("0"))?"Unmarried":"Married";
							System.out.println(" Marital Status: " +maritalStatus);
							System.out.println("     Department: " +rs.getString(5).toUpperCase());
							System.out.println("         Salary: " + "Rs. "+rs.getString(6));
							System.out.println("         Gender: " +rs.getString(7));
							System.out.println("Office Location: " +rs.getString(8).toUpperCase());
							System.out.println("        Contact: " +rs.getString(9));
							System.out.println("            DOB: " +rs.getString(10));
							System.out.println("            DOJ: " +rs.getString(11));
						}
					}
					catch(Exception e) {
						System.out.println("Error"+ e.getMessage());
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
						if(rs!=null) rs.close();
					}
					break;

				case 2:

					System.out.println("Fetching Bank details . . . ");
					System.out.println();
					try {

						connObj=connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.selectIndBank());
						st.setString(stCounter++, empID);
						stCounter=1;
						
						rs=st.executeQuery();

						while(rs.next()) {
							System.out.println("      Bank Name: " +rs.getString(1));
							System.out.println(" Account Number: " +rs.getString(2));
							System.out.println("         Branch: " +rs.getString(3));
							System.out.println("   Account Type: " +rs.getString(4));
							System.out.println("           IFSC: " +rs.getString(5));
						}
					}
					catch(Exception e) {
						System.out.println("Error"+ e.getMessage());
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
						if(rs!=null) rs.close();
					}
					break;

				case 3:

					System.out.println("Fetching Educational details . . . ");
					System.out.println();
					try {
						connObj=connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.selectIndQual());
						st.setString(stCounter++, empID);
						stCounter=1;
						
						rs=st.executeQuery();


						boolean flag=true;
						while(rs.next()) {
							flag=false;
							System.out.println(" Qualification Name: " +rs.getString(1));
							System.out.println("       Passing Year: " +rs.getString(2));
							System.out.println("     Institute Name: " +rs.getString(3));
							System.out.println("         Result (%): " +rs.getString(4));
							System.out.println();
						}
						if(flag) {
							System.out.println("No Educational Details present for Employee ID: "+empID +".");
						}
					}
					catch(Exception e) {
						System.out.println("Error"+ e.getMessage());
					}
					finally {
						if(connObj!=null) connObj.close();
						if(st!=null) st.close();
						if(rs!=null) rs.close();
					}
					break;

				case 4: 
					exitDisplay=false;
					break;
				default: System.out.println("Incorrect choice.");
				}
			}
		}
		catch(Exception e){
			System.out.println("ViewSingleUser Error: "+ e.getMessage());
		}

	}

	public static void viewLogSheet(DBConnection connector, EmployeeBean beanObj) {

		System.out.println("Fetching LogSheet . . .");
		System.out.println();

		HashMap<String, Long> totalTimeMap= new HashMap<>();

		Connection connObj=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		
		int counter=1;
		try {
			try {
				connObj=connector.connect();

				// if privelege=admin/1 fetch All logs,
				// otherwise fetch only current user's logs

				if(beanObj.getPrivilege()==1) {
					st = connObj.prepareStatement(DBQueryUtil.getLogSheetAll());
				}
				else {
					st = connObj.prepareStatement(DBQueryUtil.getLogSheetUser());
					st.setString(stCounter++, beanObj.getCurrentUser());
					stCounter=1;
				}
				rs=st.executeQuery();

				System.out.println(" > Complete logs:");
				System.out.println();

				boolean flag=true;

				while(rs.next()) {

					flag=false;

					EmployeeTimeBean timeBeanPrintObj=new EmployeeTimeBean();

					timeBeanPrintObj.setLoginTime(rs.getTime(3));
					timeBeanPrintObj.setLogoutTime(rs.getTime(4));

					long diff= rs.getTime(4).getTime() - rs.getTime(3).getTime();

					if(totalTimeMap.get(rs.getString(1))==null) 
						totalTimeMap.put(rs.getString(1), diff);
					else
						totalTimeMap.put(rs.getString(1), totalTimeMap.get(rs.getString(1))+diff);

					long diffMins = diff/60000;
					long diffSecs = (diff - (diffMins * 60000))/1000;

					System.out.format("%4s %12s %15s %12s %12s %14s\n", counter++ +")", rs.getString(1), rs.getDate(2), rs.getTime(3), rs.getTime(4), diffMins +"m "+diffSecs+ "s");
				}
				if(flag) System.out.println("   Empty.");
				flag=true;

				System.out.println();
				System.out.println(" > Time spent on this application:");
				System.out.println();

				counter=1;
				for(String userName: totalTimeMap.keySet()) {

					flag=false;
					long diffMins=totalTimeMap.get(userName)/60000;
					long diffSecs=(totalTimeMap.get(userName) - (diffMins * 60000))/1000;

					System.out.format("%4s %12s %15s\n", counter++ +")", userName, diffMins+"m "+diffSecs+"s");
				}
				if(flag) System.out.println("   Empty.");

			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
			finally {
				if(connObj!=null) connObj.close();
				if(st!=null) st.close();
				if(rs!=null) rs.close();
			}
		}
		catch(Exception e) {
			System.out.println("ViewLogSheet Erro: "+ e);
		}
	}

	public static void systemExit(EmployeeTimeBean timeBeanObj, ArrayList<EmployeeTimeBean> timeArr, EmployeeBean beanObj,DBConnection connector) {
		System.out.print("Logging user session . . . ");

		try {
			timeBeanObj.setLogoutTime(new Time(System.currentTimeMillis()));

			timeArr.add(timeBeanObj);

			logDateMap.put(beanObj.getCurrentDate(), timeArr);
			logUserMap.put(beanObj.getCurrentUser(), logDateMap);

			//retrieving timeIn, timeOut
			timeArr=logUserMap.get(beanObj.getCurrentUser()).get(beanObj.getCurrentDate());

			Connection connObj=null;
			PreparedStatement st=null;

			try {
				connObj=connector.connect();
				st=connObj.prepareStatement(DBQueryUtil.logSheetEntry());

				st.setString(stCounter++, beanObj.getCurrentUser());
				st.setDate(stCounter++, beanObj.getCurrentDate());
				st.setTime(stCounter++, timeArr.get(0).getLoginTime());
				st.setTime(stCounter++, timeArr.get(0).getLogoutTime());
				stCounter=1;

				st.executeUpdate();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
			finally {
				if(connObj!=null) connObj.close();
				if(st!=null) st.close();
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("System Exited.");
	}

	public static void addEmployee(String NewUserEmpID, HashMap<String,Integer> desigMap, HashMap<String,Integer> maritalStatusMap, HashSet<String> deptSet, HashSet<String> genderSet, HashSet<String> officeLocSet, HashSet<String> accTypeSet) throws InvalidInputException, EmptyInputException, SQLException{

		DBConnection connector=new DBConnection();
		EmployeeBean beanObj=new EmployeeBean();
		
		Connection connObj=null;
		PreparedStatement st=null;

		//empid & rank not validated

		//employeeBeanObj.setProperty("validated-property")
		//following values shall go into 'm_employee_ap'/master-table

		beanObj.setempID(NewUserEmpID); //1 B1 U1
		beanObj.setName(Validator.nameValidate()); //2

		beanObj.setDesignation(Validator.desigValidate(desigMap, connector)); //3
		beanObj.setRank(desigMap.get(beanObj.getDesignation())); //4

		beanObj.setMail(Validator.mailValidate()); //5
		beanObj.setMaritalStatus(Validator.maritalStatusValidate(maritalStatusMap)); //6
		beanObj.setDepartment(Validator.deptValidate(deptSet)); //7
		beanObj.setSalary(Validator.salaryValidate()); //8
		beanObj.setGender(Validator.genderValidate(genderSet)); //9
		beanObj.setOfficeLocID(Validator.officeLocValidate(officeLocSet)); //10
		beanObj.setContact(Validator.contactValidate()); //11
		beanObj.setDOB(Validator.dobValidate()); //12
		beanObj.setDOJ(Validator.dojValidate(beanObj.getDOB()+"")); //13

		//following values shall go into 'm_employee_bank_ap'/master-bank-Details-table
		beanObj.setBankName(Validator.bankNameValidate()); //B2
		beanObj.setAccNum(Validator.accNumValidate()); //B3
		beanObj.setBranch(Validator.branchValidate()); //B4
		beanObj.setAccType(Validator.accTypeValidate(accTypeSet)); //B5
		beanObj.setIFSC(Validator.IFSCValidate()); //B6

		beanObj.setPassword(Validator.passwordValidate()); //U2
		beanObj.setNewUserPrivilege(Validator.newUserPrivilegeValidate()); //U4

		//column common to all tables - employee & bank Details
		beanObj.setLastUpdated(new java.sql.Timestamp(System.currentTimeMillis())); //14 B7 U3

		System.out.print("Adding New Employee . . . ");
		try {

			connObj=connector.connect();
			st =connObj.prepareStatement(DBQueryUtil.newEmployee());

			st.setString(stCounter++, beanObj.getempID());
			st.setString(stCounter++, beanObj.getName());
			st.setString(stCounter++, beanObj.getDesignation());
			st.setInt(stCounter++, beanObj.getRank());
			st.setString(stCounter++, beanObj.getMail());
			st.setInt(stCounter++, beanObj.getMaritalStatus());
			st.setString(stCounter++, beanObj.getDepartment());
			st.setLong(stCounter++, beanObj.getSalary());
			st.setString(stCounter++, beanObj.getGender());
			st.setString(stCounter++, beanObj.getOfficeLocID());
			st.setString(stCounter++, beanObj.getContact());
			st.setDate(stCounter++, beanObj.getDOB());
			st.setDate(stCounter++, beanObj.getDOJ());
			st.setTimestamp(stCounter++, beanObj.getLastUpdated());
			stCounter=1;
			
			st.executeUpdate();
			
			st = connObj.prepareStatement(DBQueryUtil.newEmployeeBank());

			st.setString(stCounter++, beanObj.getempID());
			st.setString(stCounter++, beanObj.getBankName());
			st.setString(stCounter++, beanObj.geAccNum());
			st.setString(stCounter++, beanObj.getBranch());
			st.setString(stCounter++, beanObj.getAccType());
			st.setString(stCounter++, beanObj.getIFSC());
			st.setTimestamp(stCounter++, beanObj.getLastUpdated());
			stCounter=1;
			
			st.executeUpdate();

			st = connObj.prepareStatement(DBQueryUtil.newUserCredentials());

			st.setString(stCounter++, beanObj.getempID());
			st.setString(stCounter++, beanObj.getPassword());
			st.setTimestamp(stCounter++, new java.sql.Timestamp(System.currentTimeMillis()));
			st.setInt(stCounter++, beanObj.getNewUserPrivilege());
			st.setString(stCounter++, "NA");
			stCounter=1;

			st.executeUpdate();

			System.out.println("Employee Added.");

		}
		catch(SQLException e) {
			System.out.print(e);
		}
		finally {
			if(connObj!=null) connObj.close();
			if(st!=null) st.close();
		}
		System.out.println();
	}

	//the program starts here
	//all menu(s) nest inside main
	public static void main(String[] args){

		boolean loggerExit=true;
		Connection connObj=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		
		DBConnection connector =new DBConnection();

		EmployeeTimeBean timeBeanObj =new EmployeeTimeBean();
		ArrayList<EmployeeTimeBean> timeArr= new ArrayList<>();
		EmployeeBean beanObj=new EmployeeBean();
		try {
			while(loggerExit) {
				String user="";
				String password="";

				System.out.println();
				System.out.print("Enter username: ");
				user=in.nextLine().trim();
				System.out.print("Enter password: ");
				password=in.nextLine().trim();
				System.out.println();
				System.out.print("Matching credentials . . . ");
				try {
					if(Validator.noSpaceAlphaNumPatt.matcher(user).matches() && Validator.passwordPatt.matcher(password).matches()) {
						connObj=connector.connect();
						st=connObj.prepareStatement(DBQueryUtil.fetchUser());
						st.setString(stCounter++, user);
						stCounter=1;
						
						rs =st.executeQuery();

						if(rs.next() && rs.getString(1).equals(password)) {
							System.out.println("Match found.");

							beanObj.setCurrentUser(user);
							beanObj.setPrivilege(rs.getInt(2));
							beanObj.setCurrentDate(new Date(System.currentTimeMillis()));
							timeBeanObj.setLoginTime(new Time(System.currentTimeMillis()));

							loggerExit=false;
						}
						else
							System.out.println("Either password or username incorrect.");
					}
					else {
						System.out.println("Either password or username incorrect.");
					}
				}
				catch(Exception e) {
					System.out.print(e);
				}
				finally {
					if(connObj!=null)	connObj.close();
					if(st!=null)	st.close();
					if(rs!=null)	rs.close();
				}
			}


			System.out.print("Loading meta data . . . ");
			fillPresets(connector);
			System.out.println("Loaded.");

			boolean admin=beanObj.getPrivilege()==1? true:false;

			if(admin) {
				try {

					while(true) {

						System.out.println();
						System.out.println(">] Main Menu [<");
						System.out.println("--------------------------");
						System.out.println("0. Edit - Basic Details");
						System.out.println("1. Edit - Bank Details");
						System.out.println("2. Add New Employee");
						System.out.println("3. Add Educational Info");
						System.out.println("4. Remove Employee");
						System.out.println("5. Enlist All Employees");
						System.out.println("6. Get Single Employee Details");
						System.out.println("7. Manage Privileges");
						System.out.println("8. LogSheet");
						System.out.println("9. Exit System");

						System.out.println();
						String empID="";

						switch(Validator.choiceValidate()) {

						case 0: // Edit Basic Details
							editBasic(beanObj, connector);
							break;

						case 1: //bank details edit
							editBank(beanObj, connector);
							break;
						case 2: //add employee

							//instead of creating a new variable
							//to store the newly entered empID
							//here we are using, an already created global variable
							empID="";
							while(empID.equals("")) {
								System.out.print("Enter Employee ID: ");
								empID = in.next().trim().toLowerCase();
								System.out.print("Validating input . . . ");
								try {
									if(empID.equals(""))
										throw new EmptyInputException("Employee ID");
									else {
										connObj=connector.connect();
										st=connObj.prepareStatement(DBQueryUtil.checkID());
										st.setString(stCounter++, empID);
										stCounter=1;
										
										rs=st.executeQuery();

										if(rs.next()==false) {
											System.out.println("Validated.");
											break;
										}
										else {
											System.out.println("ID already exists.");
										}
									}
								}
								catch(Exception e) {
									System.out.println("Error:"+ e.getMessage());
								}
								finally{
									connObj.close();
									st.close();
									rs.close();
								}
								empID="";
								System.out.println();
							}

							addEmployee(empID, desigMap, maritalStatusMap, deptSet, genderSet, officeLocSet, accTypeSet);
							break;

						case 3: //education info
							addEducation(beanObj, connector);
							break;

						case 4: //remove employee

							empID="";

							empID=idExists(connector);

							System.out.print("Removing employee . . . ");

							try {
								connObj=connector.connect();
								st=connObj.prepareStatement(DBQueryUtil.removeEmployee());
								st.setString(stCounter++, empID);
								stCounter=1;
								st.executeUpdate();

								st=connObj.prepareStatement(DBQueryUtil.removeEmployeeBank());
								st.setString(stCounter++, empID);
								stCounter=1;
								st.executeUpdate();

								st=connObj.prepareStatement(DBQueryUtil.removeEmployeeQual());
								st.setString(stCounter++, empID);
								stCounter=1;
								st.executeUpdate();

								st=connObj.prepareStatement(DBQueryUtil.removeCredentials());
								st.setString(stCounter++, empID);
								stCounter=1;
								st.executeUpdate();

								System.out.println("Removed.");
							}
							catch(Exception e) {
								System.out.println("Error: "+ e.getMessage());
							}
							finally {
								connObj.close();
								st.close();
								rs.close();
							}
							break;

						case 5: //Enlist All Employees

							System.out.println("Fetching all records . . . ");
							System.out.println();


							try {
								connObj=connector.connect();
								st=connObj.prepareStatement(DBQueryUtil.selectAll());
								rs=st.executeQuery();

								int k=1;
								//						
								System.out.format("%11s %9s %7s %17s %17s %10s %12s %9s %18s %25s %15s %9s\n", 
										"EMPID", "DESIG.", "OFFICE", "CONTACT (+91)", "MARITAL STATUS", "D-O-B", "D-O-J", "DEPT.", "FULL NAME", "PERSONAL EMAIL", "SALARY (INR)", "GENDER");
								System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
								while(rs.next()) {

									System.out.format("%2s %6s %10s %5s %18s %15s %15s %12s %5s %20s %25s %14s %10s\n", k +") ",
											rs.getString(1), rs.getString(2), rs.getString(3).toUpperCase(), rs.getString(4), ((rs.getString(7).equals("1"))?" Married ": "Unmarried"),
											rs.getString(5), rs.getString(6), rs.getString(8).toUpperCase(), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12));
									k++;
								}
								System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

							}
							catch(Exception e) {
								System.out.println("Error: "+e.getMessage());
							}
							finally {
								connObj.close();
								st.close();
								rs.close();
							}
							break;

						case 6:	//get single employee details
							viewSingleUser(connector, beanObj);
							break;

						case 7: //Manage privilege request
							
							String selectedUser="";
							
							try {
								
								System.out.println("Fetching Requests . . . ");
								System.out.println();
								connObj=connector.connect();
								st =connObj.prepareStatement(DBQueryUtil.fetchPrivilegeRequest());
								stCounter=1;
								rs=st.executeQuery();
								
								HashSet<String> userRequestSet= new HashSet<>();
								while(rs.next()) {
									System.out.println(" "+ stCounter++ + ") \t "+ rs.getString(1) + "\t pending" );
									userRequestSet.add(rs.getString(1));
								}
								stCounter=1;
								
								System.out.println();
								selectedUser = Validator.requesteeValidate(userRequestSet);
								
								System.out.print("Updating admin privileges . . . ");
								
								//create new user entry
								st =connObj.prepareStatement(DBQueryUtil.handlePrivilegeRequest());
								st.setTimestamp(stCounter++, new Timestamp(System.currentTimeMillis()));
								st.setString(stCounter++, selectedUser);
								stCounter=1;
								st.executeUpdate();

								System.out.println("Granted.");

							}
							catch(Exception e) {
								System.out.print(e);
							}
							finally {
								connObj.close();
								st.close();
								rs.close();
							}
							break;

						case 8:
							viewLogSheet(connector, beanObj);
							break;

						case 9:
							systemExit(timeBeanObj, timeArr, beanObj, connector);
							System.exit(0);
							break;

						default: System.out.println("Incorrect choice.");
						}
					}

				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			else {
				try {
					while(true) {

						System.out.println();
						System.out.println(">] Main Menu [<");
						System.out.println("--------------------------");
						System.out.println("0. Edit your Basic Details"); //0
						System.out.println("1. Edit your Bank Details"); //1
						System.out.println("2. Add Educational Info"); //3
						System.out.println("3. View your Details"); //6
						System.out.println("4. Your LogSheet"); //8
						System.out.println("5. Request Admin Privilege"); //9
						System.out.println("6. Exit System"); //9

						System.out.println();

						switch(Validator.choiceValidate()) {
						case 0:	
							editBasic(beanObj, connector);
							break;
						case 1:
							editBank(beanObj, connector);
							break;
						case 2:
							addEducation(beanObj, connector);
							break;
						case 3:
							viewSingleUser(connector, beanObj);
							break;
						case 4: 
							viewLogSheet(connector, beanObj);
							break;
						case 5: 
							try {
								System.out.println();
								System.out.print("Filing request for privilege upgrade . . . ");								
								connObj=connector.connect();
								st=connObj.prepareStatement(DBQueryUtil.requestPrivilege());
								st.setString(stCounter++, beanObj.getCurrentUser());
								stCounter=1;
								st.executeUpdate();
								System.out.println("Done.");
							}
							catch(Exception e){
								System.out.println("RequestPrivilege Error: "+e);
							}
							finally {
								
							}
							break;
						case 6: 
							systemExit(timeBeanObj, timeArr, beanObj, connector);
							System.exit(0);
							break;
						default: System.out.println("Incorrect choice.");
						}
					}
				}
				catch(Exception e){
					System.out.println(e);
				}
			}

		}
		catch(Exception e) {
			System.out.println(e);
		}

	}
}
