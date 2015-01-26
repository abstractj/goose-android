package org.abstractj.goose;

import android.os.AsyncTask;

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
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;

    }
}
