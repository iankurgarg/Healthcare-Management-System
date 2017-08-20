# TRIGGERS

create or replace trigger movetoarchive before delete on activealerts for each row begin insert into archivealerts values (:old.patientid,:old.alerttype,:old.generatedate,:old.message,SYSDATE); end;


CREATE OR replace PROCEDURE create_alert IS
count1 number;
count2 number;
count3 number;
count4 varchar(100);
CURSOR patients IS SELECT P.userid FROM patient P;
BEGIN
count2 := 4;
DBMS_OUTPUT.PUT_LINE(''||count2);
FOR pid in patients
LOOP
SELECT COUNT(*) INTO count2 FROM (SELECT * FROM (SELECT * FROM record R WHERE R.patientid=pid.userid ORDER BY R.obsdate DESC) R1 WHERE ROWNUM <= ANY (SELECT P.maxthresh FROM patient P WHERE P.userid=pid.userid)) R2, ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = pid.userid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=pid.userid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = pid.userid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = pid.userid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =pid.userid))) PO WHERE R2.obsname = PO.obsname AND (R2.obsvalue > PO.upperlimit OR R2.obsvalue < PO.lowerlimit);
SELECT LISTAGG(R2.obsname,',') WITHIN GROUP (ORDER BY R2.obsname) AS obsnames INTO count4 FROM (SELECT * FROM (SELECT * FROM record R WHERE R.patientid=pid.userid ORDER BY R.obsdate DESC) R1 WHERE ROWNUM <= ANY (SELECT P.maxthresh FROM patient P WHERE P.userid=pid.userid)) R2, ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = pid.userid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=pid.userid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = pid.userid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = pid.userid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =pid.userid))) PO WHERE R2.obsname = PO.obsname AND (R2.obsvalue > PO.upperlimit OR R2.obsvalue < PO.lowerlimit);
SELECT COUNT(*) INTO count3 FROM activealerts A WHERE A.patientid = pid.userid AND A.alerttype = 'outside limit';
SELECT P.minthresh INTO count1 FROM patient P WHERE P.userid=pid.userid;
IF count2 >= count1 AND count3 = 0 THEN
insert into activealerts(patientid,alerttype,generatedate,message) values(pid.userid,'outside limit',SYSDATE,count4);
END IF;
END LOOP;
END;


#old
CREATE OR replace PROCEDURE create_alert2 IS
count1 varchar(100);
count2 number;
count3 number;
CURSOR patients IS SELECT P.userid FROM patient P;
BEGIN
count2 := 4;
DBMS_OUTPUT.PUT_LINE(''||count2);
FOR pid in patients
LOOP
SELECT COUNT(*) INTO count2 FROM ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = pid.userid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=pid.userid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = pid.userid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = pid.userid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =pid.userid))) R1, (SELECT R.obsname, R.obstype, MAX(R.obsdate) AS obsdate FROM record R WHERE R.patientid=pid.userid GROUP BY (R.obsname, R.obstype)) R2 WHERE R1.obsname = R2.obsname AND R1.obstype = R2.obstype AND R2.obsdate <= sysdate-R1.freq;
SELECT LISTAGG(obsname,',') WITHIN GROUP (ORDER BY obsname) AS obsnames INTO count1 FROM (SELECT DISTINCT(R2.obsname) FROM ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = pid.userid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=pid.userid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = pid.userid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = pid.userid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =pid.userid))) R1, (SELECT R.obsname, R.obstype, MAX(R.obsdate) AS obsdate FROM record R WHERE R.patientid=pid.userid GROUP BY (R.obsname, R.obstype)) R2 WHERE R1.obsname = R2.obsname AND R1.obstype = R2.obstype AND R2.obsdate <= sysdate-R1.freq);
SELECT COUNT(*) INTO count3 FROM activealerts A WHERE A.patientid = pid.userid AND A.alerttype = 'low activity' AND A.message=count1;
IF count2 > 0 AND count3 = 0 THEN
insert into activealerts(patientid,alerttype,generatedate,message) values(pid.userid,'low activity',SYSDATE,count1);
END IF;
END LOOP;
END;


#new
CREATE OR replace PROCEDURE create_alert2 IS
count1 varchar(100);
count2 number;
count3 number;
CURSOR patients IS SELECT P.userid FROM patient P;
BEGIN
count2 := 4;
DBMS_OUTPUT.PUT_LINE(''||count2);
FOR pid in patients
LOOP
SELECT COUNT (*) INTO count2 FROM ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = pid.userid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=pid.userid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = pid.userid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = pid.userid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =pid.userid))) R5 WHERE R5.obsname NOT IN (SELECT R1.obsname FROM ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = pid.userid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=pid.userid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = pid.userid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = pid.userid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =pid.userid))) R1, (SELECT R.obsname, R.obstype, MAX(R.obsdate) AS obsdate FROM record R WHERE R.patientid=pid.userid GROUP BY (R.obsname, R.obstype)) R2 WHERE R1.obsname = R2.obsname AND R1.obstype = R2.obstype AND R2.obsdate > sysdate-R1.freq);

SELECT LISTAGG(obsname,',') WITHIN GROUP (ORDER BY obsname) AS obsnames INTO count1 FROM (SELECT DISTINCT(R5.obsname) FROM ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = pid.userid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=pid.userid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = pid.userid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = pid.userid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =pid.userid))) R5 WHERE R5.obsname NOT IN (SELECT R1.obsname FROM ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = pid.userid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=pid.userid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = pid.userid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = pid.userid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =pid.userid))) R1, (SELECT R.obsname, R.obstype, MAX(R.obsdate) AS obsdate FROM record R WHERE R.patientid=pid.userid GROUP BY (R.obsname, R.obstype)) R2 WHERE R1.obsname = R2.obsname AND R1.obstype = R2.obstype AND R2.obsdate > sysdate-R1.freq));
SELECT COUNT(*) INTO count3 FROM activealerts A WHERE A.patientid = pid.userid AND A.alerttype = 'low activity';
IF count2 > 0 AND count3 = 0 THEN
insert into activealerts(patientid,alerttype,generatedate,message) values(pid.userid,'low activity',SYSDATE,count1);
END IF;
END LOOP;
END;


SELECT COUNT (*) INTO count2 FROM ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = pid.userid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=pid.userid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = pid.userid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = pid.userid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =pid.userid))) R5 WHERE R5.obsname NOT IN (SELECT R1.obsname FROM ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = pid.userid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=pid.userid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = pid.userid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = pid.userid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =pid.userid))) R1, (SELECT R.obsname, R.obstype, MAX(R.obsdate) AS obsdate FROM record R WHERE R.patientid=pid.userid GROUP BY (R.obsname, R.obstype)) R2 WHERE R1.obsname = R2.obsname AND R1.obstype = R2.obstype AND R2.obsdate > sysdate-R1.freq);


create or replace trigger checkdiagnosis before insert on diagnosis
for each row
declare
count2 integer;
counter integer;
edate date;
begin
select count(*) into counter from diagnosis where patientid = :new.patientid and diseaseid=:new.diseaseid;
if(counter <> 0) then
	select count(*) into count2 from diagnosis where patientid = :new.patientid AND diseaseid = :new.diseaseid AND enddate IS NULL;
	if(count2 <> 0) then
		raise_application_error(-20001,'You already have that disease');
	elsif (count2 = 0) then
		select d1.enddate into edate from diagnosis d1 where d1.patientid = :new.patientid and d1.diseaseid=:new.diseaseid and d1.since = (select max(d2.since) from diagnosis d2 where d2.patientid = :new.patientid and d2.diseaseid = :new.diseaseid);
		if (edate > :new.since) then
			raise_application_error(-20001,'You already have that disease');
		end if;
	end if;
end if;
END;



CREATE OR REPLACE TRIGGER evaluate_low_activity_alert
 BEFORE INSERT ON record 
  FOR EACH ROW
DECLARE
count1 number;
count2 number;
count3 number;
BEGIN
SELECT COUNT(*) INTO count1 FROM activealerts A WHERE A.patientid = :new.patientid AND A.alerttype = 'low activity' AND A.message = :new.obstype;
SELECT COUNT(*) INTO count2 FROM activealerts A WHERE A.patientid = :new.patientid AND A.alerttype = 'low activity';
SELECT COUNT(*) INTO count3 FROM activealerts A WHERE A.patientid = :new.patientid AND A.alerttype = 'low activity' AND A.message LIKE CONCAT(CONCAT('%', :new.obstype), '%');
IF count2 > 0 AND count1 > 0 THEN
	Delete from activealerts where patientid=:new.patientid AND alerttype='low activity' AND message = :new.obsname;
ELSE 
IF count2 > 0 AND count3 > 0 THEN
    UPDATE activealerts SET message = REPLACE(message, CONCAT(:new.obstype, ','));
END IF;
END IF;
END;

CREATE OR REPLACE TRIGGER evaluate_outside_limit_alert
BEFORE INSERT ON record
FOR EACH ROW
DECLARE
count1 number;
count2 number;
ulimit float;
llimit float;
maxt number;
temp1 number;
temp2 number;
BEGIN
SELECT COUNT(*) INTO count2 FROM (SELECT * FROM (SELECT * FROM record R WHERE R.patientid=:new.patientid ORDER BY R.obsdate DESC) R1 WHERE ROWNUM < ANY (SELECT P.maxthresh FROM patient P WHERE P.userid=:new.patientid)) R2, ((select hspm.obsname,hspm.obstype,hspm.upperlimit,hspm.lowerlimit,hspm.freq from phassm hspm where hspm.patientid = :new.patientid) UNION (SELECT DHO.obsname, DHO.obstype, DHO.upperlimit, DHO.lowerlimit, DHO.freq FROM dhasobs DHO WHERE DHO.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid=:new.patientid) AND DHO.obsname NOT IN (select hspm.obsname from phassm hspm where hspm.patientid = :new.patientid)) UNION (SELECT GO.obsname, GO.obstype, GO.upperlimit, GO.lowerlimit, GO.freq FROM generalobs GO WHERE GO.obsname NOT IN (SELECT D.obsname FROM dhasobs D WHERE D.diseaseid IN (SELECT DI.diseaseid FROM diagnosis DI WHERE DI.patientid = :new.patientid)) AND GO.obsname NOT IN (SELECT SR.obsname FROM phassm SR WHERE SR.patientid =:new.patientid))) PO WHERE R2.obsname = PO.obsname AND (R2.obsvalue > PO.upperlimit OR R2.obsvalue < PO.lowerlimit);
SELECT COUNT(*) INTO temp1 FROM patientobs PO WHERE PO.patientid =:new.patientid AND PO.obsname = :new.obsname AND PO.obstype = :new.obstype;
IF temp1 > 0 THEN
	SELECT PO.lowerlimit, PO.upperlimit INTO llimit, ulimit FROM patientobs PO WHERE PO.patientid = :new.patientid AND PO.obsname = :new.obsname AND PO.obstype = :new.obstype;
	IF :new.obsvalue < llimit OR :new.obsvalue > ulimit THEN
		count2:= count2+1;
	END IF;
ELSE
	SELECT COUNT(*) INTO temp2 FROM generalobs GO WHERE GO.obsname = :new.obsname;
	IF temp2 > 0 THEN
	SELECT GO.lowerlimit, GO.upperlimit INTO llimit, ulimit FROM generalobs GO WHERE GO.obsname = :new.obsname AND GO.obstype = :new.obstype;
		IF :new.obsvalue < llimit OR :new.obsvalue > ulimit THEN
		count2:= count2+1;
		END IF;
	END IF;
END IF;
SELECT P.maxthresh INTO maxt FROM patient P WHERE P.userid = :new.patientid;
IF count2 < maxt THEN
DELETE FROM activealerts WHERE alerttype='outside limit' AND patientid = :new.patientid;
END IF;
END;
