package com.app.client;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

public class MyHttpClient {

    public static String KeyStoreFilePath = "keystore/sever.jks";
    public static String KeyStorePassword = "keystorepswd";
    public static String KeyPassword = "password";

    public static String get(String uri, boolean secure) throws Exception {
        Configuration conf = null;
        if (secure) {
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(new FileInputStream(new File(KeyStoreFilePath)), KeyStorePassword.toCharArray());
            SSLContext sslContext = SslConfigurator.newInstance()//
                    // trustStore
                    // .trustManagerFactoryAlgorithm(TrustManagerFactory.getDefaultAlgorithm())//
                    // .trustStore(keyStore)//
                    // .trustStorePassword(KeyStorePassword)//
                    // keyStore
                    // .keyManagerFactoryAlgorithm(KeyManagerFactory.getDefaultAlgorithm()).keyStore(keyStore)//
                    // .keyStore(keyStore)//
                    // .keyStorePassword(KeyStorePassword)//
                    // keyPass
                    // .keyPassword(KeyPassword)//
                    // .securityProtocol("SSL")//
                    .createSSLContext();

            conf = JerseyClientBuilder.newBuilder().sslContext(sslContext).getConfiguration();
        } else {
            conf = JerseyClientBuilder.newBuilder().getConfiguration();
        }

        JerseyClient jerseyClient = JerseyClientBuilder.createClient(conf);

        Response response = jerseyClient.target(uri).request().accept(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == 200) {
            return response.readEntity(String.class);
        } else {
            return "Error status: " + response.getStatus();
        }
    }

    public static void main(String[] args) throws Exception {
        String ans = MyHttpClient.get("https://localhost:443/book/list", true);
        System.out.println(ans);

        String ans2 = MyHttpClient.get("https://localhost:443/book/list", true);
        System.out.println(ans2);
    }

}
