package INT20H.task.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceAPI {

    private String detectAPI = "https://api-us.faceplusplus.com/facepp/v3/detect";

    public List<String> getFacesTokens(String key, String secret, String photoUrl) {
        List<String> tokens = new ArrayList<>();
        try {
            String answer = makePrivateRequest(detectAPI, key, secret, photoUrl);
            System.out.println(answer);
        } catch (Exception e) {
        }
        return tokens;
    }


    private String makePrivateRequest(String url, String key, String secret, String photoUrl) {
        String response = "";
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("api_key", key);
            map.put("api_secret", secret);
            map.put("image_url", photoUrl);
            map.put("face_rectangle", "50,50,50,50");
            // -------------------------------------
            String request = mapToString(map);
            System.out.println(request);
            response = RequestHelper.simplePost(url,request);
        } catch (Exception e) {

        }
        return response;
    }


    private String mapToString(HashMap<String,String> map)
    {
        StringBuilder postData = new StringBuilder();
        try {
            for (HashMap.Entry<String, String> param : map.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
        }catch (Exception e )
        {

        }
        return  postData.toString();
    }
}


