package com.forkexec.pts.ws;

public class Faults {
    private Faults() {
    }

    public static String msgAddUser(String user) {
        return String.format("User %s already exists", user);
    }

    public static void emailAlreadyExists(Throwable cause, String desc) throws EmailAlreadyExistsFault_Exception {
        EmailAlreadyExistsFault info = new EmailAlreadyExistsFault();
        info.setMessage(desc);
        throw new EmailAlreadyExistsFault_Exception(desc, info, cause);
    }
}
