package com.vsiravara.kafka;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class KafkaBrokerCertificateExplorer {

public static void main(String[] args) {
        xmain();
    }

    public static void xmain()   {
            // Kafka broker address; bind 9092 in container to 9092 on host, see compose file
            String kafkaBrokerAddress = "localhost"; 
            int kafkaBrokerPort = 9092; // Default SSL port for Kafka brokers
    
            try {
                // Disable SSL certificate validation
    
    
                SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(kafkaBrokerAddress, kafkaBrokerPort);
                sslSocket.startHandshake();
    
                Certificate[] certificates = sslSocket.getSession().getPeerCertificates();
                X509Certificate brokerCertificate = (X509Certificate) certificates[0];
                Date expiryDate = brokerCertificate.getNotAfter();
                System.out.println("Expiry Date of Broker Certificate: " + expiryDate);
                long remainingDays = TimeUnit.DAYS.convert(expiryDate.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
                System.out.println("Remaining days to expiry: " + remainingDays);
                sslSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } 
    }
}


