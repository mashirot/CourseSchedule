package ski.mashiro.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

/**
 * @author MashiroT
 */
public class JsonUtils {

    public static <T> List<T> trans2List(ObjectMapper objectMapper, String originJson, Class<T> clazz) throws JsonProcessingException {
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return objectMapper.readValue(originJson, collectionType);
    }
}
