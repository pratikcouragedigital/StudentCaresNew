package com.studentcares.spps;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studentcares.spps.adapter.Attendance_Staff_Present_List_Adapter;
import com.studentcares.spps.commonClasses.RemoveLastComma;
import com.studentcares.spps.connectivity.Attendance_Staff_Marking;
import com.studentcares.spps.connectivity.Attendance_Get_Absent_Present_List_Staff;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.Chk_Mark_Attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//@SuppressLint("ValidFragment")
public class Attendance_Tab_Staff_Present_List extends Fragment implements View.OnClickListener{

    ProgressDialog progressDialog;
    List<Attendance_List_Items> attendanceItems = new ArrayList<Attendance_List_Items>();
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    String userId;
    String schoolId;
    LinearLayoutManager linearLayoutManager;

    int current_page = 1;
    int value;
    private View inflatedView;
    private View v;

    FloatingActionButton attendanceStaffCheckFab;
    Chk_Mark_Attendance chk_mark_attendance;
    List<String> absentStaffListArray;
    private String staffId;
    private String attendanceCount;

//    @SuppressLint("ValidFragment")
//    public Attendance_Tab_Staff_Present_List(String countP) {
//        attendanceCount = countP;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.attendance_tab_staff_present, container, false);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SessionManager sessionManager = new SessionManager(v.getContext().getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERID);
        schoolId = user.get(SessionManager.KEY_SCHOOLID);

        recyclerView = (RecyclerView) getView().findViewById(R.id.staffRecyclerView);
        recyclerView.setHasFixedSize(true);

        attendanceStaffCheckFab = (FloatingActionButton) getView().findViewById(R.id.attendanceStaffCheckFab);
        attendanceStaffCheckFab.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Attendance_Staff_Present_List_Adapter(attendanceItems, attendanceStaffCheckFab);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        getList();
    }

    private void getList(){
        try {
            Attendance_Get_Absent_Present_List_Staff showAttendanseDetails = new Attendance_Get_Absent_Present_List_Staff(getActivity());
            showAttendanseDetails.show_Present_Staff_List(attendanceItems, recyclerView, adapter,userId,schoolId, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.attendanceStaffCheckFab) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to mark as a absent?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeAttendance();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }

    }

    public void removeAttendance() {
        absentStaffListArray = chk_mark_attendance.getAbsentStaffListInstance();

        RemoveLastComma removeLastComma = new RemoveLastComma(getActivity());
        staffId = removeLastComma.CommaRemove(absentStaffListArray);

        progressDialog = new ProgressDialog(getParentFragment().getContext());
        progressDialog.setMessage("Please wait, Selected staff attendance is making as a absent");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        Attendance_Staff_Marking removeAttendance = new Attendance_Staff_Marking(getContext());
        removeAttendance.Remove_Staff_Attendance(userId, staffId,schoolId,progressDialog);
    }
}