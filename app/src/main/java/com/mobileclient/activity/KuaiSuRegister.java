//package com.mobileclient.activity;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//import cn.bmob.v3.BmobSMS;
//import cn.bmob.v3.BmobUser;
//import cn.bmob.v3.exception.BmobException;
//import cn.bmob.v3.listener.LogInListener;
//import cn.bmob.v3.listener.QueryListener;
//
///**
// * Created on 18/9/25 16:12
// * TODO 通过短信验证注册或登录
// *
// * @author zhangchaozhou
// */
//public class KuaiSuRegister extends AppCompatActivity {
//
//    EditText mEdtPhone;
//    EditText mEdtCode;
//    TextView mTvInfo;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.zhijie_register);
//        mEdtPhone=findViewById(R.id.edt_phone);
//        mEdtCode=findViewById(R.id.edt_code);
//        mTvInfo=findViewById(R.id.tv_info);
//
//
//    }
//
//
//    @OnClick({R.id.btn_send, R.id.btn_signup_or_login})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_send: {
//                String phone = mEdtPhone.getText().toString().trim();
//                if (TextUtils.isEmpty(phone)) {
//                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                /**
//                 * TODO template 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；如果没有对应的自定义短信模板，则使用默认短信模板。
//                 */
//                BmobSMS.requestSMSCode(phone, "知么网络", new QueryListener<Integer>() {
//                    @Override
//                    public void done(Integer smsId, BmobException e) {
//                        if (e == null) {
//                            mTvInfo.append("发送验证码成功，短信ID：" + smsId + "\n");
//                        } else {
//                            mTvInfo.append("发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
//                        }
//                    }
//                });
//                break;
//            }
//            case R.id.btn_signup_or_login: {
//                String phone = mEdtPhone.getText().toString().trim();
//                if (TextUtils.isEmpty(phone)) {
//                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String code = mEdtCode.getText().toString().trim();
//                if (TextUtils.isEmpty(code)) {
//                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<BmobUser>() {
//                    @Override
//                    public void done(BmobUser bmobUser, BmobException e) {
//                        if (e == null) {
//                            mTvInfo.append("短信注册或登录成功：" + bmobUser.getUsername());
//                            //startActivity(new Intent(UserSignUpOrLoginSmsActivity.this, UserMainActivity.class));
//                        } else {
//                            mTvInfo.append("短信注册或登录失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
//                        }
//                    }
//                });
//                break;
//            }
//
//            default:
//                break;
//        }
//    }
//}
