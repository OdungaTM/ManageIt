package io.futurebound.manageit;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectedNote extends AppCompatActivity {
    private List<NotesBuilder> notesList= new ArrayList<>();
    private NotesAdapter nAdapter;
    private RecyclerView notesRecycler;
    private static CustomAdapter adapter;
    ArrayList<DataModel> dataModels;
    ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NotesActivity.class);
                startActivity(intent);
            }
        });
        listView= (ListView)findViewById(R.id.listview);

        /*notesRecycler = (RecyclerView) findViewById(R.id.notes);
        nAdapter = new NotesAdapter(notesList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        notesRecycler.setLayoutManager(mLayoutManager);
        notesRecycler.setItemAnimator(new DefaultItemAnimator());
        notesRecycler.setAdapter(nAdapter);*/

        prepareNotes();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void prepareNotes() {
        String date = new SimpleDateFormat("EEE, dd MMM yyyy").format(new Date());
        File directory;
        directory = getFilesDir();
        File[] files = directory.listFiles();
        String theFile;
        for (int f = 1; f < files.length; f++) {
            theFile = "Note" + f + ".txt";
            dataModels= new ArrayList<>();
            dataModels.add(new DataModel(date,theFile, Open(theFile), "mine","home"));
            //NotesBuilder note = new NotesBuilder(theFile, Open(theFile),date);
            //notesList.add(note);
        }
        adapter= new CustomAdapter(dataModels,getApplicationContext());
        adapter.notifyDataSetChanged();//aloows for refeshing INotifyProprtychaned
        listView.setAdapter(adapter);
    }
    public  String Open(String fileName){
        String content="";
        try{
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
            Toast.makeText( this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
        return content;

    }

}
