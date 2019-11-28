import entity.UserInfo;
import service.UserService;

public class LoginTest {
	
	public static UserService us = MyClientFact.getInstance().getUserService();
	
	public static void main(String[] args) {
		
		login();
	}
	// 添加用户
	static void add_user() {
		us.addUser(UserInfo.CreateUser("张三", "test01", "1111", "10086", "test@test.com", "5D:FA:69:70"),
				100);
	}
	//	模拟登陆
	static void login() {
		UserInfo us2 =  us.login("test01", "1111");
		if(us2!=null) {
			System.out.println("登陆成功");
			System.out.println(us2.getMobile());
		}
		else {
			System.out.println("登陆失败");
		}
			
	}
}
