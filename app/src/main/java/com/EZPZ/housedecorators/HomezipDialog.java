/*
 *   @author Anthony Kritikos
 *   House Decoration Project
 *   05/01/2022
 */
package com.EZPZ.housedecorators;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class HomezipDialog extends AppCompatDialogFragment {
    private EditText editTextZipCode;
    private HomezipDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_homezipdialog, null);

        builder.setView(view)
                .setTitle("Home Zipcode")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String zipcode = editTextZipCode.getText().toString();
                        listener.homexmitZip(zipcode);
                    }
                });

        editTextZipCode = view.findViewById(R.id.enternewzipEditText);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (HomezipDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement HomezipDialogListener");
        }

    }

    public interface HomezipDialogListener{
        void homexmitZip(String zipcode);
    }
}
