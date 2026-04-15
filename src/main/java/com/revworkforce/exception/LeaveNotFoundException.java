package com.revworkforce.exception;

public class LeaveNotFoundException extends RuntimeException {
    public LeaveNotFoundException(int leaveId) {
        super("Leave request not found with ID: " + leaveId);
    }

    public LeaveNotFoundException(String message) {
        super(message);
    }
}
