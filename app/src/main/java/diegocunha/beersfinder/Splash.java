package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseUser;


public class Splash extends ActionBarActivity {

    //Variáveis Globais
    private String AppID, ClientID;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Parse Infos
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);

        getUser();
    }

    protected void getUser()
    {
        try
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setTitle("Verificando");
            progressDialog.setMessage("Carregando. . . ");
            progressDialog.show();

            ParseUser currentUser = ParseUser.getCurrentUser();

            if(currentUser != null)
            {
                progressDialog.dismiss();
                Intent intent = new Intent(this, secondActivity.class);
                startActivity(intent);
            }
            else
            {
                progressDialog.dismiss();
                Intent intent = new Intent(this, firstActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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
