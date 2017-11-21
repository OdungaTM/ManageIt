package io.futurebound.manageit;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class GraphActivity extends AppCompatActivity {
    String email, user, product_noduplicate, products_duplicate;
    int i,j, total_duplicate,total_nonduplicate;
    BarChart barChart;
    BarDataSet dataSet,dataSet1;
    ArrayList<String> xAxis_stringArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        barChart =(BarChart)findViewById(R.id.barchart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(100);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);

        final ArrayList<BarEntry> barEntries= new ArrayList<>();
        final ArrayList<BarEntry> barEntries1= new ArrayList<>();
        xAxis_stringArray = new ArrayList<>();

        SharedPreferences prefs=getSharedPreferences("values", MODE_PRIVATE);
        email= prefs.getString("email","Not saved");
        user= prefs.getString("name","Name not available");

        final String url="http://jistymarketer.com/Mark/added.php";
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params= new RequestParams();
        params.put("user",user);
        params.put("email",email);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);

                try {
                    JSONObject main = new JSONObject(response);
                    JSONArray array = main.getJSONArray("duplicate");

                    for (i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        int price_duplicate = obj.getInt("price");
                        products_duplicate = obj.getString("product");
                        int quantity_duplicate = obj.getInt("quantity");
                        int code_duplicate = obj.getInt("COUNT(code)");
                        //total price= COUNT(code)*quantity*price
                        total_duplicate = price_duplicate * quantity_duplicate * code_duplicate;
                        //Log.d("ECG",String.valueOf(total_duplicate));
                        barEntries.add(new BarEntry(Float.valueOf(i), Float.valueOf(total_duplicate)));
                        dataSet = new BarDataSet(barEntries, "Sales per Product");
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    }
                    JSONArray array2 = main.getJSONArray("nonduplicate");
                    for (j = 0; j < array2.length(); j++) {
                        JSONObject obj = array.getJSONObject(j);
                        int price_nonduplicate = obj.getInt("price");
                        product_noduplicate = obj.getString("product");
                        int quantity_noduplicate = obj.getInt("quantity");
                        //total price = quantity * price
                        total_nonduplicate = price_nonduplicate * quantity_noduplicate;
                        barEntries1.add(new BarEntry(Float.valueOf(j), Float.valueOf(total_nonduplicate)));
                        dataSet1 = new BarDataSet(barEntries1, "Sales per Product");
                        dataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

                    }


                    //xAxis_stringArray.add(products_duplicate);
                    //xAxis_stringArray.add(product_noduplicate);

                    //create bardata
                    BarData barData = new BarData(dataSet,dataSet1);
                    //barData.setBarWidth(0.9f);

                    float groupSpace=0.2f;
                    float barSpace=0.03f;
                   float barWidth = 0.43f;

                    barChart.setData(barData);
                    barData.setBarWidth(barWidth);
                   barChart.groupBars(0,barSpace,groupSpace);

                    //to separate groups

                   /* //create xaxis
                        String[]months= new String[]{"jan","feb","march"};
                        XAxis xAxis= barChart.getXAxis();
                        //xAxis.setValueFormatter(new MyXAxisValueFormatter(xAxis_stringArray));
                        xAxis.setValueFormatter(new MyXAxisValueFormatter(months));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                        xAxis.setGranularity(1);
                        xAxis.setCenterAxisLabels(true);
                        xAxis.setAxisMinimum(1);*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    public class MyXAxisValueFormatter implements IAxisValueFormatter{
        private String[]mValues;
        //ArrayList<String>mValues= new ArrayList<>();
        public MyXAxisValueFormatter(String[]values){
        //public MyXAxisValueFormatter(ArrayList<String>values){
            this.mValues= values;

        };
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];
            //return mValues.get(Math.round(value));
        }
    }

}
