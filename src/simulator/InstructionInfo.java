package simulator;

import java.util.ArrayList;

public class InstructionInfo {

    private int pc = 0;
    private String instructionString = "";
    private ArrayList<Register> sourceRegisters = new ArrayList<>();
    private Register destinationRegister = new Register();
    private int targetMemoryAddress = -99999;
    private int targetMemoryData = -99999;
    private int literalValue = -99999;
    private boolean isMulInstruction = false;
    private boolean isDivInstruction = false;
    private boolean isIntInstruction = false;

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public String getInstructionString() {
        return instructionString;
    }

    public void setInstructionString(String instructionString) {
        this.instructionString = instructionString;
    }

    public Register getDestinationRegister() {
        return destinationRegister;
    }

    public void setDestinationRegister(Register destinationRegister) {
        this.destinationRegister = destinationRegister;
    }

    public int getTargetMemoryAddress() {
        return targetMemoryAddress;
    }

    public void setTargetMemoryAddress(int targetMemoryAddress) {
        this.targetMemoryAddress = targetMemoryAddress;
    }

    public int getTargetMemoryData() {
        return targetMemoryData;
    }

    public void setTargetMemoryData(int targetMemoryData) {
        this.targetMemoryData = targetMemoryData;
    }

    public int getLiteralValue() {
        return literalValue;
    }

    public void setLiteralValue(int literalValue) {
        this.literalValue = literalValue;
    }

    public void setSourceRegisters(ArrayList<Register> sourceRegisters) {
        this.sourceRegisters = sourceRegisters;
    }

    public ArrayList<Register> getSourceRegisters() {
        return sourceRegisters;
    }

    public boolean isDivInstruction() {
        return isDivInstruction;
    }

    public void setDivInstruction(boolean divInstruction) {
        isDivInstruction = divInstruction;
    }

    public boolean isIntInstruction() {
        return isIntInstruction;
    }

    public void setIntInstruction(boolean intInstruction) {
        isIntInstruction = intInstruction;
    }

    public boolean isMulInstruction() {
        return isMulInstruction;
    }

    public void setMulInstruction(boolean mulInstruction) {
        isMulInstruction = mulInstruction;
    }
}
