package com.simmurgh.barcodeparcer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseBarcode {

    public String parseResult(String barcode){
        String searchLink = ("https://www.google.com/search?q="+barcode+"&rct=j");                                  //generate a valid google search link

        FetchURL fetch = new FetchURL();                    //obtain the content of google search in form of string
        fetch.Run(searchLink);
        String google = fetch.getOutput();

        if (google.indexOf("'','',event)\">")==-1){         //this means there are no google results
            return "error";
        }
        google = google.substring(google.indexOf("'','',event)\">")+14,google.lastIndexOf("'','',event)\">")+14);   //Trim the useless parts of text

        google = google.replaceAll("<.*?event", "");        //this is useful
        google = google.replaceAll("[^a-zA-Z0-9\\s]", " "); //remove all non charnumerics
        google = google.toLowerCase();                      //turn everything to lower cases
        google = google.trim().replaceAll(" +", " ");       //remove excess spaces
        google = google.replaceAll("amazon", "");           //remove popular shopping site names, can be useful

        Map<String, Integer>wordCount = new HashMap<String, Integer>(); //create a map of words versus its frequency in text
        for (String seq : google.split("\\s+")) {
            Integer count = wordCount.get(seq);
            wordCount.put(seq, count == null ? 1 : count + 1);
        }
        List<String> method=mostOften(wordCount, 5);
        String intres= method.get(0)+ " "+method.get(1)+ " "+method.get(2)+ " "+method.get(3)+ " "+method.get(4)+ " ";

        String result=optSequence(intres, google);          //try to obtain a reasonable sequence of words
        result=result.replaceAll(barcode , "");             //remove barcode number itself
        result = result.trim().replaceAll(" +", " ");       //remove excess spaces again

        return result;
    }

    public static List<String> mostOften(Map<String, Integer> m, int k){    //find the most frequent words in the string
        List<MyWord> l = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : m.entrySet())
            l.add(new MyWord(entry.getKey(), entry.getValue()));

        Collections.sort(l);
        List<String> list = new ArrayList<>();
        for(MyWord w : l.subList(0, k))
            list.add(w.word);
        return list;
    }

    public String optSequence(final String top, final String original) {    //an attempt to obtain a reasonable word sequence of the result
        String[] split = top.split("\\s+");
        int size = split.length;
        String outSeq = "";

        int[] index = new int[size];
        //find indexes of first occasions of top words
        for (int i = 0; i < size; i = i + 1) {
            index[i] = original.indexOf(split[i]);
        }
        ///now sort the top word collection accordingly
        for (int j = 0; j < index.length; j++) {
            for (int k = 0; k < index.length; k++) {
                if (index[j] < index[k]) {
                    int buffer = index[j];
                    index[j] = index[k];
                    index[k] = buffer;
                    //now same manipulations to string
                    String stringBuffer = split[j];
                    split[j] = split[k];
                    split[k] = stringBuffer;
                }
            }
        }
        //now shape it as string and return
        for (int i = 0; i < size; i = i + 1) {
            outSeq = outSeq + split[i] + " ";
        }
        return outSeq;
    }
}

