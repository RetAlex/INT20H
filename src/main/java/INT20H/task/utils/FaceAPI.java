package INT20H.task.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.util.buf.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class FaceAPI {

    private String detectAPI = "https://api-us.faceplusplus.com/facepp/v3/detect";
    private String faceAnalyzeAPI = "https://api-us.faceplusplus.com/facepp/v3/face/analyze";

    public  List<String> getFacesTokens(String key, String secret, String photoUrl) {
        List<String> tokens = new ArrayList<>();
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("api_key", key);
            map.put("api_secret", secret);
            map.put("image_url", photoUrl);
            String response = RequestHelper.doPost(detectAPI, map);

            JSONArray arr = getFaces(response);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject face = arr.getJSONObject(i);
                tokens.add(face.getString("face_token"));
            }
        } catch (Exception e) {
        }
        return tokens;
    }

    public List<String> getEmotionsByFaceTokens(String key, String secret, List<String> tokens) {
        List<String> emotions = new ArrayList<>();
        try {
            Map<String, String> map = new HashMap<>();
            map.put("api_key", key);
            map.put("api_secret", secret);
            map.put("face_tokens", StringUtils.join(tokens, ','));
            map.put("return_attributes", "emotion");
            String response = RequestHelper.doPost(faceAnalyzeAPI, map);

            JSONArray faces = getFaces(response);
            for (int i = 0; i < faces.length(); i++) {
                JSONObject emotion = faces.getJSONObject(i).getJSONObject("attributes").getJSONObject("emotion");
                Map<String, Double> mapped = toMap(emotion);
                String keyOfMax = Collections.max(mapped.entrySet(), Map.Entry.comparingByValue()).getKey();
                emotions.add(keyOfMax);
            }
        } catch (Exception e) {

        }
        return emotions;
    }

    private Map<String, Double> toMap(JSONObject object) {
        Iterator<String> nameItr = object.keys();
        Map<String, Double> outMap = new HashMap<String, Double>();
        while (nameItr.hasNext()) {
            String name = nameItr.next();
            outMap.put(name, object.getDouble(name));
        }
        return outMap;
    }

    private JSONArray getFaces(String response) {
        return new JSONObject(response).getJSONArray("faces");
    }

    private String mapToString(HashMap<String, String> map) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(map).toString();
    }
}


