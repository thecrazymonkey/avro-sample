# Java Producer and Consumer with Avro

This directory includes projects demonstrating how to use the Java producer and consumer with Avro and Confluent Schema Registry
The original code is changed to show support for TopicRecordNameStrategy with mixing of different schemas within the same topic.

Use Postman file to create schemas for PayMInfo and PayMResponse records.
Generate the PayMInfo and PayMResponse Java classes "mvn clean generate-sources"

Build consumer and producer applications "mvn package"

Run test producer (this assumes mutual SSL security configuration)
mvn exec:java -Djavax.net.ssl.keyStore=/<path to keystore>/<name>.keystore.jks -Djavax.net.ssl.trustStore=<path to truststore>/<name>.truststore.jks -Djavax.net.ssl.keyStorePassword=<password> -Djavax.net.ssl.trustStorePassword=<password> -Dexec.mainClass=io.confluent.examples.clients.basicavro.ProducerExample -Dexec.args=producer.properties

You should see the following:
Successfully produced 10 messages to a topic called PAYM

Process finished with exit code 0

Run test consumer (this assumes mutual SSL security configuration)
mvn exec:java -Djavax.net.ssl.keyStore=/<path to keystore>/<name>.keystore.jks -Djavax.net.ssl.trustStore=<path to truststore>/<name>.truststore.jks -Djavax.net.ssl.keyStorePassword=<password> -Djavax.net.ssl.trustStorePassword=<password> -Dexec.mainClass=io.confluent.examples.clients.basicavro.ConsumerExample -Dexec.args=consumer.properties

You should see something like:
key = id2, value = {"trace_id": "id2", "result": "OK-200", "sequence_no": 2, "process_time": "1574207602392"}
key = id2, value = {"trace_id": "id2", "info_type": "ChangeOfAddress", "info_value": "NewAddress", "process_time": "1574207602392"}
.....
.....

How to use these examples (original text from Confluent examples):

* [ProducerExample.java](src/main/java/io/confluent/examples/clients/basicavro/ProducerExample.java): see [Confluent Schema Registry tutorial](https://docs.confluent.io/current/schema-registry/docs/schema_registry_tutorial.html)
* [ConsumerExample.java](src/main/java/io/confluent/examples/clients/basicavro/ConsumerExample.java): see [Confluent Schema Registry tutorial](https://docs.confluent.io/current/schema-registry/docs/schema_registry_tutorial.html)