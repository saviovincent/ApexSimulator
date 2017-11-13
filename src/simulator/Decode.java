package simulator;

import java.util.ArrayList;

public class Decode {

    Stage stageinfo;
    InstructionInfo inputBuffer;
    InstructionInfo outputBuffer;

    Register sourceRegister1 = null;
    Register sourceRegister2 = null;
    Register destregister = null;
    ArrayList<Register> sourceRegisters = new ArrayList<>();  // used to populate source registers

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
        inputBuffer.setIntInstruction(inputBuffer.isIntInstruction());
        inputBuffer.setMulInstruction(inputBuffer.isMulInstruction());
        inputBuffer.setDivInstruction(inputBuffer.isDivInstruction());

        outputBuffer.setPc(outputBuffer.getPc());
        outputBuffer.setDestinationRegister(outputBuffer.getDestinationRegister());
        outputBuffer.setInstructionString(outputBuffer.getInstructionString());
        outputBuffer.setSourceRegisters(outputBuffer.getSourceRegisters());
        outputBuffer.setTargetMemoryAddress(outputBuffer.getTargetMemoryData());
        outputBuffer.setTargetMemoryData(outputBuffer.getTargetMemoryData());
        outputBuffer.setIntInstruction(outputBuffer.isIntInstruction());
        outputBuffer.setMulInstruction(outputBuffer.isMulInstruction());
        outputBuffer.setDivInstruction(outputBuffer.isDivInstruction());

        stageinfo.setStalled(Stage.STALLED.FALSE);
        stageinfo.setInputInstructionInfo(inputBuffer);
        stageinfo.setOutputInstructionInfo(outputBuffer);

        sourceRegister1 = null;
        sourceRegister2 = null;
        destregister = null;
        sourceRegisters = new ArrayList<>();
    }

    public void execute (Fetch fetch, Decode decode, IntegerFU integerFU, Mul1 mul1, Mul2 mul2, Memory memory, Writeback writeback, CodeLine codeLine, RegisterFile registerFile){

        if(Stage.STALLED.TRUE == decode.stageinfo.getStalled()){

            //do nothing
            return;
        }

        else if(Stage.STALLED.FALSE == decode.stageinfo.getStalled()){

            if(!fetch.outputBuffer.getInstructionString().isEmpty()) {

                System.out.println("-------D/RF details------ ");

                //copy o/p fetch buffer to i/p buffer of decode
                decode.inputBuffer.setPc(fetch.outputBuffer.getPc());
                decode.inputBuffer.setInstructionString(fetch.outputBuffer.getInstructionString());

                //start D/RF process
                String[] arguments = decode.inputBuffer.getInstructionString().split(" ");
                String[] formattedArguments = new String[5];  //keep formatted args remove commas

                for (int i = 0; i < arguments.length; i++) {
                    formattedArguments[i] = arguments[i].replaceAll(",$", "");
                }

                if (formattedArguments[0] != null &&
                        ("MOVC").equals(formattedArguments[0]) || ("LOAD").equals(formattedArguments[0]) || ("ADD").equals(formattedArguments[0]) || ("SUB").equals(formattedArguments[0])
                        || ("AND").equals(formattedArguments[0]) || ("OR").equals(formattedArguments[0]) || ("XOR").equals(formattedArguments[0]) || ("MUL").equals(formattedArguments[0])
                        || ("DIV").equals(formattedArguments[0])) {

                    if(Stage.STALLED.FALSE == decode.stageinfo.getStalled()){
                        decode.outputBuffer.setInstructionString(formattedArguments[0]); //MOVC
                        decode.outputBuffer.setPc(decode.inputBuffer.getPc());
                    }

                    if (formattedArguments[1] != null) {  //set destination register, fetched from register file using name

                        destregister = registerFile.getRegister(formattedArguments[1]); //rdest

                        if(Register.STATUS.INVALID == destregister.getRegisterStatus()){
                            decode.stageinfo.setStalled(Stage.STALLED.TRUE);
                            System.out.println("Stage is stalled with instruction: "+ decode.outputBuffer.getInstructionString()+" for register "+destregister.getRegisterName());
                            System.out.println();
                        }
                        else if(Register.STATUS.VALID == destregister.getRegisterStatus() && Stage.STALLED.FALSE == decode.stageinfo.getStalled()) {
                            destregister.setRegisterStatus(Register.STATUS.INVALID);
                            decode.outputBuffer.setDestinationRegister(destregister);
                        }
                    }

                    if (formattedArguments[2] != null && formattedArguments[2].contains("R")) {  //rsrc1
                        sourceRegister1 = registerFile.getRegister(formattedArguments[2]);

                        if(Register.STATUS.INVALID == sourceRegister1.getRegisterStatus()){
                            decode.stageinfo.setStalled(Stage.STALLED.TRUE);
                            System.out.println("Stage is stalled with instruction: "+ decode.outputBuffer.getInstructionString()+" for register "+sourceRegister1.getRegisterName());
                            System.out.println();
                        }
                        else if (Register.STATUS.VALID == sourceRegister1.getRegisterStatus() && Stage.STALLED.FALSE == decode.stageinfo.getStalled()) {
                            sourceRegister1.setRegisterStatus(Register.STATUS.INVALID);
                            sourceRegisters.add(sourceRegister1);
                        }
                    }

                    else if (formattedArguments[2] != null && formattedArguments[2].contains("#") && Stage.STALLED.FALSE == decode.stageinfo.getStalled()) {  //literal value
                        decode.outputBuffer.setLiteralValue(Integer.parseInt(formattedArguments[2].replaceAll("#", "")));
                    }

                    if (formattedArguments[3] != null && formattedArguments[3].contains("R")) {  //rsrc2
                        sourceRegister2 = registerFile.getRegister(formattedArguments[3]);

                        if(Register.STATUS.INVALID == sourceRegister2.getRegisterStatus()){
                            decode.stageinfo.setStalled(Stage.STALLED.TRUE);
                            System.out.println("Stage is stalled with instruction: "+ decode.outputBuffer.getInstructionString()+" for register "+ sourceRegister2.getRegisterName());
                            System.out.println();
                        }
                        else if (Register.STATUS.VALID == sourceRegister2.getRegisterStatus() && Stage.STALLED.FALSE == decode.stageinfo.getStalled()) {
                            sourceRegister2.setRegisterStatus(Register.STATUS.INVALID);
                            sourceRegisters.add(sourceRegister2);
                        }
                    }

                    else if (formattedArguments[3] != null && formattedArguments[3].contains("#")) {  //literal value
                        decode.outputBuffer.setLiteralValue(Integer.parseInt(formattedArguments[2].replaceAll("#", "")));
                    }

                    //clear register fetched in case of stall
                    if(decode.stageinfo.getStalled() == Stage.STALLED.TRUE){
                        if(Register.STATUS.INVALID == destregister.getRegisterStatus() && sourceRegisters.isEmpty()){
                            destregister.setRegisterStatus(Register.STATUS.VALID);
                            decode.outputBuffer.setDestinationRegister(new Register());
                        }
                    }
                }

                else if(formattedArguments[0] != null && ("STORE").equals(formattedArguments[0])){

                    if(Stage.STALLED.FALSE == decode.stageinfo.getStalled()){
                        decode.outputBuffer.setInstructionString(formattedArguments[0]); //STORE
                    }


                    if (formattedArguments[1] != null ) {  //set source register, fetched from register file using name
                        sourceRegister1 = registerFile.getRegister(formattedArguments[1]);

                        if(Register.STATUS.INVALID == sourceRegister1.getRegisterStatus()){
                            stageinfo.setStalled(Stage.STALLED.TRUE);
                            System.out.println("Stage is stalled with instruction: "+ decode.outputBuffer.getInstructionString());
                            System.out.println();
                        }

                        else if (Register.STATUS.VALID == sourceRegister1.getRegisterStatus()){
                            sourceRegister1.setRegisterStatus(Register.STATUS.INVALID);
                            sourceRegisters.add(sourceRegister1);
                        }
                    }

                    if (formattedArguments[2] != null && formattedArguments[2].contains("R")) {  //rdest

                        Register register = registerFile.getRegister(formattedArguments[2]);

                        if(Register.STATUS.INVALID == register.getRegisterStatus()){
                            stageinfo.setStalled(Stage.STALLED.TRUE);
                            System.out.println("Stage is stalled with instruction: "+ decode.outputBuffer.getInstructionString());
                            System.out.println();
                        }
                        else if (Register.STATUS.VALID == register.getRegisterStatus()){

                            register.setRegisterStatus(Register.STATUS.INVALID);
                            decode.outputBuffer.setDestinationRegister(register);
                        }
                    }

                    if (formattedArguments[3] != null && formattedArguments[3].contains("#")) {  //literal value
                        decode.outputBuffer.setLiteralValue(Integer.parseInt(formattedArguments[3].replaceAll("#", "")));
                    }
                }


                if(Stage.STALLED.FALSE == decode.stageinfo.getStalled()){
                    decode.outputBuffer.setSourceRegisters(sourceRegisters); //populate source registers

                    if(("MUL").equals(decode.outputBuffer.getInstructionString())){
                        decode.outputBuffer.setMulInstruction(true);
                    }

                    else if(("DIV").equals(decode.outputBuffer.getInstructionString())){
                        decode.outputBuffer.setDivInstruction(true);
                    }
                    else
                        decode.outputBuffer.setIntInstruction(true);
                }

                System.out.println("Command: "+decode.outputBuffer.getInstructionString());
                System.out.println("PC value: "+decode.outputBuffer.getPc());

                //print source registers
                for (int i = 0; i < decode.outputBuffer.getSourceRegisters().size() ; i++) {

                    if(null != (decode.outputBuffer.getSourceRegisters().get(i))){
                        System.out.println("Source Register "+i+": "+ decode.outputBuffer.getSourceRegisters().get(i).getRegisterName());
                    }
                    else{
                        System.out.println("Source Register: Not Applicable");
                    }
                }
                System.out.println("Destination Register: "+decode.outputBuffer.getDestinationRegister().getRegisterName());
                System.out.println("Literal Value: "+decode.outputBuffer.getLiteralValue());
                System.out.println();

                fetch.initialise(); //reinitialise fetch stage for new set of instruction
            }

            if(fetch.outputBuffer.getInstructionString().isEmpty()) {

                fetch.execute(fetch,decode,integerFU,mul1,mul2,memory,writeback,codeLine);
            }
        }
    }
}
