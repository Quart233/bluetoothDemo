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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;

import java.util.UUID;
import java.util.concurrent.BlockingDeque;

public class DeviceDetailActivity extends AppCompatActivity {
    private WebView webView;
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
         setUpTitleBarButton();
        setUpBluetoothGattCallback();
//        setUpHelloWorldButton();
        setUpWebview();
        connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.apply:
                // User chose the "Settings" item, show the app settings UI...
                Log.d(TAG, "onOptionsItemSelected: action clicked");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setUpTitleBarButton() {
//        Toolbar myToolbar = findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
        Button button = findViewById(R.id.apply);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.evaluateJavascript("javascript:getBits()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Log.d(TAG, "onReceiveValue: " + value);
                        String Service_uuid = "0000ffe0-0000-1000-8000-00805f9b34fb";
                        String Characteristic_uuid_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";
                        BluetoothGattCharacteristic gg = bluetoothGatt.getService(UUID.fromString(Service_uuid)).getCharacteristic(UUID.fromString(Characteristic_uuid_TX));
                        gg.setValue(value);
                        bluetoothGatt.writeCharacteristic(gg);
                    }
                });
            }
        });
    }

    private void setUpWebview() {
        webView = findViewById(R.id.webView);

        webView.loadUrl("file:///android_asset/www/index.html");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebContentsDebuggingEnabled(true);
    }

//    private void setUpHelloWorldButton() {
//        Button hellworld = findViewById(R.id.helloWorld);
//        hellworld.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String Service_uuid = "0000ffe0-0000-1000-8000-00805f9b34fb";
//                String Characteristic_uuid_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";
//                BluetoothGattCharacteristic gg = bluetoothGatt.getService(UUID.fromString(Service_uuid)).getCharacteristic(UUID.fromString(Characteristic_uuid_TX));
//                gg.setValue("Hello World");
//                bluetoothGatt.writeCharacteristic(gg);
//            }
//        });
//    }

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
