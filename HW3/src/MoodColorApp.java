import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MoodColorApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Цвет настроения");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        JLabel moodLabel = new JLabel("Ваш цвет настроения: ", SwingConstants.CENTER);
        frame.add(moodLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        frame.add(buttonPanel, BorderLayout.CENTER);

        JButton redButton = new JButton("Красный");
        JButton blueButton = new JButton("Синий");
        JButton greenButton = new JButton("Зелёный");

        buttonPanel.add(redButton);
        buttonPanel.add(blueButton);
        buttonPanel.add(greenButton);

        ActionListener colorChanger = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                Color color = Color.BLACK;
                String colorName = "";

                switch (source.getText()) {
                    case "Красный":
                        color = Color.RED;
                        colorName = "Красный";
                        break;
                    case "Синий":
                        color = Color.BLUE;
                        colorName = "Синий";
                        break;
                    case "Зелёный":
                        color = Color.GREEN;
                        colorName = "Зелёный";
                        break;
                }

                frame.getContentPane().setBackground(color);
                moodLabel.setText("Ваш цвет настроения: " + colorName);
            }
        };

        ActionListener consoleLogger = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                System.out.println("Кнопка \"" + source.getText() + "\" была нажата");
            }
        };

        redButton.addActionListener(colorChanger);
        redButton.addActionListener(consoleLogger);

        blueButton.addActionListener(colorChanger);
        blueButton.addActionListener(consoleLogger);

        greenButton.addActionListener(colorChanger);
        greenButton.addActionListener(consoleLogger);

        /*MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JOptionPane.showMessageDialog(frame, "Ты навёл курсор на кнопку!");
            }
        };

        redButton.addMouseListener(mouseHandler);
        blueButton.addMouseListener(mouseHandler);
        greenButton.addMouseListener(mouseHandler);*/

        frame.setVisible(true);
    }
}