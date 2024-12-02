package com.example.luckydragon;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.luckydragon.Models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public abstract class MockedEventList {
    @Mock
    protected FirebaseFirestore mockFirestore;
    // User mocks
    @Mock
    protected CollectionReference mockUsersCollection;
    @Mock
    private DocumentReference mockUserDocument;
    @Mock
    private DocumentSnapshot mockUserDocumentSnapshot;
    @Mock
    private Task<DocumentSnapshot> mockUserTask;
    @Mock
    protected Task<Void> mockVoidTask;
    // Event mocks
    @Mock
    private CollectionReference mockEventsCollection;


    @Mock
    private Query mockEventQuery;
    @Mock
    protected Task<QuerySnapshot> mockEventQueryTask;

    @Mock
    protected QuerySnapshot mockEventQuerySnapshot;
    @Mock
    private List<DocumentSnapshot> mockEventDocumentSnapshotList;
    protected abstract Map<String, Object> getMockData();
    protected abstract void extraSetup();
    protected abstract String getUserID();

    private ArrayList<Map<String, Object>> before;
    private ArrayList<Map<String, Object>> after;
    private ArrayList<Task<DocumentSnapshot>> eventListTasks;
    private ArrayList<QueryDocumentSnapshot> eventListSnapshots;
    private ArrayList<DocumentReference> eventDocRefs;
    private ArrayList<EventAnswer> eventAnswers;
    public void setEventList(ArrayList<Map<String, Object>> before, ArrayList<Map<String, Object>> after) {
        assert before.size() == after.size();
        this.before = before;
        this.after = after;

        this.eventListTasks = new ArrayList<>();
        this.eventListSnapshots = new ArrayList<>();
        this.eventDocRefs = new ArrayList<>();
        this.eventAnswers = new ArrayList<>();
        int n = before.size();
        for (int i = 0; i < n; i++) {
            eventListTasks.add(mock(Task.class));
            eventListSnapshots.add(Mockito.mock(QueryDocumentSnapshot.class));
            eventDocRefs.add(Mockito.mock(DocumentReference.class));
            eventAnswers.add(new EventAnswer(new ArrayList<>(List.of(before.get(i), after.get(i)))));
        }
    }

    @Before
    public void setup() {
        Intents.init();
        openMocks(this);

        mockUser();
        mockEvents();
        extraSetup();
    }

    @After
    public void tearDown() {
        // Reset global app state
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GlobalApp globalApp = (GlobalApp) targetContext.getApplicationContext();
        globalApp.setDb(null);
        globalApp.setUser(null);
        globalApp.resetState();

        Intents.release();
    }

    private void mockUser() {
        when(mockFirestore.collection("users")).thenReturn(mockUsersCollection);
        when(mockUsersCollection.document(anyString())).thenReturn(mockUserDocument);
        when(mockUserDocument.get()).thenReturn(mockUserTask);
        when(mockUserDocument.set(any(Map.class))).thenReturn(mockVoidTask);
        when(mockUserTask.addOnFailureListener(any(OnFailureListener.class))).thenReturn(mockUserTask);
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockUserDocumentSnapshot);
            return mockUserTask;
        }).when(mockUserTask).addOnSuccessListener(any(OnSuccessListener.class));
        when(mockUserDocumentSnapshot.getData()).thenReturn(this.getMockData());
        when(mockUserDocument.getId()).thenReturn(getUserID());
    }

    private void mockEvents() {
        when(mockFirestore.collection("events")).thenReturn(mockEventsCollection);
        int n = before.size();
        for (int i = 0; i < n; i++) {
            DocumentReference eventDoc = eventDocRefs.get(i);
            Task<DocumentSnapshot> eventTask = eventListTasks.get(i);
            DocumentSnapshot snapshot = eventListSnapshots.get(i);
            EventAnswer answer = eventAnswers.get(i);

            when(mockEventsCollection.document(Integer.toString(i))).thenReturn(eventDoc);
            when(eventDoc.get()).thenReturn(eventTask);
            when(eventDoc.set(any())).thenAnswer((invocation -> {
                Log.e("JXU", "CALL");
                answer.increment();
                return mockVoidTask;
            }));
            when(eventTask.addOnCompleteListener(any())).thenAnswer((invocation -> {
                OnCompleteListener<DocumentSnapshot> listener = invocation.getArgument(0);
                listener.onComplete(eventTask);
                return null;
            }));


            when(eventTask.isSuccessful()).thenReturn(true);
            when(eventTask.getResult()).thenReturn(snapshot);
            when(snapshot.exists()).thenReturn(true);
            when(snapshot.getData()).thenAnswer(answer);
        }

        QuerySnapshot mockEventQuerySnapshot = mock(QuerySnapshot.class);
        when(mockEventsCollection.addSnapshotListener(any())).thenAnswer(invocation -> {
            EventListener listener = invocation.getArgument(0);
            listener.onEvent(mockEventQuerySnapshot, null);
            return null;
        });
        List<DocumentSnapshot> mockEventDocumentSnapshots = mock(List.class);
        when(mockEventQuerySnapshot.size()).thenReturn(this.before.size());
        when(mockEventQuerySnapshot.getDocuments()).thenReturn(mockEventDocumentSnapshots);

        for (int i = 0; i < n; i++) {
            EventAnswer answer = eventAnswers.get(i);
            QueryDocumentSnapshot eventDocumentSnapshot = Mockito.mock(QueryDocumentSnapshot.class);
            when(mockEventDocumentSnapshots.get(i)).thenReturn(eventDocumentSnapshot);
            when(eventDocumentSnapshot.getData()).thenAnswer(answer);
            when(eventDocumentSnapshot.getId()).thenReturn(Integer.toString(i));
        }
        Task<QuerySnapshot> mockEventQuerySnapshotVoidTask = Mockito.mock(Task.class);
        when(mockEventsCollection.get()).thenReturn(mockEventQuerySnapshotVoidTask);
    }
}
