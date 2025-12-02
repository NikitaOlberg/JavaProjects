package com.englishtutor.view;
import com.englishtutor.controller.TutorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleFrame extends JFrame {
    private final TutorController controller;

    // Компоненты интерфейса
    private JLabel wordLabel, scoreLabel, progressLabel, modeLabel;
    private JTextField answerField;
    private JButton checkButton, nextButton, hintButton, startButton;
    private JComboBox<String> modeCombo;
    private JSpinner wordCountSpinner;
    private JPanel mainPanel, controlPanel, infoPanel;

    public SimpleFrame(TutorController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Английский Тренажер");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Создаем основной интерфейс
        createMainInterface();

        setContentPane(mainPanel);
    }

    private void createMainInterface() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель информации
        infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Центральная панель со словом
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Слово для перевода"));

        wordLabel = new JLabel("Нажмите 'Начать' для старта", JLabel.CENTER);
        wordLabel.setFont(new Font("Arial", Font.BOLD, 24));
        wordLabel.setForeground(Color.BLUE);
        centerPanel.add(wordLabel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Панель управления
        controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Информация"));

        scoreLabel = new JLabel("Очки: 0");
        progressLabel = new JLabel("Прогресс: 0/0");
        modeLabel = new JLabel("Режим: англ → рус");

        panel.add(scoreLabel);
        panel.add(progressLabel);
        panel.add(modeLabel);

        // Поле для выбора режима
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modePanel.add(new JLabel("Режим:"));

        modeCombo = new JComboBox<>(new String[]{"английский → русский", "русский → английский"});
        modePanel.add(modeCombo);

        panel.add(modePanel);

        // Поле для выбора количества слов
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countPanel.add(new JLabel("Слов в сессии:"));

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(10, 5, 50, 5);
        wordCountSpinner = new JSpinner(spinnerModel);
        countPanel.add(wordCountSpinner);

        panel.add(countPanel);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Управление"));

        // Поле для ввода ответа
        answerField = new JTextField(20);
        answerField.setFont(new Font("Arial", Font.PLAIN, 16));
        answerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    controller.checkAnswer(answerField.getText());
                    answerField.setText("");
                }
            }
        });

        // Кнопки
        checkButton = createButton("Проверить", Color.GREEN);
        nextButton = createButton("Следующее", Color.BLUE);
        hintButton = createButton("Подсказка", Color.ORANGE);
        startButton = createButton("Начать сессию", new Color(0, 150, 0));

        // Настройка кнопок
        Dimension buttonSize = new Dimension(140, 35);
        checkButton.setPreferredSize(buttonSize);
        nextButton.setPreferredSize(buttonSize);
        hintButton.setPreferredSize(buttonSize);
        startButton.setPreferredSize(new Dimension(150, 35));

        // Добавляем обработчики
        checkButton.addActionListener(e -> {
            controller.checkAnswer(answerField.getText());
            answerField.setText("");
            answerField.requestFocus();
        });

        nextButton.addActionListener(e -> controller.nextWord());
        hintButton.addActionListener(e -> controller.showHint());
        startButton.addActionListener(e -> startNewSession());

        // Изначально отключаем кнопки проверки
        checkButton.setEnabled(false);
        nextButton.setEnabled(false);
        hintButton.setEnabled(false);

        panel.add(new JLabel("Ваш ответ:"));
        panel.add(answerField);
        panel.add(checkButton);
        panel.add(nextButton);
        panel.add(hintButton);
        panel.add(startButton);

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEtchedBorder());

        // Эффект при наведении
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void startNewSession() {
        String mode = modeCombo.getSelectedIndex() == 0 ? "en-ru" : "ru-en";
        int wordCount = (int) wordCountSpinner.getValue();

        controller.startNewSession(wordCount, mode);

        // Активируем кнопки
        checkButton.setEnabled(true);
        nextButton.setEnabled(true);
        hintButton.setEnabled(true);
        startButton.setEnabled(false);

        answerField.requestFocus();
    }

    public void updateUI() {
        SwingUtilities.invokeLater(() -> {
            // Обновляем текущее слово
            String question = controller.getModel().getQuestion();
            wordLabel.setText(question);

            // Обновляем статистику
            scoreLabel.setText("Очки: " + controller.getModel().getCurrentScore());

            String modeText = controller.getModel().getCurrentMode().equals("en-ru") ?
                    "англ → рус" : "рус → англ";
            modeLabel.setText("Режим: " + modeText);

            // Обновляем прогресс
            int current = controller.getModel().getCurrentWordNumber();
            int total = controller.getModel().getSessionWordsCount();
            progressLabel.setText("Прогресс: " + current + "/" + total);

            // Проверяем завершение сессии
            if (controller.getModel().isSessionComplete()) {
                checkButton.setEnabled(false);
                nextButton.setEnabled(false);
                hintButton.setEnabled(false);
                startButton.setEnabled(true);
            }
        });
    }

    public void showCorrectAnswer() {
        JOptionPane.showMessageDialog(this,
                "Правильно! 👍\n+10 очков",
                "Отлично!",
                JOptionPane.INFORMATION_MESSAGE);
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
        String stats = controller.getStatistics();
        JOptionPane.showMessageDialog(this,
                "Сессия завершена!\n" + stats,
                "Результаты",
                JOptionPane.INFORMATION_MESSAGE);

        // Активируем кнопку начала новой сессии
        startButton.setEnabled(true);
        checkButton.setEnabled(false);
        nextButton.setEnabled(false);
        hintButton.setEnabled(false);
    }

    public void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
}