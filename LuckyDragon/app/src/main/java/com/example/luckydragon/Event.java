package com.example.luckydragon;

import android.util.Log;

import com.google.type.DateTime;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Event {
    private class Time {
        public Integer hours;
        public Integer minutes;

        public Time(Integer hours, Integer minutes) {
            this.hours = hours;
            this.minutes = minutes;
        }
    }
    private String name;
    private String facility;
    private Integer waitlistLimit;
    private Integer attendeeLimit;
    private String date;
    private Time time;
    private BitMatrix qrHash;

    public Event(String name, String facility, Integer waitlistLimit, Integer attendeeLimit, String date, Integer timeHours, Integer timeMinutes)  {
        this.name = name;
        this.facility = facility;
        this.waitlistLimit = waitlistLimit;
        this.attendeeLimit = attendeeLimit;
        this.date = date;
        this.time = new Time(timeHours, timeMinutes);
        this.qrHash = generateQRCode();
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("Name", name);
        eventData.put("Facility", facility);
        eventData.put("WaitlistLimit", waitlistLimit);
        eventData.put("AttendeeLimit", attendeeLimit);
        eventData.put("Date", date);
        eventData.put("Hours", time.hours);
        eventData.put("Minutes", time.minutes);
        eventData.put("HashedQR", qrHash.toString());
        return eventData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(facility, event.facility) && Objects.equals(waitlistLimit, event.waitlistLimit) && Objects.equals(attendeeLimit, event.attendeeLimit) && Objects.equals(date, event.date) && Objects.equals(time, event.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, facility, waitlistLimit, attendeeLimit, date, time);
    }

    public String getQrHash() {
        return qrHash.toString();
    }

    public BitMatrix generateQRCode() {
        String hashedStr = new String(Integer.valueOf(hashCode()).toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        try {
            return new MultiFormatWriter().encode(hashedStr, BarcodeFormat.QR_CODE, 200, 200);
        } catch (WriterException e) {
            Log.e("QR Generation", "QR encoding failed!");
            return null;
        }
    }
}
