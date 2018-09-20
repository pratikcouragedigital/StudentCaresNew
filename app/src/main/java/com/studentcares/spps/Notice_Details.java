package com.studentcares.spps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Notice_Details extends AppCompatActivity implements View.OnClickListener {

    CollapsingToolbarLayout detailsCollapsingToolbar;
    CoordinatorLayout detailsCoordinatorLayout;
    AppBarLayout detailsAppBar;
    NestedScrollView schoolDetailsNestedScrollView;

    LinearLayout containerLayout;
    RelativeLayout firstRelativeLayout;
    RelativeLayout schoolSecondRelativeLayout;

    ImageView fullImageView;
    TextView txtDate;
    TextView txtDescription;
    TextView txtHeading;
    TextView txtAddedByName;

    String heading;
    String description;
    String date;
    String addedByName;
    String imagePath;

    private static final int READ_STORAGE_PERMISSION_REQUEST = 6;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 7;

    Drawable drawable;
    Bitmap bitmap;
    String ImagePath;
    Uri URI;
    String strDirectory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_details);

        detailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.detailsCollapsingToolbar);
        detailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.detailsCoordinatorLayout);
        detailsAppBar = (AppBarLayout) findViewById(R.id.detailsAppBar);
        schoolDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.schoolDetailsNestedScrollView);

        fullImageView = (ImageView) findViewById(R.id.noticeImage);
        txtHeading = (TextView) findViewById(R.id.heading);
        txtDate = (TextView) findViewById(R.id.date);
        txtDescription = (TextView) findViewById(R.id.description);
        txtAddedByName = (TextView) findViewById(R.id.addedByName);

        containerLayout = (LinearLayout) findViewById(R.id.containerLayout);
        firstRelativeLayout = (RelativeLayout) findViewById(R.id.firstRelativeLayout);
        schoolSecondRelativeLayout = (RelativeLayout) findViewById(R.id.addresslayout);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) detailsAppBar.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        detailsAppBar.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        Intent intent = getIntent();
        if (null != intent) {
            imagePath = intent.getStringExtra("Image");
            heading = intent.getStringExtra("Title");
            description = intent.getStringExtra("Description");
            date = intent.getStringExtra("DateOfAdded");
            addedByName = intent.getStringExtra("AddedByName");
        }
        txtHeading.setText(heading);
        txtDescription.setText(description);
        txtDate.setText(date);
        txtAddedByName.setText(addedByName);

        if (imagePath == null || imagePath.equals("")) {
            fullImageView.setImageResource(R.drawable.no_image);
        } else {
            RequestOptions options = new RequestOptions().centerCrop();

            Glide.with(fullImageView.getContext())
                    .load(imagePath)
                    .apply(options)
                    .transition(withCrossFade())
                    .into(fullImageView);

            byte[] decodedString = Base64.decode(imagePath, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            fullImageView.setImageBitmap(bitmap);
            new DownloadImageTask(fullImageView).execute(imagePath);
        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) detailsAppBar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(detailsCoordinatorLayout, detailsAppBar, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onClick(View v) {
    }

    private void saveImages() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        String imgName = formattedDate+".jpg";

//        drawable = getResources().getDrawable(R.drawable.news_demo_img);
//        bitmap = ((BitmapDrawable) drawable).getBitmap();

        OutputStream fOut = null;
        String mainFolderName = getString(R.string.mainFolderName);
        strDirectory = Environment.getExternalStorageDirectory() +mainFolderName+ "/Images/Notice_List".toString();

        File f = new File(strDirectory, imgName);
        try {
            fOut = new FileOutputStream(f);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(this, "Image Saved Successfully.", Toast.LENGTH_LONG).show();

//             MediaStore.Images.Media.insertImage(getContentResolver(),
//                    f.getAbsolutePath(), f.getName(), f.getName());
            //URI = Uri.parse(ImagePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestReadStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(Notice_Details.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestWriteStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(Notice_Details.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == WRITE_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                Toast.makeText(Notice_Details.this, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (imagePath == null || imagePath.equals("")) {
                    Toast.makeText(this, "Image Not Available.", Toast.LENGTH_SHORT).show();
                } else {
                    saveImages();
                }
            } else {
                Toast.makeText(Notice_Details.this, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_full_img, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.saveImage:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestWriteStoragePermission();
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestReadStoragePermission();
                    } else if (imagePath == null || imagePath.equals("")) {
                        Toast.makeText(this, "Image Not Available.", Toast.LENGTH_SHORT).show();
                    } else {
                        saveImages();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}