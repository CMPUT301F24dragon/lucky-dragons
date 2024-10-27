/**
 * Defines the Event model class and its inner class Time.
 */

package com.example.luckydragon;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an Event object.
 * <p>
 * Issues:
 *   - could add another constructor for when waitlist limit is not specified (since it is optional)
 */
public class Event {
    /**
     * Represents a time as hours and minutes in 24 hour time.
     * e.g. 8:30 pm would have hours = 20 and minutes = 30
     */
    private class Time {
        public Integer hours;
        public Integer minutes;

        public Time(Integer hours, Integer minutes) {
            this.hours = hours;
            this.minutes = minutes;
        }

        @NonNull
        @Override
        public String toString() {
            return String.format("%02d%02d", hours, minutes);
        }
    }
    private String id;
    private String name;
    private String organizer;
    private String facility;
    private Integer waitlistLimit;
    private Integer attendeeLimit;
    private String date;
    private Time time;
    private BitMatrix qrHash;

    /**
     * Creates an Event object.
     * @param id the event id
     * @param name the name of the event
     * @param organizer the name of the event organizer
     * @param facility: the name of the event facility
     * @param waitlistLimit: the waitlist limit of the event
     * @param attendeeLimit: the attendee limit of the event
     * @param date: the date of the event, as a string YY-MM-DD
     * @param timeHours: the hour time e.g. "8" for 8:30
     * @param timeMinutes: the minute time e.g. "30" for 8:30
     */
    public Event(String id, String name, String organizer, String facility, Integer waitlistLimit, Integer attendeeLimit, String date, Integer timeHours, Integer timeMinutes)  {
        this.id = id;
        this.name = name;
        this.organizer = organizer;
        this.facility = facility;
        this.waitlistLimit = waitlistLimit;
        this.attendeeLimit = attendeeLimit;
        this.date = date;
        this.time = new Time(timeHours, timeMinutes);
        this.qrHash = generateQRCode();
    }

    /**
     * Creates a hash map containing the event info.
     * Used to add an event to Firestore.
     * @return a map containing event info
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("Name", name);
        eventData.put("Facility", facility);
        eventData.put("WaitlistLimit", waitlistLimit);
        eventData.put("AttendeeLimit", attendeeLimit);
        eventData.put("Date", date);
        eventData.put("Hours", time.hours);
        eventData.put("Minutes", time.minutes);
        eventData.put("HashedQR", qrHash.toString("1", "0"));
        return eventData;
    }

    /**
     * Checks if an Event is equal to another event.
     * @param o the other event to compare to
     * @return True if they are equal, False if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(facility, event.facility) && Objects.equals(waitlistLimit, event.waitlistLimit) && Objects.equals(attendeeLimit, event.attendeeLimit) && Objects.equals(date, event.date) && Objects.equals(time, event.time);
    }

    /**
     * Generates a hash code for the event.
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, facility, waitlistLimit, attendeeLimit, date, time);
    }

    /**
     * Gets the QR hash as a string.
     * Set bits are "1", unset bits are "9"
     * @return the string representation of the QR hash
     */
    public String getQrHash() {
        return qrHash.toString("1", "0");
    }

    /**
     * Generates a QR code for the event.
     * QR code is based off of the organizer, facility, event name, date, and time.
     * Uses the zxing library.
     * @return the QR code as a BitMatrix
     */
    public BitMatrix generateQRCode() {
        // Use event id for hashed string
        String hashedStr = id;
        // Encode string as QR code
        try {
            return new MultiFormatWriter().encode(hashedStr, BarcodeFormat.QR_CODE, 200, 200);
        } catch (WriterException e) {
            Log.e("QR Generation", "QR encoding failed!");
            return null;
        }
    }
}
