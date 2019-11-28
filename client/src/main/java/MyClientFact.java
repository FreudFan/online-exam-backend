import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jaxrs.JAXRSContract;
import service.UserService;

public class MyClientFact {
	static MyClientFact inst=new MyClientFact();
//	static String base_url="http://139.9.179.236:8888/demo/rest";
	static String base_url="http://localhost:8888/demo/rest";
	
	public void setUrl(String u){
		base_url = u;
	}
	
	public static MyClientFact getInstance()
	{
		return inst;
	}
	
	public UserService getUserService() {
		UserService client1 = Feign.builder()
				.contract(new JAXRSContract())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
				.target(UserService.class, base_url);	
		
		return client1;
	}
}
