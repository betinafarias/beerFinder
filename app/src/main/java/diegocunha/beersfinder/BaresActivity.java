package diegocunha.beersfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

/************************************************************
 * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
 * Funcao: BaresActivity                                 ****
 * Funcionalidade: Search de bares                       ****
 * Data Criacao: 13/05/2015                              ****
 ***********************************************************/
public class BaresActivity extends Activity{

    //Variaveis Globais
    private ProgressDialog mProgressDialog;
    private ConnectivityManager conectivtyManager;
    private myLocation MeuLugar;
    boolean isOn;
    private String strBarOptions, nomedoBar, nomeCerveja, tipoCerveja;
    private String strNomeBar, strRuaBar, strDist, bar;
    private String nomeBar[];
    private double Lat, Lng, parseLat, parseLng, dist, preco;
    private ListaBares item;
    private List<FavoriteList> fav_list;
    private FavoriteList favoriteList;
    private FavoriteAdapter favoriteAdapter;
    private List<ListaBares> lista2;
    private myAdapter adapter;
    private AlertDialog.Builder alertB;
    private ListView listView;
    private Button btnLoc, btnBeer, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bares);

        //Bloqueia acesso sem login
        getUser();

        //Inicia o ProgressDialog
        mProgressDialog = new ProgressDialog(this);

        //Inicia a classe myLocation;
        MeuLugar = new myLocation(this);

        //Inicializa list
        lista2 = new ArrayList<ListaBares>();
        fav_list = new ArrayList<FavoriteList>();

        //Inicializa os adapters
        favoriteAdapter = new FavoriteAdapter(this, fav_list);
        adapter = new myAdapter(this, lista2);

        //Inicializa views
        listView = (ListView)findViewById(R.id.myList);
        listView.setAdapter(favoriteAdapter);
        btnBeer = (Button)findViewById(R.id.maisb);
        btnLoc = (Button)findViewById(R.id.btnBusca);
        btnSearch = (Button)findViewById(R.id.button4);
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

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funçao: load_bar_proximo                              ****
     * Funcionalidade: Busca bar com cerveja mais barata     ****
     * Data Criacao: 13/05/2015                              ****
     ***********************************************************/
   public void load_bar_proximo(View view)
   {
       //Inicializa o ProgressDialog
       mProgressDialog.setCanceledOnTouchOutside(false);
       mProgressDialog.setCancelable(false);
       mProgressDialog.setTitle("Carregando");
       mProgressDialog.setMessage("Loading . . .");
       mProgressDialog.show();

       //Se as listas estao cheias, elas sao limpadas
       if(lista2.size() > 0)
       {
           lista2.clear();
       }


       if(fav_list.size() > 0)
       {
           fav_list.clear();
       }

       //Se ha conexao com internet
       if(verificaConexao())
       {
           //Se consegue pegar posicao
           if(MeuLugar.canGetLocation())
           {
               //Adiciona latitude e longitude
               Lat = MeuLugar.getLatitude();
               Lng = MeuLugar.getLongitude();

               try
               {
                   //Inicializa o Parse
                   ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
                   query.findInBackground(new FindCallback<ParseObject>()
                   {
                       @Override
                       public void done(List<ParseObject> list, ParseException e)
                       {
                           //Se nao a excecao
                           if(e == null)
                           {
                               //Se a lista nao esta vazia
                               if(list.size() > 0)
                               {
                                   //Varre a lista para adicionar os elementos
                                   for(int i = 0; i < list.size(); i++)
                                   {
                                       //Pega os valores da lista para adicionar na lista
                                       ParseObject parseObject = list.get(i);
                                       strNomeBar = parseObject.getString("NomeBar").replace("_", " ");
                                       strRuaBar = parseObject.getString("RuaBar");
                                       parseLat = parseObject.getDouble("Latitude");
                                       parseLng = parseObject.getDouble("Longitude");
                                       preco = parseObject.getDouble("Preco");

                                       //Calcula distancia
                                       dist = MeuLugar.calculaDistancia(Lat, parseLat, Lng, parseLng);
                                       strDist = String.format("%.2f", dist) + "km";

                                       //Adiciona os valores ao FavoriteList
                                       favoriteList = new FavoriteList(strNomeBar, strRuaBar, strDist, preco, dist, parseLat, parseLng);

                                       //Adiciona valores na lista
                                       fav_list.add(favoriteList);

                                       //Ordena Lista
                                       Collections.sort(fav_list);
                                   }

                                   //Notifica ao adapter que os dados foram modificados
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
                   Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
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
     * Funçao: bar                                           ****
     * Funcionalidade: Busca bares mais perto                ****
     * Data Criacao: 13/05/2015                              ****
     ***********************************************************/
    public void bar(View view)
    {
        //Inicializa o ProgressDialog
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Carregando");
        mProgressDialog.setMessage("Loading . . .");
        mProgressDialog.show();

        //Limpa listas caso estejam populadas
        if(lista2.size() > 0)
        {
            lista2.clear();
        }

        if(fav_list.size() > 0)
        {
            fav_list.clear();
        }

        if(verificaConexao())
        {
           if(MeuLugar.canGetLocation())
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
                                       strNomeBar = parseObject.getString("NomeBar").replace("_", " ");
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
                   mProgressDialog.dismiss();
                   ex.printStackTrace();
                   Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
               }
               finally
               {
                   listView.setAdapter(adapter);
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
     * Funçao: OpenSelections                                ****
     * Funcionalidade: busca bar por opcao                   ****
     * Data Criacao: 19/06/2015                              ****
     ***********************************************************/
    public void OpenSelections(View view)
    {
        //Verifica se tem algo salvo nas listas
        if(lista2.size() > 0)
        {
            lista2.clear();
        }

        if(fav_list.size() > 0)
        {
            fav_list.clear();
        }

        //Adiciona os valores ao Alert Dialog
        nomeBar = new String[]{"Boteco do Joaquim", "Dublin", "Marques Bier", "Mulligan", "Natalicio", "Soccer Point", "Thomas", "Tirol"};
        final AlertDialog.Builder options = new AlertDialog.Builder(this);
        options.setTitle("Selecione o bar");

        options.setItems(nomeBar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Retorna valor do item selecionado
                strBarOptions = nomeBar[which].replace(" ", "_");

                //Inicializa o ProgressDialog
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setTitle("Carregando");
                mProgressDialog.setMessage("Loading . . .");
                mProgressDialog.show();

                //Se tem conexao com internet
                if (verificaConexao())
                {
                    //Se consegue pegar posicao do GPS
                    if (MeuLugar.canGetLocation())
                    {
                        //Adiciona valores de latitude e longitude
                        Lat = MeuLugar.getLatitude();
                        Lng = MeuLugar.getLongitude();

                        try
                        {
                            //Busca valores no parse
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
                            query.whereEqualTo("NomeBar", strBarOptions);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    //Se nao houve execao
                                    if (e == null)
                                    {
                                        //Se a lista possui valores
                                        if (list.size() > 0)
                                        {
                                            //Varre a lista atras dos valores
                                            for (int i = 0; i < list.size(); i++)
                                            {
                                                //Pega item a item da lista
                                                ParseObject parseObject = list.get(i);
                                                strNomeBar = parseObject.getString("NomeBar").replace("_", " ");
                                                strRuaBar = parseObject.getString("RuaBar");
                                                parseLat = parseObject.getDouble("Latitude");
                                                parseLng = parseObject.getDouble("Longitude");

                                                //Calcula distancia
                                                dist = MeuLugar.calculaDistancia(Lat, parseLat, Lng, parseLng);
                                                strDist = String.format("%.2f", dist) + "km";

                                                //Adiciona valores ao ListaBares
                                                item = new ListaBares(strNomeBar, strRuaBar, strDist, dist, parseLat, parseLng);

                                                //Adiciona os itens a lista
                                                lista2.add(item);

                                                //Ordena a lista
                                                Collections.sort(lista2);
                                            }

                                            //Notifica o adapter que os valores foram modificados
                                            adapter.notifyDataSetChanged();

                                            //Encerra o ProgressDialog
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
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        } finally
                        {
                            listView.setAdapter(adapter);
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
        });

        AlertDialog alert11 = options.create();
        alert11.show();
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
}
