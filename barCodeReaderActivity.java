package io.futurebound.manageit;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static android.Manifest.permission.CAMERA;

public class barCodeReaderActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    //The constant REQUEST_CAMERA is used while getting the permissions from the user to use the camera.
    // ZXingScannerView provides the view to scan the QR code and Bar code as shown below.
    private  static  final int REQUEST_CAMERA=1;
    private  ZXingScannerView mScannerView;
     //ZXingScannerView  scanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "onCreate");
      mScannerView = new ZXingScannerView(this);
        //scanner=(ZXingScannerView)findViewById(R.id.mScannerView);
        //setContentView(R. layout.activity_bar_code_reader);
        //setContentView(scanner);
        setContentView(mScannerView);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if(currentapiVersion >= android.os.Build.VERSION_CODES.M){
            if(checkPermision()){
                //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

            }
            else{
                requestPermission();
            }

        }

        //setContentView(R.layout.activity_bar_code_reader);
        //We create a new ZXingScannerView by passing the context.
        // Then set the conteent view to the same using the setContentView() method. To access the Camera, we need permissions.
        //Starting from Android 6.0( Android Marshmallow) we have to get the user permissions
        //at the runtime. So the next few lines checks for the api version
        // We need to detect whether the app has the required permissions or not
        // otherwise we need to request those from the user befor accessing the camera.

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    private boolean checkPermision() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }


    public  void  onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case  REQUEST_CAMERA:
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(this, "Permission Granted, Now you can access camera", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "Permission Denied, You cannot access and camera", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }

                                            }
                                        });
                                return;

                            }

                        }
                    }
                }
                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(barCodeReaderActivity.this)
                .setMessage(message)
                //.setPositiveButton("OK", okListener)
                .setPositiveButton("OK",okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    //For the first time, when the user installs the app,
    // the app will request permission to use the Camera, on subsequent app runs we don’t need to provide any permission.
    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermision()) {
                if(mScannerView == null) {
                //if(scanner == null) {
                    mScannerView = new ZXingScannerView(this);
                   //scanner = new ZXingScannerView(this);
                    //setContentView(R. layout.activity_bar_code_reader);
                    setContentView(mScannerView);
                }
                mScannerView.setResultHandler(this);
                //scanner.setResultHandler(this);
                mScannerView.startCamera();
                //scanner.startCamera();

            } else {
                requestPermission();
            }
        }
    }
    @Override
    public  void onDestroy(){
        super.onDestroy();
       mScannerView.stopCamera();
       //scanner.stopCamera();
    }
    //We need to handle the onResume() and onDestroy() lifecycle methods for QRCodeScannerActivity.
    // In the onResume() we check if the ScannerView is null or not, in case it is null we create a new one and then start the
    // Camera to capture the QR Code using the startCamera() method.
    // Similarly in the onDestroy() we release the Camera using the stopCamera().


    //To implement the ZXingScannerView.ResultHandler we need to implement the method handleResult().
    // This method contains the Logic to handle the Result of the scan from
    // the android QR Scanner or the Bar Code scanner. Add the following code that will handle the result for you.



    @Override
    public void handleResult(Result rawResult) {
        //final String result = rawResult.getText();
        final String result = rawResult.getBarcodeFormat().toString();
        Log.d ("QRCodeScanner",rawResult.getText());
        Log.d("QRCodeScanner",rawResult.getBarcodeFormat().toString());
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // mScannerView.resumeCameraPreview(barCodeReaderActivity.this);

                final Bundle x= getIntent().getExtras();
                final Bundle c= new Bundle();

                if(x.get("toUpdate")!=null) {
                    Intent a= new Intent(getApplicationContext(),UpdateProductActivity.class);
                    c.putString("result",result);
                    a.putExtras(c);
                    startActivity(a);
                    finish();
                }
                else if (x.get("toDelete")!=null)  {
                    Intent n = new Intent(getApplicationContext(),deleteProductActivity.class);
                    c.putString("result", result);
                    n.putExtras(c);
                    startActivity(n);
                    finish();
                }


                else if(x.get("toScan")!=null){
                  Intent navigate = new Intent(getApplicationContext(),productsDetailActivity.class);
                  c.putString("result",result);
                  navigate.putExtras(c);
                 startActivity(navigate);
                    finish();
              }





            }
        });

       // builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
           // @Override
           // public void onClick(DialogInterface dialog, int which) {
               // Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(result));
              //  startActivity(browserIntent);
           // }
        //});
        builder.setMessage(rawResult.getText());
        AlertDialog alert1= builder.create();
        alert1.show();



        //On getting the result from the QR Code or Bar Code scan, this method will be called.
        // We are logging the result to the LogCat as well as Showing a AlertDialog with the result of the scan and two buttons.
        // Clicking on OK button will resume the Scanning.
        // If you scanned the QR code for some website then you can open that website by clicking on the Visit button.

    }
}
