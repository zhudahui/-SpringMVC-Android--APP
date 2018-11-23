package com.mobileclient.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Notice;
import com.mobileclient.util.HttpUtil;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*新闻公告管理业务逻辑层*/
public class NoticeService {
	/* 添加新闻公告 */
	public String AddNotice(Notice notice) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("noticeId", notice.getNoticeId() + "")
					.add("title", notice.getNoticeTitle())
					.add("content", notice.getNoticeContent())
					.add("publishDate", notice.getPublishDate())
					.add("action", "add")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "NoticeServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NoticeServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询新闻公告 */
	public List<Notice> QueryNotice(Notice queryConditionNotice) throws Exception {
		String urlString = HttpUtil.BASE_URL + "NoticeServlet?action=query";
		if(queryConditionNotice != null) {
			urlString += "&title=" + URLEncoder.encode(queryConditionNotice.getNoticeTitle(), "UTF-8") + "";
			urlString += "&publishDate=" + URLEncoder.encode(queryConditionNotice.getPublishDate(), "UTF-8") + "";
		}
		List<Notice> noticeList = new ArrayList<Notice>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Notice notice = new Notice();
				notice.setNoticeId(object.getInt("noticeId"));
				notice.setNoticeTitle(object.getString("title"));
				notice.setNoticeContent(object.getString("content"));
				notice.setPublishDate(object.getString("publishDate"));
				noticeList.add(notice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noticeList;
	}

	/* 更新新闻公告 */
	public String UpdateNotice(Notice notice) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("noticeId", notice.getNoticeId() + "")
					.add("title", notice.getNoticeTitle())
					.add("content", notice.getNoticeContent())
					.add("publishDate", notice.getPublishDate())
					.add("action", "update")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "NoticeServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NoticeServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除新闻公告 */
	public String DeleteNotice(int noticeId) {
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("noticeId", noticeId + "")
					.add("action", "delete")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "NoticeServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NoticeServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "新闻公告信息删除失败!";
		}
	}

	/* 根据公告id获取新闻公告对象 */
	public Notice GetNotice(int noticeId)  {
		List<Notice> noticeList = new ArrayList<Notice>();
		try {
			OkHttpClient client=new OkHttpClient();
			RequestBody body=new FormBody.Builder()
					.add("noticeId", noticeId + "")
					.add("action", "updateQuery")
					.build();
			Request request=new Request.Builder().url(HttpUtil.BASE_URL + "NoticeServlet?").post(body).build();
			Response response=client.newCall(request).execute();
			String result=response.body().string();
			//resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NoticeServlet?", params, "UTF-8");
			//String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Notice notice = new Notice();
				notice.setNoticeId(object.getInt("noticeId"));
				notice.setNoticeTitle(object.getString("title"));
				notice.setNoticeContent(object.getString("content"));
				notice.setPublishDate(object.getString("publishDate"));
				noticeList.add(notice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = noticeList.size();
		if(size>0) return noticeList.get(0); 
		else return null; 
	}
}
