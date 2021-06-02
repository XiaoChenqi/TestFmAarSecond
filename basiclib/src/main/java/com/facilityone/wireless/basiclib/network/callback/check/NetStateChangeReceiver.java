package com.facilityone.wireless.basiclib.network.callback.check;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.blankj.utilcode.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * <p/>
 * Email: xuhaozv@163.com
 * <p/>
 * description:接收者 接受网络状态变化
 * <p/>
 * Date: 2020-07-27 16:17
 */
public class NetStateChangeReceiver extends BroadcastReceiver {

    private static class InstanceHolder {
        private static final NetStateChangeReceiver INSTANCE = new NetStateChangeReceiver();
    }

    private List<NetStateChangeObserver> mObservers = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean connected = NetworkUtils.isConnected();
            notifyObservers(connected);
        }
    }

    public static void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(InstanceHolder.INSTANCE, intentFilter);
    }

    public static void unRegisterReceiver(Context context) {
        try {
            context.unregisterReceiver(InstanceHolder.INSTANCE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void registerObserver(NetStateChangeObserver observer) {
        if (observer == null) {
            return;
        }
        if (!InstanceHolder.INSTANCE.mObservers.contains(observer)) {
            InstanceHolder.INSTANCE.mObservers.add(observer);
        }
    }

    public static void unRegisterObserver(NetStateChangeObserver observer) {
        if (observer == null) {
            return;
        }
        if (InstanceHolder.INSTANCE.mObservers == null) {
            return;
        }
        InstanceHolder.INSTANCE.mObservers.remove(observer);
    }

    private void notifyObservers(boolean connected) {
        if (connected) {
            for (NetStateChangeObserver observer : mObservers) {
                observer.onNetConnected();
            }
        } else {
            for (NetStateChangeObserver observer : mObservers) {
                observer.onNetDisconnected();
            }
        }
    }

}

