/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.jsonb;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class JsonNodeConverterTest {
    private final JsonNodeConverter converter = new JsonNodeConverter();
    private final JSONBContext jsonbContext = new JSONBContext();

    @Test
    public void convertToDatabaseColumn_contentIsNull() {
        final Object pgObject = converter.convertToDatabaseColumn(null);
        assertThat("PGobject", pgObject, is(notNullValue()));
        assertThat("PGobject type", ((PGobject) pgObject).getType(), is("jsonb"));
        assertThat("PGobject value", ((PGobject) pgObject).getValue(), is(nullValue()));
    }

    @Test
    public void convertToDatabaseColumn() throws JSONBException {
        final String json = "{\"id\":\"test\",\"errors\":[\"err1\",\"err2\"]}";
        final JsonNode jsonNode = jsonbContext.unmarshall(json, JsonNode.class);

        final Object pgObject = converter.convertToDatabaseColumn(jsonNode);
        assertThat("PGobject", pgObject, is(notNullValue()));
        assertThat("PGobject type", ((PGobject) pgObject).getType(), is("jsonb"));
        assertThat("PGobject value", ((PGobject) pgObject).getValue(), is(json));
    }

    @Test
    public void toEntityAttribute_dbValueIsNull() {
        assertThat(converter.convertToEntityAttribute(null), is(nullValue()));
    }

    @Test
    public void toEntityAttribute() throws SQLException {
        final String json = "{\"id\":\"test\",\"errors\":[\"err1\",\"err2\"]}";
        final PGobject pgObject = new PGobject();
        pgObject.setValue(json);
        final JsonNode jsonNode = converter.convertToEntityAttribute(pgObject);
        assertThat("jsonNode", jsonNode, is(notNullValue()));
        assertThat("jsonNode value", jsonNode.toString(), is(json));
    }
}