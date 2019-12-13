package server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.catalina.Context;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.Valve;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tomcat8 implements Runnable {
	static public ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
	private final static Logger LOGGER = LoggerFactory.getLogger(Tomcat8.class);

	private static Integer PORT = 8888;	//服务端口号
	private static String CONTEXT_PATH = "";	//服务url根路径 /demo
	private static String PROJECT_PATH = System.getProperty("user.dir");// 工程物理的绝对路径
	private static String WEB_APP_PATH = PROJECT_PATH + File.separatorChar
			+ "server/src/main/resources/webapp";

	private void init() throws Exception {
		File tmpdir=Files.createTempDirectory("tomcat-temp").toFile();
		//tmpdir=new File("./tmp");
		Tomcat tomcat = new Tomcat();
		//tomcat.setBaseDir(new File("./tmp").getAbsolutePath());
		tomcat.setBaseDir(tmpdir.getAbsolutePath());

		Server server = tomcat.getServer();

		// server.setPort(8080);
		Service service = tomcat.getService();
		service.setName("Tomcat-embbeded-opt");

		Connector connector = tomcat.getConnector();
		connector.setPort(PORT);
		connector.setMaxPostSize(1024*5);
		connector.setEnableLookups(false);
		connector.setAllowTrace(false);
		connector.setURIEncoding(String.valueOf(StandardCharsets.UTF_8));

		//connector.setAttribute(name, value);

//	        File appdir=new File("webapp");//一定要绝对路径，不然无法启动
//	        String context_path="/";
		Context context =tomcat.addWebapp(CONTEXT_PATH, WEB_APP_PATH);

		StandardContext ctx=(StandardContext )context;
		WebResourceRoot resources = new StandardRoot(ctx);

//	        WebResourceSet resourceSet;
//	        File webdir=appdir;
//	        resourceSet = new DirResourceSet(resources,
//	        		"/WEB-INF",
//	        		webdir.getAbsolutePath(), "/");
//	        System.out.println("loading WEB-INF resources from as '" + webdir.getAbsolutePath() + "'");
//
//	        resources.addPreResources(resourceSet);
		ctx.setResources(resources);


		Valve log=loadAccessLog();
		service.getContainer().getPipeline().addValve(log);

//	        Valve[] vs=service.getContainer().getPipeline().getValves();
//	        System.out.println(vs);
		server.start();

		LOGGER.info("started tomcat at port="+connector.getPort()+" , for webapp ["+context.getName()+"]");

		LOGGER.info("tomcat workdir="+tmpdir.toString());
		LOGGER.info("tomcat workurl= " + "http://localhost:" + PORT + CONTEXT_PATH);

		server.await();
	}

	private Valve loadAccessLog() {
		String path=Tomcat8.class.getPackage().getName().replace('.','/');
		String fileName=path+"/"+"accesslog.prop";

		AccessLogValve log= new org.apache.catalina.valves.AccessLogValve();

		Properties p=new Properties();
		try {
			p.load(log.getClass().getClassLoader().getResourceAsStream(fileName));
			p.keySet().forEach(name->{
				String field=name+"";
				String v=p.getProperty(field)+"";
				setFieldValue(field, log, v.trim());

			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return log;
	}

	private boolean setFieldValue(String fieldName, Object obj, Object v) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "set" + firstLetter + fieldName.substring(1);
			Method method = obj.getClass().getMethod(getter, new Class[] {getFieldType(fieldName,obj.getClass())});
			method.invoke(obj, new Object[] {v});
			return true;
		} catch (Throwable e) {
			//log.error(e.getMessage(),e);
			return setFieldValue1(fieldName,obj,v);
		}
	}

	private boolean setFieldValue1(String name, Object obj,Object value)	{
		Class tc=obj.getClass();
		try{
			Field f=tc.getField(name);
			f.setAccessible(true);
			f.set(obj, value);
			return true;

		}catch(Throwable t)
		{
			LOGGER.error(t.getMessage());
			return false;
		}
	}

	private Class getFieldType(String fieldName, Class objc) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = objc.getMethod(getter, new Class[] {});
			return method.getReturnType();
		} catch (Throwable e) {
			//log.error(e.getMessage(),e);

		}

		try{
			Field f=objc.getField(fieldName);
			return f.getType();
		}catch(Throwable t)
		{
			//LOGGER.error(t.getMessage()+" not found type!");
		}

		return null;
	}

	@Override
	public void run() {
		try {
			this.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
