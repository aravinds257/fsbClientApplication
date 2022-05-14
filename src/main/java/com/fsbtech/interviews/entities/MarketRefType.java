package com.fsbtech.interviews.entities;

import java.io.Serializable;

public class MarketRefType implements Serializable
{
    private final Integer marketRefId;
    private final String marketRefName;

    public MarketRefType(Integer marketRefId, String marketRefName)
    {
        this.marketRefId = marketRefId;
        this.marketRefName = marketRefName;
    }

    public Integer getMarketRefId() 
    {
        return marketRefId;
    }

    public String getMarketRefName() 
    {
        return marketRefName;
    }

    @Override
    public String toString() {
        return "MarketRefType{" +
                "marketRefId=" + marketRefId +
                ", marketRefName='" + marketRefName + '\'' +
                '}';
    }
}
