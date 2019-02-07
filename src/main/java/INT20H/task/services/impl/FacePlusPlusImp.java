package INT20H.task.services.impl;

import INT20H.task.services._interfaces.FacePlusPlusService;
import INT20H.task.utils.FaceAPI;

import java.util.List;

public class FacePlusPlusImp implements FacePlusPlusService {

    private String key = "JNsr371qG2YY0jYB8MLs5M_E9QYsDOt4";
    private String secret = "PO04I-3jZxB1BbBeWc6VqQxhmNCFjJFZ";


    private List<String> getFaceTokensByUrl(String url) {
        List<String> tokens = null;
        try {
            FaceAPI api = new FaceAPI();
            tokens = api.getFacesTokens(key, secret, url);
        } catch (Exception e) {
        }
        return tokens;
    }


    private List<String> getEmoutionsByTokens(List<String> tokens) {
        List<String> emotions = null;
        try {
            FaceAPI api = new FaceAPI();
            emotions = api.getEmotionsByFaceTokens(key, secret, tokens);
        } catch (Exception e) {
        }
        return emotions;
    }

    @Override
    public List<String> getEmotionsByUrl(String url) {
        List<String> emotions = null;
        try {
            List<String> tokens = getFaceTokensByUrl(url);
            emotions = getEmoutionsByTokens(tokens);
        } catch (Exception e) {

        }
        return emotions;
    }
}
