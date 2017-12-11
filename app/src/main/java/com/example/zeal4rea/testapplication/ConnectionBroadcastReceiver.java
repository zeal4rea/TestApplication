package com.example.zeal4rea.testapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

public class ConnectionBroadcastReceiver extends BroadcastReceiver {
    private Handler handler;

    public ConnectionBroadcastReceiver(Handler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Message msg = new Message();
            msg.what = 101;
            switch (state) {
                case WifiManager.WIFI_STATE_ENABLING:
                    msg.obj = "WIFI_STATE_ENABLING";
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    msg.obj = "WIFI_STATE_ENABLED";
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    msg.obj = "WIFI_STATE_DISABLING";
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    msg.obj = "WIFI_STATE_DISABLED";
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    msg.obj = "WIFI_STATE_UNKNOWN";
                    break;
            }
            handler.sendMessage(msg);
        }
    }
}
