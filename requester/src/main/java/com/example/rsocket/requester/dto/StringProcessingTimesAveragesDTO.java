package com.example.rsocket.requester.dto;

import com.example.rsocket.requester.constants.CommunicationType;
import com.example.rsocket.requester.constants.OperationType;

import java.io.Serializable;
import java.text.DecimalFormat;

public class StringProcessingTimesAveragesDTO implements Serializable {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.##");

    private final CommunicationType communicationType;
    private final OperationType operationType;
    private final int bundleEntries;
    private final int stringSizeInBytes;
    private final long totalTime;
    private final double commTime;
    private final String commTimePercentage;
    private final Double serializingTime;
    private final String serializingTimePercentage;
    private final Double deSerializingTime;
    private final String deSerializingTimePercentage;

    public StringProcessingTimesAveragesDTO(CommunicationType communicationType, OperationType operationType,
                                            int bundleEntries, int stringSizeInBytes, long totalTime, double commTime,
                                            Double serializingTime, Double deSerializingTime) {
        this.communicationType = communicationType;
        this.operationType = operationType;
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

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public String getOperationType() {
        return operationType.getName().toLowerCase();
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

    public double getCommTime() {
        return commTime;
    }

    public String getCommTimePercentage() {
        return commTimePercentage;
    }

    public Double getSerializingTime() {
        return serializingTime;
    }

    public String getSerializingTimePercentage() {
        return serializingTimePercentage;
    }

    public Double getDeSerializingTime() {
        return deSerializingTime;
    }

    public String getDeserializingTimePercentage() {
        return deSerializingTimePercentage;
    }
}
