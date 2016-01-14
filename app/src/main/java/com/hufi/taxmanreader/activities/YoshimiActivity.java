package com.hufi.taxmanreader.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.google.common.base.Splitter;
import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.TaxmanReaderApplication;

import org.jose4j.jws.JsonWebSignature;
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
import java.util.UUID;

/**
 * Created by hugo on 13/12/15.
 */

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
        CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {
                webView.loadUrl(authorizeUri);
            }
        });
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
                SharedPreferences prefs = TaxmanReaderApplication.getContext().getSharedPreferences(getString(R.string.yoshimi), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.yoshimi_token), params.get("id_token")).apply();
                editor.putString(getString(R.string.access_token), params.get("access_token")).apply();
                Toast.makeText(TaxmanReaderApplication.getContext(), getString(R.string.valid_sign_in), Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidJwtException e) {
            e.printStackTrace();
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
