package simulator;

public class Memory {

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

    public void execute(Fetch fetch, Decode decode, IntegerFU integerFU, Mul1 mul1, Mul2 mul2, Memory memory, Writeback writeback, CodeLine codeLine, RegisterFile registerFile, DataMemory dataMemory,
                        Div1 div1, Div2 div2, Div3 div3, Div4 div4){

        if ((!mul2.outputBuffer.getInstructionString().isEmpty() && integerFU.outputBuffer.getInstructionString().isEmpty() && div4.outputBuffer.getInstructionString().isEmpty())
                || (!mul2.outputBuffer.getInstructionString().isEmpty() && !integerFU.outputBuffer.getInstructionString().isEmpty() && div4.outputBuffer.getInstructionString().isEmpty())) {

            //copy o/p mul2 buffer to i/p buffer of mem

            memory.inputBuffer.setPc(mul2.outputBuffer.getPc());
            memory.inputBuffer.setLiteralValue(mul2.outputBuffer.getLiteralValue());
            memory.inputBuffer.setDestinationRegister(mul2.outputBuffer.getDestinationRegister());
            memory.inputBuffer.setSourceRegisters(mul2.outputBuffer.getSourceRegisters());
            memory.inputBuffer.setInstructionString(mul2.outputBuffer.getInstructionString());
            memory.inputBuffer.setTargetMemoryData(mul2.outputBuffer.getTargetMemoryData());
            memory.inputBuffer.setTargetMemoryAddress(mul2.outputBuffer.getTargetMemoryAddress());

            performMemoryExecution(memory, dataMemory);
            mul2.initialise(); //reinitialise execute stage for new set of instruction
        }

        else if (!integerFU.outputBuffer.getInstructionString().isEmpty() && div4.outputBuffer.getInstructionString().isEmpty() && mul2.outputBuffer.getInstructionString().isEmpty()){

            //copy o/p integerFU buffer to i/p buffer of mem

            memory.inputBuffer.setPc(integerFU.outputBuffer.getPc());
            memory.inputBuffer.setLiteralValue(integerFU.outputBuffer.getLiteralValue());
            memory.inputBuffer.setDestinationRegister(integerFU.outputBuffer.getDestinationRegister());
            memory.inputBuffer.setSourceRegisters(integerFU.outputBuffer.getSourceRegisters());
            memory.inputBuffer.setInstructionString(integerFU.outputBuffer.getInstructionString());
            memory.inputBuffer.setTargetMemoryData(integerFU.outputBuffer.getTargetMemoryData());
            memory.inputBuffer.setTargetMemoryAddress(integerFU.outputBuffer.getTargetMemoryAddress());

            performMemoryExecution(memory, dataMemory);
            integerFU.initialise(); //reinitialise execute stage for new set of instruction
        }

        else if (!div4.outputBuffer.getInstructionString().isEmpty() && !mul2.outputBuffer.getInstructionString().isEmpty() &&
                !integerFU.outputBuffer.getInstructionString().isEmpty() || !div4.outputBuffer.getInstructionString().isEmpty()){

            //copy o/p Div4 buffer to i/p buffer of mem

            memory.inputBuffer.setPc(div4.outputBuffer.getPc());
            memory.inputBuffer.setLiteralValue(div4.outputBuffer.getLiteralValue());
            memory.inputBuffer.setDestinationRegister(div4.outputBuffer.getDestinationRegister());
            memory.inputBuffer.setSourceRegisters(div4.outputBuffer.getSourceRegisters());
            memory.inputBuffer.setInstructionString(div4.outputBuffer.getInstructionString());
            memory.inputBuffer.setTargetMemoryData(div4.outputBuffer.getTargetMemoryData());
            memory.inputBuffer.setTargetMemoryAddress(div4.outputBuffer.getTargetMemoryAddress());

            performMemoryExecution(memory, dataMemory);
            div4.initialise(); //reinitialise execute stage for new set of instruction
        }

        if(decode.outputBuffer.isIntInstruction()){
            integerFU.execute(fetch,decode,integerFU,mul1, mul2,memory,writeback,codeLine,registerFile,dataMemory);
        }
        if((decode.outputBuffer.isDivInstruction() && !integerFU.isIntegerFUexecuted()) || div1.isInstructionInDiv1() || div2.isInstructionInDiv2() || div3.isInstructionInDiv3()){
            div4.execute(fetch,decode,integerFU,mul1, mul2,div1,div2,div3,div4,memory,writeback,codeLine,registerFile,dataMemory);
        }
        if((decode.outputBuffer.isMulInstruction() && !integerFU.isIntegerFUexecuted()) || mul1.isInstructionInMul1()){
            mul2.execute(fetch,decode,integerFU,mul1, mul2,memory,writeback,codeLine,registerFile,dataMemory);
        }
        if(!decode.outputBuffer.isIntInstruction() && !decode.outputBuffer.isDivInstruction() && !decode.outputBuffer.isMulInstruction()){
            integerFU.execute(fetch,decode,integerFU,mul1, mul2,memory,writeback,codeLine,registerFile,dataMemory);
        }
    }

    private void performMemoryExecution(Memory memory, DataMemory dataMemory){

        if(memory.inputBuffer.getTargetMemoryAddress() != -99999 && memory.inputBuffer.getTargetMemoryData() != -99999
                && ("LOAD").equals(memory.inputBuffer.getInstructionString())){  //check if not default

            memory.inputBuffer.setTargetMemoryData(dataMemory.getData_array()[memory.inputBuffer.getTargetMemoryAddress()]);
        }

        else if (memory.inputBuffer.getTargetMemoryAddress() != -99999 && memory.inputBuffer.getTargetMemoryData() != -99999
                && ("STORE").equals(memory.inputBuffer.getInstructionString())){  //check if not default

            dataMemory.getData_array()[memory.inputBuffer.getTargetMemoryAddress()] = memory.inputBuffer.getDestinationRegister().getValue();
        }

        else{
            System.out.println("NOP for instruction: "+memory.inputBuffer.getInstructionString());
            //do nothing
        }

        memory.outputBuffer.setInstructionString(memory.inputBuffer.getInstructionString());
        memory.outputBuffer.setPc(memory.inputBuffer.getPc());
        memory.outputBuffer.setDestinationRegister(memory.inputBuffer.getDestinationRegister());
        memory.outputBuffer.setSourceRegisters(memory.inputBuffer.getSourceRegisters());
        memory.outputBuffer.setInstructionString(memory.inputBuffer.getInstructionString());
        memory.outputBuffer.setTargetMemoryData(memory.inputBuffer.getTargetMemoryData());
        memory.outputBuffer.setTargetMemoryAddress(memory.inputBuffer.getTargetMemoryAddress());

        System.out.println("-------Mem details------ ");
        System.out.println("Command: "+memory.outputBuffer.getInstructionString());
        System.out.println("PC value: "+memory.outputBuffer.getPc());

        //print source registers
        System.out.println("Target Memory Address: "+memory.outputBuffer.getTargetMemoryAddress());
        System.out.println("Destination Register: "+memory.outputBuffer.getDestinationRegister().getRegisterName());
        System.out.println("Target Memory Data: "+memory.outputBuffer.getTargetMemoryData());
        System.out.println();

    }
}
