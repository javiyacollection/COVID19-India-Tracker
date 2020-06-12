package com.india.corona.service;

import com.india.corona.dao.CoronaDAO;
import com.india.corona.entity.Corona;
import net.bytebuddy.implementation.bytecode.Throw;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CoronaService {

    @Autowired
    private CoronaDAO dao;

    @Autowired
    @Qualifier("springsRestTemplate")
    private RestTemplate springRestTemplate;

    public Corona saveCorona(Corona corona) {
        return dao.save(corona);
    }


    public void updateCases() {
    	try {
            String response = tryWithRestTemplate();
//            response = tryWithHttpURLConnection();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response);
            JSONArray array = (JSONArray) jsonObject.get("statewise");
            //System.out.println(array.size());
            Corona[] corona = new Corona[array.size()];

            for (int i = 0; i < array.size(); i++) {
                JSONObject object2 = (JSONObject) array.get(i);
                Corona co = new Corona();


                String recovered = (String) object2.get("recovered");
                System.out.println(recovered);
                co.setRecovered((String) object2.get("recovered"));

                String active = (String) object2.get("active");
                System.out.println(active);
                co.setActive(active);

                String deaths = (String) object2.get("deaths");
                System.out.println(deaths);
                co.setDeaths(deaths);

                String confirmed = (String) object2.get("confirmed");
                System.out.println(confirmed);
                co.setConfirmed(confirmed);

                String state = (String) object2.get("state");
                System.out.println(state);
                co.setStateName(state);

                corona[i] = co;
//                saveCorona(co);

            }
            List<Corona> coronaList = Arrays.asList(corona);
            dao.saveAll(coronaList);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    private String tryWithRestTemplate() throws Exception {
        String responseObject = null;
        try {
            String url = "https://api.covid19india.org/data.json";
            URI uriObject = new URI(url);
            responseObject = springRestTemplate.getForObject(uriObject, String.class);
        } catch (HttpServerErrorException e) {

            String exceptionMesasge = e.getMessage();
            if(e.getResponseBodyAsString() != null){
                exceptionMesasge = e.getResponseBodyAsString();
            }
            throw new Exception(exceptionMesasge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseObject.toString();
    }

    private String tryWithHttpURLConnection() throws Exception {
		String response = new String();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("http://genproxy.corp.amdocs.com", 8080));
        URL url = new URL("https://api.covid19india.org/data.json".trim());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        System.out.println("Proxy? " + conn.usingProxy());

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            response = response.concat(output);

        }
		return response;
    }


    //	@Scheduled(cron = "* */30 * * * * ")
    public void updateNewCases() {
        updateCases();

    }


}
