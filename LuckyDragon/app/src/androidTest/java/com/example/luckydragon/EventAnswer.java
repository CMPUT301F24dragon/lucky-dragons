package com.example.luckydragon;

import android.util.Log;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Map;

public class EventAnswer implements Answer {
    private ArrayList<Map<String, Object>> stages;
    int idx = 0;

    public EventAnswer(ArrayList<Map<String, Object>> stages) {
        this.stages = stages;
    }
    public Object answer(InvocationOnMock invocation) {
        assert idx < stages.size();
        Log.e("JXU", Integer.toString(idx));
        return stages.get(idx);
    }

    public void increment() {
        Log.e("JXU", "increment");
        idx += 1;
    }
}
