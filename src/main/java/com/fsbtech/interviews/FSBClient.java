package com.fsbtech.interviews;

import com.fsbtech.interviews.entities.Event;
import com.fsbtech.interviews.entities.MarketRefType;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//Normal client to process events
public class FSBClient implements Client {
    final static Logger logger = Logger.getLogger(FSBClient.class);
    protected Map<Integer, Event> eventsMap = new HashMap<>();
    private Object newObjectLock = new Object();
    private Object modifyObjectLock = new Object();

    public void addEvent(Event event) {
        synchronized (newObjectLock) {
            if (event != null) {
                if (eventsMap.containsKey(event.getId())) {
                    logger.info("Event id already present.");
                } else {
                    eventsMap.put(event.getId(), event);
                    logger.info("Event added : " + event);
                }
            } else {
                logger.error("Add event process failed. Event Id missing.");
            }
        }
    }

    public void eventCompleted(Integer id) {
        Event event = eventsMap.get(id);
        if (event != null) {
            synchronized (newObjectLock) {
                Object removed = eventsMap.remove(id);
                if (removed != null) {
                    logger.info("Event id: " + id + " removed");
                } else {
                    logger.info("Event id not found");
                }
            }
        }
    }

    public void attachMarketRefTypeToEvent(Integer id, MarketRefType marketRefType) {
        Event event = eventsMap.get(id);
        if (event != null) {
            synchronized (modifyObjectLock) {
                List<MarketRefType> eventMarketRefTypes = event.getMarketRefTypes().stream().collect(Collectors.toList());
                if (!eventMarketRefTypes.contains(marketRefType)) {
                    eventMarketRefTypes.add(marketRefType);
                    eventsMap.put(id,
                            new Event(id, event.getName(), event.getSubCategory(),
                                    event.getMarketRefTypes(), event.getCompleted()));
                    logger.info("Added " + marketRefType + " to event id :" + id);
                } else {
                    logger.info("MarketRefType :" + marketRefType + " already present in event");
                }
            }
        } else {
            logger.info("Event not found for provided id.");
        }
    }


    public void removeMarketRefTypeFromEvent(Integer id, MarketRefType marketRefType) {
        Event event = eventsMap.get(id);
        if (event != null) {
            synchronized (modifyObjectLock) {
                if (event.getMarketRefTypes().contains(marketRefType)) {
                    List<MarketRefType> eventMarketRefTypes = event.getMarketRefTypes().stream()
                            .filter(refType -> refType.getMarketRefId() != marketRefType.getMarketRefId())
                            .collect(Collectors.toList());
                    eventsMap.put(id,
                            new Event(id, event.getName(), event.getSubCategory(),
                                    eventMarketRefTypes, event.getCompleted()));
                    logger.info("Removed " + marketRefType + " to event id :" + id);
                } else {
                    logger.info("MarketRefType :" + marketRefType + " not present in event");
                }
            }

        } else {
            logger.info("Event not found for provided id.");
        }
    }

    public Collection<String> futureEventNamesCollection(String cat, String subcat, String marketRefName) {
        boolean catBoolean = cat != null ? !cat.trim().isEmpty() ? true : false : false;
        boolean subcatBoolean = subcat != null ? !subcat.trim().isEmpty() ? true : false : false;
        boolean marketRefBoolean = marketRefName != null ? !marketRefName.trim().isEmpty() ? true : false : false;


        List<Predicate<Event>> predicateList = new ArrayList<>();
        StringBuilder filterString = new StringBuilder("Filter by categories: ");
        if (subcatBoolean) {
            Predicate<Event> subCatPredicate = event -> event.getSubCategory().getRef().equals(subcat);
            predicateList.add(subCatPredicate);
            filterString = filterString.append("Sub Category :" + subcat + " /n");
        }
        if (catBoolean) {
            Predicate<Event> catPredicate = event -> event.getSubCategory().getCategory().getRef().equals(cat);
            predicateList.add(catPredicate);
            filterString = filterString.append("Category :" + cat + "/n");
        }

        List<String> filteredEvents = eventsMap.values().stream()
                .filter(predicateList.stream().reduce(Predicate::and).orElse(x -> true))
                .map(event -> event.toString()).collect(Collectors.toList());


        if (marketRefBoolean) {
            eventsMap.values().stream()
                    .filter(event -> event.getMarketRefTypes().stream()
                            .map(e -> e.getMarketRefName()).collect(Collectors.toList()).contains(marketRefBoolean))
                    .map(event -> event.toString()).collect(Collectors.toList());
            filterString = filterString.append("Market Ref Type :" + marketRefName + " /n");
        }

        logger.info(filterString.toString());
        return filteredEvents;
    }

    public String dumpFullStructure() {
        return eventsMap.values().toString();
    }
}
