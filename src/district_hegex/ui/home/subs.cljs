(ns district-hegex.ui.home.subs
  (:require
    [district-hegex.ui.home.events :as events]
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::dark-mode?
  (fn [db]
    (get-in db [::events/dark-mode?])))
