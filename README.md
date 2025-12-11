# ECG Watchdog

An offline Android app for tracking daily electricity meter readings and calculating costs using Ghana PURC tariffs (Effective 1 July 2025).

## Features

- **Daily Meter Reading Entry**: Input your daily electricity meter readings
- **Automatic Cost Calculation**: Uses Ghana PURC slab-based tariff system
- **Usage Trends**: Track your daily electricity consumption patterns
- **Monthly Projections**: Estimate monthly costs based on current usage
- **Spike Detection**: Get alerts when usage is unusually high (>150% of 7-day average)
- **Offline Operation**: Works completely offline with local Room database

## Tariff Structure

### Residential Tariffs (GH¢/kWh):
- **Lifeline (0–30 kWh)**: 0.795308
- **Standard (0–300 kWh)**: 1.801867  
- **High Usage (301+ kWh)**: 2.380873

### Service Charges:
- **Lifeline households**: GH¢ 2.13/month
- **Other residential consumers**: GH¢ 10.73/month

## Architecture

- **100% Offline**: No internet connection required
- **Kotlin + Jetpack Compose**: Modern Android UI
- **MVVM Architecture**: Clean separation of concerns
- **Room Database**: Local data persistence
- **Android 8+ Support**: Compatible with API level 26+

## Building

The project includes GitHub Actions for automatic APK building. Push to main/master branch to trigger a build.

### Manual Build
```bash
./gradlew assembleDebug
```

## Screens

1. **Enter Reading**: Input daily meter readings with validation
2. **Daily Summary**: View usage comparison and cost breakdown
3. **History**: Browse past readings with simple bar chart

## Usage

1. Enter your daily meter reading in kWh
2. View calculated daily usage and estimated cost
3. Monitor trends and receive spike alerts
4. Track history and monthly projections