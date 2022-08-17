/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Number
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.Short
 *  java.lang.String
 *  java.lang.System
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.Properties
 *  java.util.Set
 *  java.util.concurrent.atomic.AtomicInteger
 *  org.apache.kafka.clients.ClientDnsLookup
 *  org.apache.kafka.clients.CommonClientConfigs
 *  org.apache.kafka.clients.producer.internals.DefaultPartitioner
 *  org.apache.kafka.common.config.AbstractConfig
 *  org.apache.kafka.common.config.ConfigDef
 *  org.apache.kafka.common.config.ConfigDef$Importance
 *  org.apache.kafka.common.config.ConfigDef$NonEmptyString
 *  org.apache.kafka.common.config.ConfigDef$NonNullValidator
 *  org.apache.kafka.common.config.ConfigDef$Range
 *  org.apache.kafka.common.config.ConfigDef$Type
 *  org.apache.kafka.common.config.ConfigDef$ValidString
 *  org.apache.kafka.common.config.ConfigDef$Validator
 *  org.apache.kafka.common.config.ConfigException
 *  org.apache.kafka.common.metrics.Sensor$RecordingLevel
 *  org.apache.kafka.common.serialization.Serializer
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.apache.kafka.clients.producer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.ClientDnsLookup;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.metrics.Sensor;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerConfig
extends AbstractConfig {
    private static final Logger log = LoggerFactory.getLogger(ProducerConfig.class);
    private static final ConfigDef CONFIG;
    public static final String BOOTSTRAP_SERVERS_CONFIG = "bootstrap.servers";
    public static final String CLIENT_DNS_LOOKUP_CONFIG = "client.dns.lookup";
    public static final String METADATA_MAX_AGE_CONFIG = "metadata.max.age.ms";
    private static final String METADATA_MAX_AGE_DOC = "The period of time in milliseconds after which we force a refresh of metadata even if we haven't seen any partition leadership changes to proactively discover any new brokers or partitions.";
    public static final String METADATA_MAX_IDLE_CONFIG = "metadata.max.idle.ms";
    private static final String METADATA_MAX_IDLE_DOC = "Controls how long the producer will cache metadata for a topic that's idle. If the elapsed time since a topic was last produced to exceeds the metadata idle duration, then the topic's metadata is forgotten and the next access to it will force a metadata fetch request.";
    public static final String BATCH_SIZE_CONFIG = "batch.size";
    private static final String BATCH_SIZE_DOC = "The producer will attempt to batch records together into fewer requests whenever multiple records are being sent to the same partition. This helps performance on both the client and the server. This configuration controls the default batch size in bytes. <p>No attempt will be made to batch records larger than this size. <p>Requests sent to brokers will contain multiple batches, one for each partition with data available to be sent. <p>A small batch size will make batching less common and may reduce throughput (a batch size of zero will disable batching entirely). A very large batch size may use memory a bit more wastefully as we will always allocate a buffer of the specified batch size in anticipation of additional records.<p>Note: This setting gives the upper bound of the batch size to be sent. If we have fewer than this many bytes accumulated for this partition, we will 'linger' for the <code>linger.ms</code> time waiting for more records to show up. This <code>linger.ms</code> setting defaults to 0, which means we'll immediately send out a record even the accumulated batch size is under this <code>batch.size</code> setting.";
    public static final String ACKS_CONFIG = "acks";
    private static final String ACKS_DOC = "The number of acknowledgments the producer requires the leader to have received before considering a request complete. This controls the  durability of records that are sent. The following settings are allowed:  <ul> <li><code>acks=0</code> If set to zero then the producer will not wait for any acknowledgment from the server at all. The record will be immediately added to the socket buffer and considered sent. No guarantee can be made that the server has received the record in this case, and the <code>retries</code> configuration will not take effect (as the client won't generally know of any failures). The offset given back for each record will always be set to <code>-1</code>. <li><code>acks=1</code> This will mean the leader will write the record to its local log but will respond without awaiting full acknowledgement from all followers. In this case should the leader fail immediately after acknowledging the record but before the followers have replicated it then the record will be lost. <li><code>acks=all</code> This means the leader will wait for the full set of in-sync replicas to acknowledge the record. This guarantees that the record will not be lost as long as at least one in-sync replica remains alive. This is the strongest available guarantee. This is equivalent to the acks=-1 setting.</ul><p>Note that enabling idempotence requires this config value to be 'all'. If conflicting configurations are set and idempotence is not explicitly enabled, idempotence is disabled.";
    public static final String LINGER_MS_CONFIG = "linger.ms";
    private static final String LINGER_MS_DOC = "The producer groups together any records that arrive in between request transmissions into a single batched request. Normally this occurs only under load when records arrive faster than they can be sent out. However in some circumstances the client may want to reduce the number of requests even under moderate load. This setting accomplishes this by adding a small amount of artificial delay&mdash;that is, rather than immediately sending out a record, the producer will wait for up to the given delay to allow other records to be sent so that the sends can be batched together. This can be thought of as analogous to Nagle's algorithm in TCP. This setting gives the upper bound on the delay for batching: once we get <code>batch.size</code> worth of records for a partition it will be sent immediately regardless of this setting, however if we have fewer than this many bytes accumulated for this partition we will 'linger' for the specified time waiting for more records to show up. This setting defaults to 0 (i.e. no delay). Setting <code>linger.ms=5</code>, for example, would have the effect of reducing the number of requests sent but would add up to 5ms of latency to records sent in the absence of load.";
    public static final String REQUEST_TIMEOUT_MS_CONFIG = "request.timeout.ms";
    private static final String REQUEST_TIMEOUT_MS_DOC = "The configuration controls the maximum amount of time the client will wait for the response of a request. If the response is not received before the timeout elapses the client will resend the request if necessary or fail the request if retries are exhausted. This should be larger than <code>replica.lag.time.max.ms</code> (a broker configuration) to reduce the possibility of message duplication due to unnecessary producer retries.";
    public static final String DELIVERY_TIMEOUT_MS_CONFIG = "delivery.timeout.ms";
    private static final String DELIVERY_TIMEOUT_MS_DOC = "An upper bound on the time to report success or failure after a call to <code>send()</code> returns. This limits the total time that a record will be delayed prior to sending, the time to await acknowledgement from the broker (if expected), and the time allowed for retriable send failures. The producer may report failure to send a record earlier than this config if either an unrecoverable error is encountered, the retries have been exhausted, or the record is added to a batch which reached an earlier delivery expiration deadline. The value of this config should be greater than or equal to the sum of <code>request.timeout.ms</code> and <code>linger.ms</code>.";
    public static final String CLIENT_ID_CONFIG = "client.id";
    public static final String SEND_BUFFER_CONFIG = "send.buffer.bytes";
    public static final String RECEIVE_BUFFER_CONFIG = "receive.buffer.bytes";
    public static final String MAX_REQUEST_SIZE_CONFIG = "max.request.size";
    private static final String MAX_REQUEST_SIZE_DOC = "The maximum size of a request in bytes. This setting will limit the number of record batches the producer will send in a single request to avoid sending huge requests. This is also effectively a cap on the maximum uncompressed record batch size. Note that the server has its own cap on the record batch size (after compression if compression is enabled) which may be different from this.";
    public static final String RECONNECT_BACKOFF_MS_CONFIG = "reconnect.backoff.ms";
    public static final String RECONNECT_BACKOFF_MAX_MS_CONFIG = "reconnect.backoff.max.ms";
    public static final String MAX_BLOCK_MS_CONFIG = "max.block.ms";
    private static final String MAX_BLOCK_MS_DOC = "The configuration controls how long the <code>KafkaProducer</code>'s <code>send()</code>, <code>partitionsFor()</code>, <code>initTransactions()</code>, <code>sendOffsetsToTransaction()</code>, <code>commitTransaction()</code> and <code>abortTransaction()</code> methods will block. For <code>send()</code> this timeout bounds the total time waiting for both metadata fetch and buffer allocation (blocking in the user-supplied serializers or partitioner is not counted against this timeout). For <code>partitionsFor()</code> this timeout bounds the time spent waiting for metadata if it is unavailable. The transaction-related methods always block, but may timeout if the transaction coordinator could not be discovered or did not respond within the timeout.";
    public static final String BUFFER_MEMORY_CONFIG = "buffer.memory";
    private static final String BUFFER_MEMORY_DOC = "The total bytes of memory the producer can use to buffer records waiting to be sent to the server. If records are sent faster than they can be delivered to the server the producer will block for <code>max.block.ms</code> after which it will throw an exception.<p>This setting should correspond roughly to the total memory the producer will use, but is not a hard bound since not all memory the producer uses is used for buffering. Some additional memory will be used for compression (if compression is enabled) as well as for maintaining in-flight requests.";
    public static final String RETRY_BACKOFF_MS_CONFIG = "retry.backoff.ms";
    public static final String COMPRESSION_TYPE_CONFIG = "compression.type";
    private static final String COMPRESSION_TYPE_DOC = "The compression type for all data generated by the producer. The default is none (i.e. no compression). Valid  values are <code>none</code>, <code>gzip</code>, <code>snappy</code>, <code>lz4</code>, or <code>zstd</code>. Compression is of full batches of data, so the efficacy of batching will also impact the compression ratio (more batching means better compression).";
    public static final String METRICS_SAMPLE_WINDOW_MS_CONFIG = "metrics.sample.window.ms";
    public static final String METRICS_NUM_SAMPLES_CONFIG = "metrics.num.samples";
    public static final String METRICS_RECORDING_LEVEL_CONFIG = "metrics.recording.level";
    public static final String METRIC_REPORTER_CLASSES_CONFIG = "metric.reporters";
    private static final int MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_FOR_IDEMPOTENCE = 5;
    public static final String MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION = "max.in.flight.requests.per.connection";
    private static final String MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_DOC = "The maximum number of unacknowledged requests the client will send on a single connection before blocking. Note that if this config is set to be greater than 1 and <code>enable.idempotence</code> is set to false, there is a risk of message re-ordering after a failed send due to retries (i.e., if retries are enabled). Additionally, enabling idempotence requires this config value to be less than or equal to 5. If conflicting configurations are set and idempotence is not explicitly enabled, idempotence is disabled.";
    public static final String RETRIES_CONFIG = "retries";
    private static final String RETRIES_DOC = "Setting a value greater than zero will cause the client to resend any record whose send fails with a potentially transient error. Note that this retry is no different than if the client resent the record upon receiving the error. Produce requests will be failed before the number of retries has been exhausted if the timeout configured by <code>delivery.timeout.ms</code> expires first before successful acknowledgement. Users should generally prefer to leave this config unset and instead use <code>delivery.timeout.ms</code> to control retry behavior.<p>Enabling idempotence requires this config value to be greater than 0. If conflicting configurations are set and idempotence is not explicitly enabled, idempotence is disabled.<p>Allowing retries while setting <code>enable.idempotence</code> to <code>false</code> and <code>max.in.flight.requests.per.connection</code> to 1 will potentially change the ordering of records because if two batches are sent to a single partition, and the first fails and is retried but the second succeeds, then the records in the second batch may appear first.";
    public static final String KEY_SERIALIZER_CLASS_CONFIG = "key.serializer";
    public static final String KEY_SERIALIZER_CLASS_DOC = "Serializer class for key that implements the <code>org.apache.kafka.common.serialization.Serializer</code> interface.";
    public static final String VALUE_SERIALIZER_CLASS_CONFIG = "value.serializer";
    public static final String VALUE_SERIALIZER_CLASS_DOC = "Serializer class for value that implements the <code>org.apache.kafka.common.serialization.Serializer</code> interface.";
    public static final String SOCKET_CONNECTION_SETUP_TIMEOUT_MS_CONFIG = "socket.connection.setup.timeout.ms";
    public static final String SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS_CONFIG = "socket.connection.setup.timeout.max.ms";
    public static final String CONNECTIONS_MAX_IDLE_MS_CONFIG = "connections.max.idle.ms";
    public static final String PARTITIONER_CLASS_CONFIG = "partitioner.class";
    private static final String PARTITIONER_CLASS_DOC = "A class to use to determine which partition to be send to when produce the records. Available options are:<ul><li><code>org.apache.kafka.clients.producer.internals.DefaultPartitioner</code>: The default partitioner. This strategy will try sticking to a partition until the batch is full, or <code>linger.ms</code> is up. It works with the strategy:<ul><li>If no partition is specified but a key is present, choose a partition based on a hash of the key</li><li>If no partition or key is present, choose the sticky partition that changes when the batch is full, or <code>linger.ms</code> is up.</li></ul></li><li><code>org.apache.kafka.clients.producer.RoundRobinPartitioner</code>: This partitioning strategy is that each record in a series of consecutive records will be sent to a different partition(no matter if the 'key' is provided or not), until we run out of partitions and start over again. Note: There's a known issue that will cause uneven distribution when new batch is created. Please check KAFKA-9965 for more detail.</li><li><code>org.apache.kafka.clients.producer.UniformStickyPartitioner</code>: This partitioning strategy will try sticking to a partition(no matter if the 'key' is provided or not) until the batch is full, or <code>linger.ms</code> is up.</li></ul><p>Implementing the <code>org.apache.kafka.clients.producer.Partitioner</code> interface allows you to plug in a custom partitioner.";
    public static final String INTERCEPTOR_CLASSES_CONFIG = "interceptor.classes";
    public static final String INTERCEPTOR_CLASSES_DOC = "A list of classes to use as interceptors. Implementing the <code>org.apache.kafka.clients.producer.ProducerInterceptor</code> interface allows you to intercept (and possibly mutate) the records received by the producer before they are published to the Kafka cluster. By default, there are no interceptors.";
    public static final String ENABLE_IDEMPOTENCE_CONFIG = "enable.idempotence";
    public static final String ENABLE_IDEMPOTENCE_DOC = "When set to 'true', the producer will ensure that exactly one copy of each message is written in the stream. If 'false', producer retries due to broker failures, etc., may write duplicates of the retried message in the stream. Note that enabling idempotence requires <code>max.in.flight.requests.per.connection</code> to be less than or equal to 5 (with message ordering preserved for any allowable value), <code>retries</code> to be greater than 0, and <code>acks</code> must be 'all'. <p>Idempotence is enabled by default if no conflicting configurations are set. If conflicting configurations are set and idempotence is not explicitly enabled, idempotence is disabled. If idempotence is explicitly enabled and conflicting configurations are set, a <code>ConfigException</code> is thrown.";
    public static final String TRANSACTION_TIMEOUT_CONFIG = "transaction.timeout.ms";
    public static final String TRANSACTION_TIMEOUT_DOC = "The maximum amount of time in ms that the transaction coordinator will wait for a transaction status update from the producer before proactively aborting the ongoing transaction.If this value is larger than the transaction.max.timeout.ms setting in the broker, the request will fail with a <code>InvalidTxnTimeoutException</code> error.";
    public static final String TRANSACTIONAL_ID_CONFIG = "transactional.id";
    public static final String TRANSACTIONAL_ID_DOC = "The TransactionalId to use for transactional delivery. This enables reliability semantics which span multiple producer sessions since it allows the client to guarantee that transactions using the same TransactionalId have been completed prior to starting any new transactions. If no TransactionalId is provided, then the producer is limited to idempotent delivery. If a TransactionalId is configured, <code>enable.idempotence</code> is implied. By default the TransactionId is not configured, which means transactions cannot be used. Note that, by default, transactions require a cluster of at least three brokers which is the recommended setting for production; for development you can change this, by adjusting broker setting <code>transaction.state.log.replication.factor</code>.";
    public static final String SECURITY_PROVIDERS_CONFIG = "security.providers";
    private static final String SECURITY_PROVIDERS_DOC = "A list of configurable creator classes each returning a provider implementing security algorithms. These classes should implement the <code>org.apache.kafka.common.security.auth.SecurityProviderCreator</code> interface.";
    private static final AtomicInteger PRODUCER_CLIENT_ID_SEQUENCE;

    protected Map<String, Object> postProcessParsedConfig(Map<String, Object> parsedValues) {
        Map refinedConfigs = CommonClientConfigs.postProcessReconnectBackoffConfigs((AbstractConfig)this, parsedValues);
        this.postProcessAndValidateIdempotenceConfigs((Map<String, Object>)refinedConfigs);
        this.maybeOverrideClientId((Map<String, Object>)refinedConfigs);
        return refinedConfigs;
    }

    private void maybeOverrideClientId(Map<String, Object> configs) {
        String refinedClientId;
        boolean userConfiguredClientId = this.originals().containsKey((Object)CLIENT_ID_CONFIG);
        if (userConfiguredClientId) {
            refinedClientId = this.getString(CLIENT_ID_CONFIG);
        } else {
            String transactionalId = this.getString(TRANSACTIONAL_ID_CONFIG);
            refinedClientId = "producer-" + (transactionalId != null ? transactionalId : Integer.valueOf((int)PRODUCER_CLIENT_ID_SEQUENCE.getAndIncrement()));
        }
        configs.put((Object)CLIENT_ID_CONFIG, (Object)refinedClientId);
    }

    private void postProcessAndValidateIdempotenceConfigs(Map<String, Object> configs) {
        Map originalConfigs = this.originals();
        String acksStr = ProducerConfig.parseAcks(this.getString(ACKS_CONFIG));
        configs.put((Object)ACKS_CONFIG, (Object)acksStr);
        boolean userConfiguredIdempotence = this.originals().containsKey((Object)ENABLE_IDEMPOTENCE_CONFIG);
        boolean idempotenceEnabled = this.getBoolean(ENABLE_IDEMPOTENCE_CONFIG);
        boolean shouldDisableIdempotence = false;
        if (idempotenceEnabled) {
            int inFlightConnection;
            short acks;
            int retries = this.getInt(RETRIES_CONFIG);
            if (retries == 0) {
                if (userConfiguredIdempotence) {
                    throw new ConfigException("Must set retries to non-zero when using the idempotent producer.");
                }
                log.info("Idempotence will be disabled because {} is set to 0.", (Object)RETRIES_CONFIG, (Object)retries);
                shouldDisableIdempotence = true;
            }
            if ((acks = Short.valueOf((String)acksStr).shortValue()) != -1) {
                if (userConfiguredIdempotence) {
                    throw new ConfigException("Must set acks to all in order to use the idempotent producer. Otherwise we cannot guarantee idempotence.");
                }
                log.info("Idempotence will be disabled because {} is set to {}, not set to 'all'.", (Object)ACKS_CONFIG, (Object)acks);
                shouldDisableIdempotence = true;
            }
            if (5 < (inFlightConnection = this.getInt(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION).intValue())) {
                if (userConfiguredIdempotence) {
                    throw new ConfigException("Must set max.in.flight.requests.per.connection to at most 5 to use the idempotent producer.");
                }
                log.warn("Idempotence will be disabled because {} is set to {}, which is greater than 5. Please note that in v4.0.0 and onward, this will become an error.", (Object)MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, (Object)inFlightConnection);
                shouldDisableIdempotence = true;
            }
        }
        if (shouldDisableIdempotence) {
            configs.put((Object)ENABLE_IDEMPOTENCE_CONFIG, (Object)false);
            idempotenceEnabled = false;
        }
        boolean userConfiguredTransactions = originalConfigs.containsKey((Object)TRANSACTIONAL_ID_CONFIG);
        if (!idempotenceEnabled && userConfiguredTransactions) {
            throw new ConfigException("Cannot set a transactional.id without also enabling idempotence.");
        }
    }

    private static String parseAcks(String acksString) {
        try {
            return acksString.trim().equalsIgnoreCase("all") ? "-1" : Short.parseShort((String)acksString.trim()) + "";
        }
        catch (NumberFormatException e) {
            throw new ConfigException("Invalid configuration value for 'acks': " + acksString);
        }
    }

    static Map<String, Object> appendSerializerToConfig(Map<String, Object> configs, Serializer<?> keySerializer, Serializer<?> valueSerializer) {
        HashMap newConfigs = new HashMap(configs);
        if (keySerializer != null) {
            newConfigs.put((Object)KEY_SERIALIZER_CLASS_CONFIG, (Object)keySerializer.getClass());
        }
        if (valueSerializer != null) {
            newConfigs.put((Object)VALUE_SERIALIZER_CLASS_CONFIG, (Object)valueSerializer.getClass());
        }
        return newConfigs;
    }

    public ProducerConfig(Properties props) {
        super(CONFIG, (Map)props);
    }

    public ProducerConfig(Map<String, Object> props) {
        super(CONFIG, props);
    }

    ProducerConfig(Map<?, ?> props, boolean doLog) {
        super(CONFIG, props, doLog);
    }

    public static Set<String> configNames() {
        return CONFIG.names();
    }

    public static ConfigDef configDef() {
        return new ConfigDef(CONFIG);
    }

    public static void main(String[] args) {
        System.out.println(CONFIG.toHtml(4, config -> "producerconfigs_" + config));
    }

    static {
        PRODUCER_CLIENT_ID_SEQUENCE = new AtomicInteger(1);
        CONFIG = new ConfigDef().define(BOOTSTRAP_SERVERS_CONFIG, ConfigDef.Type.LIST, (Object)Collections.emptyList(), (ConfigDef.Validator)new ConfigDef.NonNullValidator(), ConfigDef.Importance.HIGH, "A list of host/port pairs to use for establishing the initial connection to the Kafka cluster. The client will make use of all servers irrespective of which servers are specified here for bootstrapping&mdash;this list only impacts the initial hosts used to discover the full set of servers. This list should be in the form <code>host1:port1,host2:port2,...</code>. Since these servers are just used for the initial connection to discover the full cluster membership (which may change dynamically), this list need not contain the full set of servers (you may want more than one, though, in case a server is down).").define(CLIENT_DNS_LOOKUP_CONFIG, ConfigDef.Type.STRING, (Object)ClientDnsLookup.USE_ALL_DNS_IPS.toString(), (ConfigDef.Validator)ConfigDef.ValidString.in((String[])new String[]{ClientDnsLookup.USE_ALL_DNS_IPS.toString(), ClientDnsLookup.RESOLVE_CANONICAL_BOOTSTRAP_SERVERS_ONLY.toString()}), ConfigDef.Importance.MEDIUM, "Controls how the client uses DNS lookups. If set to <code>use_all_dns_ips</code>, connect to each returned IP address in sequence until a successful connection is established. After a disconnection, the next IP is used. Once all IPs have been used once, the client resolves the IP(s) from the hostname again (both the JVM and the OS cache DNS name lookups, however). If set to <code>resolve_canonical_bootstrap_servers_only</code>, resolve each bootstrap address into a list of canonical names. After the bootstrap phase, this behaves the same as <code>use_all_dns_ips</code>.").define(BUFFER_MEMORY_CONFIG, ConfigDef.Type.LONG, (Object)0x2000000L, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Long.valueOf((long)0L)), ConfigDef.Importance.HIGH, BUFFER_MEMORY_DOC).define(RETRIES_CONFIG, ConfigDef.Type.INT, (Object)Integer.MAX_VALUE, (ConfigDef.Validator)ConfigDef.Range.between((Number)Integer.valueOf((int)0), (Number)Integer.valueOf((int)Integer.MAX_VALUE)), ConfigDef.Importance.HIGH, RETRIES_DOC).define(ACKS_CONFIG, ConfigDef.Type.STRING, (Object)"all", (ConfigDef.Validator)ConfigDef.ValidString.in((String[])new String[]{"all", "-1", "0", "1"}), ConfigDef.Importance.LOW, ACKS_DOC).define(COMPRESSION_TYPE_CONFIG, ConfigDef.Type.STRING, (Object)"none", ConfigDef.Importance.HIGH, COMPRESSION_TYPE_DOC).define(BATCH_SIZE_CONFIG, ConfigDef.Type.INT, (Object)16384, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)0)), ConfigDef.Importance.MEDIUM, BATCH_SIZE_DOC).define(LINGER_MS_CONFIG, ConfigDef.Type.LONG, (Object)0, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)0)), ConfigDef.Importance.MEDIUM, LINGER_MS_DOC).define(DELIVERY_TIMEOUT_MS_CONFIG, ConfigDef.Type.INT, (Object)120000, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)0)), ConfigDef.Importance.MEDIUM, DELIVERY_TIMEOUT_MS_DOC).define(CLIENT_ID_CONFIG, ConfigDef.Type.STRING, (Object)"", ConfigDef.Importance.MEDIUM, "An id string to pass to the server when making requests. The purpose of this is to be able to track the source of requests beyond just ip/port by allowing a logical application name to be included in server-side request logging.").define(SEND_BUFFER_CONFIG, ConfigDef.Type.INT, (Object)131072, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)-1)), ConfigDef.Importance.MEDIUM, "The size of the TCP send buffer (SO_SNDBUF) to use when sending data. If the value is -1, the OS default will be used.").define(RECEIVE_BUFFER_CONFIG, ConfigDef.Type.INT, (Object)32768, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)-1)), ConfigDef.Importance.MEDIUM, "The size of the TCP receive buffer (SO_RCVBUF) to use when reading data. If the value is -1, the OS default will be used.").define(MAX_REQUEST_SIZE_CONFIG, ConfigDef.Type.INT, (Object)0x100000, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)0)), ConfigDef.Importance.MEDIUM, MAX_REQUEST_SIZE_DOC).define(RECONNECT_BACKOFF_MS_CONFIG, ConfigDef.Type.LONG, (Object)50L, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Long.valueOf((long)0L)), ConfigDef.Importance.LOW, "The base amount of time to wait before attempting to reconnect to a given host. This avoids repeatedly connecting to a host in a tight loop. This backoff applies to all connection attempts by the client to a broker.").define(RECONNECT_BACKOFF_MAX_MS_CONFIG, ConfigDef.Type.LONG, (Object)1000L, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Long.valueOf((long)0L)), ConfigDef.Importance.LOW, "The maximum amount of time in milliseconds to wait when reconnecting to a broker that has repeatedly failed to connect. If provided, the backoff per host will increase exponentially for each consecutive connection failure, up to this maximum. After calculating the backoff increase, 20% random jitter is added to avoid connection storms.").define(RETRY_BACKOFF_MS_CONFIG, ConfigDef.Type.LONG, (Object)100L, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Long.valueOf((long)0L)), ConfigDef.Importance.LOW, "The amount of time to wait before attempting to retry a failed request to a given topic partition. This avoids repeatedly sending requests in a tight loop under some failure scenarios.").define(MAX_BLOCK_MS_CONFIG, ConfigDef.Type.LONG, (Object)60000, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)0)), ConfigDef.Importance.MEDIUM, MAX_BLOCK_MS_DOC).define(REQUEST_TIMEOUT_MS_CONFIG, ConfigDef.Type.INT, (Object)30000, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)0)), ConfigDef.Importance.MEDIUM, REQUEST_TIMEOUT_MS_DOC).define(METADATA_MAX_AGE_CONFIG, ConfigDef.Type.LONG, (Object)300000, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)0)), ConfigDef.Importance.LOW, METADATA_MAX_AGE_DOC).define(METADATA_MAX_IDLE_CONFIG, ConfigDef.Type.LONG, (Object)300000, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)5000)), ConfigDef.Importance.LOW, METADATA_MAX_IDLE_DOC).define(METRICS_SAMPLE_WINDOW_MS_CONFIG, ConfigDef.Type.LONG, (Object)30000, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)0)), ConfigDef.Importance.LOW, "The window of time a metrics sample is computed over.").define(METRICS_NUM_SAMPLES_CONFIG, ConfigDef.Type.INT, (Object)2, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)1)), ConfigDef.Importance.LOW, "The number of samples maintained to compute metrics.").define(METRICS_RECORDING_LEVEL_CONFIG, ConfigDef.Type.STRING, (Object)Sensor.RecordingLevel.INFO.toString(), (ConfigDef.Validator)ConfigDef.ValidString.in((String[])new String[]{Sensor.RecordingLevel.INFO.toString(), Sensor.RecordingLevel.DEBUG.toString(), Sensor.RecordingLevel.TRACE.toString()}), ConfigDef.Importance.LOW, "The highest recording level for metrics.").define(METRIC_REPORTER_CLASSES_CONFIG, ConfigDef.Type.LIST, (Object)Collections.emptyList(), (ConfigDef.Validator)new ConfigDef.NonNullValidator(), ConfigDef.Importance.LOW, "A list of classes to use as metrics reporters. Implementing the <code>org.apache.kafka.common.metrics.MetricsReporter</code> interface allows plugging in classes that will be notified of new metric creation. The JmxReporter is always included to register JMX statistics.").define(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, ConfigDef.Type.INT, (Object)5, (ConfigDef.Validator)ConfigDef.Range.atLeast((Number)Integer.valueOf((int)1)), ConfigDef.Importance.LOW, MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_DOC).define(KEY_SERIALIZER_CLASS_CONFIG, ConfigDef.Type.CLASS, ConfigDef.Importance.HIGH, KEY_SERIALIZER_CLASS_DOC).define(VALUE_SERIALIZER_CLASS_CONFIG, ConfigDef.Type.CLASS, ConfigDef.Importance.HIGH, VALUE_SERIALIZER_CLASS_DOC).define(SOCKET_CONNECTION_SETUP_TIMEOUT_MS_CONFIG, ConfigDef.Type.LONG, (Object)CommonClientConfigs.DEFAULT_SOCKET_CONNECTION_SETUP_TIMEOUT_MS, ConfigDef.Importance.MEDIUM, "The amount of time the client will wait for the socket connection to be established. If the connection is not built before the timeout elapses, clients will close the socket channel.").define(SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS_CONFIG, ConfigDef.Type.LONG, (Object)CommonClientConfigs.DEFAULT_SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS, ConfigDef.Importance.MEDIUM, "The maximum amount of time the client will wait for the socket connection to be established. The connection setup timeout will increase exponentially for each consecutive connection failure up to this maximum. To avoid connection storms, a randomization factor of 0.2 will be applied to the timeout resulting in a random range between 20% below and 20% above the computed value.").define(CONNECTIONS_MAX_IDLE_MS_CONFIG, ConfigDef.Type.LONG, (Object)540000, ConfigDef.Importance.MEDIUM, "Close idle connections after the number of milliseconds specified by this config.").define(PARTITIONER_CLASS_CONFIG, ConfigDef.Type.CLASS, DefaultPartitioner.class, ConfigDef.Importance.MEDIUM, PARTITIONER_CLASS_DOC).define(INTERCEPTOR_CLASSES_CONFIG, ConfigDef.Type.LIST, (Object)Collections.emptyList(), (ConfigDef.Validator)new ConfigDef.NonNullValidator(), ConfigDef.Importance.LOW, INTERCEPTOR_CLASSES_DOC).define("security.protocol", ConfigDef.Type.STRING, (Object)"PLAINTEXT", ConfigDef.Importance.MEDIUM, CommonClientConfigs.SECURITY_PROTOCOL_DOC).define(SECURITY_PROVIDERS_CONFIG, ConfigDef.Type.STRING, null, ConfigDef.Importance.LOW, SECURITY_PROVIDERS_DOC).withClientSslSupport().withClientSaslSupport().define(ENABLE_IDEMPOTENCE_CONFIG, ConfigDef.Type.BOOLEAN, (Object)true, ConfigDef.Importance.LOW, ENABLE_IDEMPOTENCE_DOC).define(TRANSACTION_TIMEOUT_CONFIG, ConfigDef.Type.INT, (Object)60000, ConfigDef.Importance.LOW, TRANSACTION_TIMEOUT_DOC).define(TRANSACTIONAL_ID_CONFIG, ConfigDef.Type.STRING, null, (ConfigDef.Validator)new ConfigDef.NonEmptyString(), ConfigDef.Importance.LOW, TRANSACTIONAL_ID_DOC);
    }
}
