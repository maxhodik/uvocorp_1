public class Staff extends Employee{

   //status (part time or full time): String
    private String status;

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
}
