package diegocunha.beersfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Timer;


public class MainActivity extends ActionBarActivity {

    private String AppID, ClientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseCrashReporting.enable(this);
        AppID = getString(R.string.AppID);
        ClientID = getString(R.string.ClientID);
        Parse.initialize(this, AppID, ClientID);
        ParseFacebookUtils.initialize(getApplicationContext());
        start();
    }

    public void start()
    {
        if(ParseCrashReporting.isCrashReportingEnabled())
        {
            SharedPreferences sp = getApplicationContext().getSharedPreferences("login_saved", Context.MODE_PRIVATE);
            String user = sp.getString("login", null);
            String pass = sp.getString("password", null);
            if(user != null && pass != null)
            {
                ParseUser.logInInBackground(user, pass, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(parseUser != null)
                        {
                            loadSecond();
                        }
                        else
                        {
                            loadFirst();
                        }

                    }
                });
            }
            else
            {
                Intent intent = new Intent(this, firstActivity.class);
                startActivity(intent);
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(), "errou", Toast.LENGTH_SHORT);
            finish();
        }
    }

    protected void loadSecond()
    {
        Intent intent = new Intent(this, secondActivity.class);
        startActivity(intent);
    }

    protected void loadFirst()
    {
        Intent intent = new Intent(this, firstActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
