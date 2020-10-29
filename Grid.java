import java.util.ArrayList;
import java.util.Collections;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

 
class Grid extends JFrame {

    public static void main(String args[]) {
        Grid game = new Grid(5, 5, 10);
    }
    
    /**
     *2
     */
    private static final long serialVersionUID = 1L;
    private boolean bGrid[][];
    private int cGrid[][];
    private int numRows;
    private int numColumns;
    private int numBombs;
    private int winCondition;
    private JButton buttons[][];

    public Grid() {
        this(8, 8, 18);
    }

    public Grid(int rows, int cols) {
        this(rows, cols, 18);
    }

    public Grid(int rows, int cols, int numBombs) {
        super("Watch your step");
        numRows = rows;
        numColumns = cols;
        this.numBombs = numBombs;
        createBombGrid();
        createCountGrid();

        winCondition = (getNumRows() * getNumColumns()) - getNumBombs();
        buttons = new JButton[getNumRows()][getNumColumns()];
        setLayout(new GridLayout(getNumRows(), getNumColumns()));

        for (int i = 0; i < getNumRows(); i++) {
            for (int j = 0; j < getNumColumns(); j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                add(buttons[i][j]);
            }
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        isResizable();
        setSize(600, 600);
        setVisible(true);
    }

    //Getters
    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getNumBombs() {
        return numBombs;
    }

    public int getCountAtLocation(int row, int column) {
        int counter = 0;
        if (isBombAtLocation(row, column)) {
             counter++;
        }

        if (row + 1 < numRows) {
            if (isBombAtLocation(row + 1, column))
                counter++;
            if (column + 1 < numColumns) {
                if (isBombAtLocation(row + 1, column + 1))
                    counter++;
            }
            if (column - 1 >= 0) { 
                if (isBombAtLocation(row + 1, column - 1))
                    counter++;
            }
        }
        if (row - 1 >= 0) { 
            if (isBombAtLocation(row - 1, column))                              
                counter++;
            if (column - 1 >= 0) { 
                if (isBombAtLocation(row - 1, column - 1))
                        counter++;
            }
            if (column + 1 < numColumns) {                              
                if (isBombAtLocation(row - 1, column + 1)) 
                    counter++;
            }
        }
        if (column + 1 < numColumns) { 
            if (isBombAtLocation(row, column + 1))                                                                    
                counter++;
        }
        if (column - 1 >= 0) { 
            if (isBombAtLocation(row, column - 1))                                                           
                counter++;
        }
        return counter;
    }

    public boolean[][] getBombGrid() {
        boolean[][] bGridR = new boolean[numRows][numColumns];
        for (int i = 0; i < numRows; i++) {
            System.arraycopy(bGrid[i], 0, bGridR[i], 0, bGrid[i].length);
        }
        return bGridR;
    }

    public int[][] getCountGrid() {
        int[][] cGridR = new int[numRows][numColumns];
        for (int i = 0; i < numRows; i++) {
            System.arraycopy(cGrid[i], 0, cGridR[i], 0, cGrid[i].length);
        }
        return cGridR;
    }


    private void createBombGrid() {

        bGrid = new boolean[numRows][numColumns];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                bGrid[i][j] = false;
            }
        }

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i < numRows * numColumns; i++) {
            list.add(new Integer(i));
        }

        Collections.shuffle(list);

        for (int i = 0; i < numBombs; i++) {
            int number = (list.get(i));
            int row = new Integer(number / numColumns);
            int column = new Integer(number % numColumns);
            bGrid[row][column] = true;
        }
    }

    private void createCountGrid() {
        cGrid = new int[numRows][numColumns];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                cGrid[i][j] = getCountAtLocation(i, j);
            }
        }
    }

    public boolean isBombAtLocation(int row, int column) {
        return (bGrid[row][column]);
    }

    
    private void retryGame() {
        createBombGrid();
        createCountGrid();
        winCondition = (getNumRows() * getNumColumns()) - getNumBombs();
        for (int i = 0; i < getNumRows(); i++) {
            for (int j = 0; j < getNumColumns(); j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        int row, col;
        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isBombAtLocation(row, col)) {
                int[][] counts = getCountGrid();
                for (int i = 0; i < getNumRows(); i++) {
                    for (int j = 0; j < getNumColumns(); j++) {
                        if (isBombAtLocation(i, j)) {
                            buttons[i][j].setText("MINE");
                        } else {
                            buttons[i][j].setText(String.valueOf(counts[i][j])); 
                        }
                        buttons[i][j].setEnabled(false);
                    }
                } 
                int status = JOptionPane.showConfirmDialog(null, "You lost. Try again?", "Game over", JOptionPane.YES_NO_OPTION);
                if (status == JOptionPane.YES_OPTION) {
                    retryGame();
                } else {
                    System.exit(0);
                }
            } 
            else {
                buttons[row][col].setText(String.valueOf(getCountAtLocation(row, col)));
                buttons[row][col].setEnabled(false);
                winCondition--;
                if (winCondition == 0) {
                    int status = JOptionPane.showConfirmDialog(null, "You won! Want to play again?", "Game over", JOptionPane.YES_NO_OPTION);
                    if (status == JOptionPane.YES_OPTION) {
                        retryGame();
                    } else {
                        System.exit(0);
                    }
                }
            }
        }
    }
}