package io.futurebound.manageit;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {
    Button success;
    TextView tvxSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        tvxSuccess= (TextView)findViewById(R.id.success);

        tvxSuccess.setText("Successful!");
        Typeface font = Typeface.createFromAsset(getAssets(),"Roboto-Bold.ttf");
        tvxSuccess.setTypeface(font);

    }


}
