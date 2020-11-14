package com.example.bluetoothdemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FaceProfileActivity extends AppCompatActivity {
    private MySQLiteHelper blcs;
    private ArrayList<FaceProfile> faceProfileList;
    private ArrayAdapter<FaceProfile> faceProfileListAdapter;
    private String TAG = "FaceProfileActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpSQLiteDatabaseHelper();
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return super.onWindowStartingActionMode(callback);
    }

    private void setUpSQLiteDatabaseHelper()
    {
        blcs = new MySQLiteHelper(this, "Blcs", null, 1);
    }

    private void setUpFaceProfileList() {
        SQLiteDatabase db = blcs.getReadableDatabase();
        String query = "select * from FaceProfiles";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            FaceProfile profile = new FaceProfile();
            profile.name = name;
            faceProfileListAdapter.add(profile);
        }


        ListView devicesListView = findViewById(R.id.devicesList);
        faceProfileList = new ArrayList();
        faceProfileListAdapter = new ArrayAdapter(FaceProfileActivity.this, R.layout.ble_item, R.id.label, faceProfileList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(R.id.label);

                FaceProfile profile = faceProfileList.get(position);
                tv.setText(profile.name);
                return view;
            }
        };

        AdapterView.OnItemClickListener devicesListOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: clicked");
//                BluetoothDevice device = faceProfileListAdapter.get(i);
//                Intent intent = new Intent(FaceProfileActivity.this, DeviceDetailActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("device", device);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        };
        devicesListView.setAdapter(faceProfileListAdapter);
        devicesListView.setOnItemClickListener(devicesListOnItemClickListener);
    }
}
