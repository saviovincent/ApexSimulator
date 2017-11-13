package simulator;

public class Fetch {

    Stage stageinfo;
    InstructionInfo inputBuffer;
    InstructionInfo outputBuffer;

    public void initialise(){

        stageinfo = new Stage();

        inputBuffer = new InstructionInfo();
        outputBuffer = new InstructionInfo();

        inputBuffer.setPc(inputBuffer.getPc());
        inputBuffer.setDestinationRegister(inputBuffer.getDestinationRegister());
        inputBuffer.setInstructionString(inputBuffer.getInstructionString());
        inputBuffer.setSourceRegisters(inputBuffer.getSourceRegisters());
        inputBuffer.setTargetMemoryAddress(inputBuffer.getTargetMemoryData());
        inputBuffer.setTargetMemoryData(inputBuffer.getTargetMemoryData());


        outputBuffer.setPc(outputBuffer.getPc());
        outputBuffer.setDestinationRegister(outputBuffer.getDestinationRegister());
        outputBuffer.setInstructionString(outputBuffer.getInstructionString());
        outputBuffer.setSourceRegisters(outputBuffer.getSourceRegisters());
        outputBuffer.setTargetMemoryAddress(outputBuffer.getTargetMemoryData());
        outputBuffer.setTargetMemoryData(outputBuffer.getTargetMemoryData());

        stageinfo.setStalled(Stage.STALLED.FALSE);
        stageinfo.setInputInstructionInfo(inputBuffer);
        stageinfo.setOutputInstructionInfo(outputBuffer);
    }

    //fetch instruction from memory
    public void execute(Fetch fetch, Decode decode, IntegerFU integerFU, Mul1 mul1, Mul2 mul2, Memory memory, Writeback writeback, CodeLine codeLine){

        if(fetch.outputBuffer.getInstructionString().isEmpty()) {

            if(null != codeLine){
                fetch.outputBuffer.setInstructionString(codeLine.getInstruction_string());
                fetch.outputBuffer.setPc(codeLine.getAddress());

                System.out.println("-------Fetch Details-----");
                System.out.println("Instruction String: " + fetch.outputBuffer.getInstructionString());
                System.out.println("PC value: " + fetch.outputBuffer.getPc());
                System.out.println();
            }

            return;
        }
    }
}
