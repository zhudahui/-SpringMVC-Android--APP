package com.mobileclient.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.TakeOrder;
import com.mobileclient.util.HttpUtil;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*代拿订单管理业务逻辑层*/
public class TakeOrderService {
	/* 添加代拿订单 */
	public String AddTakeOrder(TakeOrder takeOrder) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("orderId", takeOrder.getOrderId() + "")
					.add("expressTakeObj", takeOrder.getExpressTakeObj() + "")
					.add("userObj", takeOrder.getUserObj())
					.add("takeTime", takeOrder.getTakeTime())
					.add("orderStateObj", takeOrder.getOrderStateObj() + "")
					.add("ssdt", takeOrder.getSsdt())
					.add("evaluate", takeOrder.getEvaluate())
					.add("action", "add")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "TakeOrderServlet?").post(body).build();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TakeOrderServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询代拿订单 */
	public List<TakeOrder> QueryTakeOrder(TakeOrder queryConditionTakeOrder) throws Exception {
		String urlString = HttpUtil.BASE_URL + "TakeOrderServlet?action=query";
		if(queryConditionTakeOrder != null) {
			urlString += "&expressTakeObj=" + queryConditionTakeOrder.getExpressTakeObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionTakeOrder.getUserObj(), "UTF-8") + "";
			urlString += "&takeTime=" + URLEncoder.encode(queryConditionTakeOrder.getTakeTime(), "UTF-8") + "";
			urlString += "&orderStateObj=" + queryConditionTakeOrder.getOrderStateObj();
		}
		List<TakeOrder> takeOrderList = new ArrayList<TakeOrder>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				TakeOrder takeOrder = new TakeOrder();
				takeOrder.setOrderId(object.getInt("orderId"));
				takeOrder.setExpressTakeObj(object.getInt("expressTakeObj"));
				takeOrder.setUserObj(object.getString("userObj"));
				takeOrder.setTakeTime(object.getString("takeTime"));
				takeOrder.setOrderStateObj(object.getInt("orderStateObj"));
				takeOrder.setSsdt(object.getString("ssdt"));
				takeOrder.setEvaluate(object.getString("evaluate"));
				takeOrderList.add(takeOrder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return takeOrderList;
	}

	/* 更新代拿订单 */
	public String UpdateTakeOrder(TakeOrder takeOrder) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("orderId", takeOrder.getOrderId() + "")
					.add("expressTakeObj", takeOrder.getExpressTakeObj() + "")
					.add("userObj", takeOrder.getUserObj())
					.add("takeTime", takeOrder.getTakeTime())
					.add("orderStateObj", takeOrder.getOrderStateObj() + "")
					.add("ssdt", takeOrder.getSsdt())
					.add("evaluate", takeOrder.getEvaluate())
					.add("action", "update")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "TakeOrderServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TakeOrderServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除代拿订单 */
	public String DeleteTakeOrder(int orderId) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("orderId", orderId + "")
					.add("action", "delete")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "TakeOrderServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TakeOrderServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "代拿订单信息删除失败!";
		}
	}

	/* 根据订单id获取代拿订单对象 */
	public TakeOrder GetTakeOrder(int orderId)  {
		List<TakeOrder> takeOrderList = new ArrayList<TakeOrder>();
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("orderId", orderId + "")
					.add("action", "updateQuery")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "TakeOrderServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TakeOrderServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				TakeOrder takeOrder = new TakeOrder();
				takeOrder.setOrderId(object.getInt("orderId"));
				takeOrder.setExpressTakeObj(object.getInt("expressTakeObj"));
				takeOrder.setUserObj(object.getString("userObj"));
				takeOrder.setTakeTime(object.getString("takeTime"));
				takeOrder.setOrderStateObj(object.getInt("orderStateObj"));
				takeOrder.setSsdt(object.getString("ssdt"));
				takeOrder.setEvaluate(object.getString("evaluate"));
				takeOrderList.add(takeOrder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = takeOrderList.size();
		if(size>0) return takeOrderList.get(0); 
		else return null; 
	}
}
