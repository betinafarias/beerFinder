package diegocunha.beersfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/************************************************************
 * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
 * Classe: FavoriteAcitvity                              ****
 * Funcionalidade: Mostra bares favoritos                ****
 * Data Criacao: 22/06/2015                              ****
 ***********************************************************/
public class FavoriteAcitvity extends Activity {

    //Variaveis Globais
    myLocation MeuLocal;
    boolean isOn;
    ConnectivityManager conectivtyManager;
    FavoriteList favList2;
    List<ListaBares> listaBares;
    protected String AppID, ClientID;
    private SQLiteDatabase myDataBase, myControlDB;
    private double mLat, mLng;
    myAdapter adapter;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder alertB;
    ListView listView;
    String strDist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_favorite);

        //Bloqueia acesso sem login
        getUser();

        //Instancia classes e funcionalidades necessarias
        MeuLocal = new myLocation(this);
        listaBares = new ArrayList<ListaBares>();
        adapter = new myAdapter(this, listaBares);
        MeuLocal = new myLocation(this);
        mProgressDialog = new ProgressDialog(this);

        //Carrega favoritos
        load_favorites();
    }

    /*********************************************************
     * Autores: Diego Cunha, Gabriel Cataneo Betina Farias  **
     * Função: getUser                                      **
     * Funcionalidade: Verifica usuário                     **
     * Data Criação: 05/05/2015                             **
     *********************************************************/
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
     * Classe: load_favorites                                ****
     * Funcionalidade: Mostra bares favoritos                ****
     * Data Criacao: 22/06/2015                              ****
     ***********************************************************/
    protected void load_favorites()
    {
        //Inicializa o ProgressDialog
        mProgressDialog.setTitle("Carregando");
        mProgressDialog.setMessage("Loading. . .");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        try
        {
            //Abre Banco de dados e cria se necessario
            myDataBase = this.openOrCreateDatabase("Banco", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            myDataBase.execSQL("CREATE TABLE IF NOT EXISTS Favorites (NomeBar VARCHAR(255), RuaBar VARCHAR(255), Latitude VARCHAR(255), Longitiude VARCHAR(255));");

            Cursor controler = myDataBase.rawQuery("SELECT COUNT(*) FROM Favorites", null);
            controler.moveToFirst();

            //Se Consegue buscar posicao
            if(MeuLocal.canGetLocation())
            {
                mLat = MeuLocal.getLatitude();
                mLng = MeuLocal.getLongitude();

                //Se a valores no banco
                if(controler.getInt(0) > 0)
                {
                    //Busca todos os valores do banco
                    Cursor controler2 = myDataBase.query(false, "Favorites", null, null, null, null, null, null, null);

                    //Se buscaa tem valores
                    if(controler2 != null)
                    {
                        //Enquanto haver membros na lista
                        while(controler2.moveToNext())
                        {
                            //Adiciona as variaveis o resultado do banco
                            String Nomebar = controler2.getString(1);
                            String Ruabar = controler2.getString(2);
                            double Lat = Double.valueOf(controler2.getString(3));
                            double Lng = Double.valueOf(controler2.getString(4));

                            //Calcula distancia
                            double dDist = MeuLocal.calculaDistancia(mLat, Lat, mLng, Lng);

                            strDist = String.format("%.2f", dDist) + "km";

                            //Adiciona resultado a lista e ordena
                            ListaBares item = new ListaBares(Nomebar, Ruabar, strDist, dDist, Lat, Lng);
                            listaBares.add(item);
                            Collections.sort(listaBares);
                        }

                        //Fecha banco de dados
                        myDataBase.close();

                        //Para uso do GPS
                        MeuLocal.stopUsingGPS();

                        mProgressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Sem favoritos adicionados", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, secondActivity.class);
                    startActivity(intent);
                }
            }
        }
        catch (Exception ex)
        {
            myDataBase.close();
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally
        {
            listView = (ListView)findViewById(R.id.favListView);
            listView.setAdapter(adapter);
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
}
