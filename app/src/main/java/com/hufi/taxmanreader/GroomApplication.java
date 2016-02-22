package com.hufi.taxmanreader;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import com.hufi.taxmanreader.model.Event;
import com.hufi.taxmanreader.model.Product;
import com.hufi.taxmanreader.realm.RealmEvent;
import com.hufi.taxmanreader.realm.RealmPlace;
import com.hufi.taxmanreader.realm.RealmProduct;
import com.hufi.taxmanreader.utils.TaxmanUtils;

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
import java.util.List;

import io.realm.Realm;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroomApplication extends Application {
    private static Context sContext;
    public static GroomService service;

    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        if (TaxmanUtils.userConnected()) {
            checkUser();
        }

        SharedPreferences sharedPreferences = GroomApplication.getContext().getSharedPreferences(GroomApplication.getContext().getString(R.string.yoshimi), Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString(GroomApplication.getContext().getString(R.string.yoshimi_token), "");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Response response;

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

        fetchEvents();
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

    private void fetchEvents() {
        service.getAllEvents().enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, retrofit2.Response<List<Event>> response) {
                Realm realm = Realm.getInstance(getContext());
                realm.beginTransaction();
                realm.where(RealmEvent.class).findAll().clear();
                realm.where(RealmProduct.class).findAll().clear();
                realm.where(RealmPlace.class).findAll().clear();

                for (Event event : response.body()) {
                    if (realm.where(RealmEvent.class).equalTo("slug", event.getSlug()).count() == 0) {
                        RealmPlace place = realm.where(RealmPlace.class).equalTo("id", event.getPlace_id()).findFirst();
                        if (place == null) {
                            place = realm.createObject(RealmPlace.class);
                            place.setId(event.getPlace().getId());
                            place.setName(event.getPlace().getName());
                            place.setAddress(event.getPlace().getAddress());
                        }

                        RealmEvent realmEvent = realm.createObject(RealmEvent.class);
                        realmEvent.setSlug(event.getSlug());
                        realmEvent.setName(event.getName());
                        realmEvent.setPlace(place);

                        for (Product product : event.getProducts()) {
                            if (realm.where(RealmProduct.class).equalTo("id", product.getId()).count() == 0) {
                                RealmProduct realmProduct = realm.createObject(RealmProduct.class);
                                realmProduct.setId(product.getId());
                                realmProduct.setName(product.getName());
                                realmProduct.setPrice(product.getPrice());
                                realmProduct.setEvent(realmEvent);
                            }
                        }
                    }
                }

                realm.commitTransaction();
                Toast.makeText(GroomApplication.getContext(), getString(R.string.sync_success), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable throwable) {
                Toast.makeText(GroomApplication.getContext(), getString(R.string.sync_failed), Toast.LENGTH_LONG).show();
            }
        });
    }
}