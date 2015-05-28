package diegocunha.beersfinder;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import org.w3c.dom.Document;
import java.util.ArrayList;

public class HowtoGoActivity extends Activity implements OnMapReadyCallback {


    myLocation MeuLugar;
    TextView txt;
    private double Latitude, Longitude, barLat, barLng;
    private String strLat, strLong, strBarLat, strBarLng, strNomeBar;
    myIntineraire md;
    LatLng start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        MeuLugar = new myLocation(this);
        md = new myIntineraire();

        setContentView(R.layout.activity_howtogo);

        MapFragment mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        mMap.getMapAsync(this);
    }

    /*public void connect() {
        try {
            if (MeuLugar.canGetLocation()) {
                Latitude = MeuLugar.getLatitude();
                Longitude = MeuLugar.getLongitude();

                strLat = String.valueOf(Latitude);
                strLong = String.valueOf(Longitude);
                strBarLat = String.valueOf(barLat);
                strBarLng = String.valueOf(barLng);

                start = new LatLng(Latitude, Longitude);
                end = new LatLng(barLat, barLng);

                Document doc = md.getDocument(start, end, myIntineraire.MODE_DRIVING);

                ArrayList<LatLng> directPoint = md.getDirection(doc);
                PolylineOptions rectline = new PolylineOptions().width(3).color(Color.GREEN);

                for (int i = 0; i < directPoint.size(); i++) {
                    rectline.add(directPoint.get(i));
                }

                //Polyline polyline = mMap.addPolyline(rectline);

            } else {
                MeuLugar.AbreConfigGPS();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }*/

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        LatLng sydney = new LatLng(-33.867, 151.206);

        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        googleMap.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }
}