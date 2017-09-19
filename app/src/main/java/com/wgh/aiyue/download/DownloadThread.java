package com.wgh.aiyue.download;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.wgh.aiyue.util.ConstDefine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by   : WGH.
 */
public class DownloadThread {
    private static int mThreadCount = 3;
    private static int mFinishedThread = 0;
    private int mCurrentProgress = 0;
    private int mMaxProgress = 0;
    private String mFileName = null;
    private String mPath = null;


    public void startDownload(final Handler mHandler, String filePath, final int position) {
        mFileName = getFileName(filePath);
        mPath = filePath;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(mPath);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    int respondCode = httpURLConnection.getResponseCode();
                    if (200 == respondCode) {
                        int length = httpURLConnection.getContentLength();
                        mMaxProgress = length;
                        File file = new File(Environment.getExternalStorageDirectory(), mFileName);
                        // Readable, writable, and saved in real-time to the underlying storage device
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                        randomAccessFile.setLength(length);
                        randomAccessFile.close();
                        int size = length / mThreadCount;
                        for (int i = 0; i < mThreadCount; i++) {
                            int startIndex = i * size;
                            int endIndex = (i + 1) * size - 1;
                            if (i == mThreadCount - 1) {
                                endIndex = length - 1;
                            }
                            new DownLoadThread(mHandler, startIndex, endIndex, i, position).start();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    class DownLoadThread extends Thread {
        int startIndex;
        int endIndex;
        int threadId;
        int fileIndex;
        Handler mHandler;

        public DownLoadThread(final Handler handler, int startIndex, int endIndex, int threadId, int fileIndex) {
            super();
            this.mHandler = handler;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.threadId = threadId;
            this.fileIndex = fileIndex;
        }

        @Override
        public void run() {
            try {
                // Download progress file
                File progressFile = new File(Environment.getExternalStorageDirectory(), threadId + ".txt");
                if (progressFile.exists()) {
                    FileInputStream fileInputStream = new FileInputStream(progressFile);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                    int lastProgress = Integer.parseInt(bufferedReader.readLine());
                    startIndex += lastProgress;             // Reset start download location
                    mCurrentProgress += lastProgress;       // Reset the start progress bar position
                    fileInputStream.close();
                }
                HttpURLConnection httpURLConnection;
                URL url = new URL(mPath);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);
                // Sets the range of requested data
                httpURLConnection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
                int respondCode = httpURLConnection.getResponseCode();
                if (206 == respondCode) {
                    InputStream is = httpURLConnection.getInputStream();
                    byte[] b = new byte[1024];
                    int len = 0;
                    int total = 0;
                    File file = new File(Environment.getExternalStorageDirectory(), mFileName);
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                    // Sets the start write location of the random file for the thread
                    randomAccessFile.seek(startIndex);
                    while ((len = is.read(b)) != -1) {
                        randomAccessFile.write(b, 0, len);
                        total += len;
                        mCurrentProgress += len;
                        if (0 == mCurrentProgress % 100) {
                            Message messageProg = new Message();
                            messageProg.arg1 = fileIndex;
                            messageProg.arg2 = ((mCurrentProgress * 100) / mMaxProgress);
                            messageProg.what = ConstDefine.PGB_PER_REFRESH;
                            mHandler.sendMessage(messageProg);
                        }
                        // Construction progress log files
                        RandomAccessFile progressRaf = new RandomAccessFile(progressFile, "rwd");
                        progressRaf.write((total + "").getBytes());
                        progressRaf.close();
                    }
                    randomAccessFile.close();
                    mFinishedThread++;
                    synchronized (mPath) {
                        if (mFinishedThread == mThreadCount) {
                            for (int i = 0; i < mThreadCount; i++) {
                                File recorderFile = new File(Environment.getExternalStorageDirectory(), i + ".txt");
                                recorderFile.delete();
                            }
                            mFinishedThread = 0;
                            Message messageProg = new Message();
                            messageProg.arg1 = fileIndex;
                            messageProg.what = ConstDefine.FINISH_DOWNLOAD;
                            mHandler.sendMessage(messageProg);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // get mFileName from mPath.
    private String getFileName(String pathName) {
        if ((pathName != null) && (pathName.length() > 0)) {
            int dot = pathName.lastIndexOf('/');
            if ((dot > -1) && (dot < (pathName.length() - 1))) {
                return pathName.substring(dot + 1);
            }
        }
        return pathName;
    }
}
