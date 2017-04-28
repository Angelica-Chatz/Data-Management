package com.company;

import java.util.*;
import java.sql.*;
public class Main {

    private static final String DatabaseName = "DB_Test";
    public static void main(String[] args) {
        System.out.println("Press 1 for Task 8");
        System.out.println("Press 2 for Task 9");
        String MenuValue = "";
        while (!MenuValue.equals("0"))
        {
            MenuValue = new Scanner(System.in).nextLine();
            switch (MenuValue)
            {
                case "1":
                    System.out.println("Insert a year (4 digit number)");
                    Task_8(new Scanner(System.in).nextLine());


                    break;
                case "2":
                    System.out.println("Insert code of a region");
                    //Only with REG_005
                    Task_9(new Scanner(System.in).nextLine());
                    break;
                default:
                    break;
            }
        }

    }




    private static void Task_8(String paramDate)
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }
        Connection conn = null;

        try{
            conn = DriverManager.getConnection("jdbc:mysql://83.212.116.222:3306/DB_Test", "root", "");


            if(conn != null){
                Statement stmt = conn.createStatement();
                stmt.execute("use "+DatabaseName);
                ResultSet rs = stmt.executeQuery("SELECT "
                        + "CASE WHEN Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) < 15 THEN 'Very severely underweight'"
                        + " WHEN Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) >= 15 AND Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) < 16 "
                        + "THEN 'Severely underweight' "
                        + "WHEN Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) >= 16 AND Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) < 18.5 "
                        + "THEN 'Underweight' "
                        + "WHEN Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) >= 18.5 AND Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) < 25 "
                        + "THEN 'Normal weight' "
                        + "WHEN Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) >= 25 AND Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) < 30 "
                        + "THEN 'Overweight' "
                        + "WHEN Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) >= 30 AND Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) < 35 "
                        + "THEN 'Obese Class I (Moderately obese)' "
                        + "WHEN Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) >= 35 AND Weight_KG / ( (Height_CM / 100) * (Height_CM / 100)) < 40 "
                        + "THEN 'Obese Class II (Severely obese)' "
                        + "ELSE 'Obese Class III (Very severely obese)' "
                        + "END AS `BMI Category` , AVG(Patients.Income) AS `Average Income` "
                        + "FROM Patients, Patient_Case "
                        + "WHERE Patient_Case.PatientID = Patients.ID "
                        + "AND Patient_Case.FirstContactDate BETWEEN '2005-01-01' AND '2015-12-31' "
                        + "AND YEAR(Patients.BirthDate) = " + paramDate + " "
                        + "GROUP BY `BMI Category`;");


                while (rs.next())
                {
                    System.out.printf("The average income of patients who belong to %1$s BMI Category is %2$s " + "\r\n", rs.getString("BMI Category"), (new Double(rs.getDouble("Average Income"))));

                }
            }


        }catch(SQLException ex){

            ex.printStackTrace();
        }


    }

    private static void Task_9(String paramRegion)
    {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://83.212.116.222:3306/DB_Test", "root", "");
            Statement stmt = conn.createStatement();
            if(conn != null){

                stmt.execute("use " + DatabaseName);}

                //Get Region ID

                ResultSet rs = stmt.executeQuery("SELECT ID FROM Region WHERE RegionName='" + paramRegion + "'");
                System.out.println("Execution OK=> SELECT ID FROM Region WHERE RegionName='" + paramRegion + "'");
                rs.next();
                int RegionID = (short)rs.getInt(1);

                //GEt All Towns where is the selected Region ID
                ResultSet DTTownIDs = stmt.executeQuery("SELECT ID FROM Town WHERE RegionID=" + RegionID + "");
                System.out.println("Execution OK=> SELECT ID FROM Town WHERE RegionID=" + RegionID + "");
                ArrayList<String> TownsIdList = new ArrayList<String>();
                while (DTTownIDs.next())
                {
                    TownsIdList.add(DTTownIDs.getString("ID"));
                }
                String IdsTowns = String.join(",", TownsIdList);

                //Get All Addresses Which belong to the Selected TownIDs
            ResultSet DTAddressesIDs = stmt.executeQuery("SELECT ID FROM Addresses WHERE TownID IN (" + IdsTowns + ")");
            System.out.println("Execution OK=> SELECT ID FROM Addresses WHERE TownID IN (" + IdsTowns + ")");
            ArrayList<String> AddressesIDsList = new ArrayList<String>();
            while (DTAddressesIDs.next())
            {
                AddressesIDsList.add(DTAddressesIDs.getString("ID"));
            }
            String IdsAddresses = String.join(",", AddressesIDsList);

            ResultSet DTPatientsIDs = stmt.executeQuery("SELECT ID, HPW  FROM Patients WHERE AddressID IN (" + IdsAddresses + ") AND Gender=1 AND Marital_StatusID=1");
            System.out.println("Execution OK=> SELECT ID, HPW  FROM Patients WHERE AddressID IN (" + IdsAddresses + ") AND Gender=1 AND Marital_StatusID=1");
            ArrayList<Patients> AllPatientsList = new ArrayList<Patients>();
            while (DTPatientsIDs.next())
            {
                Patients p = new Patients();
                p.setID((Integer)DTPatientsIDs.getInt("ID") );
                p.setHPW((Integer)DTPatientsIDs.getInt("HPW"));
                AllPatientsList.add(p);
            }

            //GET The id of Diagnosis_Categories

            ResultSet Diagnosis_CategoriesIDDT = stmt.executeQuery("SELECT ID FROM Diagnosis_Categories WHERE CategoryName='Mood Disorders'");
            System.out.println("Execution OK=> SELECT ID FROM Diagnosis_Categories WHERE CategoryName='Mood Disorders'");
            Diagnosis_CategoriesIDDT.next();
            int Diagnosis_CategoriesID = (short)Diagnosis_CategoriesIDDT.getInt(1);

            //Get All Diagnosis Where Category = Diagnosis_CategoriesID selected
            ResultSet DTDiagnosisIDs = stmt.executeQuery( "SELECT ID FROM Diagnosis WHERE Diagnosis_CategoriesID=" + Diagnosis_CategoriesID + "");
            System.out.println( "Execution OK=> SELECT ID FROM Diagnosis WHERE Diagnosis_CategoriesID=" + Diagnosis_CategoriesID + "");
            //Get DiagnosisIDs Content from Patient_Case
            String SQLBuilderPatientDiaID =  "SELECT DISTINCT (PatientID) AS Result FROM Patient_Case WHERE ";

            while (DTDiagnosisIDs.next())
            {
                SQLBuilderPatientDiaID +=" DiagnosisIDs LIKE '%," + DTDiagnosisIDs.getString("ID") + ",%' OR DiagnosisIDs LIKE '" + DTDiagnosisIDs.getString("ID") + ",%' OR DiagnosisIDs LIKE '%," + DTDiagnosisIDs.getString("ID") + "'  OR DiagnosisIDs = '" + DTDiagnosisIDs.getString("ID") + "' OR";
            }
            SQLBuilderPatientDiaID = SQLBuilderPatientDiaID.substring(0, SQLBuilderPatientDiaID.length() - 3);

            ResultSet DTPatient_CaseIDs = stmt.executeQuery(SQLBuilderPatientDiaID);
            System.out.println("Execution OK=> " + SQLBuilderPatientDiaID);
            //Filtre the PatienIDs
            ArrayList<Patients> FilteredListPatients = new ArrayList<Patients>();
            while (DTPatient_CaseIDs.next())
            {
                Patients matches = null;
                for (Patients  p : AllPatientsList) {
                    if (p.ID == (Integer)DTPatient_CaseIDs.getInt("Result")) {
                        matches = (p);
                        break; // optional
                    }
                }
                if (matches != null)
                {
                    FilteredListPatients.add(matches);
                }
            }

            String SQLBuilderParentIDs  = "SELECT ID , ParentIDs FROM Patients_Childrens WHERE ";

            for (Patients it : FilteredListPatients)
            {
                SQLBuilderParentIDs += " ParentIDs LIKE '%," + it.getID() + ",%' OR ParentIDs LIKE '" + it.getID() + ",%' OR ParentIDs LIKE '%," + it.getID() + "'  OR ParentIDs = '" + it.getID() + "' OR";
            }
            SQLBuilderParentIDs = SQLBuilderParentIDs.substring(0, SQLBuilderParentIDs.length() - 3);
            ResultSet DTChildrensIDs = stmt.executeQuery(SQLBuilderParentIDs);
            System.out.println("Execution OK=> " + SQLBuilderParentIDs);
            ArrayList<PatientsAndChildrensCount> TempListCountChild = new ArrayList<PatientsAndChildrensCount>();

            while (DTChildrensIDs.next())
            {
                PatientsAndChildrensCount o = new PatientsAndChildrensCount();
                o.setChildrenID((Integer)DTChildrensIDs.getInt("ID"));
                //String[] tmp = DTChildrensIDs.getString("ParentIDs").split(",");
                String[] tmp = String.valueOf(DTChildrensIDs.getString("ParentIDs")).split("[,]", -1);
                if (tmp.length == 2)
                {
                    o.setParentID_1(Integer.parseInt(tmp[0]));
                    o.setParentID_2(Integer.parseInt(tmp[1]));
                }
                else
                {
                    o.setParentID_1(Integer.parseInt(tmp[0]));
                    o.setParentID_2(null);
                }
                TempListCountChild.add(o);
               // System.out.println(o.getParentID_1() + " " + o.getParentID_2());
            }
            ArrayList<Patients> FinalList = new ArrayList<Patients>();
            for (Patients it : FilteredListPatients) {
                int countChildren = 0;
                for (PatientsAndChildrensCount  p : TempListCountChild ) {
                    if (p.ParentID_1 == (Integer)it.getID() ||p.ParentID_2 == (Integer)it.getID() ) {
                        countChildren ++;
                    }
                }
                it.setChildrens(countChildren);
                FinalList.add(it);
            }


            int MinNumChildren=0; int MaxNumChildrens=0;

            //Get max number
            for (Patients  p : AllPatientsList) {
                if (p.getChildrens() > MaxNumChildrens)
                    MaxNumChildrens = p.getChildrens();
                if (p.getChildrens() < MinNumChildren)
                    MinNumChildren = p.getChildrens();
            }


            System.out.println("min num of Children: " + MinNumChildren + "  Max numb of Children: " + MaxNumChildrens);
            for (int i = MinNumChildren; i <= MaxNumChildrens; i++)
            {

                    double TotalHPW = 0;
                    int sizeS =0;
                    for(Patients m : FilteredListPatients){
                        if(  m.getChildrens() == i  ){
                            TotalHPW += m.getHPW();
                            sizeS++;
                        }
                    }


                    double AVGr = TotalHPW / sizeS;

                     if((new Double(AVGr)).toString() != "NaN")
                         System.out.printf("The average working hours per week is %1$s for %2$s Children " + "\r\n", (new Double(AVGr)).toString(), i);
                     else
                        System.out.printf("There are no Patients with %1$s Children " + "\r\n", i);

            }


        }catch(SQLException ex){

            ex.printStackTrace();
        }
    }




    public static class PatientsAndChildrensCount
    {
        private int ChildrenID;
        public final int getChildrenID()
        {
            return ChildrenID;
        }
        public final void setChildrenID(int value)
        {
            ChildrenID = value;
        }
        private int ParentID_1;
        public final int getParentID_1()
        {
            return ParentID_1;
        }
        public final void setParentID_1(int value)
        {
            ParentID_1 = value;
        }
        private Integer ParentID_2 = null;
        public final Integer getParentID_2()
        {
            return ParentID_2;
        }
        public final void setParentID_2(Integer value)
        {
            ParentID_2 = value;
        }


    }
    public static class Patients
    {
        private int ID;
        public final int getID()
        {
            return ID;
        }
        public final void setID(int value)
        {
            ID = value;
        }
        private double HPW;
        public final double getHPW()
        {
            return HPW;
        }
        public final void setHPW(double value)
        {
            HPW = value;
        }

        private int Childrens;
        public final int getChildrens()
        {
            return Childrens;
        }
        public final void setChildrens(int value)
        {
            Childrens = value;
        }
    }
    public static class Patient_Task9
    {

        private int NumOfChildren;
        public final int getNumOfChildren()
        {
            return NumOfChildren;
        }
        public final void setNumOfChildren(int value)
        {
            NumOfChildren = value;
        }
        private double AvgPerWeek;
        public final double getAvgPerWeek()
        {
            return AvgPerWeek;
        }
        public final void setAvgPerWeek(double value)
        {
            AvgPerWeek = value;
        }
    }




}
