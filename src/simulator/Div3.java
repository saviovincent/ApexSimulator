package simulator;

public class Div3 {
    Stage stageinfo;
    InstructionInfo inputBuffer;
    InstructionInfo outputBuffer;
    private boolean isInstructionInDiv3= false;

    public boolean isInstructionInDiv3() {
        return isInstructionInDiv3;
    }

    public void setInstructionInDiv3(boolean instructionInDiv3) {
        isInstructionInDiv3 = instructionInDiv3;
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
        setInstructionInDiv3(false);


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

        if(!div2.outputBuffer.getInstructionString().isEmpty()){

            //copy o/p decode buffer to i/p buffer of execute
            div3.inputBuffer.setPc(div2.outputBuffer.getPc());
            div3.inputBuffer.setLiteralValue(div2.outputBuffer.getLiteralValue());
            div3.inputBuffer.setDestinationRegister(div2.outputBuffer.getDestinationRegister());
            div3.inputBuffer.setSourceRegisters(div2.outputBuffer.getSourceRegisters());
            div3.inputBuffer.setInstructionString(div2.outputBuffer.getInstructionString());
            div3.inputBuffer.setTargetMemoryData(div2.outputBuffer.getTargetMemoryData());

            div3.outputBuffer.setInstructionString(div3.inputBuffer.getInstructionString());
            div3.outputBuffer.setPc(div3.inputBuffer.getPc());
            div3.outputBuffer.setDestinationRegister(div3.inputBuffer.getDestinationRegister());
            div3.outputBuffer.setInstructionString(div3.inputBuffer.getInstructionString());
            div3.outputBuffer.setTargetMemoryData(div3.inputBuffer.getTargetMemoryData());
            div3.setInstructionInDiv3(true); // flag to know div3

            System.out.println("-------DIV3 details------ ");
            System.out.println("Command: "+div3.outputBuffer.getInstructionString());
            System.out.println("PC value: "+div3.outputBuffer.getPc());

            //print source registers
            System.out.println("Target Memory Address: "+div3.outputBuffer.getTargetMemoryAddress());
            System.out.println("Destination Register: "+div3.outputBuffer.getDestinationRegister().getRegisterName());
            System.out.println("Target Memory Data: "+div3.outputBuffer.getTargetMemoryData());
            System.out.println();

            div2.initialise(); //reinitialise div2 stage for new set of instruction

        }
        if(div2.outputBuffer.getInstructionString().isEmpty()) {

            div2.execute(fetch,decode,integerFU,mul1, mul2,div1,div2,div3,div4,memory,writeback,codeLine,registerFile,dataMemory);
        }

    }
}
