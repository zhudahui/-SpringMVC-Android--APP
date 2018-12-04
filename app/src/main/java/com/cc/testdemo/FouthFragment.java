package com.cc.testdemo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mobileclient.activity.MyProgressDialog;
import com.mobileclient.activity.R;
import com.mobileclient.activity.UserInfoDetailActivity;
import com.mobileclient.activity.UserInfoListActivity;
import com.mobileclient.adapter.UserInfoSimpleAdapter;
import com.mobileclient.domain.User;
import com.mobileclient.service.UserService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FouthFragment extends Fragment {

    UserInfoSimpleAdapter adapter;
    List<Map<String, Object>> list;
    private User queryConditionUser=null;
    // @BindView(R.id.id_recyclerview)
    private ListView lv;
    private List<String> stringList;
    private MyProgressDialog dialog; //进度条	@Override
    UserService userService=new UserService();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        lv=view.findViewById(R.id.list_view);
        dialog = MyProgressDialog.getInstance(getContext());
        ButterKnife.bind(this, view);
        setViews();
        return view;
    }

    private void initData() {
        stringList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            stringList.add(String.valueOf(i));
        }

        // mAdapter = new MyRecyclerViewAdapter(getActivity(), stringList);
        //设置布局管理器
        // mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置adapter
        // mRecyclerview.setAdapter(mAdapter);
        //添加分割线
        // mRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        //mAdapter.setOnItemClickLitener(this);
    }


    private void setViews() {

        dialog.show();
        final Handler handler = new Handler();
        new Thread(){
            @Override
            public void run() {
                //在子线程中进行下载数据操作
                list = getDatas();
                //发送消失到handler，通知主线程下载完成
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.cancel();
                        adapter = new UserInfoSimpleAdapter(getContext(), list,
                                R.layout.userinfo_list_item,
                                new String[] { "userPhoto","userType","nickName" },
                                new int[] {R.id.iv_userPhoto,R.id.userType,R.id.nickName},lv);
                        lv.setAdapter(adapter);
                    }
                });
            }
        }.start();

        // 添加点击
        lv.setOnCreateContextMenuListener(UserListItemListener);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserInfoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nickName", list.get(arg2).get("nickName").toString());
                bundle.putString("userName", list.get(arg2).get("userName").toString());
                bundle.putInt("userId", Integer.parseInt(list.get(arg2).get("userId").toString()));
                bundle.putInt("studentId", Integer.parseInt(list.get(arg2).get("studentId").toString()));
                bundle.putString("userPassword", list.get(arg2).get("userPassword").toString());
                bundle.putString("userType", list.get(arg2).get("userType").toString());
                bundle.putString("userPhone", list.get(arg2).get("userPhone").toString());
                bundle.putString("userGender", list.get(arg2).get("userGender").toString());
                bundle.putString("userEmail", list.get(arg2).get("userEmail").toString());
                bundle.putString("userMoney", list.get(arg2).get("userMoney").toString());
                bundle.putString("userReputation", list.get(arg2).get("userReputation").toString());
                bundle.putString("regTime", list.get(arg2).get("regTime").toString());
                bundle.putString("userAuthFile", list.get(arg2).get("userAuthFile").toString());
                bundle.putByteArray("photo", (byte[]) list.get(arg2).get("photo"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private View.OnCreateContextMenuListener UserListItemListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 0, 0, "编辑用户信息");
            menu.add(0, 1, 0, "删除用户信息");
        }
    };








    // 删除
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("确认删除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String result = UserService.DeleteUser(user_name);
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                setViews();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private List<Map<String, Object>> getDatas() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            /* 查询用户信息 */
            List<User> userList = userService.QueryUser(queryConditionUser);
            for (int i = 0; i < userList.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                if(userList.get(i).getUserType().equals("普通用户")) {  //普通用户
                    if(userList.get(i).getUserAuthFile().equals("--"))
                    {}
                    else {
                        map.put("userId", userList.get(i).getUserId());
                        map.put("userName", userList.get(i).getUserName());
                        map.put("userPassword", userList.get(i).getUserPassword());
                        byte[] userPhoto_data = null;
                        // 获取图片数据
                        userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + userList.get(i).getUserPhoto());
                        map.put("photo", userPhoto_data);
                        Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
                        map.put("userPhoto", userPhoto);
                        map.put("userType", userList.get(i).getUserType());
                        map.put("userPhone", userList.get(i).getUserPhone());
                        map.put("userGender", userList.get(i).getUserGender());
                        map.put("userEmail", userList.get(i).getUserEmail());
                        map.put("userReputation", userList.get(i).getUserReputation());
                        map.put("userMoney", userList.get(i).getUserMoney());
                        map.put("userAuthFile", userList.get(i).getUserAuthFile());
                        map.put("regTime", userList.get(i).getRegTime());
                        map.put("studentId", userList.get(i).getStudentId());
                        map.put("nickName", userList.get(i).getNickName());
				/*byte[] userPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ UserList.get(i).getUserPhoto());// 获取图片数据
				BitmapFactory.Options userPhoto_opts = new BitmapFactory.Options();
				userPhoto_opts.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length, userPhoto_opts);
				userPhoto_opts.inSampleSize = photoListActivity.computeSampleSize(userPhoto_opts, -1, 100*100);
				userPhoto_opts.inJustDecodeBounds = false;
				try {
					Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length, userPhoto_opts);
					map.put("userPhoto", userPhoto);
				} catch (OutOfMemoryError err) { }*/
//				map.put("userPhoto", HttpUtil.BASE_URL+ UserList.get(i).getUserPhoto());
//				map.put("telephone", UserList.get(i).getTelephone());
//				map.put("shenHeState", UserList.get(i).getShenHeState());
                        list.add(map);
                    }
                }
            }
        } catch (Exception e) {
            //	Toast.makeText(getApplicationContext(), "", 1).show();
        }
        return list;
    }





}
