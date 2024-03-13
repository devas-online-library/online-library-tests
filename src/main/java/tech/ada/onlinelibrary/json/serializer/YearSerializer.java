package tech.ada.onlinelibrary.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Year;

public class YearSerializer extends JsonSerializer<Year> {
    @Override
    public void serialize(Year year, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(year.getValue()); // Serialize Year as an int
    }
}
