package INT20H.task.utils;

import INT20H.task.utils.exceptions.FaceAPIException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.buf.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.*;

@Log4j2
public class FaceAPI {

    private String detectAPI = "https://api-us.faceplusplus.com/facepp/v3/detect";
    private String faceAnalyzeAPI = "https://api-us.faceplusplus.com/facepp/v3/face/analyze";

    public List<String> getFacesTokens(String key, String secret, String photoUrl) {
        List<String> tokens = new ArrayList<>();
        try {
            String response = RequestHelper.doPost(detectAPI, createParamsToDetectAPI(key, secret, photoUrl));
            JSONArray arr = getFaces(response);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject face = arr.getJSONObject(i);
                tokens.add(face.getString("face_token"));
            }
        } catch (Exception e) {
            log.error(e);
        }
        return tokens;
    }

    public List<String> getEmotionsByFaceTokens(String key, String secret, List<String> tokens) throws FaceAPIException {
        List<String> emotions = new ArrayList<>();
        try {
            if (tokens.size() > 5)
                throw new FaceAPIException("The number of tokens must be less than or equal to five!");
            String response = RequestHelper.doPost(faceAnalyzeAPI, createParamsToFaceAnalyzAPI(key, secret, tokens));
            JSONArray faces = getFaces(response);
            for (int i = 0; i < faces.length(); i++) {
                JSONObject emotion = faces.getJSONObject(i).getJSONObject("attributes").getJSONObject("emotion");
                Map<String, Double> mapped = toMap(emotion);
                String keyOfMax = Collections.max(mapped.entrySet(), Map.Entry.comparingByValue()).getKey();
                emotions.add(keyOfMax);
            }
        } catch (Exception e) {
            log.error(e);
        }
        return emotions;
    }

    private Map<String, String> createParamsToFaceAnalyzAPI(String key, String secret, List<String> tokens) {
        HashMap<String, String> map = new HashMap<>();
        try {
            map.put("api_key", key);
            map.put("api_secret", secret);
            map.put("face_tokens", StringUtils.join(tokens, ','));
            map.put("return_attributes", "emotion");
        } catch (Exception e) {
            log.error(e);
        }
        return map;
    }

    private Map<String, String> createParamsToDetectAPI(String key, String secret, String url) {
        HashMap<String, String> map = new HashMap<>();
        try {
            map.put("api_key", key);
            map.put("api_secret", secret);
            map.put("image_url", url);
        } catch (Exception e) {
            log.error(e);
        }
        return map;
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

}


