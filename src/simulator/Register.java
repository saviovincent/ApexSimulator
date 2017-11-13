package simulator;

public class Register {

    public enum STATUS {
        VALID,
        INVALID
    }

    private int value;
    private STATUS registerStatus;
    private String registerName;
    private int registerAddress;

    public Register() {
    }

    public Register(int value, STATUS registerStatus, String registerName, int registerAddress) {
        this.value = value;
        this.registerStatus = registerStatus;
        this.registerName = registerName;
        this.registerAddress = registerAddress;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public STATUS getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(STATUS registerStatus) {
        this.registerStatus = registerStatus;
    }

}
