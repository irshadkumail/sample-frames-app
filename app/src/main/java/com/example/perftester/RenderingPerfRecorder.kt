package com.example.perftester

import android.app.Activity
import android.util.Log
import android.util.SparseIntArray
import androidx.core.app.FrameMetricsAggregator
import com.google.firebase.perf.util.Constants

object EGRenderingRecorder : RenderingPerformanceRecorder {

    private val perfReporter: RenderingPerfReporter = FirebaseRenderingPerfReporter()
    private var frameMetricsAggregator: FrameMetricsAggregator? = null
    private var renderingTrace: RenderingTrace? = null

    override fun startRecording(screenName: String, activity: Activity) {
        when {
            renderingTrace == null -> {
                Log.d(this.javaClass.name, "Started recording for  $screenName")
                frameMetricsAggregator = FrameMetricsAggregator()
                frameMetricsAggregator?.add(activity)
                renderingTrace = RenderingTrace(screenName, activity.javaClass)
                perfReporter.startTrace(screenName)
            }

            renderingTrace != null && renderingTrace!!.screenName != screenName -> {
                captureMetrics(frameMetricsAggregator?.stop()).let { perfReporter.report(it) }
                Log.d(this.javaClass.name, "Started recording for  $screenName")
                frameMetricsAggregator = FrameMetricsAggregator()
                frameMetricsAggregator?.add(activity)
                renderingTrace = RenderingTrace(screenName, activity.javaClass)
                perfReporter.startTrace(screenName)
            }

            renderingTrace != null && renderingTrace!!.screenName == screenName -> {
                //Do Nothing
                Log.d(this.javaClass.name, "Already recording the screen")
            }
        }
    }

    private fun captureMetrics(metrics: Array<SparseIntArray?>?): ScreenFrameMetric {
        var totalFrameCount = 0L
        var slowFrames = 0L
        var frozenFrames = 0L
        var totalFrameTime = 0L

        // Stops recording metrics for this Activity and returns the currently-collected metrics
        if (metrics != null) {
            val frameTimes = metrics[FrameMetricsAggregator.TOTAL_INDEX]
            if (frameTimes != null) {
                for (i in 0 until frameTimes.size()) {
                    val frameTime = frameTimes.keyAt(i)
                    val numFrames = frameTimes.valueAt(i)
                    totalFrameCount += numFrames
                    totalFrameTime += frameTime
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
        Log.d(
            this.javaClass.name, StringBuilder()
                .append(", name: ").append(getCurrentScreen())
                .append(", total_frames: ").append(totalFrameCount)
                .append(", slow_frames: ").append(slowFrames)
                .append(", avg_frame_rate: ").append(totalFrameTime / totalFrameCount).append("ms")
                .append(", frozen_frames: ").append(frozenFrames).toString()

        )

        return ScreenFrameMetric(
            totalFrames = totalFrameCount,
            slowFrames = slowFrames,
            frozenFrames = frozenFrames,
            avgFrameRate = totalFrameTime / totalFrameCount
        )
    }

    override fun stopRecording(screenName: String) {
        if (renderingTrace != null) {
            captureMetrics(frameMetricsAggregator?.stop()).let { perfReporter.report(it) }
            renderingTrace = null
        } else {
            Log.d(this.javaClass.name, "No frames recorder so far")
        }
    }

    override fun getCurrentScreen(): String? {
        return renderingTrace?.screenName
    }


}

data class RenderingTrace(
    val screenName: String,
    val activityClassName: Class<Activity>
)

data class ScreenFrameMetric(
    val totalFrames: Long,
    val slowFrames: Long,
    val frozenFrames: Long,
    val avgFrameRate: Long,
)

interface RenderingPerformanceRecorder {

    fun startRecording(screenName: String, activity: Activity)

    fun stopRecording(screenName: String)

    fun getCurrentScreen(): String?

}