package org.abstractj.goose;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText responsePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responsePanel = (EditText) findViewById(R.id.response_panel);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.server_url);

        Button button = (Button) findViewById(R.id.send_request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(radioButtonId);
                testCertificate(radioButton.getText().toString());
            }
        });
    }

    private void testCertificate(String url) {

        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream pemInputStream = assetManager.open("random.org.pem");
            new HttpRequestTask(pemInputStream, new HttpRequestTaskCallback()).execute(url);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    "an error occurred while reading pem file", Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage(), e);
        }

    }

    private final class HttpRequestTaskCallback implements Callback<Void> {

        @Override
        public void onSuccess(Void data) {
//            Toast.makeText(getApplicationContext(),
//                    "Seems the request works", Toast.LENGTH_LONG).show();
            responsePanel.setText("Seems the request works");
        }

        @Override
        public void onFailure(Exception e) {
//            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            responsePanel.setText(e.getMessage());
        }

    }

}
