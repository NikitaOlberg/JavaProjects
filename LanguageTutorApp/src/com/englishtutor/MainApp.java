package com.englishtutor;

import com.englishtutor.controller.TutorController;
import com.englishtutor.model.LanguageModel;
import com.englishtutor.view.SimpleFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainApp {
    public static void main(String[] args) {
        try {
            // Устанавливаем системный стиль
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                LanguageModel model = new LanguageModel();
                TutorController controller = new TutorController(model);
                SimpleFrame frame = new SimpleFrame(controller);
                controller.setView(frame);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "Ошибка запуска приложения: " + e.getMessage(),
                        "Ошибка",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}