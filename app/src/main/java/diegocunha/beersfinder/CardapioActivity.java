package diegocunha.beersfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CardapioActivity extends Activity
{
    private String strNomeBar, strRuaBar, strNomeCeva, strPrecoCeva, strTipoCeva;
    private ConnectivityManager conectivtyManager;
    private boolean conectado;
    private double dPrecoCeva;
    private ProgressDialog mProgressDialog;
    private CardapioList cardapio;
    private List<CardapioList>lista;
    private AlertDialog.Builder alertB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cardapio);

        Bundle extras = getIntent().getExtras();
        mProgressDialog = new ProgressDialog(this);
        lista = new ArrayList<>();

        if(extras != null)
        {
            strNomeBar = extras.getString("NomeBar");
            load_menu(strNomeBar);
        }
    }

    /************************************************************
     * Autores: Diego Cunha Gabriel Cataneo  Betina Farias   ****
     * Funï¿½ao: verificaConexao                               ****
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

    protected void load_menu(String nomedobar)
    {
        if(verificaConexao())
        {
            try
            {
                mProgressDialog.setTitle("Carregando");
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(nomedobar);
                query.findInBackground(new FindCallback<ParseObject>() {
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
                                    strNomeCeva = parseObject.getString("NomeCerveja");
                                    strTipoCeva = parseObject.getString("TipoCerveja");
                                    dPrecoCeva = parseObject.getDouble("Preco");
                                    strPrecoCeva = "R$ " + String.format("%.2f", dPrecoCeva);

                                    cardapio = new CardapioList(strNomeCeva, strPrecoCeva, strTipoCeva, dPrecoCeva);
                                    lista.add(cardapio);

                                    Collections.sort(lista);
                                }

                                mProgressDialog.dismiss();
                            }
                            else
                            {
                                mProgressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Bar sem cardápio cadastrado", Toast.LENGTH_SHORT).show();
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
            catch(Exception ex)
            {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            mProgressDialog.dismiss();
            OpenNet();
        }
    }

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
