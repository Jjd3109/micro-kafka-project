package micro.kafka.common.outboxmessagerelay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	private static final Gson gson = new GsonBuilder()
		.registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) ->
			jsonSerializationContext.serialize(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
		.registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonDeserializer<LocalDateTime>) (jsonElement, type, jsonDeserializationContext) ->
			LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
		.create();

	public static Gson getGson() {
		return gson;
	}
}
