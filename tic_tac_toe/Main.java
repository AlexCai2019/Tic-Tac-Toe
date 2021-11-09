package tic_tac_toe;

import java.awt.*;
import java.awt.event.*;

enum GameSymbol
{
    X,
    O,
    none
}

enum Result
{
    X_win,
    O_win,
    tie
}

class Game
{
    private final Frame frame;
    private final Button[][] o_x;
    private final GameSymbol[][] table;
    private final Label[] turn_label;
    private final GameSymbol[] turn_symbol;
    private final char[] turn_char;
    private final Button reset;
    private byte blocks;
    private byte turn;

    public Game()
    {
        frame = new Frame("井字遊戲");
        o_x = new Button[3][3];
        table = new GameSymbol[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                o_x[i][j] = new Button();
        turn_label = new Label[2];
        turn_label[0] = new Label("現在輪到");
        turn_label[1] = new Label("X");
        turn_symbol = new GameSymbol[]{GameSymbol.X,GameSymbol.O};
        turn_char = new char[]{'X','O'};
        reset = new Button("重置");
        blocks = 9;
        turn = 0;
    }

    public void run()
    {
        frame.setSize(880,640);
        frame.addWindowListener(new MainAdapter());
        frame.setLayout(null);

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                o_x[i][j].setBounds(i * 180 + 20,j * 180 + 60,180,180);
                o_x[i][j].setFont(new Font("新細明體", Font.PLAIN, 128));
                o_x[i][j].addActionListener(new OXListener(i,j));
                frame.add(o_x[i][j]);

                table[i][j] = GameSymbol.none;
            }
        }

        turn_label[0].setBounds(580,224,350,96);
        turn_label[0].setFont(new Font("新細明體", Font.PLAIN, 64));
        turn_label[1].setBounds(676,320,350,96);
        turn_label[1].setFont(new Font("新細明體", Font.PLAIN, 64));
        frame.add(turn_label[0]);
        frame.add(turn_label[1]);

        reset.setBounds(580, 420, 260, 84);
        reset.setFont(new Font("新細明體", Font.PLAIN, 32));
        reset.addActionListener(new ResetListener());

        frame.setVisible(true);
    }

    private boolean checkWinner()
    {
        if ((turn & 1) == 0)
        {
            for (int i = 0; i < 3; i++)
            {
                if (table[i][0] == GameSymbol.X && table[i][1] == GameSymbol.X && table[i][2] == GameSymbol.X)
                {
                    win(Result.X_win);
                    return true;
                }
                if (table[0][i] == GameSymbol.X && table[1][i] == GameSymbol.X && table[2][i] == GameSymbol.X)
                {
                    win(Result.X_win);
                    return true;
                }
            }

            if (table[0][0] == GameSymbol.X && table[1][1] == GameSymbol.X && table[2][2] == GameSymbol.X)
            {
                win(Result.X_win);
                return true;
            }
            if (table[0][2] == GameSymbol.X && table[1][1] == GameSymbol.X && table[2][0] == GameSymbol.X)
            {
                win(Result.X_win);
                return true;
            }
        }
        else
        {
            for (int i = 0; i < 3; i++)
            {
                if (table[i][0] == GameSymbol.O && table[i][1] == GameSymbol.O && table[i][2] == GameSymbol.O)
                {
                    win(Result.O_win);
                    return true;
                }
                if (table[0][i] == GameSymbol.O && table[1][i] == GameSymbol.O && table[2][i] == GameSymbol.O)
                {
                    win(Result.O_win);
                    return true;
                }
            }

            if (table[0][0] == GameSymbol.O && table[1][1] == GameSymbol.O && table[2][2] == GameSymbol.O)
            {
                win(Result.O_win);
                return true;
            }
            if (table[0][2] == GameSymbol.O && table[1][1] == GameSymbol.O && table[2][0] == GameSymbol.O)
            {
                win(Result.O_win);
                return true;
            }
        }

        if (blocks == 0)
        {
            win(Result.tie);
            return true;
        }

        return false;
    }

    private void win(Result result)
    {
        switch (result)
        {
            case X_win:
                turn_label[0].setText("獲勝的是");
                turn_label[1].setText("X");
                break;

            case O_win:
                turn_label[0].setText("獲勝的是");
                turn_label[1].setText("O");
                break;

            case tie:
                turn_label[0].setText("雙方平手");
                turn_label[1].setText("");
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        frame.add(reset);
    }

    private class OXListener implements ActionListener
    {
        private final int x, y;

        public OXListener(int set_x, int set_y)
        {
            x = set_x;
            y = set_y;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (table[x][y] == GameSymbol.none) //如果這個按鈕還沒被按過
            {
                table[x][y] = turn_symbol[turn & 1]; //紀錄到table
                o_x[x][y].setLabel(Character.toString(turn_char[turn & 1])); //設定按鈕為按下去的那方
                blocks--;
                if (!checkWinner()) //如果沒有找到贏家
                {
                    turn++; //換對方
                    turn_label[1].setText(Character.toString(turn_char[turn & 1]));
                }
            }
        }
    }

    private class ResetListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    o_x[i][j].setLabel("");
                    table[i][j] = GameSymbol.none;
                }
            }

            turn_label[0].setText("現在輪到");
            turn_label[1].setText("X");
            turn = 0;
            blocks = 9;

            frame.remove(reset);
        }
    }

    private class MainAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent windowEvent)
        {
            frame.dispose();
            System.exit(0);
        }
    }
}

public class Main
{
    public static void main(String[] args)
    {
        Game game = new Game();
        game.run();
    }
}