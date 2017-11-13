package simulator;

import java.io.*;
import java.util.ArrayList;

public class Main {

    private static String file;
    private static int lineNo = 1;
    private static int baseAddress = 4000;

    public static void main(String[] args) {

        //-------read file from cmd line-----------

        if(args.length > 0){
            file = args[0];
        }

        else {
            System.out.println("Input file not given");
        }

        //----------initialise all stage info--------------------

        Fetch fetch = new Fetch();
        fetch.initialise();

        Decode decode = new Decode();
        decode.initialise();

        IntegerFU integerFU = new IntegerFU();
        integerFU.initialise();

        Mul1 mul1 = new Mul1();
        mul1.initialise();

        Mul2 mul2 = new Mul2();
        mul2.initialise();

        Div1 div1 = new Div1();
        div1.initialise();

        Div2 div2 = new Div2();
        div2.initialise();

        Div3 div3 = new Div3();
        div3.initialise();

        Div4 div4 = new Div4();
        div4.initialise();

        Memory memory = new Memory();
        memory.initialise();

        Writeback writeback = new Writeback();
        writeback.initialise();

        // initialise data memory
        DataMemory dataMemory = new DataMemory();
        dataMemory.initialise();

        // initialise register files
        RegisterFile registerFile = new RegisterFile();
        registerFile.initialiseRegisters();

        //initialise codeMemory Array
        CodeMemory codeMemory = new CodeMemory();

        //read file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String currentInstruction;

            while ((currentInstruction = br.readLine()) != null && !currentInstruction.isEmpty()) {

                CodeLine codeLine = new CodeLine();
                codeLine.setInstruction_string(currentInstruction);
                codeLine.setFile_line_number(lineNo);
                codeLine.setAddress(baseAddress);

                codeMemory.populateCodeLine(codeLine);

                lineNo++;
                baseAddress += 4; // increment address by 4 per instruction

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<CodeLine> codeLineList = codeMemory.getCodeLineList();

        Stats stats = new Stats();
        int statsCounter = 0;

        //pipeline function starts
        for (CodeLine codeLine: codeLineList) {

            stats.setCycle(++statsCounter);
            System.out.println("***************t = "+ stats.getCycle() +"**********************");
            System.out.println();
            if(Stage.STALLED.FALSE == decode.stageinfo.getStalled()){
                writeback.execute(fetch, decode, integerFU, mul1, mul2, memory, writeback, codeLine, registerFile,dataMemory,div1, div2,div3,div4); //main control logic
            }
            else
            {
                writeback.execute(fetch, decode, integerFU, mul1, mul2, memory, writeback, null, registerFile,dataMemory,div1, div2,div3,div4); //main control logic
            }
            System.out.println();
            System.out.println();

            for (Register register:registerFile.getRegisters()) {
                if(register.getRegisterStatus() == Register.STATUS.INVALID){
                    System.out.print("Register name: "+register.getRegisterName()+" is invalid ");
                    System.out.println();
                }
            }

            if(Stage.STALLED.TRUE == decode.stageinfo.getStalled()) {

               clearStalling(decode.inputBuffer.getInstructionString(),registerFile,decode);

            }
        }

        //explicitly flush the 5 stage pipeline

        stats.setCycle(statsCounter++);
        System.out.println("-------t = "+ stats.getCycle() +"----------");
        System.out.println();
        writeback.execute(fetch, decode, integerFU, mul1, mul2, memory, writeback, null, registerFile,dataMemory,div1,div2,div3,div4);
        System.out.println();
        System.out.println();

        stats.setCycle(statsCounter++);
        System.out.println("-------t = "+ stats.getCycle() +"----------");
        System.out.println();
        writeback.execute(fetch, decode, integerFU, mul1, mul2, memory, writeback, null, registerFile,dataMemory,div1, div2,div3,div4);
        System.out.println();
        System.out.println();

        stats.setCycle(statsCounter++);
        System.out.println("-------t = "+ stats.getCycle() +"----------");
        System.out.println();
        writeback.execute(fetch, decode, integerFU, mul1, mul2, memory, writeback, null, registerFile,dataMemory,div1, div2,div3,div4);
        System.out.println();
        System.out.println();

        stats.setCycle(statsCounter++);
        System.out.println("-------t = "+ stats.getCycle() +"----------");
        System.out.println();
        writeback.execute(fetch, decode, integerFU, mul1, mul2, memory, writeback, null, registerFile,dataMemory,div1, div2,div3,div4);
        System.out.println();
        System.out.println();

        stats.setCycle(statsCounter++);
        System.out.println("-------t = "+ stats.getCycle() +"----------");
        System.out.println();
        writeback.execute(fetch, decode, integerFU, mul1, mul2, memory, writeback, null, registerFile,dataMemory,div1, div2,div3,div4);
        System.out.println();
        System.out.println();


        //display memory values
        for (int i = 0; i < dataMemory.getData_array().length; i++) {
            if ((dataMemory.getData_array()[i])!= -99999){
                System.out.println("Mem value at "+i+" is "+dataMemory.getData_array()[i]);
            }
        }
        System.out.println();

        //display register contents
        for (Register register:registerFile.getRegisters()) {
            System.out.println("Register: "+register.getRegisterName()+"Value: "+register.getValue());

        }

    }

    //different approach to clear stalling rather than providing a NOP
    //incomplete implementation
    private static void clearStalling(String instruction, RegisterFile registerFile, Decode decode){

        String[] arguments = instruction.split(" ");
        String[] formattedArguments = new String[5];  //keep formatted args remove commas
        String [] registerNames = new String[5];
        int countRegisterNames= 0;
        int validRegisterCount = 0;
        ArrayList<Register> sourceRegisters = new ArrayList<>();

        for (int i = 0; i < arguments.length; i++) {
            formattedArguments[i] = arguments[i].replaceAll(",$", "");
        }

        for (int i = 0; i < formattedArguments.length; i++) {

            if( null != formattedArguments[i] && formattedArguments[i].contains("R")){
                registerNames[i] = formattedArguments[i];
                countRegisterNames++;
            }
        }

        for (int i = 0; i < registerNames.length ; i++) {
            Register register;

            if(registerNames[i] != null){
                register = registerFile.getRegister(registerNames[i]);
                if(Register.STATUS.VALID == register.getRegisterStatus()){
                    validRegisterCount++;
                }
            }
        }

        if(countRegisterNames == validRegisterCount) {

            decode.stageinfo.setStalled(Stage.STALLED.FALSE);

            if (("MUL").equals(formattedArguments[0])) {
                decode.outputBuffer.setMulInstruction(true);
            } else if (("DIV").equals(formattedArguments[0])) {
                decode.outputBuffer.setDivInstruction(true);
            } else
                decode.outputBuffer.setIntInstruction(true);

            if (formattedArguments[0] != null &&
                    ("MOVC").equals(formattedArguments[0]) || ("LOAD").equals(formattedArguments[0]) || ("ADD").equals(formattedArguments[0]) || ("SUB").equals(formattedArguments[0])
                    || ("AND").equals(formattedArguments[0]) || ("OR").equals(formattedArguments[0]) || ("XOR").equals(formattedArguments[0]) || ("MUL").equals(formattedArguments[0])
                    || ("DIV").equals(formattedArguments[0])) {

                if (formattedArguments[1] != null) {
                    Register destregister = registerFile.getRegister(formattedArguments[1]); //rdest
                    decode.outputBuffer.setDestinationRegister(destregister);
                }
                if (formattedArguments[2] != null && formattedArguments[2].contains("R")) {  //rsrc1
                    Register sourceRegister1 = registerFile.getRegister(formattedArguments[2]);
                    sourceRegister1.setRegisterStatus(Register.STATUS.INVALID);
                    sourceRegisters.add(sourceRegister1);

                } else if (formattedArguments[2] != null && formattedArguments[2].contains("#")) {  //literal value
                    decode.outputBuffer.setLiteralValue(Integer.parseInt(formattedArguments[2].replaceAll("#", "")));
                }

                if (formattedArguments[3] != null && formattedArguments[3].contains("R")) {  //rsrc2
                    Register sourceRegister2 = registerFile.getRegister(formattedArguments[3]);
                    sourceRegister2.setRegisterStatus(Register.STATUS.INVALID);
                    sourceRegisters.add(sourceRegister2);
                } else if (formattedArguments[3] != null && formattedArguments[3].contains("#")) {  //literal value
                    decode.outputBuffer.setLiteralValue(Integer.parseInt(formattedArguments[2].replaceAll("#", "")));
                }

                decode.outputBuffer.setSourceRegisters(sourceRegisters);
            }
        }
    }
}
