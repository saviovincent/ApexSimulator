package simulator;

public class Div1 {
    Stage stageinfo;
    InstructionInfo inputBuffer;
    InstructionInfo outputBuffer;
    private boolean isInstructionInDiv1= false;

    public boolean isInstructionInDiv1() {
        return isInstructionInDiv1;
    }

    public void setInstructionInDiv1(boolean instructionInDiv1) {
        isInstructionInDiv1 = instructionInDiv1;
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
        setInstructionInDiv1(false);

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


        if(!decode.outputBuffer.getInstructionString().isEmpty() && decode.outputBuffer.isDivInstruction()){

            //copy o/p decode buffer to i/p buffer of execute
            div1.inputBuffer.setPc(decode.outputBuffer.getPc());
            div1.inputBuffer.setLiteralValue(decode.outputBuffer.getLiteralValue());
            div1.inputBuffer.setDestinationRegister(decode.outputBuffer.getDestinationRegister());
            div1.inputBuffer.setSourceRegisters(decode.outputBuffer.getSourceRegisters());
            div1.inputBuffer.setInstructionString(decode.outputBuffer.getInstructionString());
            div1.inputBuffer.setDivInstruction(decode.outputBuffer.isDivInstruction());

            if(div1.inputBuffer.getSourceRegisters().size() == 2){
                //for case DIV R3 R2 R1
                if (null != div1.inputBuffer.getSourceRegisters().get(0) && null != div1.inputBuffer.getSourceRegisters().get(1)) {
                    div1.outputBuffer.setTargetMemoryData(div1.inputBuffer.getSourceRegisters().get(0).getValue() / div1.inputBuffer.getSourceRegisters().get(1).getValue());
                }
            }

            div1.outputBuffer.setInstructionString(div1.inputBuffer.getInstructionString());
            div1.outputBuffer.setPc(div1.inputBuffer.getPc());
            div1.outputBuffer.setDestinationRegister(div1.inputBuffer.getDestinationRegister());
            div1.outputBuffer.setInstructionString(div1.inputBuffer.getInstructionString());
            div1.outputBuffer.setDivInstruction(div1.inputBuffer.isDivInstruction());
            div1.setInstructionInDiv1(true); // flag to know div1

            System.out.println("-------DIV1 details------ ");
            System.out.println("Command: "+div1.outputBuffer.getInstructionString());
            System.out.println("PC value: "+div1.outputBuffer.getPc());

            //print source registers
            System.out.println("Target Memory Address: "+div1.outputBuffer.getTargetMemoryAddress());
            System.out.println("Destination Register: "+div1.outputBuffer.getDestinationRegister().getRegisterName());
            System.out.println("Target Memory Data: "+div1.outputBuffer.getTargetMemoryData());
            System.out.println();

            decode.initialise(); //reinitialise D/RF stage for new set of instruction
        }

        if(decode.outputBuffer.isDivInstruction()) {

            decode.execute(fetch,decode,integerFU,mul1,mul2,memory,writeback,codeLine,registerFile);
        }
        else
            return;

    }
}
