package diegocunha.beersfinder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
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
    private AlertDialog.Builder alertB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUser();

        //Inicializia variaveis
        listaBares = new ArrayList<ListaBares>();
        adapterList = new myAdapter(this, listaBares);
        listinha = new ListaBares();
        MeuLugar = new myLocation(this);
        mProgressDialog = new ProgressDialog(this);

        setContentView(R.layout.activity_barlocation);
        getListView();
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getUser                                       ****
     * Funcionalidade: Bloqueia pagina sem login             ****
     * Data Criacao: 05/05/2015                              ****
     ***********************************************************/
    protected void getUser()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser == null)
        {
            Intent intent = new Intent(this, firstActivity.class);
            startActivity(intent);

        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: verificaConexao                               ****
     * Funcionalidade: Verifica status internet              ****
     * Data Criacao: 28/04/2015                              ****
     ***********************************************************/
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

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: getListView                                   ****
     * Funcionalidade: Verifica status internet              ****
     * Data Criacao: 28/04/2015                              ****
     ***********************************************************/
    protected void getListView()
    {
        try
        {
            //Inicializa o ProgressDialog
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading. . .");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            //Verifica GPS
            if(MeuLugar.canGetLocation())
            {
                //Verifica Conexao com internet
                if(verificaConexao())
                {
                    Latitude = MeuLugar.getLatitude();
                    Longitude = MeuLugar.getLongitude();

                    //Busca do Parse o resultado da pesquisa
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            //Se nao ha excecao
                            if(e == null)
                            {
                                //Verifica se a lista esta preenchida
                                if(list.size() > 0)
                                {
                                    //Adiciona valores do Parse as variaveis
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

                                    //Para o uso do GPS
                                    MeuLugar.stopUsingGPS();
                                    //Avisa adapter que dados foram atualizados
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
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    mProgressDialog.dismiss();
                    OpenNet();
                }
            }
            else
            {
                mProgressDialog.dismiss();
                OpenGPS();
            }
        }
        catch (Exception ex)
        {
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

        finally
        {
            listView = (ListView)findViewById(R.id.barLocationListView);
            listView.setAdapter(adapterList);
        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: OpenGPS                                       ****
     * Funcionalidade: Abre Config de GPS                    ****
     * Data Criacao: 11/06/2015                              ****
     ***********************************************************/
    protected void OpenGPS()
    {
        final Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        final Intent intent2 = new Intent(this, secondActivity.class);

        alertB = new AlertDialog.Builder(this);
        alertB.setTitle("Aviso");
        alertB.setMessage("GPS desativado, deseja ativar?");
        alertB.setCancelable(false);
        alertB.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(intent);
            }
        });
        alertB.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(intent2);
            }
        });

        AlertDialog alert11 = alertB.create();
        alert11.show();
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: OpenNet                                       ****
     * Funcionalidade: Abre Config de internet               ****
     * Data Criacao: 11/06/2015                              ****
     ***********************************************************/
    protected void OpenNet()
    {
        final Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        final Intent intent2 = new Intent(this, secondActivity.class);

        alertB = new AlertDialog.Builder(this);
        alertB.setTitle("Aviso");
        alertB.setMessage("Sem conex�o com internet, deseja ativar?");
        alertB.setCancelable(false);
        alertB.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(intent);
            }
        });
        alertB.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(intent2);
            }
        });

        AlertDialog alert11 = alertB.create();
        alert11.show();
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
