(ns discord-bot.commands.cmd.say-hello
  (:require [clojure.string :as string]
            [discljord.formatting :as f]))

(defn hello
  "Just say hello for someone"
  [somebody]

  {:content (str "Hey " (f/mention-user somebody) ", Whatsapp.")})

(def about
  (str "This bot was made for study"))

(defn spoiler
  "say a spoiler about a movie or game"
  [msg]

  (let [join-msg (string/join " " msg)]
    {:content (str "||" join-msg "||")}))
