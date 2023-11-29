public abstract class Employee  extends Person {

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
}