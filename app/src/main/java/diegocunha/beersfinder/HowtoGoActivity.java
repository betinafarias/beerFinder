package diegocunha.beersfinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
public class HowtoGoActivity extends FragmentActivity{

    //Variaveis Globais
    myLocation MeuLugar;
    private double Latitude, Longitude, barLat, barLng;
    private String strNomeBar, strRuaBar;
    myIntineraire md;
    LatLng start, end;
    GoogleMap googleMAp;
    LatLng lBar, lMeuLugar;
    myIntineraire DrawRoute;
    ArrayList<LatLng> markerPoints;
    MarkerOptions marker;
    Document document;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtogo);

        //Inicia variaveis necessarias
        Bundle extras = getIntent().getExtras();
        MeuLugar = new myLocation(this);
        md = new myIntineraire();
        markerPoints = new ArrayList<LatLng>();
        DrawRoute = new myIntineraire();
        marker = new MarkerOptions();
        GetRouteTask getRoute = new GetRouteTask();
        mProgressDialog = new ProgressDialog(this);

        googleMAp = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        googleMAp.setMyLocationEnabled(false);

        //Se ha valores sendo passados
        if(extras != null)
        {
            barLat = extras.getDouble("LatitudeBar");
            barLng = extras.getDouble("LongitudeBar");

            Latitude = MeuLugar.getLatitude();
            Longitude = MeuLugar.getLongitude();

            strNomeBar = extras.getString("NomeBar");
            strRuaBar = extras.getString("RuaBar");

            lBar = new LatLng(barLat, barLng);
            lMeuLugar = new LatLng(Latitude, Longitude);

            //Inicializa o ProgressDialog
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading. . .");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();

            //Inicia o draw da rota
            getRoute.execute();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar_for_location, menu);
        return true;
    }

    private class GetRouteTask extends AsyncTask<String, Void, String> {

        String response = "";
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {
            //Pega todos os valores da rota
            document = DrawRoute.getDocument(lBar, lMeuLugar, DrawRoute.MODE_DRIVING);
            response = "Success";
            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            googleMAp.clear();
            if(response.equalsIgnoreCase("Success")){
                ArrayList<LatLng> directionPoint = DrawRoute.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(5).color(
                        Color.rgb(102,102,255));

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                //Adiciona a rota no mapa
                googleMAp.addPolyline(rectLine);

                //Adiciona os marcadores no mapa
                googleMAp.moveCamera(CameraUpdateFactory.newLatLngZoom(lBar, 13));
                googleMAp.addMarker(new MarkerOptions()
                        .title(strNomeBar)
                        .snippet(strRuaBar)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bar_ico))
                        .position(lBar));

                googleMAp.addMarker(new MarkerOptions()
                        .title("Minha posicao")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position))
                        .position(lMeuLugar));

                mProgressDialog.dismiss();
            }

        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

