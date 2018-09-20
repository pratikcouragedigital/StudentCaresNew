package com.studentcares.spps.adapter;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.studentcares.spps.Attendance_Tab_Staff;
import com.studentcares.spps.Attendance_Tab_Student;
import com.studentcares.spps.Emergency_Exit;
import com.studentcares.spps.GPS;
import com.studentcares.spps.GPS_Staff_OutWork;
import com.studentcares.spps.Graph_Tab_Daily;
import com.studentcares.spps.Graph_Tab_Monthly;
import com.studentcares.spps.Leave_Apply;
import com.studentcares.spps.Leave_List_Own;
import com.studentcares.spps.Leave_To_Approve_List;
import com.studentcares.spps.My_Attendance;
import com.studentcares.spps.R;
import com.studentcares.spps.SMS_InBox_Tab;
import com.studentcares.spps.SMS_Report;
import com.studentcares.spps.SMS_Send;
import com.studentcares.spps.TimeTable_Exam;
import com.studentcares.spps.TimeTable_Staff;
import com.studentcares.spps.TimeTable_Student;
import com.studentcares.spps.model.HomeGrid_Items;
import com.studentcares.spps.sessionManager.SessionManager;

import java.util.HashMap;
import java.util.List;

public class Home_Sub_Menu_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<HomeGrid_Items> productListItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    int attCounter = 0;
    String subMenuFor;


    public Home_Sub_Menu_Adapter(List<HomeGrid_Items> items, String subMenuForA) {
        this.productListItems = items;
        this.subMenuFor = subMenuForA;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_sub_menu_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            HomeGrid_Items itemOflist = productListItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        return productListItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView menuImage;
        public TextView title;
        public View cardView;
        SessionManager sessionManager;
        String schoolId,userType,userId;

        HomeGrid_Items productListItems = new HomeGrid_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> typeOfUser = sessionManager.getUserDetails();
            schoolId = typeOfUser.get(SessionManager.KEY_SCHOOLID);
            userType = typeOfUser.get(SessionManager.KEY_USERTYPE);
            userId = typeOfUser.get(SessionManager.KEY_USERID);


            menuImage = (ImageView) itemView.findViewById(R.id.menuImage);
//            title = (TextView) itemView.findViewById(R.id.title);
            cardView = itemView;
            cardView.setOnClickListener(this);
        }

        public void bindListDetails(HomeGrid_Items productListItems) {
            this.productListItems = productListItems;

            menuImage.setImageResource(productListItems.getFirstImagePath());
//            title.setText(productListItems.gettitle());
        }

        @Override
        public void onClick(View v) {

            int position = productListItems.getgridId();
            String gridName = productListItems.gettitle();

            if (subMenuFor.equals("Attendance")){

                if(gridName.equals("Student Attendance")){
                    Intent gotoStudAttendance = new Intent(v.getContext(), Attendance_Tab_Student.class);
                    v.getContext().startActivity(gotoStudAttendance);
                }
                else if(gridName.equals("Staff Attendance")){
                    Intent gotoStaffAttendance = new Intent(v.getContext(), Attendance_Tab_Staff.class);
                    v.getContext().startActivity(gotoStaffAttendance);
                }
                else if(gridName.equals("Emergency Exit")){
                    Intent gotoEmergencyExit = new Intent(v.getContext(), Emergency_Exit.class);
                    v.getContext().startActivity(gotoEmergencyExit);
                }
                else if(gridName.equals("Own Attendance")){
                    Intent gotoMyAttendance = new Intent(v.getContext(), My_Attendance.class);
                    v.getContext().startActivity(gotoMyAttendance);
                }
            }
            else if (subMenuFor.equals("SMS")){
                if(gridName.equals("SMS Send")){
                    Intent gotoSendSMS = new Intent(v.getContext(), SMS_Send.class);
                    gotoSendSMS.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    v.getContext().startActivity(gotoSendSMS);
                }
                else if(gridName.equals("SMS Receive")){
                    Intent gotoReceiveSMS = new Intent(v.getContext(), SMS_InBox_Tab.class);
                    gotoReceiveSMS.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    v.getContext().startActivity(gotoReceiveSMS);
                }
                else if(gridName.equals("SMS Report")){
                    Intent gotoSMSReport = new Intent(v.getContext(), SMS_Report.class);
                    gotoSMSReport.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    v.getContext().startActivity(gotoSMSReport);
                }
            }
            else if (subMenuFor.equals("TimeTable")){
                if(userType.equals("Student")){
                    if(gridName.equals("Class Wise Timetable")){
//                    Toast.makeText(v.getContext(), gridName, Toast.LENGTH_SHORT).show();
                        Intent gotoTimeTableStudent = new Intent(v.getContext(), TimeTable_Student.class);
                        v.getContext().startActivity(gotoTimeTableStudent);

                    }
                    else if(gridName.equals("Exam Timetable")){
                        Toast.makeText(v.getContext(), "Working Under Progress!!", Toast.LENGTH_SHORT).show();
//                        Intent gotoTimeTableExam = new Intent(v.getContext(), TimeTable_Exam.class);
//                        v.getContext().startActivity(gotoTimeTableExam);
                    }
                }
                else{

                    if(gridName.equals("Class Wise Timetable")){
//                    Toast.makeText(v.getContext(), gridName, Toast.LENGTH_SHORT).show();
                        Intent gotoTimeTableStudent = new Intent(v.getContext(), TimeTable_Student.class);
                        v.getContext().startActivity(gotoTimeTableStudent);

                    }
                    else if(gridName.equals("Exam Timetable")){
                        //Toast.makeText(v.getContext(), gridName, Toast.LENGTH_SHORT).show();
                        Intent gotoTimeTableExam = new Intent(v.getContext(), TimeTable_Exam.class);
                        v.getContext().startActivity(gotoTimeTableExam);
                    }
                    else if(gridName.equals("Own Timetable")){
//                    Toast.makeText(v.getContext(), gridName, Toast.LENGTH_SHORT).show();
                        Intent gotoTimeTableStaff = new Intent(v.getContext(), TimeTable_Staff.class);
                        v.getContext().startActivity(gotoTimeTableStaff);
                    }
                }
            }
            else if (subMenuFor.equals("GPS")){
                if(gridName.equals("Personal GPS Tracker")){
                    Toast.makeText(v.getContext(), "Working Under Progress!!", Toast.LENGTH_SHORT).show();
                }
                else if(gridName.equals("Bus GPS Tracker")){
                    Toast.makeText(v.getContext(), "Working Under Progress!!", Toast.LENGTH_SHORT).show();
                }
                else if(gridName.equals("My Outwork Location")){
                    Intent gotoStaffOutWork = new Intent(v.getContext(), GPS_Staff_OutWork.class);
                    v.getContext().startActivity(gotoStaffOutWork);
                }
                else if(gridName.equals("Track Staff")){
                    Intent gotoStaffOutWork = new Intent(v.getContext(), GPS.class);
                    v.getContext().startActivity(gotoStaffOutWork);
                }
            }
            else if (subMenuFor.equals("Graph")){
                if(gridName.equals("Monthly Graph")){
                    Intent gotoGraphDaily = new Intent(v.getContext(), Graph_Tab_Daily.class);
                    v.getContext().startActivity(gotoGraphDaily);
                }
                else if(gridName.equals("Daily Graph")){
                    Intent gotoGraphMonthly = new Intent(v.getContext(), Graph_Tab_Monthly.class);
                    v.getContext().startActivity(gotoGraphMonthly);
                }
            }
            else if (subMenuFor.equals("Leave")){
                if(userType.equals("UserAdmin")){
                    if(gridName.equals("Apply Leave")){
                        Intent gotoLeaveForm = new Intent(v.getContext(), Leave_Apply.class);
                        v.getContext().startActivity(gotoLeaveForm);
                    }
                    else if(gridName.equals("Approve Leave")){
                        Intent gotoLeave = new Intent(v.getContext(), Leave_To_Approve_List.class);
                        v.getContext().startActivity(gotoLeave);

                    }
                    else if(gridName.equals("Own Leave List")){
                        Intent gotoLeave = new Intent(v.getContext(), Leave_List_Own.class);
                        v.getContext().startActivity(gotoLeave);
                    }
                }
                else{
                    if(gridName.equals("Apply Leave")){
                        Intent gotoLeaveForm = new Intent(v.getContext(), Leave_Apply.class);
                        v.getContext().startActivity(gotoLeaveForm);
                    }
                    else if(gridName.equals("Own Leave List")){
                        Intent gotoLeave = new Intent(v.getContext(), Leave_List_Own.class);
                        v.getContext().startActivity(gotoLeave);

                    }
                }

            }
        }
    }
}