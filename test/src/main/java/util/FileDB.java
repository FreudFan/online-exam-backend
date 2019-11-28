package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import entity.UserInfo;

public class FileDB {
//    private static final String CD = "D:\\AppDate\\";
	private static final String CD = "";
	
	/*
	 * users的文件读取方法
	 * */
	public static void saveUsers(Map<String, UserInfo> map) {
		Gson gson= new Gson();
		String json = gson.toJson(map);
		
		FileOutputStream fos = null;
		try{
			fos= new FileOutputStream(CD + "users.txt");
			fos.write(json.getBytes());
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				fos.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	//读取数据，如果文件不存在，返回new ArrayList
	public static Map<String, UserInfo> loadUsers() {
		Map<String, UserInfo> jsonListObject;
		String json = null;
		File file = new File(CD + "users.txt");
		if(!file.exists()) {
			jsonListObject = new HashMap<String, UserInfo>();
			return jsonListObject;
		}
		FileReader fr = null;
		BufferedReader br = null;
		try{
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			json = br.readLine();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
			br.close();
			fr.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		Gson gson= new Gson();
		jsonListObject = gson.fromJson(json,
				new TypeToken<Map<String, UserInfo>>(){}.getType());
		return jsonListObject;
	}
	
}
