package pt.ulisboa.tecnico.cmu.locmess;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etPasswordConfirmation;
    ProgressDialog dialog;
    private Boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirmation = (EditText) findViewById(R.id.etPasswordConfirmation);
    }


    private boolean isUsernameValid(String username) {
        String pattern= "^[a-zA-Z0-9]+$";
        return username.matches(pattern);
    }

    private boolean isPasswordTheSame(String password, String confirmation){
        return password.equals(confirmation);
    }

    public void register(View view){

        if (!isUsernameValid(etUsername.getText().toString())){
            dialogAlert("Invalid Username. Username can only have letters or numbers.");
            return;
        }

        if (etPassword.getText().toString().length()<3){
            dialogAlert("Passwords needs to be at least 3 characters long.");
            return;
        }

        if (!isPasswordTheSame(etPassword.getText().toString(), etPasswordConfirmation.getText().toString())){
            dialogAlert("Passwords don't match.");
            return;
        }




        loadingDialog("Please wait...");
        registerOnServer();


    }

    private void registerOnServer(){

        // TODO: API Requests
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    success = true;
                    loadingDialog(false);
                    if(success) dialogAlert("Register successful!");
                    else dialogAlert("Error registering!");
                }
            },
         1000);
    }

    private void dialogAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Register");
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if(success) finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void loadingDialog(String message){
        dialog = ProgressDialog.show(this, "", message, true);
    }
    private void loadingDialog(Boolean state){
        if(!state) dialog.dismiss();
    }





}
