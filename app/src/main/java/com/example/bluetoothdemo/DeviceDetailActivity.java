package com.example.bluetoothdemo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import java.util.UUID;

public class DeviceDetailActivity extends AppCompatActivity {
    private BluetoothDevice device;
    private BluetoothGattCallback bluetoothGattCallback;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private Handler handler;
    private String TAG = "DeviceDetailActivity";
    private BluetoothGatt bluetoothGatt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        Intent intent = this.getIntent();
        device = intent.getParcelableExtra("device");

        setUpHandler();
        setUpContentLoadingProgressBar();
        setUpBluetoothGattCallback();
        setUpHelloWorldButton();
        connect();
    }

    private void setUpHelloWorldButton() {
        Button hellworld = findViewById(R.id.helloWorld);
        hellworld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Service_uuid = "0000ffe0-0000-1000-8000-00805f9b34fb";
                String Characteristic_uuid_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";
                BluetoothGattCharacteristic gg = bluetoothGatt.getService(UUID.fromString(Service_uuid)).getCharacteristic(UUID.fromString(Characteristic_uuid_TX));
                gg.setValue("Hello World");
                bluetoothGatt.writeCharacteristic(gg);
            }
        });
    }

    private void setUpHandler() {
        handler = new Handler(Looper.getMainLooper());
    }

    private void setUpContentLoadingProgressBar() {
        contentLoadingProgressBar = new ContentLoadingProgressBar(DeviceDetailActivity.this);
    }

    private void setUpBluetoothGattCallback() {
        bluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                Log.d(TAG, "onConnectionStateChange: " + newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d(TAG, "onConnectionStateChange: 连接成功");
                    gatt.discoverServices();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            contentLoadingProgressBar.hide();
                        }
                    });
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                Log.d(TAG, "onCharacteristicChanged: " + characteristic.getStringValue(0));
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                Log.d(TAG, "onCharacteristicRead: " + characteristic.getStringValue(0));
            }



            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                Log.d(TAG, "onCharacteristicWrite: " + characteristic.getStringValue(0));
            }
        };
    }

    private void connect() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                contentLoadingProgressBar.show();
            }
        });
        bluetoothGatt = device.connectGatt(DeviceDetailActivity.this, true, bluetoothGattCallback);
    }
}
