package diegocunha.beersfinder;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
* Criação: 28/04/2015                   ****
* Classe: firstActivity                 ****
* Função: Login no Aplicativo           ****
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
    private ProgressDialog mProgressDialog;


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

    /*************************************************
     * Autores: Diego Cunha Gabriel Cataneo       ****
     * Criação: 28/04/2015                        ****
     * Função: void resetPass                     ****
     * Funcionalidade: Abre ResetPasswordActivity ****
     *************************************************/
    public void resetPass(View view)
    {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
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
            //Inicializa ProgressDialog
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.setTitle("Verificando");
            mProgressDialog.setMessage("Carregando. . . ");
            mProgressDialog.show();

            //Verifica conexao com internet
            if(verificaConexao())
            {
                //Pega texto dos TextView
                strLogin = login.getText().toString();
                strSenha = senha.getText().toString();

                //Realiza o Login com o PArse
                ParseUser.logInInBackground(strLogin, strSenha, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        //Se usuario existe
                        if(parseUser != null)
                        {
                            //Salva login no Android
                            SharedPreferences sp=getSharedPreferences("Login", 0);
                            SharedPreferences.Editor Ed=sp.edit();
                            Ed.putString("Unm",strLogin );
                            Ed.putString("Psw",strSenha);
                            Ed.commit();

                            mProgressDialog.dismiss();
                            second();
                        }
                        else
                        {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                mProgressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Sem internet", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex)
        {
            mProgressDialog.dismiss();
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

    //Bloqueia acesso a SplashScreen
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