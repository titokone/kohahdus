package trainer;

public class Course {
        
    int modules=0;                     // number of modules
    String courseID= null;                 //  unique course identifier
    String  courseType=null;            // type of course
    String courseStyle=null;            // are credits counted
  
public Course (int modules, String courseID,
        String  coursetype,  String courseStyle) {
    this.modules=modules;
    this.courseID=courseID;
    this.courseType=coursetype;
    this.courseStyle=courseStyle;
}
 
public int getModules() {return modules;}
public String getCourseID() {return courseID;}
public String getCourseType() {return courseType;}
public String getCourseStyle() {return courseStyle;}

}