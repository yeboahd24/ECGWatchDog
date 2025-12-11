package com.ecgwatchdog

data class Tariff(
    val lifelineRate: Double = 0.795308,
    val res0To300Rate: Double = 1.801867,
    val res301PlusRate: Double = 2.380873,
    val lifelineServiceCharge: Double = 2.13,
    val regularServiceCharge: Double = 10.73,
    val lifelineThreshold: Double = 30.0,
    val midTierThreshold: Double = 300.0
)