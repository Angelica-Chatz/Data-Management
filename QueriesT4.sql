select Outcome.Description as Outcome, avg(Therapy_Sessions.Duration) as Avg_Duration_of_Therapy_Sessions
from Therapy_Sessions, Outcome
where Therapy_Sessions.OutcomeID = Outcome.ID
and Therapy_Sessions.TherapyDate between '2015-01-01' and '2015-03-31'
group by Outcome.Description;
