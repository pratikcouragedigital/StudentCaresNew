package com.studentcares.spps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class About_StudentCares extends BaseActivity implements View.OnClickListener{

    TextView txtEmail,txtMobileNo,txtUrl;
    String contactNo ="8976560004";
    String email = "developers.studentcares@gmail.com";
    String webLoginUrl="studentcares.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_studentcares);

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

        txtMobileNo = (TextView) findViewById(R.id.txtMobileNo);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtUrl = (TextView) findViewById(R.id.txtUrl);

        txtMobileNo.setOnClickListener(this);
        txtEmail.setOnClickListener(this);
        txtUrl.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spps_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.Add_FeedBack:
                Intent intent = new Intent(this, Spps_Feedback_Form.class);
                startActivity(intent);
                return true;
            case R.id.contactUs:
                Intent intent2 = new Intent(this, Spps_Contact_Form.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.txtMobileNo){

            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contactNo));
            startActivity(callIntent);
        }
        else if(v.getId() == R.id.txtUrl){

            boolean validurl = Patterns.WEB_URL.matcher(webLoginUrl).matches();
            if(validurl == true){
                String url = "http://"+webLoginUrl;
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(launchBrowser);
            }
            else{
                Toast.makeText(this, "Its not a valid URL.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId() == R.id.txtEmail){

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
            i.putExtra(Intent.EXTRA_SUBJECT, "App Support");
//            i.putExtra(Intent.EXTRA_TEXT   , "body of email");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Please Select Email.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}