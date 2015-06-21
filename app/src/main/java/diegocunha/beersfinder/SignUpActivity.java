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
 * Cria��o: 28/04/2015                   ****
 * Classe: SignUpActivity                ****
 * Fun��o: Cria cadastro usu�rio         ****
 ********************************************/
public class SignUpActivity extends ActionBarActivity {

    //Vari�veis Globais
    private EditText edLogin, edPass, edMail;
    private String login, pass, mail, AppID, ClientID;
    private boolean conectado;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        //Direciona os EditText
        edLogin = (EditText)findViewById(R.id.edtUser);
        edPass  = (EditText)findViewById(R.id.edtPass);
        edMail  = (EditText)findViewById(R.id.edtMail);
    }

    /**********************************************
     * Autores: Diego Cunha Gabriel Cataneo    ****
     * Cria��o: 28/04/2015                     ****
     * Fun��o: boolean VerificaConexao         ****
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
     * Cria��o: 28/04/2015                     ****
     * Fun��o: void sig                        ****
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


                if(login == null || login.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Username invalido", Toast.LENGTH_SHORT).show();
                }
                else if(login.length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "Usuario deve possuir ao menos 6 caracteres", Toast.LENGTH_SHORT).show();
                }
                else if(pass == null || pass.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Senha invalida", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length() < 6 || pass.length() > 8)
                {
                    Toast.makeText(getApplicationContext(), "A senha deve ser maior do que 6 caracteres e menor do que 8", Toast.LENGTH_SHORT).show();
                }
                else if(mail == null || mail.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "E-mail invalido", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ParseUser user = new ParseUser();
                    user.setUsername(login);
                    user.setPassword(pass);
                    user.setEmail(mail);
                    user.signUpInBackground();

                    Toast.makeText(getApplicationContext(), "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
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
