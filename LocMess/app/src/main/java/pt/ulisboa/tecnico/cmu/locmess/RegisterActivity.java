package pt.ulisboa.tecnico.cmu.locmess;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etPasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
        final Button bSignup = (Button) findViewById(R.id.bSignup);
    }


    private boolean isUsernameValid(String username) {
        String pattern= "^[a-zA-Z0-9]*$";
        return username.matches(pattern);
    }

    public void register(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Register successful!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
        AlertDialog alert = builder.create();
        alert.show();
    }





}
