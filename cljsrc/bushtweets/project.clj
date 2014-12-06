(defproject bushtweets "0.1.0"
  :description "Tweet collection utility"
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 [com.twitter/hbc-core "2.2.0"]]
  :main ^:skip-aot bushtweets.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
