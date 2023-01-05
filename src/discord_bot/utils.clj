(ns discord-bot.utils)

(defn head-tail
  "Split the message in head & tail"
  [arr]
  (let [[head & tail] arr]
    {:head head :tail tail}))

