(ns district-hegex.ui.config
  (:require
    [district-hegex.shared.smart-contracts-dev :as smart-contracts-dev]
    [district-hegex.shared.smart-contracts-prod :as smart-contracts-prod]
    [district-hegex.shared.smart-contracts-qa :as smart-contracts-qa]
    [mount.core :refer [defstate]])
  (:require-macros [district-hegex.shared.macros :refer [get-environment]]))

(def contracts-to-load [:optionchef
                        :optionchefdata
                        :hegexoption
                        :weth
                        :wbtcoptions
                        :brokenethoptions])

(def development-config
  {:debug? true
   :logging {:level :debug
             :console? true}
   :time-source :js-date
   :smart-contracts {:contracts (select-keys smart-contracts-qa/smart-contracts contracts-to-load)}
   :web3-accounts {:eip55? true}
   :web3-balances {:contracts (select-keys smart-contracts-qa/smart-contracts [:DNT])}
   :web3 {:url "http://localhost:8545"}
   :web3-tx {:disable-loading-recommended-gas-prices? true
             :eip55? true}
   :web3-tx-log {:disable-using-localstorage? true
                 :tx-costs-currencies [:USD]
                 :etherscan-url "https://rinkeby.etherscan.io"}
   :router {:html5? false}
   :router-google-analytics {:enabled? false}
   :district0x-emails-public-key "2564e15aaf9593acfdc633bd08f1fc5c089aa43972dd7e8a36d67825cd0154602da47d02f30e1f74e7e72c81ba5f0b3dd20d4d4f0cc6652a2e719a0e9d4c7f10943"})

(def qa-config
  {:logging {:level :warn
             :console? true
             :sentry {:dsn "https://2388a41ba9c54e06a6373d47a69aa887@sentry.io/1494135"
                      :environment "QA"}}
   :time-source :js-date
   :smart-contracts {:contracts (select-keys smart-contracts-qa/smart-contracts contracts-to-load)}
   :web3-accounts {:eip55? true}
   :web3-balances {:contracts (select-keys smart-contracts-qa/smart-contracts [:DNT])}
   :web3 {:url "https://rinkeby.infura.io"}
   :web3-tx {:disable-loading-recommended-gas-prices? true
             :eip55? true}
   :web3-tx-log {:disable-using-localstorage? false
                 :tx-costs-currencies [:USD]
                 :etherscan-url "https://ropsten.etherscan.io"}
   :router {:html5? true}
   :router-google-analytics {:enabled? false}
   :district0x-emails-public-key "2564e15aaf9593acfdc633bd08f1fc5c089aa43972dd7e8a36d67825cd0154602da47d02f30e1f74e7e72c81ba5f0b3dd20d4d4f0cc6652a2e719a0e9d4c7f10943"})

(def qa-dev-config (merge (assoc-in qa-config [:router :html5?] false)
                          {:ipfs {:host "http://127.0.0.1:5001"
                                  :endpoint "/api/v0"
                                  :gateway "http://127.0.0.1:8080/ipfs"}
                           :web3 {:url "http://localhost:8545"}}))

(def production-config
  {:logging {:level :warn
             :console? false
             :sentry {:dsn "https://2388a41ba9c54e06a6373d47a69aa887@sentry.io/1494135"
                      :environment "PRODUCTION"}}
   :time-source :js-date
   :smart-contracts {:contracts (select-keys smart-contracts-prod/smart-contracts contracts-to-load)}
   :web3-accounts {:eip55? true}
   :web3-balances {:contracts (select-keys smart-contracts-prod/smart-contracts [:DNT])}
   :web3 {:url "https://mainnet.infura.io"}
   :web3-tx {:eip55? true}
   :web3-tx-log {:disable-using-localstorage? false
                 :open-on-tx-hash? true
                 :tx-costs-currencies [:USD]
                 :etherscan-url "https://etherscan.io"}
   :router {:html5? true}
   :router-google-analytics {:enabled? false}
   :district0x-emails-public-key "2564e15aaf9593acfdc633bd08f1fc5c089aa43972dd7e8a36d67825cd0154602da47d02f30e1f74e7e72c81ba5f0b3dd20d4d4f0cc6652a2e719a0e9d4c7f10943"})

(def config-map
  (condp = (get-environment)
    "prod" production-config
    "qa" qa-config
    "qa-dev" qa-dev-config
    "dev" development-config))
