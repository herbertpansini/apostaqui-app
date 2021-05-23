package com.pancini.herbert.apostaqui;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONParser {
    static final int CONNECT_TIMEOUT = 30000;
    static final int READ_TIMEOUT = 30000;

    static final String METHOD_GET = "GET";
    static final String METHOD_POST = "POST";
    static final String METHOD_PUT = "PUT";
    static final String METHOD_DELETE = "DELETE";

    private final String TAG_RESPONSE = "response";

    // constructor
    public JSONParser() {}

    // Get JSON from Url
    public JSONObject getJSONFromUrl(String uri) {
        String result = "";
        JSONObject jObj = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(uri);
            InputStream is;

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(METHOD_GET);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = urlConnection.getInputStream();
            } else {
                is = urlConnection.getErrorStream();
            }
            result = convertInputStreamToString(is);
            is.close();
            urlConnection.disconnect();
            jObj = this.convertJSONObject(result);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            Log.e("JSON get", "Error JSON get from uri " + e.toString());
        }

        return jObj;
    }

    // Post JSON from Url and Data
    public JSONObject postJSONFromUrlDataObject(String uri, String jsonObject) {
        String result = "";
        JSONObject jObj = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(uri);
            InputStream is;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(METHOD_POST);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setRequestProperty( "Content-Type", "application/json" );
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonObject);
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = urlConnection.getInputStream();
            } else {
                is = urlConnection.getErrorStream();
            }
            result = convertInputStreamToString(is);
            is.close();
            urlConnection.disconnect();
            jObj = this.convertJSONObject(result);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            Log.e("JSON post", "Error JSON put from uri json object " + e.toString());
        }

        return jObj;
    }

    // Post JSON from Url and Data
    public int postJSONFromUrlData(String uri, String jsonObject) {
        String result = "";
        JSONObject jObj = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(uri);
            InputStream is;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(METHOD_POST);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setRequestProperty( "Content-Type", "application/json" );
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonObject);
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = urlConnection.getInputStream();
            } else {
                is = urlConnection.getErrorStream();
            }
            result = convertInputStreamToString(is);
            is.close();
            jObj = this.convertJSONObject(result);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        }  catch (IOException e) {
            Log.e("JSON post", "Error JSON post from uri data " + e.toString());
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return getResponse(jObj);
    }

    // Put JSON from Url and Data
    public int putJSONFromUrlData(String uri, String jsonObject) {
        String result = "";
        JSONObject jObj = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(uri);
            InputStream is;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(METHOD_PUT);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setRequestProperty( "Content-Type", "application/json" );
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonObject);
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = urlConnection.getInputStream();
            } else {
                is = urlConnection.getErrorStream();
            }
            result = convertInputStreamToString(is);
            is.close();
            jObj = this.convertJSONObject(result);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        }  catch (IOException e) {
            Log.e("JSON put", "Error JSON put from uri data " + e.toString());
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return getResponse(jObj);
    }

    // Delete JSON from Url
    public int deleteJSONFromUrl(String uri) {
        String result = "";
        JSONObject jObj = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(uri);
            InputStream is;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(METHOD_DELETE);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = urlConnection.getInputStream();
            } else {
                is = urlConnection.getErrorStream();
            }
            result = convertInputStreamToString(is);
            is.close();
            jObj = this.convertJSONObject(result);
        } catch (MalformedURLException e) {
            //e.printStackTrace();
        }  catch (IOException e) {
            Log.e("JSON delete", "Error JSON delete from uri " + e.toString());
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return getResponse(jObj);
    }

    private String convertInputStreamToString(InputStream is){
        StringBuffer buffer = new StringBuffer();
        try{
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while ((linha = br.readLine()) != null){
                buffer.append(linha);
            }

            br.close();
        }catch(IOException e){
            Log.e("InputStream Parser", "Error convert input stream to String " + e.toString());
        }

        return buffer.toString();
    }

    private JSONObject convertJSONObject(String result) {
        //try parse the string to a JSON object
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jsonObj;
    }

    public int getResponse(JSONObject jsonObject) {
        int response = 0;
        try {
            // Getting JSON Object
            response = jsonObject.getInt(TAG_RESPONSE);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return response;
    }
}