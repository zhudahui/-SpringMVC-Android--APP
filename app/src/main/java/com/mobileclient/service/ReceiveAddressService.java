package com.mobileclient.service;


import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.activity.LoginActivity;
import com.mobileclient.domain.ReceiveAddress;
import com.mobileclient.domain.TakeOrder;
import com.mobileclient.util.HttpUtil;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*代拿订单管理业务逻辑层*/
public class ReceiveAddressService {
    /* 添加地址信息 */
    public String AddReceiveAdress(ReceiveAddress receiveAdress) {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("receiveId", receiveAdress.getReceiveId() + "")
                    .add("userId", receiveAdress.getUserId() + "")
                    .add("receiveName", receiveAdress.getReceiveName() + "")
                    .add("receivePhone", receiveAdress.getReceivePhone())
                    .add("receiveState", receiveAdress.getReceiveState())
                    .add("receiveAddressName",receiveAdress.getReceiveAddressName())
                    .add("action", "add")
                    .build();
            Request request = new Request.Builder().url(HttpUtil.BASE_URL + "receive-address/add?").post(body).build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.i("Address",""+result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 查询地址列表 */
    public List<ReceiveAddress> QueryReceiveAdress(ReceiveAddress queryConditionReceiveAdress) throws Exception {
        String urlString = HttpUtil.BASE_URL + "receive-address/all?action=query";
//        if(queryConditionReceiveAdress != null) {
//            urlString += "&expressTakeObj=" + queryConditionReceiveAdress.getExpressTakeObj();
//            urlString += "&userObj=" + URLEncoder.encode(queryConditionReceiveAdress.getUserObj(), "UTF-8") + "";
//            urlString += "&takeTime=" + URLEncoder.encode(queryConditionReceiveAdress.getTakeTime(), "UTF-8") + "";
//            urlString += "&orderStateObj=" + queryConditionReceiveAdress.getOrderStateObj();
//        }
        List<ReceiveAddress> receiveAdressesList = new ArrayList<ReceiveAddress>();
        byte[] resultByte;
        try {
           // resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(urlString).build();
            Response response=client.newCall(request).execute();
            String result = response.body().string();
            JSONArray array = new JSONArray(result);
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = array.getJSONObject(i);
                ReceiveAddress receiveAdress = new ReceiveAddress();
                receiveAdress.setReceiveId(object.getInt("receiveId"));
                receiveAdress.setReceiveAddressName(object.getString("receiveAddressName"));
                receiveAdress.setUserId(object.getInt("userId"));
                receiveAdress.setReceiveName(object.getString("receiveName"));
                receiveAdress.setReceivePhone(object.getString("receivePhone"));
                receiveAdress.setReceiveState(object.getString("receiveState"));
                receiveAdressesList.add(receiveAdress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiveAdressesList;
    }
    /* 根据用户Id查询地址列表 */
    public List<ReceiveAddress> QueryReceiveAdressList(int userId) throws Exception {
        String urlString = HttpUtil.BASE_URL + "receive-address/userId?userId="+userId;
        Log.i("Address",urlString);
        List<ReceiveAddress> receiveAddressesList = new ArrayList<ReceiveAddress>();

        try {
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(urlString).build();
            Response response=client.newCall(request).execute();
            String result = response.body().string();
            Log.i("Address",result);
            JSONArray array = new JSONArray(result);
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = array.getJSONObject(i);
                ReceiveAddress receiveAdress = new ReceiveAddress();
                receiveAdress.setReceiveId(object.getInt("receiveId"));
                receiveAdress.setUserId(object.getInt("userId"));
                receiveAdress.setReceiveName(object.getString("receiveName"));
                receiveAdress.setReceivePhone(object.getString("receivePhone"));
                receiveAdress.setReceiveState(object.getString("receiveState"));
                receiveAdress.setReceiveAddressName(object.getString("receiveAddressName"));
                receiveAddressesList.add(receiveAdress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiveAddressesList;
    }
    /* 更新代拿订单 */
    public String UpdateReceiveAddress(ReceiveAddress receiveAddress) {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("receiveId", receiveAddress.getReceiveId() + "")
                    .add("userId", receiveAddress.getUserId() + "")
                    .add("receiveName", receiveAddress.getReceiveName() + "")
                    .add("receivePhone", receiveAddress.getReceivePhone())
                    .add("receiveState", receiveAddress.getReceiveState())
                    .add("receiveAddressName",receiveAddress.getReceiveAddressName())
                    .add("action", "update")
                    .build();


            Log.i("pppppppp","ccc"+receiveAddress.getReceiveId());

            Request request = new Request.Builder().url(HttpUtil.BASE_URL + "receive-address/update?").post(body).build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            Log.i("pppppppp",""+result);
            //resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TakeOrderServlet?", params, "UTF-8");
            //String result = new String(resultByte, "UTF-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 删除代拿订单 */
    public String DeleteReceiveAddress(int receiveId) {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("receiveId", receiveId + "")
                    .add("action", "delete")
                    .build();
            Request request = new Request.Builder().url(HttpUtil.BASE_URL + "receive-address/delete?").post(body).build();
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


    //根据收货地址Id查询地址

    public ReceiveAddress QueryReceiveAdress(int receiveAddressId) throws Exception {
        String urlString = HttpUtil.BASE_URL + "receive-address?receiveId="+receiveAddressId;
        ReceiveAddress receiveAddress = new ReceiveAddress();
        List<ReceiveAddress> receiveAddressList = new ArrayList<ReceiveAddress>();
        try {
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(urlString).build();
            Log.i("zhu","查询name"+urlString);
            Response response=client.newCall(request).execute();
            Log.i("zhu","查询name"+response);
            String result=response.body().string();
            JSONObject object;
            if(!TextUtils.isEmpty(result)) {
                object=new JSONObject(result);
                //ReceiveAddress receiveAddress = new ReceiveAddress();
                receiveAddress.setReceiveId(object.getInt("receiveId"));
                //receiveAdress.setUserId(object.getInt("userId"));
                receiveAddress.setReceiveName(object.getString("receiveName"));
                receiveAddress.setReceivePhone(object.getString("receivePhone"));
                receiveAddress.setReceiveState(object.getString("receiveState"));
                receiveAddress.setReceiveAddressName(object.getString("receiveAddressName"));

                receiveAddressList.add(receiveAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int size = receiveAddressList.size();
        if (size > 0) return receiveAddressList.get(0);
        else return null;
    }
    /* 根据收货人获取地址对象 */
    public ReceiveAddress GetReceiveName(String receiveName) {
        List<ReceiveAddress> receiveAdressList = new ArrayList<ReceiveAddress>();
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("receiveName", receiveName + "")
                    .add("action", "updateQuery")
                    .build();
            Request request = new Request.Builder().url(HttpUtil.BASE_URL + "receive-address/receiveName?").post(body).build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            //resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TakeOrderServlet?", params, "UTF-8");
            //String result = new String(resultByte, "UTF-8");
            JSONArray array = new JSONArray(result);
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = array.getJSONObject(i);
                ReceiveAddress receiveAdress = new ReceiveAddress();
                receiveAdress.setReceiveId(object.getInt("receiveId"));
                receiveAdress.setUserId(object.getInt("userId"));
                receiveAdress.setReceiveName(object.getString("receiveName"));
                receiveAdress.setReceivePhone(object.getString("receivePhone"));
                receiveAdress.setReceiveState(object.getString("receiveState"));
                receiveAdress.setReceiveAddressName(object.getString("receiveAddressName"));
                receiveAdressList.add(receiveAdress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int size = receiveAdressList.size();
        if (size > 0) return receiveAdressList.get(0);
        else return null;
    }
}