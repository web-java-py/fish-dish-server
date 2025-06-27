package com.server.fisco.bcos;

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
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
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
public class AgricultureDeviceFB extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610a88806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80634e9d582d146100515780638ea286c21461006d578063c6326dd01461008e578063d2a580a4146100af575b600080fd5b61006b6004803603810190610066919061082d565b6100cb565b005b610075610175565b6040516100859493929190610929565b60405180910390f35b61009661040c565b6040516100a69493929190610929565b60405180910390f35b6100c960048036038101906100c4919061082d565b61068a565b005b836000800190805190602001906100e3929190610734565b5082600060010190805190602001906100fd929190610734565b508160006002019080519060200190610117929190610734565b508060006003019080519060200190610131929190610734565b507fcc84b3b60ff34a9db9f623d174c809bcfc04e3605120ba1e22a072a51f88c05b848484846040516101679493929190610929565b60405180910390a150505050565b60608060608060008001600060010160006002016000600301838054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102235780601f106101f857610100808354040283529160200191610223565b820191906000526020600020905b81548152906001019060200180831161020657829003601f168201915b50505050509350828054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102bf5780601f10610294576101008083540402835291602001916102bf565b820191906000526020600020905b8154815290600101906020018083116102a257829003601f168201915b50505050509250818054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561035b5780601f106103305761010080835404028352916020019161035b565b820191906000526020600020905b81548152906001019060200180831161033e57829003601f168201915b50505050509150808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103f75780601f106103cc576101008083540402835291602001916103f7565b820191906000526020600020905b8154815290600101906020018083116103da57829003601f168201915b50505050509050935093509350935090919293565b6000806000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104a65780601f1061047b576101008083540402835291602001916104a6565b820191906000526020600020905b81548152906001019060200180831161048957829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105445780601f1061051957610100808354040283529160200191610544565b820191906000526020600020905b81548152906001019060200180831161052757829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105e25780601f106105b7576101008083540402835291602001916105e2565b820191906000526020600020905b8154815290600101906020018083116105c557829003601f168201915b505050505090806003018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106805780601f1061065557610100808354040283529160200191610680565b820191906000526020600020905b81548152906001019060200180831161066357829003601f168201915b5050505050905084565b836000800190805190602001906106a2929190610734565b5082600060010190805190602001906106bc929190610734565b5081600060020190805190602001906106d6929190610734565b5080600060030190805190602001906106f0929190610734565b507fc5227f1b07c29cfa7240d2acc68a0e0370ce7e2a4aed4300dbbc0d326525444f848484846040516107269493929190610929565b60405180910390a150505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061077557805160ff19168380011785556107a3565b828001600101855582156107a3579182015b828111156107a2578251825591602001919060010190610787565b5b5090506107b091906107b4565b5090565b6107d691905b808211156107d25760008160009055506001016107ba565b5090565b90565b600082601f8301126107ea57600080fd5b81356107fd6107f8826109b7565b61098a565b9150808252602083016020830185838301111561081957600080fd5b6108248382846109ff565b50505092915050565b6000806000806080858703121561084357600080fd5b600085013567ffffffffffffffff81111561085d57600080fd5b610869878288016107d9565b945050602085013567ffffffffffffffff81111561088657600080fd5b610892878288016107d9565b935050604085013567ffffffffffffffff8111156108af57600080fd5b6108bb878288016107d9565b925050606085013567ffffffffffffffff8111156108d857600080fd5b6108e4878288016107d9565b91505092959194509250565b60006108fb826109e3565b61090581856109ee565b9350610915818560208601610a0e565b61091e81610a41565b840191505092915050565b6000608082019050818103600083015261094381876108f0565b9050818103602083015261095781866108f0565b9050818103604083015261096b81856108f0565b9050818103606083015261097f81846108f0565b905095945050505050565b6000604051905081810181811067ffffffffffffffff821117156109ad57600080fd5b8060405250919050565b600067ffffffffffffffff8211156109ce57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600082825260208201905092915050565b82818337600083830152505050565b60005b83811015610a2c578082015181840152602081019050610a11565b83811115610a3b576000848401525b50505050565b6000601f19601f830116905091905056fea26469706673582212201cbf8649492c248df0fa98b691dcdc519b0fdb9a1d05785a7790c5fafeacd1c664736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610a88806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80634e9d582d146100515780638ea286c21461006d578063c6326dd01461008e578063d2a580a4146100af575b600080fd5b61006b6004803603810190610066919061082d565b6100cb565b005b610075610175565b6040516100859493929190610929565b60405180910390f35b61009661040c565b6040516100a69493929190610929565b60405180910390f35b6100c960048036038101906100c4919061082d565b61068a565b005b836000800190805190602001906100e3929190610734565b5082600060010190805190602001906100fd929190610734565b508160006002019080519060200190610117929190610734565b508060006003019080519060200190610131929190610734565b507fcc84b3b60ff34a9db9f623d174c809bcfc04e3605120ba1e22a072a51f88c05b848484846040516101679493929190610929565b60405180910390a150505050565b60608060608060008001600060010160006002016000600301838054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102235780601f106101f857610100808354040283529160200191610223565b820191906000526020600020905b81548152906001019060200180831161020657829003601f168201915b50505050509350828054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102bf5780601f10610294576101008083540402835291602001916102bf565b820191906000526020600020905b8154815290600101906020018083116102a257829003601f168201915b50505050509250818054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561035b5780601f106103305761010080835404028352916020019161035b565b820191906000526020600020905b81548152906001019060200180831161033e57829003601f168201915b50505050509150808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103f75780601f106103cc576101008083540402835291602001916103f7565b820191906000526020600020905b8154815290600101906020018083116103da57829003601f168201915b50505050509050935093509350935090919293565b6000806000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104a65780601f1061047b576101008083540402835291602001916104a6565b820191906000526020600020905b81548152906001019060200180831161048957829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105445780601f1061051957610100808354040283529160200191610544565b820191906000526020600020905b81548152906001019060200180831161052757829003601f168201915b505050505090806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105e25780601f106105b7576101008083540402835291602001916105e2565b820191906000526020600020905b8154815290600101906020018083116105c557829003601f168201915b505050505090806003018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106805780601f1061065557610100808354040283529160200191610680565b820191906000526020600020905b81548152906001019060200180831161066357829003601f168201915b5050505050905084565b836000800190805190602001906106a2929190610734565b5082600060010190805190602001906106bc929190610734565b5081600060020190805190602001906106d6929190610734565b5080600060030190805190602001906106f0929190610734565b507fc5227f1b07c29cfa7240d2acc68a0e0370ce7e2a4aed4300dbbc0d326525444f848484846040516107269493929190610929565b60405180910390a150505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061077557805160ff19168380011785556107a3565b828001600101855582156107a3579182015b828111156107a2578251825591602001919060010190610787565b5b5090506107b091906107b4565b5090565b6107d691905b808211156107d25760008160009055506001016107ba565b5090565b90565b600082601f8301126107ea57600080fd5b81356107fd6107f8826109b7565b61098a565b9150808252602083016020830185838301111561081957600080fd5b6108248382846109ff565b50505092915050565b6000806000806080858703121561084357600080fd5b600085013567ffffffffffffffff81111561085d57600080fd5b610869878288016107d9565b945050602085013567ffffffffffffffff81111561088657600080fd5b610892878288016107d9565b935050604085013567ffffffffffffffff8111156108af57600080fd5b6108bb878288016107d9565b925050606085013567ffffffffffffffff8111156108d857600080fd5b6108e4878288016107d9565b91505092959194509250565b60006108fb826109e3565b61090581856109ee565b9350610915818560208601610a0e565b61091e81610a41565b840191505092915050565b6000608082019050818103600083015261094381876108f0565b9050818103602083015261095781866108f0565b9050818103604083015261096b81856108f0565b9050818103606083015261097f81846108f0565b905095945050505050565b6000604051905081810181811067ffffffffffffffff821117156109ad57600080fd5b8060405250919050565b600067ffffffffffffffff8211156109ce57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600082825260208201905092915050565b82818337600083830152505050565b60005b83811015610a2c578082015181840152602081019050610a11565b83811115610a3b576000848401525b50505050565b6000601f19601f830116905091905056fea26469706673582212201cbf8649492c248df0fa98b691dcdc519b0fdb9a1d05785a7790c5fafeacd1c664736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"methodSignatureAsString\":\"DeviceCreated(string,string,string,string)\",\"name\":\"DeviceCreated\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"inputs\":[{\"name\":\"pastureId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"batchId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"deviceTypeId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"deviceName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"DeviceUpdated(string,string,string,string)\",\"name\":\"DeviceUpdated\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"inputs\":[{\"name\":\"pastureId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"batchId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"deviceTypeId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"deviceName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"createDevice(string,string,string,string)\",\"name\":\"createDevice\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[{\"name\":\"_pastureId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_batchId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_deviceTypeId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_deviceName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"device()\",\"name\":\"device\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"inputs\":[],\"outputs\":[{\"name\":\"pastureId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"batchId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"deviceTypeId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"deviceName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}]},{\"methodSignatureAsString\":\"getDevice()\",\"name\":\"getDevice\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"inputs\":[],\"outputs\":[{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}]},{\"methodSignatureAsString\":\"updateDevice(string,string,string,string)\",\"name\":\"updateDevice\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[{\"name\":\"_pastureId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_batchId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_deviceTypeId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_deviceName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CREATEDEVICE = "createDevice";

    public static final String FUNC_DEVICE = "device";

    public static final String FUNC_GETDEVICE = "getDevice";

    public static final String FUNC_UPDATEDEVICE = "updateDevice";

    public static final Event DEVICECREATED_EVENT = new Event("DeviceCreated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event DEVICEUPDATED_EVENT = new Event("DeviceUpdated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected AgricultureDeviceFB(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List<DeviceCreatedEventResponse> getDeviceCreatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(DEVICECREATED_EVENT, transactionReceipt);
        ArrayList<DeviceCreatedEventResponse> responses = new ArrayList<DeviceCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DeviceCreatedEventResponse typedResponse = new DeviceCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pastureId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.batchId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.deviceTypeId = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.deviceName = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeDeviceCreatedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(DEVICECREATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeDeviceCreatedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(DEVICECREATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<DeviceUpdatedEventResponse> getDeviceUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(DEVICEUPDATED_EVENT, transactionReceipt);
        ArrayList<DeviceUpdatedEventResponse> responses = new ArrayList<DeviceUpdatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DeviceUpdatedEventResponse typedResponse = new DeviceUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.pastureId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.batchId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.deviceTypeId = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.deviceName = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeDeviceUpdatedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(DEVICEUPDATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeDeviceUpdatedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(DEVICEUPDATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public TransactionReceipt createDevice(String _pastureId, String _batchId, String _deviceTypeId, String _deviceName) {
        final Function function = new Function(
                FUNC_CREATEDEVICE,
                Arrays.<Type>asList(new Utf8String(_pastureId),
                new Utf8String(_batchId),
                new Utf8String(_deviceTypeId),
                new Utf8String(_deviceName)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] createDevice(String _pastureId, String _batchId, String _deviceTypeId, String _deviceName, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CREATEDEVICE,
                Arrays.<Type>asList(new Utf8String(_pastureId),
                new Utf8String(_batchId),
                new Utf8String(_deviceTypeId),
                new Utf8String(_deviceName)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCreateDevice(String _pastureId, String _batchId, String _deviceTypeId, String _deviceName) {
        final Function function = new Function(
                FUNC_CREATEDEVICE,
                Arrays.<Type>asList(new Utf8String(_pastureId),
                new Utf8String(_batchId),
                new Utf8String(_deviceTypeId),
                new Utf8String(_deviceName)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<String, String, String, String> getCreateDeviceInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATEDEVICE,
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

    public Tuple4<String, String, String, String> device() throws ContractException {
        final Function function = new Function(FUNC_DEVICE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple4<String, String, String, String>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue());
    }

    public Tuple4<String, String, String, String> getDevice() throws ContractException {
        final Function function = new Function(FUNC_GETDEVICE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple4<String, String, String, String>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue());
    }

    public TransactionReceipt updateDevice(String _pastureId, String _batchId, String _deviceTypeId, String _deviceName) {
        final Function function = new Function(
                FUNC_UPDATEDEVICE,
                Arrays.<Type>asList(new Utf8String(_pastureId),
                new Utf8String(_batchId),
                new Utf8String(_deviceTypeId),
                new Utf8String(_deviceName)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] updateDevice(String _pastureId, String _batchId, String _deviceTypeId, String _deviceName, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_UPDATEDEVICE,
                Arrays.<Type>asList(new Utf8String(_pastureId),
                new Utf8String(_batchId),
                new Utf8String(_deviceTypeId),
                new Utf8String(_deviceName)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUpdateDevice(String _pastureId, String _batchId, String _deviceTypeId, String _deviceName) {
        final Function function = new Function(
                FUNC_UPDATEDEVICE,
                Arrays.<Type>asList(new Utf8String(_pastureId),
                new Utf8String(_batchId),
                new Utf8String(_deviceTypeId),
                new Utf8String(_deviceName)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<String, String, String, String> getUpdateDeviceInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_UPDATEDEVICE,
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

    public static AgricultureDeviceFB load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new AgricultureDeviceFB(contractAddress, client, credential);
    }

    public static AgricultureDeviceFB deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(AgricultureDeviceFB.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class DeviceCreatedEventResponse {
        public TransactionReceipt.Logs log;

        public String pastureId;

        public String batchId;

        public String deviceTypeId;

        public String deviceName;
    }

    public static class DeviceUpdatedEventResponse {
        public TransactionReceipt.Logs log;

        public String pastureId;

        public String batchId;

        public String deviceTypeId;

        public String deviceName;
    }
}
