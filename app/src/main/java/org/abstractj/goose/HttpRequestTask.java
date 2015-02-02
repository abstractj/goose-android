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
public class HttpRequestTask extends AsyncTask<String, Void, InputStream> {

    private static final String TAG = HttpRequestTask.class.getSimpleName();

    private final InputStream certificate;
    private final Callback<Void> callback;

    private Exception exception;

    public HttpRequestTask(InputStream certificate, Callback<Void> callback) {
        this.certificate = certificate;
        this.callback = callback;
    }

    @Override
    protected InputStream doInBackground(String... urls) {

        TrustManager tm[] = { new AeroGearTrustManager(certificate) };

        SSLContext context;
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, tm, null);

            URL url = new URL(urls[0]);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(context.getSocketFactory());

            InputStreamReader instream = new InputStreamReader(connection.getInputStream());
            StreamTokenizer tokenizer = new StreamTokenizer(instream);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
            this.exception = e;
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            this.exception = e;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            this.exception = e;
        } catch (KeyManagementException e) {
            Log.e(TAG, e.getMessage(), e);
            this.exception = e;
        }

        return null;

    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
        super.onPostExecute(inputStream);
        if (exception == null) {
            callback.onSuccess(null);
        } else {
            callback.onFailure(exception);
        }
    }

}
