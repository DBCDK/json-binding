package dk.dbc.dataio.jsonb;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dbc.dataio.commons.utils.invariant.InvariantUtil;

import java.io.IOException;
import java.io.StringWriter;

/**
 * The JSONBContext class provides the client's entry point to the JSONB API with binding framework operations
 * unmarshal and marshal.
 * <p>
 * This class is thread safe.
 * </p>
 */
public class JSONBContext {
    private final ObjectMapper objectMapper;

    public JSONBContext() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Marshalls given value type into its corresponding JSON string representation
     * @param object object to marshall
     * @return JSON representation
     * @throws JSONBException if unable to marshall value type into its JSON representation
     */
    public String marshall(Object object) throws JSONBException {
        final StringWriter stringWriter = new StringWriter();
        try {
            objectMapper.writeValue(stringWriter, object);
        } catch (IOException e) {
            throw new JSONBException(String.format(
                    "Exception caught when trying to marshall %s object to JSON", object.getClass().getName()), e);
        }
        return stringWriter.toString();
    }

    /**
     * Unmarshalls JSON string into value type
     * @param json JSON representation of value type
     * @param tClass value type class
     * @param <T> type parameter
     * @return value type instance
     * @throws JSONBException if unable to unmarshall JSON representation to value type
     */
    public <T> T unmarshall(String json, Class<T> tClass) throws JSONBException {
        InvariantUtil.checkNotNullOrThrow(tClass, "tClass");
        try {
            return objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            throw new JSONBException(String.format(
                    "Exception caught when trying to unmarshall JSON %s to %s object", json, tClass.getName()), e);
        }
    }
}
