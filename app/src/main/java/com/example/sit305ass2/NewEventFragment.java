package com.example.sit305ass2;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sit305ass2.db.AppDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NewEventFragment extends Fragment
{
    //VARIABLES IN CLASS SCOPE
    String title, category, location;
    AppDatabase db;
    int year, month, day, hour, minute;
    int eitToDelete;
    boolean editingEvent;   //ARE WE EDITING THE EVENT? (DEFINITELY A MUCH BETTER WAY TO DO THIS)

    //WIDGETS
    EditText inputTitle, inputCategory, inputLocation;
    DatePicker inputDate;
    TimePicker inputTime;
    Button createEvent;
    //DATE FORMATTER FOR CONSISTENT STRINGS
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_event, container, false);
    }

    //USE ON VIEW CREATED FOR FRAGMENTS
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //INSTANTIATE DB
        db = AppDatabase.getDbInstance(this.requireActivity().getApplicationContext());
        // FIND ELEMENTS IN FRAGMENTS XML FILE (NEW EVENT)
        inputTitle = (EditText) getView().findViewById(R.id.inputTitle);
        inputCategory = (EditText) getView().findViewById(R.id.inputCategory);
        inputLocation = (EditText) getView().findViewById(R.id.inputLocation);
        inputDate = (DatePicker) getView().findViewById(R.id.inputeDate);
        inputTime = (TimePicker) getView().findViewById(R.id.inputTime);
        createEvent = (Button) getView().findViewById(R.id.createEvent);

        // The local variables are set to the default values of date and time picker, which is usually the current time and date
        initInputs();

        // WHEN THE USER CHANGES THE DATE
        inputDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int inputYear, int inputMonth, int inputDay) {
                year = inputYear;
                month = inputMonth + 1; //JAN STARTS AT 0 SO WE CHANGE TO ACCOMMODATE
                day = inputDay;
            }
        });

        // WHEN THE USER CHANGES THE TIME PICKER WE INSTANTLY UPDATE VALUES
        inputTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int inputHour, int inputMinute) {
                hour = inputHour;
                minute = inputMinute;
            }
        });

        //RECEIVING DATA FOR EDIT EVENT, WE EXPECT TO RECEIVE STRINGS AND INTS TO SIMULATE THE EDIT FEATURE
        getParentFragmentManager().setFragmentResultListener("dataFromHome",
                this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                String[] stringArray = result.getStringArray("editEventStrings");
                int[] intArray = result.getIntArray("editEventInts");
                //THIS IS THE UNIQUIE ID OF THE EVENTMODEL CLASS THAT ACTS AS THE ENTITY IN THE DB, PASSED FROM HOMEFRAGMENT
                eitToDelete = result.getInt("eidToDelete");
                //EDIT THE EVENT WITH GIVEN THE DATA
                editEvent(stringArray, intArray);
            }
        });
    }

    //CALL ON START METHOD BECAUSE THE NAV CONTROLLER WILL BE NULL BEFORE THIS IN THE FRAGMENT LIFECYCLE
    @Override
    public void onStart()
    {
        super.onStart();
        navController = Navigation.findNavController(getActivity().findViewById(R.id.flFragment));

        // WHEN THE BUTTON IS PRESSED, CHECKED THE INPUTS AND PASS THEM THROUGH TO HOME FRAGMENT WHEN VALIDATED
        createEvent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view1)
            {
                //RETRIEVE THE VALUES FOR THE TITLE, CATEGORY AND LOCATION
                title = inputTitle.getText().toString();
                category = inputCategory.getText().toString();
                location = inputLocation.getText().toString();
                //PLEASE NOTE THE DATE AND TIME INTS ARE UPDATED AS THE VALUES ARE CHANGED!!!

                //POPULATE ARRAYS TO BE SENT IN BUNDLE TO HOME FRAGMENT
                String[] newEventStrings = {title, category, location};
                int[] newEventInts = {year, month, day, hour, minute};

                //CHECK IF THE INPUTS ARE ALL VALID, IF THEY ARE SEND THE BUNDLE WITH DATA TO THE HOME FRAGMENT
                if (areInputsValid(newEventStrings) && areInputsValid(newEventInts))
                {
                    //CHECK BOOL TO SEE IF WE ARE EDITING THE EVENT, RATHER THAN CREATING FROM SCRATCH
                    if (editingEvent)
                    {
                        //IF WE ARE BE SURE TO DELETE THE EVENT THIS NEW ONE IS BASED ON
                        db.eventModelDao().deleteByUserId(eitToDelete);
                        editingEvent = false;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putStringArray("newEventStrings", newEventStrings);
                    bundle.putIntArray("newEventInts", newEventInts);
                    getParentFragmentManager().setFragmentResult("dataFromNewEvent", bundle);
                    //NAVIGATE TO HOME FRAGMENT
                    navController.navigate(R.id.action_newEventFragment_to_homeFragment);
                }
            }
        });
    }

    //THIS IS CALLED WHEN THIS FRAGMENT IS NAVIGATED TO VIA THE EDIT BUTTON ON AN EXISTING EVENT,
    //THEREFORE THE VALUES ARE PASSED THROUGH. NOTE THAT MONTH MUST BE DECREASED BY 1 BECAUSE JANUARY == 1
    private void editEvent(String[] stringArray, int[] intArray)
    {
        editingEvent = true;
        inputTitle.setText(stringArray[0]);
        inputCategory.setText(stringArray[1]);
        inputLocation.setText(stringArray[2]);
        inputDate.updateDate(intArray[0],intArray[1] - 1, intArray[2]);
        inputTime.setHour(intArray[3]);
        inputTime.setMinute(intArray[4]);
    }

    //OVERLOADING VALIDATION METHODS FOR INTS AND STRINGS,
    //WE ARE CHECKING IF THE INPUTS ARE POPULATED AND THE DATE IS IN THE FUTURE
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean areInputsValid(int[] data)
    {
        LocalDateTime dateTimePicked = LocalDateTime.of(year, month, day, hour, minute);

        if (dateTimePicked.isBefore(LocalDateTime.now()))
        {
            Toast.makeText(getActivity(), "Please select a date in the future" + hour + " " + minute, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean areInputsValid(String[] data)
    {
        for (String str:data)
        {
            if (str.isEmpty())
            {
                Toast.makeText(getActivity(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    //INITIALIZES THE DATE AND TIME INPUTS THAT ARE SET BY DEFAULT IN THE WIDGETS
    private void initInputs()
    {
        year = inputDate.getYear();
        month = inputDate.getMonth();
        day = inputDate.getDayOfMonth();
        hour = inputTime.getHour();
        minute = inputTime.getMinute();
    }

}