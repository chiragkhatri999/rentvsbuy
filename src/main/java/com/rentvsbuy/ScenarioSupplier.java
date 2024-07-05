package com.rentvsbuy;

import java.math.BigDecimal;

public class ScenarioSupplier {
    public static Scenario flatWith53Loan(){
        return Scenario.builder()
                .propertyValue(BigDecimal.valueOf(73_00_000))
                .downPayment(BigDecimal.valueOf(20_00_000))
                .fixedSetupCost(BigDecimal.valueOf(10_00_000L))
                .structureLifeInYears(40)
                .landPercentOwned((double) 1 /6)
                .plotAreaInSqFt(3300)
                .plotRatePerSqFt(5500)
                .rentPerMonth(17000L)
                .rentMultiplierPerYear(1.1)
                .loanRateOfInterest(0.09d)
                .loanDurationInYears(20)
                .averageInflationPerYear(0.055d)
                .averageRateOfYearlyEquityReturns(0.08d)
                .averageRateOfYearlyLandRatesIncrease(0.1d)
                .build();
    }

    public static Scenario flatWith33Loan(){
        return Scenario.builder()
                .propertyValue(BigDecimal.valueOf(73_00_000))
                .downPayment(BigDecimal.valueOf(40_00_000))
                .fixedSetupCost(BigDecimal.valueOf(10_00_000L))
                .structureLifeInYears(40)
                .landPercentOwned((double) 1 /6)
                .plotAreaInSqFt(3300)
                .plotRatePerSqFt(5500)
                .rentPerMonth(17000L)
                .rentMultiplierPerYear(1.05)
                .loanRateOfInterest(0.09d)
                .loanDurationInYears(10)
                .averageInflationPerYear(0.055d)
                .averageRateOfYearlyEquityReturns(0.08d)
                .averageRateOfYearlyLandRatesIncrease(0.072d)
                .build();
    }

    public static Scenario flatWith23Loan(){
        return Scenario.builder()
                .propertyValue(BigDecimal.valueOf(73_00_000))
                .downPayment(BigDecimal.valueOf(50_00_000))
                .fixedSetupCost(BigDecimal.valueOf(10_00_000L))
                .structureLifeInYears(40)
                .landPercentOwned((double) 1 /6)
                .plotAreaInSqFt(3300)
                .plotRatePerSqFt(5500)
                .rentPerMonth(17000L)
                .rentMultiplierPerYear(1.1)
                .loanRateOfInterest(0.09d)
                .loanDurationInYears(10)
                .averageInflationPerYear(0.055d)
                .averageRateOfYearlyEquityReturns(0.08d)
                .averageRateOfYearlyLandRatesIncrease(0.1d)
                .build();
    }
}
