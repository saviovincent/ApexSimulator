package simulator;

public class Mul1 {

    Stage stageinfo;
    InstructionInfo inputBuffer;
    InstructionInfo outputBuffer;
    private boolean isInstructionInMul1 = false;

    public boolean isInstructionInMul1() {
        return isInstructionInMul1;
    }

    public void setInstructionInMul1(boolean instructionInMul1) {
        isInstructionInMul1 = instructionInMul1;
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
        setInstructionInMul1(false);


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

            if(!decode.outputBuffer.getInstructionString().isEmpty() && decode.outputBuffer.isMulInstruction()) {

                //copy o/p decode buffer to i/p buffer of execute
                mul1.inputBuffer.setPc(decode.outputBuffer.getPc());
                mul1.inputBuffer.setLiteralValue(decode.outputBuffer.getLiteralValue());
                mul1.inputBuffer.setDestinationRegister(decode.outputBuffer.getDestinationRegister());
                mul1.inputBuffer.setSourceRegisters(decode.outputBuffer.getSourceRegisters());
                mul1.inputBuffer.setInstructionString(decode.outputBuffer.getInstructionString());

                if (("MUL").equals(mul1.inputBuffer.getInstructionString())) {

                    if(mul1.inputBuffer.getSourceRegisters().size() == 2){
                        //for case MUL R3 R2 R1
                        if (null != mul1.inputBuffer.getSourceRegisters().get(0) && null != mul1.inputBuffer.getSourceRegisters().get(1)) {
                            mul1.outputBuffer.setTargetMemoryData(mul1.inputBuffer.getSourceRegisters().get(0).getValue() * mul1.inputBuffer.getSourceRegisters().get(1).getValue());
                        }
                    }
                }

                mul1.outputBuffer.setInstructionString(mul1.inputBuffer.getInstructionString());
                mul1.outputBuffer.setPc(mul1.inputBuffer.getPc());
                mul1.outputBuffer.setDestinationRegister(mul1.inputBuffer.getDestinationRegister());
                mul1.outputBuffer.setInstructionString(mul1.inputBuffer.getInstructionString());
                mul1.setInstructionInMul1(true); // flag to know mul1

                System.out.println("-------MUL1 details------ ");
                System.out.println("Command: "+mul1.outputBuffer.getInstructionString());
                System.out.println("PC value: "+mul1.outputBuffer.getPc());

                //print source registers
                System.out.println("Target Memory Address: "+mul1.outputBuffer.getTargetMemoryAddress());
                System.out.println("Destination Register: "+mul1.outputBuffer.getDestinationRegister().getRegisterName());
                System.out.println("Target Memory Data: "+mul1.outputBuffer.getTargetMemoryData());
                System.out.println();

                decode.initialise(); //reinitialise D/RF stage for new set of instruction
            }

            if(decode.outputBuffer.isMulInstruction()) {

                decode.execute(fetch,decode,integerFU,mul1,mul2,memory,writeback,codeLine, registerFile);
            }
            else
                return;
        }
}
