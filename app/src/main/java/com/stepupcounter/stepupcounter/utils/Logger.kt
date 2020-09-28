package com.stepupcounter.stepupcounter.utils

import android.content.Context
import android.content.Context.MODE_WORLD_READABLE
import android.os.Environment
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Logger {

    fun appendLog(context: Context, text: String?) {
        val logFile =
            File(context.cacheDir,"/debug.log")
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            // BufferedWriter for performance, true to set append to file flag
            val buf = BufferedWriter(FileWriter(logFile, true))
            buf.append(SimpleDateFormat("yyyy-MM-dd").format(Date()) + text)
            buf.newLine()
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}