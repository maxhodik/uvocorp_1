import java.math.BigDecimal;
import java.math.RoundingMode;

public class Student extends Person {

    private final double PRICE = 236.45;
    private final int FEE = 52;
    private final double GPA_FOR_DISCOUNT = 3.85;

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
//        Here is the tuition invoice for Julia Alvarez:
//
//        ---------------------------------------------------------------------------
//                Julia Alvarez				ju1254
//        Credit Hours:7 ($236.45/credit hour)
//        Fees: $52
//
//        Total payment (after discount): $1,707.15			($0 discount applied)
//        ---------------------------------------------------------------------------
        BigDecimal total;
        int discount;
        if (this.getGpa() >= GPA_FOR_DISCOUNT) {
            discount = 25;
        } else discount = 0;
        total = BigDecimal.valueOf((this.getNumberOfCreditHours() * PRICE + FEE) * (100 - discount)/100).setScale(2, RoundingMode.HALF_UP);

        System.out.printf("Here is the tuition invoice for %s:\n\n", this.getFullName());
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("\t %s \t %s\n", this.getFullName(), this.getId());
        System.out.printf("Credit Hours:%s ($%s/credit hour)\n", this.getNumberOfCreditHours(), PRICE);
        System.out.printf("Fees: $%s \n \n", FEE);
        System.out.printf("Total payment (after discount): $%s\t\t\t($%s discount applied)\n", total, discount);
        System.out.println("---------------------------------------------------------------------------");

    }
}
