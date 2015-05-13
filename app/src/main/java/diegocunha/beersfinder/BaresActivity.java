package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class BaresActivity extends ActionBarActivity{

    Spinner mySpinner;
    ProgressDialog mProgressDialog;
    ConnectivityManager conectivtyManager;
    boolean isOn;
    String AppID, ClientID;
    private String nomeBar[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bares);

        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);

        setContentView(R.layout.activity_bares);
        mySpinner = (Spinner)findViewById(R.id.meuSpinner);
        loadBares();
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
     * Criação: 28/04/2015                     ****
     * Função: void loadBares                  ****
     * Funcionalidade: Preenche o Spinner      ****
     **********************************************/
   public void loadBares()
   {
       try
       {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading . . .");

            this.nomeBar = new String[]{"Dublin", "Mulligan", "Natalicio", "Soccer Point", "Thomas"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomeBar);

            mySpinner.setAdapter(adapter);
       }
       catch (Exception ex)
       {
           ex.printStackTrace();
           Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
       }
       finally
       {
            mProgressDialog.dismiss();
       }
   }

    /******************************************************
     * Autores: Diego Cunha Gabriel Cataneo            ****
     * Criação: 13/05/2015                             ****
     * Função: void sendBares                          ****
     * Funcionalidade: Manda o parametro de busca      ****
     ******************************************************/
    public void sendBares(View view)
    {

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
