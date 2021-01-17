package com.wikia.meownjik.pointstransformation;

public class PointsCalculator {
    private PointsCalculator() {}

    /**
     * Translates the mark to another evaluation system. Rounds to 2 decimals.
     * @param score - the points scored in the initial system
     * @param maxInitial - maximum points in the initial system
     * @param maxFinal - maximum points in the final system
     * @return - score in the terms of final system
     */
    public static float transform(float score, float maxInitial, float maxFinal) {
        float result = score / maxInitial * maxFinal;
        return Math.round(result * 100) / 100f;
    }

    /**
     * Translates the mark to the classic 5-point system (with + and -)
     * @param score - the points scored in the initial system
     * @param maxInitial - maximum points in the initial system
     * @return - score in the terms of final system
     */
    static public String toClassic(float score, float maxInitial)
    {
        score = transform(score, maxInitial, 5);
        String ans;
        if (score < 0.5) ans = "0";

        else if (0.5 <= score && score < 0.83) ans = "1-";    //0.66
        else if (0.83 <= score && score < 1.17) ans = "1";
        else if (1.17 <= score && score < 1.5) ans = "1+";    //1.34

        else if (1.5 <= score && score < 1.83) ans = "2-";    //1.66
        else if (1.83 <= score && score < 2.17) ans = "2";
        else if (2.17 <= score && score < 2.5) ans = "2+";    //2.34

        else if (2.5 <= score && score < 2.83) ans = "3-";    //2.66
        else if (2.83 <= score && score < 3.17) ans = "3";
        else if (3.17 <= score && score < 3.5) ans = "3+";    //3.34

        else if (3.5 <= score && score < 3.83) ans = "4-";    //3.66
        else if (3.83 <= score && score < 4.17) ans = "4";
        else if (4.17 <= score && score < 4.5) ans = "4+";    //4.34

        else if (4.5 <= score && score < 4.83) ans = "5-";    //4.66
        else if (4.83 <= score && score < 5.17) ans = "5";
        else ans = "5+";

        return ans;
    }

    /**
     * Translates the mark from the classic 5-point system (with + and -) to another one.
     * @param score - the points scored in the initial system
     * @param maxFinal - maximum points in the final system
     * @return - score in the terms of final system
     */
    static public double fromClassic(String score, float maxFinal)
    {
        double ans = 0;

        switch (score) {
            case "1-":
                ans = 0.75;
                break;
            case "1":
                ans = 1;
                break;
            case "1+":
                ans = 1.34;
                break;
            case "2-":
                ans = 1.66;
                break;
            case "2":
                ans = 2;
                break;
            case "2+":
                ans = 2.34;
                break;
            case "3-":
                ans = 2.66;
                break;
            case "3":
                ans = 3;
                break;
            case "3+":
                ans = 3.34;
                break;
            case "4-":
                ans = 3.66;
                break;
            case "4":
                ans = 4;
                break;
            case "4+":
                ans = 4.34;
                break;
            case "5-":
                ans = 4.66;
                break;
            case "5":
                ans = 5;
                break;
            case "5+":
                ans = 5.25;
                break;
            case "5++":
                ans = 5.5;
                break;
        }

        return transform((float) ans, 5, maxFinal);
    }

    /**
     * Translates the mark to the ECTS
     * @param score - the points scored in the initial system
     * @param maxInitial - maximum points in the initial system
     * @return - score in the terms of final system
     */
    static public String toEcts(float score, float maxInitial)
    {//Перетворює оцінку у 100-бальній системі в оцінку
        //за європейською шкалою (A, B, ..., E)
        String ans;
        score = transform(score, maxInitial, 100);
        if (score < 35) ans = "F"; //Complete fail
        else if (35 <= score && score < 60) ans = "Fx";   //Fail with the possibility to retry

        else if (60 <= score && score < 67) ans = "E";    //Acceptable minimum
        else if (67 <= score && score < 75) ans = "D";    //Enough

        else if (75 <= score && score < 82) ans = "C";    //Good
        else if (82 <= score && score < 90) ans = "B";    //Very good

        else if (90 <= score && score < 101) ans = "A";   //Excellent!
        else ans = "Aa";

        return ans;
    }

    /**
     * Translates the mark from ECTS to another evaluating system.
     * @param score - the points scored in the initial system
     * @param maxFinal - maximum points in the final system
     * @return - score in the terms of final system
     */
    static public double fromEcts(String score, float maxFinal)
    {//Переводить оцінку з європейської системи ECTS
        //у 100-бальну систему
        float ans = 0;
        score = score.toUpperCase();
        switch (score) {
            case "F":
                ans = 25;
                break;
            case "FX":
                ans = 40;
                break;
            case "E":
                ans = 60;
                break;
            case "D":
                ans = 70;
                break;
            case "C":
                ans = 79;
                break;
            case "B":
                ans = 89;
                break;
            case "A":
                ans = 100;
                break;
            case "AA":
                ans = 105;
                break;
        }

        return transform(ans, 100, maxFinal);
    }
}
