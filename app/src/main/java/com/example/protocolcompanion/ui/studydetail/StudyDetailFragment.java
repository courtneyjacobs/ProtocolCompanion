package com.example.protocolcompanion.ui.studydetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.protocolcompanion.R;
import com.example.protocolcompanion.Study;
import com.example.protocolcompanion.StudyViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class StudyDetailFragment extends Fragment {

    private File JSONFile;
    private StudyViewModel studyViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_studydetail, container, false);
        // Get current id from list fragment
        assert getArguments() != null;
        final String currentId = StudyDetailFragmentArgs.fromBundle(getArguments()).getCurrentId();
        studyViewModel.setCurrentStudy(Study.ITEMS.get(Integer.parseInt(currentId)));

        // Get elements
        final Switch GPSSwitch = root.findViewById(R.id.GPSSwitch);
        final Switch accelerationSwitch = root.findViewById(R.id.accelerationSwitch);
        final Switch HRSwitch = root.findViewById(R.id.HRSwitch);
        final EditText regionText = root.findViewById(R.id.regionText);
        final EditText bucketText = root.findViewById(R.id.bucketText);
        final EditText folderText = root.findViewById(R.id.folderText);
        Button saveButton = root.findViewById(R.id.saveEditStudyButton);
        final TextView studyName = root.findViewById(R.id.studyName);
        final TextView studyId = root.findViewById(R.id.studyID);

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
                studyId.setText(s);
            }
        });
        // region
        studyViewModel.getText("region").observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                regionText.setText(s);
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

                // Set appropriate values in VM
                studyViewModel.setText("region", regionText.getText().toString());
                studyViewModel.setText("bucket", bucketText.getText().toString());
                studyViewModel.setText("folder", folderText.getText().toString());
                studyViewModel.setSwitch("gps", GPSSwitch.isChecked());
                studyViewModel.setSwitch("acceleration", accelerationSwitch.isChecked());
                studyViewModel.setSwitch("hr", HRSwitch.isChecked());

                // Open file and overwrite changes to the current study
                JSONObject editedStudyJSONObject = studyViewModel.exportJSON();
                try {
                    studyViewModel.fullJSONObject.remove(currentId);
                    studyViewModel.fullJSONObject.put(currentId, editedStudyJSONObject);
                    FileWriter fileWriter = new FileWriter(JSONFile, false);
                    fileWriter.write(studyViewModel.fullJSONObject.toString());
                    fileWriter.close();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;



    }
}