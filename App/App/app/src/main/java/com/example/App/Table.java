package com.example.App;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.YELLOW;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Table extends AppCompatActivity {
    private final TableLayout table;
    private final Context context;
    private ArrayList<String> header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView cellTxt;
    private int colorText;


    public Table(TableLayout table, Context context, ArrayList<String> header, ArrayList<String[]> data) {
        this.table=table;
        this.context=context;
        this.header = header;
        this.data =data;

    }

    public void createDataTable() {
        table.removeAllViews(); //Delete the table that was there previously
        this.newRow(); //Creation of a new row for the headers

        //Create a new cell for every column in the header row and set its properties
        for(int col=0; col<this.header.size(); col++) {
            this.newCell();
            this.cellTxt.setText(header.get(col));
            this.cellTxt.setTextSize(17);
            this.cellTxt.setTypeface(null, Typeface.BOLD_ITALIC);
            this.cellTxt.setBackgroundColor(GREEN);
            this.tableRow.addView(this.cellTxt, TableRow_LayoutParams()); //Add the cell in the tableRow
        }
        table.addView(tableRow); //Add the tableRow to the table

        String text;
        //Create the number of rows needed for the data and create a cell for each column in every row
        for(int row=0; row<this.data.size(); row++){
            this.newRow();
            for(int col=0;col<data.get(row).length;col++){
                this.newCell();
                this.cellTxt.setText(data.get(row)[col]); //Add the data from this row and column
                this.cellTxt.setTextSize(17);
                this.tableRow.addView(cellTxt, TableRow_LayoutParams()); //Add the cell in the tableRow

            }

            //Set a color for even rows and a color for odd rows
            if (row % 2 == 0) {
                tableRow.setBackgroundColor(Color.parseColor("#89C9FB"));
            } else {
                tableRow.setBackgroundColor(Color.parseColor("#30A3FF"));
            }

            table.addView(tableRow);
        }

    }

    // Cell creation
    private void newCell() {
        this.cellTxt = new TextView(this.context);
        this.cellTxt.setTextSize(17);
        this.cellTxt.setGravity(Gravity.CENTER);
    }

    // Row creation
    private void newRow() {
        this.tableRow = new TableRow(this.context);
    }

    //Creation of needed layout parameters
    @NonNull
    private TableRow.LayoutParams TableRow_LayoutParams() {
        TableRow.LayoutParams p = new TableRow.LayoutParams();
        p.setMargins(1, 1, 1, 1);
        p.weight = 1;
        return p;
    }
}
