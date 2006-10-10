package fi.helsinki.cs.kohahdus.trainer;
import java.util.*;

public class Module {
    
    int taskCount =0;                         // number of tasks
    String moduleID=null;                //  unique module identifier
    String   moduletype =null;          // type of module
    boolean creditability=true;         // are credits counted
    Date creditsUpTo=null;               // credits available up to time
    String selectorName=null;          // name of the possible selector class, null if undefined
    String selectorInit=null; 
                       // selector initialization parameters as xml parameterstring

public Module ( int taskcount, String id, String mtype) {
    taskCount=taskcount;
    moduleID=id;
    moduletype=mtype;
}    

public int getTaskCount() {
    return taskCount;
}

public String getModuleID() {
    return moduleID;
}    
    
public String getModuleType() {
    return moduleType;
}    

public boolean isCreditable(Date thisday) {
    return creditability;
}    


    

}