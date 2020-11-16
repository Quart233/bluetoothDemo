package com.example.bluetoothdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private BluetoothAdapter bleAdapter;
    private ArrayAdapter<BluetoothDevice> devicesListAdapter;
    private BroadcastReceiver mReceiver;
    private static Handler mainHandler;
    private ArrayList<BluetoothDevice> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        setUpMainHandler();
        setUpBluetoothReceiver();
        setUpBluetooth();
        setUpDevicesList();
        setUpDiscoverButton();
        setUpStopDiscoverButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
    }

    private void setUpMainHandler() {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        }, 1);
    }

    private void setUpStopDiscoverButton() {
        Button button = findViewById(R.id.stopDiscover);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked");
                bleAdapter.cancelDiscovery();
            }
        });
    }

    private void setUpDiscoverButton() {
        Button button = findViewById(R.id.startDiscover);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked");
                devicesListAdapter.clear();
                bleAdapter.startDiscovery();
            }
        });
    }

    private void setUpDevicesList() {
        ListView devicesListView = findViewById(R.id.devicesList);
        deviceList = new ArrayList();
        devicesListAdapter = new ArrayAdapter(MainActivity.this, R.layout.ble_item, R.id.label, deviceList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(R.id.label);

                BluetoothDevice device = deviceList.get(position);
                tv.setText(device.getName());
                return view;
            }
        };

        AdapterView.OnItemClickListener devicesListOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: clicked");
                BluetoothDevice device = deviceList.get(i);
                Intent intent = new Intent(MainActivity.this, FaceProfileEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putParcelable("device", device);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        devicesListView.setAdapter(devicesListAdapter);
        devicesListView.setOnItemClickListener(devicesListOnItemClickListener);

        // load paired devices
        Set<BluetoothDevice> pairedDevices = bleAdapter.getBondedDevices();
        Log.d(TAG, "setUpDevicesList: " + pairedDevices.size());

        if(pairedDevices.size() > 0){
            for(BluetoothDevice device:pairedDevices) {
                // 把名字和地址取出来添加到适配器中
                devicesListAdapter.add(device);
            }
        }

    }

    private void setUpBluetoothReceiver() {
        // 创建一个接受 ACTION_FOUND 的 BroadcastReceiver
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String action = intent.getAction();
                Log.d(TAG, "onReceive: " + action);
                // 当 Discovery 发现了一个设备
                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    // 从 Intent 中获取发现的 BluetoothDevice
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // 将名字和地址放入要显示的适配器中
                    devicesListAdapter.add(device);
                }
            }
        };
        // 注册这个 BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver,filter);
    }

    private  void setUpBluetooth() {
        bleAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bleAdapter == null) {
            Toast.makeText(this, "该设备无蓝牙适配器", Toast.LENGTH_SHORT);
            return;
        }

        if (!bleAdapter.isEnabled()) {
            int REQUEST_ENABLE_BT = 1;
            Toast.makeText(this, "蓝牙未开启", Toast.LENGTH_SHORT);
            // 蓝牙授权
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
            return;
        }

        if (bleAdapter.isDiscovering()) {
            Log.d(TAG, "setUpBluetooth: cancel discovering");
            bleAdapter.cancelDiscovery();
        }
    }
}
