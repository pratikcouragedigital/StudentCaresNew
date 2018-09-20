package com.studentcares.spps.connectivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.studentcares.spps.Assign_Std_Div_Teaching_Staff;
import com.studentcares.spps.BaseActivity;
import com.studentcares.spps.Home_Menu;
import com.studentcares.spps.R;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.sqlLite.DataBaseHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Data_Synchronisation {

    private String userDetailsResponseResult,ResponseResultForAttendance,NewsResponse,StandardResponse,DivisionResponse,SubjectResponse;
    String standardId,standardName,divisionId,divisionName,subjectId,subjectName,userId,schoolId;

    private DataBaseHelper mydb;
    private String webMethName,calledFrom;
    private static Context context;
    String isTeachingStaff,isClassTeacher,saveStandard,saveDivision;


    private static ProgressDialog progressDialogBox;
    SessionManager sessionManager;


    public Data_Synchronisation(Home_Menu homeMenu) {
        context = homeMenu;
    }

    public Data_Synchronisation(BaseActivity baseActivity) {
        context = baseActivity;
    }

    public void GetAsynchData(String IdOfSchool,String IdOfUser,String callingFrom,  ProgressDialog pd) {
        schoolId = IdOfSchool;
        userId = IdOfUser;
        calledFrom = callingFrom;
        progressDialogBox = pd;
        mydb = new DataBaseHelper(context);
        sessionManager = new SessionManager(context);

        UserAttendanceDetails();
    }



    private void UserAttendanceDetails() {
        webMethName = "Attendance_All_Month";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Details Not Found")) {
                                GetStandard();
                                //GetSubject();
                            }
                            else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        String Attendance_id = obj.getString("Attendance_id");
                                        String Student_Id = obj.getString("Student_Id");
                                        String SwipCard_No = obj.getString("SwipCard_No");
                                        String School_Id = obj.getString("School_Id");
                                        String Machine_Id = obj.getString("Machine_Id");
                                        String Machine_no = obj.getString("Machine_no");
                                        String Group_Id = obj.getString("Group_Id");
                                        String Att_Type = obj.getString("Att_Type");
                                        String Att_Date = obj.getString("Att_Date");
                                        String Att_Time = obj.getString("Att_Time");
                                        String OutTime = obj.getString("OutTime");
                                        String Att_Status = obj.getString("Att_Status");
                                        String Trk_Sms = obj.getString("Trk_Sms");
                                        String PresentDate = obj.getString("PresentDate");
                                        String PresentMonth = obj.getString("PresentMonth");
                                        String PresentYear = obj.getString("PresentYear");

                                        if(!Attendance_id.equals("")){
                                            mydb.insetAttendanceDetails(Attendance_id, Student_Id, SwipCard_No, School_Id, Machine_Id, Machine_no,Group_Id, Att_Type, Att_Date, Att_Time, OutTime, Att_Status, Trk_Sms, PresentDate, PresentMonth, PresentYear);
                                        }


                                    }
                                    GetStandard();
                                    //GetSubject();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(context, "Exception" + ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                        //Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                        //GetStandard();
                        //GetSubject();
                    }
                });
    }

    private void GetStandard() {
        webMethName = "Get_All_Standard";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Not found Standard..!!")) {
                                GetDivision();
                            }
                            else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            standardId = (obj.getString("StandardId"));
                                            standardName = (obj.getString("StandardName"));
                                            mydb.insertAllStandard(standardId, standardName);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    GetDivision();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(context, "Exception" + ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void GetDivision() {
        webMethName = "Get_All_Division";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Not found Division..!!")) {
                                GetSubject();
                            }
                            else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            standardId = (obj.getString("Standard_Id"));
                                            divisionId = (obj.getString("divisionId"));
                                            divisionName = (obj.getString("divisionName"));

                                            mydb.insertAllDivision(standardId, divisionId, divisionName);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    GetSubject();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(context, "Exception" + ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void GetSubject() {
        webMethName = "Get_All_Std_Div_Sub";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Not found Subject..!!")) {
                                if(calledFrom.equals("Menu")){
                                    GetUserDetails();
                                }else{
                                    //dataIsSynched
                                    sessionManager.dataIsSynched(true);
                                    progressDialogBox.dismiss();
                                }
                            }
                            else {
                                try {


                                    JSONObject jObj = response.getJSONObject("responseDetails");
//                                    JSONArray jArr = jObj.getJSONArray("standardList");
//                                    JSONArray jArr2 = jObj.getJSONArray("divisionList");
                                    JSONArray jArr3 = jObj.getJSONArray("subjectList");
//
//                                    for (int count = 0; count < jArr.length(); count++) {
//                                        try {
//                                            JSONObject obj = jArr.getJSONObject(count);
//                                            standardId = (obj.getString("StandardId"));
//                                            standardName = (obj.getString("StandardName"));
//                                            mydb.insertAllStandard(standardId, standardName);
//
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                    for (int count = 0; count < jArr2.length(); count++) {
//                                        try {
//                                            JSONObject obj = jArr2.getJSONObject(count);
//                                            divisionId = (obj.getString("divisionId"));
//                                            divisionName = (obj.getString("divisionName"));
//                                            mydb.insertAllDivision(standardId, divisionId, divisionName);
//
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
                                    for (int count = 0; count < jArr3.length(); count++) {
                                        try {
                                            JSONObject obj = jArr3.getJSONObject(count);
                                            standardId = (obj.getString("StandardId"));
                                            divisionId = (obj.getString("DivisionId"));
                                            subjectId = (obj.getString("SubjectId"));
                                            subjectName = (obj.getString("SubjectName"));
                                            mydb.insertAllSubject(standardId, divisionId, subjectId, subjectName);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    if(calledFrom.equals("Menu")){
                                        GetUserDetails();
                                    }else{
                                        //dataIsSynched
                                        sessionManager.dataIsSynched(true);
                                        progressDialogBox.dismiss();
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(context, "Exception" + ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void GetUserDetails() {
        webMethName = "Get_User_Details";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("School_id", schoolId);
            jsonObject.put("Id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialogBox.dismiss();
                        try {
                            String res = response.getString("responseDetails");
                            if (res.equals("Invalid Details")) {

                            }
                            else {
                                sessionManager = new SessionManager(context);
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            String saveUserType = obj.getString("Usertype");
                                            String saveUserId = obj.getString("Id");
                                            String saveName = obj.getString("Name");
                                            String saveSurName = obj.getString("Surname");
                                            String userFullName = saveName + " " + saveSurName;
                                            int std = obj.getInt("StandardId");
                                            int div = obj.getInt("DivisionId");
                                            String saveStandard = String.valueOf(std);
                                            String saveDivision = String.valueOf(div);
                                            String userContactNo = obj.getString("MobileNo");
                                            String userEmail = obj.getString("Emailid");
                                            String userDob = obj.getString("Dob");
                                            String userPhoto = obj.getString("Image");
                                            String userAddress = obj.getString("Address");
                                            String userBloodGroup = obj.getString("Blood_Group");
                                            String userGrNo = obj.getString("GR_No");
                                            String userSwipeCard = obj.getString("Swipe_Card");
                                            String userRollNo = obj.getString("Roll_No");
                                            String saveSchoolId = obj.getString("School_Code");
                                            String schoolLogo = obj.getString("Logo");
                                            String schoolName = obj.getString("School_Name");
                                            String schoolWebsite = obj.getString("school_Website");
                                            String schoolContactNo = obj.getString("School_Phone_No");
                                            String schoolAddress = obj.getString("School_Address");
                                            String schoolEmail = obj.getString("School_Email");
                                            String schoolLat = obj.getString("Latitude");
                                            String schoolLongi = obj.getString("Longitude");
                                            String isTeachingStaff = obj.getString("IsTeachingStaff");
                                            String isClassTeacher = obj.getString("IsClassTeacher");
                                            String pin = obj.getString("PIN");
                                            String senderId = obj.getString("SenderId");
                                            sessionManager.createUserLoginSession(saveUserId, saveUserType, userFullName,saveStandard,saveDivision, userContactNo, userEmail,userDob,userAddress,userBloodGroup,userGrNo,userSwipeCard,userRollNo,userPhoto,saveSchoolId, schoolLogo, schoolName, schoolContactNo, schoolAddress, schoolWebsite, schoolEmail,isTeachingStaff,schoolLat,schoolLongi,isClassTeacher,pin,senderId);
                                            mydb.UpdateUserDetails(saveUserType, saveUserId, userFullName,saveStandard,saveDivision, userContactNo, userEmail,userDob,userAddress,userBloodGroup,userGrNo,userSwipeCard,userRollNo, userPhoto, saveSchoolId, schoolLogo, schoolName, schoolWebsite, schoolContactNo, schoolAddress, schoolEmail,isTeachingStaff,schoolLat,schoolLongi,isClassTeacher,pin,senderId);
                                            sessionManager.ActiveUser(saveUserId, userFullName);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    sessionManager.dataIsSynched(true);

                                    if(isTeachingStaff.equals("true")){
                                        if(isClassTeacher.equals("true")){
                                            if (saveStandard.equals("0") || saveStandard == null && saveDivision.equals("0") || saveDivision == null){
                                                Intent gotoAssign = new Intent(context, Assign_Std_Div_Teaching_Staff.class);
                                                context.startActivity(gotoAssign);

                                            }
                                            else{
                                                Intent gotoHome = new Intent(context, Home_Menu.class);
                                                context.startActivity(gotoHome);
                                            }
                                        }else{
                                            Intent gotoHome = new Intent(context, Home_Menu.class);
                                            context.startActivity(gotoHome);
                                        }

                                    }else {
                                        Intent gotoHome = new Intent(context, Home_Menu.class);
                                        context.startActivity(gotoHome);
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(context, "Exception" + ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialogBox.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }


}
