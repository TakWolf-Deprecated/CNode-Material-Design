package org.cnodejs.android.md.model.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.joda.time.DateTime;

import java.io.IOException;

public final class EntityUtils {

    private EntityUtils() {}

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
            .create();

    private static class DateTimeTypeAdapter extends TypeAdapter<DateTime> {

        @Override
        public void write(JsonWriter out, DateTime dateTime) throws IOException {
            if (dateTime == null) {
                out.nullValue();
            } else {
                out.value(dateTime.toString());
            }
        }

        @Override
        public DateTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                return new DateTime(in.nextString());
            }
        }

    }

}
