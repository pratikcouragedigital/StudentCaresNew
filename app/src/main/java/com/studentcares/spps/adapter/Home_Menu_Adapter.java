package com.studentcares.spps.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import com.studentcares.spps.About_My_School;
import com.studentcares.spps.About_StudentCares;
import com.studentcares.spps.Gallery_Title_List;
import com.studentcares.spps.Holiday;
import com.studentcares.spps.Homework_List_Parents;
import com.studentcares.spps.Home_Sub_Menus;
import com.studentcares.spps.DashBoard_Count;
import com.studentcares.spps.Home_Menu;
import com.studentcares.spps.My_Attendance;
import com.studentcares.spps.My_Profile;
import com.studentcares.spps.News_List;
import com.studentcares.spps.Notice_List;
import com.studentcares.spps.R;
import com.studentcares.spps.Machine_Details;
import com.studentcares.spps.Homework_List_Teacher;
import com.studentcares.spps.PTA_Members_Tab;
import com.studentcares.spps.Rankers;
import com.studentcares.spps.SMS_InBox_Tab;
import com.studentcares.spps.Study_Material;
import com.studentcares.spps.Syllabus;
import com.studentcares.spps.UpdateUI;
import com.studentcares.spps.firebase.MyFirebaseMesagingService;
import com.studentcares.spps.model.HomeGrid_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.NotificationCounter_Instance;

import java.util.HashMap;
import java.util.List;

public class Home_Menu_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<HomeGrid_Items> menuItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    int attCounter = 0;
    Home_Menu homeMenuActivity;
    String subMenuFor="";

    public int attendanceCount = 0;
    public int smsListCount = 0;
    public int homeworkCount = 0;
    public int newsCount = 0;
    public int noticeCount = 0;

    public String attendanceRead = "Read";
    public String smsListRead = "Read";
    public String homeworkRead = "Read";
    public String newsRead = "Read";
    public String noticeRead = "Read";
    public NotificationCounter_Instance notificationCount;

    public static final String ACTION_LOCATION_BROADCAST = MyFirebaseMesagingService.class.getName() + "MyFirebaseMesagingService";
    public static final String EXTRA_COUNT = "extra_count";
    public static final String EXTRA_READ = "extra_read";
    public static final String EXTRA_IMFROM = "extra_imFrom";
    String imFrom = "";


    public Home_Menu_Adapter(List<HomeGrid_Items> items, Home_Menu activity) {
        this.menuItems = items;
        this.homeMenuActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_menu_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            HomeGrid_Items itemOflist = menuItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        NotificationBadge mBadge;
        public ImageView menuImage;
        public TextView title;
        public View cardView;
        SessionManager sessionManager;
        String schoolId,userType,userId;

        HomeGrid_Items menuItems = new HomeGrid_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
            schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
            userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
            userId = typeOfUser.get(SessionManager.KEY_USERID);

            menuImage = (ImageView) itemView.findViewById(R.id.menuImage);
            mBadge = (NotificationBadge) itemView.findViewById(R.id.badge);
//            title = (TextView) itemView.findViewById(R.id.title);
            cardView = itemView;
            cardView.setOnClickListener(this);

        }

        public void bindListDetails(HomeGrid_Items menuItems) {
            this.menuItems = menuItems;

            menuImage.setImageResource(menuItems.getFirstImagePath());
//            title.setText(menuItems.gettitle());

            String menuName = menuItems.gettitle();


            if(menuName.equals("SMS")){
                HashMap<String, String> smsList = sessionManager.getsmsListCount();
                smsListCount = Integer.parseInt(smsList.get(SessionManager.smsListCount));
                smsListRead = smsList.get(SessionManager.smsListRead);

                if(smsListCount != 0){
                    mBadge.setNumber(smsListCount);
                }
                else{
                    mBadge.setVisibility(View.GONE);
                }
            }
            else if(menuName.equals("Homework")){
                HashMap<String, String> homework = sessionManager.gethomeworkCount();
                homeworkCount = Integer.parseInt(homework.get(SessionManager.homeworkCount));
                homeworkRead = homework.get(SessionManager.homeworkRead);

                if(homeworkCount != 0){
                    mBadge.setNumber(homeworkCount);
                }
                else{
                    mBadge.setVisibility(View.GONE);
                }
            }
            else if(menuName.equals("News")){
                HashMap<String, String> news = sessionManager.getnewsCount();
                newsCount = Integer.parseInt(news.get(sessionManager.newsCount));
                newsRead = news.get(sessionManager.newsRead);

                if(newsCount != 0){
                    mBadge.setNumber(newsCount);
                }
                else{
                    mBadge.setVisibility(View.GONE);
                }
            }
            else if(menuName.equals("Notice")){
                HashMap<String, String> notice = sessionManager.getnoticeCount();

                noticeCount = Integer.parseInt(notice.get(sessionManager.noticeCount));
                noticeRead = notice.get(sessionManager.noticeRead);

                if(noticeCount != 0){
                    mBadge.setNumber(noticeCount);
                }
                else{
                    mBadge.setVisibility(View.GONE);
                }
            }
            else{
                mBadge.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {

            int position = menuItems.getgridId();
            String gridName = menuItems.gettitle();

            if(gridName.equals("Homework")){
                homeworkRead = "Read";
                homeworkCount = 0;
                sessionManager.sethomeworkCount(String.valueOf(homeworkCount),homeworkRead);
                UpdateUI up = new UpdateUI();
                up.UpdateUIForNotificationRead(String.valueOf(homeworkCount), homeworkRead, "Homework");
            }
            else if(gridName.equals("News")){
                newsRead = "Read";
                newsCount = 0;
                sessionManager.setnewsCount(String.valueOf(newsCount),newsRead);
                UpdateUI up = new UpdateUI();
                up.UpdateUIForNotificationRead(String.valueOf(noticeCount), noticeRead, "News");
            }
            else if(gridName.equals("Notice")){
                noticeRead = "Read";
                noticeCount = 0;
                sessionManager.setnoticeCount(String.valueOf(noticeCount),noticeRead);
                UpdateUI up = new UpdateUI();
                up.UpdateUIForNotificationRead(String.valueOf(noticeCount), noticeRead, "Notice");
            }
            else if(gridName.equals("SMS")){
                smsListRead = "Read";
                smsListCount = 0;
                sessionManager.setsmsListCount(String.valueOf(smsListCount),smsListRead);
                UpdateUI up = new UpdateUI();
                up.UpdateUIForNotificationRead(String.valueOf(smsListCount), smsListRead, "SMS");
            }

            if (userType.equals("UserAdmin") || userType.equals("Staff")){
                if(gridName.equals("Dashboard")){
                    Intent gotoDashboard = new Intent(v.getContext(), DashBoard_Count.class);
                    v.getContext().startActivity(gotoDashboard);
                }
                else if(gridName.equals("Machine Details")){
                    Intent gotoMachineDetails = new Intent(v.getContext(), Machine_Details.class);
                    v.getContext().startActivity(gotoMachineDetails);
                }
                else if(gridName.equals("Attendance")){
                    subMenuFor = "Attendance";
                    Intent gotoAttendanceMenu = new Intent(v.getContext(), Home_Sub_Menus.class);
                    gotoAttendanceMenu.putExtra("subMenuFor",subMenuFor);
                    v.getContext().startActivity(gotoAttendanceMenu);
                }
                else if(gridName.equals("Graph")){
                    subMenuFor = "Graph";
                    Intent gotoGraph = new Intent(v.getContext(), Home_Sub_Menus.class);
                    gotoGraph.putExtra("subMenuFor",subMenuFor);
                    v.getContext().startActivity(gotoGraph);
                }
                else if(gridName.equals("SMS")){
                    subMenuFor = "SMS";
                    Intent gotoTimeTable = new Intent(v.getContext(), Home_Sub_Menus.class);
                    gotoTimeTable.putExtra("subMenuFor",subMenuFor);
                    v.getContext().startActivity(gotoTimeTable);
                }
                else if(gridName.equals("Homework")){
                    Intent gotoHomeworkList = new Intent(v.getContext(), Homework_List_Teacher.class);
                    v.getContext().startActivity(gotoHomeworkList);
                }
                else if(gridName.equals("Time Table")){
                    subMenuFor = "TimeTable";
                    Intent gotoTimeTable = new Intent(v.getContext(), Home_Sub_Menus.class);
                    gotoTimeTable.putExtra("subMenuFor",subMenuFor);
                    v.getContext().startActivity(gotoTimeTable);
                }
                else if(gridName.equals("News")){
                    Intent gotoNewsList = new Intent(v.getContext(), News_List.class);
                    v.getContext().startActivity(gotoNewsList);
                }
                else if(gridName.equals("Notice")){
                    Intent gotoNoticeList = new Intent(v.getContext(), Notice_List.class);
                    v.getContext().startActivity(gotoNoticeList);
                }
                else if(gridName.equals("Holiday")){
                    Intent gotoHolidays = new Intent(v.getContext(), Holiday.class);
                    v.getContext().startActivity(gotoHolidays);
                }
                else if(gridName.equals("PTA")){
                    Intent gotoPTA = new Intent(v.getContext(), PTA_Members_Tab.class);
                    v.getContext().startActivity(gotoPTA);
                }
                else if(gridName.equals("Fees")){
                    Toast.makeText(v.getContext(), "Working Under Progress!!", Toast.LENGTH_SHORT).show();
//                    Intent gotoFees = new Intent(v.getContext(), Fees_Tab.class);
//                    v.getContext().startActivity(gotoFees);
                }
                else if(gridName.equals("Gallery")){
                    Intent gotoEventList = new Intent(v.getContext(), Gallery_Title_List.class);
                    v.getContext().startActivity(gotoEventList);
                }
                else if(gridName.equals("Study Material")){
                    Intent gotoSyllabus = new Intent(v.getContext(), Study_Material.class);
                    v.getContext().startActivity(gotoSyllabus);
                }
                else if(gridName.equals("Syllabus")){
                    Intent gotoSyllabus = new Intent(v.getContext(), Syllabus.class);
                    v.getContext().startActivity(gotoSyllabus);
                }
                else if(gridName.equals("Ranker")){
                    Intent gotRanker = new Intent(v.getContext(), Rankers.class);
                    v.getContext().startActivity(gotRanker);
                }
                else if(gridName.equals("Leave")){
                    subMenuFor = "Leave";
                    Intent gotoTimeTable = new Intent(v.getContext(), Home_Sub_Menus.class);
                    gotoTimeTable.putExtra("subMenuFor",subMenuFor);
                    v.getContext().startActivity(gotoTimeTable);
                }
                else if(gridName.equals("GPS")){
                    subMenuFor = "GPS";
                    Intent gotoGPS = new Intent(v.getContext(), Home_Sub_Menus.class);
                    gotoGPS.putExtra("subMenuFor",subMenuFor);
                    v.getContext().startActivity(gotoGPS);
                }
                else if(gridName.equals("Profile")){
                    Intent gotoProfile = new Intent(v.getContext(), My_Profile.class);
                    v.getContext().startActivity(gotoProfile);
                }
                else if(gridName.equals("About School")){
                    Intent gotoSchool = new Intent(v.getContext(), About_My_School.class);
                    v.getContext().startActivity(gotoSchool);
                }
                else if(gridName.equals("StudentCares")){
                    Intent gotoStudentcares = new Intent(v.getContext(), About_StudentCares.class);
                    v.getContext().startActivity(gotoStudentcares);
                }
            }
            else if (userType.equals("NonTeachingStaff")) {
                if(gridName.equals("Dashboard")){
                    Intent gotoDashboard = new Intent(v.getContext(), DashBoard_Count.class);
                    v.getContext().startActivity(gotoDashboard);
                }
                else if(gridName.equals("Machine Details")){
                    Intent gotoMachineDetails = new Intent(v.getContext(), Machine_Details.class);
                    v.getContext().startActivity(gotoMachineDetails);
                }
                else if(gridName.equals("Attendance")){
                    Intent gotoOwnAttendance = new Intent(v.getContext(), My_Attendance.class);
                    v.getContext().startActivity(gotoOwnAttendance);
                }
                else if(gridName.equals("SMS")){
                    Intent gotoSMS = new Intent(v.getContext(), SMS_InBox_Tab.class);
                    v.getContext().startActivity(gotoSMS);
                }
                else if(gridName.equals("News")){
                    Intent gotoNews = new Intent(v.getContext(), News_List.class);
                    v.getContext().startActivity(gotoNews);
                }
                else if(gridName.equals("Notice")){
                    Intent gotoNotice = new Intent(v.getContext(), Notice_List.class);
                    v.getContext().startActivity(gotoNotice);
                }
                else if(gridName.equals("Holiday")){
                    Intent gotoHoloday = new Intent(v.getContext(), Holiday.class);
                    v.getContext().startActivity(gotoHoloday);
                }
                else if(gridName.equals("Gallery")){
                    Intent gotoGallery = new Intent(v.getContext(), Gallery_Title_List.class);
                    v.getContext().startActivity(gotoGallery);
                }
                else if(gridName.equals("Ranker")){
                    Intent gotoRankers = new Intent(v.getContext(), Rankers.class);
                    v.getContext().startActivity(gotoRankers);
                }
                else if(gridName.equals("Leave")){
                    subMenuFor = "Leave";
                    Intent gotoTimeTable = new Intent(v.getContext(), Home_Sub_Menus.class);
                    gotoTimeTable.putExtra("subMenuFor",subMenuFor);
                    v.getContext().startActivity(gotoTimeTable);
                }
                else if(gridName.equals("GPS")){
                    subMenuFor = "GPS";
                    Intent gotoGPS = new Intent(v.getContext(), Home_Sub_Menus.class);
                    gotoGPS.putExtra("subMenuFor",subMenuFor);
                    v.getContext().startActivity(gotoGPS);
                }
                else if(gridName.equals("Profile")){
                    Intent gotoProfile = new Intent(v.getContext(), My_Profile.class);
                    v.getContext().startActivity(gotoProfile);
                }
                else if(gridName.equals("About School")){
                    Intent gotoSchool = new Intent(v.getContext(), About_My_School.class);
                    v.getContext().startActivity(gotoSchool);
                }
                else if(gridName.equals("StudentCares")){
                    Intent gotoStudentcares = new Intent(v.getContext(), About_StudentCares.class);
                    v.getContext().startActivity(gotoStudentcares);
                }
            }
            else if (userType.equals("Student")) {
                if(gridName.equals("Attendance")){
                    Intent gotoAttendance = new Intent(v.getContext(), My_Attendance.class);
                    v.getContext().startActivity(gotoAttendance);
                }
                else if(gridName.equals("SMS")){
                    Intent gotoSMS = new Intent(v.getContext(), SMS_InBox_Tab.class);
                    v.getContext().startActivity(gotoSMS);
                }
                else if(gridName.equals("Homework")){
                    Intent gotoHomeWork = new Intent(v.getContext(), Homework_List_Parents.class);
                    v.getContext().startActivity(gotoHomeWork);
                }
                else if(gridName.equals("Fees")){
                    Toast.makeText(v.getContext(), "Working Under Progress!!", Toast.LENGTH_SHORT).show();
//                    Intent gotoFees = new Intent(v.getContext(), Fees_Tab.class);
//                    v.getContext().startActivity(gotoFees);
                }
                else if(gridName.equals("Time Table")){
                    subMenuFor = "TimeTable";
                    Intent gotoTimeTable = new Intent(v.getContext(), Home_Sub_Menus.class);
                    gotoTimeTable.putExtra("subMenuFor",subMenuFor);
                    v.getContext().startActivity(gotoTimeTable);
                }
                else if(gridName.equals("News")){
                    Intent gotoNews = new Intent(v.getContext(), News_List.class);
                    v.getContext().startActivity(gotoNews);
                }
                else if(gridName.equals("Notice")){
                    Intent gotoNotice = new Intent(v.getContext(), Notice_List.class);
                    v.getContext().startActivity(gotoNotice);
                }
                else if(gridName.equals("Holiday")){
                    Intent gotoHoloday = new Intent(v.getContext(), Holiday.class);
                    v.getContext().startActivity(gotoHoloday);
                }
                else if(gridName.equals("Gallery")){
                    Intent gotoGallery = new Intent(v.getContext(), Gallery_Title_List.class);
                    v.getContext().startActivity(gotoGallery);
                }
                else if(gridName.equals("Study Material")){
                    Intent gotoStudyMaterial = new Intent(v.getContext(), Study_Material.class);
                    v.getContext().startActivity(gotoStudyMaterial);
                }
                else if(gridName.equals("Syllabus")){
                    Intent gotoSyllabus = new Intent(v.getContext(), Syllabus.class);
                    v.getContext().startActivity(gotoSyllabus);
                }
                else if(gridName.equals("Ranker")){
                    Intent gotoRankers = new Intent(v.getContext(), Rankers.class);
                    v.getContext().startActivity(gotoRankers);
                }
                else if(gridName.equals("Profile")){
                    Intent gotoProfile = new Intent(v.getContext(), My_Profile.class);
                    v.getContext().startActivity(gotoProfile);
                }
                else if(gridName.equals("About School")){
                    Intent gotoSchool = new Intent(v.getContext(), About_My_School.class);
                    v.getContext().startActivity(gotoSchool);
                }
                else if(gridName.equals("StudentCares")){
                    Intent gotoStudentcares = new Intent(v.getContext(), About_StudentCares.class);
                    v.getContext().startActivity(gotoStudentcares);
                }
            }
        }
    }
}
