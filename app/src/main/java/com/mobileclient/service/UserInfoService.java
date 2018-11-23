package com.mobileclient.service;

import android.util.Log;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.UserInfo;
import com.mobileclient.util.HttpUtil;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*用户管理业务逻辑层*/
public class UserInfoService {
	/* 添加用户 */
	public String AddUserInfo(UserInfo userInfo) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("user_name",userInfo.getUser_name())
					.add("password", userInfo.getPassword())
					.add("userType", userInfo.getUserType())
					.add("name", userInfo.getName())
					.add("gender", userInfo.getGender())
					.add("birthDate", userInfo.getBirthDate().toString())
					.add("userPhoto", userInfo.getUserPhoto())
					.add("telephone", userInfo.getTelephone())
					.add("email", userInfo.getEmail())
					.add("address", userInfo.getAddress())
					.add("authFile", userInfo.getAuthFile())
					.add("shenHeState", userInfo.getShenHeState())
					.add("regTime", userInfo.getRegTime())
					.add("action", "add")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "UserInfoServlet?").post(body).build();
			Response reponse=client.newCall(request).execute();
			String result=reponse.body().string();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询用户 */
	public List<UserInfo> QueryUserInfo(UserInfo queryConditionUserInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "UserInfoServlet?action=query";
		if(queryConditionUserInfo != null) {
			urlString += "&user_name=" + URLEncoder.encode(queryConditionUserInfo.getUser_name(), "UTF-8") + "";
			urlString += "&userType=" + URLEncoder.encode(queryConditionUserInfo.getUserType(), "UTF-8") + "";
			urlString += "&name=" + URLEncoder.encode(queryConditionUserInfo.getName(), "UTF-8") + "";
			if(queryConditionUserInfo.getBirthDate() != null) {
				urlString += "&birthDate=" + URLEncoder.encode(queryConditionUserInfo.getBirthDate().toString(), "UTF-8");
			}
			urlString += "&telephone=" + URLEncoder.encode(queryConditionUserInfo.getTelephone(), "UTF-8") + "";
			urlString += "&shenHeState=" + URLEncoder.encode(queryConditionUserInfo.getShenHeState(), "UTF-8") + "";
		}
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				UserInfo userInfo = new UserInfo();
				userInfo.setUser_name(object.getString("user_name"));
				userInfo.setPassword(object.getString("password"));
				userInfo.setUserType(object.getString("userType"));
				userInfo.setName(object.getString("name"));
				userInfo.setGender(object.getString("gender"));
				userInfo.setBirthDate(Timestamp.valueOf(object.getString("birthDate")));
				userInfo.setUserPhoto(object.getString("userPhoto"));
				userInfo.setTelephone(object.getString("telephone"));
				userInfo.setEmail(object.getString("email"));
				userInfo.setAddress(object.getString("address"));
				userInfo.setAuthFile(object.getString("authFile"));
				userInfo.setShenHeState(object.getString("shenHeState"));
				userInfo.setRegTime(object.getString("regTime"));
				userInfoList.add(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userInfoList;
	}

	/* 更新用户 */
	public String UpdateUserInfo(UserInfo userInfo) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("user_name", userInfo.getUser_name())
					.add("password", userInfo.getPassword())
					.add("userType", userInfo.getUserType())
					.add("name", userInfo.getName())
					.add("gender", userInfo.getGender())
					.add("birthDate", userInfo.getBirthDate().toString())
					.add("userPhoto", userInfo.getUserPhoto())
					.add("telephone", userInfo.getTelephone())
					.add("email", userInfo.getEmail())
					.add("address", userInfo.getAddress())
					.add("authFile", userInfo.getAuthFile())
					.add("shenHeState", userInfo.getShenHeState())
					.add("regTime", userInfo.getRegTime())
					.add("action", "update")
					.build();

			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "UserInfoServlet?").post(body).build();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "UserInfoServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			Response reponse=client.newCall(request).execute();
			String result=reponse.body().string();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除用户 */
	public String DeleteUserInfo(String user_name) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("user_name", user_name)
					.add("action", "delete")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "UserInfoServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "用户信息删除失败!";
		}
	}

	/* 根据用户名获取用户对象 */
	public UserInfo GetUserInfo(String user_name)  {
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		//params.put("user_name", user_name);
		//params.put("action", "updateQuery");
		byte[] resultByte;

		try {
			OkHttpClient client=new OkHttpClient();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "UserInfoServlet?", params, "UTF-8");
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "UserInfoServlet?"+"user_name="+user_name+"&action=updateQuery").build();
			//String result = new String(resultByte, "UTF-8");
			Response response=client.newCall(request).execute();
			String reponseData=response.body().string();
			Log.i("qqqqqqqq",""+reponseData);
			JSONArray array = new JSONArray(reponseData);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				UserInfo userInfo = new UserInfo();
				userInfo.setUser_name(object.getString("user_name"));
				userInfo.setPassword(object.getString("password"));
				userInfo.setUserType(object.getString("userType"));
				userInfo.setName(object.getString("name"));
				userInfo.setGender(object.getString("gender"));
				userInfo.setBirthDate(Timestamp.valueOf(object.getString("birthDate")));
				userInfo.setUserPhoto(object.getString("userPhoto"));
				userInfo.setTelephone(object.getString("telephone"));
				userInfo.setEmail(object.getString("email"));
				userInfo.setAddress(object.getString("address"));
				userInfo.setAuthFile(object.getString("authFile"));
				userInfo.setShenHeState(object.getString("shenHeState"));
				userInfo.setRegTime(object.getString("regTime"));
				userInfoList.add(userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = userInfoList.size();
		if(size>0) return userInfoList.get(0); 
		else return null; 
	}
}
