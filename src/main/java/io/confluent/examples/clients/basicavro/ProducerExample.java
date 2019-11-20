package io.confluent.examples.clients.basicavro;

import io.confluent.kafka.schemaregistry.avro.AvroCompatibilityLevel;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.errors.SerializationException;

import javax.imageio.IIOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

public class ProducerExample {

    private static final String TOPIC = "PAYM";

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(final String[] args) {

        final Properties props = new Properties();
        String propsFile = "";
        if (args.length > 0) {
            propsFile = args[0];
        } else {
            System.err.println("Missing properties file.");
            System.exit(1);
        }
        try ( InputStream inputProps = new FileInputStream(propsFile)) {
            props.load(inputProps);
        } catch (final IIOException | FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(AbstractKafkaAvroSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY, io.confluent.kafka.serializers.subject.TopicRecordNameStrategy.class);
        props.put("auto.register.schemas", "false");

        try (KafkaProducer<String, Object> producer = new KafkaProducer<String, Object>(props)) {
            for (int i = 0; i < 10; i++) {
                final String trace_id = "id" + Integer.toString(i);
                final PayMResponse paymResponse = new PayMResponse(trace_id,"OK-200", i, Long.toString(System.currentTimeMillis()));
                final ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(TOPIC, paymResponse.getTraceId().toString(), paymResponse);
                producer.send(record);
                final PayMInfo paymInfo = new PayMInfo(trace_id,"ChangeOfAddress","NewAddress" , Long.toString(System.currentTimeMillis()));
                final ProducerRecord<String, Object> recordInfo = new ProducerRecord<String, Object>(TOPIC, paymInfo.getTraceId().toString(), paymInfo);
                producer.send(recordInfo);
                Thread.sleep(1000L);
            }
            producer.flush();
            System.out.printf("Successfully produced 10 messages to a topic called %s%n", TOPIC);
        } catch (final SerializationException e) {
            e.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}

