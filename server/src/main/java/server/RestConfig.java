package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.glassfish.jersey.server.ResourceConfig;


public class RestConfig extends ResourceConfig {
    public RestConfig()
    {
    	super();
    	this.init();
    }
    
    public void init()
    {
    	System.out.print("load restful services......");
    	String packages="sd.order.rest";
    	packages=this.loadPackages();
    	System.out.println("in packages="+packages);
    	this.packages(packages);
    	// can add thread following
    	
    	//if(use_gzip){
      	    //this.register(GzipInterceptor.class); //支持压缩
      	    this.register(TestReaderIntercetor.class);
      	//}

      	    
    	
    }
    
    public String loadPackages()
    {
    	InputStream is=this.getClass().getClassLoader().getResourceAsStream("restconfig.properties");
    	if(is!=null)
    	{
    		Properties p=new Properties();
    		try {
				p.load(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("无法加载restconfig.properties文件。。。。。。");
				e.printStackTrace();
			}
    		String ret=p.getProperty("service_packages","sd.order.rest");
    		return ret;
    	}
    	return "sd.order";
    }
    
    public static class GzipInterceptor  implements WriterInterceptor {
        @Override
        public void aroundWriteTo(WriterInterceptorContext context)
                throws IOException, WebApplicationException {

        	
            MultivaluedMap<String, Object> headers = context.getHeaders();
            
            headers.add("Content-Encoding", "gzip");
            
            String ContentType = context.getMediaType().toString();
            
            headers.add("Content-Type",ContentType+";charset=utf-8");//解决乱码问题
            
            final OutputStream gzipStream = new GZIPOutputStream(context.getOutputStream());
            context.setOutputStream(gzipStream);
            context.proceed();
            
            gzipStream.close();
            
            //System.out.println("GZIP拦截器压缩");
        }
    }
    
    public static class TestReaderIntercetor implements ReaderInterceptor{

		@Override
		public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
			// TODO Auto-generated method stub
			MultivaluedMap<String, String> headers=context.getHeaders();
			
			java.util.Iterator<String>  keys=headers.keySet().iterator();
			System.out.println("request filter:{");
			while(keys.hasNext())
			{
				String key =keys.next();
				String v=headers.getFirst(key);
				System.out.println("     "+key+"="+v);
			}
			System.out.println("}");
			return context.proceed();
		}
    	
    }
}