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
        edtUsuario = findViewById(R.id.txtUser);
        edtPassword = findViewById(R.id.txtPass);
        edtHost = findViewById(R.id.txtHost);
        btnLogin = findViewById(R.id.bLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarUsuario();
            }
        });
    }


    private void validarUsuario() {
        String host = edtHost.getText().toString();
        String user = edtUsuario.getText().toString();
        String pass = edtPassword.getText().toString();
        String link = "http://192.168.1.138/App/login.php/"+user+"/"+pass;
        Request request = new Request.Builder().url(link).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                MainActivity.this.runOnUiThread(()->Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (json.getString("Username").equals("ERROR")) {
                        login(user, link, "",true);
                    } else {

                        login(user, link,json.getString("idEstudiant"), false);
                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        });
    }

    public void login(String user, String link,String id ,@NonNull Boolean error){
        if(error){


            MainActivity.this.runOnUiThread(()->Toast.makeText(MainActivity.this, "Usuario o contrasenya incorrecta", Toast.LENGTH_SHORT).show());
        }
        else {
            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("id", id);
            intent.putExtra("url", link);
            startActivity(intent);
            MainActivity.this.runOnUiThread(()->Toast.makeText(MainActivity.this, user.toString(), Toast.LENGTH_SHORT).show());
        }
    }


}