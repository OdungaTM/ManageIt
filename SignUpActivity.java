package io.futurebound.manageit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Finish the registaration screen and return to LoginActivity
                finish();
            }
        });
    }
    public  void signup(){
        Log.d(TAG,"Signup");
        if(!validate()){
            onSignupFailed();
            return;
        }
        _signupButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString().trim();
        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();

        String url = "http://jistymarketer.com/Mark/mregister.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("_code", name);
        params.put("email", email);
        params.put("password", password);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String r= responseString;
                Toast.makeText(SignUpActivity.this, r, Toast.LENGTH_SHORT).show();

            }
        });

        // TODO: Implement your own signup logic here.
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // On complete call either onSignupSuccess or onSignupFailed
                // depending on success
                onSignupSuccess();
                // onSignupFailed();
                progressDialog.dismiss();
            }
        },3000);
    }
    public  void  onSignupSuccess(){
        _signupButton.setEnabled(true);
        setResult(RESULT_OK,null);
        Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public  void  onSignupFailed(){
        Toast.makeText(getBaseContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
        _signupButton.setEnabled(true);
    }
    public  boolean validate(){
        boolean valid =true;
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        if (name.isEmpty() || name.length() < 6) {
            _nameText.setError("at least 6 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            _passwordText.setError("between 6 and 12 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return  valid;

    }
}
