package com.example.rsocket.requester.dto;

import com.example.rsocket.requester.constants.CommunicationType;
import com.example.rsocket.requester.constants.OperationType;

import java.io.Serializable;
import java.text.DecimalFormat;

public class StringProcessingTimesDTO implements Serializable {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.##");

    private final OperationType operationType;
    private final CommunicationType communicationType;

    private final int bundleEntries;
    private final int stringSizeInBytes;
    private final long totalTime;
    private final int commTime;
    private final String commTimePercentage;
    private final Integer serializingTime;
    private final String serializingTimePercentage;
    private final Integer deSerializingTime;
    private final String deSerializingTimePercentage;

    public StringProcessingTimesDTO(OperationType operationType, CommunicationType communicationType, int bundleEntries,
                                    int stringSizeInBytes, long totalTime, int commTime, Integer serializingTime,
                                    Integer deSerializingTime) {
        this.operationType = operationType;
        this.communicationType = communicationType;

        this.bundleEntries = bundleEntries;
        this.stringSizeInBytes = stringSizeInBytes;
        this.totalTime = totalTime;

        this.commTime = commTime;
        this.commTimePercentage = DECIMAL_FORMAT.format(commTime * 100 / totalTime) + "%";

        this.serializingTime = serializingTime;
        this.serializingTimePercentage = serializingTime != null ?
                DECIMAL_FORMAT.format(serializingTime * 100 / totalTime) + "%" : null;

        this.deSerializingTime = deSerializingTime;
        this.deSerializingTimePercentage = deSerializingTime != null ?
                DECIMAL_FORMAT.format(deSerializingTime * 100 / totalTime) + "%" : null;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public int getBundleEntries() {
        return bundleEntries;
    }

    public int getStringSizeInBytes() {
        return stringSizeInBytes;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public int getCommTime() {
        return commTime;
    }

    public String getCommTimePercentage() {
        return commTimePercentage;
    }

    public Integer getSerializingTime() {
        return serializingTime;
    }

    public String getSerializingTimePercentage() {
        return serializingTimePercentage;
    }

    public Integer getDeSerializingTime() {
        return deSerializingTime;
    }

    public String getDeSerializingTimePercentage() {
        return deSerializingTimePercentage;
    }
}
