(ns bushtweets.core
  (:require [clojure.data.json :as json])
  (:import (java.util ArrayList)
           (java.util.concurrent LinkedBlockingQueue)
           (com.twitter.hbc ClientBuilder)
           (com.twitter.hbc.core Client Constants)
           (com.twitter.hbc.core.endpoint StatusesFilterEndpoint)
           (com.twitter.hbc.core.processor StringDelimitedProcessor)
           (com.twitter.hbc.httpclient.auth Authentication OAuth1))
  (:gen-class))

(def consumer-key "zryJcfij4Es6pdAOTb9LUd6EW")
(def consumer-secret "cxxlYARbOgS08sOPdm2NCfn7MrGFniYAix472iYLENiAemFd0q")
(def access-token "14400922-8yDq3k5fxqJD6hVLukcdCo8tNSqloElNjtY4FWUUI")
(def access-token-secret "F1EVDkFyNmrj9mMU7TXTQ7bT32t1Km2AcZ9XOOR8MN7Ar")

(defn create-queue [size]
  (LinkedBlockingQueue. size))

(defn create-endpoint [terms]
  (-> (StatusesFilterEndpoint.)
    (.trackTerms (ArrayList. terms))))

(defn create-auth [consumer-key consumer-secret token secret]
  (OAuth1. consumer-key consumer-secret token secret))

(defn create-client [endpoint auth queue]
  (-> (ClientBuilder.)
    (.hosts Constants/STREAM_HOST)
    (.endpoint endpoint)
    (.authentication auth)
    (.processor (StringDelimitedProcessor. queue))
    (.build)))

(defn run [terms queue-size]
  (let [queue (create-queue queue-size)
        endpoint (create-endpoint terms)
        auth (create-auth consumer-key consumer-secret access-token access-token-secret)
        client (create-client endpoint auth queue)]
    (println "Starting client")
    (.connect client)
    (while (not (.isDone client))
      (println "fetching message")
      (let [msg-json (.take queue)
            msg (json/read-str msg-json)
            tweet (get msg "text")]
        (println tweet)))
    (println "shutting down client")
    (.shutdown client)))

(defn -main
  "run run run"
  [& args]
  (run ["bushwick" "twitter"] 10000))
