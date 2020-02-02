package com.triangon.aruba_flora_fauna.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.triangon.aruba_flora_fauna.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DownloadDataDialog extends AppCompatDialogFragment {
    private DownloadDataDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setTitle("Offline Usage")
                .setMessage("Would you like to download the species images and information for offline use?")
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onYesDownloadClicked();
                    }
                });

        return builder.create();
    }

    public interface DownloadDataDialogListener {
        void onYesDownloadClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DownloadDataDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
}
