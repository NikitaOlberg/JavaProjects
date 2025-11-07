package com.englishtutor.view;

import com.englishtutor.controller.TutorController;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame {
    private final TutorController controller;

    public MainFrame(TutorController controller) {
        this.controller = controller;
        initializeWindow();
    }

    private void initializeWindow() {
        setTitle("English Language Tutor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JOptionPane.showMessageDialog(
                this,
                "Приложение успешно запущено!\n" +
                        "База данных содержит " + controller.getModel().getTotalWords() + " слова.\n\n",
                "English Language Tutor",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void updateUI() {
        // Реализация будет в следующем этапе
        System.out.println("UI update requested - Current score: " +
                controller.getModel().getCurrentScore());
    }

    public void showCorrectAnswer() {
        JOptionPane.showMessageDialog(this, "Правильно! 👍", "Отлично!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWrongAnswer(String correctAnswer) {
        JOptionPane.showMessageDialog(this,
                "Неправильно! 😕\nПравильный ответ: " + correctAnswer,
                "Попробуйте еще",
                JOptionPane.WARNING_MESSAGE);
    }

    public void showHint(String hint) {
        JOptionPane.showMessageDialog(this,
                "Подсказка: " + hint,
                "Подсказка",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showSessionComplete() {
        JOptionPane.showMessageDialog(this,
                "Сессия завершена!\n" + controller.getStatistics(),
                "Результаты",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
}