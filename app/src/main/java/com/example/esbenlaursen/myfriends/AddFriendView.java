package com.example.esbenlaursen.myfriends;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddFriendView extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    String mCurrentPhotoPath;
    File imageFile;

    ImageView imageView;

    DAO dao;

    Friend friend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_view);
        imageView = (ImageView) findViewById(R.id.imageViewUploaddPic);
        dao = new DAO(this);
    }

    public void Save(View view)
    {
        EditText txtName = (EditText) findViewById(R.id.etName);
        EditText txtAddress = (EditText) findViewById(R.id.etAddress);
        EditText txtPhone = (EditText) findViewById(R.id.etPhone);
        EditText txtURL = (EditText) findViewById(R.id.etURL);
        EditText txtEmail = (EditText) findViewById(R.id.etEmail);

        //TO DO
        boolean isValid = CheckInputs(txtName, txtAddress, txtPhone, txtURL, txtEmail);
        if(!isValid)
        {
            return;
        }

        //Default destination
        double lat = -100;
        double lon = -100;
        Location loc = TryFetchLocationFromAddress(txtAddress.getText().toString());
        if(loc != null)
        {
           lat = loc.getLatitude();
           lon = loc.getLongitude();
        }

        Log.d("long", ""+lon);
        Log.d("lat", ""+lat);

        String imagePath = "";
        if(mCurrentPhotoPath != null) {
            imagePath = mCurrentPhotoPath;
        }


        friend = new Friend(1, txtName.getText().toString(), txtAddress.getText().toString(), lat, lon,
                txtPhone.getText().toString(), txtURL.getText().toString(), txtEmail.getText().toString(), imagePath);

        dao.insert(friend);

        Intent i = new Intent(this, FriendListView.class);
        startActivity(i);
        finish();
    }

    private Location TryFetchLocationFromAddress(String adress) {
        Location loc = null;

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(adress, 5);
            if (address == null) {
                return null;
            }
            try {
                Address addressLocation = address.get(0);
                loc = new Location("loc");
                loc.setLatitude(addressLocation.getLatitude());
                loc.setLongitude(addressLocation.getLongitude());
            } catch (IndexOutOfBoundsException e) {
                Toast.makeText(this, "No location was added to your friend", Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (IOException e)
        {
            return null;
        }
        return loc;
    }

    private boolean CheckInputs(EditText txtName, EditText txtAddress, EditText txtPhone, EditText txtURL, EditText txtEmail) {
        if(txtName.getText().toString().matches(""))
        {
            Toast.makeText(this, "Please fill out name field", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtEmail.getText().toString().matches(""))
        {
            Toast.makeText(this, "Please fill out the email field", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtPhone.getText().toString().matches(""))
        {
            Toast.makeText(this, "Please fill out phone field", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtAddress.getText().toString().matches(""))
        {
            Toast.makeText(this, "Please fill out address field", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(txtURL.getText().toString().matches(""))
        {
            Toast.makeText(this, "Please fill out the URL field", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void Cancel(View view)
    {
        Intent i = new Intent(this, FriendListView.class);
        startActivity(i);
        finish();
    }

    public void TakePicture(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException ex) { }
            if(photoFile != null)
            {
                Uri photoUri = FileProvider.getUriForFile(this,"com.example.esbenlaursen.myfriends.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            try {
                File imageFile = new File(mCurrentPhotoPath);
                if(imageFile.exists())
                {
                    Bitmap myBitMap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitMap);
                }
            }
            catch (Exception e) { }
        }
    }
}
