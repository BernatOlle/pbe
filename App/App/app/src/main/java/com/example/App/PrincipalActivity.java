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
        setContentView(R.layout.activity_principal); //Show the PrincipalActivity
        this.client= new OkHttpClient(); //Create a new OkHttpClient object
        this.user = getIntent().getStringExtra("user"); //Get the user from the intent from PrincipalActivity
        this.url = "http://192.168.1.138/App_php/filtres.php/";//Url to call to the server

        //Assign each object to a variable through the id
        this.logoutButton = findViewById(R.id.logout);
        this.sendButton = findViewById(R.id.send);
        this.userLabel = findViewById(R.id.userID);
        this.userLabel.setText(this.user);
        this.tableTitle = findViewById(R.id.tableTitle);
        this.query = findViewById(R.id.query);
        this.tableLayout = findViewById(R.id.tableD);

        //If logout button is clicked on, return to MainActivity
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);//Start a new activity with the created intent
            }
        });

        //If send button is clicked on, request data from the server
        this.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataRequest();
                hideKeybaord(view);
            }
        });
    }

    //Request data to the server
    public void dataRequest(){
        String text =  this.query.getText().toString().trim(); //(Introduced text in the query) trim is to get the query without blank spaces
        String link = this.url+ "/" + this.user + "/" + text; //Build the link for the request
        Request request = new Request.Builder().url(link).build(); //Create a new request to the link
        //Create and enqueue a new call with the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONArray json = new JSONArray(Objects.requireNonNull(response.body()).string()); //Create a JSONArray with the response given by the server
                    tableResponse(json); //Create the table with the given data

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // Create the table with the JSONArray data
    public void tableResponse(JSONArray json) throws JSONException{
        ArrayList<String[]> data = new ArrayList<>(); //Create a new String ArrayList to store the data
        ArrayList<String> header = new ArrayList<>(); //Create a new String ArrayList to store the headers
        Iterator<String> iterator = null; // json.getJSONObject(0).keys function returns an Iterator
        try {
            iterator = json.getJSONObject(0).keys(); //Get the keys from the first position of the object
        }catch (JSONException e){
            e.printStackTrace();
        }
        while(iterator.hasNext()){
            header.add(iterator.next()); //Add the next String from iterator to the list of header
        }
        //Loop through the JSON object and for each column get the value associated with each header
        for(int row =0; row<json.length(); row++){
            String[] val  = new String[header.size()]; //Create a new String with as many positions as headers
            for(int col =0; col< header.size(); col++){
                val[col] = json.getJSONObject(row).get(String.valueOf(header.toArray()[col])).toString(); //Add the information associated to each of the headers to the col position in the list val[]

            }
            data.add(row,val); //Add the returned list val[] to the row position of the data list
        }

        runOnUiThread(()-> {
            PrincipalActivity.this.runOnUiThread(()-> Toast.makeText(PrincipalActivity.this, header.toString(), Toast.LENGTH_SHORT).show());

            table = new Table(tableLayout, getApplicationContext(), header, data); //Create new a  table object with the right properties and the header and data obtained

            table.createDataTable(); //Call to the function to create the dataTable in our application

        });



    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}