package com.englishtutor.view;
import com.englishtutor.controller.TutorController;
import com.englishtutor.events.GameEvent;
import com.englishtutor.events.GameEventListener;
import com.englishtutor.model.Word;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class ExtendedFrame extends JFrame implements GameEventListener {
    private final TutorController controller;

    private JLabel wordLabel, scoreLabel, levelLabel, streakLabel, timerLabel;
    private JLabel categoryLabel, modeLabel;
    private JTextField answerField;
    private JButton checkButton, nextButton, hintButton, startButton, pauseButton;
    private JComboBox<String> categoryCombo, modeCombo, gameModeCombo;
    private JSpinner wordCountSpinner, timeSpinner;
    private JPanel mainPanel, controlPanel, wordPanel, statsPanel;

    public ExtendedFrame(TutorController controller) {
        this.controller = controller;
        controller.getModel().addGameEventListener(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Английский Тренажер");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        createMainInterface();

        setContentPane(mainPanel);
    }

    private void createMainInterface() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel setupPanel = createSetupPanel();
        mainPanel.add(setupPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        JPanel wordInfoPanel = createWordInfoPanel();
        centerPanel.add(wordInfoPanel, BorderLayout.NORTH);

        wordPanel = new JPanel(new BorderLayout());
        wordPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Слово для перевода",
                TitledBorder.CENTER, TitledBorder.TOP));
        wordPanel.setPreferredSize(new Dimension(400, 150));

        wordLabel = new JLabel("Начните новую сессию", JLabel.CENTER);
        wordLabel.setFont(new Font("Arial", Font.BOLD, 36));
        wordLabel.setForeground(new Color(0, 0, 150));

        wordPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller.getModel().getCurrentWord() != null) {
                    String word = controller.getModel().getCurrentWord().getEnglish();
                    String translation = controller.getModel().getCurrentWord().getRussian();
                    JOptionPane.showMessageDialog(ExtendedFrame.this,
                            "Английский: " + word + "\nРусский: " + translation,
                            "Подсказка", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        wordPanel.add(wordLabel, BorderLayout.CENTER);

        centerPanel.add(wordPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        statsPanel = createStatsPanel();
        rightPanel.add(statsPanel, BorderLayout.NORTH);

        centerPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createSetupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Настройка сессии",
                TitledBorder.CENTER, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Категория:"), gbc);

        gbc.gridx = 1;
        categoryCombo = new JComboBox<>(controller.getModel().getCategories().toArray(new String[0]));
        categoryCombo.setPreferredSize(new Dimension(150, 25));
        panel.add(categoryCombo, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Направление:"), gbc);

        gbc.gridx = 3;
        modeCombo = new JComboBox<>(new String[]{
                controller.getModel().getModeDescription("en-ru"),
                controller.getModel().getModeDescription("ru-en"),
                controller.getModel().getModeDescription("mixed")
        });
        modeCombo.setPreferredSize(new Dimension(180, 25));
        panel.add(modeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Режим игры:"), gbc);

        gbc.gridx = 1;
        gameModeCombo = new JComboBox<>(new String[]{
                controller.getModel().getModeDescription("standard"),
                controller.getModel().getModeDescription("review"),
                controller.getModel().getModeDescription("timed"),
                controller.getModel().getModeDescription("challenge")
        });
        gameModeCombo.setPreferredSize(new Dimension(150, 25));
        panel.add(gameModeCombo, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Количество слов:"), gbc);

        gbc.gridx = 3;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(10, 5, 50, 5);
        wordCountSpinner = new JSpinner(spinnerModel);
        wordCountSpinner.setPreferredSize(new Dimension(80, 25));
        panel.add(wordCountSpinner, gbc);

        gbc.gridx = 4;
        panel.add(new JLabel("Время (сек):"), gbc);

        gbc.gridx = 5;
        SpinnerNumberModel timeModel = new SpinnerNumberModel(60, 30, 300, 30);
        timeSpinner = new JSpinner(timeModel);
        timeSpinner.setPreferredSize(new Dimension(80, 25));
        panel.add(timeSpinner, gbc);

        return panel;
    }

    private JPanel createWordInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Информация о слове",
                TitledBorder.CENTER, TitledBorder.TOP));

        categoryLabel = new JLabel("Категория: -");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        modeLabel = new JLabel("Направление перевода: -");
        modeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(categoryLabel);
        panel.add(modeLabel);

        startButton = createStyledButton("Начать сессию", new Color(0, 150, 0));
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setPreferredSize(new Dimension(150, 35));
        startButton.addActionListener(e -> startNewSession());

        panel.add(startButton);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Статистика",
                TitledBorder.CENTER, TitledBorder.TOP));
        panel.setPreferredSize(new Dimension(250, 250));

        scoreLabel = createStatLabel("Очки: 0");
        levelLabel = createStatLabel("Уровень: 1");
        streakLabel = createStatLabel("Серия: 0");
        timerLabel = createStatLabel("Время: -");

        panel.add(scoreLabel);
        panel.add(levelLabel);
        panel.add(streakLabel);
        panel.add(timerLabel);

        pauseButton = createStyledButton("Пауза", Color.ORANGE);
        pauseButton.setEnabled(false);
        pauseButton.addActionListener(e -> togglePause());
        pauseButton.setFont(new Font("Arial", Font.BOLD, 14));
        pauseButton.setPreferredSize(new Dimension(100, 35));

        JPanel pausePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pausePanel.add(pauseButton);
        panel.add(pausePanel);

        return panel;
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(0, 100, 0));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Управление",
                TitledBorder.CENTER, TitledBorder.TOP));

        answerField = new JTextField(25);
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

        checkButton = createStyledButton("Проверить", Color.GREEN);
        nextButton = createStyledButton("Следующее", Color.BLUE);
        hintButton = createStyledButton("Подсказка", Color.ORANGE);

        Dimension buttonSize = new Dimension(130, 40);
        checkButton.setPreferredSize(buttonSize);
        nextButton.setPreferredSize(buttonSize);
        hintButton.setPreferredSize(buttonSize);

        nextButton.addActionListener(e -> {
            controller.nextWord();
            updateUI();
        });

        checkButton.addActionListener(e -> {
            controller.checkAnswer(answerField.getText());
            answerField.setText("");
            answerField.requestFocus();
        });

        hintButton.addActionListener(e -> controller.showHint());

        checkButton.setEnabled(false);
        nextButton.setEnabled(false);
        hintButton.setEnabled(false);

        panel.add(new JLabel("Ваш ответ:"));
        panel.add(answerField);
        panel.add(checkButton);
        panel.add(nextButton);
        panel.add(hintButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEtchedBorder());

        return button;
    }

    private void startNewSession() {
        String category = (String) categoryCombo.getSelectedItem();
        String mode = getModeKey((String) modeCombo.getSelectedItem());
        String gameMode = getGameModeKey((String) gameModeCombo.getSelectedItem());
        int wordCount = (int) wordCountSpinner.getValue();
        int timeLimit = (int) timeSpinner.getValue();

        controller.startNewSession(category, mode, gameMode, wordCount, timeLimit);

        startButton.setEnabled(false);
        checkButton.setEnabled(true);
        nextButton.setEnabled(true);
        hintButton.setEnabled(true);
        pauseButton.setEnabled("timed".equals(gameMode));

        answerField.requestFocus();
        updateUI();
    }

    private String getModeKey(String description) {
        if (description.contains("→")) {
            if (description.contains("Английский")) return "en-ru";
            if (description.contains("Русский")) return "ru-en";
        }
        return "mixed";
    }

    private String getGameModeKey(String description) {
        if (description.contains("Стандартный")) return "standard";
        if (description.contains("Повторение")) return "review";
        if (description.contains("На время")) return "timed";
        if (description.contains("Вызов")) return "challenge";
        return "standard";
    }

    public void updateUI() {
        SwingUtilities.invokeLater(() -> {
            String question = controller.getModel().getQuestion();
            wordLabel.setText(question);

            if (controller.getModel().getCurrentWord() != null) {
                Word word = controller.getModel().getCurrentWord();
                categoryLabel.setText("Категория: " + word.getCategory());
                modeLabel.setText("Направление перевода: " +
                        controller.getModel().getModeDescription(controller.getModel().getCurrentMode()));
            }

            scoreLabel.setText("Очки: " + controller.getModel().getCurrentScore());
            levelLabel.setText("Уровень: " + controller.getModel().getLevel() +
                    " (" + controller.getModel().getExperience() + "/" +
                    controller.getModel().getExperienceForNextLevel() + ")");
            streakLabel.setText("Серия: " + controller.getModel().getCurrentStreak());

            updateTimer();

            if (controller.getModel().isSessionComplete()) {
                endSession();
            }
        });
    }

    public void updateTimer() {
        SwingUtilities.invokeLater(() -> {
            if ("timed".equals(controller.getModel().getCurrentGameMode())) {
                int timeLeft = controller.getModel().getTimeLeft();
                timerLabel.setText("Время: " + timeLeft + "с");
            } else {
                timerLabel.setText("Время: -");
            }
        });
    }

    private void togglePause() {
        if (controller.isTimerRunning()) {
            controller.pauseTimer();
            pauseButton.setText("Продолжить");
        } else {
            controller.resumeTimer();
            pauseButton.setText("Пауза");
        }
    }

    private void endSession() {
        startButton.setEnabled(true);
        checkButton.setEnabled(false);
        nextButton.setEnabled(false);
        hintButton.setEnabled(false);
        pauseButton.setEnabled(false);
        wordLabel.setText("Сессия завершена");
    }

    @Override
    public void onGameEvent(GameEvent event) {
        SwingUtilities.invokeLater(() -> {
            switch (event.getEventType()) {
                case "CORRECT_ANSWER":
                    showCorrectAnswer((Integer) event.getData());
                    break;
                case "WRONG_ANSWER":
                    showWrongAnswer((String) event.getData());
                    break;
                case "TIME_UP":
                    endSession();
                    break;
            }
            updateUI();
        });
    }

    private void showCorrectAnswer(int points) {
        Word word = controller.getModel().getCurrentWord();
        if (word != null) {
            String message = String.format("Правильно! +%d очков", points);

            if (controller.getModel().getCurrentStreak() >= 3) {
                message += String.format(" (серия: %d, бонус: +%d)",
                        controller.getModel().getCurrentStreak(),
                        controller.getModel().getCurrentStreak() * 5);
            }

            JOptionPane.showMessageDialog(this, message, "Отлично!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showWrongAnswer(String correctAnswer) {
        JOptionPane.showMessageDialog(this,
                "Неправильно! 😕\nПравильный ответ: " + correctAnswer,
                "Попробуйте еще",
                JOptionPane.WARNING_MESSAGE);
    }
}