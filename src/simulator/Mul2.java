package simulator;

public class Mul2 {

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

    public void execute( Fetch fetch, Decode decode, IntegerFU integerFU, Mul1 mul1, Mul2 mul2, Memory memory, Writeback writeback, CodeLine codeLine, RegisterFile registerFile, DataMemory dataMemory){

        if(!mul1.outputBuffer.getInstructionString().isEmpty()){

            //copy values into input buffer
            mul2.inputBuffer.setPc(mul1.outputBuffer.getPc());
            mul2.inputBuffer.setLiteralValue(mul1.outputBuffer.getLiteralValue());
            mul2.inputBuffer.setDestinationRegister(mul1.outputBuffer.getDestinationRegister());
            mul2.inputBuffer.setSourceRegisters(mul1.outputBuffer.getSourceRegisters());
            mul2.inputBuffer.setInstructionString(mul1.outputBuffer.getInstructionString());
            mul2.inputBuffer.setTargetMemoryData(mul1.outputBuffer.getTargetMemoryData());
            mul2.inputBuffer.setTargetMemoryAddress(mul1.outputBuffer.getTargetMemoryAddress());

            //copy values into o/p buffer
            mul2.outputBuffer.setInstructionString(mul2.inputBuffer.getInstructionString());
            mul2.outputBuffer.setPc(mul2.inputBuffer.getPc());
            mul2.outputBuffer.setDestinationRegister(mul2.inputBuffer.getDestinationRegister());
            mul2.outputBuffer.setSourceRegisters(mul2.inputBuffer.getSourceRegisters());
            mul2.outputBuffer.setInstructionString(mul2.inputBuffer.getInstructionString());
            mul2.outputBuffer.setTargetMemoryData(mul2.inputBuffer.getTargetMemoryData());
            mul2.outputBuffer.setTargetMemoryAddress(mul2.inputBuffer.getTargetMemoryAddress());

            System.out.println("-------MUL2 details------ ");
            System.out.println("Command: "+mul2.outputBuffer.getInstructionString());
            System.out.println("PC value: "+mul2.outputBuffer.getPc());

            //print source registers
            System.out.println("Target Memory Address: "+mul2.outputBuffer.getTargetMemoryAddress());
            System.out.println("Destination Register: "+mul2.outputBuffer.getDestinationRegister().getRegisterName());
            System.out.println("Target Memory Data: "+mul2.outputBuffer.getTargetMemoryData());
            System.out.println();

            mul1.initialise();
        }

        if(mul1.outputBuffer.getInstructionString().isEmpty() /*&& !decode.outputBuffer.isIntegerInstruction()*/) {

            mul1.execute(fetch,decode,integerFU,mul1, mul2,memory,writeback,codeLine,registerFile,dataMemory);
        }

    }
}
