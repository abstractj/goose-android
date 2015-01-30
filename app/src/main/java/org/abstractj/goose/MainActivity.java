package org.abstractj.goose;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            testCertificate();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void testCertificate() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        AssetManager assetManager = getResources().getAssets();
        InputStream pemInputStream = assetManager.open("random.org.pem");

        // Tell the URLConnection to use a SocketFactory from our SSLContext
        new HttpRequestTask(pemInputStream, new HttpRequestTaskCallback()).execute();

    }

    private final class HttpRequestTaskCallback implements Callback<Void> {

        @Override
        public void onSuccess(Void data) {
            Toast.makeText(getApplicationContext(),
                    "Seems the request works", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}
