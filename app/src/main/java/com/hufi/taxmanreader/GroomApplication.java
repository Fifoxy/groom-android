package com.hufi.taxmanreader;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import com.hufi.taxmanreader.utils.GroomUtils;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroomApplication extends Application {
    private static Context sContext;
    public static GroomService service;

    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        if (GroomUtils.userConnected()) {
            checkUser();
        }

        final SharedPreferences sharedPreferences = GroomApplication.getContext().getSharedPreferences(GroomApplication.getContext().getString(R.string.yoshimi), Context.MODE_PRIVATE);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Response response;

                final String token = sharedPreferences.getString(GroomApplication.getContext().getString(R.string.yoshimi_token), "");

                if (!token.isEmpty()) {
                    Request request = original.newBuilder()
                            .header("Authorization", "JWT " + token)
                            .build();
                    response = chain.proceed(request);
                } else {
                    response = chain.proceed(original);
                }

                return response;
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tickets.gala-isen.fr/api/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(GroomService.class);
    }

    public static Context getContext() {
        return sContext;
    }

    private void checkUser() {
        SharedPreferences prefs = GroomApplication.getContext().getSharedPreferences(getString(R.string.yoshimi), Context.MODE_PRIVATE);

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

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidJwtException e) {
            prefs.edit().remove(getString(R.string.yoshimi_token)).apply();
            prefs.edit().remove(getString(R.string.access_token)).apply();
            Toast.makeText(GroomApplication.getContext(), getString(R.string.expiration), Toast.LENGTH_LONG).show();
        }
    }
}