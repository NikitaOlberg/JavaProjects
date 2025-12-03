package com.englishtutor;

import com.englishtutor.controller.TutorController;
import com.englishtutor.model.LanguageModel;
import com.englishtutor.view.ExtendedFrame;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                LanguageModel model = new LanguageModel();
                TutorController controller = new TutorController(model);
                ExtendedFrame frame = new ExtendedFrame(controller);
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