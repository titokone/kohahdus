drop table storedanswer; 
drop table studentmodel; 
drop table taskinmodule; 
drop table task; 
drop table pluginparamattributes;
drop table pluginparameters;
drop table attributevalues; 
drop table taskattributes;
drop table student; 
drop table module;
drop table course;
drop table tasktype;

/****  
*  Tasktype:
*  Tasks are of the same type if they share both the 
*  displayer and the analyser and their initialization parameters
*/
create table tasktype (
  typename            varchar(40) not null primary key,  /* task type identifier */
  author              varchar(40),                       /* author's name - currently not used */
  datecreated         date,                              /* date the tasktype was defined - currently not used */
  description         varchar(1000),                     /* other metadata about the tasktype - xml format, not used */
  displayer           varchar(40),                       /* name of the displayer class */
  analyser            varchar(40),                       /* name of the analyser class */
  tasktypestyle       varchar(80),                       /* name of the tasktype specific stylesheet */ 
  displayerinit       varchar(1000),                     /* tasktype specific initialization parameters for the displayer, xml format */
  analyserinit        varchar(1000)                      /* tasktype specific initialization parameters for the analyser, xml format */
);



/****
* Course is a collection of modules
*/
create table course (
  courseid            varchar(20) not null primary key,  /* course identifier */
  coursename          varchar(40),                       /* course name - not used currently */
  coursemetadata      varchar(2000),                     /* course metadata in xml format - not used */
  courselogo          varchar(80),                       /* url to the image file that contains course logo */
  coursestyle         varchar(80)                        /* name of the course specific css style sheet */                            
);



/****
* Module is a part of a course
*/
create table module (   
  courseid            varchar(20) not null,  /*  course identifier */
  moduleid            varchar(20) not null,  /* identifies the module within the course */
  moduletype          varchar(20) not null,  /* specifies the type of the module - training, exam */
  modulename          varchar(40),           /* name of the module in English */
  modulestyle         varchar(80),           /* file name of the module specific style sheet */
  modulemetadata      varchar(2000),         /* module specific metadata in xml-format, not used currently */
  moduleseqno         integer,               /* module sequence number within the course */
  iscreditable        char,                  /* specifies whether student gets gredit by passing the task */
  creditsupto         timestamp,             /* upto which time credits are available */
  taskselector        varchar(80),           /* classaname for the plugin class that select the next task to be presented */   
  selectorinit        varchar(1000),         /* module specific initialization parameters for the selector plugin */
  primary key (courseid, moduleid),
  foreign key (courseid) references course
);


  
 /**** 
 * A reusable learning object
 */
create table task (
  taskid varchar(20) not null primary key,   /* each task has a unique id */
  taskname varchar(40),                      /* name of task in English - not used currently */
  author varchar(40),                        /* Name of author - not used currently */
  datecreated date,                          /* date tasktype was definet - currently not used */
  tasktype varchar(40),                      /* the type of the task - this connects the displayer and analyser to the task */
  taskmetadata varchar(2000),                /* other task specific metadata - xml-format, not used currently  */
  numberoftries_def integer,                 /* how many tries are allowed - default value */
  shouldstoreanswer_def char,                /* should the system store the answer (Y/N) - default value */
  shouldregistertry_def char,                /* should the system register the try (Y/N) - default value */
  shouldknowstudent_def char,                /* must the student be known (Y/N) -default value */
  shouldevaluate_def char,                   /* should the task be evaluated on-line (Y/N) - default value */
  cutoffvalue integer,                       /* the value to be exceeded to pass the task */
  foreign key (tasktype) references tasktype
);



/****
* the usage of a task as part of a module  
*/
create table taskinmodule (
  courseid            varchar(20) not null,  /* the course where used  */
  moduleid            varchar(20) not null,  /* the module where used */
  seqno               integer not null,      /* sequence number of the task within the module */
  taskid              varchar(20) not null,  /* which task is used */
  numberoftries       integer not null,      /* how many tries are allowed in this context */
  shouldstoreanswer   char,                  /* should the system store the answer (Y/N) in this context */
  shouldregistertry   char,                  /* should the system register the try (Y/N) in this context */
  shouldknowstudent   char,                  /* must the student be known (Y/N)in this context */
  shouldevaluate      char,                  /* should the task be evaluated immediately (Y/N) */
  cutoffvalue         integer,               /* the value to be exceeded to pass the task */
  primary key (courseid, moduleid, seqno),
  foreign key (taskid) references task,
  foreign key (courseid, moduleid) references module
);



 /***** 
 * Schema information about the task attributes used by displayers and analysers,
 * Fixed attributes of the task table are not defined here
 */
create table taskattributes(
 tasktype varchar(40) not null,              /* the task the attribute is attached to */
 attributename varchar(40),                  /* the name of the attribute */
 typeofvalue varchar(12) not null,           /* type of value
                                                NONE= no value
                                                TEXT= text value
                                                NUM= numeric value */
 languagedependent char,                     /* is the value dependent on the language (Y/N) */
 howobtained varchar(12),                    /* How the value is obtained:
                                                STORED, GENERATED, EXTERNAL */
 primary key (tasktype,attributename),
 foreign key (tasktype) references tasktype
 );



/*****
* Schemas for the plugin initialization parameters
*/
create table pluginparameters(
 classname varchar(40) not null,            /* the name of the plugin class */
 elementname varchar(40) not null,          /* the name of the element */
 sequenceno integer,                        /* the sequence number of the element in the parameter set */
 typeofvalue varchar(12) not null,          /* type of value
                                                NONE= no value
                                                TEXT= text value
                                                NUM= numeric value */
 repeat integer,                            /* how many times this element may occur at most */
 primary key (classname, elementname)
);



/****
* structure of plugin initialization parameters  
*/
create table pluginparamattributes (
 classname varchar(40) not null,           /* the name of the plugin class */
 elementname varchar(40) not null,         /* the name of the element */
 attributename varchar(40) not null,       /* name of the attribute */
 sequenceno integer,                       /* the sequence number of the element in the parameter set */
 typeofvalue varchar(12) not null,         /* type of value
                                              NONE= no value
                                              TEXT= text value
                                              NUM= numeric value  */
 primary key (classname, elementname, attributename),
 foreign key (classname, elementname) references pluginparameters
);



/****
* Attribute values for task, tasktype, course, displayer, analyser attributes
* and general error messages
* All language dependent attribute values are here and also some values that
* apply for all languages
*/ 
create table attributevalues (
  objecttype char,                        /* the type of object the attribute is attached to
                                             A= analyser
                                             C= course  
                                             D= displayer
                                             E= general error message
                                             T= task  */
  objectid   varchar(40),                /* Identifier of the object 
                                             A,D: class name
                                             C: course identifier
                                             T: taskid
                                             E: errorname */
  attributename varchar(40),              /* Name of the attribute */
  language varchar(3),                    /* language of the attribute value,  currently FI,EN,ALL */
  valuetype char,                         /* type of the value C=character, N=number */
  attributevalue varchar(2000),           /* the value as a string, most are short */
  primary key (objecttype, objectid, attributename, language)
);





/****
*  student/user
*/
create table eauser (
  userid varchar(20) not null primary key,   /* user identifier, free format, must be unique */
  lastname varchar(40) not null,             /* user's last name */
  firstname varchar(40) not null,            /* user's first name */
  email varchar(80),                         /* user's email address */
  status varchar(20) not null,               /* status: adm/privileged/teacher/student */
  extid varchar(20),                         /* identifier in some associated system (like student number in university) */
  extid2 varchar(20),
  password varchar(12),                      /* student's password */
  lpref varchar(3),                          /* preferred language */
  lastvisit timestamp                        /* last time student has visitid the system */
);



/****
* Stored answers
*/
create table storedanswer (
  sid varchar(20) not null,          /* Student identifier */
  courseid varchar(20) not null,     /* course identifier */
  moduleid varchar(20) not null,     /* module identifier */
  seqno integer not null,            /* task sequence number within the module */
  trynumber integer not null,        /* the number of try for this task */
  correctness integer not null,      /* correctnes indicator 0-100 (100 =correct) */
  whenanswered timestamp,            /* time the answer was registered */
  answer varchar(2000),              /* the given answer */
  feedbacklanguage varchar(3),       /* language used for feedback */
  feedback varchar(1000),            /* feedback specific information as packed string*/
  primary key (sid,courseid,moduleid, seqno, trynumber),
  foreign key (sid) references eauser,
  foreign key (courseid, moduleid, seqno) references taskinmodule
);



/****
* Students status and history
*/
create table studentmodel (
  sid varchar(20) not null,                /* Student identifier */
  courseid varchar(20) not null,           /* course identifier */
  moduleid varchar(20) not null,           /* module identifier */
  seqno integer not null,                  /* task sequence number within the module */
  query_parameters varchar(1000),          /* generated parameters for the query */
  lasttrynumber integer not null,          /* the biggest try number for this task */
  currentresult integer not null,          /* the highest correctness value for this task */
  hassucceeded char,                       /* result in above cutoffvalue */
  wascreditedintime char,                  /* did the correctness value exceed the cutoff value before deadline (Y/N) */
  primary key (sid,courseid,moduleid,seqno),
  foreign key (sid) references eauser,
  foreign key (courseid, moduleid, seqno) references taskinmodule
);

