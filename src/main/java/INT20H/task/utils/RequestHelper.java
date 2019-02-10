package INT20H.task.utils;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RequestHelper {

    static final private int BUFFERSIZE = 1048576;

    private static HttpClient httpClient;

    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            SSLParameters parameters = new SSLParameters();
            parameters.setProtocols(new String[]{"TLSv1.2"});
            httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMinutes(1))
                    .sslParameters(parameters)
                    .build();
        }
        return httpClient;
    }

    public static String simplePost(String url, String params, String... headers) {
        try {
            HttpRequest.Builder request = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .timeout(Duration.ofMinutes(1))
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .uri(new URI(url));
            if (headers != null && headers.length != 0) request.headers(headers);
            HttpResponse<String> stringHttpResponse = getHttpClient().sendAsync(request.build(), HttpResponse.BodyHandlers.ofString()).get();
            System.out.println(stringHttpResponse.statusCode());
            return stringHttpResponse.body();
        } catch (URISyntaxException ignored) {
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String post(String ref, Map<String, String> args) {
        return doPost(ref, args);
    }

    public static String doPost(String ref, Map<String, String> args) {
        String resultString = null;
        PostParameters params = new PostParameters();
        URL url = null;
        HttpURLConnection urlConn = null;
        try {
            url = new URL(ref);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("connection", "keep-alive");
            urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + params.boundaryString());

            MultipartEntity reqEntity = params.getMultiPart();
            args.keySet().forEach(o -> {
                try {
                    reqEntity.addPart(o, new StringBody(args.get(o)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            reqEntity.writeTo(urlConn.getOutputStream());
            if (urlConn.getResponseCode() == 200)
                resultString = readString(urlConn.getInputStream());
            else
                resultString = readString(urlConn.getErrorStream());
        } catch (Exception e) {

        }
        return resultString;
    }

    private static String readString(InputStream is) {
        StringBuffer rst = new StringBuffer();

        byte[] buffer = new byte[BUFFERSIZE];
        int len = 0;

        try {
            while ((len = is.read(buffer)) > 0)
                for (int i = 0; i < len; ++i)
                    rst.append((char) buffer[i]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rst.toString();
    }

    public static String simpleDelete(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).timeout(Duration.ofMinutes(1)).DELETE().uri(new URI(url)).build();
            return getHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (URISyntaxException ignored) {
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String GET_Request(String url, String... headers) {
        try {
            HttpRequest.Builder request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).timeout(Duration.ofMinutes(1)).GET().uri(new URI(url));
            if (headers != null && headers.length != 0) request.headers(headers);
            return getHttpClient().send(request.build(), HttpResponse.BodyHandlers.ofString()).body();
        } catch (URISyntaxException ignored) {
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String DELETE_Request(String url, String... headers) {
        try {
            HttpRequest.Builder request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).timeout(Duration.ofMinutes(1)).DELETE().uri(new URI(url));
            if (headers != null && headers.length != 0) request.headers(headers);
            return getHttpClient().send(request.build(), HttpResponse.BodyHandlers.ofString()).body();
        } catch (URISyntaxException ignored) {
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}