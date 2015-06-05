package diegocunha.beersfinder;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
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

/********************************************
 * Autores: Diego Cunha Gabriel Cataneo  ****
 * Cria??o: 08/05/2015                   ****
 * Classe: HowtoGoActivity               ****
 * Fun??o: Mostra no Maps rota           ****
 ********************************************/
public class HowtoGoActivity extends Activity{

    //Variaveis Globais
    myLocation MeuLugar;
    TextView txt;
    private double Latitude, Longitude, barLat, barLng;
    private String strLat, strLong, strBarLat, strBarLng, strNomeBar;
    myIntineraire md;
    LatLng start, end;
    GoogleMap googleMAp;
    LatLng lBar, lMeuLugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtogo);

        //Inicia variaveis necessarias
        Bundle extras = getIntent().getExtras();
        MeuLugar = new myLocation(this);
        md = new myIntineraire();

        googleMAp = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        googleMAp.setMyLocationEnabled(true);

        //Se ha valores sendo passados
        if(extras != null)
        {
            barLat = extras.getDouble("LatitudeBar");
            barLng = extras.getDouble("LongitudeBar");
            Latitude = MeuLugar.getLatitude();
            Longitude = MeuLugar.getLongitude();
            strNomeBar = extras.getString("NomeBar");
            
            googleMAp.moveCamera(CameraUpdateFactory.newLatLngZoom(lBar, 13));

            googleMAp.addMarker(new MarkerOptions()
                    .title(strNomeBar)
                    .position(lBar));

            googleMAp.addMarker(new MarkerOptions()
            .title("Minha posicao")
            .position(lMeuLugar));



        }
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

                Polyline polyline = googleMAp.addPolyline(rectline);
            } else {
                MeuLugar.AbreConfigGPS();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar_for_location, menu);
        return true;
    }
}