(ns discord-bot.core
  (:require [clojure.edn :as edn]
            [clojure.core.async :refer [chan close!]]
            [discljord.messaging :as dm]
            [dotenv :refer [env app-env]]
            [discljord.connections :as discord-ws]
            [discord-bot.commands.core :as cmd]
            [discljord.events :refer [message-pump!]])
  (:gen-class))

(def state (atom nil))

(def discord-token (env "DISCORD_TOKEN"))

(def bot-id (atom nil))

(def config (edn/read-string (slurp "config.edn")))

(defmulti handle-event (fn [type _data] type))

(defmethod handle-event :message-create
  [_ {{bot :bot} :author :keys [channel-id content author]}]

  (when-not bot
    (dm/create-message! (:rest @state) channel-id :content (cmd/run content author))))

(defmethod handle-event :ready
  [_ _]
  (discord-ws/status-update! (:gateway @state) :activity (discord-ws/create-activity :name (:playing config))))

(defmethod handle-event :default [_ _])
(defn start-bot!
  [token & intents]

  (let [event-channel (chan 100)
        gateway-connection (discord-ws/connect-bot! token event-channel :intents (set intents))
        rest-connection (dm/start-connection! token)]
    {:events  event-channel
     :gateway gateway-connection
     :rest    rest-connection}))

(defn stop-bot! [{:keys [rest gateway events] :as _state}]
  (dm/stop-connection! rest)
  (discord-ws/disconnect-bot! gateway)
  (close! events))

(defn -main [& _args]
  (reset! state (start-bot! discord-token :guild-messages))
  (reset! bot-id (:id @(dm/get-current-user! (:rest @state))))
  (try
    (message-pump! (:events @state) handle-event)
    (finally (stop-bot! @state))))

