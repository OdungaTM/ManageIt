package io.futurebound.manageit;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class Transaction1Fragment extends Fragment {
    TextView total, income, added, _purchased, lifeTime;
    SmoothProgressBar progress;
    String user, email,_date;
    int lifeTime_purchases,total_today,total_products, added_today, each_today,_result,price_today;


    public Transaction1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root= inflater.inflate(R.layout.fragment_transaction1, container, false);

        total= (TextView) root.findViewById(R.id.txtVwTtProdcts);
        income= (TextView) root.findViewById(R.id.txtVwExptdIncome);
        added= (TextView) root.findViewById(R.id.txtVwAddedProdcts);
        _purchased = (TextView) root.findViewById(R.id.txtVwRemovedProdtcs);
        lifeTime= (TextView) root.findViewById(R.id.txVwLiftimePurch);

        //adapt= new  ArrayAdapter(getActivity(),R.layout.list_item_layout,R.id.listItem,dataList);
        progress=(SmoothProgressBar)root.findViewById(R.id.progressSmoothBar);
        look();
        fetch();
        return root;
    }

    private void look() {
        NewTransactionActivity activity = (NewTransactionActivity) getActivity();
        user= activity.getUser();
        email=activity.getEmail();

        String url="http://jistymarketer.com/Mark/added.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params= new RequestParams();
        params.put("user",user);
        params.put("email",email);
        client.post(url,params, new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String text= new String(responseBody);
                try {
                    JSONObject main= new JSONObject(text);
                    JSONArray array= main.getJSONArray("total");
                    for(int i=0; i< array.length();i++){
                        JSONObject obj= array.getJSONObject(i);
                        lifeTime_purchases += Integer.parseInt(obj.getString("quantity"));
                        lifeTime.setText(String.valueOf(lifeTime_purchases));

                    }


                    JSONArray arry= main.getJSONArray("today");
                    for(int i=0; i< arry.length();i++){
                        JSONObject obj= arry.getJSONObject(i);
                        total_today += Integer.parseInt(obj.getString("quantity"));
                        each_today = Integer.parseInt(obj.getString("quantity"));
                        _purchased.setText(String.valueOf(total_today));

                        price_today= Integer.parseInt(obj.getString("price"));
                        _result += each_today*price_today;
                        income.setText(String.valueOf(_result));

                    }
                    JSONArray _getdate = main.getJSONArray("today");
                    for(int i=0; i<_getdate.length();i++){
                        JSONObject obj= _getdate.getJSONObject(i);
                       _date = obj.getString("time");
                       String _goods= obj.getString("product");
                        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor edt = pref.edit();
                        edt.putString("goods", _goods);
                        edt.putInt("price",price_today);
                        edt.commit();
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
    public int getTotal_today(){
        return _result;
    }
    public String getDate(){
        return _date;
    }

    private void fetch() {
        progress.setVisibility(View.VISIBLE);
        String url="http://jistymarketer.com/Mark/store.php";
        RequestParams params = new RequestParams();
        params.put("user",user);
        params.put("email",email);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String text = new String(responseBody);
                try {
                    JSONObject main= new JSONObject(text);
                    JSONArray array = main.getJSONArray("total");
                    for(int i=0; i< array.length();i++){
                        JSONObject obj= array.getJSONObject(i);
                        total_products += Integer.parseInt(obj.getString("stock"));
                        total.setText(String.valueOf(total_products));

                    }

                    JSONArray array2= main.getJSONArray("today");
                    for(int i=0; i< array.length();i++){
                        JSONObject obj= array2.getJSONObject(i);
                        added_today+= Integer.parseInt(obj.getString("quantity"));
                        added.setText(String.valueOf(added_today));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progress.setVisibility(View.INVISIBLE);



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
