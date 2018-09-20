package com.studentcares.spps.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.studentcares.spps.R;
import com.studentcares.spps.model.Study_Material_Items;

import java.io.File;
import java.util.List;

public class Study_Material_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Study_Material_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    RelativeLayout noteLayout;

    private static final int VIEW_TYPE_EMPTY = 1;

    String mainFolderName;
    Dialog dialog;
    TextView txtCurrentDownload;

    ProgressBar downloadProgressBar;
    String path, title,strDirectory;


    public Study_Material_Adapter(List<Study_Material_Items> items, RelativeLayout noteLayoutS) {
        this.listItems = items;
        this.noteLayout = noteLayoutS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
            return emptyViewHolder;
        }
        noteLayout.setVisibility(View.VISIBLE);
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.study_material_items, viewGroup, false);
        viewHolder = new ViewHolder(v);

        mainFolderName = viewGroup.getResources().getString(R.string.mainFolderName); //"/StudentCares";
        strDirectory = Environment.getExternalStorageDirectory() + mainFolderName + "/Study_Material/PDF".toString();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Study_Material_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {
        if(listItems.size() > 0){
            return listItems.size();
        }else {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtTitle;
        public View cardView;

        Study_Material_Items listItems = new Study_Material_Items();

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            cardView = itemView;
            cardView.setOnClickListener(this);

        }

        public void bindListDetails(Study_Material_Items listItems) {
            this.listItems = listItems;

            txtTitle.setText(listItems.gettitle());


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
            emptyTextView.setText("PDF Not Available For This Subject");
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

        downloadFile();
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

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void openFolder()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                +  File.separator + ""+mainFolderName+"/Study_Material/" + File.separator);
        intent.setDataAndType(uri, "text/csv");
        v.getContext().startActivity(Intent.createChooser(intent, "Open folder"));
    }


}