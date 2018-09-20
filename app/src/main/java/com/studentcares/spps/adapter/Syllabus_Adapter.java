package com.studentcares.spps.adapter;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Syllabus_Items;

import java.io.File;
import java.util.List;

import io.reactivex.annotations.Nullable;

public class Syllabus_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Syllabus_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    RelativeLayout noteLayout;

    private static final int VIEW_TYPE_EMPTY = 1;

    String mainFolderName;
    Dialog dialog;
    TextView txtCurrentDownload;

    ProgressBar downloadProgressBar;
    String path, title, strDirectory;


    public Syllabus_Adapter(List<Syllabus_Items> items, RelativeLayout noteLayoutS) {
        this.listItems = items;
        this.noteLayout = noteLayoutS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Syllabus_Adapter.EmptyViewHolder emptyViewHolder = new Syllabus_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        noteLayout.setVisibility(View.VISIBLE);
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.syllabus_items, viewGroup, false);
        viewHolder = new Syllabus_Adapter.ViewHolder(v);

        mainFolderName = viewGroup.getResources().getString(R.string.mainFolderName); //"/StudentCares";
        strDirectory = Environment.getExternalStorageDirectory() + mainFolderName + "/Syllabus/PDF".toString();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Syllabus_Adapter.ViewHolder) {
            Syllabus_Adapter.ViewHolder vHolder = (Syllabus_Adapter.ViewHolder) viewHolder;
            Syllabus_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        if (listItems.size() > 0) {
            return listItems.size();
        } else {
            return 1;
        }
//        return listItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtTitle;
        public View cardView;
        public LinearLayout pdfLayout;
        public RelativeLayout photoLayout;
        ImageView image;

        Syllabus_Items listItems = new Syllabus_Items();

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            image = (ImageView) itemView.findViewById(R.id.image);
            photoLayout = (RelativeLayout) itemView.findViewById(R.id.photoLayout);
            pdfLayout = (LinearLayout) itemView.findViewById(R.id.pdfLayout);
            cardView = itemView;
            cardView.setOnClickListener(this);

        }

        public void bindListDetails(Syllabus_Items listItems) {
            this.listItems = listItems;
            txtTitle.setText(listItems.gettitle());

            String path = listItems.getpdfPath();
            String splitArray[];

            splitArray = path.split("\\.");
            String isImageOrPath = splitArray[2];

            if (isImageOrPath.equals("jpg") || isImageOrPath.equals("jpeg") || isImageOrPath.equals("png")) {
                photoLayout.setVisibility(View.VISIBLE);
                pdfLayout.setVisibility(View.GONE);

                final ProgressBar image1Loader;
                image1Loader = (ProgressBar) itemView.findViewById(R.id.loading);

                String image1path = listItems.getpdfPath();
                if (image1path == null || image1path.equals("") || image1path.equals(" ")) {
                    image.setImageResource(R.drawable.no_image);
                    image1Loader.setVisibility(View.GONE);
                } else {
                    RequestOptions options = new RequestOptions().error(R.drawable.no_image);
                    Glide.with(image.getContext())
                            .asBitmap()
                            .load(listItems.getpdfPath())
                            .apply(options)
                            .listener(new RequestListener<Bitmap>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    image1Loader.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    image1Loader.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(image);
                }
            } else {
                pdfLayout.setVisibility(View.VISIBLE);
                photoLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {

            if (this.listItems != null) {
//                Intent gotoOpenPDF = new Intent(v.getContext(), Syllabus_PDF_Open.class);
//                gotoOpenPDF.putExtra("title", listItems.gettitle());
//                gotoOpenPDF.putExtra("path", listItems.getpdfPath());
//                v.getContext().startActivity(gotoOpenPDF);

                path = listItems.getpdfPath();
                title = listItems.gettitle();
                savePDF();


            }
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            noteLayout.setVisibility(View.GONE);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("PDF Not Available.");
        }
    }

    private void savePDF() {
        //dialog box to show download progress
        dialog = new Dialog(v.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.download_progress_dialog);
        dialog.setTitle("Download Progress");

        txtCurrentDownload = (TextView) dialog.findViewById(R.id.txtCurrentDownload);
        txtCurrentDownload.setText("Starting download...");
        dialog.show();

        downloadProgressBar = (ProgressBar) dialog.findViewById(R.id.downloadProgressBar);
        downloadProgressBar.setProgress(0);
        downloadProgressBar.setProgressDrawable(v.getResources().getDrawable(R.drawable.download_progress_color));

        String mainFolderName = v.getContext().getString(R.string.mainFolderName); //"/StudentCares";
        File syllabusFolder = new File(Environment.getExternalStorageDirectory() + mainFolderName + "/Syllabus/PDF/" + title + ".pdf");

        String path = syllabusFolder + "/" + title + ".pdf";
        File file = new File(path);

        if (syllabusFolder.exists()) {
            openFolder();
        } else {
            downloadFile();
        }
    }

    private void downloadFile() {

        AndroidNetworking.download(path, strDirectory, title)
                .setTag("downloadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress
                        float per = ((float) bytesDownloaded / totalBytes) * 100;
                        txtCurrentDownload.setText("Downloaded " + bytesDownloaded + "KB / " + totalBytes + "KB (" + (int) per + "%)");
                        downloadProgressBar.setProgress((int) per);
                        downloadProgressBar.setClickable(false);
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // do anything after completion
                        openFolder();
                        downloadProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void openFolder() {
//        Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
//        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
//                +  File.separator + ""+mainFolderName+"/Syllabus/" + File.separator);
//        intent2.setDataAndType(uri, "text/csv");
//        v.getContext().startActivity(Intent.createChooser(intent2, "Open folder"));

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + title);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target, "Open File");
        try {
            v.getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            Toast.makeText(v.getContext(), "You Don't have any PDF Reader please download and check. ", Toast.LENGTH_LONG).show();
        }
    }

}