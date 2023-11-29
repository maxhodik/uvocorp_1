import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FinalProject {
    private static ArrayList<Person> peopleList = new ArrayList<>();
    private static final int MIN_GPA = 0;
    private static final int MAX_GPA = 5;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to my Personal Management Program");
        while (true) {
            System.out.println("Choose one of the options:");
            System.out.println("1.  Enter the information of a faculty");
            System.out.println("2.  Enter the information of a students");
            System.out.println("3.  Print tuition invoice");
            System.out.println("4.  Print faculty information");
            System.out.println("5.  Enter the information of a staff member");
            System.out.println("6.  Print the information of a staff member");
            System.out.println("7.	Delete a person");
            System.out.println("8.  Exit Program");
//            int choice = scanner.nextInt();
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
                    finishProgram();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                    break;
            }
        }
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


    private static void finishProgram() {
        System.out.println("Exiting program.");
        System.exit(0);
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

    private static void addStaffInformation(Scanner scanner) {
//        Name of the staff member: Jamal Kareem
//        Enter the id: ja6980
//        Department: English
//        Status, Enter P for Part Time, or Enter F for Full Time: f
//
//        Staff member added!
        String status;
        System.out.println("Name of the staff member: ");
        String staffName = scanner.nextLine();
        String id = getId(scanner);
        Employee.Department department = getDepartment(scanner);
        status = getStatus(scanner);
        Staff staff = new Staff(id, staffName, department, status);
        peopleList.add(staff);
        System.out.println("Staff member added!");

    }

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

    private static void printInvoice() {
        System.out.println("Enter the student’s is:");
        Scanner scanner = new Scanner(System.in);
        String studentId = scanner.nextLine();
        try {
            Person person = peopleList.stream()
                    .filter(p -> p.getId().equals(studentId))
                    .filter(p -> p instanceof Student)
//                    .filter(p -> p.getClass().isInstance(Student.class))
                    .findFirst()
                    .orElseThrow();
            person.print();
        } catch (NoSuchElementException e) {
            System.out.printf("Student not found with ID: %s /n", studentId);
        }
    }

    private static void addStudentInformation(Scanner scanner) {
//        	Enter the student info:
//			Name of Student: Julia Alvarez
//			ID: j1254
//			Invalid ID format. Must be LetterLetterDigitDigitDigitDigit
//
//ID: ju1254
//
//Gpa: 3.26
//			Credit hours: 7
//   	Student added!
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

    private static void addFacultyInformation(Scanner scanner) {
        String full_name;
        String facultyId;
        String scanRank;
        //    Enter the faculty info:
        //			Name of the faculty:  John Miller
        //			ID: jo7894
        //
        //Rank: Instructor
        //				“Instructor” is invalid
        //			Rank: Assistant Professor
        //				“Assistant Professor” is invalid
        //
        //			Rank: Professor
        //
        //Department: Engineering
        //
        //	Faculty added!
        System.out.println("Enter the faculty info:");
        System.out.print("Name of the faculty: ");
        full_name = scanner.nextLine();
        facultyId = getId(scanner);
        Faculty.Rank rank;
        while (true) {
            System.out.print("Enter rank: ");
            scanRank = scanner.nextLine();
            try {
                rank = Faculty.Rank.getByNameIgnoringCase(scanRank);
                break;
            } catch (IllegalArgumentException e) {
                System.out.printf("%s is invalid rank", scanRank);
            }
        }
        Employee.Department facultyDepartment = getDepartment(scanner);
        Faculty faculty = new Faculty(facultyId, full_name, facultyDepartment, rank);
        peopleList.add(faculty);
        System.out.println("Faculty added!");
    }

    private static Employee.Department getDepartment(Scanner scanner) {
        while (true) {
            System.out.print("Enter department: ");
            String scannerDepartment = scanner.nextLine();
            try {
                return Employee.Department.getByNameIgnoringCase(scannerDepartment);

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
