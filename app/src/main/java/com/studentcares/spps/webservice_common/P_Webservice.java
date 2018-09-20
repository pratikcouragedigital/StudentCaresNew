package com.studentcares.spps.webservice_common;

import com.studentcares.spps.URL_WebService;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class P_Webservice {

    static URL_WebService uRL_WebService;
    private static String URL = uRL_WebService.getUrl();
    private static String NAMESPACE = "http://tempuri.org/";
    private static String SOAP_ACTION = "http://tempuri.org/";

    public static String ShowHomeworkStudentWise(String userId,String schoolId, int currentPage,String std,String div,String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("student_id");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("current_Page");
        celsiusPI.setValue(currentPage);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("Standard_Id");
        celsiusPI.setValue(std);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("Division_Id");
        celsiusPI.setValue(div);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }

        return resTxt;

    }

    public static String ShowHomeworkForParents(String userId,String schoolId, String homeworkSubjectId,String homeworkCreatedDate,int currentPage,String std,String div, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("student_id");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("Subject_Id");
        celsiusPI.setValue(homeworkSubjectId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("submission_Date");
        celsiusPI.setValue(homeworkCreatedDate);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("current_Page");
        celsiusPI.setValue(currentPage);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);
        celsiusPI=new PropertyInfo();
        celsiusPI.setName("Standard_Id");
        celsiusPI.setValue(std);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("Division_Id");
        celsiusPI.setValue(div);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }

        return resTxt;
    }


    public static String AllSubject(String userId, String webMethName) {

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("student_id");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }

        return resTxt;
    }


    public static String showOnrTimeFeesDetails(String schoolId, String userId, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("studentId");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
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
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }

        return resTxt;
    }

    public static String ShowUnpaidFeesMonthList(String schoolId, String userId, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("studentId");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
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
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }

        return resTxt;
    }

    public static String showUnpaidMothlyFeeDetail(String schoolId, String userId, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("studentId");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
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
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }

        return resTxt;
    }

    public static String showPaidFeesDetails(String schoolId, String userId, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("studentId");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
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
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }

        return resTxt;
    }

    public static String showPaidSubFeesDetails(String schoolId, String userId, int receiptNo, String feetype, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("studentId");
        celsiusPI.setValue(userId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("feetype");
        celsiusPI.setValue(feetype);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("ReceiptNo");
        celsiusPI.setValue(receiptNo);
        celsiusPI.setType(int.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }

        return resTxt;
    }

    public static String Showtimetable(String std, String div, String day, String schoolId, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("standard");
        celsiusPI.setValue(std);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI = new PropertyInfo();
        celsiusPI.setName("division");
        celsiusPI.setValue(div);
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
	
	public static String showExamTimeTable(String standard, String division, String schoolId, String webMethName) {
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        PropertyInfo celsiusPI = new PropertyInfo();
        celsiusPI.setName("standard");
        celsiusPI.setValue(standard);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("division");
        celsiusPI.setValue(division);
        celsiusPI.setType(String.class);
        request.addProperty(celsiusPI);

        celsiusPI=new PropertyInfo();
        celsiusPI.setName("School_id");
        celsiusPI.setValue(schoolId);
        celsiusPI.setType(int.class);
        request.addProperty(celsiusPI);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "No Network Found";
        }

        return resTxt;
    }
}
