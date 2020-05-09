package com.example.protocolcompanion.ui.studydetail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.protocolcompanion.R;
import com.example.protocolcompanion.Study;
import com.example.protocolcompanion.StudyViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

// Allows users to edit or delete a study, and provides the framework for adding functionality to push
// the study to a watch.
public class StudyDetailFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // protocols.json
    private File JSONFile;
    // Inject the view model
    private StudyViewModel studyViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_studydetail, container, false);
        // Get current id from list fragment
        assert getArguments() != null;
        final String currentId = StudyDetailFragmentArgs.fromBundle(getArguments()).getCurrentId();
        studyViewModel.setCurrentStudy(Study.getStudy(currentId));

        // Get JSON file
        JSONFile = new File(Objects.requireNonNull(getContext()).getFilesDir(), "protocols.json");

        // Get elements
        final Switch accelerometerSwitch = root.findViewById(R.id.accelerometerSwitch);
        final Switch ambientLightSwitch = root.findViewById(R.id.ambientLightSwitch);
        final Switch bluetoothSwitch = root.findViewById(R.id.bluetoothSwitch);
        final Switch breathSwitch = root.findViewById(R.id.breathSwitch);
        final Switch compassSwitch = root.findViewById(R.id.compassSwitch);
        final Switch gpsSwitch = root.findViewById(R.id.gpsSwitch);
        final Switch gyroscopeSwitch = root.findViewById(R.id.gyroscopeSwitch);
        final Switch hrSwitch = root.findViewById(R.id.hrSwitch);
        final Switch linearAccelerationSwitch = root.findViewById(R.id.linearAccelerationSwitch);
        final Switch offBodySwitch = root.findViewById(R.id.offBodySwitch);
        final Switch postureSwitch = root.findViewById(R.id.postureSwitch);
        final Switch ppgSwitch = root.findViewById(R.id.ppgSwitch);
        final Switch sleepSwitch = root.findViewById(R.id.sleepSwitch);
        final Switch stepCountSwitch = root.findViewById(R.id.stepCountSwitch);

        final EditText bucketText = root.findViewById(R.id.bucketText);
        final EditText folderText = root.findViewById(R.id.folderText);
        Button saveButton = root.findViewById(R.id.saveEditStudyButton);
        final EditText studyName = root.findViewById(R.id.studyName);
        final TextView studyId = root.findViewById(R.id.studyID);
        Button sendButton = root.findViewById(R.id.sendToWatchButton);
        Button deleteButton = root.findViewById(R.id.deleteButton);

        // from Android Spinner docs at https://developer.android.com/guide/topics/ui/controls/spinner
        // Set up region spinner
        final Spinner spinner = root.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.regions, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Specify listener
        spinner.setOnItemSelectedListener(this);
        // Get string array from resources
        final String[] regions = Objects.requireNonNull(getActivity()).getResources().getStringArray(R.array.regions);


        // Populate initial values (VM -> view)
        // name
        studyViewModel.getText("name").observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                studyName.setText(s);
            }
        });
        // id
        studyViewModel.getText("id").observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                String text = getString(R.string.idDisplay, s);
                studyId.setText(text);
            }
        });
        // region
        studyViewModel.getText("region").observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                spinner.setSelection(adapter.getPosition(s));
            }
        });
        // bucket
        studyViewModel.getText("bucket").observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                bucketText.setText(s);
            }
        });
        // folder
        studyViewModel.getText("folder").observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                folderText.setText(s);
            }
        });
        // accelerometer
        studyViewModel.getSwitch("accelerometer").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    accelerometerSwitch.setChecked(b);
                }
            }
        });
        // ambientLight
        studyViewModel.getSwitch("ambientLight").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    ambientLightSwitch.setChecked(b);
                }
            }
        });
        // bluetooth
        studyViewModel.getSwitch("bluetooth").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    bluetoothSwitch.setChecked(b);
                }
            }
        });
        // breath
        studyViewModel.getSwitch("breath").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    breathSwitch.setChecked(b);
                }
            }
        });
        // compass
        studyViewModel.getSwitch("compass").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    compassSwitch.setChecked(b);
                }
            }
        });
        // gps
        studyViewModel.getSwitch("gps").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    gpsSwitch.setChecked(b);
                }
            }
        });
        // gyroscope
        studyViewModel.getSwitch("gyroscope").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    gyroscopeSwitch.setChecked(b);
                }
            }
        });
        // hr
        studyViewModel.getSwitch("hr").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    hrSwitch.setChecked(b);
                }
            }
        });
        // linearAcceleration
        studyViewModel.getSwitch("linearAcceleration").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    linearAccelerationSwitch.setChecked(b);
                }
            }
        });
        // offBody
        studyViewModel.getSwitch("offBody").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    offBodySwitch.setChecked(b);
                }
            }
        });
        // posture
        studyViewModel.getSwitch("posture").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    postureSwitch.setChecked(b);
                }
            }
        });
        // ppg
        studyViewModel.getSwitch("ppg").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    ppgSwitch.setChecked(b);
                }
            }
        });
        // sleep
        studyViewModel.getSwitch("sleep").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    sleepSwitch.setChecked(b);
                }
            }
        });
        // stepCount
        studyViewModel.getSwitch("stepCount").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    stepCountSwitch.setChecked(b);
                }
            }
        });

        // save changes to study
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Set appropriate values in VM (region is set using spinner methods)
                studyViewModel.setText("bucket", bucketText.getText().toString());
                studyViewModel.setText("folder", folderText.getText().toString());
                studyViewModel.setText("name", studyName.getText().toString());

                studyViewModel.setSwitch("accelerometer", accelerometerSwitch.isChecked());
                studyViewModel.setSwitch("ambientLight", ambientLightSwitch.isChecked());
                studyViewModel.setSwitch("bluetooth", bluetoothSwitch.isChecked());
                studyViewModel.setSwitch("breath", breathSwitch.isChecked());
                studyViewModel.setSwitch("compass", compassSwitch.isChecked());
                studyViewModel.setSwitch("gps", gpsSwitch.isChecked());
                studyViewModel.setSwitch("gyroscope", gyroscopeSwitch.isChecked());
                studyViewModel.setSwitch("hr", hrSwitch.isChecked());
                studyViewModel.setSwitch("linearAcceleration", linearAccelerationSwitch.isChecked());
                studyViewModel.setSwitch("offBody", offBodySwitch.isChecked());
                studyViewModel.setSwitch("posture", postureSwitch.isChecked());
                studyViewModel.setSwitch("ppg", ppgSwitch.isChecked());
                studyViewModel.setSwitch("sleep", sleepSwitch.isChecked());
                studyViewModel.setSwitch("stepCount", stepCountSwitch.isChecked());


                // update study in place without adding a new one
                studyViewModel.updateCurrentStudy(false);


                // Save changes to file
                updateFile();

                // Create Toast notification
                Context context = getContext();
                CharSequence text = "Study successfully saved!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        // send study to watch
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Set appropriate values in VM (region is set using spinner methods)
                studyViewModel.setText("bucket", bucketText.getText().toString());
                studyViewModel.setText("folder", folderText.getText().toString());
                studyViewModel.setText("name", studyName.getText().toString());

                studyViewModel.setSwitch("accelerometer", accelerometerSwitch.isChecked());
                studyViewModel.setSwitch("ambientLight", ambientLightSwitch.isChecked());
                studyViewModel.setSwitch("bluetooth", bluetoothSwitch.isChecked());
                studyViewModel.setSwitch("breath", breathSwitch.isChecked());
                studyViewModel.setSwitch("compass", compassSwitch.isChecked());
                studyViewModel.setSwitch("gps", gpsSwitch.isChecked());
                studyViewModel.setSwitch("gyroscope", gyroscopeSwitch.isChecked());
                studyViewModel.setSwitch("hr", hrSwitch.isChecked());
                studyViewModel.setSwitch("linearAcceleration", linearAccelerationSwitch.isChecked());
                studyViewModel.setSwitch("offBody", offBodySwitch.isChecked());
                studyViewModel.setSwitch("posture", postureSwitch.isChecked());
                studyViewModel.setSwitch("ppg", ppgSwitch.isChecked());
                studyViewModel.setSwitch("sleep", sleepSwitch.isChecked());
                studyViewModel.setSwitch("stepCount", stepCountSwitch.isChecked());

                // update study in place without adding a new one
                studyViewModel.updateCurrentStudy(false);

                // Save changes to file
                updateFile();

                // Create Toast notification
                Context context = getContext();
                CharSequence text = "Study successfully saved!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        // delete study
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                studyViewModel.deleteCurrentStudy();

                // Save changes to file
                updateFile();

                // Go back to home
                Navigation.findNavController(v).navigate(R.id.nav_home);
                // Create Toast notification
                Context context = getContext();
                CharSequence text = "Study successfully deleted!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        return root;
    }

    // Opens file and overwrites changes to the current study
    private void updateFile() {
        try {
            FileWriter fileWriter = new FileWriter(JSONFile, false);
            fileWriter.write(studyViewModel.getFullJSONString());
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methods for spinner

    // when an item is selected, update the value
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // Retrieve the selected item
        String region = String.valueOf(parent.getItemAtPosition(pos));
        studyViewModel.setText("region", region);
    }

    // when nothing in the spinner is selected, the default is us-east-1 (Northern Virginia)
    public void onNothingSelected(AdapterView<?> parent) {
        String region = "us-east-1";
        studyViewModel.setText("region", region);
    }
}