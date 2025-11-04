import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GuessWordGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Угадай слово");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        JLabel statusLabel = new JLabel("Выберите правильное слово:", SwingConstants.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        frame.add(buttonPanel, BorderLayout.CENTER);

        String[] words = {"Один", "Два", "Три"};

        Random random = new Random();
        String correctWord = words[random.nextInt(words.length)];

        JButton button1 = new JButton(words[0]);
        JButton button2 = new JButton(words[1]);
        JButton button3 = new JButton(words[2]);

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

        ActionListener answerChecker = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                if (source.getText().equals(correctWord)) {
                    JOptionPane.showMessageDialog(frame, "Верно!");
                    statusLabel.setText("Вы угадали!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Попробуй снова!");
                    statusLabel.setText("Неправильно. Попробуйте еще раз!");
                }
            }
        };

        ActionListener consoleLogger = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                System.out.println("Была нажата кнопка: \"" + source.getText() + "\"");
            }
        };

        button1.addActionListener(answerChecker);
        button1.addActionListener(consoleLogger);

        button2.addActionListener(answerChecker);
        button2.addActionListener(consoleLogger);

        button3.addActionListener(answerChecker);
        button3.addActionListener(consoleLogger);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton source = (JButton) e.getSource();
                System.out.println("Навёл на кнопку: " + source.getText());
            }
        };

        button1.addMouseListener(mouseHandler);
        button2.addMouseListener(mouseHandler);
        button3.addMouseListener(mouseHandler);

        frame.setVisible(true);
    }
}