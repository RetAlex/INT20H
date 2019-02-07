package INT20H.task.services._interfaces;

import java.util.List;

public interface FacePlusPlusService {
    List<String> getFaceTokensByUrl(String url);

    public List<String> getEmoutionsByTokens(List<String> tokens);
}
