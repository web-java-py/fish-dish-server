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
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple12;
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
    public static final String[] BINARY_ARRAY = {};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"methodSignatureAsString\":\"SensorAlertCreated(uint128,string,string,string,string,string,uint32)\",\"name\":\"SensorAlertCreated\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"inputs\":[{\"name\":\"id\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"alertType\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"alertMessage\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"paramName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"paramValue\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"alertTime\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"alertLevel\",\"type\":\"uint32\",\"internalType\":\"uint32\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint32\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"SensorAlertCreate(uint128,string,string,string,string,uint128,uint128,string,string,uint128,string,uint32)\",\"name\":\"SensorAlertCreate\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[{\"name\":\"_id\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"_alertType\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_alertMessage\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_paramName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_paramValue\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_thresholdMax\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"_thresholdMin\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"_pastureId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_batchId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_deviceId\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"_alertTime\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_alertLevel\",\"type\":\"uint32\",\"internalType\":\"uint32\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint32\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"currentAlert()\",\"name\":\"currentAlert\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"inputs\":[],\"outputs\":[{\"name\":\"id\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"alertType\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"alertMessage\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"paramName\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"paramValue\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"thresholdMax\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"thresholdMin\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"pastureId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"batchId\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"deviceId\",\"type\":\"uint128\",\"internalType\":\"uint128\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint128\"},{\"name\":\"alertTime\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"alertLevel\",\"type\":\"uint32\",\"internalType\":\"uint32\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint32\"}]}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_SENSORALERTCREATE = "SensorAlertCreate";

    public static final String FUNC_CURRENTALERT = "currentAlert";

    public static final Event SENSORALERTCREATED_EVENT = new Event("SensorAlertCreated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint128>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint32>() {}));
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
            typedResponse.alertType = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.alertMessage = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.paramName = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.paramValue = (String) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.alertTime = (String) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.alertLevel = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
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

    public TransactionReceipt SensorAlertCreate(BigInteger _id, String _alertType, String _alertMessage, String _paramName, String _paramValue, BigInteger _thresholdMax, BigInteger _thresholdMin, String _pastureId, String _batchId, BigInteger _deviceId, String _alertTime, BigInteger _alertLevel) {
        final Function function = new Function(
                FUNC_SENSORALERTCREATE,
                Arrays.<Type>asList(new Uint128(_id),
                new Utf8String(_alertType),
                new Utf8String(_alertMessage),
                new Utf8String(_paramName),
                new Utf8String(_paramValue),
                new Uint128(_thresholdMax),
                new Uint128(_thresholdMin),
                new Utf8String(_pastureId),
                new Utf8String(_batchId),
                new Uint128(_deviceId),
                new Utf8String(_alertTime),
                new Uint32(_alertLevel)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] SensorAlertCreate(BigInteger _id, String _alertType, String _alertMessage, String _paramName, String _paramValue, BigInteger _thresholdMax, BigInteger _thresholdMin, String _pastureId, String _batchId, BigInteger _deviceId, String _alertTime, BigInteger _alertLevel, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SENSORALERTCREATE,
                Arrays.<Type>asList(new Uint128(_id),
                new Utf8String(_alertType),
                new Utf8String(_alertMessage),
                new Utf8String(_paramName),
                new Utf8String(_paramValue),
                new Uint128(_thresholdMax),
                new Uint128(_thresholdMin),
                new Utf8String(_pastureId),
                new Utf8String(_batchId),
                new Uint128(_deviceId),
                new Utf8String(_alertTime),
                new Uint32(_alertLevel)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSensorAlertCreate(BigInteger _id, String _alertType, String _alertMessage, String _paramName, String _paramValue, BigInteger _thresholdMax, BigInteger _thresholdMin, String _pastureId, String _batchId, BigInteger _deviceId, String _alertTime, BigInteger _alertLevel) {
        final Function function = new Function(
                FUNC_SENSORALERTCREATE,
                Arrays.<Type>asList(new Uint128(_id),
                new Utf8String(_alertType),
                new Utf8String(_alertMessage),
                new Utf8String(_paramName),
                new Utf8String(_paramValue),
                new Uint128(_thresholdMax),
                new Uint128(_thresholdMin),
                new Utf8String(_pastureId),
                new Utf8String(_batchId),
                new Uint128(_deviceId),
                new Utf8String(_alertTime),
                new Uint32(_alertLevel)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple12<BigInteger, String, String, String, String, BigInteger, BigInteger, String, String, BigInteger, String, BigInteger> getSensorAlertCreateInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SENSORALERTCREATE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint128>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint128>() {}, new TypeReference<Uint128>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint128>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint32>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple12<BigInteger, String, String, String, String, BigInteger, BigInteger, String, String, BigInteger, String, BigInteger>(

                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (String) results.get(4).getValue(),
                (BigInteger) results.get(5).getValue(),
                (BigInteger) results.get(6).getValue(),
                (String) results.get(7).getValue(),
                (String) results.get(8).getValue(),
                (BigInteger) results.get(9).getValue(),
                (String) results.get(10).getValue(),
                (BigInteger) results.get(11).getValue()
                );
    }

    public Tuple12<BigInteger, String, String, String, String, BigInteger, BigInteger, String, String, BigInteger, String, BigInteger> currentAlert() throws ContractException {
        final Function function = new Function(FUNC_CURRENTALERT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint128>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint128>() {}, new TypeReference<Uint128>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint128>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint32>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple12<BigInteger, String, String, String, String, BigInteger, BigInteger, String, String, BigInteger, String, BigInteger>(
                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (String) results.get(4).getValue(),
                (BigInteger) results.get(5).getValue(),
                (BigInteger) results.get(6).getValue(),
                (String) results.get(7).getValue(),
                (String) results.get(8).getValue(),
                (BigInteger) results.get(9).getValue(),
                (String) results.get(10).getValue(),
                (BigInteger) results.get(11).getValue());
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

        public String alertType;

        public String alertMessage;

        public String paramName;

        public String paramValue;

        public String alertTime;

        public BigInteger alertLevel;
    }
}
