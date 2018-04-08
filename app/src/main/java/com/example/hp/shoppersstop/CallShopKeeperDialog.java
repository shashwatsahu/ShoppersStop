package com.example.hp.shoppersstop;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by pc on 3/21/2018.
 */

public class CallShopKeeperDialog extends DialogFragment {

    public static  final String PHONE_NUMBER = "phone_num";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String phn = this.getArguments().getString(PHONE_NUMBER);
        final String uri = "tel:"+phn;
        builder.setTitle(R.string.call_shop)
                .setMessage("Call "+phn)
                .setPositiveButton(R.string.call_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                        startActivity(intent);

                    }
                })
                .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CallShopKeeperDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
