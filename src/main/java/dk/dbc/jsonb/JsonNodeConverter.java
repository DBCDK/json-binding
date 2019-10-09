/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.jsonb;

import com.fasterxml.jackson.databind.JsonNode;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;

/**
 * Handles mapping from/to arbitrary JSON to/from PostgreSQL JSON type
 */
@Converter
public class JsonNodeConverter implements AttributeConverter<JsonNode, PGobject> {
    private static final JSONBContext JSONB_CONTEXT = new JSONBContext();

    @Override
    public PGobject convertToDatabaseColumn(JsonNode content) throws IllegalStateException {
        final PGobject pgObject = new PGobject();
        pgObject.setType("jsonb");
        try {
            if (content != null) {
                pgObject.setValue(content.toString());
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return pgObject;
    }

    @Override
    public JsonNode convertToEntityAttribute(PGobject pgObject) throws IllegalStateException {
        if (pgObject != null) {
            try {
                return JSONB_CONTEXT.unmarshall(pgObject.getValue(), JsonNode.class);
            } catch (JSONBException e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }
}

