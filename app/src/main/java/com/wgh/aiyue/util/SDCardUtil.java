package com.wgh.aiyue.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by   : WGH.
 */
public class SDCardUtil {

    private SDCardUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * Determine if SDCard is available
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Get SD card path
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * Gets the remaining capacity unit of SD card byte
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // Gets the number of free blocks of data
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // Gets the size of a single block of data (byte)
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * Gets the remaining number of bytes of available capacity in the space in the specified path, in units byte
     */
    public static long getFreeBytes(String filePath) {
        // If it is the next path of the SD card, the available capacity of the SD card is obtained
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// If it is an internally stored path, the available capacity of the memory storage is obtained
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * Get system storage path
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }
}
