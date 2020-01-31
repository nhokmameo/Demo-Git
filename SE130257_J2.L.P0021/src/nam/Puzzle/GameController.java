package nam.Puzzle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author MSI
 */
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class GameController extends HandlingButton {

    final int SPACE_BUTTON = 30;
    final int SIZE_BUTTON = 50;

    // Create randomNumber in the list
    public List<Integer> randomNumber(int size) {
        ArrayList<Integer> listNumber = new ArrayList<>();
        for (int i = 1; i <= size * size; i++) {
            listNumber.add(i);
        }
        shuffle(size, listNumber);
        return listNumber;
    }

    //shuffled numbers in the list
    public List<Integer> shuffle(int size, List<Integer> listNumber) {
        do {
            Collections.shuffle(listNumber);
        } while (!checkShuffle(size, listNumber));
        return listNumber;
    }

    // Check shuffle's Algorithm always right!
    public boolean checkShuffle(int size, List<Integer> listNumber) {
        int count = 0;
        int posEmpty = 0;
        int max = listNumber.size();

        for (int i = 0; i < max; i++) {
            if (listNumber.get(i) == max) {
                posEmpty = 1;
                continue;
            }
            for (int j = i + 1; j < max; j++) {
                if (listNumber.get(j) == max) {
                    continue;
                }
                if (listNumber.get(i) > listNumber.get(j)) {
                    count++;
                }
            }
        }
        if (size % 2 == 1) {
            return count % 2 == 0;
        } else {
            return ((posEmpty / size + 1) % 2 == 0 && count % 2 == 0 || (posEmpty / size + 1) % 2 == 1 && count % 2 == 1);
        }
    }

    // create Game's Area
    public void createGameArea(HashMap<Integer, JButton> listButton, GameForm game) {
        int size = game.getEdge();
        ArrayList<Integer> listNumber = (ArrayList) randomNumber(size);
        game.getPnlAreaGame().removeAll();
        game.getPnlAreaGame().setLayout(new GridLayout(size, size,SPACE_BUTTON,SPACE_BUTTON));
        for (int i = 0; i < size*size; i++) {
            int num = listNumber.get(i);
            String txt = num % (size*size) !=0 ? num + "" : "";
            if(txt.equals("")){
                game.setEmptyPosi(i);
            }
            JButton btn = new JButton(txt);
            btn.setPreferredSize(new Dimension(SIZE_BUTTON,SIZE_BUTTON));
            addButtonAction(btn, listButton, game);
            listButton.put(i, btn);
            game.getPnlAreaGame().add(btn);
        }
        game.pack();
    }

    // Set size game area
    public void setSizeGameArea(GameForm game) {
        String sizeString = game.getCmbSize().getSelectedItem().toString();
        String[] sizeSquare = sizeString.split("x");
        try {
            int num = Integer.parseInt(sizeSquare[0]);
            game.setEdge(num);
        } catch (NumberFormatException nfe) {
            System.err.println("Size illegal");
        }
    }

    // Add action for btnNewGame and CmbSize
    public void addAction(HashMap<Integer, JButton> listButton, GameForm game) {
        game.getBtnNewGame().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (game.isFlag()) {
                    game.setFlag(false);
                    int confirm = JOptionPane.showConfirmDialog(game, "Do you really want to start new game?",
                            "Confirm Dialog", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        game.setFlag(true);
                        game.setTime(0);
                        game.getLbTime().setText(game.getTime() + " sec");
                        game.setMoveCount(0);
                        game.getLbCount().setText(Integer.toString(game.getMoveCount()));
                        createGameArea(listButton, game);
                    } else {
                        game.setFlag(true);
                    }
                } else {
                    game.setFlag(true);
                    game.setTime(0);
                    game.getLbTime().setText(game.getTime() + " sec");
                    game.setMoveCount(0);
                    game.getLbCount().setText(Integer.toString(game.getMoveCount()));
                    createGameArea(listButton, game);
                }

            }
        });
        game.getCmbSize().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSizeGameArea(game);
            }
        });
    }

    // Thread for time count
    public Thread ThreadElapse(GameForm game) {
        Thread elapsed = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (game.isFlag()) {
                        int time = game.getTime();
                        game.getLbTime().setText(++time + " sec");
                        game.setTime(time);
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        System.err.println("Thread elapse error");
                    }
                }
            }
        };
        return elapsed;
    }
}
