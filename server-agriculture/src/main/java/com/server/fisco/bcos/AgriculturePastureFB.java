package com.server.fisco.bcos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple7;
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
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50611166806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063021bc2fa1461005c578063258289511461007a5780636ae4a8b7146100aa57806377c9623b146100ce578063cd155828146100f2575b600080fd5b610064610122565b6040516100719190610e0e565b60405180910390f35b610094600480360381019061008f9190610ae2565b610214565b6040516100a19190610e0e565b60405180910390f35b6100b2610326565b6040516100c59796959493929190610fb0565b60405180910390f35b6100d66105b6565b6040516100e99796959493929190610fb0565b60405180910390f35b61010c60048036038101906101079190610bb9565b6108b6565b6040516101199190610e0e565b60405180910390f35b6000806000800154141561016b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161016290610f35565b60405180910390fd5b600060040154600060050154106101b7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016101ae90610f15565b60405180910390fd5b60006005016000815480929190600101919050555060008001547f7bf212115d4ce00e41e897b450bbc260c526f138186131ddba049fed057c87bc6000600501546040516102059190610f95565b60405180910390a26001905090565b60008060008001541415801561022d5750856000800154145b61026c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161026390610f75565b60405180910390fd5b84600060010190805190602001906102859291906109d4565b50836000600201908051906020019061029f9291906109d4565b5082600060030190805190602001906102b99291906109d4565b5081600060060190805190602001906102d39291906109d4565b50857fd30f0ae681961da69e31141d63b2c29eccbf03ce0d8a0942737040ce114d07b686868660006005015487604051610311959493929190610e29565b60405180910390a26001905095945050505050565b6000806000015490806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103c65780601f1061039b576101008083540402835291602001916103c6565b820191906000526020600020905b8154815290600101906020018083116103a957829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104645780601f1061043957610100808354040283529160200191610464565b820191906000526020600020905b81548152906001019060200180831161044757829003601f168201915b505050505090806003018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105025780601f106104d757610100808354040283529160200191610502565b820191906000526020600020905b8154815290600101906020018083116104e557829003601f168201915b505050505090806004015490806005015490806006018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105ac5780601f10610581576101008083540402835291602001916105ac565b820191906000526020600020905b81548152906001019060200180831161058f57829003601f168201915b5050505050905087565b60006060806060600080606060008060000154141561060a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161060190610f35565b60405180910390fd5b60008001546000600101600060020160006003016000600401546000600501546000600601858054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106c45780601f10610699576101008083540402835291602001916106c4565b820191906000526020600020905b8154815290600101906020018083116106a757829003601f168201915b50505050509550848054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107605780601f1061073557610100808354040283529160200191610760565b820191906000526020600020905b81548152906001019060200180831161074357829003601f168201915b50505050509450838054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107fc5780601f106107d1576101008083540402835291602001916107fc565b820191906000526020600020905b8154815290600101906020018083116107df57829003601f168201915b50505050509350808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108985780601f1061086d57610100808354040283529160200191610898565b820191906000526020600020905b81548152906001019060200180831161087b57829003601f168201915b50505050509050965096509650965096509650965090919293949596565b6000806000800154146108fe576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108f590610f55565b60405180910390fd5b87600080018190555086600060010190805190602001906109209291906109d4565b50856000600201908051906020019061093a9291906109d4565b5084600060030190805190602001906109549291906109d4565b50836000600401819055508260006005018190555081600060060190805190602001906109829291906109d4565b50877f7ecf02b5e6d53cb06267c5a51919722ab137de97f27cbac6fbe37bb3ea9b7b4e8888888888886040516109bd96959493929190610e98565b60405180910390a260019050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610a1557805160ff1916838001178555610a43565b82800160010185558215610a43579182015b82811115610a42578251825591602001919060010190610a27565b5b509050610a509190610a54565b5090565b610a7691905b80821115610a72576000816000905550600101610a5a565b5090565b90565b600082601f830112610a8a57600080fd5b8135610a9d610a9882611068565b61103b565b91508082526020830160208301858383011115610ab957600080fd5b610ac48382846110c6565b50505092915050565b600081359050610adc81611119565b92915050565b600080600080600060a08688031215610afa57600080fd5b6000610b0888828901610acd565b955050602086013567ffffffffffffffff811115610b2557600080fd5b610b3188828901610a79565b945050604086013567ffffffffffffffff811115610b4e57600080fd5b610b5a88828901610a79565b935050606086013567ffffffffffffffff811115610b7757600080fd5b610b8388828901610a79565b925050608086013567ffffffffffffffff811115610ba057600080fd5b610bac88828901610a79565b9150509295509295909350565b600080600080600080600060e0888a031215610bd457600080fd5b6000610be28a828b01610acd565b975050602088013567ffffffffffffffff811115610bff57600080fd5b610c0b8a828b01610a79565b965050604088013567ffffffffffffffff811115610c2857600080fd5b610c348a828b01610a79565b955050606088013567ffffffffffffffff811115610c5157600080fd5b610c5d8a828b01610a79565b9450506080610c6e8a828b01610acd565b93505060a0610c7f8a828b01610acd565b92505060c088013567ffffffffffffffff811115610c9c57600080fd5b610ca88a828b01610a79565b91505092959891949750929550565b610cc0816110b0565b82525050565b6000610cd182611094565b610cdb818561109f565b9350610ceb8185602086016110d5565b610cf481611108565b840191505092915050565b6000610d0c601b8361109f565b91507fe5b7b2e8bebee588b0e69c80e5a4a7e585bbe6ae96e695b0e9878f00000000006000830152602082019050919050565b6000610d4c600f8361109f565b91507fe5a4a7e6a39ae4b88de5ad98e59ca800000000000000000000000000000000006000830152602082019050919050565b6000610d8c60128361109f565b91507fe5a4a7e6a39ae5b7b2e7bb8fe5ad98e59ca800000000000000000000000000006000830152602082019050919050565b6000610dcc601d8361109f565b91507fe5a4a7e6a39ae4b88de5ad98e59ca8e688964944e4b88de58cb9e9858d0000006000830152602082019050919050565b610e08816110bc565b82525050565b6000602082019050610e236000830184610cb7565b92915050565b600060a0820190508181036000830152610e438188610cc6565b90508181036020830152610e578187610cc6565b90508181036040830152610e6b8186610cc6565b9050610e7a6060830185610dff565b8181036080830152610e8c8184610cc6565b90509695505050505050565b600060c0820190508181036000830152610eb28189610cc6565b90508181036020830152610ec68188610cc6565b90508181036040830152610eda8187610cc6565b9050610ee96060830186610dff565b610ef66080830185610dff565b81810360a0830152610f088184610cc6565b9050979650505050505050565b60006020820190508181036000830152610f2e81610cff565b9050919050565b60006020820190508181036000830152610f4e81610d3f565b9050919050565b60006020820190508181036000830152610f6e81610d7f565b9050919050565b60006020820190508181036000830152610f8e81610dbf565b9050919050565b6000602082019050610faa6000830184610dff565b92915050565b600060e082019050610fc5600083018a610dff565b8181036020830152610fd78189610cc6565b9050818103604083","0152610feb8188610cc6565b90508181036060830152610fff8187610cc6565b905061100e6080830186610dff565b61101b60a0830185610dff565b81810360c083015261102d8184610cc6565b905098975050505050505050565b6000604051905081810181811067ffffffffffffffff8211171561105e57600080fd5b8060405250919050565b600067ffffffffffffffff82111561107f57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600082825260208201905092915050565b60008115159050919050565b6000819050919050565b82818337600083830152505050565b60005b838110156110f35780820151818401526020810190506110d8565b83811115611102576000848401525b50505050565b6000601f19601f8301169050919050565b611122816110bc565b811461112d57600080fd5b5056fea2646970667358221220cf8504bef224fadbbcf1cba376258503504084cf642ebb7e3fe1e99d720cbae264736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50611166806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063021bc2fa1461005c578063258289511461007a5780636ae4a8b7146100aa57806377c9623b146100ce578063cd155828146100f2575b600080fd5b610064610122565b6040516100719190610e0e565b60405180910390f35b610094600480360381019061008f9190610ae2565b610214565b6040516100a19190610e0e565b60405180910390f35b6100b2610326565b6040516100c59796959493929190610fb0565b60405180910390f35b6100d66105b6565b6040516100e99796959493929190610fb0565b60405180910390f35b61010c60048036038101906101079190610bb9565b6108b6565b6040516101199190610e0e565b60405180910390f35b6000806000800154141561016b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161016290610f35565b60405180910390fd5b600060040154600060050154106101b7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016101ae90610f15565b60405180910390fd5b60006005016000815480929190600101919050555060008001547f7bf212115d4ce00e41e897b450bbc260c526f138186131ddba049fed057c87bc6000600501546040516102059190610f95565b60405180910390a26001905090565b60008060008001541415801561022d5750856000800154145b61026c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161026390610f75565b60405180910390fd5b84600060010190805190602001906102859291906109d4565b50836000600201908051906020019061029f9291906109d4565b5082600060030190805190602001906102b99291906109d4565b5081600060060190805190602001906102d39291906109d4565b50857fd30f0ae681961da69e31141d63b2c29eccbf03ce0d8a0942737040ce114d07b686868660006005015487604051610311959493929190610e29565b60405180910390a26001905095945050505050565b6000806000015490806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103c65780601f1061039b576101008083540402835291602001916103c6565b820191906000526020600020905b8154815290600101906020018083116103a957829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104645780601f1061043957610100808354040283529160200191610464565b820191906000526020600020905b81548152906001019060200180831161044757829003601f168201915b505050505090806003018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105025780601f106104d757610100808354040283529160200191610502565b820191906000526020600020905b8154815290600101906020018083116104e557829003601f168201915b505050505090806004015490806005015490806006018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105ac5780601f10610581576101008083540402835291602001916105ac565b820191906000526020600020905b81548152906001019060200180831161058f57829003601f168201915b5050505050905087565b60006060806060600080606060008060000154141561060a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161060190610f35565b60405180910390fd5b60008001546000600101600060020160006003016000600401546000600501546000600601858054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106c45780601f10610699576101008083540402835291602001916106c4565b820191906000526020600020905b8154815290600101906020018083116106a757829003601f168201915b50505050509550848054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107605780601f1061073557610100808354040283529160200191610760565b820191906000526020600020905b81548152906001019060200180831161074357829003601f168201915b50505050509450838054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107fc5780601f106107d1576101008083540402835291602001916107fc565b820191906000526020600020905b8154815290600101906020018083116107df57829003601f168201915b50505050509350808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108985780601f1061086d57610100808354040283529160200191610898565b820191906000526020600020905b81548152906001019060200180831161087b57829003601f168201915b50505050509050965096509650965096509650965090919293949596565b6000806000800154146108fe576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108f590610f55565b60405180910390fd5b87600080018190555086600060010190805190602001906109209291906109d4565b50856000600201908051906020019061093a9291906109d4565b5084600060030190805190602001906109549291906109d4565b50836000600401819055508260006005018190555081600060060190805190602001906109829291906109d4565b50877f7ecf02b5e6d53cb06267c5a51919722ab137de97f27cbac6fbe37bb3ea9b7b4e8888888888886040516109bd96959493929190610e98565b60405180910390a260019050979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610a1557805160ff1916838001178555610a43565b82800160010185558215610a43579182015b82811115610a42578251825591602001919060010190610a27565b5b509050610a509190610a54565b5090565b610a7691905b80821115610a72576000816000905550600101610a5a565b5090565b90565b600082601f830112610a8a57600080fd5b8135610a9d610a9882611068565b61103b565b91508082526020830160208301858383011115610ab957600080fd5b610ac48382846110c6565b50505092915050565b600081359050610adc81611119565b92915050565b600080600080600060a08688031215610afa57600080fd5b6000610b0888828901610acd565b955050602086013567ffffffffffffffff811115610b2557600080fd5b610b3188828901610a79565b945050604086013567ffffffffffffffff811115610b4e57600080fd5b610b5a88828901610a79565b935050606086013567ffffffffffffffff811115610b7757600080fd5b610b8388828901610a79565b925050608086013567ffffffffffffffff811115610ba057600080fd5b610bac88828901610a79565b9150509295509295909350565b600080600080600080600060e0888a031215610bd457600080fd5b6000610be28a828b01610acd565b975050602088013567ffffffffffffffff811115610bff57600080fd5b610c0b8a828b01610a79565b965050604088013567ffffffffffffffff811115610c2857600080fd5b610c348a828b01610a79565b955050606088013567ffffffffffffffff811115610c5157600080fd5b610c5d8a828b01610a79565b9450506080610c6e8a828b01610acd565b93505060a0610c7f8a828b01610acd565b92505060c088013567ffffffffffffffff811115610c9c57600080fd5b610ca88a828b01610a79565b91505092959891949750929550565b610cc0816110b0565b82525050565b6000610cd182611094565b610cdb818561109f565b9350610ceb8185602086016110d5565b610cf481611108565b840191505092915050565b6000610d0c601b8361109f565b91507fe5b7b2e8bebee588b0e69c80e5a4a7e585bbe6ae96e695b0e9878f00000000006000830152602082019050919050565b6000610d4c600f8361109f565b91507fe5a4a7e6a39ae4b88de5ad98e59ca800000000000000000000000000000000006000830152602082019050919050565b6000610d8c60128361109f565b91507fe5a4a7e6a39ae5b7b2e7bb8fe5ad98e59ca800000000000000000000000000006000830152602082019050919050565b6000610dcc601d8361109f565b91507fe5a4a7e6a39ae4b88de5ad98e59ca8e688964944e4b88de58cb9e9858d0000006000830152602082019050919050565b610e08816110bc565b82525050565b6000602082019050610e236000830184610cb7565b92915050565b600060a0820190508181036000830152610e438188610cc6565b90508181036020830152610e578187610cc6565b90508181036040830152610e6b8186610cc6565b9050610e7a6060830185610dff565b8181036080830152610e8c8184610cc6565b90509695505050505050565b600060c0820190508181036000830152610eb28189610cc6565b90508181036020830152610ec68188610cc6565b90508181036040830152610eda8187610cc6565b9050610ee96060830186610dff565b610ef66080830185610dff565b81810360a0830152610f088184610cc6565b9050979650505050505050565b60006020820190508181036000830152610f2e81610cff565b9050919050565b60006020820190508181036000830152610f4e81610d3f565b9050919050565b60006020820190508181036000830152610f6e81610d7f565b9050919050565b60006020820190508181036000830152610f8e81610dbf565b9050919050565b6000602082019050610faa6000830184610dff565b92915050565b600060e082019050610fc5600083018a610dff565b8181036020830152610fd78189610cc6565b9050818103604083","0152610feb8188610cc6565b90508181036060830152610fff8187610cc6565b905061100e6080830186610dff565b61101b60a0830185610dff565b81810360c083015261102d8184610cc6565b905098975050505050505050565b6000604051905081810181811067ffffffffffffffff8211171561105e57600080fd5b8060405250919050565b600067ffffffffffffffff82111561107f57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600082825260208201905092915050565b60008115159050919050565b6000819050919050565b82818337600083830152505050565b60005b838110156110f35780820151818401526020810190506110d8565b83811115611102576000848401525b50505050565b6000601f19601f8301169050919050565b611122816110bc565b811461112d57600080fd5b5056fea2646970667358221220cf8504bef224fadbbcf1cba376258503504084cf642ebb7e3fe1e99d720cbae264736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"methodSignatureAsString\":\"BreedingQuantityIncreased(uint256,uint256)\",\"name\":\"BreedingQuantityIncreased\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"newBreedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"PastureCreated(uint256,string,string,string,uint256,uint256,string)\",\"name\":\"PastureCreated\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"name\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"location\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"area\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"bigBreedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"breedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"description\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"PastureUpdated(uint256,string,string,string,uint256,string)\",\"name\":\"PastureUpdated\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"name\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"location\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"area\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"breedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"description\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"createPasture(uint256,string,string,string,uint256,uint256,string)\",\"name\":\"createPasture\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[{\"name\":\"_id\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"_name\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_location\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_area\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_bigBreedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"_breedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"_description\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[{\"name\":\"\",\"type\":\"bool\",\"internalType\":\"bool\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"bool\"}]},{\"methodSignatureAsString\":\"getPasture()\",\"name\":\"getPasture\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"inputs\":[],\"outputs\":[{\"name\":\"\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}]},{\"methodSignatureAsString\":\"increaseBreedingQuantity()\",\"name\":\"increaseBreedingQuantity\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[],\"outputs\":[{\"name\":\"\",\"type\":\"bool\",\"internalType\":\"bool\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"bool\"}]},{\"methodSignatureAsString\":\"pasture()\",\"name\":\"pasture\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"inputs\":[],\"outputs\":[{\"name\":\"id\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"name\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"location\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"area\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"bigBreedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"breedingQuantity\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"description\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}]},{\"methodSignatureAsString\":\"updatePasture(uint256,string,string,string,string)\",\"name\":\"updatePasture\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[{\"name\":\"_id\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"_name\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_location\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_area\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_description\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[{\"name\":\"\",\"type\":\"bool\",\"internalType\":\"bool\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"bool\"}]}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CREATEPASTURE = "createPasture";

    public static final String FUNC_GETPASTURE = "getPasture";

    public static final String FUNC_INCREASEBREEDINGQUANTITY = "increaseBreedingQuantity";

    public static final String FUNC_PASTURE = "pasture";

    public static final String FUNC_UPDATEPASTURE = "updatePasture";

    public static final Event BREEDINGQUANTITYINCREASED_EVENT = new Event("BreedingQuantityIncreased",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PASTURECREATED_EVENT = new Event("PastureCreated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event PASTUREUPDATED_EVENT = new Event("PastureUpdated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
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
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
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
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
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
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
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

    public TransactionReceipt createPasture(BigInteger _id, String _name, String _location, String _area, BigInteger _bigBreedingQuantity, BigInteger _breedingQuantity, String _description) {
        final Function function = new Function(
                FUNC_CREATEPASTURE,
                Arrays.<Type>asList(new Uint256(_id),
                new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Uint256(_bigBreedingQuantity),
                new Uint256(_breedingQuantity),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] createPasture(BigInteger _id, String _name, String _location, String _area, BigInteger _bigBreedingQuantity, BigInteger _breedingQuantity, String _description, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CREATEPASTURE,
                Arrays.<Type>asList(new Uint256(_id),
                new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Uint256(_bigBreedingQuantity),
                new Uint256(_breedingQuantity),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCreatePasture(BigInteger _id, String _name, String _location, String _area, BigInteger _bigBreedingQuantity, BigInteger _breedingQuantity, String _description) {
        final Function function = new Function(
                FUNC_CREATEPASTURE,
                Arrays.<Type>asList(new Uint256(_id),
                new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Uint256(_bigBreedingQuantity),
                new Uint256(_breedingQuantity),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, String> getCreatePastureInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATEPASTURE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, String>(

                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (BigInteger) results.get(5).getValue(),
                (String) results.get(6).getValue()
                );
    }

    public Tuple1<Boolean> getCreatePastureOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_CREATEPASTURE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, String> getPasture() throws ContractException {
        final Function function = new Function(FUNC_GETPASTURE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, String>(
                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (BigInteger) results.get(5).getValue(),
                (String) results.get(6).getValue());
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

    public Tuple1<Boolean> getIncreaseBreedingQuantityOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_INCREASEBREEDINGQUANTITY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, String> pasture() throws ContractException {
        final Function function = new Function(FUNC_PASTURE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple7<BigInteger, String, String, String, BigInteger, BigInteger, String>(
                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (BigInteger) results.get(5).getValue(),
                (String) results.get(6).getValue());
    }

    public TransactionReceipt updatePasture(BigInteger _id, String _name, String _location, String _area, String _description) {
        final Function function = new Function(
                FUNC_UPDATEPASTURE,
                Arrays.<Type>asList(new Uint256(_id),
                new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] updatePasture(BigInteger _id, String _name, String _location, String _area, String _description, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_UPDATEPASTURE,
                Arrays.<Type>asList(new Uint256(_id),
                new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUpdatePasture(BigInteger _id, String _name, String _location, String _area, String _description) {
        final Function function = new Function(
                FUNC_UPDATEPASTURE,
                Arrays.<Type>asList(new Uint256(_id),
                new Utf8String(_name),
                new Utf8String(_location),
                new Utf8String(_area),
                new Utf8String(_description)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple5<BigInteger, String, String, String, String> getUpdatePastureInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_UPDATEPASTURE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple5<BigInteger, String, String, String, String>(

                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (String) results.get(4).getValue()
                );
    }

    public Tuple1<Boolean> getUpdatePastureOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_UPDATEPASTURE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
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

        public BigInteger id;

        public BigInteger newBreedingQuantity;
    }

    public static class PastureCreatedEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger id;

        public String name;

        public String location;

        public String area;

        public BigInteger bigBreedingQuantity;

        public BigInteger breedingQuantity;

        public String description;
    }

    public static class PastureUpdatedEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger id;

        public String name;

        public String location;

        public String area;

        public BigInteger breedingQuantity;

        public String description;
    }
}
