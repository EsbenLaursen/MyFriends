package com.example.esbenlaursen.myfriends;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EsbenLaursen on 07-03-2017.
 */

public class CustomAdapter extends ArrayAdapter<Friend> {

    public ArrayList<Friend> friends;
    ImageView imageView;
    String mCurrentPhotoPath;

    public CustomAdapter(Context context, ArrayList<Friend> friends) {
        super(context, R.layout.custom_row, friends);
        this.friends = friends;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row, parent, false);

        if(position % 2 == 1){
            customView.setBackgroundColor(Color.LTGRAY);
        }

        Friend friend = friends.get(position);

        TextView txtname = (TextView) customView.findViewById(R.id.txtName);

        txtname.setText(friend.name);

        TextView txtPhone = (TextView) customView.findViewById(R.id.txtPhone);

        txtPhone.setText(friend.phoneNumber);

        imageView = (ImageView) customView.findViewById(R.id.imageView);
        setImage(friend);

        return customView;
    }

    public void updateList(List<Friend> newlist) {
        friends.clear();
        friends.addAll(newlist);
        this.notifyDataSetChanged();
    }

    private void setImage(Friend f)
    {
        if(f.getImage() != null && !f.getImage().isEmpty()){
            try {
                File imgFile = new File(f.getImage());
                if(imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                    mCurrentPhotoPath = f.getImage();
                }
            } catch (Exception e)
            {
                Log.e("read pic", "onCreate: ", e);
            }
        }
    }


}
