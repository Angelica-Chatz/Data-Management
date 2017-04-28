select count(Therapy_Sessions.ID)/ count(distinct Patient_Case.ID) 
as Avg_No_of_Therapy_Sessions_per_Case_out_of_all_recorded_cases
from Patient_Case left join Therapy_Sessions
on Patient_Case.ID= Therapy_Sessions.CaseID;