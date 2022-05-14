package com.fsbtech.interviews.entities;

import java.io.Serializable;
import java.util.Collection;

public class Event implements Serializable
{
    private final Integer id;
    private final String name;
    private final SubCategory subCategory;
    private final Collection<MarketRefType> marketRefTypes;
    private final Boolean completed;

    public Event(Integer id, String name, SubCategory subCategory, Collection<MarketRefType> marketRefTypes, Boolean completed)
    {
        this.id = id;
        this.name = name;
        this.subCategory = subCategory;
        this.marketRefTypes = marketRefTypes;
        this.completed = completed;
    }

    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public SubCategory getSubCategory()
    {
        return subCategory;
    }

    public Boolean getCompleted() 
    { 
	return completed; 
    }

    public Collection<MarketRefType> getMarketRefTypes() {
        return marketRefTypes;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subCategory=" + subCategory +
                ", marketRefTypes=" + marketRefTypes +
                ", completed=" + completed +
                "}\n";
    }
}
