package com.fsbtech.interviews.rest;

import com.fsbtech.interviews.Client;
import com.fsbtech.interviews.entities.Event;
import com.fsbtech.interviews.entities.MarketRefType;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("fsbEvents")
//Rest Client to process events
public class FSBRestControllerClient implements Client {
    protected Map<Integer, Event> eventsMap = new HashMap<>();
    final static Logger logger = Logger.getLogger(FSBRestControllerClient.class);


    @PostMapping("/addEvent")
    public void addEvent(@RequestBody Event event) {
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

    @PostMapping("/completeEvent/{id}")
    public void eventCompleted(@PathVariable Integer id) {
        Object removed = eventsMap.remove(id);
        if (removed != null) {
            logger.info("Event id: " + id + " removed");
        } else {
            logger.info("Event id not found");
        }
    }

    @PutMapping("/updateMarketRegType/{id}")
    public void attachMarketRefTypeToEvent(@PathVariable Integer id, @RequestBody MarketRefType marketRefType) {
        Event event = eventsMap.get(id);
        if (event != null) {
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
        } else {
            logger.info("Event not found for provided id.");
        }
    }

    @PostMapping("/removeMarketRefType/{id}")
    public void removeMarketRefTypeFromEvent(@PathVariable Integer id, @RequestBody MarketRefType marketRefType) {
        Event event = eventsMap.get(id);
        if (event != null) {
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
        } else {
            logger.info("Event not found for provided id.");
        }
    }

    @GetMapping("/filterEvents")
    public Collection<String> futureEventNamesCollection(@RequestParam String cat,
                                                         @RequestParam String subcat,
                                                         @RequestParam String marketRefName) {
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

    @GetMapping("/dumpFullStructure")
    public String dumpFullStructure() {
        return eventsMap.values().toString();
    }
}
