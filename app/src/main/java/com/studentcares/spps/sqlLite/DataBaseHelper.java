package com.studentcares.spps.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "StudentCares";
    // user details
    public static final String TABLE_USERDETAILS = "UserDetails";
    public static final String USERDETAILS_ID = "Id";
    public static final String USERDETAILS_USERID = "userId";
    public static final String USERDETAILS_USERTYPE= "Usertype";
    public static final String USERDETAILS_NAME = "Name";
    public static final String USERDETAILS_STANDARD = "Standard";
    public static final String USERDETAILS_DIVISION = "Division";
    public static final String USERDETAILS_MOBILENO = "MobileNo";
    public static final String USERDETAILS_EMAILID = "Emailid";
    public static final String USERDETAILS_DOB = "Dob";
    public static final String USERDETAILS_ADDRESS = "Address";
    public static final String USERDETAILS_GRNO = "GrNo";
    public static final String USERDETAILS_BLOODGROUP = "BloodGroup";
    public static final String USERDETAILS_SWIPECARDNO = "SwipeCardNo";
    public static final String USERDETAILS_ROLLNO = "RolNo";

    public static final String USERDETAILS_IMAAGE = "Image";
    public static final String USERDETAILS_SCHOOL_ID = "School_Code";
    public static final String USERDETAILS_SCHOOL_LOGO = "School_Logo";
    public static final String USERDETAILS_SCHOOL_NAME = "School_Name";
    public static final String USERDETAILS_SCHOOL_WEBSITE = "school_Website";
    public static final String USERDETAILS_SCHOOL_PHONENO = "School_Phone_No";
    public static final String USERDETAILS_SCHOOL_ADDRESS = "School_Address";
    public static final String USERDETAILS_SCHOOL_EAMIL = "School_Email";
    public static final String USERDETAILS_SCHOOL_LAT = "School_Lat";
    public static final String USERDETAILS_SCHOOL_LONGI = "School_Longi";
    public static final String USERDETAILS_ISTEACHING_STAFF = "isTeachingStaff";
    public static final String USERDETAILS_ISCLASS_TEACHER = "isClass_Teacher";
    public static final String USERDETAILS_PIN = "PIN";
    public static final String USERDETAILS_SMS_SENDERID = "SenderId";

    // homework details
    public static final String TABLE_HOMEWORKDETAILS = "HomeworkDetails";
    public static final String HOMEWORKDETAILS_ID = "Id";
    public static final String HOMEWORK_USERID = "userId";
    public static final String HOMEWORK_LISTID = "listId";
    public static final String HOMEWORK_STANDARD = "standard";
    public static final String HOMEWORK_DIVISION = "division";
    public static final String HOMEWORK_SUBJECT_NAME = "subject_Name";
    public static final String HOMEWORK_TEACHER_NAME = "teacher_Name";
    public static final String HOMEWORK_SUBMISSION_DATE = "submission_Date";
    public static final String HOMEWORK_ADDED_DATE = "addedDate";
    public static final String HOMEWORK_DESCRIPTION = "homework";
    public static final String HOMEWORK_TITLE = "homeworkTitle";
    public static final String HOMEWORK_IMAGE = "homeworkImage";

    //Standard Details
    public static final String TABLE_STANDARD = "StandardDetails";
    public static final String STANDARD_STANDARDID = "Id"; //primary key
    public static final String STANDARD_ID = "StandardId"; //server table primary key
    public static final String STANDARD_NAME = "Standard_Name";

    //Division Details
    public static final String TABLE_DIVISION = "DivisionDetails";
    public static final String DIVISION_DIVISIONID = "Id";  //primary key
    public static final String DIVISION_ID = "DivisionId";  //server table primary key
    public static final String DIVISION_NAME = "Division_Name";

    //Subject Details
    public static final String TABLE_SUBJECT = "SubjectDetails";
    public static final String SUBJECT_SUBJECTID = "Id"; //primary key
    public static final String SUBJECT_ID = "SubjectId";  //server table primary key
    public static final String SUBJECT_NAME = "Subject_Name";

    //Notice_List Details
    public static final String TABLE_NOTICE = "NoticeDetails";
    public static final String NOTICE_NOTICEID = "Id"; //primary key
    public static final String LIST_ID = "NoticeId";  //server table primary key
    public static final String NOTICE_TITLE = "Title";
    public static final String NOTICE_DESCRIPTION = "Description";
    public static final String NOTICE_ADDEDDATE = "AddedDate";
    public static final String NOTICE_ADDEDBYNAME = "AddedByName";
    public static final String NOTICE_IMAGE = "Image";

    //NEWS Details
    public static final String TABLE_NEWS = "NewsDetails";
    public static final String NEWS_NEWSID = "Id"; //primary key
    public static final String NEWS_LIST_ID = "NewsId";  //server table primary key
    public static final String NEWS_TITLE = "Title";
    public static final String NEWS_DESCRIPTION = "Description";
    public static final String NEWS_ADDEDDATE = "AddedDate";
    public static final String NEWS_ADDEDBYNAME = "AddedByName";
    public static final String NEWS_IMAGE = "Image";

    //Attendance Details
    public static final String TABLE_ATTENDANCE = "AttendanceDetails";
    public static final String ID = "Id"; //primary key
    public static final String ATTENDANCE_ID = "Attendance_id";  //server table primary key
    public static final String ATTENDANCE_STUDENT_ID = "Student_Id";
    public static final String ATTENDANCE_SWIPECARDNO = "SwipCard_No";
    public static final String ATTENDANCE_SCHOOL_ID = "School_Id";
    public static final String ATTENDANCE_MACHINE_ID = "Machine_Id";
    public static final String ATTENDANCE_MACHINE_NO = "Machine_no";
    public static final String ATTENDANCE_GROUPID = "Group_Id";
    public static final String ATTENDANCE_TYPE = "Att_Type";
    public static final String ATTENDANCE_DATE = "Att_Date";
    public static final String ATTENDANCE_INTIME = "Att_Time";
    public static final String ATTENDANCE_OUTTIME = "Att_OutTime";
    public static final String ATTENDANCE_STATUS = "Att_Status";
    public static final String ATTENDANCE_SMSTREK = "Trk_Sms";
    public static final String ATTENDANCE_PRESENT_DATE = "PresentDate";
    public static final String ATTENDANCE_PRESENT_MONTH = "PresentMonth";
    public static final String ATTENDANCE_PRESENT_YEAR = "PresentYear";

    //Msg Details
    public static final String TABLE_MSG = "MsgDetails";
    public static final String MSG_ID = "Msg_Id";  // table server primary key
    public static final String MSG_LIST_ID = "ID";  // table primary key
    public static final String MSG_TYPE = "Message_Type";
    public static final String MSG = "Message";
    public static final String MSG_DATE = "Date";
    public static final String MSG_TIME = "Time";
    public static final String USERID = "UserId";

    String strDirectory;
    String folderName;

    private HashMap hp;

    String CREATE_TABLE_USERDETAILS = "CREATE TABLE " + TABLE_USERDETAILS + "("+ USERDETAILS_ID + " INTEGER PRIMARY KEY," + USERDETAILS_USERID +" TEXT," + USERDETAILS_USERTYPE + " TEXT," + USERDETAILS_NAME + " TEXT," +USERDETAILS_MOBILENO +" TEXT," +USERDETAILS_STANDARD +" TEXT," +USERDETAILS_DIVISION +" TEXT," +USERDETAILS_EMAILID + " TEXT,"+USERDETAILS_DOB + " TEXT,"+USERDETAILS_ADDRESS + " TEXT," +USERDETAILS_GRNO + " TEXT,"+USERDETAILS_BLOODGROUP + " TEXT," +USERDETAILS_SWIPECARDNO + " TEXT," +USERDETAILS_ROLLNO + " TEXT,"   +USERDETAILS_IMAAGE + " TEXT," +USERDETAILS_SCHOOL_ID + " TEXT,"+USERDETAILS_SCHOOL_LOGO + " TEXT," +USERDETAILS_SCHOOL_NAME + " TEXT," +USERDETAILS_SCHOOL_WEBSITE + " TEXT,"+USERDETAILS_SCHOOL_PHONENO + " TEXT," +USERDETAILS_SCHOOL_ADDRESS + " TEXT," +USERDETAILS_SCHOOL_EAMIL + " TEXT,"+USERDETAILS_ISTEACHING_STAFF + " TEXT,"+USERDETAILS_SCHOOL_LAT + " TEXT,"+USERDETAILS_SCHOOL_LONGI + " TEXT,"+USERDETAILS_ISCLASS_TEACHER + " TEXT,"+USERDETAILS_PIN + " TEXT," +USERDETAILS_SMS_SENDERID + " TEXT" +")";
    String CREATE_TABLE_HOMEWORKDETAILS ="CREATE TABLE " + TABLE_HOMEWORKDETAILS + "("+ HOMEWORKDETAILS_ID + " INTEGER PRIMARY KEY,"+ HOMEWORK_USERID +" TEXT,"+ HOMEWORK_LISTID +" TEXT UNIQUE,"+ HOMEWORK_STANDARD +" TEXT,"+ HOMEWORK_DIVISION +" TEXT,"+ HOMEWORK_SUBJECT_NAME +" TEXT,"+ HOMEWORK_TEACHER_NAME + " TEXT," + HOMEWORK_SUBMISSION_DATE + " TEXT,"+HOMEWORK_ADDED_DATE +" TEXT," +HOMEWORK_DESCRIPTION + " TEXT," +HOMEWORK_TITLE + " TEXT," +HOMEWORK_IMAGE + " TEXT" +")";
    String CREATE_TABLE_STANDARD ="CREATE TABLE " + TABLE_STANDARD + "("+ STANDARD_STANDARDID + " INTEGER PRIMARY KEY,"+ STANDARD_NAME +" TEXT,"+ STANDARD_ID +" TEXT"+ ")";
    String CREATE_TABLE_DIVISION ="CREATE TABLE " + TABLE_DIVISION + "("+ DIVISION_DIVISIONID + " INTEGER PRIMARY KEY,"+STANDARD_ID +" TEXT,"+ DIVISION_NAME +" TEXT,"+ DIVISION_ID +" TEXT"+ ")";
    String CREATE_TABLE_SUBJECT ="CREATE TABLE " + TABLE_SUBJECT + "("+ SUBJECT_SUBJECTID + " INTEGER PRIMARY KEY,"+ STANDARD_ID +" TEXT,"+ DIVISION_ID +" TEXT,"+ SUBJECT_NAME +" TEXT,"+ SUBJECT_ID +" TEXT"+ ")";
    String CREATE_TABLE_NOTICE ="CREATE TABLE " + TABLE_NOTICE + "("+ NOTICE_NOTICEID + " INTEGER PRIMARY KEY,"+ LIST_ID +" TEXT,"+ NOTICE_TITLE +" TEXT ,"+ NOTICE_DESCRIPTION +" TEXT,"+ NOTICE_ADDEDDATE +" TEXT,"+ NOTICE_ADDEDBYNAME +" TEXT,"+ NOTICE_IMAGE + " TEXT" + ")";
    String CREATE_TABLE_ATTENDANCE ="CREATE TABLE " + TABLE_ATTENDANCE + "("+ ID + " INTEGER PRIMARY KEY,"+ ATTENDANCE_ID +" TEXT,"+ ATTENDANCE_STUDENT_ID +" TEXT ,"+ ATTENDANCE_SWIPECARDNO +" TEXT,"+ ATTENDANCE_SCHOOL_ID +" TEXT,"+ ATTENDANCE_MACHINE_ID +" TEXT,"+ ATTENDANCE_MACHINE_NO +" TEXT ,"+ ATTENDANCE_TYPE +" TEXT ,"+ ATTENDANCE_DATE +" TEXT ,"+ ATTENDANCE_INTIME +" TEXT ,"+ ATTENDANCE_OUTTIME +" TEXT ,"+  ATTENDANCE_GROUPID +" TEXT ,"+ ATTENDANCE_STATUS +" TEXT ,"+ ATTENDANCE_SMSTREK +" TEXT ,"+ ATTENDANCE_PRESENT_DATE +" TEXT ,"+ ATTENDANCE_PRESENT_MONTH +" TEXT ,"+ ATTENDANCE_PRESENT_YEAR + " TEXT" + ")";
    String CREATE_TABLE_NEWS ="CREATE TABLE " + TABLE_NEWS + "("+ NEWS_NEWSID + " INTEGER PRIMARY KEY,"+ NEWS_LIST_ID +" TEXT,"+ NEWS_TITLE +" TEXT ,"+ NEWS_DESCRIPTION +" TEXT,"+ NEWS_ADDEDDATE +" TEXT,"+ NEWS_ADDEDBYNAME +" TEXT,"+ NEWS_IMAGE + " TEXT"+ ")";
    String CREATE_TABLE_MSG ="CREATE TABLE " + TABLE_MSG + "("+ MSG_ID + " INTEGER PRIMARY KEY,"+ MSG_TYPE +" TEXT,"+ MSG +" TEXT ,"+ MSG_DATE +" TEXT,"+ MSG_TIME +" TEXT," + MSG_LIST_ID+ " TEXT, "+ USERID+ " TEXT "+")";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
//        super(context, Environment.getExternalStorageDirectory()+"/StudentCares/Database".toString()+DATABASE_NAME, null, DATABASE_VERSION);
//        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/StudentCares/Database".toString()+DATABASE_NAME,null);
    }

    public void deleteTableAndCreateTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDETAILS );
        db.execSQL(CREATE_TABLE_USERDETAILS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOMEWORKDETAILS);
        db.execSQL(CREATE_TABLE_HOMEWORKDETAILS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STANDARD);
        db.execSQL(CREATE_TABLE_STANDARD);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIVISION);
        db.execSQL(CREATE_TABLE_DIVISION);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECT);
        db.execSQL(CREATE_TABLE_SUBJECT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICE);
        db.execSQL(CREATE_TABLE_NOTICE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL(CREATE_TABLE_ATTENDANCE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL(CREATE_TABLE_NEWS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG);
        db.execSQL(CREATE_TABLE_MSG);

        boolean isDataSynched = false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERDETAILS);
        db.execSQL(CREATE_TABLE_HOMEWORKDETAILS);
        db.execSQL(CREATE_TABLE_STANDARD);
        db.execSQL(CREATE_TABLE_DIVISION);
        db.execSQL(CREATE_TABLE_SUBJECT);
        db.execSQL(CREATE_TABLE_NOTICE);
        db.execSQL(CREATE_TABLE_NEWS);
        db.execSQL(CREATE_TABLE_ATTENDANCE);
        db.execSQL(CREATE_TABLE_MSG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOMEWORKDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STANDARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIVISION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG);

        onCreate(db);
    }

//---------------------------- All Standard details-------------------------------//
    public boolean insertAllStandard (String standardId,String standardName) {

        String selectQuery = "SELECT * FROM "+ TABLE_STANDARD +" WHERE "+ STANDARD_ID +" = "+ standardId +"";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(STANDARD_ID, standardId);
            contentValues.put(STANDARD_NAME, standardName);
            db.insert(TABLE_STANDARD, null, contentValues);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    public JSONArray getAllStandardForSpinner() throws JSONException {

        String selectQuery = "SELECT  * FROM " + TABLE_STANDARD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(STANDARD_NAME, res.getString(res.getColumnIndex(STANDARD_NAME)));
            details.put(STANDARD_ID, res.getString(res.getColumnIndex(STANDARD_ID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

//---------------------------- All Division details-------------------------------//

    public boolean insertAllDivision (String standardId,String divisionId,String divisionName) {

        String selectQuery = "SELECT * FROM "+ TABLE_DIVISION +" WHERE "+ STANDARD_ID +" = "+ standardId+" AND "+DIVISION_ID+" = "+ divisionId+"";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(STANDARD_ID, standardId);
            contentValues.put(DIVISION_ID, divisionId);
            contentValues.put(DIVISION_NAME, divisionName);

            db.insert(TABLE_DIVISION, null, contentValues);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    public JSONArray getAllDivisionForSpinner(String standardId) throws JSONException {

        String selectQuery = "SELECT * FROM "+ TABLE_DIVISION +" WHERE "+ STANDARD_ID +" = "+ standardId +" ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(DIVISION_NAME, res.getString(res.getColumnIndex(DIVISION_NAME)));
            details.put(DIVISION_ID, res.getString(res.getColumnIndex(DIVISION_ID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

//---------------------------- All Subject details-------------------------------//

    public boolean insertAllSubject (String standardId,String divisionId,String subjectId,String subjectName) {

        String selectQuery = "SELECT * FROM "+ TABLE_SUBJECT +" WHERE "+ STANDARD_ID +" = "+ standardId+" AND "+DIVISION_ID+" = "+ divisionId+" AND " + SUBJECT_ID +" = "+ subjectId +"";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(STANDARD_ID, standardId);
            contentValues.put(DIVISION_ID, divisionId);
            contentValues.put(SUBJECT_ID, subjectId);
            contentValues.put(SUBJECT_NAME, subjectName);

            db.insert(TABLE_SUBJECT, null, contentValues);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    public JSONArray getAllSubjectForSpinner(String standardId,String divisionId) throws JSONException {

        String selectQuery = "SELECT * FROM "+ TABLE_SUBJECT +" WHERE "+ STANDARD_ID +" = "+ standardId +" AND "+ DIVISION_ID +" = "+ divisionId +" ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(SUBJECT_NAME, res.getString(res.getColumnIndex(SUBJECT_NAME)));
            details.put(SUBJECT_ID, res.getString(res.getColumnIndex(SUBJECT_ID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public JSONArray getSubjectForSyllabus(String standardId) throws JSONException {

        String selectQuery = "SELECT DISTINCT( " + SUBJECT_NAME + " ), " + SUBJECT_ID + " FROM " + TABLE_SUBJECT + " WHERE " + STANDARD_ID + " = " + standardId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();
            details.put(SUBJECT_NAME, res.getString(res.getColumnIndex(SUBJECT_NAME)));
            details.put(SUBJECT_ID, res.getString(res.getColumnIndex(SUBJECT_ID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

//---------------------------- User details-------------------------------//
    public boolean insertUserDetails (String Usertype, String userId, String Name,String Standard,String Division, String MobileNo,String Emailid,String userDob,String userAddress, String userBloodGroup,String userGrNo,String userSwipeCard,String userRollNo,String Image,String School_Code,String School_Logo,String School_Name,String school_Website,String School_Phone_No,String School_Address,String School_Email,String isTeachingStaff,String schoolLat,String schoolLongi, String isClassTeacher, String pin, String senderId) {

        String selectQuery = "SELECT * FROM "+ TABLE_USERDETAILS +" WHERE "+ USERDETAILS_USERID +" = "+ userId+" ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(USERDETAILS_USERID, userId);
            contentValues.put(USERDETAILS_USERTYPE, Usertype);
            contentValues.put(USERDETAILS_NAME, Name);
            contentValues.put(USERDETAILS_MOBILENO, MobileNo);
            contentValues.put(USERDETAILS_STANDARD, Standard);
            contentValues.put(USERDETAILS_DIVISION, Division);
            contentValues.put(USERDETAILS_EMAILID, Emailid);
            contentValues.put(USERDETAILS_DOB, userDob);
            contentValues.put(USERDETAILS_ADDRESS, userAddress);
            contentValues.put(USERDETAILS_BLOODGROUP, userBloodGroup);
            contentValues.put(USERDETAILS_GRNO, userGrNo);
            contentValues.put(USERDETAILS_SWIPECARDNO, userSwipeCard);
            contentValues.put(USERDETAILS_ROLLNO, userRollNo);
            contentValues.put(USERDETAILS_IMAAGE, Image);
            contentValues.put(USERDETAILS_SCHOOL_ID, School_Code);
            contentValues.put(USERDETAILS_SCHOOL_LOGO, School_Logo);
            contentValues.put(USERDETAILS_SCHOOL_NAME, School_Name);
            contentValues.put(USERDETAILS_SCHOOL_WEBSITE, school_Website);
            contentValues.put(USERDETAILS_SCHOOL_PHONENO, School_Phone_No);
            contentValues.put(USERDETAILS_SCHOOL_ADDRESS, School_Address);
            contentValues.put(USERDETAILS_SCHOOL_EAMIL, School_Email);
            contentValues.put(USERDETAILS_ISTEACHING_STAFF, isTeachingStaff);
            contentValues.put(USERDETAILS_SCHOOL_LAT, schoolLat);
            contentValues.put(USERDETAILS_SCHOOL_LONGI, schoolLongi);
            contentValues.put(USERDETAILS_ISCLASS_TEACHER, isClassTeacher);
            contentValues.put(USERDETAILS_PIN, pin);
            contentValues.put(USERDETAILS_SMS_SENDERID, senderId);
            db.insert(TABLE_USERDETAILS, null, contentValues);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    public boolean UpdateUserDetails (String Usertype, String userId, String Name,String Standard,String Division, String MobileNo,String Emailid,String userDob,String userAddress, String userBloodGroup,String userGrNo,String userSwipeCard,String userRollNo,String Image,String School_Code,String School_Logo,String School_Name,String school_Website,String School_Phone_No,String School_Address,String School_Email,String isTeachingStaff,String schoolLat,String schoolLongi, String isClassTeacher,String pin, String senderId){
        String selectQuery = "SELECT * FROM "+ TABLE_USERDETAILS +" WHERE "+ USERDETAILS_USERID +" = "+userId+" ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 1){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(USERDETAILS_USERID, userId);
            contentValues.put(USERDETAILS_USERTYPE, Usertype);
            contentValues.put(USERDETAILS_NAME, Name);
            contentValues.put(USERDETAILS_MOBILENO, MobileNo);
            contentValues.put(USERDETAILS_STANDARD, Standard);
            contentValues.put(USERDETAILS_DIVISION, Division);
            contentValues.put(USERDETAILS_EMAILID, Emailid);
            contentValues.put(USERDETAILS_DOB, userDob);
            contentValues.put(USERDETAILS_ADDRESS, userAddress);
            contentValues.put(USERDETAILS_BLOODGROUP, userBloodGroup);
            contentValues.put(USERDETAILS_GRNO, userGrNo);
            contentValues.put(USERDETAILS_SWIPECARDNO, userSwipeCard);
            contentValues.put(USERDETAILS_ROLLNO, userRollNo);
            contentValues.put(USERDETAILS_IMAAGE, Image);
            contentValues.put(USERDETAILS_SCHOOL_ID, School_Code);
            contentValues.put(USERDETAILS_SCHOOL_LOGO, School_Logo);
            contentValues.put(USERDETAILS_SCHOOL_NAME, School_Name);
            contentValues.put(USERDETAILS_SCHOOL_WEBSITE, school_Website);
            contentValues.put(USERDETAILS_SCHOOL_PHONENO, School_Phone_No);
            contentValues.put(USERDETAILS_SCHOOL_ADDRESS, School_Address);
            contentValues.put(USERDETAILS_SCHOOL_EAMIL, School_Email);
            contentValues.put(USERDETAILS_ISTEACHING_STAFF, isTeachingStaff);
            contentValues.put(USERDETAILS_SCHOOL_LAT, schoolLat);
            contentValues.put(USERDETAILS_SCHOOL_LONGI, schoolLongi);
            contentValues.put(USERDETAILS_ISCLASS_TEACHER, isClassTeacher);
            contentValues.put(USERDETAILS_PIN, pin);
            contentValues.put(USERDETAILS_SMS_SENDERID, senderId);
            db.update(TABLE_USERDETAILS, contentValues, USERDETAILS_USERID + " = ?",new String[]{userId});
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    public JSONArray getAllUserForSpinner() throws JSONException {
        //ArrayList<String> array_list = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_USERDETAILS +"";
        SQLiteDatabase db2 = this.getReadableDatabase();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray userDetailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject userDetails = new JSONObject();
            userDetails.put(USERDETAILS_NAME, res.getString(res.getColumnIndex(USERDETAILS_NAME)));
            userDetails.put(USERDETAILS_USERID, res.getString(res.getColumnIndex(USERDETAILS_USERID)));

// this fr array list
//            array_list.add(res.getString(res.getColumnIndex(USERDETAILS_NAME)));
//            array_list.add(res.getString(res.getColumnIndex(USERDETAILS_USERID)));
            res.moveToNext();
            userDetailsArray.put(i, userDetails);
            i++;
        }
        res.close();
        return userDetailsArray;
    }

//---------------------------- homework details-------------------------------//

    public boolean insertHomeworkDetails (String userId,String listId,String standanrd,String division,String subject_Name, String teacher_Name, String submission_Date, String addedDate,String homework,String homeworkTitle,String homeworkImage) {

        String selectQuery = "SELECT * FROM "+ TABLE_HOMEWORKDETAILS +" WHERE "+ HOMEWORK_LISTID +" = "+ listId +" ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(HOMEWORK_USERID, userId);
            contentValues.put(HOMEWORK_LISTID, listId);
            contentValues.put(HOMEWORK_STANDARD, standanrd);
            contentValues.put(HOMEWORK_DIVISION, division);
            contentValues.put(HOMEWORK_SUBJECT_NAME, subject_Name);
            contentValues.put(HOMEWORK_TEACHER_NAME, teacher_Name);
            contentValues.put(HOMEWORK_SUBMISSION_DATE, submission_Date);
            contentValues.put(HOMEWORK_ADDED_DATE, addedDate);
            contentValues.put(HOMEWORK_DESCRIPTION, homework);
            contentValues.put(HOMEWORK_TITLE, homeworkTitle);
            contentValues.put(HOMEWORK_IMAGE, homeworkImage);
            db.insert(TABLE_HOMEWORKDETAILS, null, contentValues);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    public JSONArray getAllHomeworkList(String userId) throws JSONException {

        String selectQuery = "SELECT * FROM " + TABLE_HOMEWORKDETAILS + " WHERE " + HOMEWORK_USERID +"=" +userId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(HOMEWORK_SUBJECT_NAME, res.getString(res.getColumnIndex(HOMEWORK_SUBJECT_NAME)));
            details.put(HOMEWORK_TEACHER_NAME, res.getString(res.getColumnIndex(HOMEWORK_TEACHER_NAME)));
            details.put(HOMEWORK_SUBMISSION_DATE, res.getString(res.getColumnIndex(HOMEWORK_SUBMISSION_DATE)));
            details.put(HOMEWORK_ADDED_DATE, res.getString(res.getColumnIndex(HOMEWORK_ADDED_DATE)));
            details.put(HOMEWORK_DESCRIPTION, res.getString(res.getColumnIndex(HOMEWORK_DESCRIPTION)));
            details.put(HOMEWORK_TITLE, res.getString(res.getColumnIndex(HOMEWORK_TITLE)));
            details.put(HOMEWORK_IMAGE, res.getString(res.getColumnIndex(HOMEWORK_IMAGE)));
            details.put(HOMEWORK_STANDARD, res.getString(res.getColumnIndex(HOMEWORK_STANDARD)));
            details.put(HOMEWORK_DIVISION, res.getString(res.getColumnIndex(HOMEWORK_DIVISION)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public JSONArray getAllFilterWiseHomeworkList(String userId,String filteredValue_Date_Sub,int count) throws JSONException {
        String selectQuery="";
        if(count == 1){
            selectQuery = "SELECT * FROM " + TABLE_HOMEWORKDETAILS + " WHERE " + HOMEWORK_USERID +" = " +userId+" AND "+HOMEWORK_SUBJECT_NAME +" = "+ filteredValue_Date_Sub;
        }
        else if (count == 2){
            selectQuery = "SELECT * FROM " + TABLE_HOMEWORKDETAILS + " WHERE " + HOMEWORK_USERID +" = " +userId+" AND "+HOMEWORK_SUBMISSION_DATE +" = "+ filteredValue_Date_Sub;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(HOMEWORK_SUBJECT_NAME, res.getString(res.getColumnIndex(HOMEWORK_SUBJECT_NAME)));
            details.put(HOMEWORK_TEACHER_NAME, res.getString(res.getColumnIndex(HOMEWORK_TEACHER_NAME)));
            details.put(HOMEWORK_SUBMISSION_DATE, res.getString(res.getColumnIndex(HOMEWORK_SUBMISSION_DATE)));
            details.put(HOMEWORK_ADDED_DATE, res.getString(res.getColumnIndex(HOMEWORK_ADDED_DATE)));
            details.put(HOMEWORK_DESCRIPTION, res.getString(res.getColumnIndex(HOMEWORK_DESCRIPTION)));
            details.put(HOMEWORK_TITLE, res.getString(res.getColumnIndex(HOMEWORK_TITLE)));
            details.put(HOMEWORK_IMAGE, res.getString(res.getColumnIndex(HOMEWORK_IMAGE)));
            details.put(HOMEWORK_STANDARD, res.getString(res.getColumnIndex(HOMEWORK_STANDARD)));
            details.put(HOMEWORK_DIVISION, res.getString(res.getColumnIndex(HOMEWORK_DIVISION)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

//---------------------------- Notice_List details-------------------------------//

    public boolean insertNoticeDetails (String listId, String title ,String description,String addedDate,String addedByName,String homeworkImage) {

        String selectQuery = "SELECT * FROM "+ TABLE_NOTICE +" WHERE "+ LIST_ID +" = "+ listId +" ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(LIST_ID, listId);
            contentValues.put(NOTICE_TITLE, title);
            contentValues.put(NOTICE_DESCRIPTION, description);
            contentValues.put(NOTICE_ADDEDDATE, addedDate);
            contentValues.put(NOTICE_ADDEDBYNAME, addedByName);
            contentValues.put(NOTICE_IMAGE, homeworkImage);
            db.insert(TABLE_NOTICE, null, contentValues);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    public boolean insertNewsDetails (String listId, String title ,String description,String addedDate,String addedByName,String homeworkImage) {

        String selectQuery = "SELECT * FROM "+ TABLE_NEWS +" WHERE "+ NEWS_LIST_ID +" = "+ listId +" ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(NEWS_LIST_ID, listId);
            contentValues.put(NEWS_TITLE, title);
            contentValues.put(NEWS_DESCRIPTION, description);
            contentValues.put(NEWS_ADDEDDATE, addedDate);
            contentValues.put(NEWS_ADDEDBYNAME, addedByName);
            contentValues.put(NEWS_IMAGE, homeworkImage);
            db.insert(TABLE_NEWS, null, contentValues);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    public JSONArray getAllNoticeList() throws JSONException {

        String selectQuery = "SELECT * FROM " + TABLE_NOTICE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;

        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(LIST_ID, res.getString(res.getColumnIndex(LIST_ID)));
            details.put(NOTICE_TITLE, res.getString(res.getColumnIndex(NOTICE_TITLE)));
            details.put(NOTICE_DESCRIPTION, res.getString(res.getColumnIndex(NOTICE_DESCRIPTION)));
            details.put(NOTICE_ADDEDDATE, res.getString(res.getColumnIndex(NOTICE_ADDEDDATE)));
            details.put(NOTICE_ADDEDBYNAME, res.getString(res.getColumnIndex(NOTICE_ADDEDBYNAME)));
            details.put(HOMEWORK_IMAGE, res.getString(res.getColumnIndex(HOMEWORK_IMAGE)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }


//---------------------------- Attendance details-------------------------------//
    public boolean insetAttendanceDetails(String attendance_id, String student_id, String swipCard_no,String school_id, String machine_id, String machine_no, String Group_Id, String type, String date,String in_time, String out_time, String status, String trk_sms, String presentDate, String presentMonth,String presentYear) {

        String selectQuery = "SELECT * FROM "+ TABLE_ATTENDANCE +" WHERE "+ ATTENDANCE_ID +" = "+ attendance_id +"";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ATTENDANCE_ID, attendance_id);
            contentValues.put(ATTENDANCE_STUDENT_ID, student_id);
            contentValues.put(ATTENDANCE_SWIPECARDNO, swipCard_no);
            contentValues.put(ATTENDANCE_SCHOOL_ID, school_id);
            contentValues.put(ATTENDANCE_MACHINE_ID, machine_id);
            contentValues.put(ATTENDANCE_MACHINE_NO, machine_no);
            contentValues.put(ATTENDANCE_GROUPID, Group_Id);
            contentValues.put(ATTENDANCE_DATE, date);
            contentValues.put(ATTENDANCE_INTIME, in_time);
            contentValues.put(ATTENDANCE_OUTTIME, out_time);
            contentValues.put(ATTENDANCE_STATUS, status);
            contentValues.put(ATTENDANCE_TYPE, type);
            contentValues.put(ATTENDANCE_SMSTREK, trk_sms);
            contentValues.put(ATTENDANCE_PRESENT_DATE, presentDate);
            contentValues.put(ATTENDANCE_PRESENT_MONTH, presentMonth);
            contentValues.put(ATTENDANCE_PRESENT_YEAR, presentYear);

            db.insert(TABLE_ATTENDANCE, null, contentValues);
            db.close();
            return true;
        }
        else{
            return false;
        }

    }

    public boolean UpdateAttendanceDetailsForOutTime(String dateForOutTime, String outTime) {
        String selectQuery = "SELECT * FROM "+ TABLE_ATTENDANCE +" WHERE "+ ATTENDANCE_DATE +" = "+""+dateForOutTime+""  +" AND "+ATTENDANCE_SMSTREK+" = "+"1"+" ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 1){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ATTENDANCE_OUTTIME, outTime);

          //  for multiple where clause
            String[] whereClauseArgument = new String[1];
            whereClauseArgument[0] = dateForOutTime;
            whereClauseArgument[1] = "1";
            db.update(TABLE_ATTENDANCE, contentValues, ATTENDANCE_DATE +" =? AND "+ATTENDANCE_SMSTREK+" = ?",whereClauseArgument);

//            db.update(TABLE_ATTENDANCE, contentValues, ATTENDANCE_DATE + " = ?",new String[]{dateForOutTime});
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

//    public void UpdateAttendance() throws JSONException {
//        String selectQuery = "SELECT * FROM "+ TABLE_ATTENDANCE +" WHERE "+ ATTENDANCE_SMSTREK +" = 2";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery(selectQuery, null);
//        res.moveToFirst();
//        JSONObject Root = new JSONObject();
//        JSONArray detailsArray = new JSONArray();
//        int i = 0;
//        while(res.isAfterLast() == false){
//            JSONObject details = new JSONObject();
//            String date = (res.getString(res.getColumnIndex(ATTENDANCE_DATE)));
//            String outTime = (res.getString(res.getColumnIndex(ATTENDANCE_INTIME)));
//
//
//            String selectQuery2 = "SELECT * FROM "+ TABLE_ATTENDANCE +" WHERE "+ ATTENDANCE_DATE +" = "+date+" AND "+ATTENDANCE_SMSTREK+" =1 ";
//            db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(selectQuery2, null);
//            int count =  cursor.getCount();
//            cursor.close();
//
//            if(count == 1) {
//                db = this.getWritableDatabase();
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(ATTENDANCE_OUTTIME, outTime);
//
//                //  for multiple where clause
//                String[] whereClauseArgument = new String[1];
//                whereClauseArgument[0] = date;
//                whereClauseArgument[1] = "1";
//                db.update(TABLE_ATTENDANCE, contentValues, ATTENDANCE_DATE + " =? AND " + ATTENDANCE_SMSTREK + " = ?", whereClauseArgument);
//                db.close();
//            }
//        }
//        res.close();
//    }

    public void updateStd_div_isTeaching(String standard, String division,String userId)  throws JSONException {
        String selectQuery = "SELECT * FROM "+ TABLE_USERDETAILS +" WHERE "+ USERDETAILS_USERID +" = "+userId+" ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 1){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(USERDETAILS_STANDARD, standard);
            contentValues.put(USERDETAILS_DIVISION, division);
            db.update(TABLE_USERDETAILS, contentValues, USERDETAILS_USERID + " = ?",new String[]{userId});
            db.close();
        }
    }


    public JSONArray getAttenadanceDetailsMonthWise(String userId, String month, String year) throws JSONException {

        String selectQuery = "SELECT * FROM " + TABLE_ATTENDANCE + " WHERE " + ATTENDANCE_STUDENT_ID +" = " +userId+" AND "+ATTENDANCE_PRESENT_MONTH +" = "+ month+" AND "+ATTENDANCE_PRESENT_YEAR +" = "+ year;
        //String selectQuery = "SELECT * FROM  AttendanceDetails WHERE student_id = 120032 AND presentMonth = 2 AND presentYear = 2017";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        int count =  res.getCount();
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(ATTENDANCE_ID, res.getString(res.getColumnIndex(ATTENDANCE_ID)));
            details.put(ATTENDANCE_STUDENT_ID, res.getString(res.getColumnIndex(ATTENDANCE_STUDENT_ID)));
            details.put(ATTENDANCE_SWIPECARDNO, res.getString(res.getColumnIndex(ATTENDANCE_SWIPECARDNO)));
            details.put(ATTENDANCE_SCHOOL_ID, res.getString(res.getColumnIndex(ATTENDANCE_SCHOOL_ID)));
            details.put(ATTENDANCE_MACHINE_ID, res.getString(res.getColumnIndex(ATTENDANCE_MACHINE_ID)));
            details.put(ATTENDANCE_MACHINE_NO, res.getString(res.getColumnIndex(ATTENDANCE_MACHINE_NO)));
            details.put(ATTENDANCE_GROUPID, res.getString(res.getColumnIndex(ATTENDANCE_GROUPID)));
            details.put(ATTENDANCE_TYPE, res.getString(res.getColumnIndex(ATTENDANCE_TYPE)));
            details.put(ATTENDANCE_DATE, res.getString(res.getColumnIndex(ATTENDANCE_DATE)));
            details.put(ATTENDANCE_INTIME, res.getString(res.getColumnIndex(ATTENDANCE_INTIME)));
            details.put(ATTENDANCE_OUTTIME, res.getString(res.getColumnIndex(ATTENDANCE_OUTTIME)));
            details.put(ATTENDANCE_STATUS, res.getString(res.getColumnIndex(ATTENDANCE_STATUS)));
            details.put(ATTENDANCE_SMSTREK, res.getString(res.getColumnIndex(ATTENDANCE_SMSTREK)));
            details.put(ATTENDANCE_PRESENT_DATE, res.getString(res.getColumnIndex(ATTENDANCE_PRESENT_DATE)));
            details.put(ATTENDANCE_PRESENT_MONTH, res.getString(res.getColumnIndex(ATTENDANCE_PRESENT_MONTH)));
            details.put(ATTENDANCE_PRESENT_YEAR, res.getString(res.getColumnIndex(ATTENDANCE_PRESENT_YEAR)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }


    public boolean DeleteUserAccount(String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERDETAILS, USERDETAILS_USERID + "=" + studentId, null) > 0;
    }

    public JSONArray getSelectedUserDetails(String studentId) throws JSONException {
        String selectQuery = "SELECT * FROM "+ TABLE_USERDETAILS +" WHERE "+ USERDETAILS_USERID +" = "+ studentId+" ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(USERDETAILS_USERID, res.getString(res.getColumnIndex(USERDETAILS_USERID)));
            details.put(USERDETAILS_USERTYPE, res.getString(res.getColumnIndex(USERDETAILS_USERTYPE)));
            details.put(USERDETAILS_NAME, res.getString(res.getColumnIndex(USERDETAILS_NAME)));
            details.put(USERDETAILS_STANDARD, res.getString(res.getColumnIndex(USERDETAILS_STANDARD)));
            details.put(USERDETAILS_DIVISION, res.getString(res.getColumnIndex(USERDETAILS_DIVISION)));
            details.put(USERDETAILS_MOBILENO, res.getString(res.getColumnIndex(USERDETAILS_MOBILENO)));
            details.put(USERDETAILS_EMAILID, res.getString(res.getColumnIndex(USERDETAILS_EMAILID)));
            details.put(USERDETAILS_DOB, res.getString(res.getColumnIndex(USERDETAILS_DOB)));
            details.put(USERDETAILS_ADDRESS, res.getString(res.getColumnIndex(USERDETAILS_ADDRESS)));
            details.put(USERDETAILS_GRNO, res.getString(res.getColumnIndex(USERDETAILS_GRNO)));
            details.put(USERDETAILS_BLOODGROUP, res.getString(res.getColumnIndex(USERDETAILS_BLOODGROUP)));
            details.put(USERDETAILS_SWIPECARDNO, res.getString(res.getColumnIndex(USERDETAILS_SWIPECARDNO)));
            details.put(USERDETAILS_ROLLNO, res.getString(res.getColumnIndex(USERDETAILS_ROLLNO)));
            details.put(USERDETAILS_IMAAGE, res.getString(res.getColumnIndex(USERDETAILS_IMAAGE)));
            details.put(USERDETAILS_SCHOOL_ID, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_ID)));
            details.put(USERDETAILS_SCHOOL_LOGO, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_LOGO)));
            details.put(USERDETAILS_SCHOOL_NAME, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_NAME)));
            details.put(USERDETAILS_SCHOOL_WEBSITE, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_WEBSITE)));
            details.put(USERDETAILS_SCHOOL_PHONENO, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_PHONENO)));
            details.put(USERDETAILS_SCHOOL_ADDRESS, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_ADDRESS)));
            details.put(USERDETAILS_SCHOOL_EAMIL, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_EAMIL)));
            details.put(USERDETAILS_ISTEACHING_STAFF, res.getString(res.getColumnIndex(USERDETAILS_ISTEACHING_STAFF)));
            details.put(USERDETAILS_SCHOOL_LAT, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_LAT)));
            details.put(USERDETAILS_SCHOOL_LONGI, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_LONGI)));
            details.put(USERDETAILS_ISCLASS_TEACHER, res.getString(res.getColumnIndex(USERDETAILS_ISCLASS_TEACHER)));
            details.put(USERDETAILS_PIN, res.getString(res.getColumnIndex(USERDETAILS_PIN)));
            details.put(USERDETAILS_SMS_SENDERID, res.getString(res.getColumnIndex(USERDETAILS_SMS_SENDERID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public JSONArray getDefaultUserDetails() throws JSONException {
        String selectQuery = "SELECT * FROM "+ TABLE_USERDETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(USERDETAILS_USERID, res.getString(res.getColumnIndex(USERDETAILS_USERID)));
            details.put(USERDETAILS_USERTYPE, res.getString(res.getColumnIndex(USERDETAILS_USERTYPE)));
            details.put(USERDETAILS_NAME, res.getString(res.getColumnIndex(USERDETAILS_NAME)));
            details.put(USERDETAILS_STANDARD, res.getString(res.getColumnIndex(USERDETAILS_STANDARD)));
            details.put(USERDETAILS_DIVISION, res.getString(res.getColumnIndex(USERDETAILS_DIVISION)));
            details.put(USERDETAILS_MOBILENO, res.getString(res.getColumnIndex(USERDETAILS_MOBILENO)));
            details.put(USERDETAILS_EMAILID, res.getString(res.getColumnIndex(USERDETAILS_EMAILID)));
            details.put(USERDETAILS_DOB, res.getString(res.getColumnIndex(USERDETAILS_DOB)));
            details.put(USERDETAILS_ADDRESS, res.getString(res.getColumnIndex(USERDETAILS_ADDRESS)));
            details.put(USERDETAILS_GRNO, res.getString(res.getColumnIndex(USERDETAILS_GRNO)));
            details.put(USERDETAILS_BLOODGROUP, res.getString(res.getColumnIndex(USERDETAILS_BLOODGROUP)));
            details.put(USERDETAILS_SWIPECARDNO, res.getString(res.getColumnIndex(USERDETAILS_SWIPECARDNO)));
            details.put(USERDETAILS_ROLLNO, res.getString(res.getColumnIndex(USERDETAILS_ROLLNO)));
            details.put(USERDETAILS_IMAAGE, res.getString(res.getColumnIndex(USERDETAILS_IMAAGE)));
            details.put(USERDETAILS_SCHOOL_ID, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_ID)));
            details.put(USERDETAILS_SCHOOL_LOGO, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_LOGO)));
            details.put(USERDETAILS_SCHOOL_NAME, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_NAME)));
            details.put(USERDETAILS_SCHOOL_WEBSITE, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_WEBSITE)));
            details.put(USERDETAILS_SCHOOL_PHONENO, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_PHONENO)));
            details.put(USERDETAILS_SCHOOL_ADDRESS, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_ADDRESS)));
            details.put(USERDETAILS_SCHOOL_EAMIL, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_EAMIL)));
            details.put(USERDETAILS_ISTEACHING_STAFF, res.getString(res.getColumnIndex(USERDETAILS_ISTEACHING_STAFF)));
            details.put(USERDETAILS_SCHOOL_LAT, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_LAT)));
            details.put(USERDETAILS_SCHOOL_LONGI, res.getString(res.getColumnIndex(USERDETAILS_SCHOOL_LONGI)));
            details.put(USERDETAILS_ISCLASS_TEACHER, res.getString(res.getColumnIndex(USERDETAILS_ISCLASS_TEACHER)));
            details.put(USERDETAILS_PIN, res.getString(res.getColumnIndex(USERDETAILS_PIN)));
            details.put(USERDETAILS_SMS_SENDERID, res.getString(res.getColumnIndex(USERDETAILS_SMS_SENDERID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }


    public String getDefaultUser() {
        String selectQuery = "SELECT "+USERDETAILS_USERID+" FROM "+ TABLE_USERDETAILS;
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();
        String details = "";
        if(count != 0){
            cursor.moveToFirst();
            details = String.valueOf(cursor.moveToFirst());
        }
        return details;
    }

    public boolean InsertSMS (String msgListId,String msgType,String msg,String msgDate,String msgTime,String userId) {

        String selectQuery = "SELECT * FROM  MsgDetails WHERE "+ MSG_LIST_ID+ " = '"+msgListId+"'";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MSG_LIST_ID, msgListId);
            contentValues.put(MSG_TYPE, msgType);
            contentValues.put(MSG, msg);
            contentValues.put(MSG_DATE, msgDate);
            contentValues.put(MSG_TIME, msgTime);
            contentValues.put(USERID, userId);
            db.insert(TABLE_MSG, null, contentValues);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    public JSONArray GetAllMsg(String userId,String MsgType) throws JSONException {

        String selectQuery ="";
        if(MsgType.equals("Attendance")){
            selectQuery = "SELECT * FROM "+ TABLE_MSG +" WHERE "+ MSG_TYPE +" = '"+ MsgType +"' AND "+USERID+ " = '"+userId+"'";
        }
        else{
            selectQuery = "SELECT * FROM "+TABLE_MSG+" WHERE " + MSG_TYPE + " NOT IN ('Attendance')  AND "+USERID+ " = " +userId+ "";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(MSG_ID, res.getString(res.getColumnIndex(MSG_ID)));
            details.put(MSG_LIST_ID, res.getString(res.getColumnIndex(MSG_LIST_ID)));
            details.put(MSG, res.getString(res.getColumnIndex(MSG)));
            details.put(MSG_TYPE, res.getString(res.getColumnIndex(MSG_TYPE)));
            details.put(MSG_DATE, res.getString(res.getColumnIndex(MSG_DATE)));
            details.put(MSG_TIME, res.getString(res.getColumnIndex(MSG_TIME)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public JSONArray GetDateWiseMsg(String userId,String MsgType,String Date) throws JSONException {

        String selectQuery ="";


        if(MsgType.equals("Attendance")){
            selectQuery = "SELECT * FROM "+ TABLE_MSG +" WHERE "+ MSG_TYPE +" = '"+ MsgType +"' AND "+USERID+ " = '"+userId+"' AND "+MSG_DATE+ " = '"+Date+"' ";
        }
        else{
            selectQuery = "SELECT * FROM "+TABLE_MSG+" WHERE " + MSG_TYPE + " NOT IN ('Attendance')  AND "+USERID+ " = " +userId+ " AND "+MSG_DATE+ " = '"+Date+"'";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(MSG_ID, res.getString(res.getColumnIndex(MSG_ID)));
            details.put(MSG_LIST_ID, res.getString(res.getColumnIndex(MSG_LIST_ID)));
            details.put(MSG, res.getString(res.getColumnIndex(MSG)));
            details.put(MSG_TYPE, res.getString(res.getColumnIndex(MSG_TYPE)));
            details.put(MSG_DATE, res.getString(res.getColumnIndex(MSG_DATE)));
            details.put(MSG_TIME, res.getString(res.getColumnIndex(MSG_TIME)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }
}
