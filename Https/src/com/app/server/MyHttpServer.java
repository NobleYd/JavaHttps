package com.app.server;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class MyHttpServer {

    public static String KeyStoreFilePath = "keystore/sever.jks";
    public static String KeyStorePassword = "keystorepswd";
    public static String KeyPassword = "password";

    private static String uri = "http://localhost/";
    private static String secure_uri = "https://localhost/";

    public static void start(int port, boolean secure) throws Exception {
        String uriString = uri;
        if (secure) {
            uriString = secure_uri;
        }
        URI uri = UriBuilder.fromUri(uriString).port(port).build();

        ResourceConfig conf = new ResourceConfig();
        conf.packages("com.app");

        if (secure) {
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(new FileInputStream(new File(KeyStoreFilePath)), KeyStorePassword.toCharArray());

            // 1: for sslContext
            // KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            // kmf.init(keyStore, KeyPassword.toCharArray());
            //
            // TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            // tmf.init(keyStore);
            //
            // SSLContext sslContext = SSLContext.getInstance("SSL");
            // sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            // or 2: for sslContext
            SSLContext sslContext = SslConfigurator.newInstance()//
                    // trustStore
                    .trustManagerFactoryAlgorithm(TrustManagerFactory.getDefaultAlgorithm())//
                    .trustStore(keyStore)//
                    .trustStorePassword(KeyStorePassword)//
                    // keyStore
                    .keyManagerFactoryAlgorithm(KeyManagerFactory.getDefaultAlgorithm()).keyStore(keyStore)//
                    .keyStore(keyStore)//
                    .keyStorePassword(KeyStorePassword)//
                    // keyPass
                    .keyPassword(KeyPassword)//
                    .securityProtocol("SSL")//
                    .createSSLContext();

            SSLEngineConfigurator sslEngineConf = new SSLEngineConfigurator(sslContext).setClientMode(false).setWantClientAuth(false);

            HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(uri, conf, secure, sslEngineConf);
            httpServer.start();
        } else {
            HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(uri, conf);
            httpServer.start();
        }
    }

    public static void main(String[] args) {

        try {
            MyHttpServer.start(443, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
