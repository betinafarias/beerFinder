package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.security.spec.ECField;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class selectBaresA extends ActionBarActivity {

    //Variaveis Globais
    ListView listView;
    String bar, ceva;
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

        //Bloqueia pagina de usuario sem acesso
        getUser();

        //Inicializa variaeis
        listaBares = new ArrayList<ListaBares>();
        adapterList = new myAdapter(this, listaBares);
        listinha = new ListaBares();
        MeuLugar = new myLocation(this);
        mProgressDialog = new ProgressDialog(this);
        Bundle extras = getIntent().getExtras();

        //Recebe valores do BaresActivity
        if (extras != null)
        {
            bar = extras.getString("strBar");
            ceva = extras.getString("strCeva");
        }

        setContentView(R.layout.activity_selectbares);
        getListView(bar);
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

    /*****************************************
     Autores: Diego Cunha, Gabriel Cataneo  **
     Função: getListView(String Bar)        **
     Funcionalidade: Retorna busca          **
     Data Criação: 05/05/2015               **
     ******************************************/
    protected void getListView(String Bar)
    {
        try
        {
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading. . .");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();
            if(verificaConexao())
            {
                if(MeuLugar.canGetLocation())
                {
                    Latitude = MeuLugar.getLatitude();
                    Longitude = MeuLugar.getLongitude();

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("BaresLocal");
                    query.whereEqualTo("NomeBar", Bar);

                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if(e == null)
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
                                adapterList.notifyDataSetChanged();
                                mProgressDialog.dismiss();
                            }
                            else
                            {
                                mProgressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else
                {
                    mProgressDialog.dismiss();
                    MeuLugar.AbreConfigGPS();
                }
            }
            else
            {
                mProgressDialog.dismiss();
               MeuLugar.AbreConfigNET();
            }
        }
        catch (Exception ex)
        {
            mProgressDialog.dismiss();
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        finally
        {
            listView = (ListView)findViewById(R.id.listView);
            listView.setAdapter(adapterList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_bares, menu);
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
