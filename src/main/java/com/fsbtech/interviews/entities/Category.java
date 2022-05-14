package com.fsbtech.interviews.entities;

import java.io.Serializable;

public class Category implements Serializable
{
    private final Integer id;
    private final String ref;

    public Category(Integer id, String ref)
    {
        this.id = id;
        this.ref = ref;
    }

    public Integer getId()
    {
        return id;
    }

    public String getRef()
    {
        return ref;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", ref='" + ref + '\'' +
                '}';
    }
}
