package com.example.perftester

import android.app.Activity
import android.util.Log
import android.view.WindowManager
import androidx.core.app.FrameMetricsAggregator
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.google.firebase.perf.util.Constants

/**
 * Utility class to capture Screen rendering information (Slow/Frozen frames) for the
 * `Activity` passed to the constructor [FrameRecorder.ScreenTrace].
 *
 *
 * Learn more at https://firebase.google.com/docs/perf-mon/screen-traces?platform=android.
 *
 *
 * A slow screen rendering often leads to a UI Jank which creates a bad user experience. Below are
 * some tips and references to understand and fix common UI Jank issues:
 * - https://developer.android.com/topic/performance/vitals/render.html#fixing_jank
 * - https://youtu.be/CaMTIgxCSqU (Why 60fps?)
 * - https://youtu.be/HXQhu6qfTVU (Rendering Performance)
 * - https://youtu.be/1iaHxmfZGGc (Understanding VSYNC)
 * - https://www.youtube.com/playlist?list=PLOU2XLYxmsIKEOXh5TwZEv89aofHzNCiu (Android Performance Patterns)
 *
 *
 * References:
 * - Fireperf Source Code: https://bityl.co/5v2O
 */
class FrameRecorder(private val activity: Activity, tag: String) {
    /**
     * Returns whether recording of screen traces are supported or not.
     */
    val isScreenTraceSupported: Boolean
    private val traceName: String
    private var frameMetricsAggregator: FrameMetricsAggregator? = null
    private var perfScreenTrace: Trace? = null

    /**
     * Default constructor for this class.
     *
     * @param activity for which the screen traces should be recorded.
     * @param tag      used as an identifier for the name to be used to log screen rendering
     * information (like "ActivityName-tag").
     * @implNote It will automatically force enable hardware acceleration for the passed `activity`.
     * @see .enableHardwareAcceleration
     */
    init {
        traceName = activity.getLocalClassName() + "-" + tag
        enableHardwareAcceleration(activity)
        isScreenTraceSupported = isScreenTraceSupported(activity)
        if (isScreenTraceSupported) {
            frameMetricsAggregator = FrameMetricsAggregator()
        }
    }

    /**
     * Starts recording the frame metrics for the screen traces.
     */
    fun recordScreenTrace() {
        require(isScreenTraceSupported) { "Trying to record screen trace when it's not supported!" }
        Log.d(LOG_TAG, "Recording screen trace $traceName")
        frameMetricsAggregator!!.add(activity)
        perfScreenTrace = FirebasePerformance.startTrace(screenTraceName)
    }

    /**
     * Stops recording screen traces and dispatches the trace capturing information on %age of
     * Slow/Frozen frames.
     *
     *
     * Reference: Fireperf Source Code - https://bityl.co/5v22
     */
    fun sendScreenTrace() {
        if (perfScreenTrace == null) return
        var totalFrames = 0
        var slowFrames = 0
        var frozenFrames = 0

        // Stops recording metrics for this Activity and returns the currently-collected metrics
        val arr = frameMetricsAggregator!!.reset()
        if (arr != null) {
            val frameTimes = arr[FrameMetricsAggregator.TOTAL_INDEX]
            if (frameTimes != null) {
                for (i in 0 until frameTimes.size()) {
                    val frameTime = frameTimes.keyAt(i)
                    val numFrames = frameTimes.valueAt(i)
                    totalFrames += numFrames
                    if (frameTime > Constants.FROZEN_FRAME_TIME) {
                        // Frozen frames mean the app appear frozen. The recommended thresholds is 700ms
                        frozenFrames += numFrames
                    }
                    if (frameTime > Constants.SLOW_FRAME_TIME) {
                        // Slow frames are anything above 16ms (i.e. 60 frames/second)
                        slowFrames += numFrames
                    }
                }
            }

        }
        if (totalFrames == 0 && slowFrames == 0 && frozenFrames == 0) {
            // All metrics are zero, no need to send screen trace.
            // return;
        }

        // Only incrementMetric if corresponding metric is non-zero.
        if (totalFrames > 0) {
            perfScreenTrace!!.putMetric(
              //  Constants.CounterNames.FRAMES_TOTAL.toString(),
               "total",
                totalFrames.toLong()
            )
        }
        if (slowFrames > 0) {
            perfScreenTrace!!.putMetric(
             //   Constants.CounterNames.FRAMES_SLOW.toString(),
                "slow",
                slowFrames.toLong()
            )
        }
        if (frozenFrames > 0) {
            perfScreenTrace!!.putMetric(
              //  Constants.CounterNames.FRAMES_FROZEN.toString(),
               "frozen",
                frozenFrames.toLong()
            )
        }
        Log.d(
            LOG_TAG, StringBuilder()
                .append("sendScreenTrace ").append(traceName)
                .append(", name: ").append(screenTraceName)
                .append(", total_frames: ").append(totalFrames)
                .append(", slow_frames: ").append(slowFrames)
                .append(", frozen_frames: ").append(frozenFrames).toString()
        )

        // Stop and record trace
        perfScreenTrace!!.stop()
    }

    private val screenTraceName: String
        /**
         * Reference: Fireperf Source Code - https://bityl.co/5v0V
         */
        private get() = Constants.SCREEN_TRACE_PREFIX + traceName // endregion

    companion object {
        private const val FRAME_METRICS_AGGREGATOR_CLASSNAME =
            "androidx.core.app.FrameMetricsAggregator"
        private const val LOG_TAG = "FrameRecorder"
        // region Public APIs
        /**
         * Force enable Hardware acceleration to support screen traces as we can't observe frame
         * rates for a non hardware accelerated view.
         *
         *
         * See: https://developer.android.com/guide/topics/graphics/hardware-accel
         */
        fun enableHardwareAcceleration(activity: Activity) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            )
        }
        // endregion
        // region Helper Functions
        /**
         * Reference: Fireperf Source Code - https://bityl.co/5v0Q
         */
        private fun isScreenTraceSupported(activity: Activity): Boolean {
            val hasFrameMetricsAggregatorClass = hasFrameMetricsAggregatorClass()
            val isActivityHardwareAccelerated =
                activity.window != null && activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED != 0
            val supported = hasFrameMetricsAggregatorClass && isActivityHardwareAccelerated
            Log.d(
                LOG_TAG, StringBuilder()
                    .append("isScreenTraceSupported(").append(activity).append("): ")
                    .append(supported)
                    .append(" [hasFrameMetricsAggregatorClass: ")
                    .append(hasFrameMetricsAggregatorClass)
                    .append(", isActivityHardwareAccelerated: ")
                    .append(isActivityHardwareAccelerated).append("]").toString()
            )
            return supported
        }

        /**
         * Reference: Fireperf Source Code - https://bityl.co/5v0H
         */
        private fun hasFrameMetricsAggregatorClass(): Boolean {
            return try {
                val initializerClass = Class.forName(FRAME_METRICS_AGGREGATOR_CLASSNAME)
                true
            } catch (e: ClassNotFoundException) {
                false
            }
        }
    }
}
