CREATE TABLE person (userid VARCHAR(30),fname VARCHAR(30) NOT NULL, lname VARCHAR(30), dob DATE NOT NULL, gender VARCHAR(1) NOT NULL, address VARCHAR(100), phone VARCHAR(10), password VARCHAR(100), PRIMARY KEY (userid));

CREATE TABLE patient (userid VARCHAR(30), minthresh number, maxthresh number, PRIMARY KEY (userid), FOREIGN KEY (userid) REFERENCES person ON DELETE CASCADE);

CREATE TABLE hasprimary (puserid VARCHAR(30), hsuserid VARCHAR(30) NOT NULL, since DATE NOT NULL, PRIMARY KEY (puserid), FOREIGN KEY (hsuserid) REFERENCES person, FOREIGN KEY (puserid) REFERENCES patient ON DELETE CASCADE);

CREATE TABLE hassecondary (puserid VARCHAR(30), shsuserid VARCHAR(30) NOT NULL, since DATE NOT NULL, PRIMARY KEY (puserid), FOREIGN KEY (puserid) REFERENCES hasprimary(puserid), FOREIGN KEY (shsuserid) REFERENCES person ON DELETE CASCADE);

CREATE TABLE disease (diseaseid VARCHAR(30), PRIMARY KEY(diseaseid));

CREATE TABLE diagnosis (patientid VARCHAR(30), diseaseid VARCHAR(30), since DATE NOT NULL, enddate DATE, PRIMARY KEY (patientid, diseaseid), foreign key (patientid) references patient(userid) ON DELETE CASCADE, foreign key (diseaseid) references disease(diseaseid) ON DELETE CASCADE);

#CREATE TABLE observation (obsname VARCHAR(30), description VARCHAR(100), PRIMARY KEY(obsname));

CREATE TABLE measure (obsname VARCHAR(30), obstype VARCHAR(30), PRIMARY KEY (obsname,obstype));

CREATE TABLE dhasobs (diseaseid VARCHAR(30), obsname VARCHAR(30), obstype VARCHAR(30), lowerlimit float, upperlimit  float,freq integer, PRIMARY KEY(diseaseid,obsname, obstype), FOREIGN KEY (diseaseid) REFERENCES disease(diseaseid) ON DELETE CASCADE, FOREIGN KEY (obsname, obstype) REFERENCES measure(obsname, obstype) ON DELETE CASCADE);

CREATE TABLE generalobs (obsname VARCHAR(30), obstype VARCHAR(30), lowerlimit float, upperlimit float, freq integer, PRIMARY KEY(obsname, obstype), FOREIGN KEY (obsname, obstype) REFERENCES measure(obsname, obstype) ON DELETE CASCADE); 

CREATE TABLE phassm (patientid VARCHAR(30), obsname VARCHAR(30), obstype VARCHAR(30), lowerlimit FLOAT, upperlimit FLOAT, freq INTEGER NOT NULL, PRIMARY KEY (patientid,obsname,obstype), FOREIGN KEY (patientid) REFERENCES patient(userid) ON DELETE CASCADE, FOREIGN KEY (obsname,obstype) REFERENCES measure(obsname,obstype) ON DELETE CASCADE);

Create table record (patientid varchar(30) not null,obsname varchar(30) not null,obstype varchar(30), obsdate timestamp not null, obsvalue float not null, recdate timestamp not null, primary key(patientid,obsname,obstype,obsdate), foreign key (patientid) references patient(userid) ON DELETE CASCADE, foreign key (obsname,obstype) references measure(obsname,obstype) ON DELETE CASCADE);


create table activealerts (patientid varchar(30), alerttype varchar(30),generatedate date,message varchar(100), primary key(patientid,alerttype),foreign key (patientid) references patient(userid) ON DELETE CASCADE);

create table archivealerts (patientid varchar(30), alerttype varchar(30),generatedate date,message varchar(100),cleardate timestamp,primary key (patientid,alerttype,cleardate), foreign key (patientid) references patient(userid) ON DELETE CASCADE);


#Create table measure (obsname varchar(30), obstype varchar(30), freq integer not null, upperlimit float not null, lowerlimit float not null, primary key (obsname,obstype), foreign key (obsname) references observation(obsname))

#New measure
Create table measure (obsname varchar(30), obstype varchar(30), freq integer not null, upperlimit float not null, lowerlimit float not null, primary key (obsname,obstype))
ALTER TABLE dhasobs DROP CONSTRAINT SYS_C00266787;
ALTER TABLE dhasobs ADD obstype varchar(30);
ALTER TABLE dhasobs ADD CONSTRAINT prkey PRIMARY KEY(diseaseid, obsname,obstype)
ALTER TABLE dhasobs ADD CONSTRAINT obsforkey FOREIGN KEY(obsname, obstype) REFERENCES measure(obsname, obstype)
ALTER TABLE dhasobs ADD lowerlimit float NOT NULL

ALTER TABLE dhasobs  MODIFY (lowerlimit float CONSTRAINT llnl NOT NULL); 
ALTER TABLE dhasobs  MODIFY (upperlimit float CONSTRAINT ulnl NOT NULL); 
ALTER TABLE dhasobs  MODIFY (freq integer CONSTRAINT fnl NOT NULL); 

ALTER TABLE measure DROP COLUMN lowerlimit;
ALTER TABLE measure DROP COLUMN upperlimit;
ALTER TABLE measure DROP COLUMN freq;

CREATE TABLE generalobs (obsname VARCHAR(30), obstype VARCHAR(30), lowerlimit float NOT NULL, upperlimit float NOT NULL, freq INTEGER NOT NULL, PRIMARY KEY(obsname, obstype), FOREIGN KEY (obsname, obstype) REFERENCES measure(obsname, obstype)); 


Create table phassm (patientid varchar(30), obsname varchar(30), obstype varchar(30), freq integer not null, upperlimit float not null, lowerlimit float not null, primary key (patientid,obsname,obstype), foreign key (patientid) references patient(userid), foreign key (obsname,obstype) references measure(obsname,obstype))



ALTER TABLE patient ADD minthresh number;
ALTER TABLE patient ADD maxthresh number;




#We may need to move the upper limit, lower limit and frequency to DHasObs ---- as a disease may have specific  upper limit, lower limit and frequency. i think ?
# and move the default upper limit and lower limit and frequency to GeneralObs.
# Our query will then be to first get the specific .. then the ones that are there for that disease and then the ones that are for well patients.

#INSERT

INSERT INTO PERSON VALUES('P1', 'Sheldon', 'Cooper', TO_DATE('05/26/1984','MM/DD/YYYY'), 'M', '2500 Sacramento, Apt 903, Santa Cruz, CA - 90021', NULL, 'password');
INSERT INTO PERSON VALUES('P2', 'Leonard', 'Hofstader', TO_DATE('04/19/1989','MM/DD/YYYY'), 'M', '2500 Sacramento, Apt 904, Santa Cruz, CA - 90021', NULL, 'password');
INSERT INTO PERSON VALUES('P3', 'Penny', 'Hofstader', TO_DATE('12/25/1990','MM/DD/YYYY'), 'F', '2500 Sacramento, Apt 904, Santa Cruz, CA - 90021', NULL, 'password');
INSERT INTO PERSON VALUES('P4', 'Amy', 'Farrahfowler', TO_DATE('6/15/1992','MM/DD/YYYY'), 'F', '2500 Sacramento, Apt 905, Santa Cruz, CA - 90021', NULL, 'password');

INSERT INTO PATIENT VALUES('P1', 1, 1);
INSERT INTO PATIENT VALUES('P2', 1, 1);
INSERT INTO PATIENT VALUES('P3', 1, 1);
INSERT INTO PATIENT VALUES('P4', 1, 1);


INSERT INTO hasprimary VALUES ('P1', 'P2', TO_DATE('10-21-2016', 'MM-DD-YYYY'));
INSERT INTO hasprimary VALUES ('P2', 'P3', TO_DATE('10-09-2016', 'MM-DD-YYYY'));



INSERT INTO DISEASE VALUES('Heart Disease');
INSERT INTO DISEASE VALUES('HIV');
INSERT INTO DISEASE VALUES('COPD');


INSERT INTO diagnosis VALUES('P1','Heart Disease',TO_DATE('10-22-2016', 'MM-DD-YYYY'), NULL);
INSERT INTO diagnosis VALUES('P2','HIV',TO_DATE('10-10-2016', 'MM-DD-YYYY'), NULL);


INSERT INTO MEASURE VALUES ('Weight', 'Weight');
INSERT INTO MEASURE VALUES ('Blood Pressure', 'Systolic');
INSERT INTO MEASURE VALUES ('Blood Pressure', 'Diastolic');
INSERT INTO MEASURE VALUES ('Oxygen Saturation', 'Oxygen Saturation');
INSERT INTO MEASURE VALUES ('Pain', 'Pain');
INSERT INTO MEASURE VALUES ('Mood', 'Mood');
INSERT INTO MEASURE VALUES ('Temperature', 'Temperature');

INSERT INTO dhasobs VALUES('Heart Disease', 'Weight', 'Weight', 120, 200, 7);
INSERT INTO dhasobs VALUES('Heart Disease', 'Blood Pressure', 'Systolic', 140, 159, 1);
INSERT INTO dhasobs VALUES('Heart Disease', 'Blood Pressure', 'Diastolic', 90, 99, 1);
INSERT INTO dhasobs VALUES('Heart Disease', 'Mood', 'Mood', 1, 1, 7);

INSERT INTO dhasobs VALUES('HIV', 'Weight', 'Weight', 120, 200, 7);
INSERT INTO dhasobs VALUES('HIV', 'Pain', 'Pain', 1, 5, 1);
INSERT INTO dhasobs VALUES('HIV', 'Blood Pressure', 'Systolic', NULL, NULL, 1);
INSERT INTO dhasobs VALUES('HIV', 'Blood Pressure', 'Diastolic', NULL, NULL, 1);

INSERT INTO dhasobs VALUES ('COPD', 'Oxygen Saturation', 'Oxygen Saturation', 90, 99, 1);
INSERT INTO DHASOBS VALUES ('COPD', 'Temperature', 'Temperature', 95, 100, 1);

INSERT INTO generalobs VALUES ('Weight', 'Weight', 120, 200, 7);


INSERT INTO PHASSM VALUES ('P2', 'Weight', 'Weight', 120, 190, 7);
INSERT INTO PHASSM VALUES ('P2', 'Pain', 'Pain', 1, 5, 1);
INSERT INTO PHASSM VALUES ('P2', 'Blood Pressure', 'Systolic', NULL, NULL, 1);
INSERT INTO PHASSM VALUES ('P2', 'Blood Pressure', 'Diastolic', NULL, NULL, 1);

INSERT INTO RECORD VALUES ('P2','Weight', 'Weight', TO_TIMESTAMP('10/10/2016 00:00:00', 'mm/dd/yyyy hh24/mi/ss'), 180, TO_TIMESTAMP('10/11/2016 00:00:00', 'mm/dd/yyyy hh24/mi/ss'));
INSERT INTO RECORD VALUES ('P2','Weight', 'Weight', TO_TIMESTAMP('10/17/2016 00:00:00', 'mm/dd/yyyy hh24/mi/ss'), 195, TO_TIMESTAMP('10/17/2016 00:00:00', 'mm/dd/yyyy hh24/mi/ss'));



//For finding all obs types valid for a patient;

(select hspm.obsname,hspm.obstype from phassm hspm where hspm.patientid = 'P2') UNION (select m.obsname,m.obstype from measure m where m.obsname IN ( select do.obsname from dhasobs do where do.diseaseid IN ( select diag.diseaseid from diagnosis diag where diag.patientid = 'P2')) MINUS select hspm.obsname,hspm.obstype from phassm hspm where hspm.patientid = 'P2')



(select hspm.obsname,hspm.obstype,hspm.freq,hspm.upperlimit,hspm.lowerlimit from phassm hspm where hspm.patientid = 'P3') UNION (select final.obsname,final.obstype,final.freq,final.upperlimit,final.lowerlimit from measure final,((select m.obsname,m.obstype from measure m where m.obsname IN ( (select do.obsname from dhasobs do where do.diseaseid IN ( select diag.diseaseid from diagnosis diag where diag.patientid = 'P3')) UNION (SELECT GO.obsname FROM generalobs GO) )) MINUS (select hspm.obsname,hspm.obstype from phassm hspm where hspm.patientid = 'P3')) Q where final.obsname = Q.obsname and final.obstype = Q.obstype)


SELECT P.userid FROM patient P WHERE EXISTS ((select hspm.obsname,hspm.obstype from phassm hspm where hspm.patientid = P.userid) UNION (select m.obsname,m.obstype from measure m where m.obsname IN ( select do.obsname from dhasobs do where do.diseaseid IN ( select diag.diseaseid from diagnosis diag where diag.patientid = P.userid)) MINUS select hspm.obsname,hspm.obstype from phassm hspm where hspm.patientid = P.userid))

EXISTS (select hspm.obsname,hspm.obstype,hspm.freq,hspm.upperlimit,hspm.lowerlimit from phassm hspm where hspm.patientid = P.userid) OR

SELECT P.userid FROM patient P WHERE EXISTS(select final.obsname,final.obstype,final.freq,final.upperlimit,final.lowerlimit from measure final,((select m.obsname,m.obstype from measure m where m.obsname IN ( select do.obsname from dhasobs do where do.diseaseid IN ( select diag.diseaseid from diagnosis diag where diag.patientid = P.userid))) MINUS (select hspm.obsname,hspm.obstype from phassm hspm where hspm.patientid = P.userid)) Q where final.obsname = Q.obsname and final.obstype = Q.obstype)

//FOR ALERTS;

CREATE ALERTS (patientid varchar(30), typestr varchar(50), message varchar(100), PRIMARY KEY(patientid, typestr), FOREIGN KEY patientid REFERENCES patient(userid) ON DELETE CASCADE, CHECK(typestr='Outside the limit' OR type='Low Activity'))

For creating alerts;


FOR FINDING All out the limit records of a given patient:



SELECT COUNT(*)
FROM
(SELECT * FROM (SELECT * FROM record R WHERE R.patientid='P2' ORDER BY R.obsdate DESC) AS R1 WHERE ROWNUM <= ANY (SELECT P.maxthresh FROM patient P WHERE P.userid='P2')) AS R2, (list of obs for the patient P1 along witht the upper limit and lower limits.) as PO
WHERE
R2.obsname = P0.obsname
AND (R2.obsvalue > PO.upperlimit
OR R2.obsvalue < PO.lowerlimit)


SELECT COUNT(*)
FROM PATIENT P
WHERE 
0 < (
SELECT COUNT(*)
FROM
(SELECT * FROM (SELECT * FROM record R WHERE R.patientid='P1' ORDER BY R.obsdate DESC) AS R1 WHERE ROWNUM <= ANY (SELECT P.thresholdlimit FROM patient P WHERE P.userid='P1')) AS R2, (list of obs for the patient P1 along witht the upper limit and lower limits.) as PO
WHERE
R2.obsname = P0.obsname
AND (R2.obsvalue > PO.upperlimit
OR R2.obsvalue < PO.lowerlimit))

------------------------




//CONSTRAINTS:

CHECK person not his own HS
CHECK Same person not primary and secondary ? - for this may need to go back to multiple rows in secondary.
