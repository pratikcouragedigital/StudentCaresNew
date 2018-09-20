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

import com.studentcares.spps.adapter.Attendance_Staff_Absent_List_Adapter;
import com.studentcares.spps.commonClasses.RemoveLastComma;
import com.studentcares.spps.connectivity.Attendance_Staff_Marking;
import com.studentcares.spps.connectivity.Attendance_Get_Absent_Present_List_Staff;
import com.studentcares.spps.model.Attendance_List_Items;
import com.studentcares.spps.sessionManager.SessionManager;
import com.studentcares.spps.singleton.Chk_Mark_Attendance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Attendance_Tab_Staff_Absent_List extends Fragment implements View.OnClickListener{

    ProgressDialog progressDialog;
    List<Attendance_List_Items> uaTeacherAttendanseItems = new ArrayList<Attendance_List_Items>();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.attendance_tab_staff_absent, container, false);
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

        adapter = new Attendance_Staff_Absent_List_Adapter(uaTeacherAttendanseItems, attendanceStaffCheckFab);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(this.getString(R.string.progress_msg));
        progressDialog.show();

        getList();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //int a = adapter.getCount();
                //int b = a;
                //Toast.makeText(v.getContext(), " "+b, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getList(){
        try {
            Attendance_Get_Absent_Present_List_Staff showAttendanseDetails = new Attendance_Get_Absent_Present_List_Staff(getActivity());
            showAttendanseDetails.show_Staff_Absent_List(uaTeacherAttendanseItems, recyclerView, adapter,userId,schoolId, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
		if(v.getId() == R.id.attendanceStaffCheckFab) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to mark as a present?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addAttendance();
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
	
	public void addAttendance() {
        absentStaffListArray = chk_mark_attendance.getAbsentStaffListInstance();

        RemoveLastComma removeLastComma = new RemoveLastComma(getActivity());
        staffId = removeLastComma.CommaRemove(absentStaffListArray);

        progressDialog = new ProgressDialog(getParentFragment().getContext());
        progressDialog.setMessage("Please wait, Selected staff attendance is making as a present");
        progressDialog.show();

        Attendance_Staff_Marking addAttendance = new Attendance_Staff_Marking(v.getContext());
        addAttendance.AddTodaysAttendance(userId, staffId,schoolId,progressDialog);
    }
}
