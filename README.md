# Publisher/Subscriber example with Spring Cloud Stream
This example is composed of five services, two publishers, one consumer and a message broker: 
* **TimeSender** is a supplier just sending messages with
the current date and time. The supplier is pooled y the spring framework every second. The pooling frequency can be changed with a parameter
(for example spring.cloud.stream.poller.fixed-delay=2000). It sends messages through the *timeChannel* channel. 
* **greetingsSender** is a REST API that receives a salutation from the HTTP client and sends it via a *StreamBridge* to the broker. It uses the
*greetingChannel* channel.
* **greetingsTransformer** is a consumer that receives the salutations from the broker, manipulates the message and sends the manipulated
message back to the broker. It receives the salutations from the channel *greetingChannel* and sends the transformed message to the *decoratedChannel*
* **greetingsTimeReceiver** is a consumer that receives both the time and greetings messages from the *timeChannel* and *decoratedChannel* respectively. 

[RabbitMQ](https://www.rabbitmq.com) is used as the message broker. RabbbitMQ calls *Exchange* to the channels. For each channel creates a queue
for each consumer group (see below) and a default queue for subscribers with no group. The exchanges (channels) are created when the 
*subscribers* are first created. Note that if the *publisher* is created before any *subscriber*
the messages will be lost until the first *subscriber* is created. However, once a *subscriber* is created
the channel is persistent. That is, even if there aren't any *subscribers* messages will be stored
by the broker until they are delivered.

## Project Structure
There are four modules, one for each microservice (senders and receivers) but for the RabbitMQ message broker. 
These modules are independent application that can be build and run independently.

They can be run from the IDE as we usually do. Note that you can edit the configuration for
running them and add VM options.

It is also possible and probably advisable to compile them from outside using maven. Go to
a console window. Navigate to <project_directory>/greetingssender, <project_directory>/timesender or
<project_directory>/timegreetingsreceiver and
execute `./mvn clean package `
. It will create a jar file in a directory called *target*

There is also a docker-compose.yml file that defines a docker with the RabbitMQ broker. You can use the broker in a docker
or install RabbitMQ directly on your machine.

## Running the example
**Warning:** You'll need to download and install the RabbitMQ message broker in
your local machine. https://www.rabbitmq.com
Or alternatively use the docker container defined in the docker-compose.yml file

Run the services in the following order:

1. Start the RabbitMQ broker (run the RabbitMQ docker or start it on your computer)
    * `./rabbitmq-server -detached ` to run the broker
    * `./rabbitmqctl status` to see its status
    * `./rabbitmqctl stop` to stop it
    * You can monitor the broker in the following address: `http://localhost:15672/`
2. Start one or more *greetingsTimeReceiver* services
    * `java -jar greetingsTimeReceiver/target/greetingsTimeReceiver-0.0.1-SNAPSHOT.jar` to run the first one with default parameters
    * `java -jar greetingsTimeReceiver/target/greetingsTimeReceiver-0.0.1-SNAPSHOT.jar` to run a second one with default parameters
    * `java -Dspring.cloud.stream.bindings.timeChannel.group=pepA -jar greetingsTimeReceiver/target/greetingsTimeReceiver-0.0.1-SNAPSHOT.jar
      ` to run a third one in a different consumer group for both channels
3. Start the *publishers*
    * `java -jar timesender/target/timesender-0.0.1-SNAPSHOT.jar`
    * `java -jar greetingssender/target/greetingssender-0.0.1-SNAPSHOT.jar`

## Observe the behaviour
Assuming that the *broker* and the *publishers* are started:

* When the first *subscriber* is started  (as described above) it begins to consume all the messages
* When the second *subscriber* is started (as described above) it consumes approximately half the messages.
  The other half is consumed by the first *subscriber*. This is because both of them are in the same *consumer
  group*
* When the third *subscriber* is started (as described above) the other *subscribers* don't change their
  behaviour while the third one gets all the messages
* When at least a *subscriber* has been created and then all are stopped the messages are enqueued by the
  broker. If a *subscriber* is run again it consumes all the past non consumed messages.
* If the broker is stopped while it had messages stored in the queue they aren't lost. When it
  is run again the *receivers* receive those messages. Obviously the messages sent by the *publisher*
  while the *broker* was down are lost

  