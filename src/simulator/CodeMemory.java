package simulator;

import java.util.ArrayList;

//array of code line
public class CodeMemory {

    ArrayList<CodeLine> codeLineList = new ArrayList<>();

    public CodeMemory() {
    }

    public void populateCodeLine(CodeLine cd){
        codeLineList.add(cd);
    }

    public ArrayList<CodeLine> getCodeLineList() {
        return codeLineList;
    }
}
