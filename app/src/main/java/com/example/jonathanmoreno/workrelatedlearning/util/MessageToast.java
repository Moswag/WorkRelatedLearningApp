package com.example.jonathanmoreno.workrelatedlearning.util;

import android.content.Context;
import android.widget.Toast;

public class MessageToast {

    public static void show(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
