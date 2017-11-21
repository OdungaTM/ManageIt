package io.futurebound.manageit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static io.futurebound.manageit.R.id.time;

public class UpdateProductActivity extends AppCompatActivity {
        EditText updateProductName, updateQuantity, updatePrice;
    TextView  updateCode, date;
    ProgressDialog progress;
   // Button updateAll;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        updateCode = (TextView) findViewById(R.id.txtVwUpdateCode);
        date = (TextView) findViewById(R.id.txtVwTarehe);
        updateProductName = (EditText) findViewById(R.id.edTxtUpdateProduct);
        updatePrice = (EditText) findViewById(R.id.edVwPesa);
        updateQuantity = (EditText) findViewById(R.id.edTxtUpdateDescription);
        //updateAll = (Button) findViewById(R.id.btnUpdateAll);
        progress = new ProgressDialog(this);
        progress.setMessage("Sending to Server");
        String d = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        date.setText(d);
        Typeface font = Typeface.createFromAsset(getAssets(),"Roboto-Bold.ttf");
       updateCode.setTypeface(font);
        date.setTypeface(font);
        updateProductName.setTypeface(font);
       updatePrice.setTypeface(font);
       updateQuantity.setTypeface(font);


        Bundle f= getIntent().getExtras();
        String g= f.getString("result");
        updateCode.setText(g);
    }

    public void updateStore(View view) {
        String barcode= updateCode.getText().toString().trim();
        String product= updateProductName.getText().toString().trim();
        String quantity = updateQuantity.getText().toString().trim();
        String price = updatePrice.getText().toString().trim();
        String time= date.getText().toString();

        SharedPreferences prefs=getSharedPreferences("values", MODE_PRIVATE);
        String email= prefs.getString("email","Not saved");
        String user= prefs.getString("_code","Name not available");

        if (product.isEmpty() || quantity.isEmpty() ||price.isEmpty()){
            Toast.makeText(this, "Error: Enter  Product, Price and Quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.show();
        String url = "http://jistymarketer.com/Mark/update.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("barcode", barcode);
        params.put("product", product);
        params.put("quantity", quantity);
        params.put("price", price);
        params.put("time", time);
        params.put("email", email);
        params.put("user", user);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(UpdateProductActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //Toast.makeText(UpdateProductActivity.this, "Succeded", Toast.LENGTH_SHORT).show();
                progress.dismiss();
                Intent intent= new Intent(getApplicationContext(),SuccessActivity.class);
                startActivity(intent);
                finish();


            }
        });



    }
}
