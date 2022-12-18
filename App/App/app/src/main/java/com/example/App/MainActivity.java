package com.example.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    public EditText edtUsuario, edtPassword, edtHost;
    Button btnLogin;

    public OkHttpClient client= new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUsuario = findViewById(R.id.txtUser);//Username value
        edtPassword = findViewById(R.id.txtPass);//Password value
        edtHost = findViewById(R.id.txtHost); //Host value
        btnLogin = findViewById(R.id.bLogin); //Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarUsuario();
            }
        });
    }


    private void validarUsuario() {
        //Get the text from each variable to validate the user
        String host = edtHost.getText().toString();
        String user = edtUsuario.getText().toString();
        String pass = edtPassword.getText().toString();
        String link = "http://192.168.1.138/App_php/login.php/"+user+"/"+pass;//Create the link to make the request to the server with the obtained data
        Request request = new Request.Builder().url(link).build();//Request to the server

        //Create and enqueue the call for the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                MainActivity.this.runOnUiThread(()->Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string()); //Create a JSONObject with the response given
                    // Validate the response and call the function login
                    if (json.getString("Username").equals("ERROR")) {
                        login(user, link, "",true); //Call the function login with the boolean error set to true
                    } else {

                        login(user, link,json.getString("idEstudiant"), false); //Call the function login with the boolean error set to false and the information
                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        });
    }

    public void login(String user, String link,String id ,@NonNull Boolean error){
        if(error){

            MainActivity.this.runOnUiThread(()->Toast.makeText(MainActivity.this, "Usuario o contrasenya incorrecta", Toast.LENGTH_SHORT).show()); //Show on screen the error in the login
        }
        else {
            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class); //Create an intent related to our PrincipalActivity
            intent.putExtra("user", user); //Add username to the intent
            intent.putExtra("id", id); //Add id to the intent
            intent.putExtra("url", link); //Add the url to the intent
            startActivity(intent); //Start a new activity with the created intent
            MainActivity.this.runOnUiThread(()->Toast.makeText(MainActivity.this, user.toString(), Toast.LENGTH_SHORT).show());
        }
    }


}