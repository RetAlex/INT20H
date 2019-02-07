package INT20H.task.services.impl;

import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.utils.FaceAPI;

import java.util.List;

public class FacePlusPlusImp implements FacePlusPlusService {

    private String key = "JNsr371qG2YY0jYB8MLs5M_E9QYsDOt4";
    private String secret = "PO04I-3jZxB1BbBeWc6VqQxhmNCFjJFZ";

    @Override
    public List<String> getFaceTokensByUrl(String url) {
        List<String> tokens = null;
        try {
            FaceAPI api = new FaceAPI();
            tokens = api.getFacesTokens(key, secret, url);
        } catch (Exception e) {
        }
        return tokens;
    }

    @Override
    public List<String> getEmoutionsByTokens(List<String> tokens) {
        List<String> emotions = null;
        try {
            FaceAPI api = new FaceAPI();
            emotions = api.getEmotionsByFaceTokens(key, secret, tokens);
        } catch (Exception e) {
        }
        return emotions;
    }
}
