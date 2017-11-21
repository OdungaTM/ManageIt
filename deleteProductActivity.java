package io.futurebound.manageit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class deleteProductActivity extends AppCompatActivity {
    TextView id, product, description, idfinal, time;
    EditText price, purchased;
    Button delete, confirmdelete;
    ProgressDialog progress;
    String email, user, stock;
    int productsBefore, productPurchsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        id = (TextView) findViewById(R.id.txtvwitemid);
        product = (TextView) findViewById(R.id.txtvwproductid);
        description = (TextView) findViewById(R.id.txtvwsizeid);
        idfinal = (TextView) findViewById(R.id.txtViewId);
        time = (TextView) findViewById(R.id.txtVwAdDateTime);
        price = (EditText) findViewById(R.id.editTxtPrice);
        purchased = (EditText) findViewById(R.id.editTextTotalPurchased);
        delete = (Button) findViewById(R.id.btndelete);
        progress = new ProgressDialog(this);
        progress.setMessage("Retrieving from Server");

        SharedPreferences prefs=getSharedPreferences("values", MODE_PRIVATE);
         email= prefs.getString("email","Not saved");
         user= prefs.getString("name","Name not available");

        //retrieving the information sent from barCodeReaderActivity.java
        Bundle b = getIntent().getExtras();
        String productcode= b.getString("result");
        id.setText(productcode);
    }

    public void delete(View view) {
        String barid= id.getText().toString().trim();


        if(barid.isEmpty()){
            return;
        }

        progress.show();
        String url = "http://jistymarketer.com/Mark/deleteCode.php";
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("barid", barid);
        params.put("email", email);
        params.put("user", user);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(deleteProductActivity.this, "Failed To connect to Server. Check internet conncetion", Toast.LENGTH_SHORT).show();

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //Toast.makeText(deleteProductActivity.this, "succeded", Toast.LENGTH_SHORT).show();
                progress.dismiss();

               String text = new String(responseString);
               // Toast.makeText(deleteProductActivity.this, text, Toast.LENGTH_SHORT).show();
                //Getting Details about product to delete

                try {
                    JSONArray array = new JSONArray(text);
                    for(int i=0; i<array.length();i++){
                        String id= array.getJSONObject(i).getString("id");
                        String code= array.getJSONObject(i).getString("code");
                        String products= array.getJSONObject(i).getString("product");
                        stock= array.getJSONObject(i).getString("stock");

                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        time.setText(date);

                        product.setText(products);
                        description.setText(stock);
                        idfinal.setText(code);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    public void confirm(View view) {
        String deleteId= id.getText().toString();
        String timeId= time.getText().toString();
        String productId= product.getText().toString().trim();
        String weightId= description.getText().toString().trim();
        String priceId= price.getText().toString().trim();
        String purchasedId= purchased.getText().toString().trim();
        if(timeId.isEmpty()|| productId.isEmpty()|| weightId.isEmpty()){
            Toast.makeText(this, "Confirm Product", Toast.LENGTH_SHORT).show();
            delete.setBackgroundDrawable(getResources().getDrawable(R.drawable.textviewslayout));

            return;
        }
        if(Integer.parseInt(purchasedId)> Integer.parseInt(weightId)){
            Toast.makeText(this, "sold products greater than products in store", Toast.LENGTH_SHORT).show();
        }
        if(priceId.isEmpty()||purchasedId.isEmpty()){
            Toast.makeText(this, "Enter the Quantity & Price of Product", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();
        productsBefore = Integer.parseInt(stock);
        productPurchsed= Integer.parseInt(purchasedId);
        int rem= productsBefore - productPurchsed;
        String url = "http://jistymarketer.com/Mark/permanent.php";
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("deleteId", deleteId);
        params.put("productId", productId);
        params.put("rem", rem);
        params.put("weightId", weightId);
        params.put("purchasedId", purchasedId);
        params.put("timeId", timeId);
        params.put("priceId", priceId);
        params.put("email", email);
        params.put("user", user);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(deleteProductActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                return;

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                progress.dismiss();
                Intent intent= new Intent(getApplicationContext(),SuccessActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
