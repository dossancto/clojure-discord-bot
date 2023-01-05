(ns discord-bot.commands.core
  (:require
   [clojure.string :as string]
   [discord-bot.utils :as u]
   [discljord.formatting :refer [mention-user]]
   [discord-bot.commands.cmd.embed :as cmd-embed]
   [discord-bot.commands.cmd.say-hello :as cmd-hello]))

(defn not-exist-command
  "Return a message to Command that does not exists"
  [command author]

  {:content (str "This command does not exist ( " command " ) " (mention-user author))})

(defn tokenize-content
  "Splits the message."
  [message]

  (-> message
      (string/split #" ")))

(defn valid-command
  "Returns a valid format command.
  If is not a command retuns false."
  [content]

  (let [msg (u/head-tail (tokenize-content content))]
    (if (and (= "!" (:head msg)) (seq (:tail msg)))
      (:tail msg)
      false)))

(defn format-command
  "Format to analize if the message is a command name."
  [message]

  (-> (first message)
      (string/lower-case)))

(defn run
  "Check wicth command run."
  [data]

  (let [msg (valid-command (:content data))]
    (when-not (= msg false)

      (cond
        (= "hello" (format-command msg)) (cmd-hello/hello (:author data))
        (= "about" (format-command msg))  cmd-hello/about
        (= "embed" (format-command msg))  (cmd-embed/get-embed)
        (= "spoiler" (format-command msg))  (cmd-hello/spoiler (:tail (u/head-tail msg)))
        :else (not-exist-command (format-command msg) (:author data))))))

