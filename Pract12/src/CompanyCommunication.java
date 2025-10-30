import java.util.ArrayList;
import java.util.List;


interface CompanyMediator {
    void sendMessage(String message, Department department);
    void addDepartment(Department department);
    void setDepartments(List<Department> departments);
}

class CentralCommunicationMediator implements CompanyMediator {
    private List<Department> departments;
    private List<String> messageHistory;

    public CentralCommunicationMediator() {
        this.departments = new ArrayList<>();
        this.messageHistory = new ArrayList<>();
    }

    @Override
    public void sendMessage(String message, Department department) {
        String fullMessage = String.format("[%s]: %s", department.getName(), message);
        messageHistory.add(fullMessage);

        System.out.println("\n=== Централизованное сообщение ===");
        System.out.println(fullMessage);
        System.out.println("-----------------------------------");

        for (Department dept : departments) {
            if (dept != department) {
                dept.receiveMessage(fullMessage);
            }
        }
        System.out.println("===================================\n");
    }

    @Override
    public void addDepartment(Department department) {
        departments.add(department);
        department.setMediator(this);
    }

    @Override
    public void setDepartments(List<Department> departments) {
        this.departments = departments;
        for (Department dept : departments) {
            dept.setMediator(this);
        }
    }

    public void showMessageHistory() {
        System.out.println("\n=== История сообщений ===");
        for (String message : messageHistory) {
            System.out.println(message);
        }
        System.out.println("=========================\n");
    }
}

abstract class Department {
    protected CompanyMediator mediator;
    protected String name;

    public Department(String name) {
        this.name = name;
    }

    public void setMediator(CompanyMediator mediator) {
        this.mediator = mediator;
    }

    public String getName() {
        return name;
    }

    public abstract void sendMessage(String message);
    public abstract void receiveMessage(String message);
}

class HRDepartment extends Department {
    public HRDepartment() {
        super("HR - Отдел кадров");
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(name + " отправляет сообщение...");
        mediator.sendMessage(message, this);
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println(name + " получил сообщение: " + message);

        if (message.toLowerCase().contains("новый сотрудник")) {
            System.out.println("HR: Начинаем процесс онбординга нового сотрудника!");
        } else if (message.toLowerCase().contains("отпуск")) {
            System.out.println("HR: Обрабатываем заявление на отпуск...");
        }
    }
}

class AccountingDepartment extends Department {
    public AccountingDepartment() {
        super("Бухгалтерия");
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(name + " отправляет сообщение...");
        mediator.sendMessage(message, this);
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println(name + " получил сообщение: " + message);

        if (message.toLowerCase().contains("зарплата")) {
            System.out.println("Бухгалтерия: Проверяем расчет заработной платы...");
        } else if (message.toLowerCase().contains("отчет")) {
            System.out.println("Бухгалтерия: Готовим финансовый отчет...");
        } else if (message.toLowerCase().contains("налоги")) {
            System.out.println("Бухгалтерия: Обрабатываем налоговые документы...");
        }
    }
}

class ITDepartment extends Department {
    public ITDepartment() {
        super("IT - Технический отдел");
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(name + " отправляет сообщение...");
        mediator.sendMessage(message, this);
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println(name + " получил сообщение: " + message);

        if (message.toLowerCase().contains("сбой") || message.toLowerCase().contains("проблема")) {
            System.out.println("IT: Отправляем техника для решения проблемы!");
        } else if (message.toLowerCase().contains("обновление")) {
            System.out.println("IT: Начинаем процесс обновления системы...");
        } else if (message.toLowerCase().contains("новый сотрудник")) {
            System.out.println("IT: Подготавливаем рабочее место и учетные записи...");
        }
    }
}

public class CompanyCommunication {
    public static void main(String[] args) {
        CentralCommunicationMediator mediator = new CentralCommunicationMediator();

        Department hr = new HRDepartment();
        Department accounting = new AccountingDepartment();
        Department it = new ITDepartment();

        mediator.addDepartment(hr);
        mediator.addDepartment(accounting);
        mediator.addDepartment(it);

        System.out.println("=== СИСТЕМА КОРПОРАТИВНОЙ КОММУНИКАЦИИ ===\n");

        hr.sendMessage("В понедельник к нам присоединяется новый сотрудник. Пожалуйста, подготовьте все необходимое.");

        accounting.sendMessage("Напоминаем о сроках сдачи финансовых отчетов за текущий квартал.");

        it.sendMessage("Запланировано техническое обновление системы в субботу с 22:00 до 02:00.");

        hr.sendMessage("Сотрудник Иванов подает заявление на отпуск с 15 по 30 число.");

        accounting.sendMessage("Зарплата будет выплачена 25 числа, как и обычно.");

        it.sendMessage("Обнаружен сбой в сети на 3 этаже. Работаем над решением проблемы.");

        mediator.showMessageHistory();
    }
}