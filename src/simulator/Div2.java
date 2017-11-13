package simulator;

public class Div2 {
    Stage stageinfo;
    InstructionInfo inputBuffer;
    InstructionInfo outputBuffer;
    private boolean isInstructionInDiv2= false;

    public boolean isInstructionInDiv2() {
        return isInstructionInDiv2;
    }

    public void setInstructionInDiv2(boolean instructionInDiv2) {
        isInstructionInDiv2 = instructionInDiv2;
    }

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
        setInstructionInDiv2(false);

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

    public void execute(Fetch fetch, Decode decode, IntegerFU integerFU, Mul1 mul1, Mul2 mul2, Div1 div1, Div2 div2,Div3 div3,Div4 div4,
                        Memory memory, Writeback writeback,CodeLine codeLine,RegisterFile registerFile,DataMemory dataMemory){

        if(!div1.outputBuffer.getInstructionString().isEmpty()){

            //copy o/p decode buffer to i/p buffer of execute
            div2.inputBuffer.setPc(div1.outputBuffer.getPc());
            div2.inputBuffer.setLiteralValue(div1.outputBuffer.getLiteralValue());
            div2.inputBuffer.setDestinationRegister(div1.outputBuffer.getDestinationRegister());
            div2.inputBuffer.setSourceRegisters(div1.outputBuffer.getSourceRegisters());
            div2.inputBuffer.setInstructionString(div1.outputBuffer.getInstructionString());
            div2.inputBuffer.setTargetMemoryData(div1.outputBuffer.getTargetMemoryData());

            div2.outputBuffer.setInstructionString(div2.inputBuffer.getInstructionString());
            div2.outputBuffer.setPc(div2.inputBuffer.getPc());
            div2.outputBuffer.setDestinationRegister(div2.inputBuffer.getDestinationRegister());
            div2.outputBuffer.setInstructionString(div2.inputBuffer.getInstructionString());
            div2.outputBuffer.setTargetMemoryData(div2.inputBuffer.getTargetMemoryData());
            div2.setInstructionInDiv2(true); // flag to know div2

            System.out.println("-------DIV2 details------ ");
            System.out.println("Command: "+div2.outputBuffer.getInstructionString());
            System.out.println("PC value: "+div2.outputBuffer.getPc());

            //print source registers
            System.out.println("Target Memory Address: "+div2.outputBuffer.getTargetMemoryAddress());
            System.out.println("Destination Register: "+div2.outputBuffer.getDestinationRegister().getRegisterName());
            System.out.println("Target Memory Data: "+div2.outputBuffer.getTargetMemoryData());
            System.out.println();

            div1.initialise(); //reinitialise div2 stage for new set of instruction
        }

        if(div1.outputBuffer.getInstructionString().isEmpty()) {

            div1.execute(fetch,decode,integerFU,mul1, mul2,div1,div2,div3,div4,memory,writeback,codeLine,registerFile,dataMemory);
        }

    }
}
