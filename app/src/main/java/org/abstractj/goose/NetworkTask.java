package org.abstractj.goose;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by abstractj on 1/26/15.
 */
public class NetworkTask extends AsyncTask<Void, Void, Void> {

    private SSLContext context;

    public NetworkTask(SSLContext context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... params) {
        //    // Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = null;

        try {
            url = new URL("https://192.168.59.103:8443/scarecrow/hello/bruno");

            HttpsURLConnection urlConnection =
                    (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            InputStream in = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

        return null;
    }
}
