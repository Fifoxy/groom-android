package com.hufi.taxmanreader;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import com.google.common.base.Splitter;
import com.hufi.taxmanreader.utils.TaxmanUtils;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * Created by Pierre Defache on 12/12/2015.
 */
public class TaxmanReaderApplication extends Application {
    private static Context sContext;

    public void onCreate(){
        super.onCreate();
        sContext = getApplicationContext();

        if(TaxmanUtils.userConnected()){
            checkUser();
        }
    }

    public static Context getContext() {
        return sContext;
    }

    private void checkUser(){
        SharedPreferences prefs = TaxmanReaderApplication.getContext().getSharedPreferences(getString(R.string.yoshimi), Context.MODE_PRIVATE);

        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decode(getString(R.string.yoshimi_key), Base64.DEFAULT));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setVerificationKey(publicKey)
                    .setRequireExpirationTime()
                    .setExpectedAudience(getString(R.string.client_id))
                    .setExpectedIssuer(getString(R.string.yoshimi_issuer))
                    .build();

            JwtClaims claims = jwtConsumer.processToClaims(prefs.getString(getString(R.string.yoshimi_token), ""));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            e.printStackTrace();
        } catch (InvalidJwtException e) {
            prefs.edit().remove(getString(R.string.yoshimi_token)).apply();
            prefs.edit().remove(getString(R.string.access_token)).apply();
            Toast.makeText(TaxmanReaderApplication.getContext(), getString(R.string.expiration), Toast.LENGTH_LONG).show();
        }
    }
}