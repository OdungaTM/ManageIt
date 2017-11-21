package io.futurebound.manageit;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class StoreActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeToRefesh;
    ArrayList<DataModel> dataModels;
    ListView listView;
    String date;
    private static CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        swipeToRefesh= (SwipeRefreshLayout) findViewById(R.id.swpToRefresh);

        swipeToRefesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();

            }
        });

        fetchData();

    }

    private void fetchData() {
        SharedPreferences prefs=getSharedPreferences("values", MODE_PRIVATE);
        String email= prefs.getString("email","Not saved");
        String user= prefs.getString("name","Name not available");

        String url="http://jistymarketer.com/Mark/store.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email",email);
        params.put("user",user);
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String text = new String(responseBody);//converting byte to string

                listView=(ListView)findViewById(R.id.list);
                dataModels= new ArrayList<>();
                swipeToRefesh.setRefreshing(false);//st it to false when data
                try {
                    dataModels.clear();//removes all old files and sets in new
                    JSONObject main = new JSONObject(text);
                    JSONArray array = main.getJSONArray("total");
                    for(int i=0; i< array.length();i++)//cant use a foraech loop fr json
                    {
                        JSONObject obj = array.getJSONObject(i);
                        //extracting the string from obj
                        String item = obj.getString("code");//item is the key in the json file
                        String item1 = obj.getString("product");
                        String item2 = obj.getString("stock");
                        String item3 = obj.getString("update");
                        String item4= obj.getString("date");
                        String item5= obj.getString("price");
                        date =item4;


                        dataModels.add(new DataModel(item,item1, item2, item3,item5));

                    }
                    adapter= new CustomAdapter(dataModels,getApplicationContext());
                    adapter.notifyDataSetChanged();//aloows for refeshing INotifyProprtychaned
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            DataModel dataModel= dataModels.get(position);

                            Snackbar.make(view, dataModel.getCode()+"\n"+dataModel.getProduct()+" Added on: "+date, Snackbar.LENGTH_LONG)
                                    .setAction("No action", null).show();
                        }
                    });

                    /*codeDataAdapter.notifyDataSetChanged();
                    descriptionDataAdapter.notifyDataSetChanged();
                    priceDataAdapter.notifyDataSetChanged();*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(StoreActivity.this, "Failed.Please check your connectivity", Toast.LENGTH_SHORT).show();
                swipeToRefesh.setRefreshing(false);


            }
        });
    }
}
