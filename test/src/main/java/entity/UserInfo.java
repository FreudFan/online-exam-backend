package entity;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
	private String userName;	//中文名
	private String userId;		//登陆id
	private String userPassword;//登陆密码
	private String mobile;		//手机号
	private String createTime;	//创建日期
	private String deviceNo;	//设备号
	private String email;		//邮箱
	
	private double money;		//钱，不一定用得上
	public List<Integer> orderIds;	//订单号们
	
	public UserInfo() {
		super();
	}
	//构造方法用CreateUser
	private UserInfo(String userName, String userId, String userPassword,
			String mobile, String deviceNo, String email) {
		super();
		this.userName = userName;
		this.userId = userId;
		this.userPassword = userPassword;
		this.mobile = mobile;
		this.deviceNo = deviceNo;
		this.email = email;
		
		orderIds = new ArrayList<Integer>();
		//createDate在服务器中设置
	}


	public static UserInfo CreateUser(String userName, String userId,String userPassword,
			String mobile, String email ,String deviceNo) {
		UserInfo user = new UserInfo(userName,userId,userPassword,mobile,deviceNo,email);
		user.setMoney(0);
		return user;
	}
	
	
//	@Override
//	public boolean equals(Object obj) {
//		// TODO Auto-generated method stub
//		//自反性
//        if (this == obj) 
//        	return true;
//        //任何对象不等于null，比较是否为同一类型
//        if (!(obj instanceof UserInfo)) 
//        	return false;
//        //强制类型转换
//        UserInfo another = (UserInfo)obj;
//        //如果id一样就相等
//		if(this.userId.equals(another.getUserId()))
//			return true;
//				
//		return false;
//	}
	
	public static String LegalJudge(String userName, String userId,String userPassword,
			String mobile, String email) {
		if(!judgeUserName(userName).equals("ok"))
			return judgeUserName(userName);
		else if(!judgeUserId(userId).equals("ok"))
			return judgeUserId(userId);
		else if(!judgeUserPassword(userPassword).equals("ok"))
			return judgeUserPassword(userPassword);
		else if(!judgeMobile(mobile).equals("ok"))
			return judgeMobile(mobile);
		else if(!judgeEmail(email).equals("ok"))
			return judgeEmail(email);
		else
			return "ok";
	}
	private static String judgeUserName(String userName) {
		if(userName==null)	
			return "user name can not be empty!";
		else
			return "ok";
	}
	//id不能有中文
	private static String judgeUserId(String userId) {
		if(userId==null)	
			return "user id can not be empty!";
		else
			return "ok";
	}
	private static String judgeUserPassword(String userPassword) {
		if(userPassword==null)	
			return "password can not be empty!";
		else
			return "ok";
	}
	private static String judgeMobile(String mobile) {
		if(mobile==null)	
			return "mobile can not be empty!";
		else
			return "ok";
	}
	private static String judgeEmail(String email) {
		if(email==null)	
			return "email can not be empty!";
		else
			return "ok";
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public void addMoney(double add) {
		money += add;
	}
	public double getMoney() {
		return money;
	}

	public List<Integer> getOrderIds() {
		return orderIds;
	}

	public String getMobile() {
		return mobile;
	}


	public String getCreateTime() {
		return createTime;
	}


	public String getDeviceNo() {
		return deviceNo;
	}


	public String getEmail() {
		return email;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
