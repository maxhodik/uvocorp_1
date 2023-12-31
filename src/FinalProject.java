import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class FinalProject {
    private static ArrayList<Person> peopleList = new ArrayList<>();
    private static final double MIN_GPA = 0;
    private static final double MAX_GPA = 5;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // it made for test. You can comment this row
//   peopleList = RandomPersonGenerator.generateRandomPeopleList(100);

        System.out.println("Welcome to my Personal Management Program");
        while (true) {
            displayMenu();
            int choice = getAndValidateIntInput(scanner, 1, 8);
            scanner.nextLine();
            switch (choice) {
                case 1:
                    addFacultyInformation(scanner);
                    break;
                case 2:
                    addStudentInformation(scanner);
                    break;
                case 3:
                    printInvoice();
                    break;
                case 4:
                    printFaculty();
                    break;
                case 5:
                    addStaffInformation(scanner);
                    break;
                case 6:
                    printStaff();
                    break;
                case 7:
                    deletePerson(scanner);
                    break;
                case 8:
                    finishProgram(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                    break;
            }
        }
    }

    private static void displayMenu() {
        System.out.println("Choose one of the options:");
        System.out.println("1.  Enter the information of a faculty");
        System.out.println("2.  Enter the information of a students");
        System.out.println("3.  Print tuition invoice");
        System.out.println("4.  Print faculty information");
        System.out.println("5.  Enter the information of a staff member");
        System.out.println("6.  Print the information of a staff member");
        System.out.println("7.	Delete a person");
        System.out.println("8.  Exit Program");
    }

    private static void addFacultyInformation(Scanner scanner) {
        System.out.println("Enter the faculty info:");
        System.out.print("Name of the faculty: ");
        String fullName = scanner.nextLine();
        String facultyId = getId(scanner);
        Rank rank = getRank(scanner);
        Department facultyDepartment = getDepartment(scanner);
        Person faculty = new Faculty(facultyId, fullName, facultyDepartment, rank);
        peopleList.add(faculty);
        System.out.println("Faculty added!");
    }

    private static void addStudentInformation(Scanner scanner) {
        System.out.println("Enter the student info:");
        System.out.print("Name of Student: ");
        String studentName = scanner.nextLine();
        String studentId = getId(scanner);
        double gpa = getGpa(scanner);
        System.out.println("Credit hours: ");
        int creditHours = getAndValidateIntInput(scanner, 0, Integer.MAX_VALUE);
        Person student = new Student(studentId, studentName, gpa, creditHours);
        peopleList.add(student);
        System.out.println("Student added!");
    }

    private static void printInvoice() {
        System.out.println("Enter the student’s is:");
        Scanner scanner = new Scanner(System.in);
        String studentId = scanner.nextLine();
        try {
            Person person = getPerson(studentId, (p) -> p instanceof Student);
            person.print();
        } catch (NoSuchElementException e) {
            System.out.printf("Student not found with ID: %s \n", studentId);
        }
    }

    private static void printFaculty() {
        System.out.println("Enter the Faculty’s id: ");
        Scanner scanner = new Scanner(System.in);
        String id = scanner.nextLine();
        try {
            Person person = getPerson(id, (p) -> p instanceof Faculty);
            person.print();
        } catch (NoSuchElementException e) {
            System.out.printf("Faculty not found with ID: %s \n", id);
        }
    }

    private static Person getPerson(String id, Predicate<Person> typeCheck) {
        return peopleList.stream()
                .filter(p -> p.getId().equals(id))
                .filter(typeCheck)
                .findFirst()
                .orElseThrow();
    }

    private static void printStaff() {
        System.out.println("Enter the Staff’s id: ");
        Scanner scanner = new Scanner(System.in);
        String id = scanner.nextLine();
        try {
            Person person = getPerson(id, (p -> p instanceof Staff));
            person.print();
        } catch (NoSuchElementException e) {
            System.out.printf("No staff member matched with ID: %s \n", id);
        }
    }

    private static void addStaffInformation(Scanner scanner) {

        System.out.println("Name of the staff member: ");
        String staffName = scanner.nextLine();
        String id = getId(scanner);
        Department department = getDepartment(scanner);
        Status status = getStatus(scanner);
        Person staff = new Staff(id, staffName, department, status);
        peopleList.add(staff);
        System.out.println("Staff member added!");
    }


    private static void deletePerson(Scanner scanner) {
        System.out.print("Enter the ID of the person to delete: ");
        String idToDelete = scanner.nextLine();
        for (Person person : peopleList) {
            if (person.getId().equals(idToDelete)) {
                peopleList.remove(person);
                System.out.println("Person deleted successfully.");
                return;
            }
        }
        System.out.println("Sorry no such person exists.");
    }

    private static void finishProgram(Scanner scanner) {
        String response;
        while (true) {
            System.out.println("Would you like to create the report? (Y/N): ");
            response = scanner.next().toLowerCase();
            if (response.equals("n")) {
                System.out.println("Exiting program.");
                System.exit(0);
            } else if (response.equals("y")) {
                System.out.println("Would like to sort your students by descending gpa or name (1 for gpa, 2 for name): ");
                int choice = getAndValidateIntInput(scanner, 1, 2);
                generateReport(choice);
                System.out.println("Report created and saved on your hard drive!");
                System.out.println("Goodbye!");
                System.exit(0);
            }
            System.out.printf("%s is invalid response", response);
        }


    }

    private static void generateReport(int sortChoice) {
        try {
            FileWriter fileWriter = new FileWriter("report.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printHeader(printWriter);
            printFaculty(printWriter);
            printStaff(printWriter);
            if (sortChoice == 1) {
                printStudentSortByGpa(printWriter);
            } else {
                printStudentSortByName(printWriter);
            }

            printWriter.close();

        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    private static void printStudentSortByName(PrintWriter printWriter) {
        printWriter.println("Students (Sorted by Full Name in descending order)");
        printWriter.println("-----------");
        peopleList.stream()
                .filter(p -> p instanceof Student)
                .map(p -> (Student) p)
                .sorted((p1, p2) -> p2.getFullName().compareTo(p1.getFullName()))
                .forEach(p -> p.printToFile(printWriter));
    }


    private static void printStudentSortByGpa(PrintWriter printWriter) {
        printWriter.println("Students (Sorted by GPA in descending order)");
        printWriter.println("-----------");
        peopleList.stream()
                .filter(p -> p instanceof Student)
                .map(p -> (Student) p)
                .sorted(Comparator.comparing(Student::getGpa).reversed())
                .forEach(p -> p.printToFile(printWriter));
    }

    private static void printStaff(PrintWriter printWriter) {
        printWriter.println("Staff Members");
        printWriter.println("-------------------");
        printPerson(printWriter, (p -> p instanceof Staff));
        printWriter.println();
    }

    private static void printFaculty(PrintWriter printWriter) {
        printWriter.println("Faculty Members");
        printWriter.println("-------------------------");
        printPerson(printWriter, (p -> p instanceof Faculty));
    }

    private static void printPerson(PrintWriter printWriter, Predicate<Person> typeCheck) {
        peopleList.stream()
                .filter(typeCheck)
                .forEach(f -> f.printToFile(printWriter));
        printWriter.println();
    }

    private static void printHeader(PrintWriter printWriter) {
        printWriter.println("Report created on " + LocalDate.now());
        printWriter.println("***********************");
        printWriter.println();
    }

    private static Status getStatus(Scanner scanner) {

        while (true) {
            System.out.println("Status, Enter P for Part Time, or Enter F for Full Time: ");
            String staffStatus = scanner.next().toLowerCase();
            try {
                return Status.getByNameIgnoringCase(staffStatus);
            } catch (IllegalArgumentException e) {
                System.out.printf("%s is invalid status", staffStatus);
            }
        }
    }


    private static double getGpa(Scanner scanner) {
        double gpa;
        while (true) {
            System.out.print("Enter student Gpa: ");
            try {
                gpa = scanner.nextDouble();
                if (gpa >= MIN_GPA && gpa <= MAX_GPA)
                    break;
                else System.out.printf("invalid gpa. Enter double in rage form %s to %s \n", MIN_GPA, MAX_GPA);
            } catch (InputMismatchException e) {
                System.out.println("Invalid Gpa. Please enter a valid number.");
                scanner.nextLine();
            }
        }
        return gpa;
    }

    private static Department getDepartment(Scanner scanner) {
        while (true) {
            System.out.print("Enter department: ");
            String scannerDepartment = scanner.nextLine();
            try {
                return Department.getByNameIgnoringCase(scannerDepartment);
            } catch (IllegalArgumentException e) {
                System.out.printf("%s is invalid department", scannerDepartment);
            }
        }
    }

    private static String getId(Scanner scanner) {
        String facultyId;
        while (true) {
            System.out.print("ID: ");
            facultyId = scanner.nextLine();
            if (isValidID(facultyId)) {
                if (isIdExists(facultyId)) {
                    System.out.println("ID already exists");
                } else {
                    break;
                }
            } else {
                System.out.println("Invalid ID format. Must be LetterLetterDigitDigitDigitDigit");
            }
        }
        return facultyId;
    }

    private static Rank getRank(Scanner scanner) {
        Rank rank;
        while (true) {
            System.out.print("Enter rank: ");
            String scanRank = scanner.nextLine();
            try {
                rank = Rank.getByNameIgnoringCase(scanRank);
                break;
            } catch (IllegalArgumentException e) {
                System.out.printf("%s is invalid rank", scanRank);
            }
        }
        return rank;
    }


    private static boolean isIdExists(String id) {
        if (peopleList.isEmpty()) {
            return false;
        }
        return peopleList.stream().anyMatch(p -> p.getId().equals(id));
    }

    public static boolean isValidID(String id) {
        return id.matches("[a-zA-Z]{2}\\d{4}");
    }

    public static int getAndValidateIntInput(Scanner scanner, int minValue, int maxValue) {
        int userInput;
        while (true) {
            try {
                userInput = scanner.nextInt();
                if (userInput >= minValue && userInput <= maxValue) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a number within the specified range.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        }
        return userInput;
    }
}

abstract class Person {
    private String id;
    private String fullName;

    public Person() {
    }

    public Person(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public abstract void print();

    public abstract void printToFile(PrintWriter printWriter);
}

abstract class Employee extends Person {
    private Department department;

    public Employee() {
        super();
    }

    public Employee(String id, String fullName, Department department) {
        super(id, fullName);
        this.department = department;
    }


    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

}


class Student extends Person {

    private final double PRICE = 236.45;
    private final double FEE = 52;
    private final double GPA_FOR_DISCOUNT = 3.85;
    private static int number = 0;

    private double gpa;
    private int numberOfCreditHours;

    public Student() {
        super();
    }

    public Student(String id, String fullName, double gpa, int numberOfCreditHours) {
        super(id, fullName);
        this.gpa = gpa;
        this.numberOfCreditHours = numberOfCreditHours;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getNumberOfCreditHours() {
        return numberOfCreditHours;
    }

    public void setNumberOfCreditHours(int numberOfCreditHours) {
        this.numberOfCreditHours = numberOfCreditHours;
    }

    @Override
    public void print() {
        int discount = getDiscount();
        BigDecimal total = getTotal(discount);

        System.out.printf("Here is the tuition invoice for %s:\n\n", this.getFullName());
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("\t %s \t %s\n", this.getFullName(), this.getId());
        System.out.printf("Credit Hours:%s ($%s/credit hour)\n", this.getNumberOfCreditHours(), PRICE);
        System.out.printf("Fees: $%s \n \n", FEE);
        System.out.printf("Total payment (after discount): $%s\t\t\t($%s discount applied)\n", total, discount);
        System.out.println("---------------------------------------------------------------------------");

    }

    private BigDecimal getTotal(int discount) {
        BigDecimal total;
        total = BigDecimal.valueOf((this.getNumberOfCreditHours() * PRICE + FEE) * (100 - discount) / 100).setScale(2, RoundingMode.HALF_UP);
        return total;
    }

    private int getDiscount() {
        if (this.getGpa() >= GPA_FOR_DISCOUNT) {
            return 25;
        }

        return 0;
    }

    @Override
    public void printToFile(PrintWriter printWriter) {
        printWriter.printf("%s. %s \n", ++number, this.getFullName());
        printWriter.println("ID:" + this.getId());
        printWriter.printf("Gpa : %s \n", this.getGpa());
        printWriter.printf("Credit hours : %s \n", this.getNumberOfCreditHours());
        printWriter.println();
    }


}

class Faculty extends Employee {
    private Rank rank;
    private static int number = 0;

    public Faculty(String id, String fullName, Department department, Rank rank) {
        super(id, fullName, department);
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public void print() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("\t %s \t %s\n", this.getFullName(), this.getId());
        System.out.printf("%s Department, %s\n", this.getDepartment(), this.getRank());
        System.out.println("---------------------------------------------------------------------------");
    }

    @Override
    public void printToFile(PrintWriter printWriter) {
        printWriter.printf("%s. %s \n", ++number, this.getFullName());
        printWriter.println("ID:" + this.getId());
        printWriter.printf("%s, %s \n \n", this.getDepartment(), this.getRank());
    }


}

class Staff extends Employee {
    private Status status;
    private static int number = 0;

    public Staff() {
        super();
    }

    public Staff(String id, String fullName, Department department, Status status) {
        super(id, fullName, department);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public void print() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("\t %s \t %s\n", this.getFullName(), this.getId());
        System.out.printf("%s Department, %s\n", this.getDepartment(), this.getStatus().fullName);
        System.out.println("---------------------------------------------------------------------------");
    }

    @Override
    public void printToFile(PrintWriter printWriter) {
        printWriter.printf("%s. %s \n", ++number, this.getFullName());
        printWriter.println("ID:" + this.getId());
        printWriter.printf("%s, %s \n \n", this.getDepartment(), this.getStatus().fullName);
    }
}

enum Department {

    Mathematics("mathematics"), Engineering("engineering"), English("English");


    Department(String name) {
        this.name = name;
    }

    final String name;

    public static Department getByNameIgnoringCase(String name) {
        for (Department value : Department.values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Department not found " + name);
    }

}

enum Rank {
    Professor("professor"), Adjunct("adjunct");

    Rank(String name) {
        this.name = name;
    }

    final String name;

    public static Rank getByNameIgnoringCase(String name) {
        for (Rank value : Rank.values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Rank not found " + name);
    }

}

enum Status {
    PART_TIME("p", "Part Time"), FULL_TIME("f", "Full Time");


    final String name;
    final String fullName;

    Status(String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }

    public static Status getByNameIgnoringCase(String name) {
        for (Status value : Status.values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Rank not found " + name);
    }
}
