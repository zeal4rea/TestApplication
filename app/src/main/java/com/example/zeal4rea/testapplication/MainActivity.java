package com.example.zeal4rea.testapplication;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.zeal4rea.testapplication.utils.CommonUtils;

public class MainActivity extends Activity {
    private LocationClient mLocClient = null;
    private BDAbstractLocationListener mLocListener = new MyLocListener();
    private TextView tvInfo;
    private ConnectionBroadcastReceiver mReceiver;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101 && msg.obj != null) {
                String s = String.valueOf(msg.obj);
                addTextAndToast(s);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(mLocListener);
        initLocation();
        tvInfo = (TextView) findViewById(R.id.Main$tv_info);
        Button btStart = (Button) findViewById(R.id.Main$bt_start);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTextAndToast("开始定位:" + System.currentTimeMillis());
                mLocClient.start();
            }
        });
        Button btStop = (Button) findViewById(R.id.Main$bt_stop);
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLocClient.isStarted()) {
                    addTextAndToast("停止定位:" + System.currentTimeMillis());
                    mLocClient.stop();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new ConnectionBroadcastReceiver(mHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocClient.isStarted()) {
            mLocClient.stop();
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        mLocClient.setLocOption(option);
    }

    private void addTextAndToast(String msg) {
        tvInfo.setText(tvInfo.getText().toString() + msg + "\n");
        CommonUtils.showToast(MainActivity.this, msg);
        Log.d("lzl", msg);
    }

    private class MyLocListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuilder sb = new StringBuilder();//获取定位结果
            sb.append("定位时间:").append(location.getTime()).append("\n");    //获取定位时间
            //sb.append("定位唯一ID:").append(location.getLocationID()).append("\n");    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            sb.append("定位类型:").append(location.getLocType()).append("\n");    //获取定位类型
            sb.append("纬度信息:").append(location.getLatitude()).append("\n");    //获取纬度信息
            sb.append("经度信息:").append(location.getLongitude()).append("\n");    //获取经度信息
            //sb.append("定位精准度:").append(location.getRadius()).append("\n");    //获取定位精准度
            sb.append("地址:").append(location.getAddrStr()).append("\n");    //获取地址信息
            sb.append("国家信息:").append(location.getCountry()).append("\n");    //获取国家信息
            //sb.append("国家码:").append(location.getCountryCode()).append("\n");    //获取国家码
            sb.append("城市信息:").append(location.getCity()).append("\n");    //获取城市信息
            //sb.append("城市码:").append(location.getCityCode()).append("\n");    //获取城市码
            sb.append("区县信息:").append(location.getDistrict()).append("\n");    //获取区县信息
            sb.append("街道信息:").append(location.getStreet()).append("\n");    //获取街道信息
            //sb.append("街道码:").append(location.getStreetNumber()).append("\n");    //获取街道码
            sb.append("当前位置描述信息:").append(location.getLocationDescribe()).append("\n");    //获取当前位置描述信息
            //sb.append("当前位置周边POI信息").append(location.getPoiList()).append("\n");    //获取当前位置周边POI信息
            //sb.append("楼宇ID:").append(location.getBuildingID()).append("\n");    //室内精准定位下，获取楼宇ID
            //sb.append("楼宇名称:").append(location.getBuildingName()).append("\n");    //室内精准定位下，获取楼宇名称
            //sb.append("楼层信息:").append(location.getFloor()).append("\n");    //室内精准定位下，获取当前位置所处的楼层信息
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                //当前为GPS定位结果，可获取以下信息
                sb.append("GPS定位结果").append("\n");
                sb.append("当前速度:").append(location.getSpeed()).append("\n");    //获取当前速度，单位：公里每小时
                sb.append("当前卫星数:").append(location.getSatelliteNumber()).append("\n");    //获取当前卫星数
                sb.append("海拔高度:").append(location.getAltitude()).append("\n");    //获取海拔高度信息，单位米
                sb.append("方向:").append(location.getDirection()).append("\n");    //获取方向信息，单位度
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                //当前为网络定位结果，可获取以下信息
                sb.append("网络定位结果").append("\n");
                sb.append("运营商:").append(location.getOperators()).append("\n");    //获取运营商信息
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                //当前为网络定位结果
                sb.append("离线定位结果").append("\n");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com
                sb.append("网络定位失败").append("\n");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                //当前网络不通
                sb.append("网络不通").append("\n");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码
                sb.append("缺少定位依据").append("\n");
            }
            addTextAndToast(sb.toString());
            mLocClient.stop();
            addTextAndToast("停止定位:" + System.currentTimeMillis());
            /*addTextAndToast("地址:" + location.getAddrStr() + "\n"
                    + "位置描述:" + location.getLocationDescribe() + "\n"
                    + "经纬度:" + location.getLongitude() + "," + location.getLatitude());*/
        }

        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
            addTextAndToast("诊断信息:" + String.valueOf(locType) + "," + String.valueOf(diagnosticType) + "," + diagnosticMessage);
        }
    }
}
