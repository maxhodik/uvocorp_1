public abstract class Person {
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
    public void delete (String id){
    }
    public void add(Person person){

    }
}
