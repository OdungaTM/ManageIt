package io.futurebound.manageit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }

        });
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the Sign up Activity
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivityForResult(intent,REQUEST_SIGNUP);
            }
        });
    }
    public  void login(){
        Log.d(TAG, "Login");

        if(!validate()){
            onLoginFailed();
            return;
        }
        _loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email= _emailText.getText().toString().trim();
        final String password= _passwordText.getText().toString().trim();
        //TODO:Implement authentification logic here
        String url = "http://jistymarketer.com/Mark/mlogin.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                onLoginFailed();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String r= responseString;

                try {
                    JSONObject main = new JSONObject(r);
                   JSONArray array= main.getJSONArray("user");
                    for(int i=0; i< array.length();i++){
                        JSONObject obj= array.getJSONObject(i);
                        String mail= obj.getString("email");
                        String pass= obj.getString("password");
                        String name= obj.getString("name");

                        if(mail.contentEquals(email)){
                            onLoginSuccess();
                            progressDialog.dismiss();
                            Intent intent= new Intent(getApplicationContext(),MainActivity.class);

                            SharedPreferences.Editor prefs=getSharedPreferences("values", MODE_PRIVATE).edit();
                            prefs.putString("name", name);
                            prefs.putString("email",mail);
                            prefs.commit();
                            /*Bundle x = new Bundle();
                            x.putString("name",name);
                            x.putString("mail",mail);
                            intent.putExtras(x);*/
                            startActivity(intent);
                            finish();
                        }else if(mail.isEmpty()){
                            onLoginFailed();
                            progressDialog.dismiss();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    onLoginFailed();
                }


            }
        });

       new android.os.Handler().postDelayed(new Runnable() {
            @Override
           public void run() {
               //on complete call either onLoginSuccess or onLoginFailed
               onLoginSuccess();

            }
        },3000);

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_SIGNUP){
            if(resultCode==RESULT_OK){
                //TODO implement successful sigup logic here
                //by default finish the activity and log them in automatically
                onLoginSuccess();
                this.finish();

            }
        }
    }
    @Override
    public  void onBackPressed(){
        //disable going back to MainActivity
        moveTaskToBack(true);
    }
    public  void onLoginSuccess(){
        _loginButton.setEnabled(true);

    }
    public  void onLoginFailed(){
        Toast.makeText(getBaseContext(), "Log In Failed", Toast.LENGTH_SHORT).show();
        _loginButton.setEnabled(true);
    }
    public  boolean validate(){
        boolean valid= true;
        String email= _emailText.getText().toString();
        String paswword= _passwordText.getText().toString();

        if(email.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailText.setError("enter a valid email address");
            valid=false;
        }
        else {
            _emailText.setError(null);
        }
        if(paswword.isEmpty()||paswword.length()<6||paswword.length()>12){
            _passwordText.setError("set password between 6 and 12 alphanumeric characters");
            valid= false;
        }
        else{
            _passwordText.setError(null);
        }
        return valid;
    }
}
