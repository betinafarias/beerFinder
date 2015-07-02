package diegocunha.beersfinder;

import android.app.Activity;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ComentariosActivity extends Activity
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
    private int iNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        //Bloqueia a pagina caso nao tenha login
        getUser();

        //Inicializa classes necessarias
        mProgressDialog = new ProgressDialog(this);
        Bundle extras = getIntent().getExtras();
        MeuLugar = new myLocation(this);
        list_coments = new ArrayList<ComentariosList>();
        listView = (ListView)findViewById(R.id.comentList);
        adapter = new ComentsAdapter(this, list_coments);

        //Verifica se algum parametro foi passado
        if(extras != null)
        {
            //Inicializa o ProgressDialog
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading. . .");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            //Busca as informacoes pas
            ex_nome = extras.getString("NomeBar");
            ex_rua = extras.getString("RuaBar");

            //Carrega comentarios
            load_coments(ex_nome, ex_rua);

            //Abre conscientização
            OpenConsientizacao();
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
        //Seleciona usuário atual do parse
        ParseUser currentUser = ParseUser.getCurrentUser();

        //Verifica se existe usuario atual
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
        alertB.setMessage("Sem conexão com internet, deseja ativar?");
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
     * Funçao: load_coments                                  ****
     * Funcionalidade: Carrega os comentarios                ****
     * Data Criacao: 11/06/2015                              ****
     ***********************************************************/
    protected void load_coments(String bar, String rua)
    {
        //Verifica se tem conexao com internet
        if(verificaConexao())
        {
            //Verifica se consegue pegar posicao do GPS
            if(MeuLugar.canGetLocation())
            {
                try
                {
                    //Inicializa o Parse
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Comentarios");
                    query.whereEqualTo("NomeBar", bar);
                    query.whereEqualTo("RuaBar", rua);
                    query.orderByAscending("Date");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            //Se nao ha excecao
                            if(e == null)
                            {
                                //Se tem valores na lista
                                if(list.size() > 0)
                                {
                                    //Varre a lista para adicionar os valores
                                    for(int i = 0; i < list.size(); i++)
                                    {
                                        ParseObject parseObject = list.get(i);
                                        strNomeBar = parseObject.getString("NomeBar");
                                        strRuaBar = parseObject.getString("RuaBar");
                                        strUser = parseObject.getString("Username");
                                        strComent = parseObject.getString("Comentario");

                                        //Adiciona os itens na lista
                                        item = new ComentariosList(strNomeBar, strRuaBar, strUser, strComent);
                                        list_coments.add(item);
                                    }
                                    //Notifica o adapter que os dados foram alterados
                                    adapter.notifyDataSetChanged();
                                    mProgressDialog.dismiss();
                                }
                                else
                                {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Não há comentarios neste bar", Toast.LENGTH_SHORT).show();
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

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Fun?ao: OpenConsientizacao                            ****
     * Funcionalidade: Abre Dialog de concientizacao         ****
     * Data Criacao: 16/06/2015                              ****
     ************************************************************/
    protected void OpenConsientizacao()
    {
        //Instancia classe para gerar numero aleatorio
        Random randomGenerator = new Random();

        //Recebe o numero aleatorio
        iNum = randomGenerator.nextInt(3);

        if(iNum == 2)
        {
            alertB = new AlertDialog.Builder(this);
            alertB.setTitle("Aviso");
            alertB.setMessage("Se beber não dirija!");
            alertB.setCancelable(false);
            alertB.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            alertB.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert11 = alertB.create();
            alert11.show();
        }
    }

    public void goComents(View view)
    {
        Intent intent = new Intent(this, AddComentariosActivity.class);
        intent.putExtra("NomeBar", ex_nome);
        intent.putExtra("RuaBar", ex_rua);
        startActivity(intent);
    }
}
