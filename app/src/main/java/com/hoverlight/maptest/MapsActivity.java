package com.hoverlight.maptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button go, search;
    private EditText lat, lng, zoom, cari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        go = findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLokasi();
                hideKeyboard(v);
            }
        });
        search = findViewById(R.id.searchBtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCari();
                hideKeyboard(v);
            }
        });
        lat = findViewById(R.id.lat);
        lng = findViewById(R.id.lng);
        zoom = findViewById(R.id.zoom);
        cari = findViewById(R.id.search);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ITS = new LatLng(-7.28, 112.79);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS, 15));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.none:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoPeta(Double lat, Double lng, float zoom) {
        LatLng loc = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(loc).title("Marker in "+lat+":"+lng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom));
    }

    private void gotoLokasi() {
        Double lat = Double.parseDouble(this.lat.getText().toString()),
                lng = Double.parseDouble(this.lng.getText().toString());
        Float zoom = Float.parseFloat(this.zoom.getText().toString());
        Toast.makeText(this, "Move to "+lat+":"+lng, Toast.LENGTH_SHORT).show();
        gotoPeta(lat, lng, zoom);
    }

    private void goCari() {
        Geocoder g = new Geocoder(getBaseContext());
        try {
            List<Address> daftar = g.getFromLocationName(cari.getText().toString(), 1);
            if (daftar.isEmpty())
                Toast.makeText(this, "Alamat tidak ditemukan :(", Toast.LENGTH_SHORT).show();
            else {
                Address alamat = daftar.get(0);
                Toast.makeText(this, "Alamat "+alamat.getAddressLine(0)+" ketemu!", Toast.LENGTH_SHORT).show();
                gotoPeta(alamat.getLatitude(), alamat.getLongitude(), 15);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hideKeyboard(View v) {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
