package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BarForLocationActivity extends ActionBarActivity {

    //Variaveis Globais
    ListView listView;
    private String AppID, ClientID;
    ProgressDialog mProgressDialog;
    List<ListaBares> listaBares = null;
    myAdapter adapterList;
    ListaBares listinha;
    myLocation MeuLugar;
    double Latitude, Longitude;
    ConnectivityManager conectivtyManager;
    boolean conectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Inicializa o Parse
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);
        getUser();

        listaBares = new ArrayList<ListaBares>();
        adapterList = new myAdapter(this, listaBares);
        listinha = new ListaBares();
        MeuLugar = new myLocation(this);
        mProgressDialog = new ProgressDialog(this);

        setContentView(R.layout.activity_barlocation);
        getListView();
    }

    /*****************************************
     Autores: Diego Cunha, Gabriel Cataneo  **
     Fun��o: getUser                        **
     Funcionalidade: Verifica usu�rio       **
     Data Cria��o: 05/05/2015               **
     ******************************************/
    protected void getUser()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser == null)
        {
            Intent intent = new Intent(this, firstActivity.class);
            startActivity(intent);

        }
    }

    /**********************************************
     * Autores: Diego Cunha Gabriel Cataneo    ****
     * Criação: 28/04/2015                     ****
     * Função: boolean VerificaConexao         ****
     * Funcionalidade: Retorna status conexao  ****
     **********************************************/
    public  boolean verificaConexao()
    {
        conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected())
        {
            conectado = true;
        }
        else
        {
            conectado = false;
        }

        return conectado;
    }

    protected void getListView()
    {
        try
        {
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading. . .");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();

            if(MeuLugar.canGetLocation())
            {
                if(verificaConexao())
                {
                    Latitude = MeuLugar.getLatitude();
                    Longitude = MeuLugar.getLongitude();

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if(e == null)
                            {
                                if(list.size() > 0)
                                {
                                    for(int i = 0; i <list.size();i++)
                                    {
                                        ParseObject pObject = list.get(i);
                                        String strNomeBar = pObject.getString("NomeBar");
                                        String strRuaBar = pObject.getString("RuaBar");
                                        double parseLat = pObject.getDouble("Latitude");
                                        double parseLng = pObject.getDouble("Longitude");

                                        double dist = MeuLugar.calculaDistancia(Latitude, parseLat, Longitude, parseLng);
                                        String strDist = String.format("%.2f", dist) + "km";

                                        ListaBares item = new ListaBares(strNomeBar, strRuaBar, strDist, dist, parseLat, parseLng);

                                        listaBares.add(i, item);
                                        Collections.sort(listaBares);
                                    }

                                    MeuLugar.stopUsingGPS();
                                    adapterList.notifyDataSetChanged();
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
                else
                {
                    MeuLugar.AbreConfigNET();
                }
            }
            else
            {
                MeuLugar.AbreConfigGPS();
            }
        }
        catch (Exception ex)
        {
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

        finally
        {
            listView = (ListView)findViewById(R.id.barLocationListView);
            listView.setAdapter(adapterList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar_for_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
