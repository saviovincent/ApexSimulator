package simulator;

public class RegisterFile {

    Register[] registers = new Register[16];

    //initialise registers in RegisterFile

    public void initialiseRegisters() {

        registers[0] = new Register(0, Register.STATUS.VALID, "R0", 0);
        registers[1] = new Register(0, Register.STATUS.VALID, "R1", 1);
        registers[2] = new Register(0, Register.STATUS.VALID, "R2", 2);
        registers[3] = new Register(0, Register.STATUS.VALID, "R3", 3);
        registers[4] = new Register(0, Register.STATUS.VALID, "R4", 4);
        registers[5] = new Register(0, Register.STATUS.VALID, "R5", 5);
        registers[6] = new Register(0, Register.STATUS.VALID, "R6", 6);
        registers[7] = new Register(0, Register.STATUS.VALID, "R7", 7);
        registers[8] = new Register(0, Register.STATUS.VALID, "R8", 8);
        registers[9] = new Register(0, Register.STATUS.VALID, "R9", 9);
        registers[10] = new Register(0, Register.STATUS.VALID, "R10", 10);
        registers[11] = new Register(0, Register.STATUS.VALID, "R11", 11);
        registers[12] = new Register(0, Register.STATUS.VALID, "R12", 12);
        registers[13] = new Register(0, Register.STATUS.VALID, "R13", 13);
        registers[14] = new Register(0, Register.STATUS.VALID, "R14", 14);
        registers[15] = new Register(0, Register.STATUS.VALID, "R15", 15);

    }

    public Register[] getRegisters() {
        return registers;
    }

    //fetch a register from register file
    public Register getRegister(String registerName) {

        Register fetchedRegister = new Register();

        for (Register register : registers) {
            if (registerName.equals(register.getRegisterName())) {
                fetchedRegister = register;
                break;
            }
        }

        return fetchedRegister;
    }
}
