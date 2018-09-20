package com.studentcares.spps.webservice_common;

import com.studentcares.spps.URL_WebService;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class T_Webservice {

    static URL_WebService uRL_WebService;
    private static String URL = uRL_WebService.getUrl();
    private static String NAMESPACE = "http://tempuri.org/";
    private static String SOAP_ACTION = "http://tempuri.org/";

    public static String AddHomework(String homework, String submissionDate, String staffId, String schoolId, String standard, String division, String subject, String titleOfHomework, String firstImage, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("Homework_Description");
        celsiusPI.setValue(homework);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Submission_Date");
        celsiusPI.setValue(submissionDate);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Staff_Id");
        celsiusPI.setValue(staffId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Standard_Id");
        celsiusPI.setValue(standard);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Division_Id");
        celsiusPI.setValue(division);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Subject_Id");
        celsiusPI.setValue(subject);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Homework_Title");
        celsiusPI.setValue(titleOfHomework);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Image");
        celsiusPI.setValue(firstImage);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }
    public static String ShowDefaultHomeworkListForTeacher(String School_id, String staff_Id, int current_Page, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("staff_Id");
        celsiusPI.setValue(staff_Id);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(School_id);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("current_Page");
        celsiusPI.setValue(current_Page);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String ShowHomeworkListFilterWiseForTeacher(String standard, String division, String subject, String schoolId, String date, int currentPage, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("Standard_Id");
        celsiusPI.setValue(standard);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Division_Id");
        celsiusPI.setValue(division);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Subject_Id");
        celsiusPI.setValue(subject);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("submission_Date");
        celsiusPI.setValue(date);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        celsiusPI = new PropertyInfo();
        celsiusPI.setName("current_Page");
        celsiusPI.setValue(currentPage);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String Absent_Student_List(String standard, String division, String schoolId, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("Standard_Id");
        celsiusPI.setValue(standard);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Division_Id");
        celsiusPI.setValue(division);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String Present_Student_List(String standard, String division, String userId, String schoolId, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("Standard_Id");
        celsiusPI.setValue(standard);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Division_Id");
        celsiusPI.setValue(division);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("staff_Id");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String AddAttendance(String userId, String presentStudentIds, String absentStudentIds, String schoolId,String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("Staff_Id");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Present_Id");
        celsiusPI.setValue(presentStudentIds);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Absent_Id");
        celsiusPI.setValue(absentStudentIds);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String AddAttendance_MarkAll_Present(String userId, String presentStudentIds, String schoolId,String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("Staff_Id");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Present_Id");
        celsiusPI.setValue(presentStudentIds);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String AddAttendance_MarkAll_Absent(String userId, String absentStudentIds, String schoolId,String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("Staff_Id");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Absent_Id");
        celsiusPI.setValue(absentStudentIds);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }
    public static String RemoveAttendance(String userId, String studentId, String schoolId, String webMethName) {

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("Staff_Id");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Student_Id");
        celsiusPI.setValue(studentId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String RemoveStaffAttendance(String userId, String studentId, String schoolId, String webMethName) {

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("userId");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("Staff_Ids");
        celsiusPI.setValue(studentId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String AssignStandardDivision(String standardId, String divisionId, String staffId, String schoolId, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("standard_Id");
        celsiusPI.setValue(standardId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("division_Id");
        celsiusPI.setValue(divisionId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("staff_Id");
        celsiusPI.setValue(staffId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("school_Id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String Showtimetable(String staffId, String day, String schoolId, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("staff_id");
        celsiusPI.setValue(staffId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("day");
        celsiusPI.setValue(day);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String T_Fees_NotPaid_StudentList(String schoolId, String standard, String division, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("StandardId");
        celsiusPI.setValue(standard);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("DivisionId");
        celsiusPI.setValue(division);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String Fees_Send_MSG(String schoolId, String mobileNo_sms, String ids_notification, String msgBody, String userId,String type, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("toMobileNos");
        celsiusPI.setValue(mobileNo_sms);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("toIds");
        celsiusPI.setValue(ids_notification);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("msgBody");
        celsiusPI.setValue(msgBody);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("addedBy");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("toType");
        celsiusPI.setValue(type);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }

    public static String T_Fees_Balance_StudentList(String schoolId, String standard, String division, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("StandardId");
        celsiusPI.setValue(standard);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("DivisionId");
        celsiusPI.setValue(division);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }
        return resTxt;
    }
}
