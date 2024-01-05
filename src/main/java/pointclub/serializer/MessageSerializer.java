package pointclub.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pointclub.entity.chat.Message;

import java.io.IOException;

public class MessageSerializer extends StdSerializer<Message> {

    public MessageSerializer() {
        this(null);
    }

    public MessageSerializer(Class<Message> t) {
        super(t);
    }

    @Override
    public void serialize(Message message, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("serverId", message.getServerId());
        jgen.writeStringField("content", message.getContent());
        jgen.writeNumberField("senderId", message.getSender().getServerId());
        jgen.writeNumberField("roomId", message.getChatRoom().getServerId());
        jgen.writeNumberField("sendTime", message.getSendTime().getTime());
        jgen.writeEndObject();
    }
}