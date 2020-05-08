# Protocol Companion
Protocol Companion is a mobile companion app for Android to be used with Android Wear devices. 
It allows users to create and modify research study parameters efficiently and send the information 
to Wear devices for use in health research.

# File breakdown

## Java files
### MainActivity

### Study

### StudyViewModel

### NetworkFragment and DownloadCallback

### HomeFragment and MyStudyRecyclerViewAdapter

### CreateStudyFragment

### StudyDetailFragment

### LoadStudyFragment

## XML files


### fragment_createstudy

### fragment_home and fragment_home_list

### fragment_loadstudy

### fragment_studydetail

### activity_main

### app_bar_main

### nav_header_main

### content_main

### activity_main_drawer

### context_menu
When a user long presses on a study in the study list, this context menu is activated. Currently allows
the user to edit, delete, or share a study, as well as providing the framework to allow the user to 
push the study to a watch.

### mobile_navigation
Navigation graph of how each page can be reached from other pages. Connections between pages are called
Actions and can have parameters that will be passed from the source page to the destination page.
The home page (HomeFragment currently) is specified here as well.

### strings
Contains all the hardcoded strings in the app, so all strings can be referenced by string ID instead
of the hardcoded value. When a value in this file is changed, it's automatically changed everywhere
else. Reduces coupling and errors.


## Local storage file
### protocols.json
Currently, studies are persistently stored locally in protocols.json in JSON format with the ID of each
study as the key and a nested JSON representation of the study as the value.

# General flow of the application

## Functionality: Display study list
The home/startup page of the application is the list of all studes 

## Functionality: Create study

## Functionality: View study details / Edit study

## Functionality: Delete study

## Functionality: Share study

## Functionality: Load study from QR code



# Links to JSON testing files with study/studies pre-loaded.
These can be used to test downloading a file from a QR code by generating a QR code that represents
the following links. The application will follow the link from the QR code, download the JSON, and 
import the study/studies it contains into its study list.

###test1study.json
https://www.dropbox.com/s/ibr8x0831ze7xxm/test1study.json?dl=1

###test2studies.json
https://www.dropbox.com/s/k7apsscrnoavgk3/test2studies.json?dl=1


