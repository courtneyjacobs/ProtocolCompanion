package com.example.protocolcompanion.ui.loadstudy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import com.example.protocolcompanion.R;

import org.w3c.dom.Text;

import java.util.Objects;

public class LoadStudyFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_loadstudy, container, false);

        Button btn = root.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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