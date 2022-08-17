(ns kafka-producer-consumer.producer
  (:gen-class)
  (:import
;;    (org.apache.kafka.clients.admin AdminClient NewTopic)
;;    (org.apache.kafka.common.errors TopicExistsException)
   (org.apache.kafka.clients.producer KafkaProducer ProducerConfig ProducerRecord)
   (org.apache.kafka.common.serialization StringSerializer)))

(def props {ProducerConfig/BOOTSTRAP_SERVERS_CONFIG      "127.0.0.1:9091"
            ProducerConfig/ACKS_CONFIG                   "all"
            ProducerConfig/KEY_SERIALIZER_CLASS_CONFIG   StringSerializer
            ProducerConfig/VALUE_SERIALIZER_CLASS_CONFIG StringSerializer})

;; (defn- create-topic! [topic partitions replication cloud-config]
;;   (let [ac (AdminClient/create cloud-config)]
;;     (try
;;       (.createTopics ac [(NewTopic. ^String topic  (int partitions) (short replication))])
;;       ;; Ignore TopicExistsException, which would get thrown if the topic was previously created
;;       (catch Exception e (str "caught exception: " (.getMessage e)))
;;       (finally
;;         (.close ac)))))

(defn- send-to-producer [producer topic message]
  (let [data (ProducerRecord. topic nil message)]
    (.send producer data)))

;; (defn -main [topic]
;;   (let [producer (KafkaProducer. props)]
;;     ;; (create-topic! topic 1 3 props)
;;     (loop []
;;       (let [num (str (rand-int 100))]
;;         (println (str "Sending to Kafka topic " topic ": " num))
;;         (send-to-producer producer topic num)
;;         (Thread/sleep 1000)
;;         (recur)))))

(defn -main [topic value]
  (let [producer (KafkaProducer. props)]
    (println (str "Sending to Kafka topic " topic ": " value))
    (send-to-producer producer topic value)))