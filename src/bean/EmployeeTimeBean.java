package bean;
import java.sql.Time;

public class EmployeeTimeBean {
	private Time loginTime;
	private Time logoutTime;
	
	public EmployeeTimeBean(){
		loginTime=null;
		logoutTime=null;
	}

	public Time getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Time loginTime) {
		this.loginTime = loginTime;
	}

	public Time getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Time logoutTime) {
		this.logoutTime = logoutTime;
	}
}
