package simulator;

public class IntegerFU {

    Stage stageinfo;
    InstructionInfo inputBuffer;
    InstructionInfo outputBuffer;
    private boolean integerFUexecuted = false;

    public boolean isIntegerFUexecuted() {
        return integerFUexecuted;
    }

    public void setIntegerFUexecuted(boolean integerFUexecuted) {
        this.integerFUexecuted = integerFUexecuted;
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
        setIntegerFUexecuted(false);

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


            if(!decode.outputBuffer.getInstructionString().isEmpty() && decode.outputBuffer.isIntInstruction() && null != decode.outputBuffer.getDestinationRegister().getRegisterName()) {

                //copy o/p decode buffer to i/p buffer of execute
                integerFU.inputBuffer.setPc(decode.outputBuffer.getPc());
                integerFU.inputBuffer.setLiteralValue(decode.outputBuffer.getLiteralValue());
                integerFU.inputBuffer.setDestinationRegister(decode.outputBuffer.getDestinationRegister());
                integerFU.inputBuffer.setSourceRegisters(decode.outputBuffer.getSourceRegisters());
                integerFU.inputBuffer.setInstructionString(decode.outputBuffer.getInstructionString());

                if(("MOVC").equals(integerFU.inputBuffer.getInstructionString())){

                    //for case MOVC R1 10
                    integerFU.outputBuffer.setTargetMemoryData(integerFU.inputBuffer.getLiteralValue());

                    //for case MOVC R1 R2
//                for (int i = 0; i < integerFU.inputBuffer.getSourceRegisters().length; i++) {
//                    if(null != integerFU.inputBuffer.getSourceRegisters()[i]){
//                        integerFU.outputBuffer.setTargetMemoryData(integerFU.inputBuffer.getSourceRegisters()[i].getValue());
//                    }
//                }
                }

                else if(("LOAD").equals(integerFU.inputBuffer.getInstructionString())){

                    //for case LOAD R3 R2 R1
//                if(null != integerFU.inputBuffer.getSourceRegisters().get(0) && null != integerFU.inputBuffer.getSourceRegisters().get(1)){
//                    integerFU.outputBuffer.setTargetMemoryAddress(integerFU.inputBuffer.getSourceRegisters().get(0).getValue() + integerFU.inputBuffer.getSourceRegisters().get(1).getValue());
//                }

                    if(integerFU.inputBuffer.getSourceRegisters().size() >0){

                        //for case LOAD R3 R2 10
                        if(null != integerFU.inputBuffer.getSourceRegisters().get(0)){
                            integerFU.outputBuffer.setTargetMemoryAddress(integerFU.inputBuffer.getSourceRegisters().get(0).getValue() + integerFU.inputBuffer.getLiteralValue());
                        }
                    }

//                else if(null != integerFU.inputBuffer.getSourceRegisters().get(1)){
//                    integerFU.outputBuffer.setTargetMemoryAddress(integerFU.inputBuffer.getSourceRegisters().get(1).getValue() + integerFU.inputBuffer.getLiteralValue());
//                }

                }

                else if(("STORE").equals(integerFU.inputBuffer.getInstructionString())){

                    //for case STORE R3 R2 R1
//                if(null != integerFU.inputBuffer.getSourceRegisters().get(0) && null != integerFU.inputBuffer.getSourceRegisters().get(1)){
//                    integerFU.outputBuffer.setTargetMemoryAddress(integerFU.inputBuffer.getSourceRegisters().get(0).getValue() + integerFU.inputBuffer.getSourceRegisters().get(1).getValue());
//                }

                    //for case STORE R3 R2 10
                    if(integerFU.inputBuffer.getSourceRegisters().size() > 0){
                        if(null != integerFU.inputBuffer.getSourceRegisters().get(0)){
                            integerFU.outputBuffer.setTargetMemoryAddress(integerFU.inputBuffer.getSourceRegisters().get(0).getValue() + integerFU.inputBuffer.getLiteralValue());
                        }
                    }


//                else if(null != integerFU.inputBuffer.getSourceRegisters().get(1)){
//                    integerFU.outputBuffer.setTargetMemoryAddress(integerFU.inputBuffer.getSourceRegisters().get(1).getValue() + integerFU.inputBuffer.getLiteralValue());
//                }

                }

                else if(("ADD").equals(integerFU.inputBuffer.getInstructionString())){

                    if(integerFU.inputBuffer.getSourceRegisters().size() > 0){
                        //for case ADD R3 R2 R1
                        if(null != integerFU.inputBuffer.getSourceRegisters().get(0) && null != integerFU.inputBuffer.getSourceRegisters().get(1)){
                            integerFU.outputBuffer.setTargetMemoryData(integerFU.inputBuffer.getSourceRegisters().get(0).getValue() + integerFU.inputBuffer.getSourceRegisters().get(1).getValue());
                        }
                    }
                }

                else if(("SUB").equals(integerFU.inputBuffer.getInstructionString())){

                    if(integerFU.inputBuffer.getSourceRegisters().size() > 0) {
                        //for case SUB R3 R2 R1
                        if (null != integerFU.inputBuffer.getSourceRegisters().get(0) && null != integerFU.inputBuffer.getSourceRegisters().get(1)) {
                            integerFU.outputBuffer.setTargetMemoryData(integerFU.inputBuffer.getSourceRegisters().get(0).getValue() - integerFU.inputBuffer.getSourceRegisters().get(1).getValue());
                        }
                    }
                }

                else if(("XOR").equals(integerFU.inputBuffer.getInstructionString())){

                    if(integerFU.inputBuffer.getSourceRegisters().size() > 0){

                        //for case XOR R3 R2 R1
                        if(null != integerFU.inputBuffer.getSourceRegisters().get(0) && null != integerFU.inputBuffer.getSourceRegisters().get(1)){

                            if((new Integer(1)).equals(integerFU.inputBuffer.getSourceRegisters().get(0).getValue())
                                    && (new Integer(1)).equals(integerFU.inputBuffer.getSourceRegisters().get(1).getValue())
                                    || (new Integer(0)).equals(integerFU.inputBuffer.getSourceRegisters().get(0).getValue())
                                    && (new Integer(0)).equals(integerFU.inputBuffer.getSourceRegisters().get(1).getValue())){

                                integerFU.outputBuffer.setTargetMemoryData(new Integer(0));
                            }

                            else {
                                integerFU.outputBuffer.setTargetMemoryData(new Integer(1));
                            }
                        }
                    }
                }

                else if(("AND").equals(integerFU.inputBuffer.getInstructionString())){

                    if(integerFU.inputBuffer.getSourceRegisters().size() > 0){

                        //for case AND R3 R2 R1

                        if(null != integerFU.inputBuffer.getSourceRegisters().get(0) && null != integerFU.inputBuffer.getSourceRegisters().get(1)){

                            if((new Integer(1)).equals(integerFU.inputBuffer.getSourceRegisters().get(0).getValue())
                                    && (new Integer(0)).equals(integerFU.inputBuffer.getSourceRegisters().get(1).getValue())
                                    || (new Integer(0)).equals(integerFU.inputBuffer.getSourceRegisters().get(0).getValue())
                                    && (new Integer(1)).equals(integerFU.inputBuffer.getSourceRegisters().get(1).getValue())
                                    || (new Integer(0)).equals(integerFU.inputBuffer.getSourceRegisters().get(0).getValue())
                                    && (new Integer(0)).equals(integerFU.inputBuffer.getSourceRegisters().get(1).getValue())){

                                integerFU.outputBuffer.setTargetMemoryData(new Integer(0));
                            }

                            else {
                                integerFU.outputBuffer.setTargetMemoryData(new Integer(1));
                            }
                        }
                    }
                }

                else if(("OR").equals(integerFU.inputBuffer.getInstructionString())){

                    if(integerFU.inputBuffer.getSourceRegisters().size() > 0){

                        //for case OR R3 R2 R1
                        if(null != integerFU.inputBuffer.getSourceRegisters().get(0) && null != integerFU.inputBuffer.getSourceRegisters().get(1)){

                            if((new Integer(1)).equals(integerFU.inputBuffer.getSourceRegisters().get(0).getValue())
                                    && (new Integer(0)).equals(integerFU.inputBuffer.getSourceRegisters().get(1).getValue())
                                    || (new Integer(0)).equals(integerFU.inputBuffer.getSourceRegisters().get(0).getValue())
                                    && (new Integer(1)).equals(integerFU.inputBuffer.getSourceRegisters().get(1).getValue())
                                    || (new Integer(1)).equals(integerFU.inputBuffer.getSourceRegisters().get(0).getValue())
                                    && (new Integer(1)).equals(integerFU.inputBuffer.getSourceRegisters().get(1).getValue())){

                                integerFU.outputBuffer.setTargetMemoryData(new Integer(1));
                            }

                            else {
                                integerFU.outputBuffer.setTargetMemoryData(new Integer(0));
                            }
                        }
                    }
                }

                integerFU.outputBuffer.setInstructionString(integerFU.inputBuffer.getInstructionString());
                integerFU.outputBuffer.setPc(integerFU.inputBuffer.getPc());
                integerFU.outputBuffer.setDestinationRegister(integerFU.inputBuffer.getDestinationRegister());
//                integerFU.outputBuffer.setIntegerInstruction(integerFU.inputBuffer.isIntegerInstruction());

                System.out.println("-------IntegerFU details------ ");
                System.out.println("Command: "+integerFU.outputBuffer.getInstructionString());
                System.out.println("PC value: "+integerFU.outputBuffer.getPc());

                //print source registers
                System.out.println("Target Memory Address: "+integerFU.outputBuffer.getTargetMemoryAddress());
                System.out.println("Destination Register: "+integerFU.outputBuffer.getDestinationRegister().getRegisterName());
                System.out.println("Target Memory Data: "+integerFU.outputBuffer.getTargetMemoryData());
                System.out.println();

                decode.initialise(); //reinitialise D/RF stage for new set of instruction
                integerFU.setIntegerFUexecuted(true);
            }

            if(decode.outputBuffer.getInstructionString().isEmpty() || decode.outputBuffer.isIntInstruction()) {
                decode.execute(fetch,decode,integerFU,mul1,mul2,memory,writeback,codeLine, registerFile);
            }
            else
                return;

    }
}
