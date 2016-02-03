package com.hufi.taxmanreader.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.hardware.camera2.*;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.TaxmanReaderApplication;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Pierre Defache on 13/12/2015.
 */
public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    private String cameraIDUsed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        mScannerView = new ZXingScannerView(getActivity());
        mScannerView.setAutoFocus(true);

        setupFormats();
        if(!setUpBackCamera()) cameraIDUsed = "0";
        return mScannerView;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.scanner));
        mScannerView.startCamera(Integer.valueOf(cameraIDUsed));
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        formats.add(BarcodeFormat.QR_CODE);

        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.turn_flash:
                if(mScannerView.getFlash()){
                    mScannerView.setFlash(false);
                    if(!mScannerView.getFlash()) item.setIcon(R.drawable.ic_action_flash);
                } else {
                    mScannerView.setFlash(true);
                    if(mScannerView.getFlash()) item.setIcon(R.drawable.ic_action_flash_light);
                }
                break;
            case R.id.swap_camera:
                changeCamera();
                mScannerView.stopCamera();
                mScannerView.startCamera(Integer.valueOf(cameraIDUsed));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleResult(Result result) {
        try {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decode(getString(R.string.public_key), Base64.DEFAULT));
            KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
            PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
            JsonWebSignature jws = new JsonWebSignature();
            jws.setCompactSerialization(result.getText());
            jws.setKey(publicKey);

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setVerificationKey(publicKey)
                    .build();

            JwtClaims jwtClaims = jwtConsumer.processToClaims(result.getText());
            launchResult(jwtClaims.getRawJson());
        } catch (InvalidJwtException ex) {
            launchResult("");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | JoseException ex) {
            Toast.makeText(TaxmanReaderApplication.getContext(), getString(R.string.wrong_QR_Code), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }


    private boolean changeCamera(){
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        try {
            for(String s : cameraManager.getCameraIdList()) {
                if (!s.equals(cameraIDUsed)) {
                    cameraIDUsed = s;
                    return true;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean setUpBackCamera(){
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        try {
            for(String s : cameraManager.getCameraIdList()) {
                CameraCharacteristics carac = cameraManager.getCameraCharacteristics(s);
                int cOrientation = carac.get(CameraCharacteristics.LENS_FACING);
                if(!(cOrientation == CameraCharacteristics.LENS_FACING_FRONT)){
                    cameraIDUsed = s;
                    return true;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void launchResult(String jsonResult){
        ResultFragment fragment = ResultFragment.newInstance(jsonResult);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.scanner_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
