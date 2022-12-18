package com.example.App;

import static com.example.App.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrincipalActivity extends AppCompatActivity {
    public Button logoutButton, sendButton;
    public EditText query;
    public String url;
    public TextView userLabel, tableTitle;
    public OkHttpClient client;
    public Table table;
    private TableLayout tableLayout;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        this.client= new OkHttpClient();
        this.user = getIntent().getStringExtra("user");
        this.url = "http://192.168.1.138/App/filtres.php/";

        this.logoutButton = findViewById(R.id.logout);
        this.sendButton = findViewById(R.id.send);
        this.userLabel = findViewById(R.id.userID);
        this.userLabel.setText(this.user);
        this.tableTitle = findViewById(R.id.tableTitle);
        this.query = findViewById(R.id.query);
        this.tableLayout = findViewById(R.id.tableD);

        this.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        this.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataRequest();
                hideKeybaord(view);
            }
        });
    }

    public void dataRequest(){
        String text =  this.query.getText().toString().trim(); //(Text introduit al query) trim es per agafar el query sense espais
        String link = this.url+ "/" + this.user + "/" + text;
        Request request = new Request.Builder().url(link).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONArray json = new JSONArray(Objects.requireNonNull(response.body()).string());
                    tableResponse(json);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void tableResponse(JSONArray json) throws JSONException{
        ArrayList<String[]> data = new ArrayList<>();
        ArrayList<String> header = new ArrayList<>();
        Iterator<String> iterator = null; //la funcio json.getJSONObject(0).keys retorna un iterator
        try {
            iterator = json.getJSONObject(0).keys();
        }catch (JSONException e){
            e.printStackTrace();
        }
        while(iterator.hasNext()){
            header.add(iterator.next());
        }
        for(int row =0; row<json.length(); row++){
            String[] val  = new String[header.size()];
            for(int col =0; col< header.size(); col++){
                val[col] = json.getJSONObject(row).get(String.valueOf(header.toArray()[col])).toString();

            }
            data.add(row,val);
        }

        runOnUiThread(()-> {
            PrincipalActivity.this.runOnUiThread(()-> Toast.makeText(PrincipalActivity.this, header.toString(), Toast.LENGTH_SHORT).show());

            table = new Table(tableLayout, getApplicationContext(), header, data);

            table.createDataTable();

        });



    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}