package com.example.sit305ass2;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sit305ass2.db.AppDatabase;

import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewInterface
{

    //VARIABLES FOR CLASS SCOPE
    RecyclerView recyclerView;
    List<EventModel> eventModels;
    EventRecyclerViewAdapter adapter;
    NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //USING ON VIEW CREATED FOR FRAGMENTS
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //INITIALIZE RECYCLER VIEW ON VIEW CREATED SETTING ADAPTER, LAYOUT AND THEN LOADING EVENTS LIST FROM ROOM DB
        initRecyclerView();
        loadEvents();

        //WHEN RECEIVING DATA FOR NEW EVENT, DATA WE EXPECT ARE VALUES ENTERED BY USER TO ADD INTO EVENTSLIST
        getParentFragmentManager().setFragmentResultListener("dataFromNewEvent",
                this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                //STORE RESULTS FROM BUNDLES
                String[] stringArray = result.getStringArray("newEventStrings");
                int[] intArray = result.getIntArray("newEventInts");
                //THIS METHOD DOES CREATES AN INSTANCE OF EVENTMODEL THEN ADDS TO ROOM DB AND RECYCLER
                createNewEvent(stringArray, intArray);
            }
        });
    }

    //NECESSARY INITS FOR THE RECYCLER VIEW
    private void initRecyclerView()
    {
        recyclerView = (RecyclerView) getView().findViewById(R.id.mRecyclerView);
        adapter = new EventRecyclerViewAdapter(getActivity(), eventModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //THIS LOADS THE EVENTS FROM THE ROOM DATABASE AND FILLS THE RECYCLER
    private void loadEvents()
    {
        AppDatabase db = AppDatabase.getDbInstance(this.requireActivity().getApplicationContext());
        eventModels = db.eventModelDao().getAllEvents();
        adapter.setEventList(eventModels);
    }

    //CREATING THE EVENTMODEL INSTANCE WITH DATA PASSED THROUGH NEWEVENT FRAGMENT
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNewEvent(String[] stringArray, int[] intArray )
    {
        EventModel eventModel = new EventModel(stringArray[0], stringArray[1], stringArray[2],
        intArray[0], intArray[1], intArray[2],intArray[3], intArray[4]);
        //eventModels.add(eventModel);
        AppDatabase db = AppDatabase.getDbInstance(this.requireActivity().getApplicationContext());
        db.eventModelDao().insertEvent(eventModel);
        loadEvents();
        Toast.makeText(getActivity(), "An event was successfully created", Toast.LENGTH_SHORT).show();
    }
    // TAKES US TO THE NEW EVENT SCREEN BUT WITH DATA ALREADY FILLED IN
    @Override
    public void onEditClick(int pos)
    {
        navController = Navigation.findNavController(getActivity().findViewById(R.id.flFragment));
        EventModel selectedEvent = eventModels.get(pos);
        String[] editEventStrings = {selectedEvent.getTitle(), selectedEvent.getCategory(),
                selectedEvent.getCategory()};
        int[] editEventInts = {selectedEvent.getYear(), selectedEvent.getMonth() ,selectedEvent.getDay(),
                selectedEvent.getHour(), selectedEvent.getMinute()};

        Bundle bundle = new Bundle();
        bundle.putStringArray("editEventStrings", editEventStrings);
        bundle.putIntArray("editEventInts", editEventInts);
        bundle.putInt("eidToDelete",eventModels.get(pos).eid);
        getParentFragmentManager().setFragmentResult("dataFromHome", bundle);
        navController.navigate(R.id.action_homeFragment_to_newEventFragment);
    }
    //WHEN THE DELETE BUTTON IS CLICKED, WE DELETE THE EVENT ENTIRELY
    @Override
    public void onDeleteClick(int pos)
    {
        AppDatabase db = AppDatabase.getDbInstance(this.requireActivity().getApplicationContext());
        db.eventModelDao().delete(eventModels.get(pos));
        loadEvents();
        adapter.notifyItemRemoved(pos);
        Toast.makeText(getActivity(), "The event was deleted", Toast.LENGTH_SHORT).show();
    }

}