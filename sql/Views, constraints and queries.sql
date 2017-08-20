
ADD Check constraint that dates not future dates wherever appropriate
ADD check constraint that if minthresh is null then maxthresh is null and if one is not null then other is not null.
ADD check constraint that alerttype can only take 2 values;



write in report that, adding foreign key constraint in the diagnosis table to the primary hs table will ensure that each sick patient has primary HS.
We have not done this to allow  atient to add disease without having to add hs at the same time.

# Our alerts correctly handling  the upper limit lower limit in case of multiple diseases for a patient.


# Views
CREATE VIEW alerts AS ((SELECT AA.patientid, AA.alerttype, AA.generatedate, AA.message FROM activealerts AA) UNION (SELECT A.patientid, A.alerttype, A.generatedate, A.message FROM archivealerts A));
CREATE VIEW healthsupporters AS ((SELECT * FROM hasprimary) UNION (SELECT * FROM hassecondary));

CREATE VIEW sickpatients AS (SELECT DISTINCT(patientid) FROM diagnosis WHERE enddate IS NULL);

CREATE VIEW sickpatientanddisease AS (SELECT * FROM diagnosis WHERE enddate IS NULL);

CREATE VIEW patientobs AS (SELECT SPD.patientid, DO.obsname, DO.obstype, DO.lowerlimit, DO.upperlimit, DO.freq FROM sickpatientanddisease SPD , dhasobs DO WHERE DO.diseaseid = SPD.diseaseid AND DO.obsname NOT IN (SELECT SM.obsname FROM phassm SM WHERE SM.patientid = SPD.patientid) UNION (SELECT * FROM phassm))


#Queries for report:
SELECT count(*) FROM healthsupporters hs, diagnosis DI WHERE DI.patientid = hs.puserid AND DI.diseaseid='Heart Disease' AND to_char(hs.since, 'MM-YY') = '09-16';

SELECT COUNT(DISTINCT(AA.patientid)) FROM alerts AA WHERE AA.alerttype='low activity';

SELECT P.userid, P.fname, P.lname FROM person P WHERE P.userid IN (SELECT HS.hsuserid FROM healthsupporters HS);

SELECT P.userid, P.fname, P.lname FROM person P WHERE P.userid IN (SELECT P1.userid FROM patient P1) AND P.userid NOT IN (SELECT D.patientid FROM diagnosis D WHERE D.enddate IS NULL);

SELECT COUNT(DISTINCT(R.patientid)) FROM record R WHERE R.obsdate != R.recdate;

SELECT P.userid FROM patient P WHERE p.userid NOT IN (SELECT * FROM sickpatients);
 


#Check Constraints

ALTER TABLE patient ADD CONSTRAINT mthreshc CHECK ( (minthresh IS NULL AND maxthresh IS NULL) OR (minthresh IS NOT NULL AND maxthresh IS NOT NULL));
ALTER TABLE patient ADD CONSTRAINT threshc CHECK (minthres <= maxthresh);
ALTER TABLE activealerts ADD CONSTRAINT atypec CHECK (alerttype='low activity' OR alerttype='outside limit');
ALTER TABLE hasprimary ADD CONSTRAINT phsc CHECK (puserid <> hsuserid);
ALTER TABLE hassecondary ADD CONSTRAINT shsc CHECK (puserid <> shsuserid);

ALTER TABLE record ADD CONSTRAINT CHECK recdc CHECK (recdate >= obsdate);
ALTER TABLE diagnosis ADD CONSTRAINT CHECK sinceendc CHECK (enddate >= since);
ALTER TABLE archivealerts ADD CONSTRAINT CHECK cleardatec CHECK (cleardate >= generatedate);
ALTER TABLE phassm ADD CONSTRAINT llulc CHECK (lowerlimit <= upperlimit);
ALTER TABLE generalobs ADD CONSTRAINT gollulc CHECK (lowerlimit <= upperlimit);
ALTER TABLE dhasobs ADD CONSTRAINT dhollulc CHECK (lowerlimit <= upperlimit);
ALTER TABLE person ADD CONSTRAINT genderc CHECK (gender='M' OR gender='F');

