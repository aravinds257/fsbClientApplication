package com.fsbtech.interviews.entities;

import java.io.Serializable;

public class SubCategory implements Serializable
{
    private final Integer id;
    private final String ref;
    private final Category category;

    public SubCategory(Integer id, String ref, Category category)
    {
        this.id = id;
        this.ref = ref;
        this.category = category;
    }

    public Integer getId()
    {
        return id;
    }

    public String getRef()
    {
        return ref;
    }

    public Category getCategory()
    {
        return category;
    }

    @Override
    public String toString() {
        return "SubCategory{" +
                "id=" + id +
                ", ref='" + ref + '\'' +
                ", category=" + category +
                '}';
    }
}
