package com.mobileclient.activity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;

import android.app.Activity;
import android.os.Bundle;

public class TjpuMapActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		MapView mapView = (MapView) findViewById(R.id.map);//找到地图控件
		//在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
		mapView.onCreate(savedInstanceState);
		AMap aMap = mapView.getMap();//初始化地图控制器对象
	}
}
