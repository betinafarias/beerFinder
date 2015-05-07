package diegocunha.beersfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;
import java.security.MessageDigest;

/********************************************
 * Autores: Diego Cunha Gabriel Cataneo  ****
 * Criação: 28/04/2015                   ****
 * Classe: SignUpActivity                ****
 * Função: Cria cadastro usuário         ****
 ********************************************/
public class SignUpActivity extends ActionBarActivity {

    //Variáveis Globais
    private EditText edLogin, edPass, edMail;
    private String login, pass, mail, AppID, ClientID, strPassMD5;
    private boolean conectado;
    private ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Direciona os EditText
        edLogin = (EditText)findViewById(R.id.edtUser);
        edPass  = (EditText)findViewById(R.id.edtPass);
        edMail  = (EditText)findViewById(R.id.edtMail);

        //Parse Infos
        ParseCrashReporting.enable(this);
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);
    }

    /**********************************************
     * Autores: Diego Cunha Gabriel Cataneo    ****
     * Criação: 28/04/2015                     ****
     * Função: boolean VerificaConexao         ****
     * Funcionalidade: Retorna status conexao  ****
     **********************************************/
    public  boolean verificaConexao()
    {
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

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
     * Criação: 04/05/2015                     ****
     * Função: String GeraHash                 ****
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
     * Criação: 28/04/2015                     ****
     * Função: void sig                        ****
     * Funcionalidade: Realiza cadastro Parse  ****
     * OBS: Utilizar no OnClick do bt Login    ****
     **********************************************/
    public void sig(View view)
    {
        try
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setTitle("Conectando");
            progressDialog.setMessage("Loading. . . ");
            progressDialog.show();

            if(verificaConexao())
            {
                login = edLogin.getText().toString();
                pass  = edPass.getText().toString();
                mail  = edMail.getText().toString();
                strPassMD5 = GeraHash(pass);


                if(login == null || login.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Username inválido", Toast.LENGTH_SHORT).show();
                }
                else if(login.length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "Usuário deve possuír ao menos 6 caracteres", Toast.LENGTH_SHORT).show();
                }
                else if(pass == null || pass.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Senha inválida", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length() < 6 || pass.length() > 8)
                {
                    Toast.makeText(getApplicationContext(), "A senha deve ser maior do que 6 caracteres e menor do que 8", Toast.LENGTH_SHORT).show();
                }
                else if(mail == null || mail.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "E-mail inválido", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ParseUser user = new ParseUser();
                    user.setUsername(login);
                    user.setPassword(strPassMD5);
                    user.setEmail(mail);
                    user.signUpInBackground();

                    Toast.makeText(getApplicationContext(), "Cadastrado com sucesso: " + strPassMD5, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Sem internet", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getApplication(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        finally
        {
            edLogin.setText("");
            edPass.setText("");
            edMail.setText("");
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
