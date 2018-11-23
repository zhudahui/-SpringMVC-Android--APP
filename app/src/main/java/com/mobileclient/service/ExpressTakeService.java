package com.mobileclient.service;

import android.app.DownloadManager;
import android.util.Log;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import com.mobileclient.domain.ExpressTake;
import com.mobileclient.util.HttpUtil;


/*快递代拿管理业务逻辑层*/
public class ExpressTakeService {
	/* 添加快递代拿 */
	public String AddExpressTake(ExpressTake expressTake) {
		//HashMap<String, String> params = new HashMap<String, String>();
		//byte[] resultByte;
		try {
			OkHttpClient client=new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("orderId", expressTake.getOrderId() + "")
                    .add("taskTitle", expressTake.getTaskTitle())
                    .add("companyObj",expressTake.getCompanyObj() + "")
                    .add("waybill",expressTake.getWaybill())
                    .add("receiverName",expressTake.getReceiverName())
                    .add("telephone",expressTake.getTelephone())
                    .add("receiveMemo",expressTake.getReceiveMemo())
                    .add("takePlace",expressTake.getTakePlace())
                    .add("giveMoney", expressTake.getGiveMoney() + "")
                    .add("takeStateObj",expressTake.getTakeStateObj())
                    .add("userObj",expressTake.getUserObj())
                    .add("addTime",expressTake.getAddTime())
					.add("userPhoto",expressTake.getUserPhoto())
					.add("expressCompanyAdress",expressTake.getExpressCompanyAdress())
                    .add("action","add")
                    .build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "ExpressTakeServlet?").post(body).build();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ExpressTakeServlet?", params, "UTF-8");
			Response response=client.newCall(request).execute();
			String responseData=response.body().string();
            Log.i("zhu444",responseData);
			return responseData;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询快递代拿 */
	public List<ExpressTake> QueryExpressTake(ExpressTake queryConditionExpressTake) throws Exception {

		String urlString = HttpUtil.BASE_URL + "ExpressTakeServlet?action=query";
		if(queryConditionExpressTake != null) {

			urlString += "&taskTitle=" + URLEncoder.encode(queryConditionExpressTake.getTaskTitle(), "UTF-8") + "";
            Log.i("a1",""+urlString);
			urlString += "&companyObj=" + queryConditionExpressTake.getCompanyObj();
            Log.i("a2",""+urlString);
			urlString += "&waybill=" + URLEncoder.encode(queryConditionExpressTake.getWaybill(), "UTF-8") + "";
			Log.i("a3",""+urlString);
			urlString += "&receiverName=" + URLEncoder.encode(queryConditionExpressTake.getReceiverName(), "UTF-8") + "";
			Log.i("a4",""+urlString);
			urlString += "&telephone=" + URLEncoder.encode(queryConditionExpressTake.getTelephone(), "UTF-8") + "";
			Log.i("a5",""+urlString);
			urlString += "&takePlace=" + URLEncoder.encode(queryConditionExpressTake.getTakePlace(), "UTF-8") + "";
			Log.i("a6",""+urlString);
			urlString += "&takeStateObj=" + URLEncoder.encode(queryConditionExpressTake.getTakeStateObj(), "UTF-8") + "";
			Log.i("a7",""+queryConditionExpressTake.getUserObj());
            if(queryConditionExpressTake.getUserObj()!=null)
			urlString += "&userObj=" + URLEncoder.encode(queryConditionExpressTake.getUserObj(), "UTF-8") + "";
			if(queryConditionExpressTake.getAddTime()!=null)
			urlString += "&addTime=" + URLEncoder.encode(queryConditionExpressTake.getAddTime(), "UTF-8") + "";
			if(queryConditionExpressTake.getUserPhoto()!=null)
			urlString += "&userPhoto=" + URLEncoder.encode(queryConditionExpressTake.getUserPhoto(), "UTF-8") + "";
			if(queryConditionExpressTake.getExpressCompanyAdress()!=null)
			urlString += "&expressCompanyAdress=" + URLEncoder.encode(queryConditionExpressTake.getExpressCompanyAdress(), "UTF-8") + "";
		}
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<ExpressTake> expressTakeList = new ArrayList<ExpressTake>();
		byte[] resultByte;
		try {
			Log.i("zhu3333","baichi"+urlString);
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(urlString).build();
			Log.i("zhu3333","bai"+request);
            Response response=client.newCall(request).execute();
			Log.i("zhu3333","baichi"+response);
			//resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
            String result=response.body().string();
			Log.i("zhu2333",""+result);
			//String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				ExpressTake expressTake = new ExpressTake();
				expressTake.setOrderId(object.getInt("orderId"));
				expressTake.setTaskTitle(object.getString("taskTitle"));
				expressTake.setCompanyObj(object.getInt("companyObj"));
				expressTake.setWaybill(object.getString("waybill"));
				expressTake.setReceiverName(object.getString("receiverName"));
				expressTake.setTelephone(object.getString("telephone"));
				expressTake.setReceiveMemo(object.getString("receiveMemo"));
				expressTake.setTakePlace(object.getString("takePlace"));
				expressTake.setGiveMoney((float) object.getDouble("giveMoney"));
				expressTake.setTakeStateObj(object.getString("takeStateObj"));
				expressTake.setUserObj(object.getString("userObj"));
				expressTake.setAddTime(object.getString("addTime"));
				expressTake.setUserPhoto(object.getString("userPhoto"));
				expressTake.setExpressCompanyAdress(object.getString("expressCompanyAdress"));
				expressTakeList.add(expressTake);
			}
		} catch (Exception e) {
		    Log.i("zhu1","查询失败");
			e.printStackTrace();

		}
		return expressTakeList;
	}

	/* 更新快递代拿 */
	public String UpdateExpressTake(ExpressTake expressTake) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("orderId", expressTake.getOrderId() + "");
		params.put("taskTitle", expressTake.getTaskTitle());
		params.put("companyObj", expressTake.getCompanyObj() + "");
		params.put("waybill", expressTake.getWaybill());
		params.put("receiverName", expressTake.getReceiverName());
		params.put("telephone", expressTake.getTelephone());
		params.put("receiveMemo", expressTake.getReceiveMemo());
		params.put("takePlace", expressTake.getTakePlace());
		params.put("giveMoney", expressTake.getGiveMoney() + "");
		params.put("takeStateObj", expressTake.getTakeStateObj());
		params.put("userObj", expressTake.getUserObj());
		params.put("addTime", expressTake.getAddTime());
		//params.put("action", "update");
		byte[] resultByte;
		try {

            OkHttpClient client=new OkHttpClient();
            RequestBody body=new FormBody.Builder()
                    .add("orderId", expressTake.getOrderId() + "")
                    .add("taskTitle", expressTake.getTaskTitle())
                    .add("companyObj", expressTake.getCompanyObj() + "")
                    .add("waybill", expressTake.getWaybill())
                    .add("receiverName", expressTake.getReceiverName())
                    .add("telephone", expressTake.getTelephone())
                    .add("receiveMemo", expressTake.getReceiveMemo())
                    .add("takePlace", expressTake.getTakePlace())
                    .add("giveMoney", expressTake.getGiveMoney() + "")
                    .add("takeStateObj", expressTake.getTakeStateObj())
                    .add("userObj", expressTake.getUserObj())
                    .add("addTime", expressTake.getAddTime())
                    .add("action", "update")
                    .build();
            Request request=new Request.Builder().url(HttpUtil.BASE_URL + "ExpressTakeServlet?").post(body).build();
		    Response response=client.newCall(request).execute();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ExpressTakeServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
            String result=response.body().string();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除快递代拿 */
	public String DeleteExpressTake(int orderId) {
		try {
            OkHttpClient client=new OkHttpClient();
            RequestBody body=new FormBody.Builder()
                    .add("orderId", orderId + "")
                    .add("action", "delete")
                    .build();
            Request request=new Request.Builder().url(HttpUtil.BASE_URL + "ExpressTakeServlet?").post(body).build();
            Response response=client.newCall(request).execute();
            String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ExpressTakeServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "快递代拿信息删除失败!";
		}
	}



	/* 根据订单id获取快递代拿对象 */
	public ExpressTake SearchExpressTake(int orderId)  {
		List<ExpressTake> expressTakeList = new ArrayList<ExpressTake>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("orderId", String.valueOf(orderId));
		//Log.i("34566wo",);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			Log.i("mmm",""+HttpUtil.BASE_URL + "ExpressTakeServlet?");
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ExpressTakeServlet?", params, "UTF-8");
			OkHttpClient client=new OkHttpClient();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "ExpressTakeServlet?"+"orderId="+orderId+"&action=updateQuery").build();
			Log.i("qqqqqqqq",HttpUtil.BASE_URL + "ExpressTakeServlet?"+"orderId="+orderId+"&action=updateQuery");
			Response response=client.newCall(request).execute();
			String repondeData=response.body().string();
			JSONArray jsonArray=new JSONArray(repondeData);
			for (int i = 0; i <jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				ExpressTake expressTake = new ExpressTake();
				expressTake.setOrderId(object.getInt("orderId"));
				expressTake.setTaskTitle(object.getString("taskTitle"));
				expressTake.setCompanyObj(object.getInt("companyObj"));
				expressTake.setWaybill(object.getString("waybill"));
				expressTake.setReceiverName(object.getString("receiverName"));
				expressTake.setTelephone(object.getString("telephone"));
				expressTake.setReceiveMemo(object.getString("receiveMemo"));
				expressTake.setTakePlace(object.getString("takePlace"));
				expressTake.setGiveMoney((float) object.getDouble("giveMoney"));
				expressTake.setTakeStateObj(object.getString("takeStateObj"));
				expressTake.setUserObj(object.getString("userObj"));
				expressTake.setAddTime(object.getString("addTime"));
				expressTakeList.add(expressTake);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("34566wo","查询错误");
		}
		int size = expressTakeList.size();
		if(size>0) return expressTakeList.get(0);
		else return null;
	}

}
