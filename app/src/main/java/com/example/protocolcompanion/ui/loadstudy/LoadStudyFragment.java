package com.example.protocolcompanion.ui.loadstudy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.protocolcompanion.MainActivity;
import com.example.protocolcompanion.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.util.Objects;

// Loads study from website represented by QR code. The following process is followed:
// 1. Scan QR code to get URL (this QR code will have been previously generated to represent a link to a json
//    file, on Dropbox for example, with the link configured to download the file upon following the link).
// 2. Follow URL to download linked file.
// 3. Parse JSON and import the study.
// Only step 1 is performed in this fragment currently. The other steps are performed in the Study class,
// the StudyViewModel, and the MainActivity.
public class LoadStudyFragment extends Fragment {

    // QR Code from tutorial on Mobile Vision API https://codelabs.developers.google.com/codelabs/bar-codes/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_loadstudy, container, false);

        Button btn = root.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).startDownload();
                Navigation.findNavController(v).navigate(R.id.nav_home);
            }
        });

        ImageView myImageView = root.findViewById(R.id.imgview);
        Bitmap myBitmap = BitmapFactory.decodeResource(
                Objects.requireNonNull(getContext()).getResources(),
                R.drawable.url);
        myImageView.setImageBitmap(myBitmap);

        TextView txtView = root.findViewById(R.id.txtContent);

        BarcodeDetector detector =
                new BarcodeDetector.Builder(getContext())
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();
        if(!detector.isOperational()){
            txtView.setText(R.string.qrError);
            return root;
        }

        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        Barcode thisCode = barcodes.valueAt(0);
        txtView.setText(thisCode.rawValue);
        return root;
    }
}