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
    private static final int FRAME_WIDTH = 700;
    private static final int FRAME_HEIGHT = 600;
    private static final int WORD_PANEL_HEIGHT = 150;
    private static final int STATS_PANEL_HEIGHT = 250;
    private static final int COMBO_BOX_WIDTH = 150;
    private static final int COMBO_BOX_HEIGHT = 25;
    private static final int SPINNER_WIDTH = 80;
    private static final int START_BUTTON_WIDTH = 150;
    private static final int START_BUTTON_HEIGHT = 35;
    private static final int CONTROL_BUTTON_WIDTH = 130;
    private static final int CONTROL_BUTTON_HEIGHT = 40;
    private static final int ANSWER_FIELD_COLUMNS = 25;
    private static final int WORD_FONT_SIZE = 36;
    private static final int STAT_FONT_SIZE = 16;
    private static final int INFO_FONT_SIZE = 14;
    private static final int BUTTON_FONT_SIZE = 14;
    private static final int BORDER_PADDING = 15;

    private final TutorController controller;

    private JLabel wordLabel;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private JLabel streakLabel;
    private JLabel timerLabel;
    private JLabel categoryLabel;
    private JLabel modeLabel;
    private JTextField answerField;
    private JButton checkButton;
    private JButton nextButton;
    private JButton hintButton;
    private JButton startButton;
    private JButton pauseButton;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> modeCombo;
    private JComboBox<String> gameModeCombo;
    private JSpinner wordCountSpinner;
    private JSpinner timeSpinner;
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JPanel wordPanel;
    private JPanel statsPanel;

    public ExtendedFrame(TutorController controller) {
        this.controller = controller;
        controller.getModel().addGameEventListener(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Английский Тренажер");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);

        createMainInterface();
        setContentPane(mainPanel);
    }

    private void createMainInterface() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING,
                BORDER_PADDING, BORDER_PADDING));

        JPanel setupPanel = createSetupPanel();
        mainPanel.add(setupPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        JPanel wordInfoPanel = createWordInfoPanel();
        centerPanel.add(wordInfoPanel, BorderLayout.NORTH);

        wordPanel = createWordPanel();
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
        panel.setBorder(createTitledBorder("Настройка сессии"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComponentToPanel(panel, gbc, 0, 0, new JLabel("Категория:"));
        categoryCombo = createCategoryComboBox();
        addComponentToPanel(panel, gbc, 1, 0, categoryCombo);

        addComponentToPanel(panel, gbc, 2, 0, new JLabel("Направление:"));
        modeCombo = createModeComboBox();
        addComponentToPanel(panel, gbc, 3, 0, modeCombo);

        addComponentToPanel(panel, gbc, 0, 1, new JLabel("Режим игры:"));
        gameModeCombo = createGameModeComboBox();
        addComponentToPanel(panel, gbc, 1, 1, gameModeCombo);

        addComponentToPanel(panel, gbc, 2, 1, new JLabel("Количество слов:"));
        wordCountSpinner = createWordCountSpinner();
        addComponentToPanel(panel, gbc, 3, 1, wordCountSpinner);

        addComponentToPanel(panel, gbc, 4, 1, new JLabel("Время (сек):"));
        timeSpinner = createTimeSpinner();
        addComponentToPanel(panel, gbc, 5, 1, timeSpinner);

        return panel;
    }

    private JComboBox<String> createCategoryComboBox() {
        JComboBox<String> combo = new JComboBox<>(
                controller.getModel().getCategories().toArray(new String[0]));
        combo.setPreferredSize(new Dimension(COMBO_BOX_WIDTH, COMBO_BOX_HEIGHT));
        return combo;
    }

    private JComboBox<String> createModeComboBox() {
        String[] modes = {
                controller.getModel().getModeDescription("en-ru"),
                controller.getModel().getModeDescription("ru-en"),
                controller.getModel().getModeDescription("mixed")
        };
        JComboBox<String> combo = new JComboBox<>(modes);
        combo.setPreferredSize(new Dimension(180, COMBO_BOX_HEIGHT));
        return combo;
    }

    private JComboBox<String> createGameModeComboBox() {
        String[] gameModes = {
                controller.getModel().getModeDescription("standard"),
                controller.getModel().getModeDescription("review"),
                controller.getModel().getModeDescription("timed"),
                controller.getModel().getModeDescription("challenge")
        };
        JComboBox<String> combo = new JComboBox<>(gameModes);
        combo.setPreferredSize(new Dimension(COMBO_BOX_WIDTH, COMBO_BOX_HEIGHT));
        return combo;
    }

    private JSpinner createWordCountSpinner() {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(10, 5, 50, 5);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setPreferredSize(new Dimension(SPINNER_WIDTH, COMBO_BOX_HEIGHT));
        return spinner;
    }

    private JSpinner createTimeSpinner() {
        SpinnerNumberModel timeModel = new SpinnerNumberModel(60, 30, 300, 30);
        JSpinner spinner = new JSpinner(timeModel);
        spinner.setPreferredSize(new Dimension(SPINNER_WIDTH, COMBO_BOX_HEIGHT));
        return spinner;
    }

    private void addComponentToPanel(JPanel panel, GridBagConstraints gbc,
                                     int x, int y, Component component) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(component, gbc);
    }

    private JPanel createWordInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(createTitledBorder("Информация о слове"));

        categoryLabel = createInfoLabel("Категория: -");
        modeLabel = createInfoLabel("Направление перевода: -");

        panel.add(categoryLabel);
        panel.add(modeLabel);

        startButton = createStartButton();
        panel.add(startButton);

        return panel;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, INFO_FONT_SIZE));
        return label;
    }

    private JButton createStartButton() {
        JButton button = createStyledButton("Начать сессию", new Color(0, 150, 0));
        button.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        button.setPreferredSize(new Dimension(START_BUTTON_WIDTH, START_BUTTON_HEIGHT));
        button.addActionListener(e -> startNewSession());
        return button;
    }

    private JPanel createWordPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Слово для перевода"));
        panel.setPreferredSize(new Dimension(400, WORD_PANEL_HEIGHT));

        wordLabel = createWordLabel();
        panel.add(wordLabel, BorderLayout.CENTER);

        panel.addMouseListener(new WordClickListener());

        return panel;
    }

    private JLabel createWordLabel() {
        JLabel label = new JLabel("Начните новую сессию", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, WORD_FONT_SIZE));
        label.setForeground(new Color(0, 0, 150));
        return label;
    }

    private class WordClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (controller.getModel().getCurrentWord() != null) {
                showWordHint();
            }
        }
    }

    private void showWordHint() {
        Word word = controller.getModel().getCurrentWord();
        String message = String.format("Английский: %s%nРусский: %s",
                word.getEnglish(), word.getRussian());
        JOptionPane.showMessageDialog(this, message, "Подсказка",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(createTitledBorder("Статистика"));
        panel.setPreferredSize(new Dimension(250, STATS_PANEL_HEIGHT));

        scoreLabel = createStatLabel("Очки: 0");
        levelLabel = createStatLabel("Уровень: 1");
        streakLabel = createStatLabel("Серия: 0");
        timerLabel = createStatLabel("Время: -");

        panel.add(scoreLabel);
        panel.add(levelLabel);
        panel.add(streakLabel);
        panel.add(timerLabel);
        panel.add(createPauseButtonPanel());

        return panel;
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, STAT_FONT_SIZE));
        label.setForeground(new Color(0, 100, 0));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel createPauseButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pauseButton = createPauseButton();
        panel.add(pauseButton);
        return panel;
    }

    private JButton createPauseButton() {
        JButton button = createStyledButton("Пауза", Color.ORANGE);
        button.setEnabled(false);
        button.addActionListener(e -> togglePause());
        button.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        button.setPreferredSize(new Dimension(100, START_BUTTON_HEIGHT));
        return button;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBorder(createTitledBorder("Управление"));

        answerField = createAnswerField();
        checkButton = createCheckButton();
        nextButton = createNextButton();
        hintButton = createHintButton();

        panel.add(new JLabel("Ваш ответ:"));
        panel.add(answerField);
        panel.add(checkButton);
        panel.add(nextButton);
        panel.add(hintButton);

        return panel;
    }

    private JTextField createAnswerField() {
        JTextField field = new JTextField(ANSWER_FIELD_COLUMNS);
        field.setFont(new Font("Arial", Font.PLAIN, STAT_FONT_SIZE));
        field.addKeyListener(new EnterKeyListener());
        return field;
    }

    private class EnterKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                checkAnswer();
            }
        }
    }

    private JButton createCheckButton() {
        JButton button = createStyledButton("Проверить", Color.GREEN);
        button.setPreferredSize(new Dimension(CONTROL_BUTTON_WIDTH, CONTROL_BUTTON_HEIGHT));
        button.addActionListener(e -> checkAnswer());
        button.setEnabled(false);
        return button;
    }

    private JButton createNextButton() {
        JButton button = createStyledButton("Следующее", Color.BLUE);
        button.setPreferredSize(new Dimension(CONTROL_BUTTON_WIDTH, CONTROL_BUTTON_HEIGHT));
        button.addActionListener(e -> nextWord());
        button.setEnabled(false);
        return button;
    }

    private JButton createHintButton() {
        JButton button = createStyledButton("Подсказка", Color.ORANGE);
        button.setPreferredSize(new Dimension(CONTROL_BUTTON_WIDTH, CONTROL_BUTTON_HEIGHT));
        button.addActionListener(e -> controller.showHint());
        button.setEnabled(false);
        return button;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEtchedBorder());
        return button;
    }

    private TitledBorder createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), title,
                TitledBorder.CENTER, TitledBorder.TOP);
    }

    private void startNewSession() {
        SessionParameters params = collectSessionParameters();
        controller.startNewSession(params.category, params.mode,
                params.gameMode, params.wordCount, params.timeLimit);

        updateUIForSessionStart(params.gameMode);
        answerField.requestFocus();
        updateUI();
    }

    private SessionParameters collectSessionParameters() {
        String category = (String) categoryCombo.getSelectedItem();
        String mode = ModeConverter.getModeKey((String) modeCombo.getSelectedItem());
        String gameMode = ModeConverter.getGameModeKey((String) gameModeCombo.getSelectedItem());
        int wordCount = (int) wordCountSpinner.getValue();
        int timeLimit = (int) timeSpinner.getValue();

        return new SessionParameters(category, mode, gameMode, wordCount, timeLimit);
    }

    private static class SessionParameters {
        final String category;
        final String mode;
        final String gameMode;
        final int wordCount;
        final int timeLimit;

        SessionParameters(String category, String mode, String gameMode,
                          int wordCount, int timeLimit) {
            this.category = category;
            this.mode = mode;
            this.gameMode = gameMode;
            this.wordCount = wordCount;
            this.timeLimit = timeLimit;
        }
    }

    private void updateUIForSessionStart(String gameMode) {
        startButton.setEnabled(false);
        checkButton.setEnabled(true);
        nextButton.setEnabled(true);
        hintButton.setEnabled(true);
        pauseButton.setEnabled("timed".equals(gameMode));
    }

    private static class ModeConverter {
        static String getModeKey(String description) {
            if (description.contains("→")) {
                if (description.contains("Английский")) return "en-ru";
                if (description.contains("Русский")) return "ru-en";
            }
            return "mixed";
        }

        static String getGameModeKey(String description) {
            if (description.contains("Стандартный")) return "standard";
            if (description.contains("Повторение")) return "review";
            if (description.contains("На время")) return "timed";
            if (description.contains("Вызов")) return "challenge";
            return "standard";
        }
    }

    private void checkAnswer() {
        controller.checkAnswer(answerField.getText());
        answerField.setText("");
        answerField.requestFocus();
    }

    private void nextWord() {
        controller.nextWord();
        updateUI();
    }

    public void updateUI() {
        SwingUtilities.invokeLater(() -> {
            updateWordDisplay();
            updateStatistics();
            updateSessionStatus();
        });
    }

    private void updateWordDisplay() {
        String question = controller.getModel().getQuestion();
        wordLabel.setText(question);

        Word word = controller.getModel().getCurrentWord();
        if (word != null) {
            categoryLabel.setText("Категория: " + word.getCategory());
            modeLabel.setText("Направление перевода: " +
                    controller.getModel().getModeDescription(controller.getModel().getCurrentMode()));
        }
    }

    private void updateStatistics() {
        scoreLabel.setText("Очки: " + controller.getModel().getCurrentScore());
        levelLabel.setText("Уровень: " + controller.getModel().getLevel() +
                " (" + controller.getModel().getExperience() + "/" +
                controller.getModel().getExperienceForNextLevel() + ")");
        streakLabel.setText("Серия: " + controller.getModel().getCurrentStreak());
        updateTimer();
    }

    private void updateSessionStatus() {
        if (controller.getModel().isSessionComplete()) {
            endSession();
        }
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
            handleGameEvent(event);
            updateUI();
        });
    }

    private void handleGameEvent(GameEvent event) {
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
    }

    private void showCorrectAnswer(int points) {
        Word word = controller.getModel().getCurrentWord();
        if (word != null) {
            String message = createCorrectAnswerMessage(points);
            JOptionPane.showMessageDialog(this, message, "Отлично!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String createCorrectAnswerMessage(int points) {
        StringBuilder message = new StringBuilder(String.format("Правильно! +%d очков", points));

        if (controller.getModel().getCurrentStreak() >= 3) {
            message.append(String.format(" (серия: %d, бонус: +%d)",
                    controller.getModel().getCurrentStreak(),
                    controller.getModel().getCurrentStreak() * 5));
        }

        return message.toString();
    }

    private void showWrongAnswer(String correctAnswer) {
        JOptionPane.showMessageDialog(this,
                "Неправильно! 😕\nПравильный ответ: " + correctAnswer,
                "Попробуйте еще",
                JOptionPane.WARNING_MESSAGE);
    }
}