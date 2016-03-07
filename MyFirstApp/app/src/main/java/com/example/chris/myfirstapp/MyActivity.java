package com.example.chris.myfirstapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.chris.myfirstapp.MESSAGE";
    DBAdapter myDb;

    private void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDB(){
        myDb.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // create database
        openDB();

        long newId1 = myDb.insertRow("Chris Test", 12345, "Blue");
        long newId2 = myDb.insertRow("Chris 2", 67890, "Black");
        System.out.println("NEWIDS"+newId1+" "+newId2);

        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
    }

    private void displayRecordSet(Cursor cursor) {
        String message = "";

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int studentNumber = cursor.getInt(2);

                message += "id =" + id
                        + ", name=" + name
                        + ", #=" + studentNumber
                        + "\n";
            } while(cursor.moveToNext());
        }
        cursor.close();
        System.out.println(message);

        TextView textView = (TextView) findViewById(R.id.recordTextView);
        textView.setText(message);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        closeDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
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

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void addRecord(View view){
        EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        EditText numberEditText = (EditText) findViewById(R.id.numberEditText);
        long newId2 = myDb.insertRow(nameEditText.getText().toString(),
                Integer.parseInt(numberEditText.getText().toString()), "Black");
        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
    }

    public void clearDB(View view){
        myDb.deleteAll();
        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
    }
}

