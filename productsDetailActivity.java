package io.futurebound.manageit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.w3c.dom.Text;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class productsDetailActivity extends AppCompatActivity {
    TextView barCodeInfo, dateTimeInfo;
    EditText productInfo,sizeInfo,priceInfo;
    Button submitToDatabase;
    ProgressDialog progress;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_detail);

        Typeface font = Typeface.createFromAsset(getAssets(),"Roboto-Bold.ttf");



        barCodeInfo = (TextView) findViewById(R.id.txtViewBarCode);
        dateTimeInfo = (TextView) findViewById(R.id.txtViewAddDate);
        productInfo = (EditText) findViewById(R.id.txtViewProductName);
        priceInfo = (EditText) findViewById(R.id.txtViewPrice);
        sizeInfo = (EditText) findViewById(R.id.txtViewQuantity);

        barCodeInfo.setTypeface(font);
        dateTimeInfo.setTypeface(font);
        productInfo.setTypeface(font);
        priceInfo.setTypeface(font);
        sizeInfo.setTypeface(font);
        submitToDatabase=(Button)findViewById(R.id.btnSubmitToDatabase);
       // submitToDatabase = (Button) findViewById(R.id.btnSubmitToDatabase);
        progress = new ProgressDialog(this);
        progress.setMessage("Sending to Server");

        final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dateTimeInfo.setText(date);

        //retrieving the information sent from barCodeReaderActivity.java
        Bundle b = getIntent().getExtras();
        final String productcode= b.getString("result");
        barCodeInfo.setText(productcode);
    }
    //uploading user input to database
    //implements the button onclick event

    public void save(View view) {
        SharedPreferences prefs=getSharedPreferences("values", MODE_PRIVATE);
        String email= prefs.getString("email","Not saved");
        String user= prefs.getString("name","Name not available");

        Log.d("email",email);
        String barcode= barCodeInfo.getText().toString().trim();
        String product= productInfo.getText().toString().trim();
        String quantity= sizeInfo.getText().toString().trim();
        String dateTime=dateTimeInfo.getText().toString();
        String price=priceInfo.getText().toString().trim();

        if (product.isEmpty() || quantity.isEmpty()){
            Toast.makeText(this, "Error: Enter valid Product _code and quantitity", Toast.LENGTH_SHORT).show();
            productInfo.setText("");
            sizeInfo.setText("");
           return;


        }
        progress.show();
        String url = "http://jistymarketer.com/Mark/codeSave.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("barcode", barcode);
        params.put("product", product);
        params.put("quantity", quantity);
        params.put("price",price);
        params.put("dateTime", dateTime);
        params.put("mail", email);
        params.put("user", user);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                //Toast.makeText(productsDetailActivity.this, r, Toast.LENGTH_SHORT).show();
                productInfo.setText("");
                sizeInfo.setText("");
                progress.dismiss();

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
