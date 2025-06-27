package com.server.fisco.bcos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple6;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class AgriculturePastureFB extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610db7806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063021bc2fa1461005c57806348925c8d14610066578063497d2b76146100825780636ae4a8b71461009e57806377c9623b146100c1575b600080fd5b6100646100e4565b005b610080600480360381019061007b919061093d565b610183565b005b61009c60048036038101906100979190610a00565b610234565b005b6100a66102f8565b6040516100b896959493929190610be0565b60405180910390f35b6100c9610582565b6040516100db96959493929190610be0565b60405180910390f35b60006003015460006004015410610130576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161012790610c5d565b60405180910390fd5b6000600401600081548092919060010191905055507fc34a5f26b80ea8ff232c6835f518b4942ad194e27ed96742929a8cc76e45d6b06000600401546040516101799190610c7d565b60405180910390a1565b8360008001908051906020019061019b92919061082f565b5082600060010190805190602001906101b592919061082f565b5081600060020190805190602001906101cf92919061082f565b5080600060050190805190602001906101e992919061082f565b507fb756d42aff2510a41ac1762e708662abde778b63c7210dad3c6461e1af04e59b84848460006004015485604051610226959493929190610b71565b60405180910390a150505050565b8560008001908051906020019061024c92919061082f565b50846000600101908051906020019061026692919061082f565b50836000600201908051906020019061028092919061082f565b50826000600301819055508160006004018190555080600060050190805190602001906102ae92919061082f565b507f7e2e7ad5125719026ab21bd176ba2db196c9ad967997f10a83cb5502d85fbbda8686868686866040516102e896959493929190610be0565b60405180910390a1505050505050565b6000806000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103925780601f1061036757610100808354040283529160200191610392565b820191906000526020600020905b81548152906001019060200180831161037557829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104305780601f1061040557610100808354040283529160200191610430565b820191906000526020600020905b81548152906001019060200180831161041357829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104ce5780601f106104a3576101008083540402835291602001916104ce565b820191906000526020600020905b8154815290600101906020018083116104b157829003601f168201915b505050505090806003015490806004015490806005018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105785780601f1061054d57610100808354040283529160200191610578565b820191906000526020600020905b81548152906001019060200180831161055b57829003601f168201915b5050505050905086565b6060806060600080606060008001600060010160006002016000600301546000600401546000600501858054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106405780601f1061061557610100808354040283529160200191610640565b820191906000526020600020905b81548152906001019060200180831161062357829003601f168201915b50505050509550848054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106dc5780601f106106b1576101008083540402835291602001916106dc565b820191906000526020600020905b8154815290600101906020018083116106bf57829003601f168201915b50505050509450838054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107785780601f1061074d57610100808354040283529160200191610778565b820191906000526020600020905b81548152906001019060200180831161075b57829003601f168201915b50505050509350808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108145780601f106107e957610100808354040283529160200191610814565b820191906000526020600020905b8154815290600101906020018083116107f757829003601f168201915b50505050509050955095509550955095509550909192939495565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061087057805160ff191683800117855561089e565b8280016001018555821561089e579182015b8281111561089d578251825591602001919060010190610882565b5b5090506108ab91906108af565b5090565b6108d191905b808211156108cd5760008160009055506001016108b5565b5090565b90565b600082601f8301126108e557600080fd5b81356108f86108f382610cc5565b610c98565b9150808252602083016020830185838301111561091457600080fd5b61091f838284610d17565b50505092915050565b60008135905061093781610d6a565b92915050565b6000806000806080858703121561095357600080fd5b600085013567ffffffffffffffff81111561096d57600080fd5b610979878288016108d4565b945050602085013567ffffffffffffffff81111561099657600080fd5b6109a2878288016108d4565b935050604085013567ffffffffffffffff8111156109bf57600080fd5b6109cb878288016108d4565b925050606085013567ffffffffffffffff8111156109e857600080fd5b6109f4878288016108d4565b91505092959194509250565b60008060008060008060c08789031215610a1957600080fd5b600087013567ffffffffffffffff811115610a3357600080fd5b610a3f89828a016108d4565b965050602087013567ffffffffffffffff811115610a5c57600080fd5b610a6889828a016108d4565b955050604087013567ffffffffffffffff811115610a8557600080fd5b610a9189828a016108d4565b9450506060610aa289828a01610928565b9350506080610ab389828a01610928565b92505060a087013567ffffffffffffffff811115610ad057600080fd5b610adc89828a016108d4565b9150509295509295509295565b6000610af482610cf1565b610afe8185610cfc565b9350610b0e818560208601610d26565b610b1781610d59565b840191505092915050565b6000610b2f601b83610cfc565b91507fe5b7b2e8bebee588b0e69c80e5a4a7e585bbe6ae96e695b0e9878f00000000006000830152602082019050919050565b610b6b81610d0d565b82525050565b600060a0820190508181036000830152610b8b8188610ae9565b90508181036020830152610b9f8187610ae9565b90508181036040830152610bb38186610ae9565b9050610bc26060830185610b62565b8181036080830152610bd48184610ae9565b90509695505050505050565b600060c0820190508181036000830152610bfa8189610ae9565b90508181036020830152610c0e8188610ae9565b90508181036040830152610c228187610ae9565b9050610c316060830186610b62565b610c3e6080830185610b62565b81810360a0830152610c508184610ae9565b9050979650505050505050565b60006020820190508181036000830152610c7681610b22565b9050919050565b6000602082019050610c926000830184610b62565b92915050565b6000604051905081810181811067ffffffffffffffff82111715610cbb57600080fd5b8060405250919050565b600067ffffffffffffffff821115610cdc57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600082825260208201905092915050565b6000819050919050565b82818337600083830152505050565b60005b83811015610d44578082015181840152602081019050610d29565b83811115610d53576000848401525b50505050565b6000601f19601f8301169050919050565b610d7381610d0d565b8114610d7e57600080fd5b5056fea264697066735822122099167a59e9368420c93e731a579afb0cc94c5ae8295f196df8e77e2ae523074d64736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610db7806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063021bc2fa1461005c57806348925c8d14610066578063497d2b76146100825780636ae4a8b71461009e57806377c9623b146100c1575b600080fd5b6100646100e4565b005b610080600480360381019061007b919061093d565b610183565b005b61009c60048036038101906100979190610a00565b610234565b005b6100a66102f8565b6040516100b896959493929190610be0565b60405180910390f35b6100c9610582565b6040516100db96959493929190610be0565b60405180910390f35b60006003015460006004015410610130576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161012790610c5d565b60405180910390fd5b6000600401600081548092919060010191905055507fc34a5f26b80ea8ff232c6835f518b4942ad194e27ed96742929a8cc76e45d6b06000600401546040516101799190610c7d565b60405180910390a1565b8360008001908051906020019061019b92919061082f565b5082600060010190805190602001906101b592919061082f565b5081600060020190805190602001906101cf92919061082f565b5080600060050190805190602001906101e992919061082f565b507fb756d42aff2510a41ac1762e708662abde778b63c7210dad3c6461e1af04e59b84848460006004015485604051610226959493929190610b71565b60405180910390a150505050565b8560008001908051906020019061024c92919061082f565b50846000600101908051906020019061026692919061082f565b50836000600201908051906020019061028092919061082f565b50826000600301819055508160006004018190555080600060050190805190602001906102ae92919061082f565b507f7e2e7ad5125719026ab21bd176ba2db196c9ad967997f10a83cb5502d85fbbda8686868686866040516102e896959493929190610be0565b60405180910390a1505050505050565b6000806000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103925780601f1061036757610100808354040283529160200191610392565b820191906000526020600020905b81548152906001019060200180831161037557829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104305780601f1061040557610100808354040283529160200191610430565b820191906000526020600020905b81548152906001019060200180831161041357829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104ce5780601f106104a3576101008083540402835291602001916104ce565b820191906000526020600020905b8154815290600101906020018083116104b157829003601f168201915b505050505090806003015490806004015490806005018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105785780601f1061054d57610100808354040283529160200191610578565b820191906000526020600020905b81548152906001019060200180831161055b57829003601f168201915b5050505050905086565b6060806060600080606060008001600060010160006002016000600301546000600401546000600501858054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106405780601f1061061557610100808354040283529160200191610640565b820191906000526020600020905b81548152906001019060200180831161062357829003601f168201915b50505050509550848054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106dc5780601f106106b1576101008083540402835291602001916106dc565b820191906000526020600020905b8154815290600101906020018083116106bf57829003601f168201915b50505050509450838054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107785780601f1061074d57610100808354040283529160200191610778565b820191906000526020600020905b81548152906001019060200180831161075b57829003601f168201915b50505050509350808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108145780601f106107e957610100808354040283529160200191610814565b820191906000526020600020905b8154815290600101906020018083116107f757829003601f168201915b50505050509050955095509550955095509550909192939495565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061087057805160ff191683800117855561089e565b8280016001018555821561089e579182015b8281111561089d578251825591602001919060010190610882565b5b5090506108ab91906108af565b5090565b6108d191905b808211156108cd5760008160009055506001016108b5565b5090565b90565b600082601f8301126108e557600080fd5b81356108f86108f382610cc5565b610c98565b9150808252602083016020830185838301111561091457600080fd5b61091f838284610d17565b50505092915050565b60008135905061093781610d6a565b92915050565b6000806000806080858703121561095357600080fd5b600085013567ffffffffffffffff81111561096d57600080fd5b610979878288016108d4565b945050602085013567ffffffffffffffff81111561099657600080fd5b6109a2878288016108d4565b935050604085013567ffffffffffffffff8111156109bf57600080fd5b6109cb878288016108d4565b925050606085013567ffffffffffffffff8111156109e857600080fd5b6109f4878288016108d4565b91505092959194509250565b60008060008060008060c08789031215610a1957600080fd5b600087013567ffffffffffffffff811115610a3357600080fd5b610a3f89828a016108d4565b965050602087013567ffffffffffffffff811115610a5c57600080fd5b610a6889828a016108d4565b955050604087013567ffffffffffffffff811115610a8557600080fd5b610a9189828a016108d4565b9450506060610aa289828a01610928565b9350506080610ab389828a01610928565b92505060a087013567ffffffffffffffff811115610ad057600080fd5b610adc89828a016108d4565b9150509295509295509295565b6000610af482610cf1565b610afe8185610cfc565b9350610b0e818560208601610d26565b610b1781610d59565b840191505092915050565b6000610b2f601b83610cfc565b91507fe5b7b2e8bebee588b0e69c80e5a4a7e585bbe6ae96e695b0e9878f00000000006000830152602082019050919050565b610b6b81610d0d565b82525050565b600060a0820190508181036000830152610b8b8188610ae9565b90508181036020830152610b9f8187610ae9565b90508181036040830152610bb38186610ae9565b9050610bc26060830185610b62565b8181036080830152610bd48184610ae9565b90509695505050505050565b600060c0820190508181036000830152610bfa8189610ae9565b90508181036020830152610c0e8188610ae9565b90508181036040830152610c228187610ae9565b9050610c316060830186610b62565b610c3e6080830185610b62565b81810360a0830152610c508184610ae9565b9050979650505050505050565b60006020820190508181036000830152610c7681610b22565b9050919050565b6000602082019050610c926000830184610b62565b92915050565b6000604051905081810181811067ffffffffffffffff82111715610cbb57600080fd5b8060405250919050565b600067ffffffffffffffff821115610cdc57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600082825260208201905092915050565b6000819050919050565b82818337600083830152505050565b60005b83811015610d44578082015181840152602081019050610d29565b83811115610d53576000848401525b50505050565b6000601f19601f8301169050919050565b610d7381610d0d565b8114610d7e57600080fd5b5056fea264697066735822122099167a59e9368420c93e731a579afb0cc94c5ae8295f196df8e77e2ae523074d64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"methodSignatureAsString\":\"BreedingQuantityIncreased(uint256)\",\"name\":\"BreedingQuantityIncreased\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"inputs\":[{\"name\":\"newBreedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"PastureCreated(string,string,string,uint256,uint256,string)\",\"name\":\"PastureCreated\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"inputs\":[{\"name\":\"name\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"location\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"area\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"bigBreedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"breedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"description\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"PastureUpdated(string,string,string,uint256,string)\",\"name\":\"PastureUpdated\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"inputs\":[{\"name\":\"name\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"location\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"area\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"breedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"description\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"createPasture(string,string,string,uint256,uint256,string)\",\"name\":\"createPasture\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[{\"name\":\"_name\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_location\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_area\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_bigBreedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"_breedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"_description\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"getPasture()\",\"name\":\"getPasture\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"inputs\":[],\"outputs\":[{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}]},{\"methodSignatureAsString\":\"increaseBreedingQuantity()\",\"name\":\"increaseBreedingQuantity\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[],\"outputs\":[]},{\"methodSignatureAsString\":\"pasture()\",\"name\":\"pasture\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"inputs\":[],\"outputs\":[{\"name\":\"name\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"location\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"area\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"bigBreedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"breedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"description\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}]},{\"methodSignatureAsString\":\"updatePasture(string,string,string,string)\",\"name\":\"updatePasture\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[{\"name\":\"_name\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_location\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_area\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_description\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CREATEPASTURE = "createPasture";

    public static final String FUNC_GETPASTURE = "getPasture";

    public static final String FUNC_INCREASEBREEDINGQUANTITY = "increaseBreedingQuantity";

    public static final String FUNC_PASTURE = "pasture";

    public static final String FUNC_UPDATEPASTURE = "updatePasture";

    public static final Event BREEDINGQUANTITYINCREASED_EVENT = new Event("BreedingQuantityIncreased",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event PASTURECREATED_EVENT = new Event("PastureCreated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event PASTUREUPDATED_EVENT = new Event("PastureUpdated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected AgriculturePastureFB(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List<BreedingQuantityIncreasedEventResponse> getBreedingQuantityIncreasedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(BREEDINGQUANTITYINCREASED_EVENT, transactionReceipt);
        ArrayList<BreedingQuantityIncreasedEventResponse> responses = new ArrayList<BreedingQuantityIncreasedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            BreedingQuantityIncreasedEventResponse typedResponse = new BreedingQuantityIncreasedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newBreedingQuantity = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeBreedingQuantityIncreasedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(BREEDINGQUANTITYINCREASED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeBreedingQuantityIncreasedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(BREEDINGQUANTITYINCREASED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<PastureCreatedEventResponse> getPastureCreatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PASTURECREATED_EVENT, transactionReceipt);
        ArrayList<PastureCreatedEventResponse> responses = new ArrayList<PastureCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PastureCreatedEventResponse typedResponse = new PastureCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.location = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.area = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.bigBreedingQuantity = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.breedingQuantity = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribePastureCreatedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(PASTURECREATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribePastureCreatedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(PASTURECREATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<PastureUpdatedEventResponse> getPastureUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PASTUREUPDATED_EVENT, transactionReceipt);
        ArrayList<PastureUpdatedEventResponse> responses = new ArrayList<PastureUpdatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PastureUpdatedEventResponse typedResponse = new PastureUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.location = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.area = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.breedingQuantity = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribePastureUpdatedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(PASTUREUPDATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribePastureUpdatedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(PASTUREUPDATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public TransactionReceipt createPasture(String _name, String _location, String _area, BigInteger _bigBreedingQuantity, BigInteger _breedingQuantity, String _description) {
        final Function function = new Function(
                FUNC_CREATEPASTURE,
                Arrays.<Type>asList(new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Uint256(_bigBreedingQuantity),
                new Uint256(_breedingQuantity),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] createPasture(String _name, String _location, String _area, BigInteger _bigBreedingQuantity, BigInteger _breedingQuantity, String _description, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CREATEPASTURE,
                Arrays.<Type>asList(new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Uint256(_bigBreedingQuantity),
                new Uint256(_breedingQuantity),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCreatePasture(String _name, String _location, String _area, BigInteger _bigBreedingQuantity, BigInteger _breedingQuantity, String _description) {
        final Function function = new Function(
                FUNC_CREATEPASTURE,
                Arrays.<Type>asList(new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Uint256(_bigBreedingQuantity),
                new Uint256(_breedingQuantity),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple6<String, String, String, BigInteger, BigInteger, String> getCreatePastureInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATEPASTURE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple6<String, String, String, BigInteger, BigInteger, String>(

                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (BigInteger) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (String) results.get(5).getValue()
                );
    }

    public Tuple6<String, String, String, BigInteger, BigInteger, String> getPasture() throws ContractException {
        final Function function = new Function(FUNC_GETPASTURE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple6<String, String, String, BigInteger, BigInteger, String>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (BigInteger) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (String) results.get(5).getValue());
    }

    public TransactionReceipt increaseBreedingQuantity() {
        final Function function = new Function(
                FUNC_INCREASEBREEDINGQUANTITY,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] increaseBreedingQuantity(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_INCREASEBREEDINGQUANTITY,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForIncreaseBreedingQuantity() {
        final Function function = new Function(
                FUNC_INCREASEBREEDINGQUANTITY,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple6<String, String, String, BigInteger, BigInteger, String> pasture() throws ContractException {
        final Function function = new Function(FUNC_PASTURE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple6<String, String, String, BigInteger, BigInteger, String>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (BigInteger) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (String) results.get(5).getValue());
    }

    public TransactionReceipt updatePasture(String _name, String _location, String _area, String _description) {
        final Function function = new Function(
                FUNC_UPDATEPASTURE,
                Arrays.<Type>asList(new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] updatePasture(String _name, String _location, String _area, String _description, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_UPDATEPASTURE,
                Arrays.<Type>asList(new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUpdatePasture(String _name, String _location, String _area, String _description) {
        final Function function = new Function(
                FUNC_UPDATEPASTURE,
                Arrays.<Type>asList(new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<String, String, String, String> getUpdatePastureInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_UPDATEPASTURE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<String, String, String, String>(

                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue()
                );
    }

    public static AgriculturePastureFB load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new AgriculturePastureFB(contractAddress, client, credential);
    }

    public static AgriculturePastureFB deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(AgriculturePastureFB.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class BreedingQuantityIncreasedEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger newBreedingQuantity;
    }

    public static class PastureCreatedEventResponse {
        public TransactionReceipt.Logs log;

        public String name;

        public String location;

        public String area;

        public BigInteger bigBreedingQuantity;

        public BigInteger breedingQuantity;

        public String description;
    }

    public static class PastureUpdatedEventResponse {
        public TransactionReceipt.Logs log;

        public String name;

        public String location;

        public String area;

        public BigInteger breedingQuantity;

        public String description;
    }
}
