(ns discord-bot.commands.core
  (:require
   [clojure.string :as string]
   [discljord.formatting :refer [mention-user]]
   [discord-bot.commands.cmd.say-hello :as cmd-hello]))

(defn head-tail
  "Split the message in head & tail"
  [arr]
  (let [[head & tail] arr]
    {:head head :tail tail}))

(defn not-exist-command
  "Return a message to Command that does not exists"
  [command author]

  {:content (str "This command does not exist ( " (string/join " " command) " ) " (mention-user author))})

(defn tokenize-content
  "Splits the message and lowercase it."
  [message]

  (-> message
      (string/split #" ")))

(defn valid-command
  "Returns a valid format command.
  If is not a command retuns false."
  [content]

  (let [msg (head-tail (tokenize-content content))]
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
        (= "spoiler" (format-command msg))  (cmd-hello/spoiler (:tail (head-tail msg)))
        :else (not-exist-command msg (:author data))))))

