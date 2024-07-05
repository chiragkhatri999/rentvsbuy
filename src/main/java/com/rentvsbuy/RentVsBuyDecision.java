package com.rentvsbuy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RentVsBuyDecision {
    public static void main(String[] args) {

//        giveComparison(ScenarioSupplier.flatWith23Loan());
        giveComparison(ScenarioSupplier.flatWith33Loan());
    }

    public static void giveComparison(Scenario scenario){

        // CONTROL FACTORS

        // property
        BigDecimal propertyValue = scenario.getPropertyValue();
        BigDecimal downPayment = scenario.getDownPayment();
        BigDecimal fixedSetupCost = scenario.getFixedSetupCost(); // this will have zero value after structureLifeInYears
        int structureLifeInYears = scenario.getStructureLifeInYears();
        // calculation for flat
        double landPercentOwned = scenario.getLandPercentOwned();
        double plotAreaInSqFt = scenario.getPlotAreaInSqFt();
        long plotRatePerSqFt = scenario.getPlotRatePerSqFt();

        // rent
        long rentPerMonth = scenario.getRentPerMonth();
        double rentMultiplierPerYear = scenario.getRentMultiplierPerYear();

        // loan
        double loanRateOfInterest = scenario.getLoanRateOfInterest();
        int loanDurationInYears = scenario.getLoanDurationInYears();
        double averageInflationPerYear = scenario.getAverageInflationPerYear();

        // equity
        double averageRateOfYearlyEquityReturns = scenario.getAverageRateOfYearlyEquityReturns();
        double averageRateOfYearlyLandRatesIncrease = scenario.getAverageRateOfYearlyLandRatesIncrease();
        /// CALCULATIONS START HERE

        double effectiveAverageRateOfYearlyEquityReturns = averageRateOfYearlyEquityReturns - averageInflationPerYear;
        double effectiveAverageReturnPerMonth = effectiveAverageRateOfYearlyEquityReturns/12;
        // unrecoverable cost for rent
        long rentPaidPerYear = 12 * rentPerMonth;
        BigDecimal totalRentPaid = BigDecimal.ZERO;
        double tempRentPerYear = rentPaidPerYear;
        double rentMultiplierPerYearAdjustedForInflation = rentMultiplierPerYear - averageInflationPerYear;
        for(int yearPassed=0; yearPassed < structureLifeInYears; yearPassed++){
            totalRentPaid = totalRentPaid.add(BigDecimal.valueOf(tempRentPerYear));
            tempRentPerYear *= rentMultiplierPerYearAdjustedForInflation;
        }
        System.out.println("Total rent paid: " + totalRentPaid);

        BigDecimal totalSIPReturnForRentDuration = stepUpSIPCalculator(totalRentPaid.divide(BigDecimal.valueOf(structureLifeInYears* 12L), RoundingMode.HALF_UP), effectiveAverageReturnPerMonth, structureLifeInYears*12);
        System.out.println("Total SIP return after rent duration: " + totalSIPReturnForRentDuration);



        // unrecoverable cost for owning

        // equity cost
        BigDecimal equityInvested = fixedSetupCost.add(downPayment);

        BigDecimal finalEquityValue = calculateCompoundedValue(equityInvested, effectiveAverageRateOfYearlyEquityReturns, structureLifeInYears, 1);
        System.out.println("Total equity value: " + finalEquityValue);

        // debt cost
        // SIP opportunity lost due to interest + principal paid per month
        BigDecimal loanPrincipal = propertyValue.subtract(downPayment);
        double effectiveLoanRateOfInterest = loanRateOfInterest - averageInflationPerYear;
        BigDecimal totalLoanPaid = calculateTotalLoanPaid(loanPrincipal, effectiveLoanRateOfInterest, loanDurationInYears);
        System.out.println("Total loan paid: " + totalLoanPaid);
        BigDecimal interestPaid = totalLoanPaid.subtract(loanPrincipal);
        System.out.println("Total interest paid: " + interestPaid);
        BigDecimal emiPaidPerMonth = totalLoanPaid.divide(BigDecimal.valueOf(loanDurationInYears* 12L), RoundingMode.HALF_UP);
        System.out.println("EMI per month: " + emiPaidPerMonth);

        BigDecimal totalSIPReturnForLoanDuration = stepUpSIPCalculator(emiPaidPerMonth, effectiveAverageReturnPerMonth, loanDurationInYears*12);
        System.out.println("Total SIP return after loan duration: " + totalSIPReturnForLoanDuration);
        BigDecimal totalSIPReturnForPropertyDuration = calculateCompoundedValue(totalSIPReturnForLoanDuration, effectiveAverageRateOfYearlyEquityReturns, structureLifeInYears-loanDurationInYears, 1);
        System.out.println("Total SIP return till property duration: " + totalSIPReturnForPropertyDuration);

        // value of owned piece of land
        double landOwnedInSqFt = plotAreaInSqFt * landPercentOwned;
        double initialLandValue =  landOwnedInSqFt * plotRatePerSqFt;
        System.out.println("Initial land value: " + initialLandValue);
        double effectiveAverageRateOfYearlyLandRatesIncrease = averageRateOfYearlyLandRatesIncrease - averageInflationPerYear;
        BigDecimal finalLandValue = calculateCompoundedValue(BigDecimal.valueOf(initialLandValue), effectiveAverageRateOfYearlyLandRatesIncrease, structureLifeInYears, 1);
        System.out.println("Final land value: " + finalLandValue);

        // maintenance cost for structure life (ignoring opportunity cost for this)


        // yearly tax


        BigDecimal totalCostOfOwning = totalSIPReturnForPropertyDuration.add(finalEquityValue).subtract(finalLandValue);

        BigDecimal totalCostOfRenting = totalRentPaid.add(totalSIPReturnForRentDuration);

        System.out.println("Total cost of owning: " + totalCostOfOwning);
        System.out.println("Total cost of renting: " + totalCostOfRenting);
    }

    public static BigDecimal calculateCompoundedValue(
            BigDecimal principalAmount,
            double interestRateForPeriod,
            int countOfPeriods,
            int numberOfTimesInterestAppliedInEachPeriod
    ) {
        double major = Math.pow((1 + interestRateForPeriod/numberOfTimesInterestAppliedInEachPeriod), countOfPeriods*numberOfTimesInterestAppliedInEachPeriod);
        return principalAmount.multiply(BigDecimal.valueOf(major));
    }

    public static BigDecimal calculateTotalLoanPaid(
            BigDecimal principalAmount,
            double interestRatePerPeriod,
            int countOfPeriods
    ) {
        return principalAmount.multiply(BigDecimal.valueOf(1+interestRatePerPeriod*countOfPeriods));
    }

    public static BigDecimal stepUpSIPCalculator(
            BigDecimal investmentPerPeriod,
            double returnPerPeriod,
            int countOfPeriods
    ) {
        BigDecimal totalReturn = BigDecimal.ZERO;
        for(int period = 0; period < countOfPeriods; period++){
            BigDecimal interestForPeriod = totalReturn.multiply(BigDecimal.valueOf(returnPerPeriod));
            totalReturn = totalReturn.add(investmentPerPeriod);
            totalReturn = totalReturn.add(interestForPeriod);
        }
        return totalReturn;
    }
}
