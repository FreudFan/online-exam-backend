package server;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClsUtil {
	
	static DecimalFormat df4=null;
	static DecimalFormat df2=null;	
	static DecimalFormat df1=null;
	
	public static List reverse(List list)
	{
		List rlist=new ArrayList(list);
		java.util.Collections.reverse(rlist);
		return rlist;		
	}

	public static String format1(double d)
	{
		if(null==df1)
		{
		  df1=new DecimalFormat("0.0");	
		  df1.setMaximumFractionDigits(1);
		}
		return df1.format(d);
	}
	
	public static String format2(double d)
	{
		  if(null==df2)
		  {
			  df2=new DecimalFormat("0.00");
			  df2.setMaximumFractionDigits(2);
		  }
		  return df2.format(d);
	}
	
	public static String format4(double d)
	{
		  if(null==df4)
		  {
			  df4=new DecimalFormat("0.0000");
			  df4.setMaximumFractionDigits(4);
		  }
		  return df4.format(d);
	}
	
	
   public static Object getFieldValue(String fieldName, Object obj) {  
       try {    
           String firstLetter = fieldName.substring(0, 1).toUpperCase();    
           String getter = "get" + firstLetter + fieldName.substring(1);    
           Method method = obj.getClass().getMethod(getter, new Class[] {});    
           Object value = method.invoke(obj, new Object[] {});    
           return value;    
       } catch (Throwable e) {    
           //log.error(e.getMessage(),e);    
           return getFieldValue1(fieldName,obj);    
       }    
   }  

   public static boolean setFieldValue(String fieldName, Object obj,Object v) {  
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

   public static Class getFieldType(String fieldName, Class objc) {  
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
   
   private static Object getFieldValue1(String name, Object obj) 
   { 
		Class tc=obj.getClass();
		Object r=null;
		 try{
			 Field f=tc.getField(name);
			 f.setAccessible(true);
		     r=f.get(obj);
		    //tc.getMethod(name, parameterTypes)
		    
		}catch(Throwable t)
		{
			//t.printStackTrace(); 
			//System.out.println(t.getMessage()+" not found!");			
		}
		 return r;
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
	
	

}
