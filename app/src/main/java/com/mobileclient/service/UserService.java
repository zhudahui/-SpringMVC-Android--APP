package com.mobileclient.service;

import android.text.TextUtils;
import android.util.Log;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.User;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.util.HttpUtil;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*用户管理业务逻辑层*/
public class UserService {
    /* 添加用户 */
    public String AddUserInfo(User user) {
        try {
            OkHttpClient client=new OkHttpClient();
            RequestBody body=new FormBody.Builder()
                    .add("userId",String.valueOf(user.getUserId()))
                    .add("userName",user.getUserName())
                    .add("userPassword", user.getUserPassword())
                    .add("userPhoto",user.getUserPhoto())
                    .add("userType", user.getUserType())
                    .add("userPhone", user.getUserPhone())
                    .add("userGender", user.getUserGender())
                    .add("userEmail", user.getUserEmail())
                    .add("userReputation", String.valueOf(user.getUserReputation()))
                    .add("userMoney", user.getUserMoney())
                    .add("userAuthFile", user.getUserAuthFile())
                    .add("regTime", user.getRegTime())
                    .add("nickName",user.getNickName())
                    .add("studentId", String.valueOf(user.getStudentId()))
                    .add("userAuthState",user.getUserAuthState())
                    .add("payPwd",user.getPayPwd())
                    .add("action", "add")
                    .build();
            Request request=new Request.Builder().url(HttpUtil.BASE_URL + "user/add?").post(body).build();
            Response reponse=client.newCall(request).execute();
            String result=reponse.body().string();
            Log.i("uuuuuuuu",""+result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 查询用户 */
    public List<User> QueryUser(User queryConditionUserInfo) throws Exception {
       // String urlString = HttpUtil.BASE_URL + "user/all?action=query";
//        if(queryConditionUserInfo != null) {
//            urlString += "&user_name=" + URLEncoder.encode(queryConditionUserInfo.getUser_name(), "UTF-8") + "";
//            urlString += "&userType=" + URLEncoder.encode(queryConditionUserInfo.getUserType(), "UTF-8") + "";
//            urlString += "&name=" + URLEncoder.encode(queryConditionUserInfo.getName(), "UTF-8") + "";
//            if(queryConditionUserInfo.getBirthDate() != null) {
//                urlString += "&birthDate=" + URLEncoder.encode(queryConditionUserInfo.getBirthDate().toString(), "UTF-8");
//            }
//            urlString += "&telephone=" + URLEncoder.encode(queryConditionUserInfo.getTelephone(), "UTF-8") + "";
//            urlString += "&shenHeState=" + URLEncoder.encode(queryConditionUserInfo.getShenHeState(), "UTF-8") + "";
//        }
        //第2种是基于json数据格式解析，我们采用的是第2种
        List<User> userInfoList = new ArrayList<User>();
        try {
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(HttpUtil.BASE_URL + "user/all").build();
            Log.i("ffffff",HttpUtil.BASE_URL + "user/all");
            Response reponse=client.newCall(request).execute();
            String result=reponse.body().string();
            JSONArray array = new JSONArray(result);
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = array.getJSONObject(i);
                User user=new User();
                user.setUserId(object.getInt("userId"));
                user.setUserName(object.getString("userName"));
                user.setUserPassword(object.getString("userPassword"));
                user.setUserPhoto(object.getString("userPhoto"));
                user.setUserType(object.getString("userType"));
                user.setUserPhone(object.getString("userPhone"));
                user.setUserGender(object.getString("userGender"));
                user.setUserEmail(object.getString("userEmail"));
                user.setUserReputation(object.getInt("userReputation"));
                user.setUserMoney(object.getString("userMoney"));
                user.setUserAuthFile(object.getString("userAuthFile"));
                user.setRegTime(object.getString("regTime"));
                user.setNickName(object.getString("nickName"));
                user.setStudentId(object.getInt("studentId"));
                user.setUserAuthState(object.getString("userAuthState"));
                user.setPayPwd(object.getString("payPwd"));
                userInfoList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfoList;
    }

    /* 更新用户 */
    public String UpdateUserInfo(User user) {
        try {
            Log.i("ppppuserName",user.getUserName());
            OkHttpClient client=new OkHttpClient();
            RequestBody body=new FormBody.Builder()
                    .add("userId", String.valueOf(user.getUserId()))
                    .add("userName",user.getUserName())
                    .add("userPassword", user.getUserPassword())
                    .add("userPhoto",user.getUserPhoto())
                    .add("userType", user.getUserType())
                    .add("userPhone", user.getUserPhone())
                    .add("userGender", user.getUserGender())
                    .add("userEmail", user.getUserEmail())
                    .add("userReputation", String.valueOf(user.getUserReputation()))
                    .add("userMoney", user.getUserMoney())
                    .add("userAuthFile", user.getUserAuthFile())
                    .add("regTime", user.getRegTime())
                    .add("action", "update")
                    .add("studentId", String.valueOf(user.getStudentId()))
                    .add("nickName",user.getNickName())
                    .add("userAuthState",user.getUserAuthState())
                    .add("payPwd",user.getPayPwd())
                    .build();

            Log.i("pppppuserPassword", user.getUserPassword());
            Log.i("ppppuserPhoto",user.getUserPhoto());
                    Log.i("userType", user.getUserType());
            Log.i("ppppuserPhone", user.getUserPhone());
            Log.i("ppppuserGender", user.getUserGender());
            Log.i("ppppuserEmail", user.getUserEmail());
            Log.i("ppppuserReputation", String.valueOf(user.getUserReputation()));
            Log.i("ppppuserMoney", user.getUserMoney());
            Log.i("ppppuserAuthFile", user.getUserAuthFile());
            Log.i("ppppregTime", user.getRegTime());
            Log.i("action", "update");
            Log.i("ppppstudentId", String.valueOf(user.getStudentId()));
            Log.i("ppppnickName",user.getNickName());

            Log.i("ppppppp","body"+body);
            Request request=new Request.Builder().url(HttpUtil.BASE_URL + "user/update?").post(body).build();
            //resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "UserInfoServlet?", params, "UTF-8");
            //String result = new String(resultByte, "UTF-8");
            Response reponse=client.newCall(request).execute();
            String result=reponse.body().string();
            Log.i("ppppppp","result"+result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* 删除用户 */
    public String DeleteUserInfo(int userId) {
        try {
            OkHttpClient client=new OkHttpClient();
            RequestBody body=new FormBody.Builder()
                    .add("userId", String.valueOf(userId))
                    .add("action", "delete")
                    .build();
            Request request=new Request.Builder().url(HttpUtil.BASE_URL + "user/delete?").post(body).build();
            Response response=client.newCall(request).execute();
            String result=response.body().string();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "用户信息删除失败!";
        }
    }

    /* 根据用户Id获取用户对象 */
    public User GetUserInfo(int userId)  {
        List<User> userList = new ArrayList<User>();
        try {
            OkHttpClient client=new OkHttpClient();
            //resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "UserInfoServlet?", params, "UTF-8");
            Request request=new Request.Builder().url(HttpUtil.BASE_URL + "user?"+"userId="+userId+"&action=updateQuery").build();
            //String result = new String(resultByte, "UTF-8");
            Response response=client.newCall(request).execute();
            String reponseData=response.body().string();
            JSONObject object;
            if(!TextUtils.isEmpty(reponseData)) {
                object = new JSONObject(reponseData);
           // JSONArray array = new JSONArray(reponseData);
           // int length = array.length();
           // for (int i = 0; i < length; i++) {
                //JSONObject object = array.getJSONObject(i);
                User user = new User();
                user.setUserId(object.getInt("userId"));
                user.setUserName(object.getString("userName"));
                user.setUserPassword(object.getString("userPassword"));
                user.setUserPhoto(object.getString("userPhoto"));
                user.setUserType(object.getString("userType"));
                user.setUserPhone(object.getString("userPhone"));
                user.setUserGender(object.getString("userGender"));
                user.setUserEmail(object.getString("userEmail"));
                user.setUserReputation(object.getInt("userReputation"));
                user.setUserMoney(object.getString("userMoney"));
                user.setUserAuthFile(object.getString("userAuthFile"));
                user.setRegTime(object.getString("regTime"));
                user.setNickName(object.getString("nickName"));
                user.setStudentId(object.getInt("studentId"));
                user.setUserAuthState(object.getString("userAuthState"));
                user.setPayPwd(object.getString("payPwd"));
                userList.add(user);
                Log.i("qqqqqqqq",""+userList.size());
            }
           // }
        } catch (Exception e) {

            e.printStackTrace();
        }
        int size = userList.size();
        if(size>0) return userList.get(0);
        else return null;
    }
    /* 根据登录名获取用户对象 */
    public User GetUserInfo(String nickName)  {
        List<User> userList = new ArrayList<User>();
        try {
            OkHttpClient client=new OkHttpClient();
            //resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "UserInfoServlet?", params, "UTF-8");
            Request request=new Request.Builder().url(HttpUtil.BASE_URL + "user/nickName?"+"nickName="+nickName+"&action=updateQuery").build();
            //String result = new String(resultByte, "UTF-8");
            Response response=client.newCall(request).execute();
            String reponseData=response.body().string();
            JSONObject object;
            if(!TextUtils.isEmpty(reponseData)) {
                object = new JSONObject(reponseData);
                // JSONArray array = new JSONArray(reponseData);
                // int length = array.length();
                // for (int i = 0; i < length; i++) {
                //JSONObject object = array.getJSONObject(i);
                User user = new User();
                user.setUserId(object.getInt("userId"));
                user.setUserName(object.getString("userName"));
                user.setUserPassword(object.getString("userPassword"));
                user.setUserPhoto(object.getString("userPhoto"));
                user.setUserType(object.getString("userType"));
                user.setUserPhone(object.getString("userPhone"));
                user.setUserGender(object.getString("userGender"));
                user.setUserEmail(object.getString("userEmail"));
                user.setUserReputation(object.getInt("userReputation"));
                user.setUserMoney(object.getString("userMoney"));
                user.setUserAuthFile(object.getString("userAuthFile"));
                user.setRegTime(object.getString("regTime"));
                user.setNickName(object.getString("nickName"));
                user.setStudentId(object.getInt("studentId"));
                user.setUserAuthState(object.getString("userAuthState"));
                user.setPayPwd(object.getString("payPwd"));
                userList.add(user);
                Log.i("qqqqqqqq",""+userList.size());
            }
            // }
        } catch (Exception e) {

            e.printStackTrace();
        }
        int size = userList.size();
        if(size>0) return userList.get(0);
        else return null;
    }
}