package com.example.bluetoothdemo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

public class DeviceDetailActivity extends AppCompatActivity {
    private BluetoothDevice device;
    private BluetoothGattCallback bluetoothGattCallback;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        Intent intent = this.getIntent();
        device = intent.getParcelableExtra("device");

        setUpHandler();
        setUpContentLoadingProgressBar();
        setUpBluetoothGattCallback();
        connect();
    }

    private void setUpHandler() {
        handler = new Handler(Looper.getMainLooper());
    }

    private void setUpContentLoadingProgressBar() {
        contentLoadingProgressBar = new ContentLoadingProgressBar(this);
    }

    private void setUpBluetoothGattCallback() {
        bluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        contentLoadingProgressBar.show();
                    }
                });

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // handler.post(new Runnable() {
                    //     @Override
                    //     public void run() {
                    //         contentLoadingProgressBar.hide();
                    //     }
                    // });
                }
            }
        };
    }

    private void connect() {
        device.connectGatt(DeviceDetailActivity.this, false, bluetoothGattCallback);
    }
}
