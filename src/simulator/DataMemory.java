package simulator;

//Data Memory to be written by Mem stage
public class DataMemory {

    private int data_array [] = new int[3999];

    public int[] getData_array() {
        return data_array;
    }

    public void setData_array(int[] data_array) {
        this.data_array = data_array;
    }

    //populate default values
    public void initialise() {

        for (int i = 0; i < data_array.length - 1; i++) {
            data_array[i] = -99999;
        }
    }
}
