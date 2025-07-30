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
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint128;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint32;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple5;
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
public class AgricultureDeviceSensorAlertFB extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506107be806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80633ea400ba1461003b578063f15827881461005d575b600080fd5b610043610079565b6040516100549594939291906105fe565b60405180910390f35b610077600480360381019061007291906104a3565b610291565b005b60008060000160009054906101000a90046fffffffffffffffffffffffffffffffff1690806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101355780601f1061010a57610100808354040283529160200191610135565b820191906000526020600020905b81548152906001019060200180831161011857829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101d35780601f106101a8576101008083540402835291602001916101d3565b820191906000526020600020905b8154815290600101906020018083116101b657829003601f168201915b505050505090806003018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102715780601f1061024657610100808354040283529160200191610271565b820191906000526020600020905b81548152906001019060200180831161025457829003601f168201915b5050505050908060040160009054906101000a900463ffffffff16905085565b846000800160006101000a8154816fffffffffffffffffffffffffffffffff02191690836fffffffffffffffffffffffffffffffff16021790555083600060010190805190602001906102e5929190610380565b5082600060020190805190602001906102ff929190610380565b508160006003019080519060200190610319929190610380565b5080600060040160006101000a81548163ffffffff021916908363ffffffff1602179055507f29e29b882391a3e72e24b8e3297449394c55c0cee9a025b5b8978f21b7437793858584604051610371939291906105b9565b60405180910390a15050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106103c157805160ff19168380011785556103ef565b828001600101855582156103ef579182015b828111156103ee5782518255916020019190600101906103d3565b5b5090506103fc9190610400565b5090565b61042291905b8082111561041e576000816000905550600101610406565b5090565b90565b600082601f83011261043657600080fd5b813561044961044482610693565b610666565b9150808252602083016020830185838301111561046557600080fd5b610470838284610707565b50505092915050565b6000813590506104888161075a565b92915050565b60008135905061049d81610771565b92915050565b600080600080600060a086880312156104bb57600080fd5b60006104c988828901610479565b955050602086013567ffffffffffffffff8111156104e657600080fd5b6104f288828901610425565b945050604086013567ffffffffffffffff81111561050f57600080fd5b61051b88828901610425565b935050606086013567ffffffffffffffff81111561053857600080fd5b61054488828901610425565b92505060806105558882890161048e565b9150509295509295909350565b600061056d826106bf565b61057781856106ca565b9350610587818560208601610716565b61059081610749565b840191505092915050565b6105a4816106db565b82525050565b6105b3816106f7565b82525050565b60006060820190506105ce600083018661059b565b81810360208301526105e08185610562565b905081810360408301526105f48184610562565b9050949350505050565b600060a082019050610613600083018861059b565b81810360208301526106258187610562565b905081810360408301526106398186610562565b9050818103606083015261064d8185610562565b905061065c60808301846105aa565b9695505050505050565b6000604051905081810181811067ffffffffffffffff8211171561068957600080fd5b8060405250919050565b600067ffffffffffffffff8211156106aa57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600082825260208201905092915050565b60006fffffffffffffffffffffffffffffffff82169050919050565b600063ffffffff82169050919050565b82818337600083830152505050565b60005b83811015610734578082015181840152602081019050610719565b83811115610743576000848401525b50505050565b6000601f19601f8301169050919050565b610763816106db565b811461076e57600080fd5b50565b61077a816106f7565b811461078557600080fd5b5056fea2646970667358221220cdbe3f3a1ccfdf27966d0325325ea39a86059d542460fedcf908b8def541f1fa64736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b506107be806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80633ea400ba1461003b578063f15827881461005d575b600080fd5b610043610079565b6040516100549594939291906105fe565b60405180910390f35b610077600480360381019061007291906104a3565b610291565b005b60008060000160009054906101000a90046fffffffffffffffffffffffffffffffff1690806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101355780601f1061010a57610100808354040283529160200191610135565b820191906000526020600020905b81548152906001019060200180831161011857829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101d35780601f106101a8576101008083540402835291602001916101d3565b820191906000526020600020905b8154815290600101906020018083116101b657829003601f168201915b505050505090806003018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102715780601f1061024657610100808354040283529160200191610271565b820191906000526020600020905b81548152906001019060200180831161025457829003601f168201915b5050505050908060040160009054906101000a900463ffffffff16905085565b846000800160006101000a8154816fffffffffffffffffffffffffffffffff02191690836fffffffffffffffffffffffffffffffff16021790555083600060010190805190602001906102e5929190610380565b5082600060020190805190602001906102ff929190610380565b508160006003019080519060200190610319929190610380565b5080600060040160006101000a81548163ffffffff021916908363ffffffff1602179055507f29e29b882391a3e72e24b8e3297449394c55c0cee9a025b5b8978f21b7437793858584604051610371939291906105b9565b60405180910390a15050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106103c157805160ff19168380011785556103ef565b828001600101855582156103ef579182015b828111156103ee5782518255916020019190600101906103d3565b5b5090506103fc9190610400565b5090565b61042291905b8082111561041e576000816000905550600101610406565b5090565b90565b600082601f83011261043657600080fd5b813561044961044482610693565b610666565b9150808252602083016020830185838301111561046557600080fd5b610470838284610707565b50505092915050565b6000813590506104888161075a565b92915050565b60008135905061049d81610771565b92915050565b600080600080600060a086880312156104bb57600080fd5b60006104c988828901610479565b955050602086013567ffffffffffffffff8111156104e657600080fd5b6104f288828901610425565b945050604086013567ffffffffffffffff81111561050f57600080fd5b61051b88828901610425565b935050606086013567ffffffffffffffff81111561053857600080fd5b61054488828901610425565b92505060806105558882890161048e565b9150509295509295909350565b600061056d826106bf565b61057781856106ca565b9350610587818560208601610716565b61059081610749565b840191505092915050565b6105a4816106db565b82525050565b6105b3816106f7565b82525050565b60006060820190506105ce600083018661059b565b81810360208301526105e08185610562565b905081810360408301526105f48184610562565b9050949350505050565b600060a082019050610613600083018861059b565b81810360208301526106258187610562565b905081810360408301526106398186610562565b9050818103606083015261064d8185610562565b905061065c60808301846105aa565b9695505050505050565b6000604051905081810181811067ffffffffffffffff8211171561068957600080fd5b8060405250919050565b600067ffffffffffffffff8211156106aa57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600082825260208201905092915050565b60006fffffffffffffffffffffffffffffffff82169050919050565b600063ffffffff82169050919050565b82818337600083830152505050565b60005b83811015610734578082015181840152602081019050610719565b83811115610743576000848401525b50505050565b6000601f19601f8301169050919050565b610763816106db565b811461076e57600080fd5b50565b61077a816106f7565b811461078557600080fd5b5056fea2646970667358221220cdbe3f3a1ccfdf27966d0325325ea39a86059d542460fedcf908b8def541f1fa64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"methodSignatureAsString\":\"SensorAlertCreated(uint128,string,string)\",\"name\":\"SensorAlertCreated\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"inputs\":[{\"name\":\"id\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"deviceName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"alertMessage\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"SensorAlertCreate(uint128,string,string,string,uint32)\",\"name\":\"SensorAlertCreate\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[{\"name\":\"_id\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"_deviceName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_alertType\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_alertMessage\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_alertLevel\",\"type\":\"uint32\",\"internalType\":\"uint32\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint32\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"sensorAlert()\",\"name\":\"sensorAlert\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"inputs\":[],\"outputs\":[{\"name\":\"id\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"deviceName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"alertType\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"alertMessage\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"alertLevel\",\"type\":\"uint32\",\"internalType\":\"uint32\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint32\"}]}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_SENSORALERTCREATE = "SensorAlertCreate";

    public static final String FUNC_SENSORALERT = "sensorAlert";

    public static final Event SENSORALERTCREATED_EVENT = new Event("SensorAlertCreated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint128>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected AgricultureDeviceSensorAlertFB(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List<SensorAlertCreatedEventResponse> getSensorAlertCreatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(SENSORALERTCREATED_EVENT, transactionReceipt);
        ArrayList<SensorAlertCreatedEventResponse> responses = new ArrayList<SensorAlertCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SensorAlertCreatedEventResponse typedResponse = new SensorAlertCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.deviceName = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.alertMessage = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeSensorAlertCreatedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(SENSORALERTCREATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeSensorAlertCreatedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(SENSORALERTCREATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public TransactionReceipt SensorAlertCreate(BigInteger _id, String _deviceName, String _alertType, String _alertMessage, BigInteger _alertLevel) {
        final Function function = new Function(
                FUNC_SENSORALERTCREATE,
                Arrays.<Type>asList(new Uint128(_id),
                new Utf8String(_deviceName),
                new Utf8String(_alertType),
                new Utf8String(_alertMessage),
                new Uint32(_alertLevel)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] SensorAlertCreate(BigInteger _id, String _deviceName, String _alertType, String _alertMessage, BigInteger _alertLevel, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SENSORALERTCREATE,
                Arrays.<Type>asList(new Uint128(_id),
                new Utf8String(_deviceName),
                new Utf8String(_alertType),
                new Utf8String(_alertMessage),
                new Uint32(_alertLevel)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSensorAlertCreate(BigInteger _id, String _deviceName, String _alertType, String _alertMessage, BigInteger _alertLevel) {
        final Function function = new Function(
                FUNC_SENSORALERTCREATE,
                Arrays.<Type>asList(new Uint128(_id),
                new Utf8String(_deviceName),
                new Utf8String(_alertType),
                new Utf8String(_alertMessage),
                new Uint32(_alertLevel)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple5<BigInteger, String, String, String, BigInteger> getSensorAlertCreateInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SENSORALERTCREATE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint128>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple5<BigInteger, String, String, String, BigInteger>(

                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue()
                );
    }

    public Tuple5<BigInteger, String, String, String, BigInteger> sensorAlert() throws ContractException {
        final Function function = new Function(FUNC_SENSORALERT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint128>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint32>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple5<BigInteger, String, String, String, BigInteger>(
                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue());
    }

    public static AgricultureDeviceSensorAlertFB load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new AgricultureDeviceSensorAlertFB(contractAddress, client, credential);
    }

    public static AgricultureDeviceSensorAlertFB deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(AgricultureDeviceSensorAlertFB.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class SensorAlertCreatedEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger id;

        public String deviceName;

        public String alertMessage;
    }
}
