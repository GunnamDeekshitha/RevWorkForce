package com.revworkforce.exception;

public class InsufficientLeaveBalanceException extends RuntimeException {
    public InsufficientLeaveBalanceException(String leaveType, int requested) {
        super("Insufficient " + leaveType + " leave balance. Requested: " + requested + " days.");
    }

    public InsufficientLeaveBalanceException(String message) {
        super(message);
    }
}
