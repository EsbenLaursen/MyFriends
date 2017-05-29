package com.example.esbenlaursen.myfriends;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ShowFriend extends AppCompatActivity {

    DAO dao;
    int id;
    Friend f;
    private String mCurrentPhotoPath;
    TextView detailsTxt;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend);
        Intent i = getIntent();
        id = i.getIntExtra("id", 0);

        dao = new DAO(this);
         f = dao.getOne(id);

        String detail = f.name + "\n\n" + f.getPhoneNumber() + "\n\n" + f.getEmail() + "\n\n" + f.getURL() + "\n\n" + f.getAddress();

        detailsTxt =(TextView) findViewById(R.id.frienddetailTxt);
        detailsTxt.setText(detail);

        imageView = (ImageView) findViewById(R.id.imageView2);
        setImage();

    }



    public void GoBack(View view) {
        Intent i = new Intent(this, FriendListView.class);
        startActivity(i);
        finish();
    }


    public void MakeCall(View view)
    {
        String phone = "";
        if(f != null)
        {
            phone = f.getPhoneNumber().toString();
        }
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

    public void EditFriend(View view)
    {
        Intent i = new Intent(this, EditFriend.class);
        i.putExtra("id", id);
        startActivity(i);
        finish();
    }

    public void SendSMS(View view)
    {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                + 29863201)));

    }

    public void SendMail(View view)
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Esben.laursen@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ShowFriend.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }
    private void setImage()
    {
        if(f.getImage() != null && !f.getImage().isEmpty()){
            try {
                File imgFile = new File(f.getImage());
                if(imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                    mCurrentPhotoPath = f.getImage();
                }
            }catch (Exception e)
            {
                Log.e("read pic", "onCreate: ", e);
            }
        }
    }
}
