package com.simmurgh.barcodeparcer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
public class FetchURL {

    private String output;
    private String url;

    public FetchURL()
    {
        output = "";
    }

    public String getOutput()
    {
        return output;
    }

    public void Run(String u)
    {
        url = u;
        Thread t =  new Thread() {

            public void run() {

                URL textUrl;
                try {

                    textUrl = new URL(url);

                    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(textUrl.openStream()));

                    String StringBuffer;
                    String stringText = "";
                    while ((StringBuffer = bufferReader.readLine()) != null) {
                        stringText += StringBuffer;
                    }
                    bufferReader.close();

                    output = stringText;

                } catch (Exception e) {
                    // TODO Auto-generated catch block

                    output= e.toString();
                }

            }
        };

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
