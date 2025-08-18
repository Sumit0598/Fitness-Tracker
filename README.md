# Fitness-Tracker

*COMPANY* : CODECH IT SOLUTIONS
*NAME* : SUMIT KUMAR
*INTERN ID* : CT06DZ105
*DOMAIN* : APP DEVELOPMENT
*DURATION* : 6 WEEKS
*MENTOR* : NEELA SANTOSH

## 
The Fitness Tracker Application is an Android app developed in Kotlin that helps users monitor and manage their physical activity in real time. Utilizing Android’s built-in sensors, the app tracks steps, distance walked, and calories burned. By providing real-time feedback and historical insights, the app encourages healthier lifestyle choices and helps users achieve their fitness goals.

Features :

- Real-Time Step Counting
-Tracks user steps using the Step Counter and Step Detector sensors.

-Provides accurate and responsive updates as the user moves throughout the day.

Distance and Calorie Calculation :

. Calculates the distance traveled based on step count and stride length.

. Estimates calories burned using user weight and activity data.

Daily Activity Dashboard :

. Displays total steps, distance, and calories burned.

. Real-time visualization helps users monitor progress throughout the day.

Historical Data and Trends :

- Stores daily step data in a local database (Room or SharedPreferences).

- Displays weekly or monthly summaries with line charts or bar charts for visual tracking of activity trends.

Data Reset and Backup :

- Users can reset daily step count manually.

- App supports automatic reset at midnight to track steps for the new day.

Lightweight and Efficient :

- Minimal battery consumption using foreground service for continuous step detection.

-Runs smoothly even in the background without affecting other apps.

Technical Highlights :

- Android Sensor Integration

- Uses SensorManager to access TYPE_STEP_COUNTER and TYPE_STEP_DETECTOR sensors.

- Ensures compatibility with devices running Android 6.0 (Marshmallow) and above.

Foreground Service for Real-Time Tracking :

Implements StepCounterService to keep counting steps even when the app is minimized.

Provides live updates to the UI via LiveData and ViewModel.


Data can be visualized in charts for weekly or monthly trends.

Data Visualization :

Integrates MPAndroidChart for displaying step trends over time.

Pie charts  make it easier to compare daily or weekly activity levels.

Modern Android Architecture :

Uses activities, fragments, RecyclerView, ViewModel, LiveData, and Material Design components for a smooth and intuitive user experience.

Kotlin coroutines handle asynchronous data updates efficiently.

Benefits :-

. Provides users with insights into daily physical activity.

. Encourages healthier lifestyle choices through real-time feedback and reminders.

. Tracks progress over time, allowing users to set and achieve fitness goals.

. Lightweight, efficient, and works seamlessly in the background without draining the battery.


# OUTPUT

