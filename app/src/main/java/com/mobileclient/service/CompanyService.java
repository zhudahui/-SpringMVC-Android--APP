package com.mobileclient.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Company;
import com.mobileclient.util.HttpUtil;


import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*物流公司管理业务逻辑层*/
public class CompanyService {
	/* 添加物流公司 */
	public String AddCompany(Company company) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("companyId", company.getCompanyId() + "")
					.add("companyName", company.getCompanyName())
					.add("action", "add")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "CompanyServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CompanyServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询物流公司 */
	public List<Company> QueryCompany(Company queryConditionCompany) throws Exception {
		if(queryConditionCompany != null) {
		}
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Company> companyList = new ArrayList<Company>();
		byte[] resultByte;
		try {
			OkHttpClient client=new OkHttpClient();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "CompanyServlet?"+"action=query").build();
			//Log.i("zhuzhu","http://192.168.43.1:8080/JavaWebProject/CompanyServlet?"+"action=query");
			Response response=client.newCall(request).execute();
			String responseData=response.body().string();
			Log.i("huhui",""+responseData);
			JSONArray array = new JSONArray(responseData);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Company company = new Company();
				company.setCompanyId(object.getInt("companyId"));
				company.setCompanyName(object.getString("companyName"));
				companyList.add(company);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return companyList;
	}
	/* 更新物流公司 */
	public String UpdateCompany(Company company) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("companyId", company.getCompanyId() + "")
					.add("companyName", company.getCompanyName())
					.add("action", "update")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "CompanyServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CompanyServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除物流公司 */
	public String DeleteCompany(int companyId) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("companyId", companyId + "")
					.add("action", "delete")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "CompanyServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CompanyServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "物流公司信息删除失败!";
		}
	}

	/* 根据公司id获取物流公司对象 */
	public Company GetCompany(int companyId)  {
		List<Company> companyList = new ArrayList<Company>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("companyId", companyId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CompanyServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Company company = new Company();
				company.setCompanyId(object.getInt("companyId"));
				company.setCompanyName(object.getString("companyName"));
				companyList.add(company);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = companyList.size();
		if(size>0) return companyList.get(0); 
		else return null; 
	}
}
