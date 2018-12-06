package com.mobileclient.service;

import android.text.TextUtils;
import android.util.Log;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.activity.LoginActivity;
import com.mobileclient.domain.Order;
import com.mobileclient.domain.TakeOrder;
import com.mobileclient.util.HttpUtil;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*代拿订单管理业务逻辑层*/
public class OrderService {
    /* 添加代拿订单 */
    public String AddOrder(Order order) {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("orderId", order.getOrderId() + "")
                    .add("orderName", order.getOrderName())
                    .add("userId", String.valueOf(order.getUserId()))
                    .add("expressCompanyName", "" + order.getExpressCompanyName())
                    .add("expressCompanyAddress", order.getExpressCompanyAddress())
                    .add("receiveAddressId", String.valueOf(order.getReceiveAddressId()))
                    .add("addTime", order.getAddTime())
                    .add("orderState", order.getOrderState())
                    .add("orderPay", order.getOrderPay())
                    .add("remark", order.getRemark())
                    .add("receiveCode", String.valueOf(order.getReceiveCode()))
                    .add("userPhone",order.getUserPhone())
                    .add("orderEvaluate",order.getOrderEvaluate())
                    .add("takeUserId",String.valueOf(order.getTakeUserId()))
                    .add("action", "add")
                    .build();
            Log.i("order","body"+body);
            Request request = new Request.Builder().url(HttpUtil.BASE_URL + "order/add?").post(body).build();
            //resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TakeOrderServlet?", params, "UTF-8");
            //String result = new String(resultByte, "UTF-8");
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.i("order",""+result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 查询代拿订单 */
    public List<Order> QueryOrder(Order queryConditionTakeOrder) throws Exception {
        String urlString = HttpUtil.BASE_URL + "order/all?action=query";
        if (queryConditionTakeOrder != null) {
            urlString += "&orderName=" + queryConditionTakeOrder.getOrderName();
            urlString += "&useId=" + URLEncoder.encode(String.valueOf(queryConditionTakeOrder.getUserId()), "UTF-8") + "";
            urlString += "&expressCompanyName=" + URLEncoder.encode(queryConditionTakeOrder.getExpressCompanyName(), "UTF-8") + "";
            urlString += "&expressCompanyAddress=" + URLEncoder.encode(queryConditionTakeOrder.getExpressCompanyAddress(),"UTF-8")+"";
            urlString += "&receiveAddressId=" + URLEncoder.encode(String.valueOf(queryConditionTakeOrder.getReceiveAddressId()),"UTF-8")+"";
            urlString += "&addTime=" + queryConditionTakeOrder.getAddTime();
            urlString += "&orderState=" + queryConditionTakeOrder.getOrderState();
            urlString += "&orderPay=" + queryConditionTakeOrder.getOrderPay();
            urlString += "&remark=" + queryConditionTakeOrder.getRemark();
            urlString += "&receiveCode=" + queryConditionTakeOrder.getReceiveCode();
            urlString += "&userPhone=" + queryConditionTakeOrder.getUserPhone();
            urlString += "&orderEvaluate=" + queryConditionTakeOrder.getOrderEvaluate();
            urlString += "&takeUserId=" + queryConditionTakeOrder.getTakeUserId();
        }
        Log.i("zhuhui",urlString);
        List<Order> orderList = new ArrayList<Order>();
        try {
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(urlString).build();
            Response response=client.newCall(request).execute();
            String result =response.body().string();
            JSONArray array = new JSONArray(result);
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = array.getJSONObject(i);
                Order order = new Order();
                order.setOrderId(object.getInt("orderId"));
                Log.i("zhuhui",object.getInt("orderId")+"result");
                order.setOrderName(object.getString("orderName"));
                order.setUserId(object.getInt("userId"));
                order.setExpressCompanyName(object.getString("expressCompanyName"));
                order.setExpressCompanyAdress(object.getString("expressCompanyAddress"));
                order.setReceiveAdressId(object.getInt("receiveAddressId"));
                order.setAddTime(object.getString("addTime"));
                order.setOrderState(object.getString("orderState"));
                order.setOrderPay(object.getString("orderPay"));
                order.setRemark(object.getString("remark"));
                order.setReceiveCode(object.getString("receiveCode"));
                order.setOrderEvaluate(object.getString("orderEvaluate"));
                order.setTakeUserId(object.getInt("takeUserId"));

                //order.set
                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("zhuhui",orderList.size()+"result");
        return orderList;
    }
    //根据订单名、用户名，发布者、快递公司、酬金  订单状态进行查询
    public List<Order> UserQuery(int userId) throws Exception {
        String urlString = HttpUtil.BASE_URL + "order/user?userId="+userId;

        Log.i("zhuhui",""+urlString);
        List<Order> orderList = new ArrayList<Order>();
        try {
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(urlString).build();
            Response response=client.newCall(request).execute();
            String result =response.body().string();
            JSONArray array = new JSONArray(result);
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = array.getJSONObject(i);
                Order order = new Order();
                order.setOrderId(object.getInt("orderId"));
                order.setOrderName(object.getString("orderName"));
                order.setUserId(object.getInt("userId"));
                order.setExpressCompanyName(object.getString("expressCompanyName"));
                order.setExpressCompanyAdress(object.getString("expressCompanyAddress"));
                order.setReceiveAdressId(object.getInt("receiveAddressId"));
                order.setAddTime(object.getString("addTime"));
                order.setOrderState(object.getString("orderState"));
                order.setOrderPay(object.getString("orderPay"));
                order.setRemark(object.getString("remark"));
                order.setReceiveCode(object.getString("receiveCode"));
                order.setOrderEvaluate(object.getString("orderEvaluate"));
                order.setTakeUserId(object.getInt("takeUserId"));
                //order.set
                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }
    //
    public List<Order> takeUserQuery(int takeUserId) throws Exception {
        String urlString = HttpUtil.BASE_URL + "order/takeUserId?takeUserId="+takeUserId;

        Log.i("zhuhui",""+urlString);
        List<Order> orderList = new ArrayList<Order>();
        try {
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(urlString).build();
            Response response=client.newCall(request).execute();
            String result =response.body().string();
            JSONArray array = new JSONArray(result);
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = array.getJSONObject(i);
                Order order = new Order();
                order.setOrderId(object.getInt("orderId"));
                order.setOrderName(object.getString("orderName"));
                order.setUserId(object.getInt("userId"));
                order.setExpressCompanyName(object.getString("expressCompanyName"));
                order.setExpressCompanyAdress(object.getString("expressCompanyAddress"));
                order.setReceiveAdressId(object.getInt("receiveAddressId"));
                order.setAddTime(object.getString("addTime"));
                order.setOrderState(object.getString("orderState"));
                order.setOrderPay(object.getString("orderPay"));
                order.setRemark(object.getString("remark"));
                order.setReceiveCode(object.getString("receiveCode"));
                order.setOrderEvaluate(object.getString("orderEvaluate"));
                order.setTakeUserId(object.getInt("takeUserId"));
                //order.set
                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }
    //根据代取者Id查询
    public Order QueryTake(int orderId) throws Exception {
        String urlString = HttpUtil.BASE_URL + "order?orderId="+orderId;
        Log.i("zhuhui",""+urlString);
        List<Order> orderList = new ArrayList<Order>();
        try {
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(urlString).build();
            Response response=client.newCall(request).execute();
            String result =response.body().string();
            JSONObject object;
            Order order = new Order();
            if(!TextUtils.isEmpty(result)) {
                object=new JSONObject(result);
                //ReceiveAddress receiveAddress = new ReceiveAddress();
                order.setOrderId(object.getInt("orderId"));
                order.setOrderName(object.getString("orderName"));
                order.setUserId(object.getInt("userId"));
                order.setExpressCompanyName(object.getString("expressCompanyName"));
                order.setExpressCompanyAdress(object.getString("expressCompanyAddress"));
                order.setReceiveAdressId(object.getInt("receiveAddressId"));
                order.setAddTime(object.getString("addTime"));
                order.setOrderState(object.getString("orderState"));
                order.setOrderPay(object.getString("orderPay"));
                order.setRemark(object.getString("remark"));
                order.setReceiveCode(object.getString("receiveCode"));
                order.setOrderEvaluate(object.getString("orderEvaluate"));
                order.setTakeUserId(object.getInt("takeUserId"));
                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int size = orderList.size();
        if (size > 0) return orderList.get(0);
        else return null;
    }
    /* 更新代拿订单 */
    public String UpdateOrder(Order order) {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("orderId", order.getOrderId() + "")
                    .add("orderName", order.getOrderName())
                    .add("userId", String.valueOf(order.getUserId()))
                    .add("expressCompanyName", "" + order.getExpressCompanyName())
                    .add("expressCompanyAddress", order.getExpressCompanyAddress())
                    .add("receiveAddressId", String.valueOf(order.getReceiveAddressId()))
                    .add("addTime", order.getAddTime())
                    .add("orderState", order.getOrderState())
                    .add("orderPay", order.getOrderPay())
                    .add("remark", order.getRemark())
                    .add("receiveCode", String.valueOf(order.getReceiveCode()))
                    .add("orderEvaluate", order.getOrderEvaluate())
                    .add("takeUserId", String.valueOf(order.getTakeUserId()))
                    .add("action", "update")
                    .build();
            Request request = new Request.Builder().url(HttpUtil.BASE_URL + "order/update?").post(body).build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            //resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TakeOrderServlet?", params, "UTF-8");
            //String result = new String(resultByte, "UTF-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 删除代拿订单 */
    public String DeleteOrder(int orderId) {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("orderId", orderId + "")
                    .add("action", "delete")
                    .build();
            Request request = new Request.Builder().url(HttpUtil.BASE_URL + "TakeOrderServlet?").post(body).build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            //resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TakeOrderServlet?", params, "UTF-8");
            //String result = new String(resultByte, "UTF-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "代拿订单信息删除失败!";
        }
    }

//    /* 根据订单id获取代拿订单对象 */
//    public Order GetOrder(int orderId) {
//        List<Order> orderList = new ArrayList<Order>();
//        try {
//            OkHttpClient client = new OkHttpClient();
//            RequestBody body = new FormBody.Builder()
//                    .add("orderId", orderId + "")
//                    .add("action", "updateQuery")
//                    .build();
//            Request request = new Request.Builder().url(HttpUtil.BASE_URL + "TakeOrderServlet?").post(body).build();
//            Response response = client.newCall(request).execute();
//            String result = response.body().string();
//            //resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TakeOrderServlet?", params, "UTF-8");
//            //String result = new String(resultByte, "UTF-8");
//            JSONArray array = new JSONArray(result);
//            int length = array.length();
//            for (int i = 0; i < length; i++) {
//                JSONObject object = array.getJSONObject(i);
//                Order order = new Order();
//                order.setOrderId(object.getInt("orderId"));
//                order.setOrderName(object.getString("orderName"));
//                order.setUserId(object.getInt("userId"));
//                order.setExpressCompanyName(object.getString("expressCompanyName"));
//                order.setExpressCompanyAdress(object.getString("expressCompanyAddress"));
//                order.setReceiveAdressId(object.getInt("receiveAddressId"));
//                order.setAddTime(object.getString("addTime"));
//                order.setOrderState(object.getString("orderState"));
//                order.setOrderPay(object.getString("orderPay"));
//                order.setRemark(object.getString("remark"));
//                order.setReceiveCode(object.getInt("receiveCode"));
//                orderList.add(order);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        int size = orderList.size();
//        if (size > 0) return orderList.get(0);
//        else return null;
//    }
}