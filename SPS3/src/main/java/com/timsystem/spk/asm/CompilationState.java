package com.timsystem.spk.asm;

public class CompilationState {

    private int segmentState, buildState;

    public CompilationState(int segmentState, int buildState) {
        this.segmentState = segmentState;
        this.buildState = buildState;
    }

    public int getSegmentState() {
        return segmentState;
    }

    public void setSegmentState(int segmentState) {
        this.segmentState = segmentState;
    }

    public int getBuildState() {
        return buildState;
    }

    public void setBuildState(int buildState) {
        this.buildState = buildState;
    }
}
