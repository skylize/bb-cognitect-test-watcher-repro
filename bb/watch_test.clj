#!/usr/bin/env bb
(ns watch-test
  (:require [babashka.cli :as cli]
            [babashka.tasks :as tasks]
            [clojure.string :as str]
            [pod.babashka.filewatcher :as fw]))

(defn -main [& args]
  (let [arg-map (cli/parse-opts args {:coerce {:dirs []}})
        ; default paths if none provided
        paths (or (:dirs arg-map) ["test" "src"])
        ; fn to start watcher
        watch (fn [path]
                (fw/watch path
                          (fn [ev] (when (#{:write} (:type ev))
                                     (tasks/run 'test:bb args)))))]
    ; start watcher for each path
    (mapv watch paths)
    (println (str "Watching `" (str/join "`, `" paths)
                  "` for changes. Press Ctrl-C to exit..."))
    @(promise)))
