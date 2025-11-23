/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;


/**
 *
 * @author huyhoang
 */
public class ButtonEvent extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private int row;
    private int col;
    private int bound = 2;
    private int size = 50;
    private int score = 0;
    private JButton[][] btn;
    private Point p1 = null;
    private Point p2 = null;
    private Controller algorithm;
    private PointLine line;
    private MainFrame frame;
    private Color backGroundColor = Color.lightGray;
    private int item;
   

    public ButtonEvent(MainFrame frame, int row, int col) {
        this.frame = frame;
        this.row = row + 2;
        this.col = col + 2;
        item = row * col / 2;

        setLayout(new GridLayout(row, col, bound, bound));
        setBackground(backGroundColor);
        setPreferredSize(new Dimension((size + bound) * col, (size + bound)
                * row));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setAlignmentY(JPanel.CENTER_ALIGNMENT);

        newGame();

    }

    public void newGame() {
        algorithm = new Controller(this.frame, this.row, this.col);
        addArrayButton();

    }

    private void addArrayButton() {
        btn = new JButton[row][col];
        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                btn[i][j] = createButton(i + "," + j);
                Icon icon = getIcon(algorithm.getMatrix()[i][j]);
                btn[i][j].setIcon(icon);
                add(btn[i][j]);
            }
        }
    }

    private Icon getIcon(int index) {
        int width = 48, height = 48;
        Image image = new ImageIcon(getClass().getResource(
                "/icon/" + index + ".png")).getImage();
        Icon icon = new ImageIcon(image.getScaledInstance(width, height,
                image.SCALE_SMOOTH));
        return icon;

    }

    private JButton createButton(String action) {
        JButton btn = new JButton();
        btn.setActionCommand(action);
        btn.setBorder(null);
        btn.addActionListener(this);
        return btn;
    }

    public void execute(Point p1, Point p2) {
        System.out.println("delete");
        setDisable(btn[p1.x][p1.y]);
        setDisable(btn[p2.x][p2.y]);
    }

    private void setDisable(JButton btn) {
        btn.setIcon(null);
        btn.setBackground(backGroundColor);
        btn.setEnabled(false);
    }
    
    public void resetRemainingIcons() {
    // Lấy danh sách các icon còn lại và vị trí trống
    List<Integer> remainingIcons = new ArrayList<>();
    List<Point> emptyPositions = new ArrayList<>();
    
    // Thu thập các icon còn lại và vị trí trống
    for (int i = 1; i < row - 1; i++) {
        for (int j = 1; j < col - 1; j++) {
            if (btn[i][j].isEnabled() && algorithm.getMatrix()[i][j] != 0) {
                remainingIcons.add(algorithm.getMatrix()[i][j]);
            } else if (!btn[i][j].isEnabled()) {
                emptyPositions.add(new Point(i, j));
            }
        }
    }
    
    // Xáo trộn danh sách icon
    Collections.shuffle(remainingIcons);
    
    // Đặt lại các icon vào vị trí ngẫu nhiên
    Random rand = new Random();
    for (int i = 1; i < row - 1; i++) {
        for (int j = 1; j < col - 1; j++) {
            if (btn[i][j].isEnabled() && algorithm.getMatrix()[i][j] != 0) {
                // Lấy icon ngẫu nhiên từ danh sách
                if (!remainingIcons.isEmpty()) {
                    int randomIndex = rand.nextInt(remainingIcons.size());
                    int iconValue = remainingIcons.get(randomIndex);
                    algorithm.getMatrix()[i][j] = iconValue;
                    remainingIcons.remove(randomIndex);
                    
                    // Cập nhật icon hiển thị
                    Icon icon = getIcon(iconValue);
                    btn[i][j].setIcon(icon);
                }
            }
        }
    }
    
    // Reset border và các điểm đang chọn
    if (p1 != null) {
        btn[p1.x][p1.y].setBorder(null);
        p1 = null;
    }
    p2 = null;
    
    System.out.println("Icons have been reset!");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String btnIndex = e.getActionCommand();
        int indexDot = btnIndex.lastIndexOf(",");
        int x = Integer.parseInt(btnIndex.substring(0, indexDot));
        int y = Integer.parseInt(btnIndex.substring(indexDot + 1,
                btnIndex.length()));
        if (p1 == null) {
            p1 = new Point(x, y);
            btn[p1.x][p1.y].setBorder(new LineBorder(Color.red));
        } else {
            p2 = new Point(x, y);
            System.out.println("(" + p1.x + "," + p1.y + ")" + " --> " + "("
                    + p2.x + "," + p2.y + ")");
            line = algorithm.checkTwoPoint(p1, p2);
            if (line != null) {
                System.out.println("line != null");
                algorithm.getMatrix()[p1.x][p1.y] = 0;
                algorithm.getMatrix()[p2.x][p2.y] = 0;
                algorithm.showMatrix();
                execute(p1, p2);
                line = null;
                score += 10;
                item--;
                frame.time++;
                frame.lbScore.setText(score + "");
            }
            btn[p1.x][p1.y].setBorder(null);
            p1 = null;
            p2 = null;
            System.out.println("done");
//            if (item == 0) {
//                if (frame.showDialogNewGame(
//                        "You are winer!\nDo you want play again?", "Win", 1) == true) {
//                };
//            }
            // Trong actionPerformed(), tìm phần xử lý khi item == 0 và sửa thành:
            // Trong actionPerformed(), tìm phần xử lý khi item == 0 và sửa thành:
            if (item == 0) {
                // Tính điểm cuối cùng = 320 + thời gian còn lại
                int finalScore = 320 + frame.time;

                // Lấy thời điểm thực khi chiến thắng
                java.sql.Timestamp winTimestamp = new java.sql.Timestamp(System.currentTimeMillis());

                // Hiển thị popup thông báo chiến thắng với điểm số
                String winMessage = "You are winner!\n" +
                                   "Final Score: " + finalScore + " points\n" +
                                   "Time Remaining: " + frame.time + " seconds\n" +
                                   "Do you want to play again?";

                // Lưu điểm và thời điểm thực vào database
                DAO_pikachu dao = new DAO_pikachu();
                dao.saveScore(finalScore, winTimestamp);

                if (frame.showDialogNewGame(winMessage, "Win", 1) == true) {
                    // Xử lý khi chơi lại game
                };
            }
        }
    }
}