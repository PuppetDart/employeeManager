package validator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Pattern;

import doa.DBConnection;
import exceptionHandlers.EmptyInputException;
import exceptionHandlers.InvalidInputException;
import util.DBQueryUtil;

public class Validator {

	static Scanner in =new Scanner(System.in);
	static InvalidInputException invalidExObj=new InvalidInputException();
	
	public static Pattern choicePattObj = Pattern.compile(new String ("^\\d{1,2}$"));
	public static Pattern mailPattObj = Pattern.compile(new String ("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"));
	public static Pattern contactPattObj = Pattern.compile(new String ("^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$"));
	public static Pattern salaryPattObj = Pattern.compile(new String ("^\\d{1,10}$"));
	public static Pattern datePattObj = Pattern.compile(new String ("^(?:\\d{4}[\\-\\/]?(?:(?:(?:(?:0[13578]|1[02])[\\-\\/]?(?:0[1-9]|[1-2][0-9]|3[01]))|(?:(?:0[469]|11)[\\-\\/](?:0[1-9]|[1-2][0-9]|30))|(?:02[\\-\\/]?(?:0[1-9]|1[0-9]|2[0-8]))))|(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26]))|(?:(?:[02468][048])|[13579][26])00)[\\-\\/]?02[\\-\\/]?29)$"));
	public static Pattern IFSCPattObj = Pattern.compile(new String ("^[A-Z]{4}0[A-Z0-9]{6}$"));
	public static Pattern accNumPattObj = Pattern.compile(new String ("^\\d{9,18}$"));
	public static Pattern yearPattObj = Pattern.compile(new String ("^\\d{4}$"));
	public static Pattern markPattObj = Pattern.compile(new String("^(?<whole>99|\\d{1,2})(?<fract>\\.\\d{1,2})?$"));

	public static Pattern binaryPattObj = Pattern.compile(new String("^[01]{1}$"));
	
	public static Pattern passwordPatt = Pattern.compile(new String ("(?=.*[A-Z].*)(?=.*[!@#$&*])(?=.*[0-9].*)(?=.*[a-z].*).{8,}"));
	
	public static Pattern minStringPatt = Pattern.compile(new String ("^(([a-zA-Z]+\\s)*[a-zA-Z]+){3,60}$"));
	public static Pattern noSpaceAlphaNumPatt = Pattern.compile(new String ("([a-zA-Z0-9]{3,})*"));
	public static Pattern minAlphaNumPatt = Pattern.compile(new String ("([a-zA-Z0-9\\s]{3,})*"));
	
	static Calendar now = Calendar.getInstance();
	static int currentYear = now.get(Calendar.YEAR);
	static int currentMonth = now.get(Calendar.MONTH) + 1;
	static int currentDate = now.get(Calendar.DATE);
	
	public static int choiceValidate() throws InvalidInputException, EmptyInputException{
		String choice="";
		while(choice=="") {
			System.out.print("Enter Choice: ");
			choice=in.nextLine().trim();
			try {
				if(choice=="")
					throw new EmptyInputException("Choice");
				else if(choicePattObj.matcher(choice).matches())
	    			break;
				else
        			throw invalidExObj;
        	}
        	catch(InvalidInputException e) {
        		System.out.println();
        		e.invalid(choice+"", "choice");
        		System.out.println("Criteria - Only digit. Max length 2.");
        		System.out.println("Example: 4");
        	}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}
        	
			choice="";
			System.out.println();
		}
		return Integer.parseInt(choice);
	}
	
	public static String userValidate() throws InvalidInputException, EmptyInputException{
		String user="";
		while(user.equals("")) {
        	
        	System.out.print("Enter Username: ");
        	user = in.nextLine().trim();
        	try {
        		if(user.equals("")) {
            		throw new EmptyInputException("user");
        		}
        		else if(noSpaceAlphaNumPatt.matcher(user).matches()) {
        			break;
        		}
        		else
        			throw invalidExObj;
        	}
        	catch(InvalidInputException e) {
        		System.out.println();
        		e.invalid(user, "user");
        		System.out.println("Criteria - Only Alpha-Numerics. Minimum length 3.");
        		System.out.println("Example: abhishek04");
        	}
        	catch(EmptyInputException e) {}
        	catch(Exception e) {
				System.out.println(e);
			}
        	
    		user="";
    		System.out.println();
        }
		return user;
	}
	
	public static String passwordValidate() throws InvalidInputException, EmptyInputException{
		String password="";
		while(password.equals("")) {
        	
        	System.out.print("Enter Password (8 characters): ");
        	password = in.nextLine().trim();
        	try {
        		if(password.equals("")) {
            		throw new EmptyInputException("password");
        		}
        		else if(passwordPatt.matcher(password).matches()) {
        			break;
        		}
        		else
        			throw invalidExObj;
        	}
        	catch(InvalidInputException e) {
        		System.out.println();
        		e.invalid(password, "password");
        		System.out.println("Criteria - Minimum 1 lower-case alphabet, upper-case alphabet, digit, special character. Minimum length 8.");
        		System.out.println("Example: aB@hishek04");
        	}
        	catch(EmptyInputException e) {}
        	catch(Exception e) {
				System.out.println(e);
			}
        	
    		password="";
			System.out.println();
        }
		return password;
	}
	
	public static String nameValidate() throws InvalidInputException, EmptyInputException{
		String name="";
		while(name.equals("")) {
        	
        	System.out.print("Enter Name: ");
        	name = in.nextLine().trim().toLowerCase().replaceAll(" +", " ");
        	try {
        		if(name.equals("")) {
            		throw new EmptyInputException("name");
        		}
        		else if(minStringPatt.matcher(name).matches()) {
        			break;
        		}
        		else
        			throw invalidExObj;
        	}
        	catch(InvalidInputException e) {
        		System.out.println();
        		e.invalid(name, "name");
        		System.out.println("Criteria - Only alphabets. Minimum length 3.");
        		System.out.println("Example: abhishek pandey");
        	}
        	catch(EmptyInputException e) {}
        	catch(Exception e) {
				System.out.println(e);
			}
        	
    		name="";
			System.out.println();
        }
		return name;
	}
	
	public static String desigValidate(HashMap<String,Integer> desigMap, DBConnection connector) throws InvalidInputException, EmptyInputException{
		
		String designation="";
		while(designation.equals("")) {
        	
        	System.out.print("Enter Designation (ceo/manager/senior/associate/intern): ");
        	designation = in.nextLine().trim().toLowerCase();
        	
        	try {
        		if(designation.equals(""))
            		throw new EmptyInputException("designation");
        		else if(desigMap.containsKey(designation)) {
        			
        			if(designation.equals("ceo")) {
        				Connection connObj=connector.connect();
            			PreparedStatement st=connObj.prepareStatement(DBQueryUtil.checkCEO());
    					ResultSet rs=st.executeQuery();

    					connObj.close();
    					
    					if(rs.next()==true) {
    						System.out.println("! CEO already exists. !");
    					}
    					else
    						break;
        			}
        			else
        				break;
        		}
        		else
        			throw invalidExObj;
        	}
        	catch(InvalidInputException e){
        		System.out.println();
        		e.invalid(designation, "designation");
        		System.out.println("Criteria - Must be from among given options.");
        		System.out.println("Example: intern");
        	}
        	catch(EmptyInputException e) {}
        	catch(Exception e) {
				System.out.println(e);
			}

    		designation="";
			System.out.println();
        }
		return designation;
	}
	
	public static String mailValidate() {
		String mailID="";
		
		while(mailID=="") {
			System.out.print("Enter Personal Mail ID: ");
			mailID = in.nextLine().trim().toLowerCase();
			
			try {
				if(mailID.equals(""))
					throw new EmptyInputException("mail ID");
				else if(mailPattObj.matcher(mailID).matches()) {
            		break;
        		}
        		else
        			throw invalidExObj;
			}
			catch(InvalidInputException e){
				System.out.println();
        		e.invalid(mailID, "Mail ID");
        		System.out.println("Criteria - Format: 'xx@xx.xx'");
        		System.out.println("Example: abhishek@gmail.co");
        	}
			catch(EmptyInputException e) {}
        	catch(Exception e) {
				System.out.println(e);
			}
			
    		mailID="";
			System.out.println();
		}
		return mailID;
	}
	
	public static int maritalStatusValidate(HashMap<String,Integer> maritalStatusMap) {
		
		String maritalStatus="";
		
		while(maritalStatus=="") {
			System.out.print("Married? (yes/no): ");
			maritalStatus = in.nextLine().trim().toLowerCase();
			
			try {
				if(maritalStatus.equals("y")) {
					maritalStatus+="es";
				}
				if(maritalStatus.equals("n")) {
					maritalStatus+="o";
				}
				
				if(maritalStatus.equals(""))
					throw new EmptyInputException("Marital Status");
				else if(maritalStatusMap.containsKey(maritalStatus))
                    break;
        		else
        			throw invalidExObj;
			}
			catch(InvalidInputException e){
				System.out.println();
        		e.invalid(maritalStatus, "Marital Status");
        		System.out.println("Criteria - Must be from among given options.");
        		System.out.println("Example: no");
        	}
			catch(EmptyInputException e) {}
        	catch(Exception e) {
				System.out.println(e);
			}

    		maritalStatus="";
			System.out.println();
		}
		return maritalStatus.equals("yes")?1:0;
	}
	
	public static String deptValidate(HashSet<String> deptSet) {
		
		String department="";
		
		while(department=="") {
			System.out.print("Enter Department (HR/IT/BA/DEV): ");
			department = in.nextLine().trim().toLowerCase();
			
			try {
				if(department.equals("")) {
					throw new EmptyInputException("Department");
				}
				else if(deptSet.contains(department)) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(department, "Department");
				System.out.println("Criteria - Must be from among given options.");
				System.out.println("Example: HR");
			}
			catch(EmptyInputException e) {}
        	catch(Exception e) {
				System.out.println(e);
			}

			department="";
			System.out.println();
		}
		return department;
	}
	
	public static Long salaryValidate() {
		String salary="";
		
		while(salary=="") {
			System.out.print("Enter Annual salary (INR): ");
			salary = in.nextLine().trim();
			try {
				if(salary.equals("")) {
					throw new EmptyInputException("Salary");
				}
				else if(salaryPattObj.matcher(salary).matches()) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(salary, "Salary");
				System.out.println("Criteria - Only digits. Maximum Length 10.");
				System.out.println("Example: 9005000");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}
			salary="";
			System.out.println();
		}
		return Long.parseLong(salary);
	}

	public static String genderValidate(HashSet<String> genderSet) {
		String gender="";
		
		while(gender=="") {
			System.out.print("Enter Gender (male/female/lgbtq): ");
			gender = in.nextLine().trim().toLowerCase();
			
			try {
				if(gender.equals("")) {
					throw new EmptyInputException("Gender");
				}
				else if(genderSet.contains(gender)) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(gender, "Gender");
				System.out.println("Criteria - Must be from among given options.");
				System.out.println("Example: male");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}

			gender="";
			System.out.println();
		}
		return gender;
	}
	
	public static String officeLocValidate(HashSet<String> officeLocSet) {
		String loc="";
		while(loc.equals("")) {
			System.out.print("Enter Office Location (DE/MU/BA/KO/PU): ");
			loc = in.nextLine().trim().toLowerCase();
			
			try {
				if(loc.equals("")) {
					throw new EmptyInputException("Office Location");
				}
				else if(officeLocSet.contains(loc)) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(loc, "Office Location");
				System.out.println("Criteria - Must be from among given options.");
				System.out.println("Example: MU");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}
			loc="";
			System.out.println();
		}
		return loc;
	}
	
	
	public static String contactValidate() {
		String contact="";
		while(contact.equals("")) {
        	
        	System.out.print("Enter Contact: ");
        	contact = in.nextLine().trim().toLowerCase();
        	try {
        		if(contact.equals("")) {
            		throw new EmptyInputException("contact");
        		}
        		else if(contactPattObj.matcher(contact).matches()) {
        			contact=contact.substring(contact.length() - 10);
        			break;
        		}
        		else
        			throw invalidExObj;
        	}
        	catch(InvalidInputException e) {
        		System.out.println();
        		e.invalid(contact, "contact");
        		System.out.println("Criteria - Begin with '6789'. Length 10. Only Digits. Only Indian code (+91).");
        		System.out.println("Example: '6789728990' or '+91 6789728990' or 916789728990");
        	}
        	catch(EmptyInputException e) {}
        	catch(Exception e) {
				System.out.println(e);
			}
        	contact="";
			System.out.println();
        }
		return contact;
	}
	
	public static java.sql.Date dobValidate() {
		String date="";
		Date dateObj=null;
		java.sql.Date dateSQL=null;
		
		while(date.equals("")) {
        	
        	System.out.print("Enter dob (yyyy-mm-dd): ");
        	date = in.nextLine().trim().toLowerCase();
			date = date.replace('/', '-').replace('.', '-');
        	try {
        		if(date.equals("")) {
            		throw new EmptyInputException("DOB");
        		}
        		else if(datePattObj.matcher(date).matches()) {
        			String dateArr[]=date.split("-");
        			int year = Integer.parseInt(dateArr[0]);
        			
        			//validation for age 16 to 95
        			if(year >= (currentYear-95) && year <= (currentYear-16)) {
        				
        				//provide date format
            			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            			
            			//parsed into java.util.date
            			dateObj=dateFormat.parse(date);
            			
            			//convert java.util.date into java.sql.date
            			dateSQL=new java.sql.Date(dateObj.getTime());
            			break;
        			}
        			else {
        				throw invalidExObj;
        			}
        		}
        		else {
        			throw invalidExObj;
        		}
        	}
        	catch(InvalidInputException e) {
        		System.out.println();
        		e.invalid(date, "DOB");
        		System.out.println("Criteria - Age between 16 to 95. Format: YYYY-MM-DD");
        		System.out.println("Example: 2000-12-01");
        	}
        	catch(EmptyInputException e) {}
        	catch(ParseException e) {
        		System.out.println("Could not parse inserted date.");
        	}
        	catch(Exception e) {
				System.out.println(e);
			}
        	date="";
			System.out.println();
        }
		return dateSQL;
	}
	
	public static java.sql.Date dojValidate(String DOB) {
		String date="";
		Date dateObj=null;
		java.sql.Date dateSQL=null;
		
		while(date.equals("")) {
        	
        	System.out.print("Enter doj (yyyy-mm-dd): ");
        	date = in.nextLine().trim().toLowerCase();
        	date = date.replace('/', '-').replace('.', '-');
			
        	try {
        		if(date.equals("")) {
            		throw new EmptyInputException("DOJ");
        		}
        		else if(datePattObj.matcher(date).matches()) {
        			
        			String DOJArr[]=date.split("-");
        			int DOJyear = Integer.parseInt(DOJArr[0]);
        			int DOJmonth = Integer.parseInt(DOJArr[1]);
//        			int DOJdate = Integer.parseInt(DOJArr[2]);
        			
        			String DOBArr[]=DOB.split("-");
        			int DOByear = Integer.parseInt(DOBArr[0]);
        			
        			if((DOJyear >= (currentYear-95) && DOJyear < currentYear && (DOByear+16) <= DOJyear) || ((DOByear+16) <= DOJyear && DOJyear == currentYear && currentMonth >= DOJmonth)) {
        				
            			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            			dateObj=dateFormat.parse(date);
            			dateSQL=new java.sql.Date(dateObj.getTime());
            			
            			break;
        			}
        			else {
        				throw invalidExObj;
        			}
        		}
        		else
        			throw invalidExObj;
        	}
        	catch(InvalidInputException e) {
        		System.out.println();
        		e.invalid(date, "DOJ");
        		System.out.println("Criteria - Date within 95 years. DOB should be less than DOJ. Format: YYYY-MM-DD");
        		System.out.println("Example: 2018-11-23");
        	}
        	catch(EmptyInputException e) {}
        	catch(ParseException e) {
        		System.out.println("Could not parse inserted date.");
        	}
        	catch(Exception e) {
				System.out.println(e);
			}
        	date="";
			System.out.println();
        }
		return dateSQL;
	}
	
	public static String bankNameValidate() {
		String bankName="";
		while(bankName=="") {
			System.out.print("Enter Bank Name: ");
			bankName = in.nextLine().trim().replaceAll(" +", " ");
			try {
				if(bankName.equals("")) {
					throw new EmptyInputException("Bank Name");
				}
				else if(minStringPatt.matcher(bankName).matches()) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(bankName, "Bank name");
        		System.out.println("Criteria - Only alphabets. Minimum length 3.");
        		System.out.println("Example: ICICI");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}
			bankName="";
			System.out.println();
		}
		return bankName;
	}
	
	public static String accNumValidate() {
		String accNum="";
		while(accNum=="") {
			System.out.print("Enter Account Number: ");
			accNum = in.nextLine().trim();
			try {
				if(accNum.equals("")) {
					throw new EmptyInputException("Account Number");
				}
				else if(accNumPattObj.matcher(accNum).matches()) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(accNum, "Account Number");
        		System.out.println("Criteria - Only digits. Length 9-18.");
        		System.out.println("Example: 6790050010");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}
			accNum="";
			System.out.println();
		}
		return accNum;
	}
	
	public static String branchValidate() {
		String branch="";
		while(branch=="") {
			System.out.print("Enter branch: ");
			branch = in.nextLine().trim().replaceAll(" +", " ");
			try {
				if(branch.equals("")) {
					throw new EmptyInputException("branch");
				}
				else if(minAlphaNumPatt.matcher(branch).matches()) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(branch, "Branch Name");
        		System.out.println("Criteria - Only Alpha Numerics. Minimum Length 3.");
        		System.out.println("Example: Sector 3");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}
			branch="";
			System.out.println();
		}
		return branch;
	}
	
	public static String accTypeValidate(HashSet<String> accTypeSet) {
		String accType="";

		while(accType=="") {
			System.out.print("Enter Account Type (savings/current/salary): ");
			accType = in.nextLine().trim().toLowerCase();
			
			try {
				
				if(accType=="saving"){
					accType=accType+"s";
				}
				
				if(accType.equals("")) {
					throw new EmptyInputException("Account Type");
				}
				else if(accTypeSet.contains(accType)) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(accType, "Account Type");
        		System.out.println("Criteria - Must be from among given options.");
        		System.out.println("Example: savings");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}

			accType="";
			System.out.println();
		}
		return accType;
	}
	
	public static String IFSCValidate() {
		String IFSC="";
		while(IFSC=="") {
			System.out.print("Enter IFSC: ");
			IFSC = in.nextLine().trim().toUpperCase();
			try {
				if(IFSC.equals("")) {
					throw new EmptyInputException("IFSC");
				}
				else if(IFSCPattObj.matcher(IFSC).matches()) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(IFSC, "IFSC");
				System.out.println("Criteria - Format: 4 alphabets, followed by 0, followed by 6 digits.");
				System.out.println("Example: ICIC0193678");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}
			IFSC="";
			System.out.println();
		}
		return IFSC;
	}
	
	public static String qualNameValidate(HashSet<String> qualNameSet) {
		String qualName="";

		while(qualName=="") {
			System.out.print("Enter Qualification name (10th/12th/undergrad/postgrad): ");
			qualName = in.nextLine().trim().toLowerCase();

			try {
				if(qualName.equals("")) {
					throw new EmptyInputException("Qualification Name");
				}
				else if(qualNameSet.contains(qualName)) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(qualName, "Qualification Name");
        		System.out.println("Criteria - Must be from among given options.");
        		System.out.println("Example: 10th");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}

			qualName="";
			System.out.println();
		}
		return qualName;
	}
	
	public static String passYearValidate() {
		String passYear="";

		while(passYear=="") {
			System.out.print("Enter Passing Year: ");
			passYear = in.nextLine().trim().toLowerCase();

			try {
				if(passYear.equals("")) {
					throw new EmptyInputException("Pass Year");
				}
				else if(yearPattObj.matcher(passYear).matches()) {
					int passYearInt = Integer.parseInt(passYear);
					if(passYearInt>currentYear-85 && passYearInt<=currentYear) 
						break;
					else
						throw invalidExObj;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(passYear, "Passing Year");
        		System.out.println("Criteria - Format: YYYY. Between 85 to current year.");
        		System.out.println("Example: 1989");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}

			passYear="";
			System.out.println();
		}
		return passYear;
	}
	public static String orgNameValidate() {
		String orgName="";

		while(orgName=="") {
			System.out.print("Enter Institute Name: ");
			orgName = in.nextLine().trim().replaceAll(" +", " ");

			try {
				if(orgName.equals("")) {
					throw new EmptyInputException("Institute Name");
				}
				else if(minStringPatt.matcher(orgName).matches()) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(orgName, "Institute Name");
        		System.out.println("Criteria - Minimum length 3. Only alphabets.");
        		System.out.println("Example: DPS");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}

			orgName="";
			System.out.println();
		}
		return orgName;
	}
	public static Float resultValidate() {
		String result="";

		while(result=="") {
			System.out.print("Enter Result: ");
			result = in.nextLine().trim().toLowerCase();

			try {
				if(result.equals("")) {
					throw new EmptyInputException("Result");
				}
				else if(markPattObj.matcher(result).matches()) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(result, "Result");
        		System.out.println("Criteria - Between 0 & 100. Only 2 decimal places.");
        		System.out.println("Example: 98.50 or 60");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}

			result="";
			System.out.println();
		}
		return Float.parseFloat(result);
	}
	public static int newUserPrivilegeValidate() {
		String newUserPrivilege="";
		while(newUserPrivilege=="") {
			System.out.print("Enter User Privilege (1:admin/0:general): ");
			newUserPrivilege = in.nextLine().trim();
			try {
				if(newUserPrivilege.equals("")) {
					throw new EmptyInputException("User Privilege");
				}
				else if(binaryPattObj.matcher(newUserPrivilege).matches()) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(newUserPrivilege, "User Privilege");
        		System.out.println("Criteria - Either 0 or 1.");
        		System.out.println("Example: 1");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}

			newUserPrivilege="";
			System.out.println();
		}
		return Integer.parseInt(newUserPrivilege);
	}
	
	public static String requesteeValidate(HashSet<String> userRequestSet) {
		String selectedUser="";
		while(selectedUser.equals("")) {
			System.out.print("Enter Employee ID to grant privilges: ");
			selectedUser = in.nextLine().trim();
			
			try {
				if(selectedUser.equals("")) {
					throw new EmptyInputException("Employee ID");
				}
				else if(userRequestSet.contains(selectedUser)) {
					break;
				}
				else
					throw invalidExObj;
			}
			catch(InvalidInputException e) {
				System.out.println();
				e.invalid(selectedUser, "EMployee ID");
				System.out.println("Criteria - Must be from among listed requestees.");
			}
			catch(EmptyInputException e) {}
			catch(Exception e) {
				System.out.println(e);
			}

			selectedUser="";
			System.out.println();
		}
		return selectedUser;
	}
}
