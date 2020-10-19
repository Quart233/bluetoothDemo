package com.example.bluetoothdemo;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BleRecevier extends BroadcastReceiver {
    private String TAG = "BleRecevier";
    @Override
    public void onReceive(Context context, Intent intent){
        String action = intent.getAction();
        // 当 Discovery 发现了一个设备
        if(BluetoothDevice.ACTION_FOUND.equals(action)){
            // 从 Intent 中获取发现的 BluetoothDevice
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // 将名字和地址放入要显示的适配器中
            // devicesListAdapter.add(device.getName() + "\n" + device.getAddress());
            String deivce = device.getName() + "\n" + device.getAddress();
            Log.d(TAG, "onReceive: " + deivce);
        }
    }
}
