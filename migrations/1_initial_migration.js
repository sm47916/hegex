const { env, smartContractsPath, contractPathByNet } = require("../truffle.js");
const { encodeContractEDN, writeSmartContracts, getSmartContractAddress, readSmartContractsFile, setSmartContractAddress } = require("./utils.js");



let smartContractsList = [];

const hegicETHFactory = {
  "ropsten": "0x77041D13e0B9587e0062239d083b51cB6d81404D",
  "mainnet": "0xefc0eeadc1132a12c9487d800112693bf49ecfa2",
}

const hegicWBTCFactory = {
  "ropsten": "0x3aD466588F5f8f1Ce896645d5322db3F25810639",
  "mainnet": "0x3961245db602ed7c03eeccda33ea3846bd8723bd",
}


const Migrations = artifacts.require("Migrations");
const chefData = artifacts.require('OptionChefData');
const chef = artifacts.require('OptionChef');
const token = artifacts.require('Hegexoption.sol');
const METADATA_BASE = "https://stacksideflow.github.io/hegexoption-nft/meta/"

module.exports = async (deployer, network) => {
  console.log("Migrating Hegex to " + network);
  await deployer.deploy(Migrations);
  const migrations = await Migrations.deployed();
  //important - will throw on localnet as hegic contracts are deployed separately

  await deployer.deploy(chefData);
  const chefdatad = await chefData.deployed();
  await deployer.deploy(chef, hegicETHFactory[network], hegicWBTCFactory[network], chefdatad.address);
  const chefd = await chef.deployed();
  await deployer.deploy(token, chefd.address, METADATA_BASE);
  const tokend = await token.deployed();

  await chefd.updateHegexoption(tokend.address);
  await chefdatad.transferOwnership(chefd.address);

  let smartContracts = readSmartContractsFile(smartContractsPath);

  assignContract(chefd, "OptionChef", "optionchef");
  assignContract(tokend, "Hegexoption", "hegexoption");
  writeSmartContracts(contractPathByNet(network), smartContractsList, env)
};

function assignContract(contract_instance, contractName, contract_key, opts) {
  console.log("- Assigning '" + contractName + "' to smart contract listing...");
  opts = opts || {};
  smartContractsList = smartContractsList.concat(
    encodeContractEDN(contract_instance, contractName, contract_key, opts));
}
