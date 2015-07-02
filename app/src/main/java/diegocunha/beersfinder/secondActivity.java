package diegocunha.beersfinder;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
public class secondActivity extends Activity {

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);


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
     * Criação: 18/06/2015                                  **
     * Função: void abre_favs                               **
     * Funcionalidade: Mostra bares marcados como favs      **
     *********************************************************/
    public void abre_favs(View view)
    {
        Intent intent = new Intent(this, FavoriteAcitvity.class);
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
