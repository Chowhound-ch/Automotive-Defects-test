package per.chowhound.hfut.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.entity.StringEntity;
import per.chowhound.hfut.domain.File;

import java.io.UnsupportedEncodingException;

public class DefectUtils {

    public static JsonNode doRecognize(File file, String model) {
        JsonNode node = null;
        try {
            node = HttpUtil.doPostJson("http://chowhound-pc.com:8888/defect", new StringEntity("{\"img_url\":\"" + file.getUrl() + "\",\"model_url\":\"" + model + "\"}"));
//            node = HttpUtil.doPostJson("http://127.0.0.1:8888/defect", new StringEntity("{\"img_url\":\"" + file.getUrl() + "\",\"model_url\":\"" + model + "\"}"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (node.get("code") == null || node.get("code").asInt() != 0) {
            return null;
        }
        return node;
    }
}
