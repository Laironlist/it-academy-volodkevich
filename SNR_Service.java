// Импортируем необходимые классы
import java.util.ArrayList;      // Для работы со списками
import java.util.HashMap;        // Для хранения данных в виде ключ-значение
import java.util.Scanner;        // Для ввода данных
import java.time.LocalDate;      // Для работы с датами
import java.time.format.DateTimeFormatter; // Для форматирования дат и времени

public class SNR_Service {

    // Основной метод программы
    public static void main(String[] args) {
        // Создаем объект Scanner для ввода данных
        Scanner scanner = new Scanner(System.in);

        // Создаем объект нашего автосервиса
        AutoService autoService = new AutoService();

        // Переменная для хранения выбора пользователя
        int choice;

        // Главный цикл программы
        do {
            // Выводим меню
            System.out.println("\n===== SNR-Сервис =====");
            System.out.println("1. Просмотреть услуги и цены");
            System.out.println("2. Записаться на обслуживание");
            System.out.println("3. Просмотреть мои записи");
            System.out.println("4. Отменить запись");
            System.out.println("5. Информация о сервисе");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            // Считываем выбор пользователя
            choice = scanner.nextInt();
            scanner.nextLine(); // Очищаем буфер после nextInt()

            // Обрабатываем выбор пользователя
            switch (choice) {
                case 1:
                    autoService.showServices();
                    break;
                case 2:
                    autoService.makeAppointment(scanner);
                    break;
                case 3:
                    autoService.showAppointments();
                    break;
                case 4:
                    autoService.cancelAppointment(scanner);
                    break;
                case 5:
                    autoService.showInfo();
                    break;
                case 0:
                    System.out.println("Спасибо за использование нашего приложения!");
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }

        } while (choice != 0); // Продолжаем пока пользователь не выберет 0

        scanner.close(); // Закрываем Scanner
    }
}

// Класс, представляющий автосервис
class AutoService {

    // Хранилище услуг: ключ - название услуги, значение - цена
    private HashMap<String, Double> services;

    // Список доступных временных слотов
    private ArrayList<String> availableTimeSlots;

    // Список записей клиентов
    private ArrayList<Appointment> appointments;

    // Конструктор класса (вызывается при создании объекта)
    public AutoService() {
        // Инициализируем все коллекции
        services = new HashMap<>();
        availableTimeSlots = new ArrayList<>();
        appointments = new ArrayList<>();

        // Добавляем услуги и цены
        initializeServices();

        // Добавляем доступные временные слоты
        initializeTimeSlots();
    }

    // Метод для инициализации услуг
    private void initializeServices() {
        services.put("Замена масла", 2000.0);
        services.put("Замена тормозных колодок", 5000.0);
        services.put("Замена аккумулятора", 3000.0);
        services.put("Шиномонтаж (4 колеса)", 4000.0);
        services.put("Диагностика двигателя", 3500.0);
        services.put("Развал-схождение", 4500.0);
        services.put("Замена фильтров (воздушный + салонный)", 2500.0);
        services.put("Компьютерная диагностика", 3000.0);
        services.put("Мойка автомобиля", 1500.0);
        services.put("Полировка кузова", 8000.0);
    }

    // Метод для инициализации временных слотов
    private void initializeTimeSlots() {
        // Рабочие часы: с 9:00 до 18:00, перерыв 13:00-14:00
        for (int hour = 9; hour < 18; hour++) {
            if (hour != 13) { // Пропускаем время обеда
                availableTimeSlots.add(String.format("%02d:00", hour));
                availableTimeSlots.add(String.format("%02d:30", hour));
            }
        }
    }

    // Метод для отображения всех услуг
    public void showServices() {
        System.out.println("\n=== УСЛУГИ И ЦЕНЫ ===");
        System.out.println("№  | Услуга                          | Цена (руб.)");
        System.out.println("---|---------------------------------|------------");

        int counter = 1;
        // Проходим по всем услугам
        for (String service : services.keySet()) {
            // %-30s - выравнивание строки по левому краю на 30 символов
            // %8.2f - форматирование числа (8 символов, 2 знака после запятой)
            System.out.printf("%-3d| %-30s | %,8.2f%n",
                    counter++,
                    service,
                    services.get(service));
        }

        System.out.println("\nПримечание: Цены могут меняться в зависимости от модели автомобиля");
    }

    // Метод для создания записи
    public void makeAppointment(Scanner scanner) {
        System.out.println("\n=== ЗАПИСЬ НА ОБСЛУЖИВАНИЕ ===");

        // Запрашиваем имя клиента
        System.out.print("Введите ваше имя: ");
        String name = scanner.nextLine();

        // Запрашиваем номер телефона
        System.out.print("Введите ваш телефон: ");
        String phone = scanner.nextLine();

        // Запрашиваем марку и модель автомобиля
        System.out.print("Введите марку и модель автомобиля: ");
        String carModel = scanner.nextLine();

        // Показываем доступные услуги
        showServices();

        // Выбираем услугу
        System.out.print("\nВведите номер услуги: ");
        int serviceChoice = scanner.nextInt();
        scanner.nextLine(); // Очищаем буфер

        // Получаем выбранную услугу
        String selectedService = getServiceByNumber(serviceChoice);
        if (selectedService == null) {
            System.out.println("Неверный номер услуги!");
            return;
        }

        // Показываем доступные даты
        System.out.println("\n=== ДОСТУПНЫЕ ДАТЫ ===");
        LocalDate today = LocalDate.now();
        for (int i = 1; i <= 7; i++) { // На 7 дней вперед
            LocalDate date = today.plusDays(i);
            // Форматируем дату в понятный вид
            System.out.println(i + ". " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        // Выбираем дату
        System.out.print("Выберите номер даты (1-7): ");
        int dayChoice = scanner.nextInt();
        scanner.nextLine(); // Очищаем буфер

        if (dayChoice < 1 || dayChoice > 7) {
            System.out.println("Неверный выбор даты!");
            return;
        }

        LocalDate selectedDate = today.plusDays(dayChoice);

        // Показываем доступное время
        System.out.println("\n=== ДОСТУПНОЕ ВРЕМЯ ===");
        for (int i = 0; i < availableTimeSlots.size(); i++) {
            System.out.println((i + 1) + ". " + availableTimeSlots.get(i));
        }

        // Выбираем время
        System.out.print("Выберите номер времени: ");
        int timeChoice = scanner.nextInt();
        scanner.nextLine(); // Очищаем буфер

        if (timeChoice < 1 || timeChoice > availableTimeSlots.size()) {
            System.out.println("Неверный выбор времени!");
            return;
        }

        String selectedTime = availableTimeSlots.get(timeChoice - 1);

        // Создаем новую запись
        Appointment appointment = new Appointment(
                name,
                phone,
                carModel,
                selectedService,
                selectedDate,
                selectedTime,
                services.get(selectedService)
        );

        // Добавляем запись в список
        appointments.add(appointment);

        // Удаляем выбранное время из доступных слотов
        availableTimeSlots.remove(timeChoice - 1);

        System.out.println("\n=== ЗАПИСЬ УСПЕШНО СОЗДАНА ===");
        System.out.println("Номер записи: " + appointment.getId());
        System.out.println("Клиент: " + name);
        System.out.println("Услуга: " + selectedService);
        System.out.println("Дата: " + selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        System.out.println("Время: " + selectedTime);
        System.out.println("Стоимость: " + services.get(selectedService) + " руб.");
        System.out.println("\nЖдем вас в нашем сервисе!");
    }

    // Метод для получения услуги по номеру
    private String getServiceByNumber(int number) {
        // Преобразуем HashMap в ArrayList для доступа по индексу
        ArrayList<String> serviceList = new ArrayList<>(services.keySet());
        if (number > 0 && number <= serviceList.size()) {
            return serviceList.get(number - 1);
        }
        return null;
    }

    // Метод для отображения всех записей
    public void showAppointments() {
        System.out.println("\n=== ВАШИ ЗАПИСИ ===");

        if (appointments.isEmpty()) {
            System.out.println("У вас нет активных записей.");
            return;
        }

        // Выводим все записи
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
            System.out.println("------------------------");
        }
    }

    // Метод для отмены записи
    public void cancelAppointment(Scanner scanner) {
        System.out.println("\n=== ОТМЕНА ЗАПИСИ ===");

        if (appointments.isEmpty()) {
            System.out.println("Нет активных записей для отмены.");
            return;
        }

        // Показываем все записи с номерами
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println((i + 1) + ". " + appointments.get(i).getShortInfo());
        }

        System.out.print("Введите номер записи для отмены: ");
        int appointmentNumber = scanner.nextInt();
        scanner.nextLine(); // Очищаем буфер

        if (appointmentNumber > 0 && appointmentNumber <= appointments.size()) {
            // Получаем отменяемую запись
            Appointment canceledAppointment = appointments.get(appointmentNumber - 1);

            // Возвращаем время в список доступных
            availableTimeSlots.add(canceledAppointment.getTime());

            // Удаляем запись
            appointments.remove(appointmentNumber - 1);

            System.out.println("Запись успешно отменена!");
        } else {
            System.out.println("Неверный номер записи!");
        }
    }

    // Метод для отображения информации о сервисе
    public void showInfo() {
        System.out.println("\n=== ИНФОРМАЦИЯ О СЕРВИСЕ ===");
        System.out.println("Название:'Snr Service'");
        System.out.println("Адрес: г. Казань, ул. Михаила Миля, д. 49, к. 1");
        System.out.println("Телефон: +7 (843) 258-22-78");
        System.out.println("Часы работы:");
        System.out.println("  Ежедневно: 8:30 - 20:00");
        System.out.println("\nУслуги:");
        System.out.println("  • Диагностика и ремонт всех систем автомобиля");
        System.out.println("  • Техническое обслуживание");
        System.out.println("  • Кузовной ремонт и покраска");
        System.out.println("  • Шиномонтаж и балансировка");
        System.out.println("  • Продажа автозапчастей");
        System.out.println("\nГарантия на все виды работ - 1 год!");
    }
}

// Класс для представления записи на обслуживание
class Appointment {
    // Статическая переменная для генерации уникальных ID
    private static int nextId = 1;

    // Поля класса
    private int id;                 // Уникальный номер записи
    private String customerName;    // Имя клиента
    private String phone;           // Телефон клиента
    private String carModel;        // Марка и модель авто
    private String service;         // Услуга
    private LocalDate date;         // Дата записи
    private String time;            // Время записи
    private double price;           // Стоимость

    // Конструктор
    public Appointment(String customerName, String phone, String carModel,
                       String service, LocalDate date, String time, double price) {
        this.id = nextId++;         // Присваиваем уникальный ID
        this.customerName = customerName;
        this.phone = phone;
        this.carModel = carModel;
        this.service = service;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    // Геттеры (методы для получения значений полей)
    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getCarModel() { return carModel; }
    public String getService() { return service; }
    public LocalDate getDate() { return date; }
    public String getTime() { return time; }
    public double getPrice() { return price; }

    // Метод для краткого отображения информации
    public String getShortInfo() {
        return String.format("Запись №%d: %s - %s (%s)",
                id, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), time, service);
    }

    // Метод для полного отображения информации (переопределяем toString())
    @Override
    public String toString() {
        return String.format(
                "Запись №%d\n" +
                        "Клиент: %s\n" +
                        "Телефон: %s\n" +
                        "Автомобиль: %s\n" +
                        "Услуга: %s\n" +
                        "Дата: %s\n" +
                        "Время: %s\n" +
                        "Стоимость: %.2f руб.",
                id, customerName, phone, carModel, service,
                date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                time, price
        );
    }
}