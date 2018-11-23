package com.mobileclient.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.OrderState;
import com.mobileclient.util.HttpUtil;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*订单状态管理业务逻辑层*/
public class OrderStateService {
	/* 添加订单状态 */
	public String AddOrderState(OrderState orderState) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("orderStateId", orderState.getOrderStateId() + "")
					.add("orderStateName",orderState.getOrderStateName())
					.add("action", "add")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "OrderStateServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "OrderStateServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询订单状态 */
	public List<OrderState> QueryOrderState(OrderState queryConditionOrderState) throws Exception {
		String urlString = HttpUtil.BASE_URL + "OrderStateServlet?action=query";
		if(queryConditionOrderState != null) {
		}
		List<OrderState> orderStateList = new ArrayList<OrderState>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				OrderState orderState = new OrderState();
				orderState.setOrderStateId(object.getInt("orderStateId"));
				orderState.setOrderStateName(object.getString("orderStateName"));
				orderStateList.add(orderState);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderStateList;
	}

	/* 更新订单状态 */
	public String UpdateOrderState(OrderState orderState) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("orderStateId", orderState.getOrderStateId() + "")
					.add("orderStateName",orderState.getOrderStateName())
					.add("action", "update")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "OrderStateServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "OrderStateServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除订单状态 */
	public String DeleteOrderState(int orderStateId) {

		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("orderStateId", orderStateId + "")
					.add("action", "delete")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "OrderStateServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "OrderStateServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "订单状态信息删除失败!";
		}
	}

	/* 根据订单状态id获取订单状态对象 */
	public OrderState GetOrderState(int orderStateId)  {
		List<OrderState> orderStateList = new ArrayList<OrderState>();
		byte[] resultByte;
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("orderStateId", orderStateId + "")
					.add("action", "updateQuery")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "OrderStateServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "OrderStateServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				OrderState orderState = new OrderState();
				orderState.setOrderStateId(object.getInt("orderStateId"));
				orderState.setOrderStateName(object.getString("orderStateName"));
				orderStateList.add(orderState);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = orderStateList.size();
		if(size>0) return orderStateList.get(0); 
		else return null; 
	}
}
