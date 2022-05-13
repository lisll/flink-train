package com.datapipeline.streamAndBatch;

public enum Direction {
    EAST(0){
        @Override
        public String printDirection() {
            return "0";
        }
    },
    WEST(180){
        @Override
        public String printDirection() {
            return "180";
        }
    },
    NORTH(90){
        @Override
        public String printDirection() {
            return "90";
        }
    },
    SOUTH(270){
        @Override
        public String printDirection() {
            return "270";
        }
    };
    // constructor
    private Direction(int angle) {
        System.out.println("调用Direction中的构造方法"+angle);
        this.angle = angle;
    }
    // internal state
    private int angle;
    public int getAngle() {
        return angle;
    }
    public abstract String printDirection();
}
