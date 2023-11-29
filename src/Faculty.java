public class Faculty extends Employee {

    private Rank rank;

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

}
