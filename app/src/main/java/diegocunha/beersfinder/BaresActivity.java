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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.internal.CollectionMapper;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaresActivity extends ActionBarActivity{

    //Variaveis Globais
    Spinner mySpinner, spinerCerveja, spinnerTipoCeva;
    ProgressDialog mProgressDialog;
    ConnectivityManager conectivtyManager;
    myLocation MeuLugar;
    boolean isOn;
    private String AppID, ClientID, nomedoBar, nomeCerveja, tipoCerveja;
    String strNomeBar, strRuaBar, strDist, bar;
    private String nomeBar[];
    private double Lat, Lng, parseLat, parseLng, dist, preco;
    ListaBares item;
    List<FavoriteList> fav_list;
    FavoriteList favoriteList;
    FavoriteAdapter favoriteAdapter;
    List<ListaBares> lista2;
    myAdapter adapter;
    AlertDialog.Builder alertB;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bares);

        //Inicializa o Parse
        getUser();

        //Inicia o ProgressDialog
        mProgressDialog = new ProgressDialog(this);

        //Inicia a classe myLocation;
        MeuLugar = new myLocation(this);

        //Inicializa list
        lista2 = new ArrayList<ListaBares>();
        fav_list = new ArrayList<FavoriteList>();
        favoriteAdapter = new FavoriteAdapter(this, fav_list);
        adapter = new myAdapter(this, lista2);
        listView = (ListView)findViewById(R.id.myList);
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
            isOn = true;
        }
        else
        {
            isOn = false;
        }

        return isOn;
    }

    /**********************************************
     * Autores: Diego Cunha Gabriel Cataneo    ****
     * Criação: 13/05/2015                     ****
     * Função: void loadBares                  ****
     * Funcionalidade: Preenche os Spinners    ****
     **********************************************/
   public void load_bar_proximo(View view)
   {
       mProgressDialog.setCanceledOnTouchOutside(false);
       mProgressDialog.setCancelable(false);
       mProgressDialog.setTitle("Carregando");
       mProgressDialog.setMessage("Loading . . .");
       mProgressDialog.show();

       if(lista2.size() > 0)
       {
           lista2.clear();
       }
       else if(fav_list.size() > 0)
       {
           fav_list.clear();
       }

       if(verificaConexao())
       {
           if(MeuLugar.canGetLocation())
           {
               Lat = MeuLugar.getLatitude();
               Lng = MeuLugar.getLongitude();
               try
               {
                   ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
                   query.findInBackground(new FindCallback<ParseObject>() {
                       @Override
                       public void done(List<ParseObject> list, ParseException e) {
                           if(e == null)
                           {
                               if(list.size() > 0)
                               {
                                   for(int i = 0; i < list.size(); i++)
                                   {
                                       ParseObject parseObject = list.get(i);

                                       strNomeBar = parseObject.getString("NomeBar");
                                       strRuaBar = parseObject.getString("RuaBar");
                                       parseLat = parseObject.getDouble("Latitude");
                                       parseLng = parseObject.getDouble("Longitude");
                                       preco = parseObject.getDouble("Preco");

                                       dist = MeuLugar.calculaDistancia(Lat, parseLat, Lng, parseLng);
                                       strDist = String.format("%.2f", dist) + "km";

                                       favoriteList = new FavoriteList(strNomeBar, strRuaBar, strDist, preco, dist, parseLat, parseLng);

                                       fav_list.add(favoriteList);
                                       Collections.sort(fav_list);
                                   }

                                   favoriteAdapter.notifyDataSetChanged();
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
               catch (Exception ex)
               {
                   mProgressDialog.dismiss();
                   ex.printStackTrace();
                   Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
               }
               finally
               {
                   listView.setAdapter(favoriteAdapter);
               }
           }
           else
           {
               mProgressDialog.dismiss();
               OpenGPS();
           }
       }
       else
       {
           mProgressDialog.dismiss();
           OpenNet();
       }
   }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: sendBares                                     ****
     * Funcionalidade: Manda parametros para busca           ****
     * Data Criacao: 13/05/2015                              ****
     ***********************************************************/
    public void bar(View view)
    {
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Carregando");
        mProgressDialog.setMessage("Loading . . .");

        if(lista2.size() > 0)
        {
            lista2.clear();
        }
        else if(fav_list.size() > 0)
        {
            fav_list.clear();
        }

        if(verificaConexao())
        {
            try
            {
                Lat = MeuLugar.getLatitude();
                Lng = MeuLugar.getLongitude();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
                query.findInBackground(new FindCallback<ParseObject>()
                {
                    @Override
                    public void done(List<ParseObject> list, ParseException e)
                    {
                        if(e == null)
                        {
                            if(list.size() > 0)
                            {
                                for(int i = 0; i < list.size(); i++)
                                {
                                    ParseObject parseObject = list.get(i);
                                    strNomeBar = parseObject.getString("NomeBar");
                                    strRuaBar = parseObject.getString("RuaBar");
                                    parseLat = parseObject.getDouble("Latitude");
                                    parseLng = parseObject.getDouble("Longitude");

                                    dist = MeuLugar.calculaDistancia(Lat, parseLat, Lng, parseLng);
                                    strDist = String.format("%.2f", dist) + "km";
                                    item = new ListaBares(strNomeBar, strRuaBar, strDist, dist, parseLat, parseLng);

                                    lista2.add(item);
                                    Collections.sort(lista2);
                                }

                                adapter.notifyDataSetChanged();
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally
            {
                listView.setAdapter(adapter);
            }
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
        alertB.setMessage("Sem conexao com internet, deseja ativar?");
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
        getMenuInflater().inflate(R.menu.menu_bares, menu);
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
