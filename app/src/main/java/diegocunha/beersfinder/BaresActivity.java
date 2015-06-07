package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseUser;

import java.lang.reflect.Array;

/********************************************
 * Autores: Diego Cunha Gabriel Cataneo  ****
 * Cria��o: 08/05/2015                   ****
 * Classe: BaresActivity                 ****
 * Fun��o: Filtro para bares             ****
 ********************************************/
public class BaresActivity extends ActionBarActivity{

    //Variaveis Globais
    Spinner mySpinner, spinerCerveja, spinnerTipoCeva;
    ProgressDialog mProgressDialog;
    ConnectivityManager conectivtyManager;
    boolean isOn;
    private String AppID, ClientID, nomedoBar, nomeCerveja, tipoCerveja;
    private String nomeBar[], nomeCeva[], tamanhoCeva[];
    int barPosition, cevaPosition, tipoCevaPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bares);

        //Inicializa o Parse
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);
        getUser();

        //Preenche os Spinners
        mySpinner = (Spinner)findViewById(R.id.meuSpinner);
        spinerCerveja = (Spinner)findViewById(R.id.spinnerBebidas);
        spinnerTipoCeva = (Spinner)findViewById(R.id.spinnerTipoCeva);
        loadBares();
    }

    /*****************************************
     Autores: Diego Cunha, Gabriel Cataneo  **
     Função: getUser                        **
     Funcionalidade: Verifica usuário       **
     Data Criação: 05/05/2015               **
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
   public void loadBares()
   {
       try
       {
            //Adiciona os valores aos Spinners
           this.nomeBar = new String[]{"Escolha uma opcao", "Dublin", "Mulligan", "Natalicio", "Soccer Point", "Thomas Pub", "Tirol"};
           this.nomeCeva = new String[]{"Escolha uma opcao", "Budweiser", "Heineken", "Stella", "Polar", "Skol"};
           this.tamanhoCeva = new String[]{"Escolha uma opcao", "500ml","600ml", "Long Neck", "Artesanal", "Chopp"};

           //Cria ArrayAdapter para os Spinner
           ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomeBar);
           ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomeCeva);
           ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tamanhoCeva);

           //Carrega Spinner
           mySpinner.setAdapter(adapter);
           spinerCerveja.setAdapter(adapter2);
           spinnerTipoCeva.setAdapter(adapter3);
       }
       catch (Exception ex)
       {
           ex.printStackTrace();
           Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
        //Inicia o ProgressDialog
        mProgressDialog = new ProgressDialog(this);

        try
        {
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setTitle("Carregando");
            mProgressDialog.setMessage("Loading . . .");

            nomedoBar = mySpinner.getSelectedItem().toString();
            nomeCerveja = spinerCerveja.getSelectedItem().toString();
            tipoCerveja = spinnerTipoCeva.getSelectedItem().toString();

            barPosition = mySpinner.getSelectedItemPosition();
            cevaPosition = spinerCerveja.getSelectedItemPosition();
            tipoCevaPosition = spinnerTipoCeva.getSelectedItemPosition();

            //Manda informações para SelectedBaresActivity
            if(barPosition == 0 && cevaPosition == 0 && tipoCevaPosition == 0)
            {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Escolha um parametro válido", Toast.LENGTH_SHORT).show();
            }
            else if(tipoCevaPosition == 0 && barPosition == 0 && cevaPosition != 0)
            {
                Intent intent = new Intent(getApplicationContext(), selectBaresA.class);
                intent.putExtra("strCeva", nomeCerveja);
                startActivity(intent);
                mProgressDialog.dismiss();
            }
            else if(barPosition != 0 && cevaPosition == 0 && tipoCevaPosition == 0)
            {
                Intent intent = new Intent(getApplicationContext(), selectBaresA.class);
                intent.putExtra("strBar", nomedoBar);
                startActivity(intent);
                mProgressDialog.dismiss();
            }
            else
            {
                mProgressDialog.dismiss();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
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
