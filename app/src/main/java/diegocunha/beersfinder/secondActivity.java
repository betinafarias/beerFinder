package diegocunha.beersfinder;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

/*********************************************************
 * Autores: Diego Cunha Gabriel Cataneo Betina Farias ****
 * Criação: 28/04/2015                                ****
 * Classe: secondActivity                             ****
 * Função: Login no Aplicativo                        ****
 *********************************************************/
public class secondActivity extends ActionBarActivity {

    //Variáveis globais
    protected String AppID, ClientID;
    private Integer count = 0;
    myLocation MeuLocal;
    boolean isOn;
    ConnectivityManager conectivtyManager;
    List<ListaBares> listaBares = null;
    ListaBares listinha;
    myAdapter adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Parse Infos
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);

        //Inicializa variaveis necessarias
        listaBares = new ArrayList<ListaBares>();
        adapterList = new myAdapter(this, listaBares);
        listinha = new ListaBares();
        MeuLocal = new myLocation(this);

        //Bloqueia pagina quando nao tem login
        getUser();
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

    /*********************************************************
     * Autores: Diego Cunha, Gabriel Cataneo Betina Farias  **
     * Função: getUser                                      **
     * Funcionalidade: Abre filtro de busca de bares        **
     * Data Criação: 05/05/2015                             **
     *********************************************************/
    public void AbreLista(View view)
    {
        Intent intent = new Intent(this, BaresActivity.class);
        startActivity(intent);
    }

    /*********************************************************
     * Autores: Diego Cunha, Gabriel Cataneo Betina Farias  **
     * Função: getUser                                      **
     * Funcionalidade: Mostra bares no Mapa                 **
     * Data Criação: 05/05/2015                             **
     *********************************************************/
    public void AbreMaps(View view)
    {
        Intent intent = new Intent(this, BaresOnMapActivity.class);
        startActivity(intent);
    }

    /*********************************************************
     * Autores: Diego Cunha, Gabriel Cataneo Betina Farias  **
     * Criação: 08/05/2015                                  **
     * Função: void BarProximo                              **
     * Funcionalidade: Busca bares mais pertos              **
     *********************************************************/
    public void BarProximo(View view)
    {
        Intent intent = new Intent(this, BarForLocationActivity.class);
        startActivity(intent);
    }

    /*********************************************************
     * Autores: Diego Cunha, Gabriel Cataneo Betina Farias  **
     * Criação: 06/06/2015                                  **
     * Função: void BarProximo                              **
     * Funcionalidade: Abre pagina para curtir              **
     *********************************************************/
    public void OpenFace(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1562830260649134"));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
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

    /*********************************************************
     * Autores: Diego Cunha, Gabriel Cataneo Betina Farias  **
     * Função: getUser                                      **
     * Funcionalidade: Efetua logout                        **
     * Data Criação: 05/05/2015                             **
     *********************************************************/
    @Override
    public void onBackPressed()
    {
        count++;

        if(count < 2)
        {
            Toast.makeText(getApplication(), "Aperte novamente para sair", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            Intent intent = new Intent(this, firstActivity.class);
            startActivity(intent);
        }
    }
}
