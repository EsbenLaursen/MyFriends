package com.example.esbenlaursen.myfriends;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditFriend extends AppCompatActivity implements OnMapReadyCallback {


    DAO dao;
    int id;
    Friend f;

    //picture
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String LOGTAG = "Camera01";
    private String mCurrentPhotoPath;

    //LocationManagement
    GoogleMap mMap;
    Location location;

    //Fields
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPhone;
    EditText editTextAddress;
    EditText editTextHomepage;
    ImageView imageView;
    Button takepic;
    Button save;
    Button cancel;
    Button currentLoc;
    Button locationFromAddress;

    //my own location manager
    LocationStuff ls = new LocationStuff();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);
        String dss = Environment.getExternalStorageDirectory().getAbsolutePath();
        dao = new DAO(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
       id = i.getIntExtra("id", 0);

        f = dao.getOne(id);



        locationFromAddress = (Button) findViewById(R.id.btnLocationFromAddress);
        currentLoc = (Button) findViewById(R.id.btnCurrentLoc);

        editTextName = (EditText) findViewById(R.id.txtEditName);
        editTextName.setText(f.name);
        editTextEmail = (EditText) findViewById(R.id.txtEditEmail);
        editTextEmail.setText(f.email);
        editTextPhone = (EditText) findViewById(R.id.txtEditPhone);
        editTextPhone.setText(f.phoneNumber);
        editTextAddress = (EditText) findViewById(R.id.txtEditAddress);
        editTextAddress.setText(f.address);
        editTextHomepage = (EditText) findViewById(R.id.txtEditHomepage);
        editTextHomepage.setText(f.URL);
        imageView = (ImageView) findViewById(R.id.imageViewEditPic);
      //  imageView.setImageResource(R.drawable.therock);

        takepic = (Button) findViewById(R.id.btnTakepicture);
        save = (Button) findViewById(R.id.btnEditSave);
        cancel = (Button) findViewById(R.id.btnEditCancel);

        setImage();
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

    public void Delete(View view)
    {
         dao.deleteOne(f.id);
        Toast.makeText(this, "You've lost a friend xD", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, FriendListView.class);
        startActivity(i);
        finish();
    }

    public void SaveFriend(View view)
    {
        f.name = editTextName.getText().toString();
        f.email = editTextEmail.getText().toString();
        f.phoneNumber = editTextPhone.getText().toString();
        f.address = editTextAddress.getText().toString();
        f.URL = editTextHomepage.getText().toString();

        if(location != null)// should never be null tho
        {
            f.setLongitude(location.getLongitude());
            f.setLatitude(location.getLatitude());
        }

        // mCurrentPhotoPath.toString();
        // /storage/emulated/0/Android/data/com.example.esbenlaursen.myfriends/files/Pictures/JPEG_20170502_200351_-1393891881.jpg
        if(mCurrentPhotoPath != null) {
            f.setImage(mCurrentPhotoPath);
        }

        dao.Update(f);

        Intent i = new Intent(this, FriendListView.class);
        startActivity(i);
        finish();
    }

    public void Cancel(View view)
    {
        Intent i = new Intent(this, FriendListView.class);
        startActivity(i);
        finish();
    }


    public boolean GetLastKnowLocation() {
        location =  ls.getLastKnownLocation(this);;

        if (location == null) {
            Toast.makeText(getApplicationContext(), "Last known location is null",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        //WE GOT THE LOCATION. NOW SET IT
        return setCurrentLocation(location.getLongitude(), location.getLatitude());
    }

    public boolean setCurrentLocation(double lon, double lat)
    {
            LatLng hcmus = new LatLng(lat, lon);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
            mMap.addMarker(new MarkerOptions()
                    .title("Your mate's location")
                    .position(hcmus));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            mMap.setMyLocationEnabled(true);

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        boolean locationFound = false;
        //IF USER DOES HAVE A LOCATION (LON/LAT)
        if((f.getLatitude() > -90 || f.getLatitude() < 90) && (f.getLongitude() > -180 || f.getLongitude() < 180))
        {
            double longitude = f.getLongitude();
            double latitude = f.getLatitude();
            location = new Location("locaaa");
            location.setLongitude(longitude);
            location.setLatitude(latitude);
            setCurrentLocation(longitude, latitude);
            locationFound = true;
        }
        // ELSE TRY TO GET THE ADDRESS
        if(!locationFound) {
            boolean foundByAddress = FetchAddressAndShowLocation();
            if(foundByAddress) {
                locationFound = true;
            }
        }
        //Show current location
        if(!locationFound) {
            GetLastKnowLocation();
        }
    }

    public boolean FetchAddressAndShowLocation(){
        String adress = editTextAddress.getText().toString();
           // sets the location (which is used in save) to the address location
                Geocoder coder = new Geocoder(this);
                List<Address> address;
                try {
                    address = coder.getFromLocationName(adress,5);
                    if (address==null) {
                        Log.d("Address: ", adress);
                        return false;
                    }
                    try {
                        Address addressLocation = address.get(0);

                        double lat = addressLocation.getLatitude();
                        double lon = addressLocation.getLongitude();

                        location = new Location("location");
                 location.setLatitude(lat);
                 location.setLongitude(lon);

                setCurrentLocation(lon, lat);
            } catch(IndexOutOfBoundsException e)
                {
                    Toast.makeText(this, "Couldn't find location from that address", Toast.LENGTH_LONG).show();
                    // GetLastKnowLocation();
                    return false;
            }

        } catch (IOException e)
        {
            //GetLastKnowLocation();
            Toast.makeText(this, "Couldn't find location from that address", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

    public void LocationFromAddressOnClick(View view)
    {
        FetchAddressAndShowLocation();
    }

    public void CurrentLocationOnClick(View view)
    {
        GetLastKnowLocation();
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
            catch (IOException ex)
            {

            }
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
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
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
            catch (Exception e)
            {

            }
        }
    }
}
