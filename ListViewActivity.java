package io.futurebound.manageit;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=(ListView)findViewById(R.id.list);

        dataModels= new ArrayList<>();
        dataModels.add(new DataModel("Apple Pie","100", "Android 1.0", "1","September 23, 2008"));
        dataModels.add(new DataModel("Banana Bread","100", "Android 1.1", "2","February 9, 2009"));
        dataModels.add(new DataModel("Cupcake","100", "Android 1.5", "3","April 27, 2009"));
        dataModels.add(new DataModel("Donut","100","Android 1.6","4","September 15, 2009"));
        dataModels.add(new DataModel("Eclair","100", "Android 2.0", "5","October 26, 2009"));
        dataModels.add(new DataModel("Froyo", "100","Android 2.2", "8","May 20, 2010"));
        dataModels.add(new DataModel("Gingerbread", "100","Android 2.3", "9","December 6, 2010"));
        dataModels.add(new DataModel("Honeycomb","100","Android 3.0","11","February 22, 2011"));
        dataModels.add(new DataModel("Ice Cream Sandwich","100", "Android 4.0", "14","October 18, 2011"));
        dataModels.add(new DataModel("Jelly Bean", "Android 4.2","100", "16","July 9, 2012"));
        dataModels.add(new DataModel("Kitkat", "Android 4.4","100", "19","October 31, 2013"));
        dataModels.add(new DataModel("Lollipop","Android 5.0","100","21","November 12, 2014"));
        dataModels.add(new DataModel("Marshmallow", "Android 6.0","100", "23","October 5, 2015"));

        adapter= new CustomAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel= dataModels.get(position);

                Snackbar.make(view, dataModel.getCode()+"\n"+dataModel.getProduct()+" API: "+dataModel.getQuantity(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
