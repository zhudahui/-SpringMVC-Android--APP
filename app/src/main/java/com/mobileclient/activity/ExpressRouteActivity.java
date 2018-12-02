package com.mobileclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;

import com.mobileclient.util.AMapUtil;
import com.mobileclient.util.ToastUtil;

import overlay.WalkRouteOverlay;

/**
 * Route路径规划: 驾车规划、公交规划、步行规划
 */
public class ExpressRouteActivity extends Activity implements OnMapClickListener,GeocodeSearch.OnGeocodeSearchListener, OnClickListener,
        OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener,LocationSource,AMapLocationListener,RadioGroup.OnCheckedChangeListener {
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private WalkRouteResult mWalkRouteResult;
    private LatLonPoint mStartPoint = new LatLonPoint(39.071833, 117.116767);//起点，116.335891,39.942295
    private LatLonPoint mEndPoint = new LatLonPoint(39.071833, 117.116767);//终点，116.481288,39.995576
    private LatLonPoint mStartPoint_bus = new LatLonPoint(39.071833, 117.116767);//起点，111.670801,40.818311
    private LatLonPoint mEndPoint_bus = new LatLonPoint(39.071833, 117.116767);;//终点，
    private LatLonPoint mEnd;
    private String mCurrentCityName = "北京";
    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;
    private final int ROUTE_TYPE_CROSSTOWN = 4;
    private double lat ;
    private double lon;
    private int i=0 ;
    private LinearLayout mBusResultLayout;
    private RelativeLayout mBottomLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    //private ImageView mBus;
    //private ImageView mDrive;
    // private ListView mBusResultList;
    private ProgressDialog progDialog = null;// 搜索时进度条

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private RadioGroup mGPSModeGroup;

    private TextView mLocationErrText;
    //===============================地理编码==============================================================

    private GeocodeSearch geocoderSearch;
    private String addressName;

    private Marker geoMarker;
    private TextView btn_search;
    private EditText edt;
    private double lat1,lon1;
    private String value;

    //===================================地理编码===============================================================
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_express_route);

        mContext = this.getApplicationContext();
        mapView = (MapView) findViewById(R.id.route_map);
        mapView.onCreate(bundle);// 此方法必须重写
        init();
//		getIntentData();
        setfromandtoMarker();
        Intent intent = getIntent();
        if (intent != null) {


            value =  intent .getStringExtra("point");

            Log.i("22222222222222222222", String.valueOf(value));

            getLatlon(value);
        }
        /***
         *
         *
         * 定位
         */
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        mLocationOption.setOnceLocation(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
        mLocationOption.setOnceLocation(false);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        registerListener();
       // mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
//        mGPSModeGroup.setOnCheckedChangeListener(this);
        mLocationErrText = (TextView)findViewById(R.id.location_errInfo_text);
        mLocationErrText.setVisibility(View.GONE);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);

        //	mDrive = (ImageView)findViewById(R.id.route_drive);
        //mBus = (ImageView)findViewById(R.id.route_bus);
        //mWalk = (ImageView)findViewById(R.id.route_walk);
        //mBusResultList = (ListView) findViewById(R.id.bus_result_list);
        //Button geoButton = (Button) findViewById(R.id.geoButton);
   //     btn_search=findViewById(R.id.btn_search);
    //    edt=findViewById(R.id.edt);

        //geoButton.setText("GeoCoding(天津工业大学)");
   //     btn_search.setOnClickListener(this);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(ExpressRouteActivity.this);
        aMap.setOnMarkerClickListener(ExpressRouteActivity.this);
        aMap.setOnInfoWindowClickListener(ExpressRouteActivity.this);
        aMap.setInfoWindowAdapter(ExpressRouteActivity.this);

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

//	/**
//	 * 公交路线搜索
//     */
//	public void onBusClick(View view) {
//		searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
//		mDrive.setImageResource(R.drawable.route_drive_normal);
//		mBus.setImageResource(R.drawable.route_bus_select);
//		mWalk.setImageResource(R.drawable.route_walk_normal);
//		mapView.setVisibility(View.GONE);
//		mBusResultLayout.setVisibility(View.VISIBLE);
//	}

//	/**
//	 * 驾车路线搜索
//	 */
//	public void onDriveClick(View view) {
//		searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
//		mDrive.setImageResource(R.drawable.route_drive_select);
//		mBus.setImageResource(R.drawable.route_bus_normal);
//		mWalk.setImageResource(R.drawable.route_walk_normal);
//		mapView.setVisibility(View.VISIBLE);
//		mBusResultLayout.setVisibility(View.GONE);
//	}

//	/**
//	 * 步行路线搜索
//     */
//	public void onWalkClick(View view) {
//		searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
//		mDrive.setImageResource(R.drawable.route_drive_normal);
//		mBus.setImageResource(R.drawable.route_bus_normal);
//		mWalk.setImageResource(R.drawable.route_walk_select);
//		mapView.setVisibility(View.VISIBLE);
//		mBusResultLayout.setVisibility(View.GONE);
//	}
//
//	/**
//	 * 跨城公交路线搜索
//     */
//	public void onCrosstownBusClick(View view) {
//		searchRouteResult(ROUTE_TYPE_CROSSTOWN, RouteSearch.BusDefault);
//		mDrive.setImageResource(R.drawable.route_drive_normal);
//		mBus.setImageResource(R.drawable.route_bus_normal);
//		mWalk.setImageResource(R.drawable.route_walk_normal);
//		mapView.setVisibility(View.GONE);
//		mBusResultLayout.setVisibility(View.VISIBLE);
//	}
//
    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        mStartPoint=new LatLonPoint(lat,lon);
        mEndPoint=new LatLonPoint(lat1,lon1);
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "起点未设置");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置");
        }
        //showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);

        WalkRouteQuery query = new WalkRouteQuery(fromAndTo);
        mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询

    }

    /**
     * 公交路线搜索结果方法回调
     */
    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        dissmissProgressDialog();
//		mBottomLayout.setVisibility(View.GONE);
//		aMap.clear();// 清理地图上的所有覆盖物
//		if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
//			if (result != null && result.getPaths() != null) {
//				if (result.getPaths().size() > 0) {
//					mBusRouteResult = result;
//					BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
//				//	mBusResultList.setAdapter(mBusResultListAdapter);
//				} else if (result != null && result.getPaths() == null) {
//					ToastUtil.show(mContext, R.string.no_result);
//				}
//			} else {
//				ToastUtil.show(mContext, R.string.no_result);
//			}
//		} else {
//			ToastUtil.showerror(this.getApplicationContext(), errorCode);
//		}
    }

    /**
     * 驾车路线搜索结果方法回调
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
//		dissmissProgressDialog();
//		aMap.clear();// 清理地图上的所有覆盖物
//		if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
//			if (result != null && result.getPaths() != null) {
//				if (result.getPaths().size() > 0) {
//					mDriveRouteResult = result;
//					final DrivePath drivePath = mDriveRouteResult.getPaths()
//							.get(0);
//					if(drivePath == null) {
//						return;
//					}
//					DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
//							mContext, aMap, drivePath,
//							mDriveRouteResult.getStartPos(),
//							mDriveRouteResult.getTargetPos(), null);
//					drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
//					drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
//					drivingRouteOverlay.removeFromMap();
//					drivingRouteOverlay.addToMap();
//					drivingRouteOverlay.zoomToSpan();
//					mBottomLayout.setVisibility(View.VISIBLE);
//					int dis = (int) drivePath.getDistance();
//					int dur = (int) drivePath.getDuration();
//					String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
//					mRotueTimeDes.setText(des);
//					mRouteDetailDes.setVisibility(View.VISIBLE);
//					int taxiCost = (int) mDriveRouteResult.getTaxiCost();
//					mRouteDetailDes.setText("打车约"+taxiCost+"元");
//					mBottomLayout.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							Intent intent = new Intent(mContext,
//									DriveRouteDetailActivity.class);
//							intent.putExtra("drive_path", drivePath);
//							intent.putExtra("drive_result",
//									mDriveRouteResult);
//							startActivity(intent);
//						}
//					});
//				} else if (result != null && result.getPaths() == null) {
//					ToastUtil.show(mContext, R.string.no_result);
//				}
//
//			} else {
//				ToastUtil.show(mContext, R.string.no_result);
//			}
//		} else {
//			ToastUtil.showerror(this.getApplicationContext(), errorCode);
//		}


    }

    /**
     * 步行路线搜索结果方法回调
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    if(walkPath == null) {
                        return;
                    }
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
                    mBottomLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//							Intent intent = new Intent(mContext,
//									WalkRouteDetailActivity.class);
//							intent.putExtra("walk_path", walkPath);
//							intent.putExtra("walk_result",
//									mWalkRouteResult);
//							startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }


//	/**
//	 * 显示进度框
//	 */
//	private void showProgressDialog() {
//		if (progDialog == null) {
//			progDialog = new ProgressDialog(this);
//		}
//		    progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		    progDialog.setIndeterminate(false);
//		    progDialog.setCancelable(true);
//		    progDialog.setMessage("正在搜索");
//		    progDialog.show();
//	    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }



    /**
     * 骑行路线搜索结果方法回调
     */
    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }
    /**
     * 初始化
     */


    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(mLocationOption == null || mlocationClient == null) {
            return;
        }
        switch (checkedId) {
            case R.id.gps_locate_button:
                // 设置定位的类型为定位模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
                mLocationOption.setOnceLocation(true);
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                mLocationOption.setOnceLocation(false);
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();

                break;
            case R.id.gps_follow_button:
                // 设置定位的类型为 跟随模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
                mLocationOption.setOnceLocation(false);
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();
                break;
            case R.id.gps_rotate_button:
                // 设置定位的类型为根据地图面向方向旋转
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                mLocationOption.setOnceLocation(false);
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();
                break;
        }

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mLocationErrText.setVisibility(View.GONE);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                lat = amapLocation.getLatitude();//获取纬度
                lon = amapLocation.getLongitude();//获取经度
                if(i==0) {
                    searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
                    //mWalk.setImageResource(R.drawable.route_walk_select);
                    mapView.setVisibility(View.VISIBLE);
                    mBusResultLayout.setVisibility(View.GONE);
                    i=1;
                    Log.i("11", "1111111111");
                }

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
        }
    }
    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
    //===================================================================
    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        //	showDialog();

        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求

    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        //	dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);

                if(address != null) {
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                    geoMarker.setPosition(AMapUtil.convertToLatLng(address
                            .getLatLonPoint()));

                    lon1=address.getLatLonPoint().getLongitude();
                    lat1=address.getLatLonPoint().getLatitude();
                    addressName = "经纬度值:" + lon1 + "\n位置描述:"
                            + lat1;
                    ToastUtil.show(ExpressRouteActivity.this, addressName);
                    searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
                    //mWalk.setImageResource(R.drawable.route_walk_select);
                    mapView.setVisibility(View.VISIBLE);
                    mBusResultLayout.setVisibility(View.GONE);
                    //i=1;
                    Log.i("11", "aaaaaaaaaaaaaaaaaaaaa");

                }
            } else {
                ToastUtil.show(ExpressRouteActivity.this, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            /**
//             * 响应地理编码按钮
//             */
//            case R.id.btn_search:
//                getLatlon(String.valueOf(edt.getText()));
//                break;
//            default:
//                break;
//        }
    }
}

