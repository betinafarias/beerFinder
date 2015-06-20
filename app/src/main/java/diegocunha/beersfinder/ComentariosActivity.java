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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ComentariosActivity extends ActionBarActivity
{
    //Variaveis Globais
    private ConnectivityManager conectivtyManager;
    private boolean isOn;
    private AlertDialog.Builder alertB;
    private ProgressDialog mProgressDialog;
    private String ex_nome, ex_rua;
    myLocation MeuLugar;
    private String strNomeBar, strRuaBar, strComent, strUser;
    private ComentariosList item;
    List<ComentariosList> list_coments;
    private ListView listView;
    private ComentsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        //Bloqueia a pagina caso nao tenha login
        getUser();

        mProgressDialog = new ProgressDialog(this);
        Bundle extras = getIntent().getExtras();
        MeuLugar = new myLocation(this);
        list_coments = new ArrayList<ComentariosList>();
        listView = (ListView)findViewById(R.id.comentList);
        adapter = new ComentsAdapter(this, list_coments);

        if(extras != null)
        {
            //Inicializa o ProgressDialog
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading. . .");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            ex_nome = extras.getString("NomeBar");
            ex_rua = extras.getString("RuaBar");

            load_coments(ex_nome, ex_rua);
        }
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

    protected void load_coments(String bar, String rua)
    {
        if(verificaConexao())
        {
            if(MeuLugar.canGetLocation())
            {
                try
                {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Comentarios");
                    query.whereEqualTo("NomeBar", bar);
                    query.whereEqualTo("RuaBar", rua);
                    query.orderByAscending("Date");
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
                                        strUser = parseObject.getString("Username");
                                        strComent = parseObject.getString("Comentario");

                                        item = new ComentariosList(strNomeBar, strRuaBar, strUser, strComent);
                                        list_coments.add(item);
                                    }
                                    adapter.notifyDataSetChanged();
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
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comentarios, menu);
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
