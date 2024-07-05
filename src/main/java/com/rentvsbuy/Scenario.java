package com.rentvsbuy;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;


@Builder
@Getter
public class Scenario {
    // property
    private BigDecimal propertyValue;
    private BigDecimal downPayment;
    private BigDecimal fixedSetupCost; // this will have zero value after structureLifeInYears
    private int structureLifeInYears;
    // calculation for flat
    private double landPercentOwned;
    private double plotAreaInSqFt;
    private long plotRatePerSqFt;

    // rent
    private long rentPerMonth;
    private double rentMultiplierPerYear;

    // loan
    private double loanRateOfInterest;
    private int loanDurationInYears;
    private double averageInflationPerYear;

    // equity
    private double averageRateOfYearlyEquityReturns;
    private double averageRateOfYearlyLandRatesIncrease;
}
