package com.robin.easyplayer.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * @author lubin
 * @version 1.0
 */
public class AudioRecordTool {
    private final String TAG = this.getClass().getName();

    private final int DEFAULT_SOURCE = MediaRecorder.AudioSource.MIC;  //麦克风
    private final int DEFAULT_RATE = 44100;    //采样率
    private final int DEFAULT_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;   //双通道(左右声道)
    private final int DEFAULT_FORMAT = AudioFormat.ENCODING_PCM_16BIT;   //数据位宽16位

    private AudioRecord mAudioRecord;
    private int mMinBufferSize;


    private boolean isRecording = false;

    public void startRecord() {
        startRecord(DEFAULT_SOURCE, DEFAULT_RATE, DEFAULT_CHANNEL, DEFAULT_FORMAT);
    }


    public void startRecord(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat) {

        mMinBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        if (mMinBufferSize == AudioRecord.ERROR_BAD_VALUE) {
            Log.d(TAG, "Invalid parameter");
            return;
        }

        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig,
                audioFormat, mMinBufferSize);
        if (mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
            Log.d(TAG, "AudioRecord initialize fail");
            return;
        }

        mAudioRecord.startRecording();
        isRecording = true;

        CaptureThread t = new CaptureThread();
        t.start();
        Log.d(TAG, "AudioRecord Start");
    }


    public void stopRecord() {
        isRecording = false;
        if (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            mAudioRecord.stop();
        }
        mAudioRecord.release();
        mOnAudioFrameCaptureListener = null;
        Log.d(TAG, "AudioRecord Stop");
    }

    private class CaptureThread extends Thread {

        @Override
        public void run() {
            while (isRecording) {
                byte[] buffer = new byte[mMinBufferSize];
                int result = mAudioRecord.read(buffer, 0, buffer.length);
                Log.d(TAG, "Captured  " + result + "  byte");
                if (mOnAudioFrameCaptureListener != null) {
                    mOnAudioFrameCaptureListener.onAudioFrameCapture(buffer);
                }
            }
        }
    }

    public interface onAudioFrameCaptureListener {
        void onAudioFrameCapture(byte[] audioData);
    }
    private onAudioFrameCaptureListener mOnAudioFrameCaptureListener;

    public void setOnAudioFrameCaptureListener(onAudioFrameCaptureListener listener) {
        mOnAudioFrameCaptureListener = listener;
    }
}
