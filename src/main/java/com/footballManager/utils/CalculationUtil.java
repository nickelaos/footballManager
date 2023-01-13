package com.footballManager.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class CalculationUtil {
    public static BigDecimal getCostOfTransfer(Float commissionForTransfer, Date dateOfBirth, Date startOfCareer) {
        LocalDate birthDate = dateOfBirth.toLocalDate();
        LocalDate careerStart = startOfCareer.toLocalDate();
        LocalDate current = LocalDate.now();


        BigDecimal age = BigDecimal.valueOf(Period.between(birthDate, current).getYears());
        BigDecimal experience = BigDecimal.valueOf(Period.between(careerStart, current).getMonths()+
                (Period.between(careerStart,current).getYears()*12));

        return experience.multiply(BigDecimal.valueOf(100000)).
                divide(age, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(1+commissionForTransfer));
    }
}
