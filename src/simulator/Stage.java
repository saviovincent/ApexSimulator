package simulator;

public class Stage {

    public enum STALLED{
        TRUE,
        FALSE
    }

    private InstructionInfo inputInstructionInfo;
    private InstructionInfo outputInstructionInfo;
    private STALLED stalled = STALLED.FALSE;

    public InstructionInfo getInputInstructionInfo() {
        return inputInstructionInfo;
    }

    public void setInputInstructionInfo(InstructionInfo inputInstructionInfo) {
        this.inputInstructionInfo = inputInstructionInfo;
    }

    public InstructionInfo getOutputInstructionInfo() {
        return outputInstructionInfo;
    }

    public void setOutputInstructionInfo(InstructionInfo outputInstructionInfo) {
        this.outputInstructionInfo = outputInstructionInfo;
    }

    public STALLED getStalled() {
        return stalled;
    }

    public void setStalled(STALLED stalled) {
        this.stalled = stalled;
    }

}
