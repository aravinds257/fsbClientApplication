package com.fsbtech.interviews;

import com.fsbtech.interviews.entities.Event;
import com.fsbtech.interviews.entities.MarketRefType;

import java.util.Collection;

public interface Client
{
    /**
     * Adding event to event repository
     *
     * @param event - Event object representing a new Event to add to the repository.
     */
    public void addEvent(Event event);

    /**
     * Update event status to completed. 
     *
     * @param id - Identifier for event to be marked as completed.
     */
    public void eventCompleted(Integer id);

    /**
     * Add marketRefType to an existing event
     *
     * @param id - Identifier for event to add marketRefType to.
     * @param marketRefType - Market supported by Event.
     */
    public void attachMarketRefTypeToEvent(Integer id, MarketRefType marketRefType);

    /**
     * Remove marketRefType from an existing event
     *
     * @param id - Identifier for event to remove the market from.
     * @param marketRefType - marketRefType to remove from event.
     */
    public void removeMarketRefTypeFromEvent(Integer id, MarketRefType marketRefType);

    /**
     * Collection of incomplete event names based on provided filter.
     * If argument is null, it should include every possible values.
     *
     * @param cat - Cetegory ref identify the category to filter by.
     * @param subcat - SubCategory ref identify the sub-category to filter by.
     * @param marketRefName - MarketRefType name to filter by.
     *
     * @return Collection of the names of incompleted events from the relevant category ref, subcategort ref and marketRefName.
     */
    public Collection<String> futureEventNamesCollection(String cat, String subcat, String marketRefName);

    /**
     * Formatted structure
     * @return formatted dump or the data structure 
     */
    public String dumpFullStructure();
}
