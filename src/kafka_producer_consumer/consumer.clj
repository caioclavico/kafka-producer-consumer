(ns kafka-producer-consumer.consumer
  (:gen-class)
  (:require [clojure.tools.logging :as log])
  (:import
   (java.time Duration)
   (org.apache.kafka.clients.consumer ConsumerConfig KafkaConsumer)))

(def props {ConsumerConfig/BOOTSTRAP_SERVERS_CONFIG        "127.0.0.1:9091"
            ConsumerConfig/GROUP_ID_CONFIG                 "test1"
            ConsumerConfig/ENABLE_AUTO_COMMIT_CONFIG       "true"
            ConsumerConfig/AUTO_COMMIT_INTERVAL_MS_CONFIG  "1000"
            ConsumerConfig/AUTO_OFFSET_RESET_CONFIG               "earliest"
            ConsumerConfig/KEY_DESERIALIZER_CLASS_CONFIG   "org.apache.kafka.common.serialization.StringDeserializer"
            ConsumerConfig/VALUE_DESERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringDeserializer"})

(defn -main [topic]
  (let [consumer (KafkaConsumer. props)]
    (println "Testing")
    (.subscribe consumer [topic])
    (while true
      (let [records (.poll consumer (Duration/ofMillis 10000))]
        (doseq [record records]
          (log/info "Sending on value" (str "Processed Value" (.value record))))
        (.commitAsync consumer)))
    ;; (loop [tc 0
    ;;        records []]
    ;;   (let [new-tc (reduce
    ;;                 (fn [tc record]
    ;;                   (let [value  (.value record)
    ;;                         cnt    (get value "count")
    ;;                         new-tc (+ tc cnt)]
    ;;                     (printf "Consumed record with key %s and value %s, and updated total count to %d\n"
    ;;                             (.key record)
    ;;                             value
    ;;                             new-tc)
    ;;                     new-tc))
    ;;                 tc
    ;;                 records)]
    ;;     (println "Waiting for message in KafkaConsumer.poll")
    ;;     (recur new-tc
    ;;            (seq (.poll consumer (Duration/ofSeconds 1))))))))
    ))