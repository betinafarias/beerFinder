package diegocunha.beersfinder;

import android.app.Activity;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class BaresOnMapActivity extends Activity {

    //Variaveis Globais
    private String AppID, ClientID, valorBar, nomeCeva, resultado;
    private GoogleMap googleMAp;
    private LatLng lBar, lMeuLugar;
    private double Latitude, Longitude;
    myLocation MeuLugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baresonmap);

        //Direciona ID do Parse
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        googleMAp = ((MapFragment) getFragmentManager().findFragmentById(R.id.map2))
                .getMap();
        googleMAp.setMyLocationEnabled(true);
        MeuLugar = new myLocation(this);

        //Inicializa informacoes
        Parse.initialize(this, AppID, ClientID);
        loadbares();

    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Criação: 04/06/2015                                   ****
     * Função: loadbares                                     ****
     * Funcionalidade: Mostra bares no maps                  ****
     ************************************************************/
    protected void loadbares()
    {
        //Adiciona valores de latitude e longitude
        Latitude = MeuLugar.getLatitude();
        Longitude = MeuLugar.getLongitude();
        lMeuLugar = new LatLng(Latitude, Longitude);

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
                                double parseLat = pObject.getDouble("Latitude");
                                double parseLng = pObject.getDouble("Longitude");
                                String strRuaBar = pObject.getString("RuaBar");

                                //Adiciona no maps o resultado
                                googleMAp.moveCamera(CameraUpdateFactory.newLatLngZoom(lMeuLugar, 13));
                                lBar = new LatLng(parseLat, parseLng);
                                googleMAp.addMarker(new MarkerOptions()
                                        .title(strNomeBar)
                                        .snippet(strRuaBar)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bar_ico))
                                        .position(lBar));
                            }

                            googleMAp.addMarker(new MarkerOptions()
                                    .title("Eu estou aqui")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position))
                                    .position(lMeuLugar));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Lista vazia", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /*protected String load_cerveja(String txNomeBar)
    {
        try
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(txNomeBar);
            query.setLimit(1);
            query.orderByAscending("Preco");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if(e == null)
                    {
                        if(list.size() > 0)
                        {
                            for(int i = 0; i <list.size(); i++)
                            {
                                ParseObject pObject =list.get(i);
                                nomeCeva = pObject.getString("NomeCerveja");
                                valorBar = "R$ " +String.valueOf(pObject.getDouble("Preco")).replace(".", ",");
                                resultado = nomeCeva + " " + valorBar;
                            }
                        }
                        else
                        {
                            nomeCeva = "null";
                            valorBar = "R$ 0,00";
                        }
                    }
                    else
                    {
                        nomeCeva = "null";
                        e.printStackTrace();
                        valorBar = "R$ 0,00";
                    }
                }
            });
        }
        catch (Exception ex)
        {
            valorBar = "R$ 0,00";
            ex.printStackTrace();
        }

        return resultado;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bares_on_map, menu);
        return true;
    }
}
