package io.getarrays.securecapita.roles.prerunner;

public enum AUTH_ROLE {
    USER,
    DEPUTY_REGISTRAR,
    REGISTRAR,
    ADMIN,
    PRINCIPAL_ADMIN,
    HEAD_ADMIN,
    HEAD_IT,
    DEPUTY_HEAD_IT ,
    SOFTWARE_DEVELOPER ,
    SYS_ANALYST ,
    ASSISTANT_SECRETARY,
  ICT_OFFICER ,
    SYSADMIN,
    AUDITOR,
    ASSISTANT_ADMIN,
    AdminCS,
    SECRETARY,
    HEADADMIN,
    DEPUTYHEADADMIN,

    ADMINOFFICER,


    ;

//which?
    public static int getMaxPriority() {
        return 5;
    }
}
//Initiation/Creation - by Administration Officer
//Approval - by Principal Administration Officer
//Confirmation - by Deputy Head of Administration
//Authorization - by Head of Administration
//Submission to PMU - by Head of Administration
//Completion - by Initiator (on receiving goods)
//if we change you have to edit in frontend,ok
//ok?yes