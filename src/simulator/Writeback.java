package simulator;

public class Writeback {

    Stage stageMem;
    InstructionInfo inputBuffer;
    InstructionInfo outputBuffer;

    public void initialise(){

        stageMem = new Stage();

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

        stageMem.setStalled(Stage.STALLED.FALSE);
        stageMem.setInputInstructionInfo(inputBuffer);
        stageMem.setOutputInstructionInfo(outputBuffer);
    }

    public void execute( Fetch fetch, Decode decode, IntegerFU integerFU, Mul1 mul1, Mul2 mul2, Memory memory, Writeback writeback, CodeLine codeLine, RegisterFile registerFile, DataMemory dataMemory, Div1
                         div1, Div2 div2, Div3 div3, Div4 div4) {

        if (!memory.outputBuffer.getInstructionString().isEmpty()) {

            //copy memory o/p buffer to i/p buffer of writeback

            writeback.inputBuffer.setPc(memory.outputBuffer.getPc());
            writeback.inputBuffer.setLiteralValue(memory.outputBuffer.getLiteralValue());
            writeback.inputBuffer.setDestinationRegister(memory.outputBuffer.getDestinationRegister());
            writeback.inputBuffer.setSourceRegisters(memory.outputBuffer.getSourceRegisters());
            writeback.inputBuffer.setInstructionString(memory.outputBuffer.getInstructionString());
            writeback.inputBuffer.setTargetMemoryData(memory.outputBuffer.getTargetMemoryData());
            writeback.inputBuffer.setTargetMemoryAddress(memory.outputBuffer.getTargetMemoryAddress());

            if (writeback.inputBuffer.getTargetMemoryData() == -99999) { //check with default value :if equal means nothing to write
                //do nothing
            } else if (!(writeback.inputBuffer.getTargetMemoryData() == -99999)) {

                writeback.inputBuffer.getDestinationRegister().setValue(writeback.inputBuffer.getTargetMemoryData());
            }

            writeback.outputBuffer.setInstructionString(writeback.inputBuffer.getInstructionString());
            writeback.outputBuffer.setDestinationRegister(writeback.inputBuffer.getDestinationRegister());
            writeback.outputBuffer.getDestinationRegister().setRegisterStatus(Register.STATUS.INVALID);
            writeback.outputBuffer.setSourceRegisters(writeback.inputBuffer.getSourceRegisters());

            System.out.println("-------WB details------ ");
            System.out.println("Command: " + memory.outputBuffer.getInstructionString());
            System.out.println("PC value: " + memory.outputBuffer.getPc());

            //print source registers
            System.out.println("Target Memory Address: " + memory.outputBuffer.getTargetMemoryAddress());
            System.out.println("Destination Register: " + memory.outputBuffer.getDestinationRegister().getRegisterName());
            System.out.println("Target Memory Data: " + memory.outputBuffer.getTargetMemoryData());
            System.out.println("Value in Destination Register: " + writeback.inputBuffer.getDestinationRegister().getValue());
            System.out.println("Instruction completed: " + memory.outputBuffer.getInstructionString());
            System.out.println();

            if (!writeback.outputBuffer.getInstructionString().isEmpty()) {

                //free source registers

                if (writeback.outputBuffer.getSourceRegisters().size() > 0) {  // previous instrcution had register as source

                    for (int i = 0; i < writeback.outputBuffer.getSourceRegisters().size(); i++) {

                        String registerToBeFreed = writeback.outputBuffer.getSourceRegisters().get(i).getRegisterName();
                        Register register = registerFile.getRegister(registerToBeFreed);
                        register.setRegisterStatus(Register.STATUS.VALID);

                    }
                }

                //free destination registers
                Register registerToBeFreed = writeback.outputBuffer.getDestinationRegister();
                registerToBeFreed.setRegisterStatus(Register.STATUS.VALID);

                memory.initialise(); //reinitialise memory
            }
        }

        if (memory.outputBuffer.getInstructionString().isEmpty()) {
                memory.execute(fetch, decode, integerFU, mul1, mul2, memory, writeback, codeLine, registerFile, dataMemory, div1, div2, div3, div4);
        }
    }
}
