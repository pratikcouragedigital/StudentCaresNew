package com.studentcares.spps.backup_restore;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class Backup_Restore {

    public void importDB(Activity activity) {
        try {
            String state = Environment.getExternalStorageState();
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                String currentDBPath = "//data/com.studentcares.donbosco_nerul.studentcares/databases/StudentCares";
                String backupDBPath = "/StudentCares/Database/DatabaseBackup.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(activity, "Import Successful!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Import Failed!", Toast.LENGTH_SHORT).show();

        }
    }

    public void exportDB(Activity activity) {
        try {
            String state = Environment.getExternalStorageState();
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                String currentDBPath = "//data/com.studentcares.donbosco_nerul.studentcares/databases/StudentCares";
                String backupDBPath = "/StudentCares/Database/DatabaseBackup.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(activity, "Backup Successful!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Backup Failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
