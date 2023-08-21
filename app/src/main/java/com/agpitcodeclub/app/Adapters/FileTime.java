package com.agpitcodeclub.app.Adapters;

import android.annotation.TargetApi;
import android.os.Build;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileTime {
    private String time;
    @TargetApi(Build.VERSION_CODES.O)
    public FileTime() {
        LocalDateTime now = LocalDateTime.now();

        // Define a custom format without symbols
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        }

        // Format the date and time
        String formattedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDateTime = now.format(formatter);
        }
        this.time=formattedDateTime;
    }

    public String getFileTime(){
        return time;
    }
}
