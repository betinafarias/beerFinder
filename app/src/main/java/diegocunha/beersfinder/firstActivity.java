package diegocunha.beersfinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.w3c.dom.Text;
import java.security.MessageDigest;
import java.util.List;

    /*******************************************
    * Autores: Diego Cunha Gabriel Cataneo  ****
    * Criação: 28/04/2015                 ****
    * Classe: firstActivity                 ****
    * Função: Login no Aplicativo         ****
    ********************************************/
public class firstActivity extends ActionBarActivity
{
	//Variáveis Globais
    private CallbackManager callbackManager;
    private EditText login, senha;
    private String strLogin, strSenha, strUser, strPass, AppID, ClientID, strMD5Pass;
    private LocationManager locationManager;
    private boolean conectado, load = false;
    private ConnectivityManager conectivtyManager;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Inicializa o sdk do facebook para aparecer o botão de login NÃO MEXER
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_first);

        //Pega infos dos EditText
        login = (EditText)findViewById(R.id.edtLogin);
        senha = (EditText)findViewById(R.id.edtSenha);

        //Parse Infos
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);
        ParseFacebookUtils.initialize(getApplicationContext());

    }


    /*******************************************
     * Autores: Diego Cunha Gabriel Cataneo ****
     * Criação: 28/04/2015                ****
     * Função: void second                ****
     * Funcionalidade: Abre secondLayout    ****
     *******************************************/
    public void second()
    {
        Intent intet = new Intent(this, secondActivity.class);
        startActivity(intet);
    }

    /**********************************************
     * Autores: Diego Cunha Gabriel Cataneo    ****
     * Criação: 28/04/2015                   ****
     * Função: boolean VerificaConexao       ****
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

    /**********************************************
     * Autores: Diego Cunha Gabriel Cataneo    ****
     * Criação: 04/05/2015                   ****
     * Função: String GeraHash               ****
     * Funcionalidade: Retorna senha cript     ****
     **********************************************/
    protected String GeraHash(String temp)
    {
        String resultado;
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            byte[] buffer = temp.getBytes();
            md.update(buffer);
            byte[] digest = md.digest();

            String hStr = "";

            for (int i = 0; i < digest.length; i++)
            {
                hStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
            }

            resultado = hStr;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            resultado = "";
        }

        return resultado;
    }

    /**********************************************
     * Autores: Diego Cunha Gabriel Cataneo    ****
     * Criação: 28/04/2015                   ****
     * Função: void Sigin                    ****
     * Funcionalidade: Abre Layout de cadastro ****
     **********************************************/
    public void Sigin(View view)
    {
        Intent intet = new Intent(this, SignUpActivity.class);
        startActivity(intet);
    }

    /**********************************************
     * Autores: Diego Cunha Gabriel Cataneo    ****
     * Criação: 28/04/2015                   ****
     * Função: void Login                    ****
     * Funcionalidade: Realiza o login         ****
     **********************************************/
    public void Login(View view)
    {

        try
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setTitle("Verificando");
            progressDialog.setMessage("Carregando. . . ");
            progressDialog.show();

            if(verificaConexao())
            {
                strLogin = login.getText().toString();
                strSenha = senha.getText().toString();
                strMD5Pass = GeraHash(strSenha);

                ParseUser.logInInBackground(strLogin, strMD5Pass, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(parseUser != null)
                        {
                            progressDialog.dismiss();
                            second();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Sem internet", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex)
        {
            progressDialog.dismiss();
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erro: " + ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}