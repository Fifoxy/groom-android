package fr.galaisen.groomreader.utils;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class GroomScannerView extends ZXingScannerView {

    public GroomScannerView(Context context) {
        super(context);
    }

    public GroomScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        try {
            super.onPreviewFrame(data, camera);
        }catch (Exception e){}
    }
}
