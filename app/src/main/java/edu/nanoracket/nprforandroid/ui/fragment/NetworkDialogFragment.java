package edu.nanoracket.nprforandroid.ui.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NetworkDialogFragment extends DialogFragment {

    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String INTENTACTION = "intentAction";

    private String intentAction;
    private int title;
    private int message;

    public static NetworkDialogFragment newInstance(int title, int message, String intentAction) {
        NetworkDialogFragment fragment = new NetworkDialogFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        args.putInt(MESSAGE, message);
        args.putString(INTENTACTION, intentAction);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        title = getArguments().getInt(TITLE);
        message = getArguments().getInt(MESSAGE);
        intentAction = getArguments().getString(INTENTACTION);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(intentAction);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
    }

}
