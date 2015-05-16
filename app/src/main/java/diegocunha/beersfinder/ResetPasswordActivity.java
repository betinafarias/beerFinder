package diegocunha.beersfinder;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.text.ParseException;


public class ResetPasswordActivity extends ActionBarActivity {

    EditText mail, confirm;
    String strMail, strConfirm, AppID, ClientID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        //Inicializa o Parse
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);
        ParseFacebookUtils.initialize(getApplicationContext());
        
        mail = (EditText)findViewById(R.id.mail);
        confirm = (EditText)findViewById(R.id.ConfirmeEmail);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reset_password, menu);
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
