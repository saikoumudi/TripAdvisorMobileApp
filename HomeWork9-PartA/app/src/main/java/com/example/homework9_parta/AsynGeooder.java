package com.example.homework9_parta;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;


public class AsynGeooder extends AsyncTask<String , Void, Address > {

    private Context mContext;

    GetAddress getAddress;


    interface GetAddress
    {
       public void Get(Address address);
    }


    public AsynGeooder(Context sContext)
    {
        getAddress = (GetAddress)sContext;
        this.mContext = sContext;

    }



    @Override
    protected void onPostExecute(Address address) {
        super.onPostExecute(address);
        getAddress.Get(address);
    }

    @Override
    protected Address doInBackground(String... params) {

        Address address = null;
        Geocoder geocoder = new Geocoder(mContext);

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(params[0],1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses != null)
        {
            address = addresses.get(0);
        }

        return address;
    }
}
