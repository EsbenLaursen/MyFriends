package com.example.esbenlaursen.myfriends;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Random;

public class FriendListView extends AppCompatActivity {

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    ArrayList<Friend> friends;
    DAO dao;
    ListView listView;
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_view);

        dao = new DAO(this);
        friends = (ArrayList) dao.getAll();


        adapter = new CustomAdapter(this, friends);

        listView = (ListView) findViewById(R.id.listFriends);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ShowFriend.class);
                Friend f = friends.get(position);
                intent.putExtra("id", f.id);
                startActivity(intent);
                finish();
            }
        });

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                Random random = new Random();
               int i = random.nextInt(friends.size());
                Friend f = friends.get(i);
                callPhoneNumber(f.getPhoneNumber());
            }
        });


    }

    public void AddFriend(View view) {
        Intent i = new Intent(this, AddFriendView.class);
        startActivity(i);
        finish();

    }



    public void Populate(View view)
    {
        AddDummyData();
    }

    public void AddDummyData(){
        Friend f1 = new Friend(1, "Esben Munk Laursen", "gl. vardevej 63, 6700 esbjerg", 55.467590, 8.446297,  "ebbe@gmail.com", "12465472", "Esben.com", "/storage/emulated/0/Android/data/com.example.esbenlaursen.myfriends/files/Pictures/JPEG_20170502_200351_-1393891881.jpg");
        Friend f2 = new Friend(2, "Emil Dall", "Jyllandsgade 33, 6700 Esbjerg",  55.467590, 8.446297, "Emil@gmail.com", "13465472", "Emil.com", "/storage/emulated/0/Android/data/com.example.esbenlaursen.myfriends/files/Pictures/JPEG_20170502_200351_-1393891881.jpg");
        Friend f3 = new Friend(3, "Kenny Jensen", "Skolegade 63, 6700 Esbjerg",55.467590, 8.446297,"Lars@gmail.com", "14465472", "Lars.com", "/storage/emulated/0/Android/data/com.example.esbenlaursen.myfriends/files/Pictures/JPEG_20170502_200351_-1393891881.jpg");
        Friend f4 = new Friend(4, "Lars Ost", "Skolegade 63, 6700 Esbjerg",  55.467590, 8.446297, "Lars@gmail.com", "15465472", "Lars.com", "/storage/emulated/0/Android/data/com.example.esbenlaursen.myfriends/files/Pictures/JPEG_20170502_200351_-1393891881.jpg");
        Friend f5 = new Friend(5, "No location2", "Nørregade 19, 6700 Esbjerg",-1, -1,"Lars@gmail.com", "16465472", "Lars.com", "/storage/emulated/0/Android/data/com.example.esbenlaursen.myfriends/files/Pictures/JPEG_20170502_200351_-1393891881.jpg");
        Friend f6 = new Friend(6, "no location3", "Nørregade 30, 6700 Esbjerg",  -1, -1, "Lars@gmail.com", "17465472", "Lars.com", "/storage/emulated/0/Android/data/com.example.esbenlaursen.myfriends/files/Pictures/JPEG_20170502_200351_-1393891881.jpg");
        Friend f7 = new Friend(7, "no loca or address", "gtrhtrehth",-1, -1,"Lars@gmail.com", "18465472", "Lars.com", "/storage/emulated/0/Android/data/com.example.esbenlaursen.myfriends/files/Pictures/JPEG_20170502_200351_-1393891881.jpg");

        dao.insert(f1);
        dao.insert(f2);
        dao.insert(f3);
        dao.insert(f4);
        dao.insert(f5);
        dao.insert(f6);
        dao.insert(f7);

        friends = (ArrayList) dao.getAll();
        adapter.updateList(friends);
    }

    public void DeleteAll(View view)
    {
        dao.deleteAll();
        friends.clear();
        adapter.updateList(friends);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }


    public void callPhoneNumber(String phone)
    {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
    }

}
