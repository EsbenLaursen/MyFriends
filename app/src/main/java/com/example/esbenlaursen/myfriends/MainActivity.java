package com.example.esbenlaursen.myfriends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

        DAO dao = new DAO(this);

      TextView textView = (TextView) findViewById(R.id.txtTotalFriends);
        textView.setText(dao.getAll().size()+"");
    }

    public void friendsViewOnClick(View view){

        // startActivity(new Intent(this, AndroidDatabaseManager.class));
          startActivity(new Intent(this, FriendListView.class));
    }







}
