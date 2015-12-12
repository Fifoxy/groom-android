package com.hufi.taxmanreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hufi.taxmanreader.utils.Constants;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.openssl.PEMParser;


public class ScanActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    private Key key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        mScannerView.setAutoFocus(true);
        setupFormats();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        String key = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEASezeQmkN3NVL6PcfGXv0lkftLh0" +
                "8tf70W+j7KwmWO5y1rygtEODKb5dyvj/63NEwzon3zLOAwqOTPwxC59pyQ==";

        try {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decode(key, Base64.DEFAULT));
            KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
            PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
            JsonWebSignature jws = new JsonWebSignature();
            jws.setCompactSerialization(rawResult.getText());
            jws.setKey(publicKey);
            if (jws.verifySignature()) {
                Log.v("TRY", "TRUST");
            }
            else {
                Log.v("TRY", "NOTRUST");
            }
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (JoseException e) {
            e.printStackTrace();
        }

        /*PEMParser pemParser = new PEMParser(new StringReader(key));
        try {
            SubjectPublicKeyInfo pKey = (SubjectPublicKeyInfo) pemParser.readObject();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(pKey.getEncoded());
            KeyFactory factory = KeyFactory.getInstance("ECDSA");
            PublicKey publicKey = factory.generatePublic(spec);
            JsonWebSignature jws = new JsonWebSignature();
            jws.setCompactSerialization(rawResult.getText());
            jws.setKey(publicKey);
            if (jws.verifySignature()) {
                Log.v("TRY", "TRUST");
            }
            else {
                Log.v("TRY", "NOTRUST");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (JoseException e) {
            e.printStackTrace();
        }*/

        /*String key = "-----BEGIN PUBLIC KEY-----\n" +
                "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAENnhMXfP+BADBNKz+RJaEKU5y1ca08StD\n" +
                "ZkG/B1EVVVcHtFp6sspt/R6oy+Tp1D9jObMXJt603nDgGiJUanzGrQ==\n" +
                "-----END PUBLIC KEY-----";

        PEMParser pemParser = new PEMParser(new StringReader(key));
        try {
            SubjectPublicKeyInfo pKey = (SubjectPublicKeyInfo) pemParser.readObject();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(pKey.getEncoded());
            KeyFactory factory = KeyFactory.getInstance("ECDSA");
            PublicKey publicKey = factory.generatePublic(spec);
            JsonWebSignature jws = new JsonWebSignature();
            jws.setCompactSerialization(rawResult.getText());
            jws.setKey(publicKey);
            if (jws.verifySignature()) {
                Log.v("TRY", "TRUST");
            }
            else {
                Log.v("TRY", "NOTRUST");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (JoseException e) {
            e.printStackTrace();
        }*/

     /*   try {
            Jwts.parser().setSigningKey(getString(R.string.public_key)).parseClaimsJwt(rawResult.getText());
            Log.v("TRY", "TRUST");
            //OK, we can trust this JWT

        } catch (SignatureException e) {

            //don't trust the JWT!
            Log.v("CATCH", "DON'T TRUST");
        } */



        /*try {
            String key = "MHQCAQEEIDzESrZFmTaOozu2NyiS8LMZGqkHfpSOoI/qA9Lw+d4NoAcGBSuBBAAK\n" +
                    "oUQDQgAE7kIqoSQzC/UUXdFdQ9Xvu1Lri7pFfd7xDbQWhSqHaDtj+XY36Z1Cznun\n" +
                    "GDxlA0AavdVDuoGXxNQPIed3FxPE3Q==";

            Base64.decode(key, Base64.DEFAULT);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(key.getBytes());
            KeyFactory factory = KeyFactory.getInstance("ECDSA");
            PublicKey publicKey = factory.generatePublic(spec);
            // Jwts.parser().setSigningKey(publicKey).parseClaimsJwt(rawResult.getText());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }*/

        Log.v("Scan result", rawResult.getText()); // Prints scan results
        Log.v("Scan format", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        formats.add(BarcodeFormat.QR_CODE);

        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }
}
