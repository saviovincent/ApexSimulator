package simulator;

// Each Code line info
public class CodeLine {

    private String instruction_string;
    private int file_line_number;
    private int address;

    public CodeLine() {
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getInstruction_string() {
        return instruction_string;
    }

    public void setInstruction_string(String instruction_string) {
        this.instruction_string = instruction_string;
    }

    public int getFile_line_number() {
        return file_line_number;
    }

    public void setFile_line_number(int file_line_number) {
        this.file_line_number = file_line_number;
    }
}
