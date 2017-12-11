package com.example.zeal4rea.testapplication;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zeal4rea.testapplication.utils.CommonUtils;

import java.util.List;

public class WifiListActivity extends Activity {
    private WifiListAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 111 && msg.obj != null) {
                adapter.refreshData((List<ScanResult>) msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        List<ScanResult> data = wifiManager.getScanResults();
        ListView wifiList = (ListView) findViewById(R.id.Wifi$lv_list);
        adapter = new WifiListAdapter(this, data);
        wifiList.setAdapter(adapter);
    }

    private class WifiListAdapter extends BaseAdapter {
        private Context context;
        private List<ScanResult> data;
        private ViewHolder holder;

        public WifiListAdapter(Context context, List<ScanResult> data) {
            super();
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.wifi_list_item, viewGroup, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.bssid = (TextView) convertView.findViewById(R.id.Wifi$tv_bssid);
                holder.ssid = (TextView) convertView.findViewById(R.id.Wifi$tv_ssid);
                holder.cababilities = (TextView) convertView.findViewById(R.id.Wifi$tv_capabilities);
                holder.frequency = (TextView) convertView.findViewById(R.id.Wifi$tv_frequency);
                holder.level = (TextView) convertView.findViewById(R.id.Wifi$tv_level);
                holder.timestamp = (TextView) convertView.findViewById(R.id.Wifi$tv_timestamp);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (!CommonUtils.isNullOrEmpty(data)) {
                ScanResult result = (ScanResult) getItem(position);
                Log.d("lzl", result.BSSID + "," + result.SSID + "," + result.capabilities + "," + result.frequency + "," + result.level + "," + result.timestamp);
                holder.bssid.setText(result.BSSID);
                holder.ssid.setText(result.SSID);
                holder.cababilities.setText(result.capabilities);
                holder.frequency.setText(String.valueOf(result.frequency).concat("MHz"));
                holder.level.setText(String.valueOf(result.level).concat("dBm"));
                holder.timestamp.setText(String.valueOf(result.timestamp));
            }
            return convertView;
        }

        public void refreshData(List<ScanResult> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return CommonUtils.isNullOrEmpty(data) ? 0 : data.size();
        }

        @Override
        public Object getItem(int i) {
            return CommonUtils.isNullOrEmpty(data) ? null : data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        private class ViewHolder {
            public TextView bssid;
            public TextView ssid;
            public TextView cababilities;
            public TextView frequency;
            public TextView level;
            public TextView timestamp;
        }
    }
}
