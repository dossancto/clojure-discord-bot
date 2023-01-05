(ns discord-bot.commands.cmd.embed
  (:require [clojure.data.json :as json]))

(defn get-embed
  "Return a embed for testing"
  []

  {:embed {:title "Test" :description "Embed discription" :fields [{:name "Field Title" :value "Field Value"}]}})
