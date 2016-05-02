package com.simmurgh.barcodeparcer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.regex.Pattern;
import me.sudar.zxingorient.ZxingOrient;
import me.sudar.zxingorient.ZxingOrientResult;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        //////////////////////////launch ZXING barcode scanner
        ZxingOrient integrator = new ZxingOrient(ScanActivity.this);
        integrator.setInfo("Scan barcode of the product you want to buy");
        integrator.setIcon(R.drawable.barcode);
        integrator.initiateScan();
        //
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        ZxingOrientResult scanResult =
                ZxingOrient.parseActivityResult(requestCode, resultCode, intent);

        if ( scanResult.getContents() != null) {
            String result = intent.getStringExtra("SCAN_RESULT");

            if (Pattern.matches("[0-9]{1,13}", result)) {
                //scanned label was a barcode, now parse it
                String data = new ParseBarcode().parseResult(result);
                if (data=="error"){
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
                //return the parse result to parent activity
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",data);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            } else {
                //scanned label was probably QR bar, just return it as is with QR: tag infront
                String qr = "QR: " + result;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",qr);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        }
    }
}
