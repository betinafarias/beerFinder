package diegocunha.beersfinder;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HowtoGoActivity extends FragmentActivity {

    private GoogleMap mMap;
    myLocation MeuLugar;
    private double Latitude, Longitude, barLat, barLng;
    private String strLat, strLong, strBarLat, strBarLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        MeuLugar = new myLocation(this);
        mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        if(extras != null)
        {
            barLat = extras.getDouble("Latitude");
            barLng = extras.getDouble("Longitude");
            connect();
        }

        setContentView(R.layout.activity_howtogo);
        setUpMapIfNeeded();

    }

    public void connect()
    {
        try
        {
            Latitude = MeuLugar.getLatitude();
            Longitude = MeuLugar.getLongitude();

            strLat = String.valueOf(Latitude);
            strLong = String.valueOf(Longitude);
            strBarLat = String.valueOf(barLat);
            strBarLng = String.valueOf(barLng);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
