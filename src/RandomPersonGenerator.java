import java.util.ArrayList;
import java.util.Random;

public class RandomPersonGenerator {
    public static ArrayList<Person> generateRandomPeopleList(int size) {
        ArrayList<Person> peopleList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            int personType = random.nextInt(3); // 0 for Student, 1 for Faculty, 2 for Staff

            if (personType == 0) {
                peopleList.add(generateRandomStudent(random, i));
            } else if (personType == 1) {
                peopleList.add(generateRandomFaculty(random, i));
            } else {
                peopleList.add(generateRandomStaff(random, i));
            }
        }
        return peopleList;
    }

    private static Student generateRandomStudent(Random random, int i) {
        String fullName = generateRandomFullName();
        String id = generateRandomID(i);
        double gpa = 2.0 + (4.0 - 2.0) * random.nextDouble();
        int creditHours = random.nextInt(20);

        return new Student(id, fullName, gpa, creditHours);
    }

    private static Faculty generateRandomFaculty(Random random, int i) {
        String fullName = generateRandomFullName();
        String id = generateRandomID(i);
        String department = generateRandomDepartment(random);
        Department dep = Department.getByNameIgnoringCase(department);
        String rank = generateRandomRank(random);
        Rank rank1 = Rank.getByNameIgnoringCase(rank);

        return new Faculty(id, fullName, dep, rank1);
    }

    private static Staff generateRandomStaff(Random random, int i) {
        String fullName = generateRandomFullName();
        String id = generateRandomID(i);
        String department = generateRandomDepartment(random);
        Department dep = Department.getByNameIgnoringCase(department);
        String status = generateRandomStatus(random);
        Status stat= Status.getByNameIgnoringCase(status);

        return new Staff(id, fullName, dep, stat);
    }

    private static String generateRandomFullName() {
   StringBuilder name= new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = (char) random.nextInt(65,122);
         name.append(c);
        }
        return name.toString();
    }

    private static String generateRandomID(int i) {
        return "rp"+(1000+i);
    }

    private static String generateRandomDepartment(Random random) {
        String[] departments = {"Mathematics", "Engineering", "English"};
        return departments[random.nextInt(departments.length)];
    }

    private static String generateRandomRank(Random random) {
        String[] ranks = {"Professor", "Adjunct"};
        return ranks[random.nextInt(ranks.length)];
    }

    private static String generateRandomStatus(Random random) {
        String[] statuses = {"F", "P"};
        return statuses[random.nextInt(statuses.length)];
    }
}
