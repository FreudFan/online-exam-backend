package server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import datasource.ConnectionManager;
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

public class Tomcat8 {
	static public ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	    public static void main(String[] args) throws Exception {
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
	        connector.setPort(8888);
	        connector.setMaxPostSize(1024*5);
	        connector.setEnableLookups(false);
	        connector.setAllowTrace(false);
	        connector.setURIEncoding("UTF-8");



	        //connector.setAttribute(name, value);

	        File appdir=new File("webapp");//一定要绝对路径，不然无法启动
	        String context_path="/demo";
	        Context context =tomcat.addWebapp(context_path, appdir.getAbsolutePath());

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

			initDatabaseConnectionPool();	//初始化连接池

//	        Valve[] vs=service.getContainer().getPipeline().getValves();
//	        System.out.println(vs);
	        server.start();

	        System.out.println("started tomcat at port="+connector.getPort()+" , for webapp ["+context.getName()+"]");

	        System.out.println("tomcat workdir="+tmpdir.toString());

	        server.await();
	    }
	    
	    private static Valve loadAccessLog()
	    {
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
	    
	    private static boolean setFieldValue(String fieldName, Object obj, Object v) {
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
	    
	    private static boolean setFieldValue1(String name, Object obj,Object value) 
	    { 
	 		Class tc=obj.getClass();
	 		 try{
	 			 Field f=tc.getField(name);
	 			 f.setAccessible(true);
	 		     f.set(obj, value);
	 		     return true;
	 		     
	 		}catch(Throwable t)
	 		{
	 			System.out.println(t.getMessage());
	 			return false;
	 		}
	    }
	 	
	    
	    private static Class getFieldType(String fieldName, Class objc) {
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
	 			//System.out.println(t.getMessage()+" not found type!");
	 		}
	 		
	 		return null; 
	    } 

	    private static void initDatabaseConnectionPool() {
			ConnectionManager.getConnection();
		}

}
