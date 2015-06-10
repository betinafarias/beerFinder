package diegocunha.beersfinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BaresOnMapActivity extends Activity {

    //Variaveis Globais
    private String AppID, ClientID, resultado, strHour, strAbertura, strFechamento;
    private GoogleMap googleMAp;
    private LatLng lBar, lMeuLugar;
    private double Latitude, Longitude;
    myLocation MeuLugar;
    private ProgressDialog mProgressDialog;
    private Marker barMarker, myMarker;
    private Calendar cal1, cal2, cal3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baresonmap);

        //Direciona ID do Parse
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);

        //Instancia maps a variavel
        googleMAp = ((MapFragment) getFragmentManager().findFragmentById(R.id.map2))
                .getMap();
        googleMAp.setMyLocationEnabled(true);

        //Inicializa informacoes
        Parse.initialize(this, AppID, ClientID);
        mProgressDialog = new ProgressDialog(this);
        MeuLugar = new myLocation(this);
        loadbares();

    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Função: loadbares                                     ****
     * Funcionalidade: Mostra bares no maps                  ****
     * Criação: 04/06/2015                                   ****
     ************************************************************/
    protected void loadbares()
    {
        //Incializa o ProgressDialog
        mProgressDialog.setTitle("Carregando");
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        //Adiciona valores de latitude e longitude
        Latitude = MeuLugar.getLatitude();
        Longitude = MeuLugar.getLongitude();
        lMeuLugar = new LatLng(Latitude, Longitude);

        //DateTime
        cal1 = Calendar.getInstance(); // Abertura
        cal2 = Calendar.getInstance(); // Fechamento
        cal3 = Calendar.getInstance(); // Atual;

        //Hora Atual;
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        strHour = sdf.format(now);


        //Move camera para posicionamento do usuario
        googleMAp.moveCamera(CameraUpdateFactory.newLatLngZoom(lMeuLugar, 13));

        //Adiciona marker para posicao do usuario
        myMarker = googleMAp.addMarker(new MarkerOptions()
                .title("Eu estou aqui")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position))
                .position(lMeuLugar));

        try
        {
            //Carrega informações do Parse
            ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e)
                {
                    //Se nao ha excecao
                    if (e == null)
                    {
                        //Verifica se a lista esta preenchida
                        if (list.size() > 0)
                        {
                            //Adiciona valores do Parse as variaveis
                            for (int i = 0; i < list.size(); i++)
                            {
                                ParseObject pObject = list.get(i);
                                String strNomeBar = pObject.getString("NomeBar");
                                String strRuaBar = pObject.getString("RuaBar");

                                double parseLat = pObject.getDouble("Latitude");
                                double parseLng = pObject.getDouble("Longitude");

                                strAbertura = pObject.getString("Abertura");
                                strFechamento = pObject.getString("Fechamento");

                                //Abertura
                                String[] parts = strAbertura.split(":");
                                cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                                cal1.set(Calendar.SECOND, Integer.parseInt(parts[2]));

                                //Fechamento
                                parts = strFechamento.split(":");
                                cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                                cal2.set(Calendar.SECOND, Integer.parseInt(parts[2]));
                                cal2.add(Calendar.DATE, 1);

                                //Hora atual
                                parts = strHour.split(":");
                                cal3.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                                cal3.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                                cal3.set(Calendar.SECOND, Integer.parseInt(parts[2]));

                                if(cal3.after(cal1) && cal3.before(cal2))
                                {
                                    resultado = "Aberto";
                                }
                                else
                                {
                                    resultado = "Fechado";
                                }

                                lBar = new LatLng(parseLat, parseLng);
                                barMarker = googleMAp.addMarker(new MarkerOptions()
                                        .title(strNomeBar)
                                        .snippet(resultado)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bar_ico))
                                        .position(lBar));
                            }

                            mProgressDialog.dismiss();
                        }
                        else
                        {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Lista vazia", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bares_on_map, menu);
        return true;
    }
}
