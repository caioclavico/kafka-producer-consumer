(ns kafka-producer-consumer.consumer
  (:gen-class)
  (:require
    [clojure.data.json :as json]
    [clojure.java.io :as jio])
  (:import
    (org.apache.kafka.clients.consumer ConsumerConfig KafkaConsumer)
    (java.time Duration)
    (java.util Properties)))

(defn- build-props [fname]
  (with-open [config (jio/reader fname)]
    (doto (Properties.)
      (.putAll {ConsumerConfig/GROUP_ID_CONFIG                 "clojure_example_group"
                ConsumerConfig/KEY_DESERIALIZER_CLASS_CONFIG   "org.apache.kafka.common.serialization.StringDeserializer"
                ConsumerConfig/VALUE_DESERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringDeserializer"})
      (.load config))))

(defn consumer! [fname topic]
  (with-open [consumer (KafkaConsumer. (build-props fname))]
    (.subscribe consumer [topic])
    (loop [tc 0 records []]
      (let [new-tc (reduce
          (fn [tc record]
            (let [value (.value record)
              count (get (json/read-str value) "count")
              new-tc (+ tc cnt)]
              (printfn "Consumed key %s, value %s, total count is %d\n" (.key record) value new-tc)
              new-tc))
            tc records)]
      (println "Waiting for message")
      (recur new-tc
        (sec (.poll consumer (Duration/ofSeconds 1))))))))

(defn -main
  "I kind of do something ... maybe."
  [& args]
  (apply consume args))