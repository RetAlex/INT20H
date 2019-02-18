package INT20H.task.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

import static INT20H.task.resources.configuration.FlickrConfig.rootCacheDir;

@Log4j2
@Service
public class CacheUtils {

    public final static  String photoCacheDir = "photoCache";
    public final static  String emotionCacheDir = "emotionCache";

    public Object loadCacheFromFile(String cacheDir, TypeReference typeReference) {
        try {
            File dir = new File(rootCacheDir + cacheDir);
            String[] list = dir.list();
            if (!dir.exists() || list == null || list.length == 0) return null;
            Arrays.sort(list);
            File actualCache = new File(dir.getPath() + "/" + list[list.length - 1]);

            return new ObjectMapper().readValue(actualCache, typeReference);
        } catch (Exception e){
            log.error("Can not load Cache From File : \n" + e) ;
        }
        return null;
    }

    public void storeCache(Object cache, String cacheDir) {
        try {
            File dir = new File(rootCacheDir + cacheDir);
            if (!dir.exists() && !dir.mkdirs())
                throw new Exception("Can not create new dir with path + " + dir.getPath());

            int fileNum = 0;

            String[] list = dir.list();
            if (list != null && list.length > 0) {
                Arrays.sort(list);
                fileNum = Integer.valueOf(list[list.length - 1]) + 1;
            }

            File newCache = new File(dir.getPath() + "/" + fileNum);
            if (!newCache.createNewFile())
                throw new Exception("Can not create new file with path + " + newCache.getPath());

            FileWriter writer = new FileWriter(newCache);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            String str = new ObjectMapper().writeValueAsString(cache);

            bufferedWriter.write(str);
            bufferedWriter.flush();
            bufferedWriter.close();
            writer.close();
            log.info("Stored cache");

            if (fileNum != 0) {
                if(!new File(rootCacheDir + cacheDir + "/" + list[list.length - 1]).delete()) throw new Exception("Can not delete cache file!");
            }

            if(list != null && list.length > 2) {
                for(int i = 1; i < list.length; i++){
                    new File(rootCacheDir + cacheDir + "/" + list[i]).delete();
                }
            }
        } catch (Exception e){
            log.error("Can not store cache:\n " + e);
        }
    }
}
