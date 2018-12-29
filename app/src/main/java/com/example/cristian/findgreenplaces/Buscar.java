package com.example.cristian.findgreenplaces;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Buscar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        Geocoder geo = new Geocoder(this);
        int maxResultados = 1;
        List<Address> adress = null;
        try {
            adress = geo.getFromLocationName("Santiago", maxResultados);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LatLng latLng = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
    }
}
