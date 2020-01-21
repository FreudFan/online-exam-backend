package edu.sandau.tomcat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

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
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class Tomcat8 implements Runnable {
	private final static Logger LOGGER = LoggerFactory.getLogger(Tomcat8.class);

	public Tomcat8() {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("application.properties");
		if( is != null ) {
			Properties p = new Properties();
			try {
				p.load(is);
			} catch (IOException e) {
				LOGGER.error("无法加载application.properties文件。。。。。。");
				e.printStackTrace();
			}
			String port = p.getProperty("sever.port","8080");
			this.port = NumberUtils.toInt(port);
		}
	}

	/***
	 * 服务端口号
	 */
	private Integer port;

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
		connector.setPort(port);
		connector.setMaxPostSize(1024*5);
		connector.setEnableLookups(false);
		connector.setAllowTrace(false);
		connector.setURIEncoding(String.valueOf(StandardCharsets.UTF_8));

		/***
		 * 服务url根路径 /demo
		 */
		String contextPath = "";
		// webapp 目录
		ClassPathResource resource = new ClassPathResource("webapp");
		String webAppPath = URLDecoder.decode(resource.getURL().toString().substring(6), "UTF-8");

		Context context = tomcat.addWebapp(contextPath, webAppPath);
		StandardContext ctx=(StandardContext )context;
		WebResourceRoot resources = new StandardRoot(ctx);

		ctx.setResources(resources);

		Valve log=loadAccessLog();
		service.getContainer().getPipeline().addValve(log);

		server.start();

		LOGGER.info("started tomcat at port=" + connector.getPort() + " , for webapp [" + context.getName() + "]");

		LOGGER.info("tomcat workdir="+tmpdir.toString());
		LOGGER.info("tomcat workurl= " + "http://localhost:" + port + contextPath);

		server.await();
	}

	private Valve loadAccessLog() {
		AccessLogValve log= new org.apache.catalina.valves.AccessLogValve();

		Properties p = new Properties();
		try {
			p.load(log.getClass().getClassLoader().getResourceAsStream("accesslog.prop"));
			p.keySet().forEach(name->{
				String field=name+"";
				String v=p.getProperty(field)+"";
				setFieldValue(field, log, v.trim());
			});
		} catch (IOException e) {
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
