package com.studentcares.spps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Full_Image_Activity extends BaseActivity implements View.OnClickListener {

    ImageView fullImageView;
    String imagePath;
    String folderName;
    String imageName;

    Drawable drawable;
    Bitmap bitmap;
    String ImagePath;
    Uri URI;
    String strDirectory;

    private static final int READ_STORAGE_PERMISSION_REQUEST = 6;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 7;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String title = activityInfo.loadLabel(getPackageManager())
                .toString();

        txtActivityName.setText(title);

        fullImageView = (ImageView) findViewById(R.id.full_image_view);
        //fullImageView.setImageResource(R.drawable.news_demo_img);
        fullImageView.setOnClickListener(this);
        final ProgressBar imageLoader;
        imageLoader = (ProgressBar) findViewById(R.id.loading);

        Intent intent = getIntent();
        if (null != intent) {
            imagePath = intent.getStringExtra("Image");
            folderName = intent.getStringExtra("ImageFolder");
        }

        if (imagePath == null || imagePath.equals("") || imagePath.equals(" ")) {
            fullImageView.setImageResource(R.drawable.no_image);
            imageLoader.setVisibility(View.GONE);
        } else {
            RequestOptions options = new RequestOptions().centerCrop();

            Glide.with(fullImageView.getContext())
                    .load(imagePath)
                    .apply(options)
                    .transition(withCrossFade())
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            imageLoader.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imageLoader.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(fullImageView);

        }
//        byte[] decodedString = Base64.decode(imagePath, Base64.DEFAULT);
//        bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        fullImageView.setImageBitmap(bitmap);
        new DownloadImageTask(fullImageView).execute(imagePath);
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

        String mainFolderName = getString(R.string.mainFolderName); //"/StudentCares";

        if (folderName.equals("Profile")) {
            strDirectory = Environment.getExternalStorageDirectory() +mainFolderName+ "/Images/Profile".toString();
        } else if (folderName.equals("Homework")) {
            strDirectory = Environment.getExternalStorageDirectory() +mainFolderName+ "/Images/Homework".toString();
        } else if (folderName.equals("Event")) {
            strDirectory = Environment.getExternalStorageDirectory() + mainFolderName+"/Images/Event".toString();
        } else if (folderName.equals("Notice")) {
            strDirectory = Environment.getExternalStorageDirectory() + mainFolderName+"/Images/Notice".toString();
        } else if (folderName.equals("News")) {
            strDirectory = Environment.getExternalStorageDirectory() +mainFolderName+ "/Images/News".toString();
        }

        File f = new File(strDirectory, imgName);
        try {
            fOut = new FileOutputStream(f);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(Full_Image_Activity.this, "Image Saved Successfully.", Toast.LENGTH_LONG).show();

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
            ActivityCompat.requestPermissions(Full_Image_Activity.this,
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
            ActivityCompat.requestPermissions(Full_Image_Activity.this,
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
                Toast.makeText(Full_Image_Activity.this, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (imagePath == null || imagePath.equals("")) {
                    Toast.makeText(this, "Image Not Available.", Toast.LENGTH_SHORT).show();
                } else {
                    saveImages();
                }
            } else {
                Toast.makeText(Full_Image_Activity.this, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_full_img, menu);

        MenuItem menuItem = menu.getItem(0);
        ImageView button = (ImageView) menuItem.getActionView();

        float density = this.getResources().getDisplayMetrics().density;
        int padding = (int)(5 * density);
        button.setPadding(padding,padding,padding,padding);

        button.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_file_download_white_24dp));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Full_Image_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestWriteStoragePermission();
                } else {
                    if (ActivityCompat.checkSelfPermission(Full_Image_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestReadStoragePermission();
                    } else if (imagePath == null || imagePath.equals("")) {
                        Toast.makeText(Full_Image_Activity.this, "Image Not Available.", Toast.LENGTH_SHORT).show();
                    } else {
                        saveImages();
                    }
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.saveImage:
                if (ActivityCompat.checkSelfPermission(Full_Image_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestWriteStoragePermission();
                } else {
                    if (ActivityCompat.checkSelfPermission(Full_Image_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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
