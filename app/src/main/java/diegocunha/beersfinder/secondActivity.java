package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.share.widget.LikeView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/********************************************
 * Autores: Diego Cunha Gabriel Cataneo  ****
 * Criação: 28/04/2015                   ****
 * Classe: secondActivity                ****
 * Função: Login no Aplicativo           ****
 ********************************************/
public class secondActivity extends ActionBarActivity {

    //Variáveis globais
    ProgressDialog mProgressDialog;
    private TextView txt;
    private String AppID, ClientID, Bar;
    private Integer count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Função que permite ao usuário dar Like na página ofical
        LikeView likeView = (LikeView)findViewById(R.id.like_view);
        likeView.setObjectIdAndType("https://www.facebook.com/BeersFinder", LikeView.ObjectType.PAGE);

        //Parse Infos
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);

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

        if(currentUser != null)
        {
            Intent intent = new Intent(this, secondActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, firstActivity.class);
            startActivity(intent);
        }
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

    /*****************************************
     Autores: Diego Cunha, Gabriel Cataneo  **
     Função: getUser                        **
     Funcionalidade: Efetua logou           **
     Data Criação: 05/05/2015               **
     ******************************************/
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
