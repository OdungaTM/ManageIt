package io.futurebound.manageit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class Transaction3Fragment extends Fragment {
    String user, email;
    ArrayList<DataModel> dataModels;
    ListView listView;
    SwipeRefreshLayout swipeToRefesh ;
    private static CustomAdapter adapter;

    public Transaction3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_transaction3, container, false);
        listView=(ListView)root.findViewById(R.id.list);
        swipeToRefesh= (SwipeRefreshLayout) root.findViewById(R.id.swpToRefresh);

        swipeToRefesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch();

            }
        });

        fetch();

        return root;
    }

    private void fetch() {
        NewTransactionActivity activity = (NewTransactionActivity) getActivity();
        user= activity.getUser();
        email=activity.getEmail();
        String url="http://jistymarketer.com/Mark/added.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params= new RequestParams();
        params.put("user",user);
        params.put("email",email);
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String text = new String(responseBody);//converting byte to string
                dataModels= new ArrayList<>();
                swipeToRefesh.setRefreshing(false);

                try {
                    dataModels.clear();
                    JSONObject main= new JSONObject(text);
                    JSONArray array =main.getJSONArray("total");
                    for(int i=0; i< array.length();i++)//cant use a foraech loop fr json
                    {
                        JSONObject obj = array.getJSONObject(i);
                        //extracting the string from obj
                        String item = obj.getString("code");//item is the key in the json file
                        String item1 = obj.getString("product");
                        String item2 = obj.getString("quantity");
                        String item3 = obj.getString("time");
                        String item4= obj.getString("price");


                        dataModels.add(new DataModel(item,item1,item2,item3,item4));
                        //String t= dataModels.toString();

                        adapter= new CustomAdapter(dataModels,getContext());
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                DataModel dataModel= dataModels.get(position);

                                Snackbar.make(view, dataModel.getCode()+"\n"+dataModel.getProduct()+" Purchased on: "+dataModel.getDat()+"@" +dataModel.getCost() +"per product", Snackbar.LENGTH_LONG)
                                        .setAction("No action", null).show();
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
    public ArrayList get_DataList()
    {
        return dataModels;
    }

}
