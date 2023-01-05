(ns discord-bot.commands.cmd.say-hello)

(defn hello
  "Just say hello for someone"
  [somebody]

  (str "Hey " somebody ", Whatsapp."))

(def about
  (str "This bot was made for study"))
