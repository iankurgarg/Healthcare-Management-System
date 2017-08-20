CREATE TABLE person (userid varchar(20), fname varchar(30) NOT NULL, lname varchar(30), dob date NOT NULL, gender varchar(1) NOT NULL, address varchar(100), phone varchar(11), password varchar(100) NOT NULL, PRIMARY KEY (userid));

INSERT INTO person VALUES('agarg12', 'Ankur', 'Garg', TO_DATE('01-29-1993','MM-DD-YYYY'), 'M', 'Raleigh', '9195237670', 'ankur123');
INSERT INTO person VALUES('rachit', 'Rachit', 'Shrivastava', TO_DATE('02-05-1992','MM-DD-YYYY'), 'M', 'Raleigh', '9195237670', 'ankur123');
INSERT INTO person VALUES('chinmoy', 'Chinmoy', 'Baruah', TO_DATE('12-05-1991','MM-DD-YYYY'), 'M', 'Raleigh', '9195237670', 'ankur123');
INSERT INTO person VALUES('sanket', 'Sanket', 'Shahane', TO_DATE('04-15-1994','MM-DD-YYYY'), 'M', 'Raleigh', '9195237670', 'ankur123');
INSERT INTO person VALUES('aneesh', 'Aneesh', 'Gupta', TO_DATE('07-12-1990','MM-DD-YYYY'), 'M', 'Raleigh', '9195237670', 'ankur123');

CREATE TABLE patient (userid varchar(20), PRIMARY KEY (userid), FOREIGN KEY(userid) REFERENCES person ON DELETE CASCADE);
INSERT INTO patient VALUES('rachit');
INSERT INTO patient VALUES('aneesh');
INSERT INTO patient VALUES('agarg12');


INSERT INTO patient (userid) VALUES('abs12');

CREATE TABLE hasp (patientid varchar(20), hsid varchar(20))

CREATE TABLE hashs (patientid varchar(20), hsid varchar(20), type int NOT NULL, since date NOT NULL, PRIMARY KEY (patientid, hsid), UNIQUE(patientid, type), FOREIGN KEY (patientid) REFERENCES patient(userid) ON DELETE CASCADE, FOREIGN KEY (hsid) REFERENCES person(userid) ON DELETE CASCADE);

ALTER TABLE hashs ADD CONSTRAINT maxhs CHECK (type=0 OR type=1);

CREATE OR REPLACE TRIGGER hsinsert BEFORE INSERT ON hashs FOR EACH ROW DECLARE ctype number; BEGIN SELECT COUNT(*) INTO ctype FROM hashs WHERE patientid=:new.patientid; :new.type := ctype; END;

CREATE OR REPLACE TRIGGER hsdelete AFTER DELETE ON hashs BEGIN UPDATE hashs H SET H.type=0 WHERE H.patientid=:old.patientid; END;


INSERT INTO hashs VALUES('agarg12', 'chinmoy', 0, TO_DATE('01-29-2016','MM-DD-YYYY'));



//conditional


IF EXISTS (SELECT * FROM ane) THEN (SELECT * FROM ane) ELSE (SELECT * FROM are) END
SELECT CASE WHEN (SELECT COUNT(*) FROM ane) > 0 THEN x.a AS value1 ELSE y.a AS value1 FROM ane x, are y;










// Not working
CREATE ASSERTION maxtwohs CHECK ( 0 = (SELECT COUNT(*) FROM (SELECT H.patientid FROM hashs H GROUP BY H.patientid HAVING CONUT(*) >  2)))

ALTER TABLE hashs ADD CONSTRAINT maxtwohs CHECK (NOT EXISTS (SELECT H1.patientid FROM hashs H1 GROUP BY H1.patientid HAVING COUNT(*) > 2));

//OTHER STUFF
CREATE TABLE t(a varchar(20), b int DEFAULT 0 AUTO_INCREMENT, PRIMARY KEY(a, b));
ALTER TABLE t ADD CONSTRAINT c1 CHECK (b=0 OR b=1);

CREATE OR REPLACE TRIGGER hsinsert AFTER INSERT ON hashs FOR EACH ROW DECLARE ptype number; BEGIN SELECT MIN(type) INTO ptype FROM hashs WHERE patientid=:new.patientid; UPDATE hashs SET type = CASE WHEN ptype <> 0 THEN 0 ELSE 1 END WHERE patientid=:new.patientid AND hsid=:new.hsid; END;