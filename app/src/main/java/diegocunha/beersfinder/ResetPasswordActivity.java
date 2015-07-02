package diegocunha.beersfinder;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.text.ParseException;

/**********************************************************
 * Autores: Diego Cunha Gabriel Cataneo Betina Farias  ****
 * Criação: 28/04/2015                  			   ****
 * Classe: ResetPasswordActivity                       ****
 * Função: Recupera senha                              ****
 *********************************************************/
public class ResetPasswordActivity extends Activity {

    //Variaveis Globais
    EditText mail, confirm;
    String strMail, strConfirm, AppID, ClientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        //Inicializa os view
        mail = (EditText)findViewById(R.id.mail);
        confirm = (EditText)findViewById(R.id.ConfirmeEmail);
    }

    /***********************************************************
     * Autores: Diego Cunha Gabriel Cataneo Betina Farias   ****
     * Criação: 28/04/2015                  			    ****
     * Classe: resetMail                                    ****
     * Função: Verifica informaceos e manda para o Parse    ****
     ***********************************************************/
    public void resetMail(View view)
    {
        try
        {
            strMail = mail.getText().toString();
            strConfirm = confirm.getText().toString();

            if(strMail.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Campo e-mail em branco", Toast.LENGTH_SHORT).show();
            }
            else if(strConfirm.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Campo de confirmação em branco", Toast.LENGTH_SHORT).show();
            }
            else if(strMail.equals(strConfirm))
            {
               ParseUser.requestPasswordResetInBackground(strMail, new RequestPasswordResetCallback() {
                   @Override
                   public void done(com.parse.ParseException e) {
                       if(e == null)
                       {
                           Toast.makeText(getApplicationContext(), "Enviado requisição para e-mail de cadastro", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
