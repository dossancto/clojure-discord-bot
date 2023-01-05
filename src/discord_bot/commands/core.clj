(ns discord-bot.commands.core
  (:require
   [clojure.string :as string]
   [discljord.formatting :refer [mention-user]]
   [discord-bot.commands.cmd.say-hello :as cmd-hello]))

(defn not-exist-command
  "Return a message to Command that does not exists"
  [command author]

  (str "This command does not exist ( " (string/join " " command) " ) " (mention-user author)))

(defn tokenize-content
  "Splits the message and lowercase it."
  [message]

  (-> message
      (string/lower-case)
      (string/split #" ")))

(defn valid-command
  "Returns a valid format command.
  If is not a command retuns false."
  [content]

  (let [[prefix & message] (tokenize-content content)]
    (if (and (= "!" prefix) (seq message))
      message
      false)))

(defn run
  "Check wicth command run."
  [content author]

  (let [msg (valid-command content)]
    (when-not (= msg false)

      (cond
        (= "hello" (first msg)) (cmd-hello/hello (:username author))
        (= "about" (first msg))  cmd-hello/about
        :else (not-exist-command msg author)))))

