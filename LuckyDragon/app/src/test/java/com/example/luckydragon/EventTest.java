package com.example.luckydragon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EventTest {

    private Event mockEvent() {
        List<String> waitList = new ArrayList<String>(Arrays.asList("device-1"));
        return new Event("t_event_id", "t_event", "t_organizer_id",
                "t_organizer", "t_facility", 5, 5,
                "20241101",24, 0, waitList);
    }

    @Test
    void testRemoveFromWaitlist() {
        Event event = mockEvent();
        String entrant = event.waitList.get(0);
        event.removeFromWaitList(entrant);

        assertFalse(event.waitList.contains(entrant));
    }

    @Test
    void testDrawEntrantFromWaitlist() {
        Event event = mockEvent();
        String chosenEntrant = event.drawEntrantFromWaitingList();


        assertTrue(event.waitList.contains(chosenEntrant));

        event.removeFromWaitList(chosenEntrant);
        chosenEntrant = event.drawEntrantFromWaitingList();
        assertNull(chosenEntrant);
    }
}
