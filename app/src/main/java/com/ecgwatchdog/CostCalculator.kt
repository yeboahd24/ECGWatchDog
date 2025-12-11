package com.ecgwatchdog

import kotlin.math.round

data class CostBreakdown(
    val lifeline: Double,
    val mid: Double,
    val high: Double,
    val serviceCharge: Double,
    val total: Double
)

object CostCalculator {
    private val tariff = Tariff()
    
    fun calculateDailyCost(kwh: Double): CostBreakdown {
        val dailyServiceCharge = if (kwh <= tariff.lifelineThreshold) {
            tariff.lifelineServiceCharge / 30
        } else {
            tariff.regularServiceCharge / 30
        }
        
        return when {
            kwh <= tariff.lifelineThreshold -> {
                val lifeline = kwh * tariff.lifelineRate
                CostBreakdown(
                    lifeline = round(lifeline * 100) / 100,
                    mid = 0.0,
                    high = 0.0,
                    serviceCharge = round(dailyServiceCharge * 100) / 100,
                    total = round((lifeline + dailyServiceCharge) * 100) / 100
                )
            }
            kwh <= tariff.midTierThreshold -> {
                val lifeline = tariff.lifelineThreshold * tariff.lifelineRate
                val mid = (kwh - tariff.lifelineThreshold) * tariff.res0To300Rate
                val total = lifeline + mid + dailyServiceCharge
                CostBreakdown(
                    lifeline = round(lifeline * 100) / 100,
                    mid = round(mid * 100) / 100,
                    high = 0.0,
                    serviceCharge = round(dailyServiceCharge * 100) / 100,
                    total = round(total * 100) / 100
                )
            }
            else -> {
                val lifeline = tariff.lifelineThreshold * tariff.lifelineRate
                val mid = (tariff.midTierThreshold - tariff.lifelineThreshold) * tariff.res0To300Rate
                val high = (kwh - tariff.midTierThreshold) * tariff.res301PlusRate
                val total = lifeline + mid + high + dailyServiceCharge
                CostBreakdown(
                    lifeline = round(lifeline * 100) / 100,
                    mid = round(mid * 100) / 100,
                    high = round(high * 100) / 100,
                    serviceCharge = round(dailyServiceCharge * 100) / 100,
                    total = round(total * 100) / 100
                )
            }
        }
    }
    
    fun formatCurrency(amount: Double): String {
        return "GH¢ %.2f".format(amount)
    }
    
    fun getUsageCategory(kwh: Double): String {
        return when {
            kwh <= tariff.lifelineThreshold -> "Lifeline"
            kwh <= tariff.midTierThreshold -> "Standard"
            else -> "High Usage"
        }
    }
}