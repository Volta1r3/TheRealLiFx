package com.example.thereallifx;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.prefs.Preferences;

public class GetBulbNumber extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText bulbNumber = view.findViewById(R.id.bulb_number);
                        int numberBulbs = Integer.parseInt(bulbNumber.getText().toString());

                        Context context = getContext();
                        assert context != null;
                        SharedPreferences sharedPreferences = context.getSharedPreferences("pref_name", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("number_bulbs", numberBulbs);
                        editor.apply();
                        Log.d("number_bulbs", String.valueOf(numberBulbs));
                        GetBulbNumber.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
