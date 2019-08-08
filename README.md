# student_records_revised

Revised version of student_records Android application. Users can do CRUD operations on student information (name, grade, picture, etc).  Android Jetpack architecture components were used in order to create an application more structurally coherent than the initial attempt (student_records).

# Jetpack components used:

-Android ViewModel was employed in order to separate the data and UI logic of an activity

-Room Persistence Library was used to retrieve and store student data in an organized manner

-LiveData in ViewModel was observed in order to trigger an update of an activity's UI once data was retrieved from the RoomDatabase

-Use of a repository class as a single source of truth for the application

# Other features:

-Glide image loading library to load photos into ImageViews

-ItemTouchHelper to allow the user to remove a recyclerview item by swiping

-DiffUtil is used instead of notifyDataSetChanged to detect changes to the student recyclerview in StudentListActivity, which are then dispatched to its adapter.

-Photos are taken and then written into internal storage using FileProvider
