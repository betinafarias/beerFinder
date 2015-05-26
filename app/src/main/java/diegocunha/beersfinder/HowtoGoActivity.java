package diegocunha.beersfinder;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HowtoGoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    myLocation MeuLugar;
    private double Latitude, Longitude, barLat, barLng;
    private String strLat, strLong, strBarLat, strBarLng;
    myIntineraire md;
    LatLng start, end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        MeuLugar = new myLocation(this);
        md = new myIntineraire();

        setContentView(R.layout.activity_howtogo);
        setUpMapIfNeeded();

        if(extras != null)
        {
            barLat = extras.getDouble("Latitude");
            barLng = extras.getDouble("Longitude");
            connect();
        }
    }

    public void connect()
    {
        try
        {
            if(MeuLugar.canGetLocation())
            {
                Latitude = MeuLugar.getLatitude();
                Longitude = MeuLugar.getLongitude();

                strLat = String.valueOf(Latitude);
                strLong = String.valueOf(Longitude);
                strBarLat = String.valueOf(barLat);
                strBarLng = String.valueOf(barLng);

                start = new LatLng(Latitude, Longitude);
                end  = new LatLng(barLat, barLng);

                Document doc = md.getDocument(start, end, myIntineraire.MODE_DRIVING);

                ArrayList<LatLng> directPoint = md.getDirection(doc);
                PolylineOptions rectline = new PolylineOptions().width(3).color(Color.GREEN);

                for(int i = 0; i <directPoint.size();i++)
                {
                    rectline.add(directPoint.get(i));
                }

                Polyline polyline = mMap.addPolyline(rectline);

            }
            else
            {
                MeuLugar.AbreConfigGPS();
            }
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
            //connect();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        double sLat = MeuLugar.getLatitude();
        double sLng = MeuLugar.getLongitude();
        mMap.addMarker(new MarkerOptions().position(new LatLng(sLat, sLng)).title("Eu aqui"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        connect();
    }
}
