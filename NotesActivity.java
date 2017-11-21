package io.futurebound.manageit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    EditText EditText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        EditText1= (EditText) findViewById(R.id.notesEditText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Save("Note1.txt");
               /* try{
                    //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(fileName, 0));
                    OutputStreamWriter out = new OutputStreamWriter(openFileOutput("note1.txt", Context.MODE_APPEND));
                    out.write(EditText1.getText().toString());
                    out.close();
                    //EditText1.setText("");
                    //Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(NotesActivity.this,  "Saved!", Toast.LENGTH_SHORT).show();
                }catch (Throwable t){
                    //Toast.makeText(this, "Exception:" +t.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(NotesActivity.this, "Exception:" +t.toString(), Toast.LENGTH_SHORT).show();
                }*/

                //Intent intent = new Intent(getApplicationContext(),SelectedNote.class);
                //startActivity(intent);
            }
        });

        //EditText1.setText(Open("Note1.txt"));
    }
    private void write() {
        EditText1.setText("");

    }
    private void Save(String fileName) {
        try{
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(fileName, 0));
            //OutputStreamWriter out = new OutputStreamWriter(openFileOutput("note1.txt", Context.MODE_APPEND));
            out.write(EditText1.getText().toString());
            out.close();
            //EditText1.setText("");
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        }catch (Throwable t){
            Toast.makeText(this, "Exception:" +t.toString(), Toast.LENGTH_SHORT).show();
        }
    }
   /* @RequiresApi(api = Build.VERSION_CODES.N)
    private void Save() {
        try{
           String date = new SimpleDateFormat("EEE, dd MMM yyyy").format(new Date());
            String fileName = EditText1.getText().toString();
            File myFile= new File("note.txt");
            BufferedWriter buff= new BufferedWriter(new FileWriter(myFile));

            buff.append ( fileName );
            buff.newLine ( );
            buff.append ( date );
            buff.newLine ( );
            buff.close();

            //EditText1.setText("");
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        }catch (Throwable t){
            Toast.makeText(this, "Exception:" +t.toString(), Toast.LENGTH_SHORT).show();
        }
    }*/


    public String Open(String fileName) {
        String content = "";
        if (FileExists(fileName)) {
            try {
                InputStream in = openFileInput(fileName);
                if ( in != null) {
                    InputStreamReader tmp = new InputStreamReader( in );
                    BufferedReader reader = new BufferedReader(tmp);
                    String str;
                    StringBuilder buf = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        buf.append(str + "\n");
                    } in .close();
                    content = buf.toString();
                }
            } catch (java.io.FileNotFoundException e) {} catch (Throwable t) {
                Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return content;
    }
    public boolean FileExists(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

}
