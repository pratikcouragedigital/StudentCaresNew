package com.studentcares.spps.gps_service;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetAddress_From_LatLon {

    String address;
    private static Context context;

    public GetAddress_From_LatLon(Context allGpsDevices) {
        context = allGpsDevices;
    }

    public String getAddressFromLatLong(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
//                String add = String.valueOf(obj);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getThoroughfare(); //palm beach road
            add = add + "\n" + obj.getSubLocality(); //vashi
            add = add + "," + obj.getLocality(); //mumbai
            add = add + "," + obj.getPostalCode();
            add = add + "\n" + obj.getAdminArea(); //maharashtra
            add = add + "," + obj.getCountryName();  //india

            address = add;

            Log.v("IGA", "Address" + add);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }
}
