package simulator;

public class Div4 {
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

    public void execute( Fetch fetch, Decode decode, IntegerFU integerFU, Mul1 mul1, Mul2 mul2, Div1 div1, Div2 div2,Div3 div3,Div4 div4,
                         Memory memory, Writeback writeback,CodeLine codeLine,RegisterFile registerFile,DataMemory dataMemory){

        if(!div3.outputBuffer.getInstructionString().isEmpty()){

            //copy o/p decode buffer to i/p buffer of execute
            div4.inputBuffer.setPc(div3.outputBuffer.getPc());
            div4.inputBuffer.setLiteralValue(div3.outputBuffer.getLiteralValue());
            div4.inputBuffer.setDestinationRegister(div3.outputBuffer.getDestinationRegister());
            div4.inputBuffer.setSourceRegisters(div3.outputBuffer.getSourceRegisters());
            div4.inputBuffer.setInstructionString(div3.outputBuffer.getInstructionString());
            div4.inputBuffer.setTargetMemoryData(div3.outputBuffer.getTargetMemoryData());

            div4.outputBuffer.setInstructionString(div4.inputBuffer.getInstructionString());
            div4.outputBuffer.setPc(div4.inputBuffer.getPc());
            div4.outputBuffer.setDestinationRegister(div4.inputBuffer.getDestinationRegister());
            div4.outputBuffer.setInstructionString(div4.inputBuffer.getInstructionString());
            div4.outputBuffer.setTargetMemoryData(div4.inputBuffer.getTargetMemoryData());

            System.out.println("-------DIV4 details------ ");
            System.out.println("Command: "+div4.outputBuffer.getInstructionString());
            System.out.println("PC value: "+div4.outputBuffer.getPc());

            //print source registers
            System.out.println("Target Memory Address: "+div4.outputBuffer.getTargetMemoryAddress());
            System.out.println("Destination Register: "+div4.outputBuffer.getDestinationRegister().getRegisterName());
            System.out.println("Target Memory Data: "+div4.outputBuffer.getTargetMemoryData());
            System.out.println();

            div3.initialise(); //reinitialise div2 stage for new set of instruction
        }


        if(div3.outputBuffer.getInstructionString().isEmpty()) {

            div3.execute(fetch,decode,integerFU,mul1, mul2,div1,div2,div3,div4,memory,writeback,codeLine,registerFile,dataMemory);
        }
    }
}
