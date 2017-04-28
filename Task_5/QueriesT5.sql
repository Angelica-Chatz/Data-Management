select Diagnosis_Categories.CategoryName as Diagnosis_Category, count(Patient_Case.ID) as Recorded_Cases_per_Category
from Patient_Case, Diagnosis, Diagnosis_Categories
where Diagnosis.ID in (select Patient_Case.DiagnosisIDs from Patient_Case)
and Diagnosis.Diagnosis_CategoriesID = Diagnosis_Categories.ID
and Patient_Case.FirstContactDate between '2014-01-01' and '2014-08-31'
group by Diagnosis_Categories.CategoryName
order by count(Patient_Case.ID) desc, Diagnosis_Categories.CategoryName;