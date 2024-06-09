package com.example.perftester

import com.google.firebase.Firebase
import com.google.firebase.perf.metrics.Trace
import com.google.firebase.perf.performance
import com.google.firebase.tracing.FirebaseTrace


class FirebaseRenderingPerfReporter : RenderingPerfReporter {

    private var trace: Trace? = null

    override fun startTrace(name: String) {
        trace = Firebase.performance.newTrace(name)
        trace?.start()
    }

    override fun report(frameMetric: ScreenFrameMetric) {
        trace?.let {
            it.putMetric("total", frameMetric.totalFrames)
            it.putMetric("slow",frameMetric.slowFrames)
            it.putMetric("frozen", frameMetric.frozenFrames)
            it.putMetric("average", frameMetric.avgFrameRate)
            it.stop()
        }
    }

}


interface RenderingPerfReporter {

    fun startTrace(name: String)

    fun report(frameMetric: ScreenFrameMetric)
}