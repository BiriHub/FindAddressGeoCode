package com.classes;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;

public class GeoCoding {

    //protocollo applicativo utilizzato
    private final String protocol="https:"; // HTTP + SSL

    //prefisso
    private final String urlString;

    // YOUR API KEY
    private final String apiKey = "&key=YOUR_API_KEY";

    private boolean result = false;
    private String url;

    public GeoCoding() {
        urlString=protocol+"//maps.googleapis.com/maps/api/geocode/xml?latlng=";
    }

    private boolean requestElevation(double latitude,double longitude,String filename) {

        URL server;
        HttpsURLConnection service;
        BufferedReader input;
        BufferedWriter output;
        String line;
        String location;
        int status;

        try {
            location = latitude +","+ longitude;

            url = urlString + URLEncoder.encode(location, "UTF-8") + apiKey; // costruzione dello URL di interrogazione del servizio
            server = new URL(url);
            service = (HttpsURLConnection)server.openConnection();
            service.setRequestProperty("Host", "maps.googleapis.com"); // impostazione header richiesta: host interrogato
            service.setRequestProperty("Accept", "application/xml"); // impostazione header richiesta: formato risposta (XML)
            service.setRequestProperty("Accept-Charset", "UTF-8"); // impostazione header richiesta: codifica risposta (UTF-8)
            service.setRequestMethod("GET"); // impostazione metodo di richiesta GET
            service.setDoInput(true); // attivazione ricezione
            service.connect(); // connessione al servizio

            status = service.getResponseCode(); // verifica stato risposta

            if (status != 200) {
                return false; // errore
            }


            // apertura stream di ricezione da risorsa web


            input = new BufferedReader(new InputStreamReader(service.getInputStream(), "UTF-8"));

            // apertura stream per scrittura su file
            output = new BufferedWriter(new FileWriter(filename));
            // ciclo di lettura da web e scrittura su file
            while ((line = input.readLine()) != null) {
                output.write(line); output.newLine();
            }
            input.close(); output.close();
            result = true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static void main(String[] args) {

        // variables
        double latitude, longitude;
        String filename;

        //coordinates
        //latitude = LATITUDE VALUE ;
        latitude=10;

        //longitude = LONGITUDE VALUE ;
        longitude=-90.961452;

        //file su cui viene salvata la richiesta inviata dal Web server
        filename="src/com/outputfile/risposta.xml";

        //creo un oggetto
        GeoCoding elevation = new GeoCoding();

        boolean requestAnswer= elevation.requestElevation(latitude, longitude,filename);
        if (requestAnswer)
            System.out.println("Risposta salvata nel file '"+filename+"'.");
        else
            System.out.println("Errore interrogazione servizio.");
    }
}