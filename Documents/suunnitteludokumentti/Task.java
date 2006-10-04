// Laajennetaan olemassa olevaa Task luokkaa
public class Task {
	
	/** Return the name of this task */
	public String getName() {}
	
	/** Set the name of this task */
	public void setName(String name) {}
	
	/** Return name of the last person who has modified this task */
	public String getAuthor() {}

	/** Set "last task modification by" attribute to Name */
	public void setAuthor(String name) {}
	
	/** Return the date and time this task was last modified */
	public Date getModificationDate() {}
	
	
	public Criterion[] getCriteria(Task task) {} // Delegate to DBHandler
	
	
	public void setCriteria(Task task, Criterion[] criteria) {} // Delegate to DBHandler
/*	
	
	
	%%%%% Task-luokka %%%%%
	public DisplayerInterface getDisplayer(String language) 
	public AnalyserInterface getAnalyser(String language) 
	public boolean shouldAllowRetry(int triesSoFar) 
	public boolean shouldRegisterTry() 
	public boolean shouldStoreAnswer() 
	public boolean shouldBeAnalysed() 
	public boolean shouldKnowStudent()
	public String getTasktypeID () 
	public Tasktype getTasktype() 
	public String getTaskID() 
	public int getCutoffvalue() 
	public String getStyle() 
	public boolean wasSuccess(int points) 
	public boolean isInTime(Timestamp when) 
	
	String taskID;
	String courseID;
	String moduleID;
	String tasktypeID;
	Tasktype tasktype;
	int seqNo;
	Timestamp deadLine;
	boolean shouldStore;
	boolean shouldRegister;
	boolean shouldKnow;
	boolean shouldEvaluate;
	int cutoffvalue;
	int noOfTries;
	String style;
	
	%%%%% Task-taulu %%%%%
	taskid varchar(20) not null primary key,   /* each task has a unique id */
//	taskname varchar(40),                      /* name of task in English - not used currently */
//	tasktype varchar(40),                      /* the type of the task - this connects the displayer and analyser to the task */
//	taskmetadata varchar(2000),                /* other task specific metadata - xml-format, not used currently  */
//	numberoftries_def integer,                 /* how many tries are allowed - default value */
//	shouldstoreanswer_def char,                /* should the system store the answer (Y/N) - default value */
//	shouldregistertry_def char,                /* should the system register the try (Y/N) - default value */
//	shouldknowstudent_def char,                /* must the student be known (Y/N) -default value */
//	shouldevaluate_def char,                   /* should the task be evaluated on-line (Y/N) - default value */
//	cutoffvalue integer,                       /* the value to be exceeded to pass the task */
//	foreign key (tasktype) references tasktype
	
	
//	%%%%% Taskinmodule-taulu %%%%%
//	courseid            varchar(20) not null,  /* the course where used  */
//	moduleid            varchar(20) not null,  /* the module where used */
//	seqno               integer not null,      /* sequence number of the task within the module */
//	taskid              varchar(20) not null,  /* which task is used */
//	numberoftries       integer not null,      /* how many tries are allowed in this context */
//	shouldstoreanswer   char,                  /* should the system store the answer (Y/N) in this context */
//	shouldregistertry   char,                  /* should the system register the try (Y/N) in this context */
//	shouldknowstudent   char,                  /* must the student be known (Y/N)in this context */
//	shouldevaluate      char,                  /* should the task be evaluated immediately (Y/N) */
//	cutoffvalue         integer,               /* the value to be exceeded to pass the task */
//	primary key (courseid, moduleid, seqno),
//	foreign key (taskid) references task,
//	foreign key (courseid, moduleid) references module

}
