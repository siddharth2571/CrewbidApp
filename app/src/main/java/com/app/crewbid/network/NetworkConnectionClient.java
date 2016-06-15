package com.app.crewbid.network;

import android.util.Log;

import com.app.crewbid.interfaces.KeyInterface;
import com.app.crewbid.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

public class NetworkConnectionClient implements KeyInterface {
    private static final String TAG = NetworkConnectionClient.class
            .getSimpleName();
    public static final int GET = 0, POST = 1, DELETE = 2, MULTIPART = 3;

    private ClsNetworkResponse clsResponse;
    private String delimiter = "--";
    private String boundary = "SwA" + Long.toString(System.currentTimeMillis())
            + "SwA";

    public NetworkConnectionClient(ClsNetworkResponse clsResponse) {
        this.clsResponse = clsResponse;
    }

    public void execute(int method) {
        HttpURLConnection connection = null;
        String response = null;
        try {
            URL urlNetwork = new URL(clsResponse.getUrl());
            connection = (HttpURLConnection) urlNetwork.openConnection();
            connection.setReadTimeout(20000);
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // connection.setRequestProperty("Content-Type",
            // "application/json");
            connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            connection.setDoInput(true);

            switch (method) {
                case GET: {
                    connection.setRequestMethod("GET");
                    break;
                }

                case POST: {
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    OutputStream outputStream = connection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(outputStream, "UTF-8"));
                    // bufferedWriter
                    // .write(createJson(clsResponse.getHashMapParams()));

                    // String json = createJson(clsResponse.getHashMapParams());
                    // HashMap<String, Object> map = new HashMap<String, Object>(1);
                    // map.put("json", json);
                    // String req = getParamQuery(map);
                    // form type request
                    String req = getParamQuery(clsResponse.getHashMapParams());
                    bufferedWriter.write(req);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    break;
                }

                case MULTIPART: {
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + boundary);
                    OutputStream outputStream = connection.getOutputStream();
                    setMultipartData(outputStream, clsResponse.getHashMapParams());
                    outputStream.flush();
                    outputStream.close();
                    break;
                }
            }

            Log.e(TAG, "URL ?" + clsResponse.getUrl());
            connection.connect();
            String encoding = connection.getHeaderField("Content-Encoding");
            Utility.log(TAG, "Header-Encoding ?" + encoding);
            String contentEncoding = connection.getContentEncoding();
            Utility.log(TAG, "Content-Encoding ?" + contentEncoding);
            // response = decompress(connection.getInputStream());
            // Utility.log(TAG, "Response@" + response);

            // for non-gzip encoding
            InputStream inputStream = connection.getInputStream();
            response = convertStreamToString(inputStream);
            clsResponse.setSuccess(true);
            clsResponse.setResult_String(response);

            Utility.log(TAG, "Response@" + response);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            clsResponse.setSuccess(false);
            clsResponse
                    .setDispMessage("Something went wrong, please try again later");
        } catch (IOException e) {
            e.printStackTrace();
            clsResponse.setSuccess(false);
            clsResponse
                    .setDispMessage("Problem occured, please check your internet connection");
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    public void addFormPart(OutputStream os, String paramName, String value)
            throws Exception {
        writeParamData(os, paramName, value);
    }

    private void writeParamData(OutputStream os, String paramName, String value)
            throws Exception {
        os.write((delimiter + boundary + "\r\n").getBytes());
        os.write("Content-Type: text/plain\r\n".getBytes());
        os.write(("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n")
                .getBytes());
        os.write(("\r\n" + value + "\r\n").getBytes());
    }

    public void addFilePart(OutputStream os, String paramName, String fileName,
                            String filePath) throws Exception {
        File file = new File(Utility.filter(filePath));
        if (!file.exists()) {
            Log.e(TAG, "Multipart param: file not found at " + filePath);
            return;
        }

        os.write((delimiter + boundary + "\r\n").getBytes());
        os.write(("Content-Disposition: form-data; name=\"" + paramName
                + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
        os.write(("Content-Type: application/octet-stream\r\n").getBytes());
        os.write(("Content-Transfer-Encoding: binary\r\n").getBytes());
        os.write("\r\n".getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        int read = -1;
        byte[] buffer = new byte[1024];
        while ((read == fileInputStream.read(buffer))) {
            os.write(buffer, 0, read);
        }
        fileInputStream.close();
        os.write("\r\n".getBytes());
    }

    public static String decompress(InputStream is) throws IOException {
        GZIPInputStream gis = new GZIPInputStream(is);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[1024];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }
        gis.close();
        is.close();
        return string.toString();
    }

    @SuppressWarnings("unused")
    private static String convertStreamToString(InputStream is)
            throws IOException {
        // For non-gzip encoded stream
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private void setMultipartData(OutputStream os, HashMap<String, Object> map) {
        for (Entry<String, Object> set : map.entrySet()) {
            Object object = set.getValue();
            if (object instanceof File) {
                File file = (File) object;
                try {
                    addFilePart(os, set.getKey().toString(), file.getName(),
                            file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    addFormPart(os, set.getKey().toString(), set.getValue()
                            .toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String createJson(HashMap<String, Object> hashJsondata) {
        String json = null;
        try {
            JSONObject jsonObject = new JSONObject();

            for (Entry<String, Object> iterable_element : hashJsondata
                    .entrySet()) {
                String keyFromTable = iterable_element.getKey();
                if (hashJsondata.get(keyFromTable) == null
                        || hashJsondata.get(keyFromTable).toString().length() == 0) {
                    jsonObject.put(keyFromTable, "");
                } else {
                    jsonObject
                            .put(keyFromTable, hashJsondata.get(keyFromTable));
                }
            }
            json = jsonObject.toString();
            Utility.log(TAG, "Json:" + json);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getParamQuery(HashMap<String, Object> params)
            throws UnsupportedEncodingException {
        // for form base request param
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Entry<String, Object> sets : params.entrySet()) {
            sets.getKey();
            sets.getValue();
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(sets.getKey(), "UTF-8"));
            result.append("=");
            String value = sets.getValue() == null ? "" : sets.getValue()
                    .toString();
            result.append(URLEncoder.encode(value, "UTF-8"));
        }
        return result.toString();
    }

    // public void executeHttpMime() {
    // HttpURLConnection connection = null;
    // DataOutputStream outputStream = null;
    // InputStream inputStream = null;
    //
    // String twoHyphens = "--";
    // String boundary = "*****" + Long.toString(System.currentTimeMillis()) +
    // "*****";
    // String lineEnd = "\r\n";
    //
    // String result = "";
    //
    // int bytesRead, bytesAvailable, bufferSize;
    // byte[] buffer;
    // int maxBufferSize = 1 * 1024 * 1024;
    //
    // String[] q = filepath.split("/");
    // int idx = q.length - 1;
    //
    // try {
    // URL url = new URL(clsResponse.getUrl());
    // connection = (HttpURLConnection) url.openConnection();
    // connection.setDoInput(true);
    // connection.setDoOutput(true);
    // connection.setUseCaches(false);
    //
    // connection.setRequestMethod("POST");
    // connection.setRequestProperty("Connection", "Keep-Alive");
    // connection.setRequestProperty("User-Agent",
    // "Android Multipart HTTP Client 1.0");
    // connection.setRequestProperty("Content-Type",
    // "multipart/form-data; boundary=" + boundary);
    // outputStream = new DataOutputStream(connection.getOutputStream());
    //
    // if (clsResponse.getHashMapFiles() != null) {
    // for (Entry<String, File> iterable_element :
    // clsResponse.getHashMapFiles().entrySet()) {
    // String keyFromTable = iterable_element.getKey();
    // File file = iterable_element.getValue();
    // FileInputStream fileInputStream = new FileInputStream(file);
    // outputStream.writeBytes(twoHyphens + boundary + lineEnd);
    // outputStream.writeBytes("Content-Disposition: form-data; name=\""
    // + Utility.getFileName(file.getAbsolutePath()) + "\"; filename=\"" +
    // q[idx] + "\""
    // + lineEnd);
    // outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
    // outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
    // outputStream.writeBytes(lineEnd);
    // bytesAvailable = fileInputStream.available();
    // bufferSize = Math.min(bytesAvailable, maxBufferSize);
    // buffer = new byte[bufferSize];
    //
    // bytesRead = fileInputStream.read(buffer, 0, bufferSize);
    // while (bytesRead > 0) {
    // outputStream.write(buffer, 0, bufferSize);
    // bytesAvailable = fileInputStream.available();
    // bufferSize = Math.min(bytesAvailable, maxBufferSize);
    // bytesRead = fileInputStream.read(buffer, 0, bufferSize);
    // }
    // outputStream.writeBytes(lineEnd);
    // }
    // }
    //
    // // Upload POST Data
    // String[] posts = post.split("&");
    // int max = posts.length;
    // for (int i = 0; i < max; i++) {
    // outputStream.writeBytes(twoHyphens + boundary + lineEnd);
    // String[] kv = posts[i].split("=");
    // outputStream.writeBytes("Content-Disposition: form-data; name=\"" + kv[0]
    // + "\"" + lineEnd);
    // outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
    // outputStream.writeBytes(lineEnd);
    // outputStream.writeBytes(kv[1]);
    // outputStream.writeBytes(lineEnd);
    // }
    //
    // outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
    //
    // inputStream = connection.getInputStream();
    // result = this.convertStreamToString(inputStream);
    //
    // fileInputStream.close();
    // inputStream.close();
    // outputStream.flush();
    // outputStream.close();
    //
    // return result;
    // } catch (Exception e) {
    // Log.e("MultipartRequest", "Multipart Form Upload Error");
    // e.printStackTrace();
    // return "error";
    // }
    // }

    public ClsNetworkResponse getClsResponse() {
        return clsResponse;
    }

    public void setClsResponse(ClsNetworkResponse clsResponse) {
        this.clsResponse = clsResponse;
    }
}