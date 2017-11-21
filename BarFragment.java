package io.futurebound.manageit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
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

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


public class BarFragment extends Fragment implements ZXingScannerView.ResultHandler {
    //The constant REQUEST_CAMERA is used while getting the permissions from the user to use the camera.
    // ZXingScannerView provides the view to scan the QR code and Bar code as shown below.
    private  static  final int REQUEST_CAMERA=1;
    private  ZXingScannerView mScannerView;

    public BarFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View root= inflater.inflate(R.layout.fragment_bar, container, false);
        //View root= inflater.inflate(mScannerView,container,false);

        mScannerView = new ZXingScannerView(getContext());

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if(currentapiVersion >= android.os.Build.VERSION_CODES.M){
            if(checkPermision()){
                //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

            }
            else{
                requestPermission();
            }

        }
        return mScannerView;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, REQUEST_CAMERA);
    }

    private boolean checkPermision() {
        return (ContextCompat.checkSelfPermission(getContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }
    public  void  onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case  REQUEST_CAMERA:
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(getContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(getContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_SHORT).show();
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
        new android.support.v7.app.AlertDialog.Builder(getContext())
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
                    mScannerView = new ZXingScannerView(getContext());
                    //scanner = new ZXingScannerView(this);
                    //setContentView(R. layout.activity_bar_code_reader);
                   //return mScannerView;
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

    @Override
    public void handleResult(Result rawResult) {
        final String result = rawResult.getText();
        //final String result = rawResult.getBarcodeFormat().toString();
        //Log.d ("QRCodeScanner",rawResult.getText());
        //Log.d("QRCodeScanner",rawResult.getBarcodeFormat().toString());
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // mScannerView.resumeCameraPreview(barCodeReaderActivity.this);

                //final Bundle x= getIntent().getExtras();
                final Bundle x= getActivity().getIntent().getExtras();
                final Bundle c= new Bundle();
                //int  update= x.getInt("toUpdate");
                int add= x.getInt("toScan");






                //if(x.get("toUpdate").equals(R.id.btnUpdateProduct)) {
                if(add== 7000) {
                    Intent a= new Intent(getContext(),UpdateProductActivity.class);
                    c.putString("result",result);
                    a.putExtras(c);
                    startActivity(a);
                    getActivity().finish();
                }
                else if (add==6000)  {
                    Intent n = new Intent(getContext(),deleteProductActivity.class);
                    c.putString("result", result);
                    n.putExtras(c);
                    startActivity(n);
                    getActivity().finish();
                }


                else if(add==5000){
                    Intent navigate = new Intent(getContext(),productsDetailActivity.class);
                    c.putString("result",result);
                    navigate.putExtras(c);
                    startActivity(navigate);
                    getActivity().finish();
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

