package com.fsbtech.interviews;

import com.fsbtech.interviews.entities.Category;
import com.fsbtech.interviews.entities.Event;
import com.fsbtech.interviews.entities.MarketRefType;
import com.fsbtech.interviews.entities.SubCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class FSBClientTest {

    private FSBClient fsbClient;
    private List<Event> testEvents;

    Category tennisCategory = new Category(1,"Tennis");
    Category footballCategory = new Category(2,"Football");
    SubCategory subCategory1  = new SubCategory(1,"Premier League", footballCategory);
    SubCategory subCategory2  = new SubCategory(2,"Champions League", footballCategory);
    SubCategory subCategory3  = new SubCategory(3,"French Open", tennisCategory);
    SubCategory subCategory4  = new SubCategory(4,"Wimbledon", tennisCategory);
    MarketRefType refType1  = new MarketRefType(1,"Home");
    MarketRefType refType2  = new MarketRefType(2,"Draw");
    MarketRefType refType3  = new MarketRefType(3,"Away");

    @BeforeEach
    private void setup(){
        fsbClient = new FSBClient();
        testEvents = addEvents(fsbClient);
    }

    private List<Event> addEvents(FSBClient client) {
        List<Event> testEvents = new ArrayList<>();
        testEvents.add(new Event(1, "ManU vs Chelsea", subCategory1, List.of(refType1, refType2, refType3), false));
        testEvents.add(new Event(2, "ManU vs Chelsea", subCategory2, List.of(refType1, refType2, refType3), false));
        testEvents.add(new Event(3, "ManCity vs ManU", subCategory1, List.of(refType1, refType2, refType3), false));
        testEvents.add(new Event(4, "Roger vs Nadal", subCategory3, List.of(refType1), false));
        testEvents.add(new Event(5, "Nadal vs Djokovic", subCategory4, List.of(refType1,refType2), false));
        testEvents.add(new Event(6, "Paes vs Roger", subCategory3, List.of(refType2), false));
        return testEvents;
    }

    @Test
    void addEvent() {
        Event testEvent = testEvents.get(0);
        Integer testId = testEvent.getId();
        fsbClient.addEvent(testEvent);
        assertEquals(fsbClient.eventsMap.get(testId),testEvent);
        assertEquals(fsbClient.eventsMap.size(),1);
    }

    @Test
    void addDuplicateEvents() {
        Event testEvent = testEvents.get(0);
        Integer testId = testEvent.getId();
        fsbClient.addEvent(testEvent);
        fsbClient.addEvent(testEvent);
        fsbClient.addEvent(testEvent);
        assertEquals(fsbClient.eventsMap.get(testId),testEvent);
        assertEquals(fsbClient.eventsMap.size(),1);
    }

    @Test
    void eventCompleted() {
        Event testEvent = testEvents.get(0);
        Event testEvent2 = testEvents.get(1);
        fsbClient.addEvent(testEvent);
        fsbClient.addEvent(testEvent2);
        Integer testId = testEvent2.getId();
        fsbClient.eventCompleted(testId);
        assertEquals(fsbClient.eventsMap.size(),1);
        assertTrue(fsbClient.eventsMap.get(testId)==null);
    }

    @Test
    void attachMarketRefTypeToEvent() {

        Event testEvent = testEvents.get(0);
        Event testEvent2 = testEvents.get(3);
        fsbClient.addEvent(testEvent);
        fsbClient.addEvent(testEvent2);
        Integer testId = testEvent.getId();
        fsbClient.attachMarketRefTypeToEvent(testId, refType2);
        assertEquals(fsbClient.eventsMap.size(),2);
        assertTrue(fsbClient.eventsMap.get(testId).getMarketRefTypes().contains(refType2));
    }

    @Test
    void removeMarketRefTypeFromEvent() {
        Event testEvent = testEvents.get(0);
        Event testEvent2 = testEvents.get(4);
        fsbClient.addEvent(testEvent);
        fsbClient.addEvent(testEvent2);
        Integer testId = testEvent2.getId();
        fsbClient.removeMarketRefTypeFromEvent(testId, refType2);
        assertEquals(fsbClient.eventsMap.size(),2);
        assertTrue(!fsbClient.eventsMap.get(testId).getMarketRefTypes().contains(refType2));
    }

    @Test
    void futureEventNamesCollectionWithCategory() {
        testEvents.forEach(event -> fsbClient.addEvent(event));
        Collection<String> result = fsbClient.futureEventNamesCollection("Football", null, null);
        assertEquals(3, result.size());
    }

    @Test
    void futureEventNamesCollectionWithSubCategory() {
        testEvents.forEach(event ->fsbClient.addEvent(event));
        Collection<String> result = fsbClient.futureEventNamesCollection("", "Premier League", null);
        assertEquals(2, result.size());
    }

    @Test
    void futureEventNamesCollectionWithRefType() {
        testEvents.forEach(event ->fsbClient.addEvent(event));
        Collection<String> result = fsbClient.futureEventNamesCollection(null, "", "Home");
        assertEquals(6, result.size());
    }

    @Test
    void futureEventNamesCollectionWithAll() {
        testEvents.forEach(event ->fsbClient.addEvent(event));
        Collection<String> result = fsbClient.futureEventNamesCollection("Football", "Premier League", "Away");
        assertEquals(2, result.size());
    }

    @Test
    void futureEventNamesCollectionWithSubCategoryAndRefType() {
        testEvents.forEach(event ->fsbClient.addEvent(event));
        Collection<String> result = fsbClient.futureEventNamesCollection(null, "Premier League", "Home");
        assertEquals(2, result.size());
    }


    @Test
    void dumpFullStructure() {
        Event testEvent = testEvents.get(0);
        Event testEvent2 = testEvents.get(1);
        String expectedOutput = "[Event{id=1, name='ManU vs Chelsea', subCategory=SubCategory{id=1, ref='Premier League', category=Category{id=2, ref='Football'}}, marketRefTypes=[MarketRefType{marketRefId=1, marketRefName='Home'}, MarketRefType{marketRefId=2, marketRefName='Draw'}, MarketRefType{marketRefId=3, marketRefName='Away'}], completed=false}\n" +
                ", Event{id=2, name='ManU vs Chelsea', subCategory=SubCategory{id=2, ref='Champions League', category=Category{id=2, ref='Football'}}, marketRefTypes=[MarketRefType{marketRefId=1, marketRefName='Home'}, MarketRefType{marketRefId=2, marketRefName='Draw'}, MarketRefType{marketRefId=3, marketRefName='Away'}], completed=false}\n" +
                ", Event{id=3, name='ManCity vs ManU', subCategory=SubCategory{id=1, ref='Premier League', category=Category{id=2, ref='Football'}}, marketRefTypes=[MarketRefType{marketRefId=1, marketRefName='Home'}, MarketRefType{marketRefId=2, marketRefName='Draw'}, MarketRefType{marketRefId=3, marketRefName='Away'}], completed=false}\n" +
                ", Event{id=4, name='Roger vs Nadal', subCategory=SubCategory{id=3, ref='French Open', category=Category{id=1, ref='Tennis'}}, marketRefTypes=[MarketRefType{marketRefId=1, marketRefName='Home'}], completed=false}\n" +
                ", Event{id=5, name='Nadal vs Djokovic', subCategory=SubCategory{id=4, ref='Wimbledon', category=Category{id=1, ref='Tennis'}}, marketRefTypes=[MarketRefType{marketRefId=1, marketRefName='Home'}, MarketRefType{marketRefId=2, marketRefName='Draw'}], completed=false}\n" +
                ", Event{id=6, name='Paes vs Roger', subCategory=SubCategory{id=3, ref='French Open', category=Category{id=1, ref='Tennis'}}, marketRefTypes=[MarketRefType{marketRefId=2, marketRefName='Draw'}], completed=false}\n" +
                "]";
        fsbClient.addEvent(testEvent);
        fsbClient.addEvent(testEvent2);
        Integer testId = testEvent.getId();
        testEvents.stream().forEach(e ->fsbClient.addEvent(e));
        String output = fsbClient.dumpFullStructure();
        assertEquals(expectedOutput, output);
    }
}