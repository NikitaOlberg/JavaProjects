import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LanguageModel model = new LanguageModel();

                    TutorController controller = new TutorController(model);

                    MainFrame mainFrame = new MainFrame(controller);

                    controller.setView(mainFrame);

                    mainFrame.setVisible(true);

                    controller.startNewSession();

                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorDialog("Ошибка запуска приложения: " + e.getMessage());
                }
            }

            private void showErrorDialog(String message) {
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        message,
                        "Ошибка",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}