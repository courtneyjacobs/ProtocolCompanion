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

public class StudyDetailFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private File JSONFile;
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
        final Switch GPSSwitch = root.findViewById(R.id.GPSSwitch);
        final Switch accelerationSwitch = root.findViewById(R.id.accelerationSwitch);
        final Switch HRSwitch = root.findViewById(R.id.HRSwitch);
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
        // gps
        studyViewModel.getSwitch("gps").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    GPSSwitch.setChecked(b);
                }
            }
        });
        // acceleration
        studyViewModel.getSwitch("acceleration").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    accelerationSwitch.setChecked(b);
                }
            }
        });
        // hr
        studyViewModel.getSwitch("hr").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    HRSwitch.setChecked(b);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Set appropriate values in VM (region is set using spinner methods)
                studyViewModel.setText("bucket", bucketText.getText().toString());
                studyViewModel.setText("folder", folderText.getText().toString());
                studyViewModel.setSwitch("gps", GPSSwitch.isChecked());
                studyViewModel.setSwitch("acceleration", accelerationSwitch.isChecked());
                studyViewModel.setSwitch("hr", HRSwitch.isChecked());
                studyViewModel.setText("name", studyName.getText().toString());

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

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Set appropriate values in VM (region is set using spinner methods)
                studyViewModel.setText("bucket", bucketText.getText().toString());
                studyViewModel.setText("folder", folderText.getText().toString());
                studyViewModel.setSwitch("gps", GPSSwitch.isChecked());
                studyViewModel.setSwitch("acceleration", accelerationSwitch.isChecked());
                studyViewModel.setSwitch("hr", HRSwitch.isChecked());
                studyViewModel.setText("name", studyName.getText().toString());

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
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // Retrieve the selected item
        String region = String.valueOf(parent.getItemAtPosition(pos));
        studyViewModel.setText("region", region);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        String region = "us-east-1";
        studyViewModel.setText("region", region);
    }
}