import java.lang.module.ModuleDescriptor.Builder;
import java.util.Date;

public class Project {
    int number;
    String title;
    String buildingType;
    String adress;
    String erfNumber;
    float totalFee;
    float amountPaidToDate;
    String deadline;
    Architect architect;
    Contractor contractor;
    Customer customer;

    public Project(ProjectBuilder Builder){
        this.number = Builder.number;
        this.title = Builder.title;
        this.buildingType = Builder.buildingType;
        this.adress = Builder.adress;
        this.erfNumber = Builder.erfNumber;
        this.totalFee = Builder.totalFee;
        this.amountPaidToDate = Builder.amountPaidToDate;
        this.deadline = Builder.deadline;
        this.architect = Builder.architect;
        this.contractor = Builder.contractor;
        this.customer = Builder.customer;
    }

    public static class ProjectBuilder{

        //Attributes
        int number;
        String title;
        String buildingType;
        String adress;
        String erfNumber;
        float totalFee;
        float amountPaidToDate;
        String deadline;
        Architect architect;
        Contractor contractor;
        Customer customer;

        //Methods
        public ProjectBuilder(){

        }

        public ProjectBuilder number(int number){
            this.number = number;
            return this;
        }

        public ProjectBuilder title(String title){
            this.title = title;
            return this;
        }

        public ProjectBuilder buildingType(String buildingType){
            this.buildingType = buildingType;
            return this;
        }

        public ProjectBuilder adress(String adress){
            this.adress = adress;
            return this;
        }

        public ProjectBuilder erfNumber(String erfNumber){
            this.erfNumber = erfNumber;
            return this;
        }

        public ProjectBuilder totalFee(float totalFee){
            this.totalFee = totalFee;
            return this;
        }

        public ProjectBuilder amountPaidToDate(float amountPaidToDate){
            this.amountPaidToDate = amountPaidToDate;
            return this;
        }

        public ProjectBuilder deadline(String deadline){
            this.deadline = deadline;
            return this;
        }

        public ProjectBuilder architect(Architect architect){
            this.architect = architect;
            return this;
        }

        public ProjectBuilder contractor(Contractor contractor){
            this.contractor = contractor;
            return this;
        }
        
        public ProjectBuilder customer(Customer customer){
            this.customer = customer;
            return this;
        }

        public Project build(){

            Project project = new Project(this);

            project.number = this.number;
            if(this.title == ""){
                project.title = this.buildingType + " " + this.customer.lastName;
            }
            else{
                project.title = this.title;
            }
            project.buildingType = this.buildingType;
            project.adress = this.adress;
            project.erfNumber = this.erfNumber;
            project.totalFee = this.totalFee;
            project.amountPaidToDate = this.amountPaidToDate;
            project.deadline = this.deadline;
            project.architect = this.architect;
            project.contractor = this.contractor;
            project.customer = this.customer;

            return project;
        }

    }
}