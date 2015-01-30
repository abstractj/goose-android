package org.abstractj.goose;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by abstractj on 1/26/15.
 */
public class HttpRequestTask extends AsyncTask<Void, Void, InputStream> {

    private static final String TAG = HttpRequestTask.class.getSimpleName();

    private InputStream certificate;

    public HttpRequestTask(InputStream certificate) {
        this.certificate = certificate;
    }

    @Override
    protected InputStream doInBackground(Void... params) {

        TrustManager tm[] = { new AeroGearTrustManager(certificate) };

        SSLContext context;
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, tm, null);

            URL url = new URL("https://www.random.org");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(context.getSocketFactory());

            InputStreamReader instream = new InputStreamReader(connection.getInputStream());
            StreamTokenizer tokenizer = new StreamTokenizer(instream);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (KeyManagementException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;

    }
}
