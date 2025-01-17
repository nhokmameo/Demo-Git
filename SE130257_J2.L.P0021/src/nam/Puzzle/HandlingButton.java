/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nam.Puzzle;

/**
 *
 * @author MSI
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class HandlingButton {

    // Add action to button 
    public void addButtonAction(JButton btn, HashMap<Integer, JButton> listButton, GameForm game) {
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.setFlag(true);
                String txt = btn.getText();
                if (swappable(txt, listButton, game)) {
                    swapCell(listButton, game);
                    updateCount(game);
                    solveWin(listButton, game);
                }
            }
        });
    }

    //When players win, player cannot interact with button
    public void removeButtonAction(HashMap<Integer, JButton> listButton) {
        for (Map.Entry<Integer, JButton> entry : listButton.entrySet()) {
            JButton value = entry.getValue();
            for (ActionListener e : value.getActionListeners()) {
                value.removeActionListener(e);
            }
        }
    }

    // Count number of the movement and display into screen
    public void updateCount(GameForm game) {
        int current = game.getMoveCount();
        game.setMoveCount(++current);
        game.getLbCount().setText(current + "");
    }

    public void swapCell(HashMap<Integer, JButton> listButton, GameForm game) {
        int current = game.getCurrentPosi();
        int empty = game.getEmptyPosi();
        String txt = listButton.get(current).getText();
        listButton.get(empty).setText(txt);
        listButton.get(current).setText("");
        game.setEmptyPosi(current);
    }

    public boolean swappable(String text, HashMap<Integer, JButton> listButton, GameForm game) {
        return isTop(text, listButton, game) || isBottom(text, listButton, game)
                || isLeft(text, listButton, game) || isRight(text, listButton, game);
    }

    public boolean isTop(String text, HashMap<Integer, JButton> listButton, GameForm game) {
        int size  = game.getEdge();
        int emp = game.getEmptyPosi();
        int posti = emp-size;
        if(posti>=0){
            String txt = listButton.get(posti).getText();
            if(txt.equals(text)){
                game.setCurrentPosi(posti);
                return true;
            }
        }
        return false;
    }

    public boolean isBottom(String text, HashMap<Integer, JButton> listButton, GameForm game) {
        int size = game.getEdge();
        int emp = game.getEmptyPosi();
        int posti = size+emp;
        if(posti<size*size){
            String txt = listButton.get(posti).getText();
            if(txt.equals(text)){
                game.setCurrentPosi(posti);
                return true;
            }
        }
        return false;
    }

    public boolean isRight(String text, HashMap<Integer, JButton> listButton, GameForm game) {
        int size = game.getEdge();
        int emp = game.getEmptyPosi();
        int post= emp+1;
        if(post % size !=0){
            String txt =listButton.get(post).getText();
            if(txt.equals(text)){
                game.setCurrentPosi(post);
                return true;
            }
        }
        return false;
    }

    public boolean isLeft(String text, HashMap<Integer, JButton> listButton, GameForm game) {
        int size = game.getEdge();
        int emp = game.getEmptyPosi();
        int post = emp-1;
        if(post>=0 && post % size !=size-1){
            String txt= listButton.get(post).getText();
            if(txt.equals(text)){
                game.setCurrentPosi(post);
                return true;
            }
        }
        return false;
    }

    // When players win, display message to screen, reset time and stop the interaction of players with buttons
    public void solveWin(HashMap<Integer, JButton> listButton, GameForm game) {
        if (checkWin(listButton)) {
            game.setFlag(false);
            JOptionPane.showMessageDialog(game, "You Win !!!", "Congratulate", 1);
            game.getLbTime().setText(Integer.toString(game.getTime()) + " sec ");
            removeButtonAction(listButton);
        }
    }

    // Check condition to win
    public boolean checkWin(HashMap<Integer, JButton> listButton) {
        // for loop to get key and vakue of HashMap
        for (Map.Entry<Integer, JButton> entry : listButton.entrySet()) {
            Integer key = entry.getKey();
            JButton value = entry.getValue();
            String txt = value.getText();
            if (!txt.equals("")) {
                int num = 0;
                try {
                    num = Integer.parseInt(txt);
                } catch (NumberFormatException numf) {
                    System.err.println("Convert number checkWin error");
                }
                if (num - 1 != key) {
                    return false;
                }
            }
        }
        return true;
    }
}
