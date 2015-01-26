package org.abstractj.goose;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by abstractj on 1/27/15.
 */
public class AeroGearTrustManager implements X509TrustManager {

    public static final String CERTIFICATE_TYPE = "X.509";
    private final String pinningPublicKey;

    public AeroGearTrustManager(InputStream certificate) {
        this.pinningPublicKey = initialize(certificate);
    }

    public String initialize(InputStream certificate){

        String encodedPublicKey = null;

        try {
            CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE);
            Certificate ca = cf.generateCertificate(certificate);
            encodedPublicKey = new BigInteger(1, ca.getPublicKey().getEncoded()).toString(16);

        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return encodedPublicKey;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        validateServer(chain, authType);

        validatePin(chain, pinningPublicKey);
    }


    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    private void validateServer(X509Certificate[] chain, String authType) throws CertificateException {

        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);

            for (TrustManager trustManager : tmf.getTrustManagers()) {
                ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
            }
        } catch (Exception e) {
            throw new CertificateException(e);
        }
    }

    private void validatePin(X509Certificate[] chain, String pinningPublicKey) throws CertificateException {

        RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
        String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);

        final boolean expected = pinningPublicKey.equalsIgnoreCase(encoded);
        if (!expected) {
            throw new CertificateException("checkServerTrusted: Expected public key: "
                    + pinningPublicKey + ", got public key:" + encoded);
        }
    }
}
