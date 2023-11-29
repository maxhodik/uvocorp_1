import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FinalProject {
    private static ArrayList<Person> peopleList = new ArrayList<>();
    private static final int MIN_GPA = 0;
    private static final int MAX_GPA = 5;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to my Personal Management Program");
        while (true) {
            displayMenu();
            int choice = validateIntInput(scanner, 1, 8);
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
        String full_name;
        String facultyId;
        String scanRank;
        System.out.println("Enter the faculty info:");
        System.out.print("Name of the faculty: ");
        full_name = scanner.nextLine();
        facultyId = getId(scanner);
        Rank rank;
        while (true) {
            System.out.print("Enter rank: ");
            scanRank = scanner.nextLine();
            try {
                rank = Rank.getByNameIgnoringCase(scanRank);
                break;
            } catch (IllegalArgumentException e) {
                System.out.printf("%s is invalid rank", scanRank);
            }
        }
        Department facultyDepartment = getDepartment(scanner);
        Faculty faculty = new Faculty(facultyId, full_name, facultyDepartment, rank);
        peopleList.add(faculty);
        System.out.println("Faculty added!");
    }

    private static void addStudentInformation(Scanner scanner) {

        double gpa;
        System.out.println("Enter the student info:");
        System.out.print("Name of Student: ");
        String studentName = scanner.nextLine();
        String studentId = getId(scanner);
        gpa = getGpa(scanner);
        System.out.println("Credit hours: ");
        int creditHours = validateIntInput(scanner, 0, Integer.MAX_VALUE);
        Student student = new Student(studentId, studentName, gpa, creditHours);
        peopleList.add(student);
        System.out.println("Student added!");
    }

    private static void printInvoice() {
        System.out.println("Enter the student’s is:");
        Scanner scanner = new Scanner(System.in);
        String studentId = scanner.nextLine();
        try {
            Person person = peopleList.stream()
                    .filter(p -> p.getId().equals(studentId))
                    .filter(p -> p instanceof Student)
                    .findFirst()
                    .orElseThrow();
            person.print();
        } catch (NoSuchElementException e) {
            System.out.printf("Student not found with ID: %s /n", studentId);
        }
    }

    private static void printFaculty() {
        System.out.println("Enter the Faculty’s id: ");
        Scanner scanner = new Scanner(System.in);
        String id = scanner.nextLine();
        try {
            Person person = peopleList.stream()
                    .filter(p -> p.getId().equals(id))
                    .filter(p -> p instanceof Faculty)
                    .findFirst()
                    .orElseThrow();
            person.print();
        } catch (NoSuchElementException e) {
            System.out.printf("Faculty not found with ID: %s /n", id);
        }
    }

    private static void addStaffInformation(Scanner scanner) {
        String status;
        System.out.println("Name of the staff member: ");
        String staffName = scanner.nextLine();
        String id = getId(scanner);
        Department department = getDepartment(scanner);
        status = getStatus(scanner);
        Staff staff = new Staff(id, staffName, department, status);
        peopleList.add(staff);
        System.out.println("Staff member added!");
    }

    private static void printStaff() {
        System.out.println("Enter the Staff’s id: ");
        Scanner scanner = new Scanner(System.in);
        String id = scanner.nextLine();
        try {
            Person person = peopleList.stream()
                    .filter(p -> p.getId().equals(id))
                    .filter(p -> p instanceof Staff)
                    .findFirst()
                    .orElseThrow();
            person.print();
        } catch (NoSuchElementException e) {
            System.out.printf("No staff member matched with ID: %s /n", id);
        }
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
//        Would you like to create the report? (Y/N): y
//        Would like to sort your students by descending gpa or name (1 for gpa, 2 for name): 1


        String response;
        while (true) {
            System.out.println("Would you like to create the report? (Y/N): ");
            response = scanner.next().toLowerCase();
            if (response.equals("n")) {
                System.out.println("Exiting program.");
                System.exit(0);
            } else if (response.equals("y")) {
                System.out.println("Would like to sort your students by descending gpa or name (1 for gpa, 2 for name): ");
                int choice = validateIntInput(scanner, 1, 2);

                generateReport();
                break;
            }
            System.out.printf("%s is invalid response", response);
        }


    }

    private static void generateReport() {
        try {
            FileWriter fileWriter = new FileWriter("report.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
//         Print the header
            printWriter.println("Report created on " + LocalDate.now());
            printWriter.println("***********************");
            printWriter.println();

            // Print faculty information
            printWriter.println("Faculty Members");
            printWriter.println("-------------------------");
            peopleList.stream()
                    .filter(p -> p instanceof Faculty)
                    .forEach(f -> f.printToFile(printWriter));
            printWriter.println();

//             Print staff information
            printWriter.println("Staff Members");
            printWriter.println("-------------------");
            peopleList.stream()
                    .filter(p -> p instanceof Staff)
                    .forEach(f -> f.printToFile(printWriter));
            printWriter.println();

            //             Print student information
            printWriter.println("Students (Sorted by GPA in descending order)");
            printWriter.println("-----------");
            peopleList.stream()
                    .filter(p -> p instanceof Student)
                    .map(p -> (Student) p)
                    .sorted(Comparator.reverseOrder())
//                    .sorted((p1, p2) -> p2.getFullName().compareTo(p1.getFullName()))
                    .forEach(p -> p.printToFile(printWriter));
            List<Person> students = peopleList.stream().filter(p -> p instanceof Student).toList();


            printWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
//        try {
//            FileWriter fileWriter = new FileWriter("report.txt");
//            PrintWriter printWriter = new PrintWriter(fileWriter);
//
//            // Print the header
//            printWriter.println("Report created on " + getCurrentDate());
//            printWriter.println("***********************");
//            printWriter.println();
//
//            // Print faculty information
//            printWriter.println("Faculty Members");
//            printWriter.println("-------------------------");
//            peopleList.stream()
//                    .filter(p-> p instanceof  Faculty)
//                    .forEach(Person::print);
//
//            for (Person person : peopleList) {
//                if (person instanceof Faculty) {
//                    ((Faculty) person).print();
//                }
//            }
//            printWriter.println();
//
//            // Print staff information
//            printWriter.println("Staff Members");
//            printWriter.println("-------------------");
//            for (Person person : peopleList) {
//                if (person instanceof Staff) {
//                    ((Staff) person).print();
//                }
//            }
//            printWriter.println();
//
//            // Sort students by GPA in descending order
//            List<Student> students = new ArrayList<>();
//            for (Person person : peopleList) {
//                if (person instanceof Student) {
//                    students.add((Student) person);
//                }
//            }
//            Collections.sort(students, Collections.reverseOrder());
//
//            // Print students information
//            printWriter.println("Students (Sorted by GPA in descending order)");
//            printWriter.println("-----------");
//            for (Student student : students) {
//                student.print();
//            }
//
//            printWriter.close();
//            System.out.println("Report created and saved on your hard drive!");
//
//        } catch (IOException e) {
//            System.out.println("Error writing to the file: " + e.getMessage());
//        }
    }

//    private static String getCurrentDate() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//        return dateFormat.format(new Date());
//    }

//}

    private static String getStatus(Scanner scanner) {
        String status;
        while (true) {
            System.out.println("Status, Enter P for Part Time, or Enter F for Full Time: ");
            String staffStatus = scanner.next().toLowerCase();
            if (staffStatus.equals("f")) {
                status = "Full Time";
                break;
            } else if (staffStatus.equals("p")) {
                status = "Part Time";
                break;
            }
            System.out.printf("%s is invalid status", staffStatus);
        }
        return status;
    }


    private static double getGpa(Scanner scanner) {
        double gpa;
        while (true) {
            System.out.print("Enter student Gpa: ");
            try {
                gpa = scanner.nextDouble();
                //todo read "."
                if (gpa >= MIN_GPA && gpa <= MAX_GPA)
                    break;
                else System.out.printf("invalid gpa. Enter double in rage form %s to %s /n", MIN_GPA, MAX_GPA);
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
                if (isIdUnique(facultyId)) {
                    System.out.println("ID already exists");
                } else break;
            } else System.out.println("Invalid ID format. Must be LetterLetterDigitDigitDigitDigit");
        }
        return facultyId;
    }


    private static boolean isIdUnique(String id) {

        if (peopleList.isEmpty() || peopleList == null) return false;
        return peopleList.stream().anyMatch(p -> p.getId().equals(id));
    }

    public static boolean isValidID(String id) {
        return id.matches("[a-zA-Z]{2}\\d{4}");
    }

    public static int validateIntInput(Scanner scanner, int minValue, int maxValue) {
        int userInput;
        while (true) {
            try {
                userInput = scanner.nextInt();
                if (userInput >= minValue && userInput <= maxValue) {
                    break; // Input is valid, exit the loop
                } else {
                    System.out.println("Invalid input. Please enter a number within the specified range.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear the buffer
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

    // 	department (mathematics, engineering or english)
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


class Student extends Person implements Comparable<Student> {

    private final double PRICE = 236.45;
    private final int FEE = 52;
    private final double GPA_FOR_DISCOUNT = 3.85;
    private static int number;

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
        BigDecimal total;
        int discount;
        if (this.getGpa() >= GPA_FOR_DISCOUNT) {
            discount = 25;
        } else discount = 0;
        total = BigDecimal.valueOf((this.getNumberOfCreditHours() * PRICE + FEE) * (100 - discount) / 100).setScale(2, RoundingMode.HALF_UP);

        System.out.printf("Here is the tuition invoice for %s:\n\n", this.getFullName());
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("\t %s \t %s\n", this.getFullName(), this.getId());
        System.out.printf("Credit Hours:%s ($%s/credit hour)\n", this.getNumberOfCreditHours(), PRICE);
        System.out.printf("Fees: $%s \n \n", FEE);
        System.out.printf("Total payment (after discount): $%s\t\t\t($%s discount applied)\n", total, discount);
        System.out.println("---------------------------------------------------------------------------");

    }

    @Override
    public void printToFile(PrintWriter printWriter) {
        printWriter.printf("%s. %s \n", ++number, this.getFullName());
        printWriter.println("ID:" + this.getId());
        printWriter.printf("Gpa : %s \n", this.getGpa());
        printWriter.printf("Credit hours : %s \n", this.getNumberOfCreditHours());
        printWriter.println();
    }

    @Override
    public int compareTo(Student o) {
        return (int) (this.getGpa()-o.getGpa());

    }
}

class Faculty extends Employee {
    private Rank rank;
    private static int number;

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
    private String status;
    private static int number;

    public Staff() {
        super();
    }

    public Staff(String id, String fullName, Department department, String status) {
        super(id, fullName, department);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void print() {
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("\t %s \t %s\n", this.getFullName(), this.getId());
        System.out.printf("%s Department, %s\n", this.getDepartment(), this.getStatus());
        System.out.println("---------------------------------------------------------------------------");
    }

    @Override
    public void printToFile(PrintWriter printWriter) {
        printWriter.printf("%s. %s \n", ++number, this.getFullName());
        printWriter.println("ID:" + this.getId());
        printWriter.printf("%s, %s \n \n", this.getDepartment(), this.getStatus());
    }
}

enum Department {

    Mathematics("mathematics"), Engineering("engineering"), English("English");


    Department(String name) {
        this.name = name;
    }

    String name;

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

    String name;

    public static Rank getByNameIgnoringCase(String name) {
        for (Rank value : Rank.values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Rank not found " + name);
    }

}
