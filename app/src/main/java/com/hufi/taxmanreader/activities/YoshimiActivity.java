package com.hufi.taxmanreader.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.common.base.Splitter;
import com.hufi.taxmanreader.GroomApplication;
import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.model.User;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YoshimiActivity extends Activity {
    private String nonce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final WebView webView = new WebView(this);
        setContentView(webView);

        this.nonce = UUID.randomUUID().toString();

        final String authorizeUri = getString(R.string.authorize_endpoint) + "?client_id=" + getString(R.string.client_id)
                + "&redirect_uri=" + getString(R.string.redirect_uri) + "&scope=openid+profile+email&response_type=id_token+token" +
                "&nonce=" + this.nonce;

        webView.setWebViewClient(new YoshimiClient());
        CookieManager.getInstance().removeAllCookie();
        webView.loadUrl(authorizeUri);
    }

    public void onYoshimiReturn(Uri uri) {
            final Map<String, String> params = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(uri.getEncodedFragment());

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

            JwtClaims claims = jwtConsumer.processToClaims(params.get("id_token"));
            if (claims.getClaimValue("nonce").equals(this.nonce)) {
                GroomApplication.service.getUser("JWT " + params.get("id_token")).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (!response.body().isAdmin()) {
                            Toast.makeText(GroomApplication.getContext(), getString(R.string.no_admin), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            SharedPreferences prefs = GroomApplication.getContext().getSharedPreferences(getString(R.string.yoshimi), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(getString(R.string.yoshimi_token), params.get("id_token")).apply();
                            editor.putString(getString(R.string.access_token), params.get("access_token")).apply();
                            Toast.makeText(GroomApplication.getContext(), getString(R.string.valid_sign_in), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(GroomApplication.getContext(), getString(R.string.net_failure), Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(GroomApplication.getContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private class YoshimiClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onLoadResource(view, url);
            Uri current = Uri.parse(url);
            Uri redirect_uri = Uri.parse(getString(R.string.redirect_uri));
            if (current.getHost().equals(redirect_uri.getHost()) && current.getPath().equals(redirect_uri.getPath())) {
                onYoshimiReturn(current);
            }
        }
    }
}
