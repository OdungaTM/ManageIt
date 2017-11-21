package io.futurebound.manageit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button scanObject, removeObject, updateObject,storeProducts,transactions,overview;
    TextView email, name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        email = (TextView)header.findViewById(R.id.txtViewAddress);
        name = (TextView)header.findViewById(R.id.txtViewName);

        SharedPreferences prefs=getSharedPreferences("values", MODE_PRIVATE);
        String mail= prefs.getString("email","Empty");
        String user= prefs.getString("name","Empty");
        email.setText(mail);
        name.setText(user);

        String nameString= name.getText().toString();
        String emailString= email.getText().toString();

       if(nameString.contentEquals("Empty")||emailString.contentEquals("Empty")){
           Intent tent = new Intent(getApplicationContext(),LoginActivity.class);
           startActivity(tent);

       }

        scanObject = (Button) findViewById(R.id.btnAddProduct);
        removeObject= (Button) findViewById(R.id.btnDeleteProduct);
       updateObject= (Button) findViewById(R.id.btnUpdateProduct);
        transactions= (Button) findViewById(R.id.btnTransactions);
       storeProducts= (Button) findViewById(R.id.btnStore);
      overview= (Button) findViewById(R.id.btnOverview);

      overview.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent n= new Intent(getApplicationContext(),GraphActivity.class);
              startActivity(n);

          }
      });
        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(),NewTransactionActivity.class);
                startActivity(n);
            }
        });

        storeProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n= new Intent(getApplicationContext(),StoreActivity.class);
                startActivity(n);
            }
        });
        final Bundle b= new Bundle();
        final Intent navigate = new Intent(MainActivity.this,BarCodeMain.class);

        //navigates to the barCodeReaderActivity
        scanObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.putInt("toScan",5000);
                navigate.putExtras(b);
                startActivity(navigate);


            }
        });

       removeObject.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               b.putInt("toScan",6000);
               navigate.putExtras(b);
               startActivity(navigate);

           }
       });
        updateObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.putInt("toScan",7000);
                navigate.putExtras(b);
                startActivity(navigate);


            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        /*Bundle x= getIntent().getExtras();
        String _user =x.getString("name");
        String _email= x.getString("mail");
        email.setText(_email);
        name.setText(_user);
        SharedPreferences.Editor prefs=getSharedPreferences("values", MODE_PRIVATE).edit();
        prefs.putString("name", _user);
        prefs.putString("email",_email);
        prefs.commit();*/



    }


    public boolean  resume() {
        boolean valid= true;
        String nameString= name.getText().toString();
        String emailString= email.getText().toString();
        if(nameString.isEmpty()||emailString.isEmpty()){
           valid= false;
        }
        else {
            valid= true;
        }
        return valid;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
           Intent intent = new Intent(getApplicationContext(),NotesActivity.class);
            startActivity(intent);
        }
         else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            email.setText("");
            name.setText("");
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
